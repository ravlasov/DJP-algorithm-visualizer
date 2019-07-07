package algorithm;

import java.awt.*;
import java.util.ArrayList;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.hierarchical.*;


public class Graph {

    private final ArrayList<Edge> edgeList;
    private ArrayList<Edge> answerEdgesList;

    public Graph() {
        edgeList = new ArrayList<Edge>();
        answerEdgesList = new ArrayList<Edge>();
    }

    public Graph(ArrayList<Edge> edgeList) {
        this.edgeList = edgeList;
        answerEdgesList = new ArrayList<Edge>();
    }

    public void addEdge(Edge e)
    {
        edgeList.add(e);
    }

    public void addEdge(int name1, int name2, int cost)
    {
        edgeList.add(new Edge(name1, name2, cost));
    }

    public void addToAnswer(Edge e)
    {
        Vertex[] v = e.getAdjasent();
        for (Edge ed : edgeList)
        {
            Vertex[] x = ed.getAdjasent();
            if (x[0].equals(v[0]) || x[0].equals(v[1]))
                x[0].setColor(Color.GREEN);
            if (x[1].equals(v[0]) || x[0].equals(v[1]))
                x[1].setColor(Color.GREEN);
        }
        answerEdgesList.add(e);
    }

    public Edge getFirstEdge()
    {
        return edgeList.get(0);
    }

    public ArrayList<Edge> getIncedentEdges(Vertex vertex) //Возвращает список ребер, инцедентных с вершиной
    {
        ArrayList<Edge> incedentEdges = new ArrayList<>();
        for (Edge e : edgeList)
        {
            if (e.isAdjacent(vertex))
                incedentEdges.add(e);
        }
        return incedentEdges;
    }

    private ArrayList<Vertex> getVertexList()
    {
        ArrayList<Vertex> list= new ArrayList<>();
        for (Edge e : edgeList)
        {
            Vertex[] v = e.getAdjasent();
            if (!list.contains(v[0])) {
                list.add(v[0]);
            }
            if (!list.contains(v[1]))
            {
                list.add(v[1]);
            }
        }

        return list;
    }

    public mxGraphComponent createGraphComponent() //Создает компоненту для отображения графа
    {
        mxGraph graph = new mxGraph();
        Object grParent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try
        {
            ArrayList<Vertex> vertex = getVertexList();
            Object[] vertexObj = new Object[vertex.size()];
            for (int i = 0; i < vertex.size(); i++)
            {
                Color c = vertex.get(i).getColor();
                String styleVertex = "shape=ellipse;fontSize=16;" +
                                    String.format("fillColor=#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                vertexObj[i] = graph.insertVertex(grParent, null, vertex.get(i).getName(), 50, 100, 50, 50,
                                                    styleVertex);
            }

            for (Edge e : edgeList)
            {
                Color c = e.getColor();
                String costStr;
                String styleEdge = "align=left;strokeWidth=2;startArrow=none;endArrow=none;fontSize=16;";
                if (c == Color.WHITE) {
                    costStr = null;
                    styleEdge +="strokeColor=none";
                }
                else {
                    costStr = String.valueOf(e.getCost());
                    styleEdge += String.format("strokeColor=#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                }

                Vertex[] adjV = e.getAdjasent();
                graph.insertEdge(grParent, null, costStr, vertexObj[adjV[0].getName()-1], vertexObj[adjV[1].getName()-1],
                                    styleEdge);
            }

        }
        finally
        {
            graph.getModel().endUpdate();
        }
        var layout = new mxHierarchicalLayout(graph);
        layout.execute(grParent);

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setBounds(0, 30, 555, 600);
        graphComponent.setEnabled(false);
        graphComponent.setVisible(true);
        return graphComponent;
    }


    public void print()
    {
        for (Edge e : edgeList)
        {
            Vertex[] v = e.getAdjasent();
            System.out.println(v[0].getName() + " " + v[1].getName() + " " + e.getCost());
        }
    }
    public void printA()
    {
        for (Edge e : answerEdgesList)
        {
            Vertex[] v = e.getAdjasent();
            System.out.println(v[0].getName() + " " + v[1].getName() + " " + e.getCost());
        }
    }

    public static Graph getGraphFromString(String str)
    {
        Graph gr = new Graph();
        String[] substr = str.split("\n");
        for (String s : substr)
        {
            String[] elements = s.split(" ");
            gr.addEdge(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
        }
        return gr;
    }

}
