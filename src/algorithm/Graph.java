package algorithm;

import java.util.ArrayList;

public class Graph {

    private final ArrayList<Edge> edgeList;

    public Graph(ArrayList<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    public void addEdge(Edge e)
    {
        edgeList.add(e);
    }

    public void addEdge(int name1, int name2, int cost)
    {
        edgeList.add(new Edge(name1, name2, cost));
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

}
