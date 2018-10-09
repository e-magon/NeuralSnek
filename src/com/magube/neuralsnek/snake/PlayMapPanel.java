package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PlayMapPanel extends JPanel {

    private int telaW;
    private int telaH;

    private SnakePlayer player;
    private Apple apple;

    private final int blockSize;
    private final int actualBlockSize;
    private final Color headColor;
    private final Color bodyColor;
    private final Color appleColor;
    private final Color gridColor;
    private final Color bgColor;

    private int[] cord;

    public PlayMapPanel() {
        blockSize = 20;
        actualBlockSize = blockSize - 1;

        headColor = Color.GREEN;
        bodyColor = new Color(0, 180, 0); //Verde scuro
        appleColor = Color.RED;
        gridColor = Color.BLACK;
        bgColor = Color.GRAY;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        telaW = this.getWidth();
        telaH = this.getHeight();
        //System.out.println(telaW + " - " + telaH);

        this.setBackground(bgColor);

        //Creazione linee verticali
        //Ogni 20 pixel, una riga
        for (int k = 0; k < telaW; k += blockSize) {
            drawLine(g, k, 0, 1, telaH, gridColor);
        }

        //Creazione linee orizzontali
        //Ogni 20 pixel, una riga
        for (int k = 0; k < telaH; k += blockSize) {
            drawLine(g, 0, k, telaW, 1, gridColor);
        }

        if (player != null) {
            int thisX, thisY;

            //Disegno del serpente
            for (int thisBlock = 0; thisBlock < player.getSnakePlayer().size(); thisBlock++) {
                thisX = player.getSnakePlayer().get(thisBlock)[0];
                thisY = player.getSnakePlayer().get(thisBlock)[1];
                cord = getPixelCoords(thisX, thisY);

                if (thisBlock == 0) {
                    drawBlock(g, cord[0], cord[1], headColor);
                } else {
                    drawBlock(g, cord[0], cord[1], bodyColor);
                }
            }

            //Disegno della mela
            if (apple != null && apple.getCoord() != null) {
                cord = getPixelCoords(apple.getCoord()[0], apple.getCoord()[1]);
                drawBlock(g, cord[0], cord[1], appleColor);
            }
        }
    }

    public void drawLine(Graphics g, int corX, int corY, int widht, int height, Color color) {
        g.setColor(color);
        g.fillRect(corX, corY, widht, height);
    }

    public void drawBlock(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x - actualBlockSize / 2, y - actualBlockSize / 2, actualBlockSize, actualBlockSize);
    }

    /**
     * Metodo che trasforma le coordinate "astratte" della griglia di gioco in
     * vere e proprie coordinate in pixel. Usato per disegnare i blocchi nella
     * griglia di gioco.
     *
     * @param x coordinata x della griglia di gioco (parte da 1, a sinistra)
     * @param y coordinata y della criglia di gioco (parte da 1, in alto)
     * @return array di int da 2 celle contenente la x e la y in pixel del
     * centro del blocco specificato in input
     */
    public int[] getPixelCoords(int x, int y) {
        return new int[]{(x * blockSize) - 10, (y * blockSize) - 10};
    }

    public void setPlayer(SnakePlayer player) {
        this.player = player;
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    public int getTelaW() {
        return telaW;
    }

    public int getTelaH() {
        return telaH;
    }

    public int getBlockSize() {
        return blockSize;
    }
}
