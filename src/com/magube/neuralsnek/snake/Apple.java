package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.Utils;
import java.util.ArrayList;

public class Apple {

    private int maxW, maxH;
    private int[] coords;
    private ArrayList<int[]> playerCoords;

    public void newCoord() {
        boolean trovato = false;

        while (!trovato) {
            coords = Utils.randomPos(maxW, maxH);
            trovato = true;

            for (int[] thisBlock : playerCoords) {
                if (thisBlock[0] == coords[0] && thisBlock[1] == coords[1]) {
                    //Non va bene, il blocco è occupato da snek
                    trovato = false;
                    break;
                }
            }
        }

        System.out.println("Disegno la mela a " + coords[0] + " " + coords[1]);
    }

    public int[] getCoords() {
        return coords;
    }

    public void setMaxW(int maxW) {
        this.maxW = maxW;
    }

    public void setMaxH(int maxH) {
        this.maxH = maxH;
    }

    public ArrayList<int[]> getPlayerCoords() {
        return playerCoords;
    }

    public void setPlayerCoords(ArrayList<int[]> playerCoords) {
        this.playerCoords = playerCoords;
    }
}
