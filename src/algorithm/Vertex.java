package algorithm;

import java.awt.Color;

public class Vertex {
    private final int name;
    private Color color = Color.GRAY;

    public Vertex(int value) {
        this.name = value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getName()
    {
        return name;
    }

    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof Vertex))
            return false;
        Vertex v = (Vertex)o;
        return this.name == v.name;
    }
}
