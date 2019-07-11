package gui;

import com.sun.tools.javac.Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutWindow extends JFrame {
    private JPanel mainPanel = new JPanel();
    private JLabel theme = new JLabel("Dijkstra-Jarnik-Prim algorithm");
    private JTextArea description = new JTextArea("The program implements step-by-step visualization of the " +
            "Dijkstra-Jarnik-Prim algorithm for finding the minimum spanning tree in a graph. The input to the program " +
            "is an undirected graph, which is defined graphically, or in text form (a list of edges and their weights). " +
            "After the completion of the algorithm, the resulting spanning tree is displayed on the main window, it is " +
            "also possible to save the result in a text file. During the execution of the algorithm, the user can see " +
            "the previous and current state of the program.");
    private JButton descriptionButton = new JButton("Algorithm description");
    private JTextArea students = new JTextArea("Authors:\n" +
            "Vlasov Roman\n"+
            "Sychevsky Radimir\n"+
            "Iolshina Valeria");
    private JButton close = new JButton("OK");

    private MainWindow parent;


    AboutWindow(MainWindow parent) {
        super("Pathetic Attempt");
        setVisible(true);
        this.parent = parent;
        parent.setEnabled(false);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.setEnabled(true);
                dispose();
            }
        });
        setBounds(150, 150, 900, 450);

        Color c = new Color(234, 255, 226);
        mainPanel.setBackground(c);
        mainPanel.setLayout(null);
        add(mainPanel);

        description.setEditable(false);
        students.setEditable(false);

        mainPanel.add(theme);
        mainPanel.add(description);
        mainPanel.add(descriptionButton);
        mainPanel.add(students);
        mainPanel.add(close);

        theme.setBounds             (280,30,500,50);
        description.setBounds       (30, 90, 840, 200);
        descriptionButton.setBounds (510, 310, 360, 30);
        students.setBounds          (30, 300, 250, 100);
        close.setBounds             (510, 350, 360, 30);

        Font f = new Font("Monospaced", Font.PLAIN, 18);
        theme.setFont(f);
        description.setFont(f);
        descriptionButton.setFont(f);
        students.setFont(f);
        close.setFont(f);

        description.setBackground(c);
        students.setBackground(c);

        description.setLineWrap(true);
        description.setWrapStyleWord(true);

        close.addActionListener(new TestActionListener());
        descriptionButton.addActionListener(new TestActionListener());
    }

    class TestActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == close) {
                parent.setEnabled(true);
                dispose();
            }
            if(e.getSource() == descriptionButton) {
                String description = "<html>The algorithm may informally be described as performing the following steps:<br>" +
                        "<br>" +
                        "Initialize a tree with a single vertex, chosen arbitrarily from the graph.<br>" +
                        "Grow the tree by one edge: of the edges that connect the tree to vertices not <br>" +
                        "yet in the tree, find the minimum-weight edge, and transfer it to the tree.<br>" +
                        "Repeat step 2 (until all vertices are in the tree).</html>";
                JLabel message = new JLabel(description);

                message.setFont(new Font("Monospaced", Font.PLAIN, 18));
                JOptionPane.showMessageDialog(new JFrame(), message, "Algorithm description", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}
