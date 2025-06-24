package org.game;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class APlayerThread extends Thread {
    private final Board board;
    private final List<Position> goals;
    private final CyclicBarrier barrier;
    private volatile boolean running = true;

    public APlayerThread(Board board, List<Position> goals, CyclicBarrier barrier) {
        this.board = board;
        this.goals = goals;
        this.barrier = barrier;
    }

    public void run() {
        while (running) {
            try {
                barrier.await();
                Position next = GameUtils.findNextMoveTowards(board, board.aPlayer.position, goals);
                if (next != null) {
                    synchronized (board) {
                        board.moveEntity(board.aPlayer, next);
                    }
                }


                barrier.await();
                Thread.sleep(2000);
            } catch (Exception ignored) {}
        }
    }

    public void stopRunning() {
        running = false;
    }
}
