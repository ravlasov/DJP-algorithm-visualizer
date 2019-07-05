package algorithm;

import java.awt.Color;
import java.util.ArrayList;

public class Edge {
    private Vertex v1;
    private Vertex v2;
    private int cost;
    private Color color;

    public Edge(int name1, int name2, int cost) {
        v1 = new Vertex(name1);
        v2 = new Vertex(name2);
        this.cost = cost;
    }

    public boolean isAdjacent(Vertex v)
    {
        if (v.getName() == v1.getName() || v.getName() == v2.getName())
            return true;
        return false;
    }

    public Vertex[] getAdjasent()
    {
        return new Vertex[] {v1, v2};
    }

    public int getCost()
    {
        return cost;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Edge getCheapestEdge(ArrayList<Edge> edges)
    {
        Edge cheapest = edges.get(0);
        int min = cheapest.getCost();
        for (Edge e : edges)
        {
            if (e.getCost() < min)
            {
                min = e.getCost();
                cheapest = e;
            }
        }
        return cheapest;
    }


    public String toString()
    {
        return v1.getName() + " " + v2.getName() + " " + cost + "\n";
    }

    public static String getListAsString(ArrayList<Edge> edges)
    {
        StringBuilder str = new StringBuilder();
        for (Edge e : edges)
        {
            str.append(e.toString());
        }
        return str.toString();
    }
}
