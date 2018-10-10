package com.magube.neuralsnek.snake.OLD;

import java.awt.Color;
import java.util.ArrayList;

public class GameThread extends Thread {
    private final JTela tela;
    int telaW;
    int telaH;
    int blockSize;

    Color headColor = Color.GREEN;
    Color bodyColor = new Color(0, 180, 0);
    Color appleColor = Color.RED;

    //Matrice che rappresenta le coordinate del serpente.
    //Ogni riga della matrice è un blocco del serpente, e le due colonne le coordinate x ed y
    ArrayList<int[]> snakePlayer = new ArrayList<>(0);

    private int[] getPixelCoords(int x, int y) {
        return new int[]{(x * blockSize) - 10, (y * blockSize) - 10};
    }

    public GameThread(final JTela tela, int telaW, int telaH, int blockSize) {
        this.tela = tela;
        this.telaW = telaW;
        this.telaH = telaH;
        this.blockSize = blockSize;

        snakePlayer.add(new int[]{17, 12});
        snakePlayer.add(new int[]{18, 12});
        snakePlayer.add(new int[]{19, 12});

        tela.setBackground(Color.LIGHT_GRAY);
    }

    private void drawBlock(int x, int y, Color color) {
        tela.drawBlock(tela.getGraphics(), x, y, color);
    }

    public void drawSnake() {
        int[] coords;
        int[] blocco;

        for (int k = 0; k < snakePlayer.size(); k++) {
            blocco = snakePlayer.get(k);
            coords = getPixelCoords(blocco[0], blocco[1]);

            if (k == 0) {
                drawBlock(coords[0], coords[1], headColor);
            } else {
                drawBlock(coords[0], coords[1], bodyColor);
            }
        }
    }

    @Override
    public void run() {
        
    }

    public synchronized void moveSnake(int direzione) {
        //0 alto, 1 destra, 2 basso, 3 sinistra
        switch (direzione) {
            case 0:
                System.out.println("Vado su");
                break;
            case 1:
                System.out.println("Vado destra");
                break;
            case 2:
                System.out.println("Vado giù");
                break;
            case 3:
                System.out.println("Vado sinistra");
        }
    }
}