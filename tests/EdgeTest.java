import algorithm.Edge;
import algorithm.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class EdgeTest {

    @Test
    public void isAdjacent() {
        String nameV1 = "Name 1";
        String nameV2 = "Name 2";
        Edge edge = new Edge(nameV1, nameV2, 13);
        Assert.assertEquals(true, edge.isAdjacent(new Vertex(nameV1)));
        Assert.assertEquals(true, edge.isAdjacent(new Vertex(nameV2)));
        Assert.assertEquals(false, edge.isAdjacent(new Vertex("Name 3")));

    }

    @Test
    public void getCost() {
        int cost = 100;
        Edge edge = new Edge("N1", "N2", cost);
        Assert.assertEquals(cost, edge.getCost());
    }

    @Test
    public void getColor() {
        Edge edge = new Edge("N1", "N2", 100);
        edge.setColor(Color.BLACK);
        Assert.assertEquals(Color.BLACK, edge.getColor());
        edge.setColor(Color.WHITE);
        Assert.assertEquals(Color.WHITE, edge.getColor());
    }

    @Test
    public void getCheapestEdge() {
        Edge e1 = new Edge("N11", "N12", 30);
        Edge e2 = new Edge("N21", "N22", 100);
        Edge e3 = new Edge("N31", "N32", 10);
        ArrayList<Edge> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        Assert.assertEquals(e3, Edge.getCheapestEdge(list));
    }

    @Test
    public void getListAsString() {
        Edge e1 = new Edge("N11", "N12", 30);
        Edge e2 = new Edge("N21", "N22", 100);
        Edge e3 = new Edge("N31", "N32", 10);
        ArrayList<Edge> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);
        String expected = "\tN11 N12 30\n" +
                "\tN21 N22 100\n" +
                "\tN31 N32 10\n";
        Assert.assertEquals(expected, Edge.getListAsString(list));
    }
}