package algorithm;

import java.awt.Color;

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

    public static Edge getCheapestEdge(Edge[] edges)
    {
        int min = edges[0].getCost();
        Edge cheapest = edges[0];
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
}
