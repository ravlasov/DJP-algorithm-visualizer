package gui;

import java.awt.*;
import javax.swing.*;

public class GraphGUI_Input extends JFrame {
    private final MainWindow parent;
    private JPanel mainPanel            = new JPanel();
    private JPanel inputGraphPanel      = new JPanel();
    private JButton applyInputGraph     = new JButton("Apply");
    private JButton cancelInputGraph    = new JButton("Cancel");
    private JLabel inputMode            = new JLabel("Edit mode:");
    private JRadioButton modeVertex     = new JRadioButton("Vertex editing");
    private JRadioButton modeEdge       = new JRadioButton("Edge editing");

    public GraphGUI_Input(MainWindow mainWindow){
        super("Graph Input");
        parent = mainWindow;
        setBounds(150, 150, 1210, 910);
        setVisible(true);

        Color mainColor = new Color(234, 255, 226); //#FFFADD

        mainPanel.setBackground(mainColor); //#F6FFF8
        mainPanel.setLayout(null);
        add(mainPanel);

        mainPanel.add(inputGraphPanel);
        mainPanel.add(applyInputGraph);
        mainPanel.add(cancelInputGraph);
        mainPanel.add(inputMode);
        mainPanel.add(modeVertex);
        mainPanel.add(modeEdge);

        inputGraphPanel.setBounds   (30,30, 1140, 740);
        inputMode.setBounds         (30, 790, 120, 70);
        cancelInputGraph.setBounds  (480, 810, 300, 50);
        applyInputGraph.setBounds   (810, 790, 360, 70);
        modeVertex.setBounds        (140, 790, 130, 45);
        modeEdge.setBounds          (140, 820, 130, 45);

        ButtonGroup group = new ButtonGroup();
        group.add(modeVertex);
        group.add(modeEdge);
        modeVertex.setSelected(true);

        Color c = new Color(255, 250, 221); //#FFFADD
        inputGraphPanel.setBackground(c);
        modeVertex.setBackground(mainColor);
        modeEdge.setBackground(mainColor);

        applyInputGraph.setEnabled(false);

    }
}