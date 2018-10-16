package com.magube.neuralsnek.snake.brain;

import com.magube.neuralsnek.brain.manager.NNManager;
import com.magube.neuralsnek.snake.GameWindow;
import com.magube.neuralsnek.snake.utils.Utils;

public class SnekNN extends Thread {

    NNManager nnManager;
    GameWindow gameWindow;

    private final int numCreature;
    private final int inputNeu;
    private final int hidLayNum;
    private final int hidNeuNum;
    private final int outputNeu;
    private final double soglie;

    public SnekNN() {
        numCreature = 1;
        inputNeu = 8;
        hidLayNum = 2;
        hidNeuNum = 8;
        outputNeu = 4;
        soglie = 0.5;

        nnManager = new NNManager(false, numCreature, inputNeu, hidLayNum, hidNeuNum, outputNeu, soglie);

        gameWindow = new GameWindow(false);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setTitle("Snake - Addestra");
        gameWindow.setVisible(true);
    }

    @Override
    public void run() {
        nnManager.creaReti();
        gameWindow.getGameThread().start();

        while (true) {
            gameWindow.getGameThread().setPronto(true);
            Utils.sleep(1000);

            int playerX = gameWindow.getPlayer().getCoords().get(0)[0];
            int playerY = gameWindow.getPlayer().getCoords().get(0)[1];

            int melaX = gameWindow.getApple().getCoords()[0];
            int melaY = gameWindow.getApple().getCoords()[1];

            int appleDistX = melaX - playerX;
            int appleDistY = melaY - playerY;

            double valoreMelaX = map(appleDistX, false);
            double valoreMelaY = map(appleDistY, true);
            
            System.out.println(valoreMelaY);

            double[] input = new double[8];
            if (valoreMelaY < 0) {
                input[0] = -appleDistY;
                input[2] = 0d;
            } else {
                input[0] = 0d;
                input[2] = appleDistY;
            }
            
            if (valoreMelaX < 0) {
                input[3] = -appleDistX;
                input[1] = 0d;
            } else {
                input[3] = 0d;
                input[1] = appleDistX;
            }

            nnManager.setInput(input);
            //for (double k : input)
                //System.out.println(k + " ");
            //System.out.println("\n\n\n");
        }
    }

    /**
     * Prende il valore di una distanza e ritorna un valore tra 0 e 1 in base a
     * quanto è vicino. Come range usa la larghezza ed altezza del canvas
     *
     * @return 1 se è la cella successiva, 0.1 se è molto lontano, ecc
     */
    public double map(int distanza, boolean verticale) {
        double valore;
        if (verticale) {
            valore = (double) distanza / (gameWindow.getCanvas().getHeight() / gameWindow.getCanvas().getBlockSize());
        } else {
            valore = (double) distanza / (gameWindow.getCanvas().getWidth() / gameWindow.getCanvas().getBlockSize());
        }

        return 1 - valore;
    }
}
