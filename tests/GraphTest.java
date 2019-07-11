import algorithm.DJPAlgorithm;
import algorithm.Graph;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraphTest {

    @Test
    public void countFinalCost() {
        String graph = "1 6 5\n" +
                "2 6 20\n" +
                "11 10 3\n" +
                "8 11 7\n" +
                "9 12 4\n" +
                "11 12 14\n" +
                "8 9 5\n" +
                "8 7 31\n" +
                "7 10 22\n" +
                "10 8 6\n" +
                "4 8 11\n" +
                "4 7 13\n" +
                "4 3 2\n" +
                "7 3 5\n" +
                "1 3 9\n" +
                "4 5 2\n" +
                "5 6 1\n" +
                "2 5 9\n" +
                "4 6 10\n" +
                "4 9 4\n" +
                "9 11 15\n" +
                "5 9 17\n" +
                "1 4 12";
        Graph g = Graph.getGraphFromString(graph);
        g.createGraphComponent();
        Assert.assertEquals(0, g.countFinalCost());
        DJPAlgorithm alg = new DJPAlgorithm();
        alg.init(g);
        while (!alg.isFinished())
        {
            alg.makeStep();
        }
        Assert.assertEquals(46, g.countFinalCost());
        alg = new DJPAlgorithm();
        alg.init(g);
        while (!alg.isFinished())
        {
            alg.makeStep();
        }
        Assert.assertEquals(46, g.countFinalCost());

    }

    @Test
    public void isValid() {
        String testStr = "1 6 5\n" +
                "2 6 20\n" +
                "11 10 3\n" +
                "8 11 7\n" +
                "9 12 4\n" +
                "11 12 14\n" +
                "8 9 5\n" +
                "8 7 31\n" +
                "7 10 22\n" +
                "10 8 6\n" +
                "4 8 11\n" +
                "4 7 13\n" +
                "4 3 2\n" +
                "7 3 5\n" +
                "1 3 9\n" +
                "4 5 2\n" +
                "5 6 1\n" +
                "2 5 9\n" +
                "4 6 10\n" +
                "4 9 4\n" +
                "9 11 15\n" +
                "5 9 17\n" +
                "1 4 12";
        Assert.assertEquals(true, Graph.isValid(testStr));
        testStr = "1 2 3\n" +
                "2 3 4\n" +
                "1 4 5\n" +
                "1 7 8\n" +
                "9 10 3\n" +
                "1 3 4";
        Assert.assertEquals(false, Graph.isValid(testStr));
    }

    @Test
    public void toString1() {
        String testStr = "1 6 5\n" +
                "2 6 20\n" +
                "11 10 3\n" +
                "8 11 7\n" +
                "9 12 4\n" +
                "11 12 14\n" +
                "8 9 5\n" +
                "8 7 31\n" +
                "7 10 22\n" +
                "10 8 6\n" +
                "4 8 11\n" +
                "4 7 13\n" +
                "4 3 2\n" +
                "7 3 5\n" +
                "1 3 9\n" +
                "4 5 2\n" +
                "5 6 1\n" +
                "2 5 9\n" +
                "4 6 10\n" +
                "4 9 4\n" +
                "9 11 15\n" +
                "5 9 17\n" +
                "1 4 12";
        Graph g = Graph.getGraphFromString(testStr);
        Assert.assertEquals(testStr + "\n", g.toString());
        testStr = "1 2 3\n" +
                "2 3 4\n" +
                "1 4 5\n" +
                "1 7 8\n" +
                "9 10 3\n" +
                "1 3 4";
        g = Graph.getGraphFromString(testStr);
        Assert.assertEquals(testStr + "\n", g.toString());
    }
}