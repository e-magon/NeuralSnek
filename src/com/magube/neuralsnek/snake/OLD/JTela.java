package com.magube.neuralsnek.snake.OLD;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class JTela extends JPanel {

    private final int blockSize = 20;
    private final int actualBlockSize = blockSize - 1; //Non considera i bordi creati dalla griglia

    private int telaH;
    private int telaW;

    private Runnable drawSnekCallback = null;

    
    public void drawBlock(Graphics g, int corX, int corY, Color color) {
        g.setColor(color);
        g.fillRect(corX - actualBlockSize / 2, corY - actualBlockSize / 2, actualBlockSize, actualBlockSize);
    }

    public void drawLine(Graphics g, int corX, int corY, int widht, int height, Color color) {
        g.setColor(color);
        g.fillRect(corX, corY, widht, height);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //Creazione linee verticali
        //Ogni 20 pixel, una riga
        for (int k = 0; k < telaW; k += blockSize) {
            drawLine(getGraphics(), k, 0, 1, telaH, Color.DARK_GRAY);
        }

        //Creazione linee orizzontali
        //Ogni 20 pixel, una riga
        for (int k = 0; k < telaH; k += blockSize) {
            drawLine(getGraphics(), 0, k, telaW, 1, Color.DARK_GRAY);
        }

        //dopo aver creato la griglia, disegna il serpente
        drawSnekCallback.run();
    }

    public void setTelaH(int telaH) {
        this.telaH = telaH;
    }

    public void setTelaW(int telaW) {
        this.telaW = telaW;
    }

    public void setDrawSnekCallback(Runnable drawSnekCallback) {
        this.drawSnekCallback = drawSnekCallback;
    }
}
