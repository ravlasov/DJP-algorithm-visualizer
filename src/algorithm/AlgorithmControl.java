package algorithm;

import com.mxgraph.swing.mxGraphComponent;

public interface AlgorithmControl {
    void init(Graph graph);
    Graph getCurrent();
    String getComment();
    void makeStep();
    boolean isFinished();
    mxGraphComponent undo();
    boolean canBeUndone();
}
