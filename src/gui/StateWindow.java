package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StateWindow  extends JFrame {
    private final MainWindow parent;
    private JPanel mainPanel            = new JPanel();
    private JButton nextStep            = new JButton("Next step ->");
    private JButton prevStep            = new JButton("<- Previous step");
    private JButton interruptAlgorithm  = new JButton("Go to main menu");
    private JButton startPauseTimer     = new JButton("Pause");
    private JLabel timeCounter          = new JLabel("Timer");
    private JPanel prevGraphPanel       = new JPanel();
    private JPanel nextGraphPanel       = new JPanel();
    private JTextArea log               = new JTextArea();

    public StateWindow(MainWindow  mainWindow) {
        super("State");
        parent = mainWindow;
        parent.setVisible(false);
        setBounds(150, 150, 1210, 900);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });

        setVisible(true);

        mainPanel.setBackground(new Color(234, 255, 226)); //#F6FFF8
        mainPanel.setLayout(null);
        add(mainPanel);

        mainPanel.add(prevGraphPanel);
        mainPanel.add(nextGraphPanel);
        mainPanel.add(log);
        mainPanel.add(interruptAlgorithm);
        mainPanel.add(prevStep);
        mainPanel.add(nextStep);
        mainPanel.add(startPauseTimer);
        mainPanel.add(timeCounter);

        prevGraphPanel.setBounds    (30, 30, 560, 540);
        nextGraphPanel.setBounds    (620, 30, 560, 540);
        log.setBounds               (30, 585, 1150, 165);
        interruptAlgorithm.setBounds(30, 780,265, 70);
        prevStep.setBounds          (325, 780, 265, 70);
        nextStep.setBounds          (620, 780, 265, 70);
        startPauseTimer.setBounds   (910, 780, 120, 70);
        timeCounter.setBounds       (1040, 780, 140, 70);

        Color c = new Color(255, 250, 221); //#FFFADD
        prevGraphPanel.setBackground(c);
        nextGraphPanel.setBackground(c);
        log.setBackground(c);

        interruptAlgorithm.addActionListener(new eventHandler());

        log.setEditable(false);

        nextStep.setEnabled(false);
        prevStep.setEnabled(false);
        startPauseTimer.setEnabled(false);

        log.setText("Test text");
        timeCounter.setText("appears soon");
    }

    class eventHandler implements ActionListener{
        @Override
        public void actionPerformed (ActionEvent actionEvent){
            if(actionEvent.getSource() == interruptAlgorithm) {
                parent.setVisible(true);
                dispose();
            }
        }
    }
}
