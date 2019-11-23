package pl.ketom.crawlingworms.model;

import java.util.function.Consumer;

public class SpecificBoard implements Board {
    private final int NO_OWNER = -1;
    private final int NO_CHUNK = -1;
    private final int MIDDLE_CHUNK = 3;

    private int[] chunkOwner;
    private int[] chunkAcquiresCounter;
    private int width;
    private int height;

    private Consumer<Board> onUpdated;

    public SpecificBoard() {
        width = 11;
        height = 18;

        chunkOwner = new int[4];
        for (int i = 0; i < chunkOwner.length; i++) {
            chunkOwner[i] = NO_OWNER;
        }

        chunkAcquiresCounter = new int[4];
        for (int i = 0; i < chunkOwner.length; i++) {
            chunkAcquiresCounter[i] = 0;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void acquirePoint(Worm worm, Point point) {
        int requestedChunk = getChunkFromPoint(point);
        if (requestedChunk == NO_CHUNK) {
            return;
        }

        int id = worm.getId();

        // first chunk of path
        int firstChunk = id % 3;

        // second chunk of path
        int secondChunk = (id + 2) % 3;

        // chunk not used in path
        int otherChunk = (id + 1) % 3;

        synchronized (this) {
            while (chunkOwner[requestedChunk] != id) {
                if(!worm.isRunning()) {
                    return;
                }
                if (requestedChunk == firstChunk) {
                    if (chunkOwner[firstChunk] == NO_OWNER) {
                        if (chunkOwner[secondChunk] == NO_OWNER && chunkOwner[MIDDLE_CHUNK] == NO_OWNER) {
                            chunkOwner[firstChunk] = id;
                            chunkOwner[secondChunk] = id;
                            chunkOwner[MIDDLE_CHUNK] = id;
                            continue;
                        } else if (chunkOwner[otherChunk] != NO_OWNER) {
                            chunkOwner[firstChunk] = id;
                            continue;
                        }
                    }
                } else if (requestedChunk == MIDDLE_CHUNK) {
                    if (chunkOwner[MIDDLE_CHUNK] == NO_OWNER && chunkOwner[secondChunk] == NO_OWNER) {
                        chunkOwner[MIDDLE_CHUNK] = id;
                        chunkOwner[secondChunk] = id;
                        continue;
                    }
                } else {
                    throw new RuntimeException("Worm[" + worm.getId() + "] tried to acquire point" + point);
                }

                try {
                    worm.setSleeping(true);
                    wait();
                } catch (InterruptedException e) {
                    // do nothing
                }
            }

            // reserving chunk can make some other chunk possible to reserve
            notifyAll();
        }

        worm.setSleeping(false);

        chunkAcquiresCounter[requestedChunk]++;
        onUpdated();
    }

    public void releasePoint(Worm worm, Point point) {
        int chunk = getChunkFromPoint(point);
        if (chunk == NO_CHUNK) {
            return;
        }

        if (chunkOwner[chunk] != worm.getId()) {
            throw new IllegalArgumentException("Something went wrong!");
        }

        synchronized (this) {
            chunkAcquiresCounter[chunk]--;

            if (chunkAcquiresCounter[chunk] == 0) {
                chunkOwner[chunk] = NO_OWNER;
                notifyAll();
            }
        }
        onUpdated();
    }

    public int getPointOwnerId(Point point) {
        int chunk = getChunkFromPoint(point);
        if (chunk == NO_CHUNK) {
            return NO_OWNER;
        }
        return chunkOwner[chunk];
    }

    // safe to call without synchronization, because it operates on constant values
    private int getChunkFromPoint(Point point) {
        if (point.getX() == 5 && point.getY() >= 0 && point.getY() <= 9) {
            return 0;
        }

        if (point.getX() >= 6 && point.getX() <= 8 && point.getY() == 10) {
            return 1;
        }

        if (point.getX() >= 2 && point.getX() <= 4 && point.getY() == 10) {
            return 2;
        }

        if (point.getX() == 5 && point.getY() == 10) {
            return MIDDLE_CHUNK;
        }

        return NO_CHUNK;
    }

    public void setOnUpdated(Consumer<Board> test) {
        this.onUpdated = test;
        onUpdated();
    }

    private void onUpdated() {
        if (onUpdated != null) {
            onUpdated.accept(this);
        }
    }
}
