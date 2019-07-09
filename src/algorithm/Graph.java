package algorithm;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.hierarchical.*;


public class Graph {
    private mxGraph graph;


    private ArrayList<Edge> edgeList;
    private ArrayList<Edge> answerEdgesList;

    public Graph() {
        edgeList = new ArrayList<Edge>();
        answerEdgesList = new ArrayList<Edge>();
    }

    public Graph(ArrayList<Edge> edgeList) {
        this.edgeList = edgeList;
        answerEdgesList = new ArrayList<Edge>();
    }

    public Graph(ArrayList<Edge> edgeList, mxGraph graph)
    {
        this.edgeList = edgeList;
        this.graph = graph;
        answerEdgesList = new ArrayList<Edge>();
    }

    public void addEdge(Edge e)
    {
        edgeList.add(e);
    }

    public void addEdge(String name1, String name2, int cost)
    {
        edgeList.add(new Edge(name1, name2, cost));
    }

    public void removeEdge(String name1, String name2)
    {
        for (Iterator<Edge> it = edgeList.iterator(); it.hasNext();)
        {
            Edge e = it.next();
            Vertex[] v = e.getAdjasent();
            if (name1.equals(v[0].getName()) || name1.equals(v[1].getName()))
                if (name2.equals(v[0].getName()) || name2.equals(v[1].getName()))
                    it.remove();
        }
    }

    public void removeVertex(String name)
    {
        for(Iterator<Edge> it = edgeList.iterator(); it.hasNext();)
        {
            Edge e = it.next();
            Vertex[] v = e.getAdjasent();
            if (name.equals(v[0].getName()) || name.equals(v[1].getName()))
                it.remove();
        }
    }

    public void addToAnswer(Edge e)
    {
        Vertex[] v = e.getAdjasent();
        for (Edge ed : edgeList)
        {
            Vertex[] x = ed.getAdjasent();
            if (x[0].equals(v[0]) || x[0].equals(v[1]))
                x[0].setColor(Color.GREEN);
            if (x[1].equals(v[0]) || x[1].equals(v[1]))
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
        graph = new mxGraph();
        Object grParent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            ArrayList<Vertex> vertex = getVertexList();
            Object[] vertexObj = new Object[vertex.size()];
            for (int i = 0; i < vertex.size(); i++) {
                Color c = vertex.get(i).getColor();
                String styleVertex = "shape=ellipse;fontSize=16;" +
                        String.format("fillColor=#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                vertexObj[i] = graph.insertVertex(grParent, null, vertex.get(i).getName(), 50, 100, 50, 50,
                        styleVertex);
            }

            for (Edge e : edgeList) {
                Color c = e.getColor();
                String costStr = String.valueOf(e.getCost());
                String styleEdge = "align=center;strokeWidth=2;startArrow=none;endArrow=none;fontSize=16;" +
                        String.format("strokeColor=#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());

                Vertex[] adjV = e.getAdjasent();

                graph.insertEdge(grParent, null, costStr, vertexObj[vertex.indexOf(adjV[0])],
                        vertexObj[vertex.indexOf(adjV[1])],
                        styleEdge);
            }

        } finally {
            graph.getModel().endUpdate();
        }
        var layout = new mxHierarchicalLayout(graph);
        layout.execute(grParent);

        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setCellsDisconnectable(false);
        graph.setConnectableEdges(false);

        graph.setVertexLabelsMovable(false);
        graph.setEdgeLabelsMovable(false);
        graph.setResetEdgesOnMove(true);
        graph.setCellsResizable(false);

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setVisible(true);
        graphComponent.setConnectable(false);
        return graphComponent;
    }

    public mxGraphComponent updateGraphComponent()
    {
        Object[] vertexes = graph.getChildVertices(graph.getDefaultParent());
        Object[] newVertexes = new Object[vertexes.length];
        int i = 0;
        mxGraph tmp = new mxGraph();
        Object grParent = tmp.getDefaultParent();
        ArrayList<Vertex> vertex = getVertexList();


        tmp.getModel().beginUpdate();
        try{
            for (Object x : vertexes)
            {
                mxCell v = (mxCell)x;
                int ind = vertex.indexOf(new Vertex(v.getValue().toString()));
                if (ind < 0)
                    continue;
                Color c = vertex.get(ind).getColor();
                String styleVertex = "shape=ellipse;fontSize=16;" +
                        String.format("fillColor=#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                mxGeometry g = v.getGeometry();
                newVertexes[i] = tmp.insertVertex(grParent, null, v.getValue(), g.getX(), g.getY(), g.getWidth(), g.getHeight(), styleVertex);
                i++;
            }

            for (Edge e : edgeList) {
                Color c = e.getColor();
                String costStr = String.valueOf(e.getCost());
                String styleEdge = "align=center;strokeWidth=2;startArrow=none;endArrow=none;fontSize=16;" +
                        String.format("strokeColor=#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());

                Vertex[] adjV = e.getAdjasent();
                Object v1 = null;
                Object v2 = null;

                for (Object o : newVertexes)
                {
                    if (o == null)
                        continue;
                    mxCell v = (mxCell)o;
                    if (adjV[0].equals(new Vertex(v.getValue().toString()))) {
                        v1 = v;
                    }
                    else if (adjV[1].equals(new Vertex(v.getValue().toString()))) {
                        v2 = v;
                    }

                }
                tmp.insertEdge(grParent, null, costStr, v1, v2, styleEdge);
            }

        }
        finally {
            tmp.getModel().endUpdate();
        }

        tmp.setAllowDanglingEdges(false);
        tmp.setCellsEditable(false);
        tmp.setCellsDisconnectable(false);
        tmp.setConnectableEdges(false);

        tmp.setVertexLabelsMovable(false);
        tmp.setEdgeLabelsMovable(false);
        tmp.setResetEdgesOnMove(true);
        tmp.setCellsResizable(false);

        mxGraphComponent graphComponent = new mxGraphComponent(tmp);
        graphComponent.setVisible(true);
        graphComponent.setEnabled(false);
        graphComponent.setConnectable(false);
        return graphComponent;
    }


    public void print()
    {
        for (Edge e : edgeList)
        {
            Vertex[] v = e.getAdjasent();
        }
    }
    public void printA()
    {
        for (Edge e : answerEdgesList)
        {
            Vertex[] v = e.getAdjasent();
        }
    }

    public static Graph getGraphFromString(String str)
    {
        Graph gr = new Graph();
        String[] substr = str.split("\n");
        for (String s : substr)
        {
            String[] elements = s.split(" ");
            gr.addEdge(elements[0], elements[1], Integer.parseInt(elements[2]));
        }
        return gr;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (Edge e : edgeList)
        {
            str.append(e.toString());
        }
        return str.toString();
    }

    public String answerToString()
    {
        StringBuilder str = new StringBuilder();
        for (Edge e : answerEdgesList)
        {
            str.append(e.toString());
        }
        return str.toString();
    }

    public Graph backupGraph()
    {
        ArrayList<Edge> tmp = new ArrayList<>();
        tmp.addAll(edgeList);
        return new Graph(tmp, graph);
    }
    public void restoreGraph(Graph copy)
    {
        this.graph = copy.graph;
        this.edgeList = copy.edgeList;
    }
    public void restoreMXGraph(Graph copy)
    {
        this.graph = copy.graph;
    }
    public mxGraph getMXGraph(){
        return graph;
    }
    public void setMXGraph(mxGraph graph){
        this.graph = graph;
    }
    public void resetColors()
    {
        for (Edge e : edgeList)
        {
            Vertex[] v = e.getAdjasent();
            v[0].setColor(Color.GRAY);
            v[1].setColor(Color.GRAY);
            e.setColor(Color.GRAY);
        }
    }

    public static boolean isValid(String str) {
        String str1 = str.replaceAll("\n ", "\n");
        String[] subStr;
        String delimeter1 = "\n";
        String delimeter2 = " ";
        subStr = str1.split(delimeter1);
        int[][] arr = new int[2][subStr.length];
        String[] tmpStr;
        for (int i = 0; i < subStr.length; i++) {
            tmpStr = subStr[i].split(delimeter2);
            arr[0][i] = Integer.parseInt(tmpStr[0]);
            arr[1][i] = Integer.parseInt(tmpStr[1]);
        }

        boolean[] checked = new boolean[subStr.length];
        checked[0] = true;
        processHelper.goInside(arr, checked, 0);

        for (int i = 0; i < subStr.length; i++) {
            if (!checked[i])
                return false;
        }
        return true;
    }

}
class processHelper {
    public static void goInside(int[][] arr, boolean[] checked, int tmp){
        for(int i = 0; i < checked.length; i++){
            if(checked[i])
                continue;
            if(arr[0][i] == arr[0][tmp] || arr[1][i] == arr[0][tmp] || arr[0][i] == arr[1][tmp] || arr[1][i] == arr[1][tmp]){
                checked[i] = true;
                goInside(arr, checked, i);
            }
        }

    }
}