package algorithm;

import com.mxgraph.swing.mxGraphComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class DJPAlgorithm implements AlgorithmControl {
    private Graph gr;
    private ArrayList<Vertex> visited;
    private byte phase; // 0 - find incedent Edges, 1 - choose and add cheapest
    private ArrayList<Edge> incedentEdgesList;
    private boolean finished;
    private String comment;
    private String bothVisited;
    private Stack<CanBeUndone> acts;

    private mxGraphComponent beforeStep0;
    private mxGraphComponent beforeStep1;


    @Override
    public void init(Graph graph) {
        gr = graph;
        acts = new Stack<>();
        visited = new ArrayList<Vertex>();
        Edge e = graph.getFirstEdge();
        Vertex[] v = e.getAdjasent();
        visited.add(v[0]);
        v[0].setColor(Color.GREEN);
        phase = 0;
        finished = false;
        beforeStep0 = gr.updateGraphComponent();
        beforeStep1 = gr.updateGraphComponent();
        incedentEdgesList = new ArrayList<>();

        comment = "Arbitrary Vertex selected: '" + v[0].getName() + "'.\n";

    }

    @Override
    public Graph getCurrent() {
        return gr;
    }

    @Override
    public void makeStep() {
        gr.paintVisited(visited);
        if (phase == 0)
        {
            actStep0 action = new actStep0();
            beforeStep0 = gr.updateGraphComponent();

            if (incedentEdgesList != null){
                for (Edge e : incedentEdgesList)
                {
                    if (e.getColor() != Color.GREEN)
                        e.setColor(Color.GRAY);
                }
                //incedentEdgesList.clear();
            }
            incedentEdgesList = getAllIncedentEdges();
            action.setNewIncedentEdges(incedentEdgesList);
            for (Edge e : incedentEdgesList)
            {
                e.setColor(Color.BLUE);
            }
            acts.push(action);
            phase = 1;
            comment = "Current adjasent edges are:\n" + Edge.getListAsString(incedentEdgesList) +
                    (bothVisited.length() == 0 ? "" :
                      "Theese edges are no longer considered:\n" + bothVisited);
            bothVisited = null;
            if (incedentEdgesList.size() == 0) {
                comment = "Algorithm is finished. Answer is:\n" + gr.answerToString();
                finished = true;
            }
        }
        else if (phase == 1)
        {
            actStep1 action = new actStep1();
            beforeStep1 = gr.updateGraphComponent();

            if (incedentEdgesList.size() == 0) {
                System.out.println("!!!");
                return;
            }
            Edge cheapest = Edge.getCheapestEdge(incedentEdgesList);
            action.setIncedentEdges(incedentEdgesList);

            comment = "The cheapest one is: " + cheapest.toString();
            cheapest.setColor(Color.GREEN);
            gr.addToAnswer(cheapest);
            Vertex[] v = cheapest.getAdjasent();


            for (Edge e : incedentEdgesList)
            {
                Color c = new Color(0, 255, 238); //#00FFEE
                if (e.getColor() == Color.BLUE)
                    e.setColor(c);
            }
            if (!isVisited(v[0])) {
                visited.add(v[0]);
                v[0].setColor(Color.GREEN);
                action.setAddedToAnswer(v[0]);

            }
            if (!isVisited(v[1])) {
                visited.add(v[1]);
                v[1].setColor(Color.GREEN);
                action.setAddedToAnswer(v[1]);
            }
            acts.push(action);
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
    public mxGraphComponent undo() {
        CanBeUndone action = acts.pop();
        mxGraphComponent comp = action.undo();
        gr.paintVisited(visited);
        return comp;
    }

    @Override
    public boolean canBeUndone() {
        return !acts.isEmpty();
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
        StringBuilder str = new StringBuilder();
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
                if (!str.toString().contains(e.toString()))
                    str.append("\t" + e.toString());
                it.remove();
            }
        }
        bothVisited = str.toString();

        return incedent;
    }




    class actStep0 implements CanBeUndone {
        private String lastComment;
        private Edge addedToAnswer = null;
        private ArrayList<Edge> incedentEdges;
        private ArrayList<Edge> newIncedentEdges;
        private mxGraphComponent before0;
        private mxGraphComponent before1;

        actStep0()
        {
            lastComment = comment;
            before0 = beforeStep0;
            before1 = beforeStep1;
            incedentEdges = incedentEdgesList;
            if (incedentEdges != null)
            {
                for (Edge e : incedentEdges)
                {
                    if (e.getColor() == Color.GREEN)
                        addedToAnswer = e;
                }
            }



        }

        public void setNewIncedentEdges(ArrayList<Edge> list)
        {
            newIncedentEdges = list;
        }

        @Override
        public mxGraphComponent undo() {
            beforeStep0 = before0;
            beforeStep1 = before1;
            incedentEdgesList = newIncedentEdges;
            comment = lastComment;
            finished = false;
            for (Edge e : newIncedentEdges)
            {
                e.setColor(Color.GRAY);
            }
            if (incedentEdges != null)
                for (Edge e : incedentEdges)
                {
                    if (e == addedToAnswer)
                        e.setColor(Color.GREEN);
                    else
                    {
                        Color c;
                        if (addedToAnswer == null)
                            c = Color.GRAY;
                        else
                            c = new Color(0, 255, 238);
                        e.setColor(c);
                    }
                }



            phase = 0;
            return before1;
        }
    }

    class actStep1 implements CanBeUndone{
        private String lastComment;
        private Vertex addedToAnswer;
        private ArrayList<Edge> incedentEdges;
        private mxGraphComponent before0;
        private mxGraphComponent before1;

        actStep1()
        {
            lastComment = comment;
            before0 = beforeStep0;
            before1 = beforeStep1;
        }

        public void setAddedToAnswer(Vertex e)
        {
            addedToAnswer = e;
        }
        public void setIncedentEdges(ArrayList<Edge> list)
        {
            incedentEdges = list;
        }

        @Override
        public mxGraphComponent undo() {
            beforeStep0 = before0;
            beforeStep1 = before1;
            comment = lastComment;
            visited.remove(addedToAnswer);
            addedToAnswer.setColor(Color.GRAY);
            incedentEdgesList = incedentEdges;
            for (Edge e : incedentEdges)
            {
                e.setColor(Color.BLUE);
            }
            phase = 1;
            return before0;
        }
    }


}