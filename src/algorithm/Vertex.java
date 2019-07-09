package algorithm;

import java.awt.Color;

public class Vertex {
    private final String name;
    private Color color = Color.GRAY;

    public Vertex(String value) {
        this.name = value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getName()
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
        return this.name.equals(v.name);
    }
}
