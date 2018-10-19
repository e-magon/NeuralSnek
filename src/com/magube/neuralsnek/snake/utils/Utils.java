package com.magube.neuralsnek.snake.utils;

import com.magube.neuralsnek.snake.GameWindow;
import java.util.Random;

public class Utils {

    private static Random rn = new Random();

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            //ex.printStackTrace(System.err);
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
        int x = rn.nextInt(maxX) + 1;
        int y = rn.nextInt(maxY) + 1;

        return new int[]{x, y};
    }
    
    /**
     * Ritorna un double casuale tra il min e max
     * @param min
     * @param max
     * @return double generato a caso
     */
    public static double randomDouble(double min, double max) {
        double random = new Random().nextDouble();
        return min + (random * (max - min));
    }
    
    /**
     * Prende il valore di una distanza e ritorna un valore tra 0 e 1 in base a
     * quanto è vicino. Come range usa la larghezza ed altezza del canvas
     *
     * @return 1 se è la cella successiva, 0.1 se è molto lontano, ecc
     */
    public static double map(int distanza, boolean verticale, GameWindow game) {
        double valore;
        if (verticale) {
            valore = (double) distanza / (game.getCanvas().getHeight() / game.getCanvas().getBlockSize());
//            System.out.printf("Ho fatto %.2f / %.3f che fa %.2f\n", (double) distanza, (double) (gameWindow.getCanvas().getHeight() / gameWindow.getCanvas().getBlockSize()), valore);
        } else {
            valore = (double) distanza / (game.getCanvas().getWidth() / game.getCanvas().getBlockSize());
//            System.out.printf("Ho fatto %.2f / %.3f che fa %.2f\n\n", (double) distanza, (double) (gameWindow.getCanvas().getWidth() / gameWindow.getCanvas().getBlockSize()), valore);
        }

        return 1 - valore;
    }
}
