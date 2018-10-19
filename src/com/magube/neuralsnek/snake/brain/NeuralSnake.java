package com.magube.neuralsnek.snake.brain;

import com.magube.neuralsnek.brain.components.NNetwork;
import com.magube.neuralsnek.brain.manager.NNManager;
import com.magube.neuralsnek.snake.GameWindow;
import com.magube.neuralsnek.snake.SnakePlayer;
import com.magube.neuralsnek.snake.utils.Utils;
import java.util.Random;

/**
 * Questa classe è una creatura Snake con una rete neurale.
 *
 * @author emanuelemagon
 */
public class NeuralSnake extends Thread {

    NNManager nnManager;
    GameWindow[] gameWindows;

    private int inputNeu;
    private int hidLayNum;
    private int hidNeuNum;
    private int outputNeu;
    private double[] input;
    private double soglie;

    private NNetwork cervello;
    private SnakePlayer corpo;

    private boolean logging;

    public NeuralSnake(int inputNeu, int hidLayNum, int hidNeuNum, int outputNeu, double soglie, boolean logging) {
        this.inputNeu = inputNeu;
        this.hidLayNum = hidLayNum;
        this.hidNeuNum = hidNeuNum;
        this.outputNeu = outputNeu;
        this.soglie = soglie;

        this.logging = logging;
        
        corpo = new SnakePlayer();
        Utils.sleep(300);
    }

    public void creaRete() {
        //Crea tante creature quante sono state dette
        cervello = new NNetwork(inputNeu, hidLayNum, hidNeuNum, outputNeu, logging);

        //Imposta le soglie
        impostaSoglie(cervello);

        //Collega le reti
        cervello.collega();
        
        //genera i pesi
        generaPesi(cervello);

        //Info
        System.out.println(cervello.getInfo());
        System.out.println("---------------");
    }

    private void impostaSoglie(NNetwork cervello) {
        //Per ogni livello (parte da 1 per saltare input che non ha soglie):
        for (int livello = 1; livello < cervello.getTotalLayersNumber(); livello++) {
            //Per ogni neurone di questo livello:
            for (int neu = 0; neu < cervello.getLayerSize(livello); neu++) {
                cervello.setSoglia(livello, neu, soglie);
            }
        }
    }

    public void generaPesi(NNetwork cervello) {
        double min = 0;
        double max = 1;
        double[] randomPesi = null;

        for (int livello = 0; livello < cervello.getTotalLayersNumber(); livello++) {
            //Per ogni livello:
            for (int neu = 0; neu < cervello.getLayerSize(livello); neu++) {
                //Per ogni neurone di questo livello:
                if (livello == 0) {
                    //Per ogni neurone di input:
                    randomPesi = new double[1];
                    randomPesi[0] = Utils.randomDouble(min, max);
                }

                if (livello == 1) {
                    //E' di secondo livello, quindi numero di pesi è uguale a num neuroni di input
                    randomPesi = new double[inputNeu];

                    for (int k = 0; k < inputNeu; k++) {
                        randomPesi[k] = Utils.randomDouble(min, max);
                    }
                } else {
                    randomPesi = new double[hidNeuNum];

                    for (int k = 0; k < hidNeuNum; k++) {
                        randomPesi[k] = Utils.randomDouble(min, max);
                    }
                }

                cervello.setPesi(livello, neu, randomPesi);
            }
        }
    }

    public double[] calcola() {
        //Ritorna un array con il risultato della rete
        double[] risultato = new double[outputNeu];

        //Per ogni neurone di input di questa rete, metti il corrispettivo input
        for (int k = 0; k < input.length; k++) {
            cervello.setValoreInput(k, input[k]);
        }

        //Salva l'elaborazione nella matrice
        risultato = cervello.compute();

        return risultato;
    }

    @Override
    public void run() {
        nnManager.creaReti();
        nnManager.generaPesi();

    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
        //Lo modifica per la rete
        cervello.setLogging(logging);
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double[] input) {
        this.input = input;

    }

    public int getInputNeu() {
        return inputNeu;
    }

    public void setInputNeu(int inputNeu) {
        this.inputNeu = inputNeu;
    }

    public int getHidLay() {
        return hidLayNum;
    }

    public void setHidLay(int hidLay) {
        this.hidLayNum = hidLay;
    }

    public int getHidNeu() {
        return hidNeuNum;
    }

    public void setHidNeu(int hidNeu) {
        this.hidNeuNum = hidNeu;
    }

    public int getOutputNeu() {
        return outputNeu;
    }

    public void setOutputNeu(int outputNeu) {
        this.outputNeu = outputNeu;
    }

    public double getSoglie() {
        return soglie;
    }

    public void setSoglie(double soglie) {
        this.soglie = soglie;
    }

    public int getHidLayNum() {
        return hidLayNum;
    }

    public void setHidLayNum(int hidLayNum) {
        this.hidLayNum = hidLayNum;
    }

    public int getHidNeuNum() {
        return hidNeuNum;
    }

    public void setHidNeuNum(int hidNeuNum) {
        this.hidNeuNum = hidNeuNum;
    }

    public NNetwork getCervello() {
        return cervello;
    }

    public void setCervello(NNetwork cervello) {
        this.cervello = cervello;
    }

    public SnakePlayer getCorpo() {
        return corpo;
    }

    public void setCorpo(SnakePlayer corpo) {
        this.corpo = corpo;
    }
}
