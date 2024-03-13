package app.jisaker.core;

import java.util.regex.Pattern;

public class GPU implements Comparable<GPU> {
    public String maker;
    public String vendor;
    public String aliasName;
    public String series;
    public String miniSeries;
    public String name;
    public String url;
    public int price;
    public SpecGPU spec;

    public GPU(String maker, String vendor, String aliasName, String series, String miniSeries, String name, String url, int price, SpecGPU spec) {
        this.maker = maker;
        this.vendor = vendor;
        this.aliasName = aliasName;
        this.series = series;
        this.miniSeries = miniSeries;
        this.name = name;
        this.url = url;
        this.price = price;
        this.spec = spec;
    }

    @Override
    public String toString() {
        return "[" + this.vendor + "] " + this.maker + " " + this.series + " " + this.miniSeries + " " + this.name;
    }

    @Override
    public int compareTo(GPU o) {
        if (o == null) {
            return 0;
        }

        if (!this.maker.equals(o.maker)) {
            return this.maker.compareTo(o.maker);
        }

        if (!this.series.equals(o.series)) {
            return this.series.compareTo(o.series);
        }

        if (!this.name.equals(o.name)) {
            if (this.series.equals("GeForce")) {
                System.out.println(this.name + " (o: " + o.name + ")");
                var p = Pattern.compile("[0-9]*");
                var m = p.matcher(this.name);
                var m2 = p.matcher(o.name);

                if (m.find() && m2.find()) {
                    int meNum = Integer.parseInt(m.group());
                    int oNum = Integer.parseInt(m2.group());

                    if (this.miniSeries.equals("RTX")) {
                        meNum += 100000;
                    }

                    if (this.miniSeries.equals("GTX")) {
                        meNum += 50000;
                    }

                    if (o.miniSeries.equals("RTX")) {
                        oNum += 100000;
                    }

                    if (o.miniSeries.equals("GTX")) {
                        oNum += 50000;
                    }

                    System.out.println("Me: " + meNum);
                    System.out.println("O: " + oNum);

                    if (meNum != oNum) {
                        return oNum - meNum;
                    }

                    if (this.name.replaceAll(m.group(), "").equalsIgnoreCase("super")) {
                        meNum += 1;
                    } else if (this.name.replaceAll(m.group(), "").equalsIgnoreCase("ti")) {
                        meNum += 2;
                    }

                    if (o.name.replaceAll(m.group(), "").equalsIgnoreCase("super")) {
                        oNum += 1;
                    } else if (o.name.replaceAll(m.group(), "").equalsIgnoreCase("ti")) {
                        oNum += 2;
                    }

                    System.out.println("========== NEW ==========");
                    System.out.println("Me: " + meNum);
                    System.out.println("O: " + oNum);

                    return oNum - meNum;
                }
            }
        }

        if (!this.vendor.equals(o.vendor)) {
            return this.vendor.compareTo(o.vendor);
        }

        return 0;
    }
}
