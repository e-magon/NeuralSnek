package com.magube.neuralsnek.brain.manager;

import com.magube.neuralsnek.brain.Network;
import java.util.ArrayList;
import java.util.Random;

public class NNManager {
    private boolean logging;
    
    private int numCreature;
    private int inputNeu;
    private int hidLayNum;
    private int hidNeuNum;
    private int outputNeu;
    private double[] input;
    private double soglie;
    private Network[] creature;

    public NNManager(boolean logging, int numCreature, int inputNeu, int hidLayNum,
            int hidNeuNum, int outputNeu, double soglie) {
        this.logging = logging;
        
        this.numCreature = numCreature;
        this.inputNeu = inputNeu;
        this.hidLayNum = hidLayNum;
        this.hidNeuNum = hidNeuNum;
        this.outputNeu = outputNeu;
        
        this.soglie = soglie;
    }
        
    public void creaReti() {
        //Crea tante creature quante sono state dette
        creature = new Network[numCreature];
        for (int rete=0; rete<creature.length; rete++)
            creature[rete] = new Network(inputNeu, hidLayNum, hidNeuNum, outputNeu, logging);
        
        //Imposta le soglie
        impostaSoglie(creature);
        
        //Info
        for (int rete=0; rete<creature.length; rete++) {
            System.out.println(creature[rete].getInfo());
            System.out.println("---------------");
        }
        
        //Collega le reti
        for (int rete=0; rete<creature.length; rete++)
            creature[rete].collega();
    }

    private void impostaSoglie(Network[] creature) {
        for (int rete=0; rete<creature.length; rete++)
            //Per ogni rete:
            for (int livello=1; livello<creature[rete].getTotalLayersNumber(); livello++)
                //Per ogni livello (parte da 1 per saltare input che non ha soglie):
                for (int neu=0; neu<creature[rete].getLayerSize(livello); neu++)
                    //Per ogni neurone di questo livello:
                    creature[rete].setSoglia(livello, neu, soglie);
    }
    
    public void generaPesi() {
        double min = 0;
        double max = 1;
        double[] randomPesi = null;
        
        for (int rete=0; rete<creature.length; rete++) {
            //Per ogni rete:
            for (int livello=0; livello<creature[rete].getTotalLayersNumber(); livello++) {
                //Per ogni livello:
                for (int neu=0; neu<creature[rete].getLayerSize(livello); neu++) {
                    //Per ogni neurone di questo livello:
                    if (livello == 0) {
                        //Per ogni neurone di input:
                        randomPesi = new double[1];
                        randomPesi[0] = randomDouble(min, max);
                    }
                    
                    if (livello == 1) {
                        //E' di secondo livello, quindi numero di pesi è uguale a num neuroni di input
                        randomPesi = new double[inputNeu];
                        
                        for (int k=0; k<inputNeu; k++)
                            randomPesi[k] = randomDouble(min, max);
                    }
                    else {
                        randomPesi = new double[hidNeuNum];
                        
                        for (int k=0; k<hidNeuNum; k++)
                            randomPesi[k] = randomDouble(min, max);
                    }
                                
                    creature[rete].setPesi(livello, neu, randomPesi);
                }
            }
        }
    }
    
    private static double randomDouble(double min, double max) {
        double random = new Random().nextDouble();
        return min + (random * (max - min));
    }
    
    
    public double[][] calcola() {
        //Ritorna una matrice: il risultato di ogni rete
        double[][] risultato = new double[numCreature][outputNeu];
        
        //Single thread:
        for (int rete=0; rete<numCreature; rete++) {
            //Per ogni rete:
            for (int k=0; k<input.length; k++)
                //Per ogni neurone di input di questa rete, metti il corrispettivo input
                creature[rete].setValoreInput(k, input[k]);
            //Salva l'elaborazione nella matrice
            risultato[rete] = creature[rete].compute();
        }
        
        return risultato;
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
        
        while(ordinati < risultati.length) {
            massimo = 0;
            posizione = 0;
            //Finchè c'è ancora qualcosa da ordinare:
            for (int k=0; k<risultati.length; k++) {
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
        for (int k=0; k<2; k++) {
            genitori[k] = classifica[k];
        }
        
        for (int rete=0; rete<numCreature; rete++) {
            //Per tutte le reti:
            for (int livello=0; livello<creature[rete].getTotalLayersNumber(); livello++) {
                //Per ogni livello:
                for (int neurone=0; neurone<creature[rete].getLayerSize(livello); neurone++) {
                    //Per ogni neurone di questo livello di questa rete:
                    //Qui bisogna fare che i figli hanno un po' di neuroni dal padre,
                    //un po' dalla madre. Poi bisogna aggiungere una mutazione con il 20% di probabilità
                    
                    Random rd = new Random();
                    int generato = rd.nextInt(10);
                    //Crea un numero tra 0 e 9 inclusi e guarda se è sotto 5
                    ArrayList <Double> pesiGenitore;
                    double[] pesiNuovi = null;
                    
                    if (rd.nextInt(10) < 5) 
                        //Questo neurone ha i pesi del genitore 1
                        pesiGenitore = creature[genitori[0]].getPesi(livello, neurone);
                    
                    else
                        //Questo neurone ha i pesi del genitore 2
                        pesiGenitore = creature[genitori[1]].getPesi(livello, neurone);
                    
                    //Trasforma l'arraylist di Double in array di double
                    pesiNuovi = new double[pesiGenitore.size()];
                    for (int i=0; i<pesiNuovi.length; i++)
                        pesiNuovi[i] = pesiGenitore.get(i);

                    creature[rete].setPesi(livello, neurone, pesiNuovi);
                    
                    
                    //Qui si aggiunge la mutazione
                    //Prende i pesi attuali
                    ArrayList <Double> currentPesi = creature[rete].getPesi(livello, neurone);
                    for (int thisPeso=0; thisPeso<currentPesi.size(); thisPeso++) {
                        //Per ogni peso in ingresso di questo neurone:
                        if (rd.nextInt(5) == 0) {
                            //20% di probabilità:
                            double tmpPeso = currentPesi.get(thisPeso);
                            //Genera un double casuale usando come tetti minimi e massimi
                            //i bound passati alla funzione
                            double min = mutationBound * -1;
                            double max = mutationBound;
                            //double mutazione = min + (max - min) * rd.nextDouble();
                            double mutazione = randomDouble(min, max);
                               
                            //Si aggiunge la mutazione creata al peso
                            tmpPeso += mutazione;
                            //Rimette nell'array list il peso modificato
                            currentPesi.set(thisPeso, tmpPeso);
                        }
                    }
                    
                    //Crea l'array da passare come nuovi pesi
                    double[] mutatedPesi = new double[currentPesi.size()];
                    //Riempie l'array con i pesi dell'arraylist (che contiene alcuni mutati)
                    for (int k=0; k<mutatedPesi.length; k++)
                        mutatedPesi[k] = currentPesi.get(k);
                    
                    //Imposta i nuovi pesi
                    creature[rete].setPesi(livello, neurone, mutatedPesi);
                }
            }
        }
    }
    
    
    
    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
        //Lo modifica per ogni rete
        for (Network rete : creature)
            rete.setLogging(logging);
    }

    public int getNumReti() {
        return numCreature;
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double[] input) {
        this.input = input;
        
    }

    public void setNumReti(int numReti) {
        this.numCreature = numReti;
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

    public Network[] getReti() {
        return creature;
    }

    public void setReti(Network[] reti) {
        this.creature = reti;
    }

    public void setSoglie(double soglie) {
        this.soglie = soglie;
    }
}