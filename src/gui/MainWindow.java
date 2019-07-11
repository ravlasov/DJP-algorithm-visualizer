package gui;

import algorithm.DJPAlgorithm;
import algorithm.Graph;
import com.mxgraph.swing.mxGraphComponent;
import com.sun.tools.javac.Main;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import javax.swing.*;

public class MainWindow extends JFrame {
    private JPanel mainPanel                = new JPanel();
    private JButton getGraphFromFile        = new JButton("Import graph from a file");
    private JButton getGraphFromKeyboard    = new JButton("Edit a graph from text");
    private JButton getGraphFromGUI         = new JButton("Edit a graph manually");
    private JButton saveOutputGraphToFile   = new JButton("Export output graph to a file");
    private JButton saveInputGraphToFile    = new JButton("Export input graph to a file");
    private JButton runAlgorithm            = new JButton("Start DJP algorithm");
    private JPanel inputGraphPanel          = new JPanel();
    private JPanel outputGraphPanel         = new JPanel();

    private JLabel labelInputGraph          = new JLabel("Input Graph", SwingConstants.CENTER);
    private JLabel labelOutputGraph         = new JLabel("Output Graph", SwingConstants.CENTER);
    private Graph graph = null;

    private mxGraphComponent inpGraphComp;
    private mxGraphComponent outpGraphComp;


    public MainWindow() {
        super("DJP Algorithm Visualizer");
        setBounds(150, 150, 1210, 900);
        Dimension d = new Dimension();
        d.setSize(1080, 700);
        setMinimumSize(d);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Color c = new Color(234, 255, 226); //#F6FFF8
        mainPanel.setBackground(c);
        mainPanel.setLayout(null);
        add(mainPanel);

        mainPanel.add(getGraphFromFile);
        mainPanel.add(getGraphFromKeyboard);
        mainPanel.add(getGraphFromGUI);
        mainPanel.add(saveOutputGraphToFile);
        mainPanel.add(saveInputGraphToFile);
        mainPanel.add(runAlgorithm);
        mainPanel.add(inputGraphPanel);
        mainPanel.add(outputGraphPanel);

        getGraphFromFile.setBounds      (30, 30, 360, 70);
        getGraphFromKeyboard.setBounds  (420, 30, 360, 70);
        getGraphFromGUI.setBounds       (810, 30, 360, 70);
        saveOutputGraphToFile.setBounds (810, 130,360, 50);
        saveInputGraphToFile.setBounds  (30, 130, 360, 50);
        runAlgorithm.setBounds          (420, 130, 360, 70);
        inputGraphPanel.setBounds       (30, 210, 555, 630);
        outputGraphPanel.setBounds      (615 ,210 ,555, 630);

        inputGraphPanel.add(labelInputGraph);
        labelInputGraph.setBounds(0, 0, 555, 20);

        outputGraphPanel.add(labelOutputGraph);
        labelOutputGraph.setBounds(0,0, 555, 20);

        Font f = new Font("Monospaced", Font.PLAIN, 18);
        getGraphFromFile.setFont(f);
        getGraphFromKeyboard.setFont(f);
        getGraphFromGUI.setFont(f);
        saveOutputGraphToFile.setFont(f);
        saveInputGraphToFile.setFont(f);
        runAlgorithm.setFont(f);
        labelInputGraph.setFont(f);
        labelOutputGraph.setFont(f);

        inputGraphPanel.setBackground(c);
        outputGraphPanel.setBackground(c);

        eventHandler eH = new eventHandler();
        runAlgorithm.addActionListener(eH);
        getGraphFromGUI.addActionListener(eH);
        getGraphFromKeyboard.addActionListener(eH);
        getGraphFromFile.addActionListener(eH);
        saveOutputGraphToFile.addActionListener(eH);
        saveInputGraphToFile.addActionListener(eH);
        mainPanel.addComponentListener(eH);
        setFocusable(true);
        addKeyListener(eH);
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }


    class eventHandler implements ActionListener, ComponentListener, KeyListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == runAlgorithm) {
                if (graph == null)
                    return;
                DJPAlgorithm algorithm = new DJPAlgorithm();
                //Graph bu = graph.backupGraph();
                //graph = Graph.getGraphFromString(graph.toString());
                //graph.restoreMXGraph(bu);
                graph.resetColors();
                String inp = JOptionPane.showInputDialog(MainWindow.this,
                        "Input start vertex: ", "");
                if (inp == null) {
                    algorithm.init(graph);
                }
                else
                {
                    algorithm.init(graph, inp);
                }
                StateWindow state = new StateWindow(MainWindow.this, this, algorithm);
                outputGraphPanel.removeAll();
                outputGraphPanel.add(labelOutputGraph);
            }
            if(actionEvent.getSource() == getGraphFromGUI){
                GraphGUI_Input graphGUI_input = new GraphGUI_Input(MainWindow.this, this, graph);
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
                if (!Graph.isValid(str)) {
                    JLabel msg = new JLabel("The graph must be connected");
                    msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(MainWindow.this, msg,
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                graph = Graph.getGraphFromString(str);
                inputGraphPanel.removeAll();
                inputGraphPanel.add(labelInputGraph);
                inputGraphPanel.setLayout(null);
                graph.createGraphComponent();
                mxGraphComponent grComp = graph.updateGraphComponent();
                grComp.setEnabled(false);
                grComp.setBounds(0, 30 , inputGraphPanel.getWidth(), inputGraphPanel.getHeight());
                inputGraphPanel.add(grComp);
                inputGraphPanel.updateUI();
            }
            if(actionEvent.getSource() == getGraphFromKeyboard)
            {
                GraphText_Input GUIGraph = new GraphText_Input(MainWindow.this, this,
                                                                graph != null ? graph.toString() : "");
            }
            if(actionEvent.getSource() == saveOutputGraphToFile)
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
            if (actionEvent.getSource() == saveInputGraphToFile)
            {
                if (graph == null || graph.toString().length() == 0)
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
                    newGraph += graph.toString();
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
                inputGraphPanel.add(labelInputGraph);
                inputGraphPanel.setLayout(null);
                graph.createGraphComponent();
                mxGraphComponent grComp = graph.updateGraphComponent();
                grComp.setBounds(0, 30,  inputGraphPanel.getWidth(), inputGraphPanel.getHeight());
                grComp.setEnabled(false);
                inputGraphPanel.add(grComp);
                inputGraphPanel.updateUI();
                inpGraphComp = graph.updateGraphComponent();

            }
            if(actionEvent.getSource() instanceof Graph){
                if (actionEvent.getActionCommand().equals("Finished")) {
                    outputGraphPanel.removeAll();
                    outputGraphPanel.add(labelOutputGraph);
                    outputGraphPanel.setLayout(null);
                    Graph tmp = (Graph) actionEvent.getSource();
                    mxGraphComponent grComp = tmp.updateGraphComponent();
                    grComp.setBounds(0, 30, outputGraphPanel.getWidth(), outputGraphPanel.getHeight() - 30);
                    outputGraphPanel.add(grComp);
                    outputGraphPanel.updateUI();
                    outpGraphComp = graph.updateGraphComponent();

                }
                if (actionEvent.getActionCommand().equals("Edited") || actionEvent.getActionCommand().equals("Not changed"))
                {
                    if (actionEvent.getSource() == null || actionEvent.getSource().toString().length() == 0)
                        return;
                    inputGraphPanel.removeAll();
                    inputGraphPanel.add(labelInputGraph);
                    inputGraphPanel.setLayout(null);
                    graph = (Graph) actionEvent.getSource();
                    mxGraphComponent grComp = graph.updateGraphComponent();
                    grComp.setEnabled(false);
                    grComp.setBounds(0, 30, inputGraphPanel.getWidth(), inputGraphPanel.getHeight() - 30);
                    inputGraphPanel.add(grComp);
                    inputGraphPanel.updateUI();
                    inpGraphComp = graph.updateGraphComponent();
                }
            }
        }

        @Override
        public void componentResized(ComponentEvent componentEvent) {
            if (componentEvent.getComponent() == mainPanel)
            {
                int w = mainPanel.getWidth();
                int h = mainPanel.getHeight();
                w = (w - 360 * 3) / 4;
                h = h / 60;
                getGraphFromFile.setBounds      (w, h, 360, 70);
                getGraphFromKeyboard.setBounds  (360 + 2 * w, h, 360, 70);
                getGraphFromGUI.setBounds       (720 + 3 * w, h, 360, 70);
                saveOutputGraphToFile.setBounds (720 + 3 * w, 70 + 2 * h , 360, 50);
                saveInputGraphToFile.setBounds  (w, 70 + 2 * h , 360, 50);
                runAlgorithm.setBounds          (360 + 2 * w, 70 + 2 * h , 360, 70);
                int width = mainPanel.getWidth();
                width -= 60;
                width /= 2;
                int height = mainPanel.getHeight();
                height = height - 140 - 4 * h;
                inputGraphPanel.setBounds       (20         , 140 + 3 * h, width, height);
                outputGraphPanel.setBounds      (width + 40, 140 + 3 * h, width, height);

                for (int i = 0; i < inputGraphPanel.getComponentCount(); i++)
                {
                    if (inputGraphPanel.getComponent(i) instanceof mxGraphComponent)
                        inputGraphPanel.getComponent(i).setBounds(0, 30, width, height - 30);
                }
                for (int i = 0; i < outputGraphPanel.getComponentCount(); i++)
                {
                    if (outputGraphPanel.getComponent(i) instanceof mxGraphComponent)
                        outputGraphPanel.getComponent(i).setBounds(0, 30, width, height - 30);
                }
                if (inpGraphComp != null) {
                    inputGraphPanel.removeAll();
                    inputGraphPanel.add(labelInputGraph);
                    inpGraphComp.setBounds(0, 30, inputGraphPanel.getWidth(), inputGraphPanel.getHeight() - 30);
                    inputGraphPanel.add(inpGraphComp);
                    inputGraphPanel.updateUI();
                }
                if (outpGraphComp != null)
                {
                    outputGraphPanel.removeAll();
                    outputGraphPanel.add(labelOutputGraph);
                    outpGraphComp.setBounds(0, 30, outputGraphPanel.getWidth(), outputGraphPanel.getHeight() - 30);
                    outputGraphPanel.add(outpGraphComp);
                    outputGraphPanel.updateUI();
                }

            }
        }

        @Override
        public void componentMoved(ComponentEvent componentEvent) {

        }

        @Override
        public void componentShown(ComponentEvent componentEvent) {

        }

        @Override
        public void componentHidden(ComponentEvent componentEvent) {

        }

        @Override
        public void keyTyped(KeyEvent keyEvent) {
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int keyCode = keyEvent.getExtendedKeyCode();
            if(keyCode == KeyEvent.VK_F1)
            {
                AboutWindow aboutWindow = new AboutWindow(MainWindow.this);
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

}
