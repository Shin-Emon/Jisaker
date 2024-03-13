package app.jisaker.core;

import app.jisaker.Main;

public class InitController extends Thread {
    @Override
    public void run() {
        Main.mp.loadStart();
        System.out.println("ロード中...");
        try {
            // initialize
            var init = new Initializer();
            init.start();
            init.join();

            Main.mp.loadOK();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ロード完了");
    }
}