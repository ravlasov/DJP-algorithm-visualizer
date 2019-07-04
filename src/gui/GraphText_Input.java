package gui;

import java.awt.*;
import javax.swing.*;

public class GraphText_Input extends JFrame {
    private final MainWindow parent;
    private JPanel mainPanel            = new JPanel();
    private JTextArea text              = new JTextArea();
    private JButton applyInputGraph     = new JButton("Apply");
    private JButton cancelInputGraph    = new JButton("Cancel");

    public GraphText_Input(MainWindow mainWindow) {
        super("Graph Input");
        parent = mainWindow;
        setBounds(250, 250, 800, 600);
        setVisible(true);

        Color mainColor = new Color(234, 255, 226); //#FFFADD

        mainPanel.setBackground(mainColor); //#F6FFF8
        mainPanel.setLayout(null);
        add(mainPanel);

        mainPanel.add(text);
        mainPanel.add(applyInputGraph);
        mainPanel.add(cancelInputGraph);

        text.setBounds   (30,30, 730, 430);
        cancelInputGraph.setBounds  (30, 480, 360, 70);
        applyInputGraph.setBounds   (400, 480, 360, 70);

        Color c = new Color(255, 250, 221); //#FFFADD
        text.setBackground(c);

        applyInputGraph.setEnabled(false);

    }

}
