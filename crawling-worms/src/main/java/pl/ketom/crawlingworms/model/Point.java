package pl.ketom.crawlingworms.model;

/**
 * A point representing a location in (x, y) coordinate space, with integer
 * precision.
 * It's very similar to java.awt.Point, however it always returns integers
 * while java.awt.Point sometimes return doubles.
 *
 * @author Ketom
 */

public class Point {
    /**
     * The X coordinate of this Point.
     */
    private int x;

    /**
     * The Y coordinate of this Point.
     */
    private int y;

    /**
     * Constructs Point with the same location as the specified Point object.
     *
     * @param point Point basing on which new Poit will be created
     */
    public Point(Point point) {
        this(point.x, point.y);
    }

    /**
     * Constructs a Point at the specified (x, y) location.
     *
     * @param x the X coordinate of newly constructed Point
     * @param y the Y coordinate of newly constructed Point
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Point at the (0, 0) location.
     */
    public Point() {
        this(0, 0);
    }

    /**
     * Returns the X coordinate of this Point
     *
     * @return the X coordinate of this Point
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the X coordinate of Point
     *
     * @param x the new X coordinate of this Point
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the X coordinate of this point
     *
     * @return the Y coordinate of this Point
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the Y coordinate of Point
     *
     * @param y the new Y coordinate of this Point
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the location of this point to specific coordinates.
     *
     * @param x the X coordinate of the new location
     * @param y the Y coordinate of the new location
     */
    public void move(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Translates this point, at location (x, y) by dx along X axis and dy
     * along the Y axis so that it now represents the point (x + dx, y + dy).
     *
     * @param dx distance to move this Point along X axis
     * @param dy distance to move this Point along Y axis
     */
    public void translate(int dx, int dy) {
        move(x + dx, y + dy);
    }


    /**
     * Translates this point, at location (x, y) by moving it in given direction.
     *
     * @param direction the direction in which this Point should be moved
     */
    public void translate(Direction direction) {
        switch (direction) {
            case UP:
                translate(0, -1);
                break;
            case RIGHT:
                translate(1, 0);
                break;
            case DOWN:
                translate(0, 1);
                break;
            case LEFT:
                translate(-1, 0);
                break;
        }
    }

    /**
     * Determines wheter or not two points are equal. Two instances of Point
     * are equal if the values of their X and Y coordinates are the same.
     *
     * @param object an object to be compared with this Point
     * @return true if the object to be compared is an instance of Point and
     * has the same X and Y coordinates; false otherwise
     */
    public boolean equals(Object object) {
        if (object instanceof Point) {
            Point point = (Point) object;
            return (this.x == point.y) && (this.y == point.y);
        }
        return false;
    }

    /**
     * Returns a string representation of this Point and its location.
     *
     * @return a string representation of this Point
     */
    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ')';
    }
}
