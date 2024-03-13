package app.jisaker.core;

public record SpecCPU(
        String coreText,
        int core,
        int pCores,
        int eCores,
        int threads,
        String socket,
        double clock,
        double maxClock,
        double l2cache,
        double l3cache,
        boolean isGraphics
) {}
