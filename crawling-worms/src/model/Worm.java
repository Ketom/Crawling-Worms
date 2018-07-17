package model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Worm implements Runnable {
    private Board board;
    private Path path;
    private List<Point> segments;
    private Consumer<Worm> onUpdated;
    private Thread thread;
    private int id;
    private double speed;
    private boolean isPaused;
    private boolean isRunning;
    private boolean isSleeping;

    public Worm(int id, Board board, Path path, int length) {
        this.id = id;
        this.board = board;
        this.path = path;
        this.speed = 0;
        this.segments = new LinkedList<>();
        this.isSleeping = false;
        this.isPaused = false;
        this.isRunning = false;

        path.movePosition(-length);

        // we don't reserve used segments this way
        for (int i = 0; i < length; i++) {
            segments.add(0, new Point(path.getPoint()));
            path.nextPosition();
        }
        path.movePosition(-1);
    }

    public int getId() {
        return id;
    }

    private void move() {
        if (!isRunning) {
            return;
        }
        Point newPosition = path.nextPoint();
        board.acquirePoint(this, newPosition);
        segments.add(0, new Point(newPosition));
        Point oldPoint = segments.remove(segments.size() - 1);
        board.releasePoint(this, oldPoint);
        onUpdated();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        interrupt();
        onUpdated();
    }

    private void sleepNoThrow(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    private void sleep() {
        while (isRunning && (speed <= 0 || isPaused)) {
            sleepNoThrow(1000);
        }
        if (speed > 0) {
            long sleepTime = (long) (1000 / speed);
            long currentTime = System.currentTimeMillis();
            long startTime = currentTime;
            long endTime = startTime + sleepTime;

            long remainingTime = endTime - currentTime;
            while (remainingTime > 0 && isRunning) {
                sleepNoThrow(remainingTime);

                // sleep time will change if speed changes
                sleepTime = (long) (1000 / speed);
                endTime = startTime + sleepTime;
                currentTime = System.currentTimeMillis();
                remainingTime = endTime - currentTime;
            }
        }
    }

    public void terminate() {
        isRunning = false;
        interrupt();
        thread = null;
    }

    private void interrupt() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public List<Point> getSegments() {
        List<Point> result = new LinkedList<>();
        for (Point point : segments) {
            result.add(new Point(point));
        }
        return result;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public void setSleeping(boolean sleeping) {
        isSleeping = sleeping;
        onUpdated();
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
        interrupt();
    }

    public void run() {
        if(isRunning) {
            throw new RuntimeException("Worm [" +  id + "] is already running!");
        }
        isRunning = true;
        thread = Thread.currentThread();
        while (isRunning) {
            move();
            sleep();
        }
    }

    public void setOnUpdated(Consumer<Worm> test) {
        this.onUpdated = test;
        onUpdated();
    }

    private void onUpdated() {
        if (onUpdated != null) {
            onUpdated.accept(this);
        }
    }
}
