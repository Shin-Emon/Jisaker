package app.jisaker.graphics;

import app.jisaker.Main;
import app.jisaker.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class MainPanel extends JPanel {
    private final JComboBox<CPU> cpus = new JComboBox<>();
    private final JTabbedPane tabPane = new JTabbedPane();
    private final ArrayList<Project> projects = new ArrayList<>();
    private final ArrayList<JPanel> projectPanels = new ArrayList<>();
    private final JLabel status = new JLabel("");
    private int index = 0;
    private ContentPanel mainPanel;
    private JPanel underPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private boolean firstLoaded = false;
    public MainPanel() {
        setLayout(new BorderLayout());

        status.setFont(new Font("メイリオ", Font.ITALIC, 15));
        underPane.add(status);

        this.add(tabPane, BorderLayout.CENTER);
        this.add(underPane, BorderLayout.SOUTH);
    }

    public void newProject(Project proj) {
        index++;
        this.tabPane.add(new ContentPanel(proj));
        this.tabPane.setTitleAt(index, proj.getName());
    }

    public void loadStart() {
        setStatus("ロード中...");
    }

    public void loadOK() {
        setStatus("ロード完了");
        timer.start();

        if (!firstLoaded) {
            mainPanel = new ContentPanel("untitled");
            tabPane.add(mainPanel);
            tabPane.setTitleAt(index, "untitled");
            mainPanel.load();

            firstLoaded = true;
        }
    }
    private int timerCount = 0;
    private final Timer timer = new Timer(1000, this::loadOKTimer);
    private void loadOKTimer(ActionEvent e) {
        if (timerCount == 5) {
            setStatus("");
            timer.stop();
        }
        timerCount++;
    }
    private void setStatus(String status) {
        this.status.setText(status);
    }
}
