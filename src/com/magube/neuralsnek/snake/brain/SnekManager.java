package com.magube.neuralsnek.snake.brain;

import com.magube.neuralsnek.snake.Apple;
import com.magube.neuralsnek.snake.GameThread;
import com.magube.neuralsnek.snake.GameWindow;
import com.magube.neuralsnek.snake.SnakePlayer;
import com.magube.neuralsnek.snake.utils.Utils;
import java.util.ArrayList;
import java.util.Random;

public class SnekManager {

    private final int numCreature;
    private final NeuralSnake[] creature;

    private GameWindow gameWindow;
    private GameThread gameThread;
    private Apple apple;

    public SnekManager() {
        numCreature = 3;
        creature = new NeuralSnake[numCreature];

        for (NeuralSnake creatura : creature) {
            creatura = new NeuralSnake(8, 2, 8, 4, 0.5, true);
            creatura.creaRete();
            creatura.getCorpo();
        }
        gameWindow = new GameWindow(false);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);
        
        apple = new Apple(creature[0].getCorpo().getCoords());
        apple.newCoord();
        
        Utils.sleep(500);

        gameThread = new GameThread(gameWindow.getCanvas(), creature[0].getCorpo(),
                apple, gameWindow.getLabelPunti(), gameWindow.getLabelPerso(), true);
        
        
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
