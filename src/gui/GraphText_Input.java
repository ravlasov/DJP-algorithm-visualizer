package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphText_Input extends JFrame {
    private final MainWindow parent;
    private String textString;
    private JPanel mainPanel            = new JPanel();
    private JTextArea text              = new JTextArea();
    private JButton applyInputGraph     = new JButton("Apply");
    private JButton cancelInputGraph    = new JButton("Cancel");

    public GraphText_Input(MainWindow mainWindow, String str) {
        super("Graph Input");
        parent = mainWindow;
        parent.setEnabled(false);
        setBounds(250, 250, 800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
                        {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                parent.setEnabled(true);
                                dispose();
                            }
                        });
        setVisible(true);

        Color mainColor = new Color(234, 255, 226); //#FFFADD

        mainPanel.setBackground(mainColor); //#F6FFF8
        mainPanel.setLayout(null);
        add(mainPanel);

        JScrollPane scroll = new JScrollPane(text);

        mainPanel.add(scroll);
        mainPanel.add(applyInputGraph);
        mainPanel.add(cancelInputGraph);

        scroll.setBounds   (30,30, 730, 430);
        cancelInputGraph.setBounds  (30, 480, 360, 70);
        applyInputGraph.setBounds   (400, 480, 360, 70);

        Font f = new Font("Monospaced", Font.PLAIN, 18);
        applyInputGraph.setFont(f);
        cancelInputGraph.setFont(f);
        text.setFont(f);

        Color c = new Color(255, 250, 221); //#FFFADD
        text.setBackground(c);


        cancelInputGraph.addActionListener(new eventHandler());

        applyInputGraph.setEnabled(false);

    }

    class eventHandler implements ActionListener{
        @Override
        public void actionPerformed (ActionEvent actionEvent){
            if(actionEvent.getSource() == cancelInputGraph) {
                parent.setEnabled(true);
                dispose();
            }
        }
    }
}
