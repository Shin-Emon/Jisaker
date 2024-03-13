package app.jisaker.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitializeThreadCPU extends Thread {
    @Override
    public void run() {
        List<CPU> cpus = new ArrayList<>();

        // CPU
        try {
            URL csvUrl = new URL("https://testpage-pink.vercel.app/new/cpus.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(csvUrl.openStream()));
            String l;
            List<String> allLines = new ArrayList<>();

            // allLines をスレッドで分割して処理する
            while ((l = br.readLine()) != null) {
                allLines.add(l);
            }
            allLines.parallelStream().forEach(line -> {
                String[] values = line.split(",");

                // 値段を取得
                String url = values[5];
                int price = getPrice(url);
//                System.out.println(values[1] + "世代 " + values[0] + " " + values[2] + " " + values[3] + "-" + values[4] + " : " + price + " YEN");

                // スペックを取得
                SpecCPU spec = null;
                try {
                    spec = getSpecCPU(url + "spec/#tab", values[0]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                CPU cpu = new CPU(
                        values[0],
                        Integer.parseInt(values[1]),
                        values[2],
                        values[3],
                        values[4],
                        url,
                        price,
                        spec
                );

                cpus.add(cpu);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(cpus);

        Public.cpus = cpus;
    }

    private int getPrice(String url) {
        int price = -1;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("body div#all div#contents1035 div#pc div#main div.contents930 div#itmArea div#itmBoxMax div.itmBoxBottom div.itmBoxIn div#productAll div#productInfoBox div.priceBoxWrap div.priceWrap div.subInfoObj1 p");
            for (Element element : elements) {
                String priceText = element.text();
                priceText = priceText.replaceAll("¥", "").replaceAll(",", "").trim();
                price = Integer.parseInt(priceText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return price;
    }

    private SpecCPU getSpecCPU(String url, String maker) throws Exception {
        String coreText = "";
        int pCores = 0;
        int eCores = 0;
        int core = 0;
        int threads = 0;
        String socket = "";
        double clock = 0;
        double maxClock = 0;
        double l2cache = 0;
        double l3cache = 0;
        boolean isGraphics = false;
        Document document = Jsoup.connect(url).get();
        //スペック表のtrすべてを取得
        Elements trElements = document.select(
                "div#all div#contents930 div#pc div#main div#tabContents div#specContents div#mainLeft table.tblBorderGray.mTop15 tbody tr"
        );
        int index = 0;
        for (Element tr : trElements) {
            if (index > 0) {
                switch (index) {
                    case 2: {
                        Element td = tr.select("td").get(0);
                        socket = td.text().trim();
                        td = tr.select("td").get(1);
                        coreText = td.text().replaceAll("コア", "").trim();
                        String[] pSplit = coreText.split("\\+");
                        core = Integer.parseInt(pSplit[0].split(" ")[0]);
                        if (maker.equals("Intel")) {
                            pCores = Integer.parseInt(pSplit[0].split(" ")[1].replaceAll("：", "").replaceAll("P", ""));
                            eCores = Integer.parseInt(pSplit[1].replaceAll("：", "").replaceAll("E", ""));
                        }

                        break;
                    }
                    case 3: {
                        Element td = tr.select("td").get(1);
                        clock = Float.parseFloat(td.text().replaceAll("GHz", "").trim());
                        break;
                    }
                    case 4: {
                        Element td = tr.select("td").get(0);
                        maxClock = Double.parseDouble(td.text().replaceAll("GHz", "").trim());
                        td = tr.select("td").get(1);
                        threads = Integer.parseInt(td.text().trim());
                        break;
                    }
                    case 5: {
                        Element td = tr.select("td").get(1);
                        l3cache = Double.parseDouble(td.text().replaceAll("MB", "").trim());
                        break;
                    }
                    case 6: {
                        Element td = tr.select("td").get(0);
                        l2cache = Double.parseDouble(td.text().replaceAll("MB", "").trim());
                        td = tr.select("td").get(1);
                        if (td.text().trim().equals("　")) {
                            isGraphics = false;
                        } else {
                            isGraphics = true;
                        }
                        break;
                    }
                }
            }
            index++;
        }
        return new SpecCPU(
                coreText,
                core,
                pCores,
                eCores,
                threads,
                socket,
                clock,
                maxClock,
                l2cache,
                l3cache,
                isGraphics
        );
    }
}

