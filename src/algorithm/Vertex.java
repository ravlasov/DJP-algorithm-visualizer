package algorithm;

import java.awt.Color;

public class Vertex {
    private final int name;
    private Color color;

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
}
