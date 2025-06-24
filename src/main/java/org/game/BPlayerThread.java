package org.game;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BPlayerThread extends Thread {
    private final Board board;
    private final Entity bEntity;
    private final CyclicBarrier barrier;
    private volatile boolean running = true;

    public BPlayerThread(Board board, Entity bEntity, CyclicBarrier barrier) {
        this.board = board;
        this.bEntity = bEntity;
        this.barrier = barrier;
    }

    public void run() {
        while (running) {
            try {
                barrier.await();
                //Position target = board.aPlayer.position;
                Position target = new Position(board.aPlayer.position.row,board.aPlayer.position.col);
                Position next = GameUtils.findNextMoveTowards(board, bEntity.position, List.of(target));

                if (next != null) {
                    synchronized (board) {
                        board.moveEntity(bEntity, next);
                    }
                }


                Thread.sleep(2000);
                barrier.await();
            } catch (Exception ignored) {}
        }
    }

    public void stopRunning() {
        running = false;
    }
}
