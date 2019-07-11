import algorithm.Vertex;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class VertexTest {

    @Test
    public void getColor() {
        Vertex v = new Vertex("Test");
        v.setColor(Color.BLACK);
        Assert.assertEquals(Color.BLACK, v.getColor());
        v.setColor(Color.WHITE);
        Assert.assertEquals(Color.WHITE, v.getColor());
    }

    @Test
    public void getName() {
        String name = "Test name";
        Vertex v = new Vertex(name);
        Assert.assertEquals(name, v.getName());
    }

    @Test
    public void equals1() {
        String name1 = "Name 1";
        String name2 = "Name 2";
        Vertex v1 = new Vertex(name1);
        Vertex v2 = new Vertex(name2);
        Assert.assertEquals(false, v1.equals(v2));
        Assert.assertEquals(true, v1.equals(new Vertex(name1)));
    }
}