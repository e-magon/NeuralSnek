package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.Utils;
import javax.swing.JLabel;

public class GameThread extends Thread {

    private final PlayMapPanel canvas;
    private final SnakePlayer player;
    private final Apple apple;

    private boolean moved;
    private boolean ready;
    private final int TARGET_FPS;
    private final boolean playable;
    private int punteggio;

    private boolean perso;

    private final JLabel labelPerso;
    private final JLabel labelPunti;

    public GameThread(PlayMapPanel canvas, SnakePlayer player, Apple apple,
            JLabel labelPunti, JLabel labelPerso, boolean playable) {
        this.canvas = canvas;
        this.player = player;
        this.apple = apple;
        this.labelPerso = labelPerso;
        this.labelPunti = labelPunti;
        this.playable = playable;

        punteggio = 0;

        canvas.setApple(apple);
        canvas.setPlayer(player);

        moved = false;
        TARGET_FPS = 7;

        ready = playable;
        perso = false;
    }

    @Override
    public void run() {
        Utils.sleep(200); //Aspetta l'inizializzazione del player, altrimenti perde subito

//        int maxWidth = canvas.getWidth() / canvas.getBlockSize();
//        int maxHeight = canvas.getHeight() / canvas.getBlockSize();
        int maxWidth = 35, maxHeight = 24;

        apple.setMaxW(maxWidth);
        apple.setMaxH(maxHeight);
        apple.newCoord();
        //apple.setCoords(new int[]{15, 7});    //PER DEBUG

        while (!perso) {
            if (!ready) {
                Utils.sleep(1);
                continue;
            }

            boolean snakeHit = player.checkCollision();
            int[] nextPos = player.muovi();

            boolean isXok = (nextPos[0] > 0) && (nextPos[0] <= maxWidth);
            boolean isYok = (nextPos[1] > 0) && (nextPos[1] <= maxHeight);

            if (!(isXok && isYok) || snakeHit) {
                System.err.println("Ho perso perchè sono andato a x, y: " + nextPos[0] + " " + nextPos[1]);
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

            if (playable) {
                Utils.sleep((int) ((1f / TARGET_FPS) * 1000));
            } else {
                ready = false;
            }
        }

        labelPerso.setVisible(true);
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

    public boolean isPronto() {
        return ready;
    }

    public void setPronto(boolean pronto) {
        this.ready = pronto;
    }

    public boolean isPerso() {
        return perso;
    }

    public void setPerso(boolean perso) {
        this.perso = perso;
    }

    public int getPunteggio() {
        return punteggio;
    }
}
