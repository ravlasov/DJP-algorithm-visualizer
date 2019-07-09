package gui;

import algorithm.Graph;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphText_Input extends JFrame {
    private final MainWindow parent;
    private JPanel mainPanel            = new JPanel();
    private JTextArea text              = new JTextArea();
    private JScrollPane scroll;
    private JButton applyInputGraph     = new JButton("Apply");
    private JButton cancelInputGraph    = new JButton("Cancel");
    private ActionListener recipient;

    public GraphText_Input(MainWindow mainWindow, ActionListener recipient, String str) {
        super("Graph Input");
        this.recipient = recipient;
        parent = mainWindow;
        parent.setEnabled(false);
        text.setText(str);
        setBounds(250, 250, 800, 600);
        Dimension d = new Dimension();
        d.setSize(760, 300);
        setMinimumSize(d);
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

        scroll = new JScrollPane(text);

        mainPanel.add(scroll);
        mainPanel.add(applyInputGraph);
        mainPanel.add(cancelInputGraph);

        scroll.setBounds            (30,30, 730, 430);
        cancelInputGraph.setBounds  (30, 480, 360, 70);
        applyInputGraph.setBounds   (400, 480, 360, 70);

        Font f = new Font("Monospaced", Font.PLAIN, 18);
        applyInputGraph.setFont(f);
        cancelInputGraph.setFont(f);
        text.setFont(f);

        Color c = new Color(255, 250, 221); //#FFFADD
        text.setBackground(c);


        cancelInputGraph.addActionListener(new eventHandler());
        applyInputGraph.addActionListener(new eventHandler());
        mainPanel.addComponentListener(new eventHandler());

    }

    class eventHandler implements ActionListener, ComponentListener{
        @Override
        public void actionPerformed (ActionEvent actionEvent){
            if(actionEvent.getSource() == cancelInputGraph) {
                parent.setEnabled(true);
                dispose();
            }
            if(actionEvent.getSource() == applyInputGraph)
            {
                int id = (int)System.currentTimeMillis();

                String str = text.getText();
                if (str.length() == 0)
                {
                    JLabel msg = new JLabel("The graph must contain at least one edge");
                    msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(GraphText_Input.this, msg,
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                str += "\n";
                str = str.replaceAll("\n\n", "\n");
                str = str.replaceAll("  ", " ");
                str = str.replaceAll("\n ", "\n");
                if (str.startsWith(" ")) {
                    JLabel msg = new JLabel("Text starts with invalid symbol");
                    msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(GraphText_Input.this, msg,
                                                    "Invalid input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!Graph.isValid(str)) {
                    JLabel msg = new JLabel("The graph must be connected");
                    msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(GraphText_Input.this, msg,
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                    return;
                }


                ActionEvent message = new ActionEvent(GraphText_Input.this, id, str);
                recipient.actionPerformed(message);
                parent.setEnabled(true);
                dispose();
            }
        }

        @Override
        public void componentResized(ComponentEvent componentEvent) {
            if (componentEvent.getComponent() == mainPanel)
            {
                int w = mainPanel.getWidth();
                int width = w;
                int h = mainPanel.getHeight();
                int height = h;
                w /= 60;
                width -= 2 * w;
                h = h / 60;
                height -= 3 * h + 70;
                scroll.setBounds            (w,h, width, height);
                cancelInputGraph.setBounds  (w, mainPanel.getHeight() - 70 -  h, 360, 70);
                applyInputGraph.setBounds   (mainPanel.getWidth() - 360 - w, mainPanel.getHeight() - 70 -  h, 360, 70);
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
    }
}
