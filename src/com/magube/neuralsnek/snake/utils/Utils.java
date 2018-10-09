package com.magube.neuralsnek.snake.utils;

import java.util.Random;

public class Utils {

    private static Random rn = new Random();

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Ritorna una posizione casuale. Usato per generare la posizione delle mele
     *
     * @param maxX blocco massimo su x
     * @param maxY blocco massimo su y
     * @return array di coordinate x, y generate
     */
    public static int[] randomPos(int maxX, int maxY) {
        int x = rn.nextInt(maxX + 1) + 1;
        int y = rn.nextInt(maxY + 1) + 1;

        return new int[]{x, y};
    }
}
