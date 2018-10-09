package com.magube.neuralsnek.snake.utils;

public class Apple {

    private int maxW, maxH;
    private int[] coord;

    public void newCoord() {
        coord = Utils.randomPos(maxW, maxH);
        System.out.println("Disegno la mela a " + coord[0] + " " + coord[1]);
    }

    public int[] getCoord() {
        return coord;
    }
    

    public void setMaxW(int maxW) {
        this.maxW = maxW;
    }

    public void setMaxH(int maxH) {
        this.maxH = maxH;
    }
}
