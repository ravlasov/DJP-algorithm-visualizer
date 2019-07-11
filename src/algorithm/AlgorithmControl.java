package algorithm;

import com.mxgraph.swing.mxGraphComponent;

public interface AlgorithmControl {
    void init(Graph graph);
    void init(Graph graph, String startVertexName);
    Graph getCurrentGraphState();
    String getComment();
    void makeStep();
    boolean isFinished();
    mxGraphComponent undo();
    boolean canBeUndone();
}
