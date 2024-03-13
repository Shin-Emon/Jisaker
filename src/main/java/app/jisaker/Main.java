package app.jisaker;

import app.jisaker.core.InitController;
import app.jisaker.core.ProjectManager;
import app.jisaker.graphics.MainPanel;
import app.jisaker.graphics.MainWindow;

public class Main {
    public static MainPanel mp = new MainPanel();
    public static ProjectManager pm = new ProjectManager();
    public static void main(String[] args) {
        InitController c = new InitController();
        try {
            c.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainWindow mw = new MainWindow();
        mw.add(mp);
        mw.setVisible(true);
    }
}
