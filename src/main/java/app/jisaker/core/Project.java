package app.jisaker.core;

public class Project {
    private String name;
    private CPU cpu;
    private GPU gpu;

    public Project() {}

    public Project(String name, CPU cpu, GPU gpu) {
        this.name = name;
        this.cpu = cpu;
        this.gpu = gpu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public GPU getGpu() {
        return gpu;
    }

    public void setGpu(GPU gpu) {
        this.gpu = gpu;
    }
}
