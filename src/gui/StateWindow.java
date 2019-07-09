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
    private JScrollPane scroll;

    private JLabel labelBeforeState     = new JLabel("Before State Graph", SwingConstants.CENTER);
    private JLabel labelCurrentState    = new JLabel("Current State Graph", SwingConstants.CENTER);
    private DJPAlgorithm algorithm;
    private ActionListener recipient;
    private Timer timer;

    public StateWindow(MainWindow  mainWindow, ActionListener recipient, DJPAlgorithm algorithm) {
        super("State");
        this.algorithm = algorithm;
        this.recipient = recipient;
        parent = mainWindow;
        parent.setVisible(false);
        setBounds(150, 150, 1210, 900);
        Dimension d = new Dimension();
        d.setSize(1080, 700);
        setMinimumSize(d);
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

        scroll = new JScrollPane(log);

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
        mxGraphComponent grComp = algorithm.getCurrent().updateGraphComponent();

        grComp.setBounds(0, 30, nextGraphPanel.getWidth(), nextGraphPanel.getHeight() - 30);
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
        prevStep.addActionListener(new eventHandler());
        startPauseTimer.addActionListener(new eventHandler());
        mainPanel.addComponentListener(new eventHandler());

        log.setEditable(false);


        timeCounter.setText("appears soon");
        log.setText(algorithm.getComment());
    }

    class eventHandler implements ActionListener, ComponentListener{
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
                    grComp.setBounds(0, 30, prevGraphPanel.getWidth(), prevGraphPanel.getHeight() - 30);
                    prevGraphPanel.add(grComp);
                    prevGraphPanel.updateUI();
                    algorithm.makeStep();
                    log.setText(log.getText() + algorithm.getComment());
                    nextGraphPanel.removeAll();
                    nextGraphPanel.add(labelCurrentState);
                    //grComp = algorithm.getCurrent().createGraphComponent();
                    grComp = algorithm.getCurrent().updateGraphComponent();
                    grComp.setBounds(0, 30, nextGraphPanel.getWidth(), nextGraphPanel.getHeight() - 30);
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
            if(actionEvent.getSource() == prevStep)
            {
                if (algorithm.canBeUndone()) {
                    //System.out.println("Undo: " + algorithm.getComment());
                    log.setText(log.getText().replaceAll(algorithm.getComment(), ""));
                    mxGraphComponent  tmp = algorithm.undo();

                    nextGraphPanel.removeAll();
                    nextGraphPanel.add(labelCurrentState);
                    mxGraphComponent grComp = algorithm.getCurrent().updateGraphComponent();
                    grComp.setBounds(0, 30, nextGraphPanel.getWidth(), nextGraphPanel.getHeight() - 30);
                    nextGraphPanel.add(grComp);
                    nextGraphPanel.updateUI();

                    prevGraphPanel.removeAll();
                    prevGraphPanel.add(labelBeforeState);
                    tmp.setBounds(0, 30, prevGraphPanel.getWidth(), prevGraphPanel.getHeight() - 30);
                    prevGraphPanel.add(tmp);
                    prevGraphPanel.updateUI();
                }

            }
            if(actionEvent.getSource() == startPauseTimer)
            {
                if (timer == null) {
                    timer = new Timer(1000, x -> {
                        System.out.println("Time");
                    });
                    timer.start();
                    startPauseTimer.setText("Stop timer");
                }
                else
                {
                    System.out.println("Timer stopped");
                    startPauseTimer.setText("Start timer");
                    timer.stop();
                    timer = null;
                }
            }
        }


        @Override
        public void componentResized(ComponentEvent componentEvent) {
            if (componentEvent.getComponent() == mainPanel)
            {
                int w = mainPanel.getWidth();
                w = (w - 1055) / 6;
                int h = mainPanel.getHeight();
                h /= 30;
                int width = mainPanel.getWidth();
                int height = mainPanel.getHeight() - 4 * h - 70;

                prevGraphPanel.setBounds    (w, h, (width - 3 * w) / 2, height * 2 / 3);
                nextGraphPanel.setBounds    ((width - 3 * w) / 2 + 2 * w, h, (width - 3 * w) / 2, height * 2 / 3);
                scroll.setBounds            (w, height * 2 / 3 + 2 * h, mainPanel.getWidth() - 2 * w, height / 3);
                interruptAlgorithm.setBounds(w, mainPanel.getHeight() - 70 - h,265, 70);
                prevStep.setBounds          (2 * w + 265, mainPanel.getHeight() - 70 - h, 265, 70);
                nextStep.setBounds          (3 * w + 530, mainPanel.getHeight() - 70 - h, 265, 70);
                startPauseTimer.setBounds   (4 * w + 795, mainPanel.getHeight() - 70 - h, 120, 70);
                timeCounter.setBounds       (5 * w + 915, mainPanel.getHeight() - 70 - h, 140, 70);

                for (int i = 0; i < prevGraphPanel.getComponentCount(); i++)
                {
                    if (prevGraphPanel.getComponent(i) instanceof mxGraphComponent)
                        prevGraphPanel.getComponent(i).setBounds(0, 30, width, height - 30);
                }
                for (int i = 0; i < nextGraphPanel.getComponentCount(); i++)
                {
                    if (nextGraphPanel.getComponent(i) instanceof mxGraphComponent)
                        nextGraphPanel.getComponent(i).setBounds(0, 30, width, height - 30);
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
    }
}
