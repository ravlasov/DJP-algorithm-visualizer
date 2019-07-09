package gui;

import algorithm.Graph;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GraphGUI_Input extends JFrame {
    private final MainWindow parent;
    private JPanel mainPanel            = new JPanel();
    private JPanel inputGraphPanel      = new JPanel();
    private JButton applyInputGraph     = new JButton("Apply");
    private JButton cancelInputGraph    = new JButton("Cancel");
    private JLabel inputMode            = new JLabel("Edit mode:");
    private JRadioButton modeVertex     = new JRadioButton("Vertex editing");
    private JRadioButton modeEdge       = new JRadioButton("Edge editing");
    private ActionListener recipient;
    private Graph graph;
    private final Graph backUp;
    private mxGraphComponent comp;

    private Object selected;


    public GraphGUI_Input(MainWindow mainWindow, ActionListener recipient, Graph gr){
        super("Graph Input");
        parent = mainWindow;
        this.recipient = recipient;
        this.graph = gr;
        if (gr != null)
            backUp = gr.backupGraph();
        else
            backUp = new Graph();
        parent.setVisible(false);
        setBounds(150, 150, 1210, 910);
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

        Color mainColor = new Color(234, 255, 226); //#FFFADD

        mainPanel.setBackground(mainColor); //#F6FFF8
        mainPanel.setLayout(null);
        add(mainPanel);

        mainPanel.add(inputGraphPanel);
        mainPanel.add(applyInputGraph);
        mainPanel.add(cancelInputGraph);
        mainPanel.add(inputMode);
        mainPanel.add(modeVertex);
        mainPanel.add(modeEdge);

        inputGraphPanel.setBounds   (30,30, 1140, 740);
        inputMode.setBounds         (30, 790, 120, 70);
        cancelInputGraph.setBounds  (480, 810, 300, 50);
        applyInputGraph.setBounds   (810, 790, 360, 70);
        modeVertex.setBounds        (140, 790, 200, 45);
        modeEdge.setBounds          (140, 820, 200, 45);

        Font f = new Font("Monospaced", Font.PLAIN, 18);
        inputMode.setFont(f);
        cancelInputGraph.setFont(f);
        applyInputGraph.setFont(f);
        modeEdge.setFont(f);
        modeVertex.setFont(f);


        ButtonGroup group = new ButtonGroup();
        group.add(modeVertex);
        group.add(modeEdge);
        modeVertex.addChangeListener(new eventHandler());
        modeEdge.addChangeListener(new eventHandler());
        modeVertex.setSelected(true);

        Color c = new Color(255, 250, 221); //#FFFADD
        inputGraphPanel.setBackground(c);
        modeVertex.setBackground(mainColor);
        modeEdge.setBackground(mainColor);

        cancelInputGraph.addActionListener(new eventHandler());
        applyInputGraph.addActionListener(new eventHandler());


        if (graph != null)
        {
            graph.resetColors();
            comp = graph.updateGraphComponent();
        }
        else
        {
            mxGraph tmp = new mxGraph();
            tmp.setAllowDanglingEdges(false);
            tmp.setCellsEditable(false);
            tmp.setCellsDisconnectable(false);
            tmp.setConnectableEdges(false);
            tmp.setVertexLabelsMovable(false);
            tmp.setEdgeLabelsMovable(false);
            tmp.setResetEdgesOnMove(true);
            tmp.setCellsResizable(false);
            graph = new Graph();
            graph.setMXGraph(tmp);
            comp = new mxGraphComponent(tmp);
        }
        inputGraphPanel.add(comp);
        inputGraphPanel.setLayout(null);
        inputGraphPanel.updateUI();
        comp.setVisible(true);
        comp.setEnabled(true);
        comp.setConnectable(false);
        comp.setBounds(0, 0,  1140, 740);
        comp.setDragEnabled(false);
        comp.getGraphControl().addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                double x = mouseEvent.getPoint().getX();
                double y = mouseEvent.getPoint().getY();
                ArrayList<Object> vertexList = new ArrayList<Object>
                                    (Arrays.asList(comp.getGraph().getChildVertices(comp.getGraph().getDefaultParent())));
                Object o = comp.getCellAt((int)x, (int)y);

                if (o != null && o instanceof mxCell && vertexList.contains(o)){
                    if (selected == null)
                    {
                        if (modeVertex.isSelected()) {
                            graph.removeVertex(((mxCell)o).getValue().toString());
                            comp.getGraph().getModel().remove(o);
                        }
                        else{
                            selected = o;
                        }
                        return;
                    }
                    else if (selected == o)
                    {
                        selected = null;
                        return;

                    }
                    else
                    {
                        try {
                            if (modeEdge.isSelected()) {
                                // if Edge exists - remove it and return
                                for (int i = 0; i < comp.getGraph().getModel().getEdgeCount(comp.getGraph().getSelectionCell()); i++) {
                                    if (selected == ((mxCell) comp.getGraph().getModel().getEdgeAt(comp.getGraph().getSelectionCell(), i)).getSource()
                                            || selected == ((mxCell) comp.getGraph().getModel().getEdgeAt(comp.getGraph().getSelectionCell(), i)).getTarget()) {
                                        if (o == ((mxCell) comp.getGraph().getModel().getEdgeAt(comp.getGraph().getSelectionCell(), i)).getSource()
                                                || o == ((mxCell) comp.getGraph().getModel().getEdgeAt(comp.getGraph().getSelectionCell(), i)).getTarget()) {
                                            comp.getGraph().getModel().remove(comp.getGraph().getModel().getEdgeAt(comp.getGraph().getSelectionCell(), i));
                                            graph.removeEdge(((mxCell)selected).getValue().toString(),
                                                    ((mxCell)o).getValue().toString());
                                            return;
                                        }
                                    }
                                }
                                // else add new Edge
                                comp.getGraph().getModel().beginUpdate();
                                try {
                                    Color c = Color.GRAY;
                                    String inp = JOptionPane.showInputDialog(GraphGUI_Input.this, "Input edge cost:", "10");
                                    if (!inp.matches("^[0-9]+$"))
                                    {
                                        JLabel msg = new JLabel("Input a number");
                                        msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                                        JOptionPane.showMessageDialog(GraphGUI_Input.this, msg,
                                                "Invalid input", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }
                                    int cost = Integer.parseInt(inp);
                                    String style = "align=center;strokeWidth=2;startArrow=none;endArrow=none;fontSize=16;" +
                                            String.format("strokeColor=#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                                    comp.getGraph().insertEdge(comp.getGraph().getDefaultParent(), null,
                                            inp, selected, o, style);
                                    graph.addEdge(((mxCell)selected).getValue().toString(),
                                            ((mxCell)o).getValue().toString(), cost);
                                } finally {
                                    comp.getGraph().getModel().endUpdate();
                                }
                                return;
                            }
                        }
                        finally {
                            selected = null;
                        }
                    }
                }
                else {
                    if (modeVertex.isSelected()) {
                        comp.getGraph().getModel().beginUpdate();
                        try {
                            int i = 0;
                            boolean valid = false;
                            while (!valid) {
                                i++;
                                for (Object p : vertexList) {
                                    mxCell px = (mxCell) p;
                                    valid |= px.getValue().toString().equals(String.valueOf(i));
                                }
                                valid = !valid;
                            }


                            Color c = Color.GRAY;
                            String styleVertex = "shape=ellipse;fontSize=16;" +
                                    String.format("fillColor=#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                            comp.getGraph().insertVertex(comp.getGraph().getDefaultParent(), null, String.valueOf(i),
                                    x - 25, y - 25, 50, 50, styleVertex);
                        } finally {
                            comp.getGraph().getModel().endUpdate();
                        }
                    }
                }
            }
        });

    }

    class eventHandler implements ActionListener, ChangeListener {
        @Override
        public void actionPerformed (ActionEvent actionEvent){
            if(actionEvent.getSource() == cancelInputGraph) {
                int id = (int)System.currentTimeMillis();
                if(backUp != null)
                {
                    ActionEvent message = new ActionEvent(backUp, id, "Not changed");
                    recipient.actionPerformed(message);
                }
                parent.setVisible(true);
                dispose();
            }
            if(actionEvent.getSource() == applyInputGraph)
            {
                int id = (int)System.currentTimeMillis();
                if (graph == null)
                {
                    JLabel msg = new JLabel("No graph entered");
                    msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(GraphGUI_Input.this, msg,
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String str = graph.toString();
                if (str.length() == 0)
                {
                    JLabel msg = new JLabel("The graph must contain at least one edge");
                    msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(GraphGUI_Input.this, msg,
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!Graph.isValid(str)) {
                    JLabel msg = new JLabel("The graph must be connected");
                    msg.setFont( new Font("Monospaced", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(GraphGUI_Input.this, msg,
                            "Invalid input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                comp.getGraph().moveCells(comp.getGraph().getChildCells(comp.getGraph().getDefaultParent()),
                        getDistanceX(), getDistanceY());
                graph.setMXGraph(comp.getGraph());
                ActionEvent message = new ActionEvent(graph, id, "Edited");
                recipient.actionPerformed(message);
                parent.setVisible(true);
                dispose();
            }
        }

        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            selected = null;
        }

        private double getDistanceX ()
        {
            double leftMost = Double.MAX_VALUE;
            Object[] vertexes = comp.getGraph().getChildVertices(comp.getGraph().getDefaultParent());
            for (Object c : vertexes)
            {
                mxCell v = (mxCell)c;
                if (v.getGeometry().getX() < leftMost)
                    leftMost = v.getGeometry().getX();
            }
            return 10 - leftMost;
        }
        private double getDistanceY ()
        {
            double leftMost = Double.MAX_VALUE;
            Object[] vertexes = comp.getGraph().getChildVertices(comp.getGraph().getDefaultParent());
            for (Object c : vertexes)
            {
                mxCell v = (mxCell)c;
                if (v.getGeometry().getY() < leftMost)
                    leftMost = v.getGeometry().getY();
            }
            return 10 - leftMost;
        }
    }

}