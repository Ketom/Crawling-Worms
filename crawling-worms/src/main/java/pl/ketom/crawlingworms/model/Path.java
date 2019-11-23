package pl.ketom.crawlingworms.model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Path {
    private int id;
    private List<Direction> directions;
    private List<Point> points;
    private int position;
    private Consumer<Path> onUpdated;

    public Path(Path path) {
        this.id = path.id;
        this.directions = new LinkedList<>();
        this.points = new LinkedList<>();
        this.position = path.position;

        for (Point point : path.points) {
            this.points.add(new Point(point));
        }

        for (Direction direction : path.directions) {
            this.directions.add(direction);
        }
    }

    public Path(int id, Point startingPoint, String pathString) {
        this.id = id;
        this.directions = new LinkedList<>();
        this.points = new LinkedList<>();
        this.position = 0;

        Point point = new Point(startingPoint);

        for (int i = 0; i < pathString.length(); i++) {
            points.add(new Point(point));
            Direction direction = getDirectionFromChar(pathString.charAt(i));
            directions.add(direction);
            point.translate(direction);
        }
    }

    public int getId() {
        return id;
    }

    private Direction getDirectionFromChar(char ch) {
        switch (ch) {
            case 'U':
                return Direction.UP;
            case 'L':
                return Direction.LEFT;
            case 'D':
                return Direction.DOWN;
            case 'R':
                return Direction.RIGHT;
        }
        throw new IllegalArgumentException("Illegal character specifying direction.");
    }

    public Point getPoint() {
        return new Point(points.get(position));
    }

    public int getPosition() {
        return position;
    }

    private void setPosition(int position) {
        // works as desired for negative numbers too
        this.position = Math.floorMod(position, directions.size());
    }

    public Direction getDirection() {
        return directions.get(position);
    }

    public Point nextPoint() {
        nextPosition();
        return getPoint();
    }

    public void resetPosition() {
        setPosition(0);
    }

    public void movePosition(int delta) {
        setPosition(position + delta);
    }

    public void nextPosition() {
        movePosition(1);
    }


    public void setOnUpdated(Consumer<Path> test) {
        this.onUpdated = test;
        onUpdated();
    }

    private void onUpdated() {
        if (onUpdated != null) {
            onUpdated.accept(this);
        }
    }
}
