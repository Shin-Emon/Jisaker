package app.jisaker.core;

public class CPU implements Comparable<CPU> {
    public String maker;
    public String series;
    public int gen;
    public String level;
    public String underName;
    public String fullName;
    public String url;
    public SpecCPU spec;
    public int price;
    public CPU(String maker, int gen, String series, String level, String underName, String url, int price, SpecCPU spec) {
        this.maker = maker;
        this.gen = gen;
        this.series = series;
        this.level = level;
        this.underName = underName;
        this.url = url;
        this.price = price;
        this.spec = spec;

        this.fullName = maker + " " + series + " " + level + "-" + underName;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public int compareTo(CPU o) {
        // Step 1: Intel / AMD
        if (!o.maker.equals(this.maker)) {
            if (this.maker.equals("Intel")) {
                // me: Intel, o: AMD
                return -1;
            }

            return 1;
        }

        if (!(this.gen - o.gen == 0)) {
            return this.gen - o.gen;
        }

        if (!this.level.equals(o.level)) {
            return this.level.compareTo(o.level);
        }

        if (!this.underName.equals(o.underName)) {
            return this.underName.compareTo(o.underName);
        }

        return 0;
    }
}
