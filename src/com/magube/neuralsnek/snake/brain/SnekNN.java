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
            
            
            Utils.sleep(500);
            gameWindow.getGameThread().setPronto(true);
        }
    }
}
