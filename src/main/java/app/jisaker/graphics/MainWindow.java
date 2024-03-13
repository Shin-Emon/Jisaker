package app.jisaker.graphics;

import app.jisaker.Main;
import app.jisaker.core.CPUFactory;
import app.jisaker.core.GPUFactory;
import app.jisaker.core.InitController;
import app.jisaker.core.Project;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("自作er");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");

        JMenuItem _new = new JMenuItem("New");
        JMenuItem reload = new JMenuItem("Reload");
        JMenuItem exit = new JMenuItem("Exit");
        _new.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(null, "新規プロジェクト名を入力してください", "自作er", JOptionPane.QUESTION_MESSAGE);
            try {
                Main.mp.newProject(new Project(name, CPUFactory.getOne(), GPUFactory.getOne()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        reload.addActionListener(e -> new InitController().start());
        exit.addActionListener(e -> System.exit(0));
        file.add(_new);
        file.add(reload);
        file.add(exit);

        menuBar.add(file);

        setJMenuBar(menuBar);
    }
}
