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

public class InitializeThreadGPU extends Thread {
    @Override
    public void run() {
        List<GPU> gpus = new ArrayList<>();

        // GPU
        try {
            URL csvUrl = new URL("https://testpage-pink.vercel.app/new/gpus.csv");
            BufferedReader cpuReader = new BufferedReader(new InputStreamReader(csvUrl.openStream()));
            String l;
            List<String> allLines = new ArrayList<>();

            // allLines をスレッドで分割して処理する
            while ((l = cpuReader.readLine()) != null) {
                allLines.add(l);
            }
            allLines.parallelStream().forEach(line -> {
                String[] values = line.split(",");

                // 値段を取得
                String url = values[7];
                int price = getPrice(url);

                // スペックを取得
                SpecGPU spec = null;
                try {
                    spec = getSpecGPU(url + "spec/#tab", values[0], 3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(spec);

                GPU gpu = new GPU(
                        values[0],
                        values[1],
                        values[3],
                        values[4],
                        values[5],
                        values[6],
                        url,
                        price,
                        spec
                );
                gpus.add(gpu);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(gpus);
        Collections.reverse(gpus);

        Public.gpus = gpus;
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

    private SpecGPU getSpecGPU(String url, String maker, int fanCount) throws Exception {
        String chip = "";
        int memoryGB = 0;
        int cuda = -1;
        int memoryBus = 0;
        int memoryClock = 0;
        String busInterface = "";
        String resolution = "";
        DisplayPin displayPin = null;
        boolean airCooler = false;
        boolean waterCooler = false;
        boolean fanLess = false;
        boolean semiFanLess = false;
        String directX = "";
        String openGL = "";

        Document document = Jsoup.connect(url).get();
        //スペック表のtrすべてを取得
        Elements trElements = document.select(
                "div#all div#contents930 div#pc div#main div#tabContents div#specContents div#mainLeft table.tblBorderGray.mTop15 tbody tr"
        );
        int index = 0;
        for (Element tr : trElements) {
            if (index > 0) {
                switch (index) {
                    case 1 -> {
                        Element td = tr.select("td").get(0);
                        chip = td.text();
                        td = tr.select("td").get(1);
                        Pattern p = Pattern.compile("[0-9]+GB");
                        Matcher m = p.matcher(td.text());
                        if (m.find()) {
                            memoryGB = Integer.parseInt(m.group().replaceAll("GB", ""));
                        }
                    }

                    case 2 -> {
                        Element td = tr.select("td").get(0);
                        cuda = !td.text().trim().isEmpty() ? Integer.parseInt(td.text().trim()) : -1;
                    }

                    case 3 -> {
                        Element td = tr.select("td").get(0);
                        memoryBus = Integer.parseInt(td.text().replaceAll("bit", ""));
                        td = tr.select("td").get(1);
                        String memClockText = td.text().replaceAll("Gbps", "").replaceAll("　", "").replaceAll(" ", "");
                        if (!memClockText.isEmpty()) {
                            memoryClock = Integer.parseInt(memClockText);
                        }

                    }

                    case 4 -> {
                        Element td = tr.select("td").get(0);
                        busInterface = td.text();
                        td = tr.select("td").get(1);
                        resolution = td.text();
                    }

                    case 5 -> {
                        Element td = tr.select("td").get(0);
                        Pattern pattern = Pattern.compile("(x[0-9]+)");
                        Matcher m = pattern.matcher(td.text());
                        List<Integer> portCounts = new ArrayList<>();
                        while (m.find()) {
                            portCounts.add(Integer.parseInt(m.group().replaceAll("x", "")));
                        }
                        int[] c = new int[3];
                        for (int i = 0; i < 2; i++) {
                            c[i] = portCounts.get(i);
                        }
                        displayPin = new DisplayPin(c[0], c[1], c[2]);

                        td = tr.select("td").get(1);
                        airCooler = td.text().contains("空冷");
                        waterCooler = td.text().contains("水冷");
                    }

                    case 7 -> {
                        Element td = tr.select("td").get(0);
                        fanLess = !td.text().isBlank();
                        td = tr.select("td").get(1);
                        semiFanLess = !td.text().isBlank();
                    }
                }
            }
            index++;
        }

        return new SpecGPU(
                fanCount,
                chip,
                memoryGB,
                cuda,
                memoryBus,
                memoryClock,
                busInterface,
                resolution,
                displayPin,
                airCooler,
                waterCooler,
                fanLess,
                semiFanLess,
                directX,
                openGL
        );
    }
}

