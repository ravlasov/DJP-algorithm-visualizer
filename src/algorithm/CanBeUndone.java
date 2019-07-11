package algorithm;

import com.mxgraph.swing.mxGraphComponent;

@FunctionalInterface
public interface CanBeUndone {
    mxGraphComponent undo();
}
