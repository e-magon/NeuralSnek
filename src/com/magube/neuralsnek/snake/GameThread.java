package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.*;
import javax.swing.JLabel;

public class GameThread extends Thread {

    private final PlayMapPanel canvas;
    private final SnakePlayer player;
    private Apple apple;

    Boolean isPerso;

    boolean moved = false;
    private final int targetFps;

    private final JLabel labelPerso;

    public GameThread(PlayMapPanel canvas, SnakePlayer player, Apple apple, Boolean isPerso, JLabel labelPerso) {
        this.canvas = canvas;
        this.player = player;
        this.apple = apple;
        this.isPerso = isPerso;
        this.labelPerso = labelPerso;

        canvas.setApple(apple);
        canvas.setPlayer(player);

        targetFps = 10;
    }

    @Override
    public void run() {
        Utils.sleep(200); //Aspetta l'inizializzazione del player, altrimenti da subito perso
        int maxWidth = canvas.getTelaW() / canvas.getBlockSize();
        int maxHeight = canvas.getTelaH() / canvas.getBlockSize();
        apple.setMaxW(maxWidth);
        apple.setMaxH(maxHeight);
        apple.newCoord();
        isPerso = false;

        while (!isPerso) {
            boolean snakeHit = player.checkMove();
            int[] nextPos = player.muovi();

            boolean isXok = (nextPos[0] > 0) && (nextPos[0] <= maxWidth);
            boolean isYok = (nextPos[1] > 0) && (nextPos[1] <= maxHeight);

            if (!(isXok && isYok) || snakeHit) {
                labelPerso.setVisible(true);
                isPerso = true;
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
