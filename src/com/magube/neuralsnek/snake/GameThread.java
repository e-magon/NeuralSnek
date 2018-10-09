package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.*;
import javax.swing.JLabel;

public class GameThread extends Thread {

    private final PlayMapPanel canvas;
    private final SnakePlayer player;
    private Apple apple;

    private final JLabel labelPerso;

    boolean moved = false;
    private final int targetFps;

    public GameThread(PlayMapPanel canvas, SnakePlayer player, Apple apple, JLabel labelPerso) {
        this.canvas = canvas;
        this.player = player;
        this.apple = apple;
        this.labelPerso = labelPerso;

        canvas.setApple(apple);
        canvas.setPlayer(player);

        targetFps = 8;
    }

    @Override
    public void run() {
        Utils.sleep(200); //Aspetta l'inizializzazione del player, altrimenti da subito perso
        boolean perso = false;
        int maxWidth = canvas.getTelaW() / canvas.getBlockSize();
        int maxHeight = canvas.getTelaH() / canvas.getBlockSize();
        apple.setMaxW(maxWidth);
        apple.setMaxH(maxHeight);
        apple.newCoord();

        while (!perso) {
            int[] nextPos = player.muovi();

            boolean isXok = (nextPos[0] > 0) && (nextPos[0] <= maxWidth);
            boolean isYok = (nextPos[1] > 0) && (nextPos[1] <= maxHeight);

            if (!(isXok && isYok)) {
                labelPerso.setVisible(true);
                perso = true;
            } else {
                int appleX = apple.getCoord()[0];
                int appleY = apple.getCoord()[1];

                if (nextPos[0] == appleX && nextPos[1] == appleY) {
                    System.out.println("Mela mangiata");
                    player.addBlock();
                    apple.newCoord();
                }

                canvas.repaint();
                moved = false;
            }
            Utils.sleep((int) ((1f / targetFps) * 1000));
        }
    }

    public void move(int direzione) {
        if (!moved) {
            //Se la prossima direzione è l'opposto di quella attuale, non muoverti
            if ((direzione + 2) % 4 != player.getDirezioneTesta()) {
                player.setDirezioneTesta(direzione);
                moved = true;
            }
        }
    }
}
