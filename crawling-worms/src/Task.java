import model.*;

import java.util.function.Consumer;

public class Task {
    private Board board;
    private Path paths[];
    private Worm worms[];
    private Thread threads[];
    private Consumer<Board> onBoardUpdated;
    private Consumer<Path> onPathUpdated;
    private Consumer<Worm> onWormUpdated;

    public Task() {
        board = new SpecificBoard();
        paths = new Path[3];
        worms = new Worm[3];

        paths[0] = new Path(0, new Point(5, 10), "LLLLLUUUUUUUUUURRRRRDDDDDDDDDD");
        paths[0].movePosition(13);
        worms[0] = new Worm(0, board, paths[0], 6);
        worms[0].setSpeed(5);

        paths[1] = new Path(1, new Point(5, 10), "UUUUUUUUUURRRRRDDDDDDDDDDLLLLL");
        paths[1].movePosition(-7);
        worms[1] = new Worm(1, board, paths[1], 9);
        worms[1].setSpeed(4);

        paths[2] = new Path(2, new Point(5, 10), "RRRDDDDDDDLLLLLLUUUUUUURRR");
        paths[2].movePosition(-8);
        worms[2] = new Worm(2, board, paths[2], 4);
        worms[2].setSpeed(6);

        board.setOnUpdated(this::onBoardUpdated);
        for (int i = 0; i < 3; i++) {
            paths[i].setOnUpdated(this::onPathUpdated);
            worms[i].setOnUpdated(this::onWormUpdated);
        }
        setPaused(true);
    }

    public void setOnBoardUpdated(Consumer<Board> onBoardUpdated) {
        this.onBoardUpdated = onBoardUpdated;
        onBoardUpdated(board);
    }

    public void setOnPathUpdated(Consumer<Path> onPathUpdated) {
        this.onPathUpdated = onPathUpdated;
        for (Path path : paths) {
            onPathUpdated(path);
        }
    }

    public void setOnWormUpdated(Consumer<Worm> onWormUpdated) {
        this.onWormUpdated = onWormUpdated;
        for (Worm worm : worms) {
            onWormUpdated(worm);
        }
    }

    private void onBoardUpdated(Board board) {
        if (onBoardUpdated != null) {
            onBoardUpdated.accept(board);
        }
    }

    private void onPathUpdated(Path path) {
        if (onPathUpdated != null) {
            onPathUpdated.accept(path);
        }
    }

    private void onWormUpdated(Worm worm) {
        if (onWormUpdated != null) {
            onWormUpdated.accept(worm);
        }
    }

    public void setPaused(boolean paused) {
        for (int i = 0; i < worms.length; i++) {
            worms[i].setPaused(paused);
        }
    }

    public void setWormSpeed(int id, double speed) {
        worms[id].setSpeed(speed);
    }

    public double getWormSpeed(int id) {
        return worms[id].getSpeed();
    }


    public void terminate() {
        for (int i = 0; i < worms.length; i++) {
            if (worms[i] != null) {
                worms[i].terminate();
            }
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                if (threads[i] != null) {
                    threads[i].join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        worms = null;
        threads = null;
    }

    public void start() {
        threads = new Thread[3];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(worms[i], "W-" + i);
            threads[i].start();
        }
    }
}
