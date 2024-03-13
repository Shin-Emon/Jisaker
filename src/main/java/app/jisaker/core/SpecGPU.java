package app.jisaker.core;

public record SpecGPU(
        int fanCount,
        String chip,
        int memoryGB,
        int cuda,
        int memoryBus,
        int memoryClock,
        String busInterface,
        String resolution,
        DisplayPin displayPin,
        boolean airCooler,
        boolean waterCooler,
        boolean fanLess,
        boolean semiFanLess,
        String directX,
        String openGL
) {
    @Override
    public String toString() {
        return chip + "\n Memory: " + memoryGB + "GB \n Bus: " + memoryBus + "bit \n CUDA Cores Count: " + cuda + "\n Bus Interface: " + busInterface + "\n Fan Less: " + fanLess + "\n Semi Fan Less: " + semiFanLess;
    }
}