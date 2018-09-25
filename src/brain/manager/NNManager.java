package brain.manager;

import brain.Network;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class NNManager {
    //Manager che ha un input e un output aspettato predefinito
    private boolean logging;
    
    private int numReti;
    private int inputNeu;
    private int hidLay;
    private int hidNeu;
    private int outputNeu;
    private double[] target;
    private double[] input;
    private double soglie;
    private Network[] reti;

    public NNManager(boolean logging, int numReti, int inputNeu, int hidLay,
            int hidNeu, int outputNeu, double soglie) {
        this.logging = logging;
        this.numReti = numReti;
        this.inputNeu = inputNeu;
        this.hidLay = hidLay;
        this.hidNeu = hidNeu;
        this.outputNeu = outputNeu;
        this.soglie = soglie;
    }
        
    public void creaReti() {
        //Crea tante reti quante sono state dette
        reti = new Network[numReti];
        for (int rete=0; rete<reti.length; rete++)
            reti[rete] = new Network(inputNeu, hidLay, hidNeu, outputNeu, logging);
        
        //Imposta le soglie
        impostaSoglie();
        
        //Info
//        for (int rete=0; rete<reti.length; rete++) {
//            System.out.println(reti[rete].getInfo());
//            System.out.println("---------------");
//        }
        
        //Collega le reti
        for (int rete=0; rete<reti.length; rete++)
            reti[rete].collega();
    }

    private void impostaSoglie() {
        for (int rete=0; rete<reti.length; rete++)
            //Per ogni rete:
            for (int livello=1; livello<reti[rete].getTotalLayersNumber(); livello++)
                //Per ogni livello (parte da 1 per saltare input che non ha soglie):
                for (int neu=0; neu<reti[rete].getLayerSize(livello); neu++)
                    //Per ogni neurone di questo livello:
                    reti[rete].setSoglia(livello, neu, soglie);
    }
    
    public void generaPesi() {
        double min = -1;
        double max = 1;
        double[] randomPesi = null;
        
        for (int rete=0; rete<reti.length; rete++)
            //Per ogni rete:
            for (int livello=0; livello<reti[rete].getTotalLayersNumber(); livello++)
                //Per ogni livello:
                for (int neu=0; neu<reti[rete].getLayerSize(livello); neu++) {
                    //Per ogni neurone di questo livello:
                    if (livello <= 1) {
                        //E' di input oppure è il secondo livello, quindi numero di pesi è uguale a num neuroni di input
                        randomPesi = new double[inputNeu];
                        
                        for (int k=0; k<inputNeu; k++)
                            randomPesi[k] = randomDouble(min, max);
                    }
                    else {
                        randomPesi = new double[hidNeu];
                        
                        for (int k=0; k<hidNeu; k++)
                            randomPesi[k] = randomDouble(min, max);
                    }
                                
                    reti[rete].setPesi(livello, neu, randomPesi);
                }
    }
    
    private static double randomDouble(double min, double max) {
        double random = new Random().nextDouble();
        return min + (random * (max - min));
    }
    
    
    public double[][] calcola() {
        //Ritorna una matrice: il risultato di ogni rete
        double[][] risultato = new double[numReti][outputNeu];
        
        //Single thread:
        for (int rete=0; rete<numReti; rete++) {
            //Per ogni rete:
            for (int k=0; k<input.length; k++)
                //Per ogni neurone di input di questa rete, metti il corrispettivo input
                reti[rete].setValoreInput(k, input[k]);
            //Salva l'elaborazione nella matrice
            risultato[rete] = reti[rete].compute();
        }
        
        return risultato;
    }
    
    public int valuta(double[] output) {
        //Indica il fitness delle reti
        int punti = 0;
        
        //Controllo per vedere se il target e l'input sono lunghi uguali
        if (output.length == target.length) {
            for (int ris=0; ris<output.length; ris++) {
                //Per ogni risultato:
                if (output[ris] == target[ris])
                    punti++;
            }
            return punti;
        }
        else
            return -99;  
    }
  
    public int[] ordina(int[] input) {
        //Viene clonato per non intaccare l'array passato
        int[] risultati = input.clone();
        int[] classifica = new int[risultati.length];
        
        //Variabile temporanea per salvare il più alto
        double massimo = 0;
        //Indica in che posizione è il massimo
        int posizione = -1;
        //Indica a quale posizione della classifica va messo il prossimo trovato maggiore
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
            risultati[posizione] = -1;
            //Salva la posizione nella classifica
            classifica[ordinati] = posizione;
            //Aumenta di uno il numero di posizioni ordinate
            ordinati++;
        }
        
        return classifica;
    }
    
    public void newGeneration(int[] classifica, double mutationBound) {
        //Crea altre reti facendo la media tra quelli dei genitori e
        //applicando una mutazione casuale.
        //Gli input: classifica e quanti prenderne dai migliori come genitori.
        double[] newPesi;
        int genitoriTot = 2;
        
        //Crea i genitori, cioè copia tanti quanti sono quelli specificati dalla classifica
        int[] genitori = new int[genitoriTot];
        for (int k=0; k<genitoriTot; k++) {
            genitori[k] = classifica[k];
        }
        
        for (int rete=0; rete<numReti; rete++) {
            //Per tutte le reti:
            for (int livello=0; livello<reti[rete].getTotalLayersNumber(); livello++) {
                //Per ogni livello:
                for (int neurone=0; neurone<reti[rete].getLayerSize(livello); neurone++) {
                    //Per ogni neurone di questo livello:
                    //Qui bisogna fare che i figli hanno un po' di neuroni dal padre,
                    //un po' dalla madre. Poi bisogna aggiungere una mutazione con il 10% di probabilità
                    
                    Random rd = new Random();
                    int generato = rd.nextInt(10);
                    //Crea un numero tra 0 e 9 inclusi e guarda se è sotto 5
                    ArrayList <Double> pesiGenitore;
                    double[] pesiNuovi = null;
                    if (rd.nextInt(10) < 5) {
                        //Questo neurone ha i pesi del genitore 1
                        pesiGenitore = reti[genitori[0]].getPesi(livello, neurone);
                        
                        //Trasforma l'arraylist di Double in array di double
                        pesiNuovi = new double[pesiGenitore.size()];
                        for (int i=0; i<pesiNuovi.length; i++)
                            pesiNuovi[i] = pesiGenitore.get(i);
                        
                        reti[rete].setPesi(livello, neurone, pesiNuovi);
                    }
                    else {
                        //Questo neurone ha i pesi del genitore 2
                        pesiGenitore = reti[genitori[1]].getPesi(livello, neurone);
                        
                        //Trasforma l'arraylist di Double in array di double
                        pesiNuovi = new double[pesiGenitore.size()];
                        for (int i=0; i<pesiNuovi.length; i++)
                            pesiNuovi[i] = pesiGenitore.get(i);
                        
                        reti[rete].setPesi(livello, neurone, pesiNuovi);
                    }
                    //Qui si aggiunge la mutazione
                    //Prende i pesi attuali
                    ArrayList <Double> currentPesi = reti[rete].getPesi(livello, neurone);
                    for (int peso=0; peso<currentPesi.size(); peso++) {
                        //Per ogni peso in ingresso di questo neurone:
                        if (rd.nextInt(5) == 0) {
                            //20% di probabilità:
                            double tmpPeso = currentPesi.get(peso);
                            //Genera un double casuale usando come tetti minimi e massimi
                            //i bound passati alla funzione
                            double min = mutationBound * -1;
                            double max = mutationBound;
                            double mutazione = min + (max - min) * rd.nextDouble();
                            //Si aggiunge la mutazione creata al peso
                            tmpPeso += mutazione;
                            //Rimette nell'array list il peso modificato
                            currentPesi.set(peso, tmpPeso);
                        }
                    }
                    
                    //Crea l'array da passare come nuovi pesi
                    double[] mutatedPesi = new double[currentPesi.size()];
                    //Riempie l'array con i pesi dell'arraylist (che contiene alcuni mutati)
                    for (int k=0; k<mutatedPesi.length; k++)
                        mutatedPesi[k] = currentPesi.get(k);
                    
                    //Imposta i nuovi pesi
                    reti[rete].setPesi(livello, neurone, mutatedPesi);
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
        for (Network rete : reti)
            rete.setLogging(logging);
    }

    public int getNumReti() {
        return numReti;
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double[] input) {
        this.input = input;
        
    }
    
    public double[] getTarget() {
        return target;
    }

    public void setTarget(double[] target) {
        this.target = target;
    }

    public void setNumReti(int numReti) {
        this.numReti = numReti;
    }

    public int getInputNeu() {
        return inputNeu;
    }

    public void setInputNeu(int inputNeu) {
        this.inputNeu = inputNeu;
    }

    public int getHidLay() {
        return hidLay;
    }

    public void setHidLay(int hidLay) {
        this.hidLay = hidLay;
    }

    public int getHidNeu() {
        return hidNeu;
    }

    public void setHidNeu(int hidNeu) {
        this.hidNeu = hidNeu;
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
        return reti;
    }

    public void setReti(Network[] reti) {
        this.reti = reti;
    }

    public void setSoglie(double soglie) {
        this.soglie = soglie;
    }
}