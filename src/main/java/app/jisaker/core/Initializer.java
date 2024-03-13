package app.jisaker.core;

public class Initializer extends Thread {
    @Override
    public void run() {
        var cpuInit = new InitializeThreadCPU();
        var gpuInit = new InitializeThreadGPU();

        cpuInit.start();
        gpuInit.start();

        try {
            cpuInit.join();
            gpuInit.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
