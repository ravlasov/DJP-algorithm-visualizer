package algorithm;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DJPAlgorithm implements AlgorithmControl {
    private Graph gr;
    private ArrayList<Vertex> visited;
    private byte phase; // 0 - find incedent Edges, 1 - choose and add cheapest
    private ArrayList<Edge> incedentEdgesList;
    private boolean finished;
    private String comment;

    @Override
    public void init(Graph graph) {
        gr = graph;
        visited = new ArrayList<Vertex>();
        Edge e = graph.getFirstEdge();
        Vertex[] v = e.getAdjasent();
        visited.add(v[0]);
        v[0].setColor(Color.GREEN);
        phase = 0;
        finished = false;

        comment = "Arbitrary Vertex selected: '" + v[0].getName() + "'.\n";

    }

    @Override
    public Graph getCurrent() {
        return gr;
    }

    @Override
    public void makeStep() {
        if (phase == 0)
        {
            incedentEdgesList = getAllIncedentEdges();
            for (Edge e : incedentEdgesList)
            {
                e.setColor(Color.BLUE);
            }
            phase = 1;
            comment = "Current adjasent edges are:\n" + Edge.getListAsString(incedentEdgesList);
            if (incedentEdgesList.size() == 0) {
                finished = true;
            }
        }
        else if (phase == 1)
        {
            Edge cheapest = Edge.getCheapestEdge(incedentEdgesList);
            comment = "The cheapest one is: " + cheapest.toString();
            cheapest.setColor(Color.GREEN);
            gr.addToAnswer(cheapest);
            Vertex[] v = cheapest.getAdjasent();
            for (Edge e : incedentEdgesList)
            {
                if (e.getColor() == Color.BLUE)
                    e.setColor(Color.GRAY);
            }
            if (!isVisited(v[0])) {
                visited.add(v[0]);
                v[0].setColor(Color.GREEN);
            }
            if (!isVisited(v[1])) {
                visited.add(v[1]);
                v[1].setColor(Color.GREEN);
            }
            incedentEdgesList.clear();
            phase = 0;
        }


    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void undo() {
        //appears soon
    }

    @Override
    public boolean canBeUndone() {
        return false;
    }

    private boolean isVisited(Vertex v)
    {
        for (Vertex x : visited)
        {
            if (v.equals(x))
                return true;
        }
        return false;
    }

    private ArrayList<Edge> getAllIncedentEdges()
    {
        ArrayList<Edge> incedent = new ArrayList<>();
        for (Vertex v : visited)
        {
            incedent.addAll(gr.getIncedentEdges(v));
        }

        for(Iterator<Edge> it = incedent.iterator(); it.hasNext();)
        {
            Edge e = it.next();
            Vertex[] c = e.getAdjasent();
            if (isVisited(c[0]) && isVisited(c[1])) {
                if (e.getColor() == Color.GRAY)
                    e.setColor(Color.WHITE);
                it.remove();
            }
        }

        return incedent;
    }
}
