package org.game;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * The {@code Game} class manages the main logic and lifecycle of the MatrixConcurrent game.
 * It is responsible for initializing the board, starting the threads that control the A and B players,
 * displaying the board state, checking for win/loss conditions, and coordinating synchronization using a {@link CyclicBarrier}.
 *
 * The game ends under any of the following conditions:
 * - A reaches a phone (A wins).
 * - A is caught by any B (B wins).
 * - A cannot reach any phone (draw/game over).
 */
public class Game {

    /** The shared game board. */
    private final Board board = new Board();

    /** The thread responsible for controlling the A player. */
    private APlayerThread aThread;

    /** Threads responsible for controlling each B player. */
    private final List<BPlayerThread> bThreads = new ArrayList<>();

    /** Synchronization barrier to coordinate turn-based movement. */
    private final CyclicBarrier barrier;

    /**
     * Constructs the game with the provided synchronization barrier.
     *
     * @param barrier the CyclicBarrier used to synchronize thread movement
     */
    public Game(CyclicBarrier barrier){
        this.barrier = barrier;
    }

    /**
     * Starts the game by:
     * - Initializing the board and entities.
     * - Spawning and starting threads for A and B players.
     * - Displaying the board and running the game loop until a win/loss condition is met.
     *
     * @throws InterruptedException if thread sleep is interrupted
     * @throws BrokenBarrierException if the barrier is broken during synchronization
     */
    public void start() throws InterruptedException, BrokenBarrierException {
        board.initialize();
        board.display();

        // Create and start A thread
        aThread = new APlayerThread(board, board.phones, barrier);

        // Create and start B threads
        for (Entity b : board.bPlayers) {
            bThreads.add(new BPlayerThread(board, b, barrier));
        }

        aThread.start();
        for (BPlayerThread bt : bThreads) bt.start();

        // Game loop
        while (true) {
            barrier.await();  // wait before printing
            Thread.sleep(2000);

            synchronized (board) {
                System.out.println("\nEstado del tablero:");
                board.display();

                // Check for end conditions
                if (hasWin() || isCaught() || !hasPathToPhone()) {
                    if (hasWin()) {
                        System.out.println("A ha llegado a un teléfono. ¡A gana!");
                    } else if (isCaught()) {
                        System.out.println("B ha atrapado a A. ¡B gana!");
                    } else {
                        System.out.println("A no tiene camino hacia ningún teléfono. Juego terminado.");
                    }
                    break;
                }
            }

            barrier.await();  // wait after processing
        }

        stopThreads();
        System.exit(0);
    }

    /**
     * Checks if A-player has reached any phone.
     *
     * @return true if A has won; false otherwise
     */
    private boolean hasWin() {
        for (Position phone : board.phones) {
            if (board.aPlayer.position.equals(phone)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if A-player has been caught by any B-player.
     *
     * @return true if A is caught; false otherwise
     */
    private boolean isCaught() {
        for (Entity b : board.bPlayers) {
            if (b.position.equals(board.aPlayer.position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether there is a valid path from A-player to any phone.
     *
     * @return true if a path exists; false if A is blocked
     */
    private boolean hasPathToPhone() {
        return GameUtils.findNextMoveTowards(board, board.aPlayer.position, board.phones) != null;
    }

    /**
     * Signals all entity threads to stop running.
     */
    private void stopThreads() {
        aThread.stopRunning();
        for (BPlayerThread bt : bThreads) bt.stopRunning();
    }
}
