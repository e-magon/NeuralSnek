package com.magube.neuralsnek.snake;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class JTela extends JPanel {
    int blockSize = 20;
    int actualBlockSize = blockSize - 1;
    
    int telaH;
    int telaW;

    public void drawBlock(Graphics g, int corX, int corY, Color color) {
        g.setColor(color);
        g.fillRect(corX - actualBlockSize / 2, corY - actualBlockSize / 2, actualBlockSize, actualBlockSize);
    }

    public void drawLine(Graphics g, int corX, int corY, int widht, int height, Color color) {
        g.setColor(color);
        g.fillRect(corX, corY, widht, height);
    }
    private void drawHLine(int x, int y, Color color) {
        
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
        for (int k=0; k<telaH; k+=blockSize) {
            drawLine(getGraphics(), 0, k, telaW, 1, Color.DARK_GRAY);
        }
    }

    
    public void setTelaH(int telaH) {
        this.telaH = telaH;
    }

    public void setTelaW(int telaW) {
        this.telaW = telaW;
    }
}
