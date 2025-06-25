package org.game;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Entry point for the MatrixConcurrent game.
 *
 * This class initializes the synchronization barrier and starts the game loop.
 * The barrier is configured to wait for 4 threads: 1 A-player thread, 2 B-player threads,
 * and the main thread (used for printing and logic coordination).
 */
public class Main {

    /**
     * Main method that launches the game.
     *
     * @param args command-line arguments (not used)
     * @throws InterruptedException if the main thread is interrupted
     * @throws BrokenBarrierException if the synchronization barrier is broken
     */
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        // Create a barrier for 4 parties (A, B1, B2, and the main thread)
        CyclicBarrier barrier = new CyclicBarrier(4);

        // Initialize and start the game
        Game game = new Game(barrier);
        game.start();
    }
}
