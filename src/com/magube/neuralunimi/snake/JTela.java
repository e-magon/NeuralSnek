package com.magube.neuralunimi.snake;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class JTela extends JPanel {
    int sizeColore = 20;
    
    public void init(Graphics g) {
        super.paintComponent(g);
    }
    
    public void drawBlock(Graphics g, int corX, int corY, Color color) {
        g.setColor(color);
        g.fillRect(corX-sizeColore/2, corY-sizeColore/2, sizeColore, sizeColore);
    }
    
    public void drawLine(Graphics g, int corX, int corY, int widht, int height, Color color) {
        g.setColor(color);
        g.fillRect(corX, corY, widht, height);
    }
}