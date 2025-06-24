package org.game;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class Game {
    private final Board board = new Board();
    private APlayerThread aThread;
    private final List<BPlayerThread> bThreads = new ArrayList<>();
    private final CyclicBarrier barrier;

    public Game(CyclicBarrier barrier){
        this.barrier = barrier;
    }

    public void start() throws InterruptedException {
        board.initialize();
        board.display();

        aThread = new APlayerThread(board, board.phones, barrier);
        for (Entity b : board.bPlayers) {
            bThreads.add(new BPlayerThread(board, b, barrier));
        }

        aThread.start();
        for (BPlayerThread bt : bThreads) bt.start();

        while (true) {
            Thread.sleep(2000);
            synchronized (board) {
                System.out.println("\nEstado del tablero:");
                board.display();

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


        }


        stopThreads();
    }

    private boolean hasWin() {
        for (Position phone : board.phones) {
            if (board.aPlayer.position.equals(phone)) {
                System.out.println("A ha llegado a un teléfono. ¡A gana!");
                return true;
            }
        }
        return false;
    }

    private boolean isCaught() {
        for (Entity b : board.bPlayers) {
            if (b.position.equals(board.aPlayer.position)) {
                System.out.println("B ha atrapado a A. ¡B gana!");
                return true;
            }
        }
        return false;
    }

    private boolean hasPathToPhone() {
        return GameUtils.findNextMoveTowards(board, board.aPlayer.position, board.phones) != null;
    }

    private void stopThreads() {
        aThread.stopRunning();
        for (BPlayerThread bt : bThreads) bt.stopRunning();
    }
}
