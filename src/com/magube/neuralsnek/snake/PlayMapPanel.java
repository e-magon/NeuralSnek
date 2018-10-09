package com.magube.neuralsnek.snake;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PlayMapPanel extends JPanel {

    private int telaW;
    private int telaH;

    private final int blockSize;
    private final int actualBlockSize;
    private final Color headColor;
    private final Color bodyColor;
    private final Color appleColor;
    private final Color gridColor;
    private final Color bgColor;

    public PlayMapPanel() {
        blockSize = 20;
        actualBlockSize = blockSize - 1;
        
        headColor = Color.GREEN;
        bodyColor = new Color(0, 180, 0); //Verde scuro
        appleColor = Color.RED;
        gridColor = Color.BLACK;
        bgColor = Color.LIGHT_GRAY;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        telaW = this.getWidth();
        telaH = this.getHeight();
        System.out.println(telaW + " - " + telaH);

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
        
        drawBlock(g, 30, 30, headColor);
        drawBlock(g, 30, 50, bodyColor);
        drawBlock(g, 30, 70, bodyColor);
        drawBlock(g, 30, 90, bodyColor);
    }

    public void drawLine(Graphics g, int corX, int corY, int widht, int height, Color color) {
        g.setColor(color);
        g.fillRect(corX, corY, widht, height);
    }

    public void drawBlock(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x - actualBlockSize / 2, y - actualBlockSize / 2, actualBlockSize, actualBlockSize);
    }
}
