package gui;

import algorithm.DJPAlgorithm;
import algorithm.Graph;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends JFrame {
    private JPanel mainPanel                = new JPanel();
    private JButton getGraphFromFile        = new JButton("Import graph from a file");
    private JButton getGraphFromKeyboard    = new JButton("Create a graph from text");
    private JButton getGraphFromGUI         = new JButton("Create a graph manually");
    private JButton saveGraphToFile         = new JButton("Export graph to a file");
    private JButton runAlgorithm            = new JButton("Start DJP algorithm");
    private JPanel inputGraphPanel          = new JPanel();
    private JPanel outputGraphPanel         = new JPanel();

    private JLabel lableInputGraph          = new JLabel("Input Graph");
    private JLabel lableOutputGraph          = new JLabel("Output Graph");



    public MainWindow() {
        super("DJP Algorithm Visualizer");
        setBounds(150, 150, 1210, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        mainPanel.setBackground(new Color(234, 255, 226)); //#F6FFF8
        mainPanel.setLayout(null);
        add(mainPanel);

        mainPanel.add(getGraphFromFile);
        mainPanel.add(getGraphFromKeyboard);
        mainPanel.add(getGraphFromGUI);
        mainPanel.add(saveGraphToFile);
        mainPanel.add(runAlgorithm);
        mainPanel.add(inputGraphPanel);
        mainPanel.add(outputGraphPanel);

        getGraphFromFile.setBounds      (30, 30, 360, 70);
        getGraphFromKeyboard.setBounds  (420, 30, 360, 70);
        getGraphFromGUI.setBounds       (810, 30, 360, 70);
        saveGraphToFile.setBounds       (870, 130,300, 50);
        runAlgorithm.setBounds          (420, 130, 360, 70);
        inputGraphPanel.setBounds       (30, 210, 555, 630);
        outputGraphPanel.setBounds      (615 ,210 ,555, 630);

        inputGraphPanel.add(lableInputGraph);
        lableInputGraph.setBounds(0, 0, 555, 20);

        outputGraphPanel.add(lableOutputGraph);
        lableOutputGraph.setBounds(0,0, 555, 20);

        Font f = new Font("Monospaced", Font.PLAIN, 18);
        getGraphFromFile.setFont(f);
        getGraphFromKeyboard.setFont(f);
        getGraphFromGUI.setFont(f);
        saveGraphToFile.setFont(f);
        runAlgorithm.setFont(f);
        lableInputGraph.setFont(f);
        lableOutputGraph.setFont(f);






        Color c = new Color(255, 250, 221); //#FFFADD
        inputGraphPanel.setBackground(c);
        outputGraphPanel.setBackground(c);

        runAlgorithm.addActionListener(new eventHandler());
        getGraphFromGUI.addActionListener(new eventHandler());
        getGraphFromKeyboard.addActionListener(new eventHandler());
        getGraphFromFile.addActionListener(new eventHandler());
        saveGraphToFile.addActionListener(new eventHandler());
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        Graph g = new Graph();
        g.addEdge(1, 2, 10);
        g.addEdge(2, 3, 20);
        g.addEdge(3, 4, 50);
        g.addEdge(1, 3, 30);
        g.addEdge(1, 4, 15);
        g.print();
        DJPAlgorithm a = new DJPAlgorithm();
        a.init(g);
        while (a.isFinished() == false)
        {
            System.out.println(a.getComment());
            a.makeStep();
        }

        g.printA();

    }


    class eventHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == runAlgorithm) {
                StateWindow state = new StateWindow(MainWindow.this);
            }
            if(actionEvent.getSource() == getGraphFromGUI){
                GraphGUI_Input graphGUI_input = new GraphGUI_Input(MainWindow.this);
            }
            if(actionEvent.getSource() == getGraphFromFile)
            {
                FileDialog fd = new FileDialog(MainWindow.this, "Choose a file", FileDialog.LOAD);
                fd.setDirectory(System.getProperty("user.home"));
                fd.setFilenameFilter((file, s) -> s.endsWith(".txt"));
                fd.setVisible(true);
                String filename = fd.getFile();
            }
            if(actionEvent.getSource() == getGraphFromKeyboard)
            {
                String str = null;
                GraphText_Input GUIGraph = new GraphText_Input(MainWindow.this, str);
            }
            if(actionEvent.getSource() == saveGraphToFile)
            {
                FileDialog fd = new FileDialog(MainWindow.this, "Choose a file", FileDialog.LOAD);
                fd.setDirectory(System.getProperty("user.home"));
                fd.setFilenameFilter((file, s) -> s.endsWith(".txt"));
                fd.setVisible(true);
                String filename = fd.getFile();
            }
        }
    }

}
