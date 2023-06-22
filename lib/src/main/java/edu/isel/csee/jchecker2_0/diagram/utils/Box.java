package edu.isel.csee.jchecker2_0.diagram.utils;

/**
 * Class for diagram box
 */
public class Box {
    private int x;
    private int y;
    private int width;
    private int height;
    /**
     * The number of line for box
     */
    public int lineCount;

    /**
     * Constructor for Box class
     */
    public Box() { }

    /**
     * Method for setting x of diagram box
     * @param x x value
     */
    public void setX(int x) { this.x = x; }

    /**
     * Method for return x of diagram box
     * @return x
     */
    public int getX() { return this.x; }

    /**
     * Method for setting y of diagram box
     * @param y y value
     */
    public void setY(int y) { this.y = y; }

    /**
     * Method for return y of diagram box
     * @return y
     */
    public int getY() { return this.y; }

    /**
     * Method for setting width of diagram box
     * @param width width value
     */
    public void setWidth(int width) { this.width = width; }

    /**
     * Method for return width of diagram box
     * @return width
     */
    public int getWidth() { return this.width; }

    /**
     * Method for setting height of diagram box
     * @param height height value
     */
    public void setHeight(int height) { this.height = height; }

    /**
     * Method for return height of diagram box
     * @return height
     */
    public int getHeight() { return this.height; }

    /**
     * Method for setting the number of line in diagram box
     * @param lineCount the number of line
     */
    public void setLineCount(int lineCount) { this.lineCount = lineCount; }

    /**
     * Method for return the number of line in diagram box
     * @return lineCount
     */
    public int getLineCount() { return this.lineCount; }
}
