package gui;

import algorithm.DJPAlgorithm;
import algorithm.Graph;
import com.mxgraph.swing.mxGraphComponent;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
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

    private JLabel lableInputGraph          = new JLabel("Input Graph", SwingConstants.CENTER);
    private JLabel lableOutputGraph         = new JLabel("Output Graph", SwingConstants.CENTER);
    private Graph graph = null;


    public MainWindow() {
        super("DJP Algorithm Visualizer");
        setBounds(150, 150, 1210, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Color c = new Color(234, 255, 226); //#F6FFF8
        mainPanel.setBackground(c);
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
    }


    class eventHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == runAlgorithm) {
                if (graph == null)
                    return;
                DJPAlgorithm algorithm = new DJPAlgorithm();
                graph = Graph.getGraphFromString(graph.toString());
                graph.print();
                algorithm.init(graph);
                StateWindow state = new StateWindow(MainWindow.this, this, algorithm);
                outputGraphPanel.removeAll();
                outputGraphPanel.add(lableOutputGraph);
            }
            if(actionEvent.getSource() == getGraphFromGUI){
                GraphGUI_Input graphGUI_input = new GraphGUI_Input(MainWindow.this);
            }
            if(actionEvent.getSource() == getGraphFromFile)
            {
                String str = "";
                FileDialog fd = new FileDialog(MainWindow.this, "Choose a file", FileDialog.LOAD);
                fd.setDirectory(System.getProperty("user.home"));
                fd.setFilenameFilter((file, s) -> s.endsWith(".txt"));
                fd.setVisible(true);

                String filename = fd.getDirectory();
                if (filename == null)
                    return;
                filename += fd.getFile();
                try {
                    FileReader fr = new FileReader(filename);
                    Scanner scan = new Scanner(fr);
                    while (scan.hasNextLine()) {
                        str += scan.nextLine();
                        str += "\n";
                    }
                    fr.close();
                } catch (Exception e) {
                    JLabel errMsg = new JLabel("An error has occured");
                    errMsg.setFont(new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(null, errMsg, "ERROR", JOptionPane.WARNING_MESSAGE);
                }
                /*if (!Graph.isValid(str)) {
                    JLabel msg = new JLabel("The graph must be connected");
                    msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(MainWindow.this, msg,
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                    return;
                }*/
                graph = Graph.getGraphFromString(str);
                inputGraphPanel.removeAll();
                inputGraphPanel.add(lableInputGraph);
                inputGraphPanel.setLayout(null);
                mxGraphComponent grComp = graph.createGraphComponent();
                grComp.setBounds(0, 20 , 555, 610);
                inputGraphPanel.add(grComp);
                inputGraphPanel.updateUI();
            }
            if(actionEvent.getSource() == getGraphFromKeyboard)
            {
                GraphText_Input GUIGraph = new GraphText_Input(MainWindow.this, this);
            }
            if(actionEvent.getSource() == saveGraphToFile)
            {
                if (graph == null || graph.answerToString().length() == 0)
                {
                    JLabel noGraph = new JLabel("Nothing to export");
                    noGraph.setFont(new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(null, noGraph, "No graph found", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                FileDialog fd = new FileDialog(MainWindow.this, "Choose a file", FileDialog.LOAD);
                fd.setDirectory(System.getProperty("user.home"));
                fd.setFilenameFilter((file, s) -> s.endsWith(".txt"));
                fd.setVisible(true);
                String filename = fd.getDirectory();
                if (filename == null)
                    return;
                String newGraph = "";
                filename += fd.getFile();
                try {
                    FileWriter newFile = new FileWriter(filename);
                    newGraph += graph.answerToString();
                    newFile.write(newGraph);

                    newFile.close();
                }
                catch (Exception e){
                    JLabel errMsg = new JLabel("An error has occured");
                    errMsg.setFont(new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(null, errMsg, "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
            if(actionEvent.getSource() instanceof GraphText_Input){
                String str = actionEvent.getActionCommand();
                graph = Graph.getGraphFromString(str);
                inputGraphPanel.removeAll();
                inputGraphPanel.add(lableInputGraph);
                inputGraphPanel.setLayout(null);
                mxGraphComponent grComp = graph.createGraphComponent();
                grComp.setBounds(0, 30 , 555, 600);
                inputGraphPanel.add(grComp);
                inputGraphPanel.updateUI();

            }
            if(actionEvent.getSource() instanceof Graph){
                outputGraphPanel.removeAll();
                outputGraphPanel.add(lableOutputGraph);
                outputGraphPanel.setLayout(null);
                Graph tmp = (Graph)actionEvent.getSource();
                mxGraphComponent grComp = tmp.createGraphComponent();
                grComp.setBounds(0, 30 , 555, 600);
                outputGraphPanel.add(grComp);
                outputGraphPanel.updateUI();
            }
        }
    }

}
