package algorithm;

public interface AlgorithmControl {
    void init(Graph graph);
    Graph getCurrent();
    String getComment();
    void makeStep();
    boolean isFinished();
    void undo();
    boolean canBeUndone();
}
