package gui;

import algorithm.DJPAlgorithm;
import algorithm.Graph;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

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

    private JLabel labelBeforeState     = new JLabel("Before State Graph", SwingConstants.CENTER);
    private JLabel labelCurrentState    = new JLabel("Current State Graph", SwingConstants.CENTER);
    private DJPAlgorithm algorithm;
    private ActionListener recipient;

    public StateWindow(MainWindow  mainWindow, ActionListener recipient, DJPAlgorithm algorithm) {
        super("State");
        this.algorithm = algorithm;
        this.recipient = recipient;
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

        Color c = new Color(234, 255, 226); //#F6FFF8
        mainPanel.setBackground(c);
        mainPanel.setLayout(null);
        add(mainPanel);

        JScrollPane scroll = new JScrollPane(log);

        mainPanel.add(prevGraphPanel);
        mainPanel.add(nextGraphPanel);
        mainPanel.add(scroll);
        mainPanel.add(interruptAlgorithm);
        mainPanel.add(prevStep);
        mainPanel.add(nextStep);
        mainPanel.add(startPauseTimer);
        mainPanel.add(timeCounter);

        prevGraphPanel.setBounds    (30, 30, 560, 540);
        nextGraphPanel.setBounds    (620, 30, 560, 540);
        scroll.setBounds            (30, 585, 1150, 165);
        interruptAlgorithm.setBounds(30, 780,265, 70);
        prevStep.setBounds          (325, 780, 265, 70);
        nextStep.setBounds          (620, 780, 265, 70);
        startPauseTimer.setBounds   (910, 780, 120, 70);
        timeCounter.setBounds       (1040, 780, 140, 70);

        prevGraphPanel.add(labelBeforeState);
        labelBeforeState.setBounds(0, 0, 560, 20);
        prevGraphPanel.setLayout(null);

        nextGraphPanel.add(labelCurrentState);
        labelCurrentState.setBounds(0,0,560,20);
        nextGraphPanel.setLayout(null);
        //mxGraphComponent grComp = algorithm.getCurrent().createGraphComponent();
        mxGraphComponent grComp = algorithm.getCurrent().updateGraphComponent();

        grComp.setBounds(0, 30, 560, 510);
        nextGraphPanel.add(grComp);
        nextGraphPanel.updateUI();

        Font f = new Font("Monospaced", Font.PLAIN, 18);
        interruptAlgorithm.setFont(f);
        prevStep.setFont(f);
        nextStep.setFont(f);
        startPauseTimer.setFont(f);
        timeCounter.setFont(f);
        labelCurrentState.setFont(f);
        labelBeforeState.setFont(f);
        log.setFont(f);

        prevGraphPanel.setBackground(c);
        nextGraphPanel.setBackground(c);
        log.setBackground(new Color(255, 250, 221)); //#FFFADD

        interruptAlgorithm.addActionListener(new eventHandler());
        nextStep.addActionListener(new eventHandler());

        log.setEditable(false);

        prevStep.setEnabled(false);
        startPauseTimer.setEnabled(false);

        timeCounter.setText("appears soon");
        log.setText(algorithm.getComment());
    }

    class eventHandler implements ActionListener{
        @Override
        public void actionPerformed (ActionEvent actionEvent){
            if(actionEvent.getSource() == interruptAlgorithm) {
                parent.setVisible(true);
                dispose();
            }
            if(actionEvent.getSource() == nextStep){
                if(!algorithm.isFinished()){
                    prevGraphPanel.removeAll();
                    prevGraphPanel.add(labelBeforeState);
                    //mxGraphComponent grComp = algorithm.getCurrent().createGraphComponent();
                    mxGraphComponent grComp = algorithm.getCurrent().updateGraphComponent();
                    grComp.setBounds(0, 30, 560, 510);
                    prevGraphPanel.add(grComp);                    prevGraphPanel.updateUI();
                    algorithm.makeStep();
                    log.setText(log.getText() + algorithm.getComment());
                    nextGraphPanel.removeAll();
                    nextGraphPanel.add(labelCurrentState);
                    //grComp = algorithm.getCurrent().createGraphComponent();
                    grComp = algorithm.getCurrent().updateGraphComponent();
                    grComp.setBounds(0, 30, 560, 510);
                    nextGraphPanel.add(grComp);
                    nextGraphPanel.updateUI();
                }
                else{
                    parent.setVisible(true);

                    int id = (int)System.currentTimeMillis();
                    ActionEvent message = new ActionEvent(algorithm.getCurrent(), id, "Finished");
                    recipient.actionPerformed(message);
                    dispose();
                }
            }
        }
    }
}
