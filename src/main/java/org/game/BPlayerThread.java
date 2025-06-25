package org.game;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * This class represents a thread responsible for controlling a B-player (enemy) in the MatrixConcurrent game.
 * Each B-player moves independently, attempting to reach and catch the A-player.
 *
 * The B-player calculates its path to the A-player's current position using a pathfinding utility,
 * and all B-threads synchronize their moves with other threads (including A) via a {@link CyclicBarrier}.
 */
public class BPlayerThread extends Thread {

    /** Reference to the shared game board. */
    private final Board board;

    /** The specific B-player entity controlled by this thread. */
    private final Entity bEntity;

    /** Barrier used to synchronize all entities’ movement steps. */
    private final CyclicBarrier barrier;

    /** Flag that controls the execution of the thread. */
    private volatile boolean running = true;

    /**
     * Constructs a new B-player thread.
     *
     * @param board   the shared game board
     * @param bEntity the B-player entity to control
     * @param barrier the barrier used for thread synchronization
     */
    public BPlayerThread(Board board, Entity bEntity, CyclicBarrier barrier) {
        this.board = board;
        this.bEntity = bEntity;
        this.barrier = barrier;
    }

    /**
     * Executes the thread's logic in a loop:
     * - Waits for all threads to sync at the barrier.
     * - Calculates the next position toward the A-player.
     * - Moves the B-player if a valid move is found.
     * - Waits again to allow board visualization.
     * - Sleeps for 2 seconds before the next cycle.
     */
    public void run() {
        while (running) {
            try {
                // Wait for other entities to reach the barrier
                barrier.await();

                // Attempt to move toward A-player, synchronized to avoid race conditions
                synchronized (board) {
                    Position target = new Position(board.aPlayer.position.row, board.aPlayer.position.col);
                    Position next = GameUtils.findNextMoveTowards(board, bEntity.position, List.of(target));

                    if (next != null) {
                        board.moveEntity(bEntity, next);
                    }
                }

                // Sleep to slow down movement for observation
                Thread.sleep(2000);

                // Wait again before the next round
                barrier.await();
            } catch (Exception ignored) {
                // Barrier or interruption exceptions are ignored in simulation context
            }
        }
    }

    /**
     * Stops the execution of this B-player thread.
     */
    public void stopRunning() {
        running = false;
    }
}
