package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.Utils;
import javax.swing.JLabel;

public class GameThread extends Thread {

    private final PlayMapPanel canvas;
    private final SnakePlayer player;
    private final Apple apple;

    private boolean moved;
    private final int TARGET_FPS;
    private int punteggio;

    private final JLabel labelPerso;
    private final JLabel labelPunti;

    public GameThread(PlayMapPanel canvas, SnakePlayer player, Apple apple,
            JLabel labelPunti, JLabel labelPerso) {
        this.canvas = canvas;
        this.player = player;
        this.apple = apple;
        this.labelPerso = labelPerso;
        this.labelPunti = labelPunti;

        punteggio = 0;

        canvas.setApple(apple);
        canvas.setPlayer(player);

        moved = false;
        TARGET_FPS = 7;
    }

    @Override
    public void run() {
        Utils.sleep(150); //Aspetta l'inizializzazione del player, altrimenti perde subito

        int maxWidth = canvas.getTelaW() / canvas.getBlockSize();
        int maxHeight = canvas.getTelaH() / canvas.getBlockSize();

        apple.setMaxW(maxWidth);
        apple.setMaxH(maxHeight);
        apple.newCoord();

        boolean perso = false;

        while (!perso) {
            boolean snakeHit = player.checkCollision();
            int[] nextPos = player.muovi();

            boolean isXok = (nextPos[0] > 0) && (nextPos[0] <= maxWidth);
            boolean isYok = (nextPos[1] > 0) && (nextPos[1] <= maxHeight);

            if (!(isXok && isYok) || snakeHit) {
                labelPerso.setVisible(true);
                perso = true;
            } else {
                int appleX = apple.getCoords()[0];
                int appleY = apple.getCoords()[1];

                if (nextPos[0] == appleX && nextPos[1] == appleY) {
                    System.out.println("Mela mangiata");
                    punteggio++;
                    labelPunti.setText("Punteggio: " + punteggio);
                    player.addBlock();
                    apple.newCoord();
                }

                canvas.repaint();
                moved = false;
            }

            Utils.sleep((int) ((1f / TARGET_FPS) * 1000));
        }
    }

    public void move(int direzione) {
        if (!moved) {
            //Se la prossima direzione è l'opposto di quella attuale, non muoverti
            if (player.getDirezioneTesta() != (direzione + 2) % 4) {
                player.setDirezioneTesta(direzione);
                moved = true;
            }
        }
    }
}
