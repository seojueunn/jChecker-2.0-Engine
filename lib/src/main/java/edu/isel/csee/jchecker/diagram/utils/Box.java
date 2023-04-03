package edu.isel.csee.jchecker.diagram.utils;

public class Box {
    private int x;
    private int y;
    private int width;
    private int height;
    public int lineCount;

    public Box() { }

    public void setX(int x) { this.x = x; }
    public int getX() { return this.x; }

    public void setY(int y) { this.y = y; }
    public int getY() { return this.y; }

    public void setWidth(int width) { this.width = width; }
    public int getWidth() { return this.width; }

    public void setHeight(int height) { this.height = height; }
    public int getHeight() { return this.height; }

    public void setLineCount(int lineCount) { this.lineCount = lineCount; }
    public int getLineCount() { return this.lineCount; }
}
