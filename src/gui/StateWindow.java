package gui;

import algorithm.DJPAlgorithm;
import com.mxgraph.swing.mxGraphComponent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StateWindow  extends JFrame {
    private final MainWindow parent;
    private int timerTime               = 10000;
    private JPanel mainPanel            = new JPanel();
    private JButton nextStep            = new JButton("Next step ->");
    private JButton prevStep            = new JButton("<- Previous step");
    private JButton interruptAlgorithm  = new JButton("Go to main menu");
    private JButton startPauseTimer     = new JButton("Pause");
    private JLabel timeCounter          = new JLabel(Integer.toString(timerTime/1000), SwingConstants.CENTER);
    private JPanel prevGraphPanel       = new JPanel();
    private JPanel nextGraphPanel       = new JPanel();
    private JTextArea log               = new JTextArea();
    private JScrollPane scroll;

    private JLabel labelBeforeState     = new JLabel("Before State Graph", SwingConstants.CENTER);
    private JLabel labelCurrentState    = new JLabel("Current State Graph", SwingConstants.CENTER);
    private DJPAlgorithm algorithm;
    private ActionListener recipient;
    private boolean timerFlaag          = true;
    private int currentTime             = timerTime;
    private javax.swing.Timer timerLabel= new javax.swing.Timer(1000, y->{
        currentTime -= 1000;
        if(currentTime == 0) {
            nextStep.doClick();
            currentTime = timerTime;
        }
        timeCounter.setText(Integer.toString(currentTime/1000));
        timeCounter.updateUI();
    });

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
        timeCounter.setFont(new Font("Monospaced", Font.PLAIN, 30));
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
        timerLabel.start();
        startPauseTimer.setText("Pause");
        timeCounter.addMouseListener(new eventHandler());
        log.setText(algorithm.getComment());
    }

    class eventHandler implements ActionListener, ComponentListener, MouseListener {
        @Override
        public void actionPerformed (ActionEvent actionEvent){
            if(actionEvent.getSource() == interruptAlgorithm) {
                timerLabel.stop();
                parent.setVisible(true);
                dispose();
            }
            if(actionEvent.getSource() == nextStep){
                if(!algorithm.isFinished()){
                    prevGraphPanel.removeAll();
                    prevGraphPanel.add(labelBeforeState);
                    mxGraphComponent grComp = algorithm.getCurrent().updateGraphComponent();
                    grComp.setBounds(0, 30, prevGraphPanel.getWidth(), prevGraphPanel.getHeight() - 30);
                    prevGraphPanel.add(grComp);
                    prevGraphPanel.updateUI();
                    algorithm.makeStep();
                    log.setText(log.getText() + algorithm.getComment());
                    nextGraphPanel.removeAll();
                    nextGraphPanel.add(labelCurrentState);
                    grComp = algorithm.getCurrent().updateGraphComponent();
                    grComp.setBounds(0, 30, nextGraphPanel.getWidth(), nextGraphPanel.getHeight() - 30);
                    nextGraphPanel.add(grComp);
                    nextGraphPanel.updateUI();
                    currentTime = timerTime;
                    timeCounter.setText(Integer.toString(currentTime/1000));
                    timeCounter.updateUI();
                }
                else{
                    parent.setVisible(true);
                    timerLabel.stop();
                    int id = (int)System.currentTimeMillis();
                    ActionEvent message = new ActionEvent(algorithm.getCurrent(), id, "Finished");
                    recipient.actionPerformed(message);
                    dispose();
                }
            }
            if(actionEvent.getSource() == prevStep)
            {
                timerLabel.stop();
                timerFlaag = false;
                startPauseTimer.setText("Start");
                currentTime = timerTime;
                timeCounter.setText(Integer.toString(currentTime/1000));
                timeCounter.updateUI();
                if (algorithm.canBeUndone()) {
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
                if(timerFlaag){
                    timerLabel.stop();
                    startPauseTimer.setText("Start");
                    timerFlaag = false;
                }
                else{
                    timerLabel.start();
                    startPauseTimer.setText("Pause");
                    timerFlaag = true;
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

        @Override
        public void mouseClicked(MouseEvent e) {
            timerLabel.stop();
            try {
                int tmpTime = Integer.parseInt(JOptionPane.showInputDialog(StateWindow.this,
                        "Input new timer time: ", String.valueOf(currentTime/1000)));
                if (tmpTime > 0) {
                    if(tmpTime < currentTime)
                        currentTime = tmpTime * 1000;
                    timerTime = tmpTime * 1000;
                }

            }catch (Exception e1){
                timerTime = 10000;
            }

            timerLabel.start();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
