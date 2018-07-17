package model;

import java.util.function.Consumer;

public interface Board {
    void acquirePoint(Worm worm, Point point);

    void releasePoint(Worm worm, Point point);

    int getWidth();

    int getHeight();

    int getPointOwnerId(Point point);

    void setOnUpdated(Consumer<Board> test);
}
