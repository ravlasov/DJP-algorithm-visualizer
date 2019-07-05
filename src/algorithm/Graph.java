package algorithm;

import java.util.ArrayList;

public class Graph {

    private final ArrayList<Edge> edgeList;
    private ArrayList<Edge> answerEdgesList;

    public Graph() {
        this.edgeList = new ArrayList<>();
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

}
