package app.jisaker.graphics;

import app.jisaker.Main;
import app.jisaker.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContentPanel extends JPanel {
    private final JComboBox<CPU> cpus = new JComboBox<>();
    private final JComboBox<GPU> gpus = new JComboBox<>();
    private final JPanel cpuPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel gpuPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel sumPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JLabel cpuPrice = new JLabel("0円");
    private final JLabel gpuPrice = new JLabel("0円");
    private JLabel sumPrice = new JLabel("合計0円");
    private Project proj = new Project();
    public void load() {
        if (Public.cpus != null && Public.gpus != null) {
            Public.cpus.parallelStream().forEachOrdered(this.cpus::addItem);
            Public.gpus.parallelStream().forEachOrdered(this.gpus::addItem);
        }
        Public.gpus.forEach(System.out::println);
    }
    public ContentPanel(Project proj) {
        load();
        this.proj = proj;
        init();
    }
    public ContentPanel(String name) {
        load();
        proj.setName(name);
        Main.pm.projects.add(proj);

        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // title
        var title = new JLabel("自作er");
        title.setFont(new Font("メイリオ", Font.PLAIN, 30));

        // CPU
        JLabel cpuLabel = new JLabel("CPU ");
        cpuLabel.setFont(new Font("メイリオ", Font.PLAIN, 20));
        cpuPane.add(cpuLabel);
        cpuPane.add(cpus);
        cpuPane.add(cpuPrice);
        cpuPrice.setFont(new Font("メイリオ", Font.PLAIN, 20));
        cpus.addActionListener(this::cpuSelectionChanged);

        // GPU
        JLabel gpuLabel = new JLabel("GPU ");
        gpuLabel.setFont(new Font("メイリオ", Font.PLAIN, 20));
        gpuPane.add(gpuLabel);
        gpuPane.add(gpus);
        gpuPane.add(gpuPrice);
        gpuPrice.setFont(new Font("メイリオ", Font.PLAIN, 20));
        gpus.addActionListener(this::gpuSelectionChanged);

        // sum label
        sumPrice.setFont(new Font("メイリオ", Font.PLAIN, 25));
        sumPrice.setForeground(Color.RED);
        sumPane.add(sumPrice);

        this.add(title);
        this.add(cpuPane);
        this.add(gpuPane);
        this.add(sumPane);
    }
    private int cpuP, gpuP;
    private void cpuSelectionChanged(ActionEvent e) {
        var cpu = cpus.getSelectedItem();
        cpuP = ((CPU) Objects.requireNonNull(cpu)).price;
        cpuPrice.setText(cpuP == -1 ? "価格情報なし" : cpuP + "円");
        sum();
    }

    private void gpuSelectionChanged(ActionEvent e) {
        gpuP = ((GPU) Objects.requireNonNull(gpus.getSelectedItem())).price;
        gpuPrice.setText(gpuP == -1 ? "価格情報なし" : gpuP + "円");
        sum();
    }

    private void sum() {
        this.sumPrice.setText("合計" + (cpuP + gpuP) + "円");
    }
}
