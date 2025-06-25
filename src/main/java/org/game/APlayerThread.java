package org.game;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * This class represents a thread responsible for controlling the 'A' player in the MatrixConcurrent game.
 * The player attempts to reach one of the available phone positions (goals) on the board.
 *
 * Each movement cycle is synchronized with other entity threads using a CyclicBarrier to ensure
 * all entities move in coordinated steps.
 */
public class APlayerThread extends Thread {

    /** Reference to the shared game board. */
    private final Board board;

    /** List of goal positions the A player tries to reach (typically phones). */
    private final List<Position> goals;

    /** Synchronization barrier used to coordinate movement among multiple threads. */
    private final CyclicBarrier barrier;

    /** Flag to control the thread's running state. */
    private volatile boolean running = true;

    /**
     * Constructs a thread to control the A player.
     *
     * @param board   the game board shared among all entities
     * @param goals   list of goal positions (e.g., phones)
     * @param barrier synchronization barrier for coordinating turns
     */
    public APlayerThread(Board board, List<Position> goals, CyclicBarrier barrier) {
        this.board = board;
        this.goals = goals;
        this.barrier = barrier;
    }

    /**
     * Runs the thread loop, where the A player attempts to move toward the closest reachable goal.
     * The thread waits at the barrier twice: once before moving, and once after.
     * Each cycle pauses for 2 seconds to allow the board state to be observed.
     */
    public void run() {
        while (running) {
            try {
                // Wait for other entities before moving
                barrier.await();

                // Calculate and perform the next move, synchronized on the board
                synchronized (board) {
                    Position next = GameUtils.findNextMoveTowards(board, board.aPlayer.position, goals);
                    if (next != null) {
                        board.moveEntity(board.aPlayer, next);
                    }
                }

                // Wait again to allow all entities to complete their moves before displaying
                barrier.await();

                // Pause to slow down the simulation visually
                Thread.sleep(2000);
            } catch (Exception ignored) {
                // Exceptions (e.g., BrokenBarrierException) are ignored in this simulation context
            }
        }
    }

    /**
     * Signals the thread to stop running.
     */
    public void stopRunning() {
        running = false;
    }
}
