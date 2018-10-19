package com.magube.neuralsnek.snake.brain;

import com.magube.neuralsnek.snake.GameThread;
import com.magube.neuralsnek.snake.GameWindow;
import com.magube.neuralsnek.snake.utils.Utils;
import java.util.ArrayList;
import java.util.Random;

public class SnekManager extends Thread {

    private final int numCreature;
    private final NeuralSnake[] creature;

    private GameWindow gameWindow;

    private GameThread gameThreadPointer;

    public SnekManager() {
        numCreature = 1;
        creature = new NeuralSnake[numCreature];
    }

    @Override
    public void run() {
        int numClassifica = 0;
        int[] classifica = new int[numCreature];

        for (NeuralSnake thisCreatura : creature) {
            thisCreatura = new NeuralSnake(8, 2, 8, 4, 0.5, false);
            thisCreatura.creaRete();
            thisCreatura.getCorpo();

            gameWindow = new GameWindow(false);
            gameWindow.setLocationRelativeTo(null);
            gameWindow.setTitle("Snake - Addestra");
            gameWindow.setVisible(true);
            gameThreadPointer = gameWindow.getGameThread();

            gameThreadPointer.start();
            Utils.sleep(300);
            gameThreadPointer.setPronto(true);
            Utils.sleep(300);
            gameThreadPointer.setPronto(true);
            Utils.sleep(300);
            gameThreadPointer.setPronto(true);

            //Ciclo di addestramento
            int punteggio = 0;
            int mosseRimanenti = 100;

            while (true) {
                gameThreadPointer.setPronto(true);
                mosseRimanenti--;
                Utils.sleep(1000);
                if (gameThreadPointer.isPerso() || mosseRimanenti <= 0) {
                    break;
                }
                int playerX = gameWindow.getPlayer().getCoords().get(0)[0];
                int playerY = gameWindow.getPlayer().getCoords().get(0)[1];

                int melaX = gameWindow.getApple().getCoords()[0];
                int melaY = gameWindow.getApple().getCoords()[1];

                double valoreMelaX = Utils.map(melaX - playerX, false, gameWindow);
                double valoreMelaY = Utils.map(melaY - playerY, true, gameWindow);

                double[] input = new double[8];
                if (valoreMelaY < 0) {
                    input[0] = -valoreMelaY;
                    input[2] = 0d;
                } else {
                    input[0] = 0d;
                    input[2] = valoreMelaY;
                }

                if (valoreMelaX < 0) {
                    input[3] = -valoreMelaX;
                    input[1] = 0d;
                } else {
                    input[3] = 0d;
                    input[1] = valoreMelaX;
                }

                thisCreatura.setInput(input);
                System.out.println("Input: ");
                for (double thisIn : input) {
                    System.out.print(thisIn + "  ");
                }
                System.out.println();

                double[] risultati = thisCreatura.calcola();
                System.out.println("È risultato: ");
                for (double thisResult : risultati) {
                    System.out.print(thisResult + " ");
                }
                System.out.println();

//                System.out.println("Il valore maggiore è " + valoreMaggiore + " con direzione " + direzMaggiore);
            }
        }

    }

    public int[] ordina(int[] punteggiOttenuti) {
        //Viene clonato per non intaccare l'array passato
        int[] risultati = punteggiOttenuti.clone();
        int[] classifica = new int[risultati.length];

        //Variabile temporanea per salvare il più alto
        double massimo = 0;
        //Indica in che posizione è il massimo
        int posizione = -1;

        //Indica a quale posizione della classifica va messo la prossima rete
        //migliore, tralasciando quelle già considerate
        int ordinati = 0;

        while (ordinati < risultati.length) {
            massimo = 0;
            posizione = 0;
            //Finchè c'è ancora qualcosa da ordinare:
            for (int k = 0; k < risultati.length; k++) {
                //Per ogni risultato della rete
                if (risultati[k] >= massimo) {
                    massimo = risultati[k];
                    posizione = k;
                }
            }
            //Mette a -1 il risultato in modo che non venga più considerato
            risultati[posizione] = -100;
            //Salva la posizione nella classifica
            classifica[ordinati] = posizione;
            //Aumenta di uno il numero di posizioni ordinate
            ordinati++;
        }

        return classifica;
    }

    public void newGeneration(int[] classifica, double mutationBound) {
        //Crea altre reti prendendo i pesi dai quelli dei genitori e
        //applicando una mutazione casuale.
        //Gli input: classifica
        double[] newPesi;

        //Crea i genitori, cioè copia tanti quanti sono quelli specificati dalla classifica
        int[] genitori = new int[2];
        for (int k = 0; k < 2; k++) {
            genitori[k] = classifica[k];
        }

        for (int rete = 0; rete < numCreature; rete++) {
            //Per tutte le reti:
            for (int livello = 0; livello < creature[rete].getCervello().getTotalLayersNumber(); livello++) {
                //Per ogni livello:
                for (int neurone = 0; neurone < creature[rete].getCervello().getLayerSize(livello); neurone++) {
                    //Per ogni neurone di questo livello di questa rete:
                    //Qui bisogna fare che i figli hanno un po' di neuroni dal padre,
                    //un po' dalla madre. Poi bisogna aggiungere una mutazione con il 20% di probabilità

                    Random rd = new Random();
                    int generato = rd.nextInt(10);
                    //Crea un numero tra 0 e 9 inclusi e guarda se è sotto 5
                    ArrayList<Double> pesiGenitore;
                    double[] pesiNuovi = null;

                    if (rd.nextInt(10) < 5) //Questo neurone ha i pesi del genitore 1
                    {
                        pesiGenitore = creature[genitori[0]].getCervello().getPesi(livello, neurone);
                    } else //Questo neurone ha i pesi del genitore 2
                    {
                        pesiGenitore = creature[genitori[1]].getCervello().getPesi(livello, neurone);
                    }

                    //Trasforma l'arraylist di Double in array di double
                    pesiNuovi = new double[pesiGenitore.size()];
                    for (int i = 0; i < pesiNuovi.length; i++) {
                        pesiNuovi[i] = pesiGenitore.get(i);
                    }

                    creature[rete].getCervello().setPesi(livello, neurone, pesiNuovi);

                    //Qui si aggiunge la mutazione
                    //Prende i pesi attuali
                    ArrayList<Double> currentPesi = creature[rete].getCervello().getPesi(livello, neurone);
                    for (int thisPeso = 0; thisPeso < currentPesi.size(); thisPeso++) {
                        //Per ogni peso in ingresso di questo neurone:
                        if (rd.nextInt(5) == 0) {
                            //20% di probabilità:
                            double tmpPeso = currentPesi.get(thisPeso);
                            //Genera un double casuale usando come tetti minimi e massimi
                            //i bound passati alla funzione
                            double min = mutationBound * -1;
                            double max = mutationBound;
                            //double mutazione = min + (max - min) * rd.nextDouble();
                            double mutazione = Utils.randomDouble(min, max);

                            //Si aggiunge la mutazione creata al peso
                            tmpPeso += mutazione;
                            //Rimette nell'array list il peso modificato
                            currentPesi.set(thisPeso, tmpPeso);
                        }
                    }

                    //Crea l'array da passare come nuovi pesi
                    double[] mutatedPesi = new double[currentPesi.size()];
                    //Riempie l'array con i pesi dell'arraylist (che contiene alcuni mutati)
                    for (int k = 0; k < mutatedPesi.length; k++) {
                        mutatedPesi[k] = currentPesi.get(k);
                    }

                    //Imposta i nuovi pesi
                    creature[rete].getCervello().setPesi(livello, neurone, mutatedPesi);
                }
            }
        }
    }
}
