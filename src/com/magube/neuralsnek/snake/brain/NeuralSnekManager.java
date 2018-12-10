package com.magube.neuralsnek.snake.brain;

import com.magube.neuralsnek.snake.GameThread;
import com.magube.neuralsnek.snake.GameWindow;
import com.magube.neuralsnek.snake.utils.Utils;
import java.util.ArrayList;
import java.util.Random;

public class NeuralSnekManager extends Thread {

    private final int numCreature;
    private final NeuralSnake[] creature;
    private final int mosseTotali;
    private final int numGenerazioni;
    private final double soglie;
    private boolean hasMoved;    //Variabile usata per vedere se si è già mosso in questo frame

    private final int attesa; //tra un frame e l'altro in ms

    private GameWindow gameWindow;
    private GameThread gameThreadPointer;

    public NeuralSnekManager() {
        numCreature = 10;
        mosseTotali = 2000;
        numGenerazioni = 5;
        soglie = 0.8;
        creature = new NeuralSnake[numCreature];

        attesa = 20;
    }

    @Override
    public void run() {
        int[] classificaOrdinata = null, numMeleMangiate = null;
        for (int thisGenerazione = 0; thisGenerazione < numGenerazioni; thisGenerazione++) {
            System.out.println("New gen");
            int numClassifica = 0;
            numMeleMangiate = new int[numCreature];
            GameWindow oldWindow = null;    //Usate per chiudere la finestra precedente dopo aver aperta quella nuova
            
            //For che riempie array di nuove creature
            for (int k=0; k<numCreature; k++) {
                creature[k] = new NeuralSnake(8, 3, 6, 3, soglie, false);
                creature[k].creaRete();
            }

            for (NeuralSnake thisCreatura : creature) {
                Utils.sleep(150);
                

                gameWindow = new GameWindow(false, thisCreatura.getCorpo());
                gameWindow.setLocationRelativeTo(null);
                gameWindow.setTitle("NeuralSnek - Addestra - Generazione " + thisGenerazione +  " - creatura n. " + numClassifica);
                gameWindow.setVisible(true);
                if (oldWindow != null) {
                    oldWindow.dispose();
                }
                gameThreadPointer = gameWindow.getGameThread();

                gameThreadPointer.start();

                //Ciclo di addestramento
                int mosseRimanenti = mosseTotali;

                Utils.sleep(150);
                while (true) {
                    if (gameWindow.getApple().getCoords() == null || gameWindow.getPlayer().getCoords().get(0) == null) {
                        Utils.sleep(70);
                        continue;
                    }
                    if (hasMoved) {
                        Utils.sleep(70);
                        continue;
                    }

                    Utils.sleep(attesa);
//                Utils.sleep(1000);
                    gameThreadPointer.setPronto(true);

                    mosseRimanenti--;

                    if (gameThreadPointer.isPerso() || mosseRimanenti <= 0) {
                        gameThreadPointer.setPerso(true);
                        if (mosseRimanenti <= 0) {
                            System.err.println("Perso per mosse!");
                        }
                        break;
                    }

                    int playerX = gameWindow.getPlayer().getCoords().get(0)[0];
                    int playerY = gameWindow.getPlayer().getCoords().get(0)[1];

                    //Distanza dalla mela
                    int melaX = gameWindow.getApple().getCoords()[0];
                    int melaY = gameWindow.getApple().getCoords()[1];

                    double valoreMelaX = Utils.mapDistance(melaX, playerX, gameWindow.getWidth(), gameWindow.getCanvas().getBlockSize());
                    double valoreMelaY = Utils.mapDistance(melaY, playerY, gameWindow.getHeight(), gameWindow.getCanvas().getBlockSize());

                    //valoreMelaX *= 10;
                    //valoreMelaY *= 10;
                    double[] input = new double[8];
                    if (playerY > melaY) {
                        input[0] = valoreMelaY;
                        input[2] = 0d;
                    } else {
                        input[0] = 0d;
                        input[2] = valoreMelaY;
                    }

                    if (playerX > melaX) {
                        input[3] = valoreMelaX;
                        input[1] = 0d;
                    } else {
                        input[3] = 0d;
                        input[1] = valoreMelaX;
                    }

                    //Distanza dai muri
                    int larghezzaMappa = gameWindow.getWidth() / gameWindow.getCanvas().getBlockSize();     //35
                    int altezzaMappa = gameWindow.getHeight() / gameWindow.getCanvas().getBlockSize();      //24

                    input[4] = Utils.mapDistance(0, playerY, gameWindow.getHeight(), gameWindow.getCanvas().getBlockSize());
                    input[5] = Utils.mapDistance(larghezzaMappa, playerX, gameWindow.getWidth(), gameWindow.getCanvas().getBlockSize());
                    input[6] = Utils.mapDistance(altezzaMappa, playerY, gameWindow.getHeight(), gameWindow.getCanvas().getBlockSize());
                    input[7] = Utils.mapDistance(0, playerX, gameWindow.getWidth(), gameWindow.getCanvas().getBlockSize());

                    thisCreatura.setInput(input);
                    System.out.println("Input: ");
                    for (int k = 0; k < 4; k++) {
                        System.out.printf("%.3f ", input[k]);
                    }
                    System.out.println();
                    for (int k = 4; k < 8; k++) {
                        System.out.printf("%.3f ", input[k]);
                    }
                    System.out.println("\n");

                    double[] risultati = thisCreatura.calcola();
                    System.out.println("È risultato: ");
                    for (double thisResult : risultati) {
                        System.out.print(thisResult + " ");
                    }
                    System.out.println();

                    //La rete decide la direzione assoluta (non va bene, a volte decide di andare al contrario e non può)
                    /*
                double valoreMaggiore = -99d;
                int direzMaggiore = 0;

                for (int k = 0; k < risultati.length; k++) {
                    if (risultati[k] > valoreMaggiore) {
                        valoreMaggiore = risultati[k];
                        direzMaggiore = k;
                    }
                }

                System.out.println("Il valore maggiore è " + valoreMaggiore + " con direzione " + direzMaggiore);
                gameThreadPointer.move(direzMaggiore);
                System.out.println("direzione attuale: " + thisCreatura.getCorpo().getDirezioneTesta());
                     */
                    double valoreMaggiore = -99d;
                    int direzioneScelta = 0;        //-1 antiorario, 0 dritto, 1 orario

                    for (int k = 0; k < risultati.length; k++) {
                        if (risultati[k] > valoreMaggiore) {
                            valoreMaggiore = risultati[k];
                            direzioneScelta = k;
                        }
                    }

                    switch (direzioneScelta) {
                        case 0:
                            direzioneScelta = 3;
                            break;
                        case 1:
                            direzioneScelta = 0;
                            break;
                        case 2:
                            direzioneScelta = 1;
                            break;
                    }

                    System.out.println("Il valore maggiore è " + valoreMaggiore + " con direzione relativa scelta: " + direzioneScelta);
                    int direzAttuale = thisCreatura.getCorpo().getDirezioneTesta();
                    System.out.println("direz attuale " + direzAttuale);
                    System.out.println("direz assoluta scelta " + (direzioneScelta + direzAttuale) % 4);

                    if (!hasMoved) {
                        gameThreadPointer.move((direzioneScelta + direzAttuale) % 4);
                        //this.hasMoved = true;
                    }

                }

                oldWindow = gameWindow;
                numMeleMangiate[numClassifica] = gameThreadPointer.getPunteggio();
                numClassifica++;
            }

            System.out.println("\n\nClassifica ordinata:");
            classificaOrdinata = ordina(numMeleMangiate);
            for (int thisSnake : classificaOrdinata) {
                System.out.println("Creatura " + thisSnake + " con mele mangiate: " + numMeleMangiate[thisSnake]);
            }
            
            Utils.sleep(50);
            newGeneration(classificaOrdinata, 0.05, creature);
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

    public void newGeneration(int[] classifica, double mutationBound, NeuralSnake[] thisCreature) {
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
            for (int livello = 0; livello < thisCreature[rete].getCervello().getTotalLayersNumber(); livello++) {
                //Per ogni livello:
                for (int neurone = 0; neurone < thisCreature[rete].getCervello().getLayerSize(livello); neurone++) {
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
                        pesiGenitore = thisCreature[genitori[0]].getCervello().getPesi(livello, neurone);
                    } else //Questo neurone ha i pesi del genitore 2
                    {
                        pesiGenitore = thisCreature[genitori[1]].getCervello().getPesi(livello, neurone);
                    }

                    //Trasforma l'arraylist di Double in array di double
                    pesiNuovi = new double[pesiGenitore.size()];
                    for (int i = 0; i < pesiNuovi.length; i++) {
                        pesiNuovi[i] = pesiGenitore.get(i);
                    }

                    thisCreature[rete].getCervello().setPesi(livello, neurone, pesiNuovi);

                    //Qui si aggiunge la mutazione
                    //Prende i pesi attuali
                    ArrayList<Double> currentPesi = thisCreature[rete].getCervello().getPesi(livello, neurone);
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
                    thisCreature[rete].getCervello().setPesi(livello, neurone, mutatedPesi);
                }
            }
        }
    }
}