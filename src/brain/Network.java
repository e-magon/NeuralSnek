package brain;

import java.util.ArrayList;

public class Network {
    private int inputNeuronsNumber; //Numero di neuroni input
    private int hiddenLayersNumber; //Numero di livelli nascosti
    private int hiddenNeuronsNumber; //Numero di neuroni per ogni livello nascosto
    private int outputNeuronsNumber; //Numero di neuroni output
    
    private boolean logging;
    
    private Neuron[][] network; //Matrice di neuroni

    //Costruttore con parametri
    public Network(int inputNeu, int hidLay, int hidNeu, int outputNeu, boolean logging) {
        inputNeuronsNumber=inputNeu;
        hiddenLayersNumber=hidLay;
        hiddenNeuronsNumber=hidNeu;
        outputNeuronsNumber=outputNeu;
        this.logging = logging;
        
        //Imposta la matrice a x livelli (x è il numero di livelli nascosti + 2, quelli per input e output)
        network = new Neuron[hiddenLayersNumber+2][];
        
        //Alloca spazio nel primo livello per i neuroni di input
        network[0] = new Neuron[inputNeuronsNumber];
        
        //Crea un neurone di input fino a riempire il primo layer
        for (int k=0; k<inputNeuronsNumber; k++) {
            //Usa il costruttore per quelli di input
            network[0][k] = new Neuron(logging, true);
            //Imposta il nome: Layer0_NeuronK
            network[0][k].setName("L0_N" + k);
        }
        
        //Crea gli array per ogni livello nascosto
        for (int k=1; k<=hiddenLayersNumber; k++) { //k è il layer
            //Alloca spazio per i neuroni del livello k
            network[k] = new Neuron[hiddenNeuronsNumber];
            //Crea un neurone in ogni cella del livello
            for (int c=0; c<hiddenNeuronsNumber; c++) { //c è il numero del neurone di quel livello
                network[k][c] = new Neuron(logging);
                //LayerK_NeuronC
                network[k][c].setName("L" + k + "_N" + c);
            }
        }

        //Alloca spazio per i neuroni di output
        network[hiddenLayersNumber+1] = new Neuron[outputNeuronsNumber];
        
        //Crea i neuroni di output
        for (int c=0; c<outputNeuronsNumber; c++) { //c è il numero del neurone di quel livello
            network[hiddenLayersNumber+1][c] = new Neuron(logging);
            //LayerK_NeuronC
            network[hiddenLayersNumber+1][c]
                    .setName("L" + (hiddenLayersNumber+1) + "_N" + c);
        }
        
        //Conferma a utente
        if (logging)
            System.out.printf("Creata una rete neurale con %d neuroni di input, "
                + "%d neuroni nascosti per %d livelli nascosti, e %d neuroni "
                + "di output.\n\n", inputNeuronsNumber, hiddenNeuronsNumber,
                hiddenLayersNumber, outputNeuronsNumber);
    }

    
    //Connette tutti i neuroni tra i livelli
    public void collega() {
        //Connette tutti i neuroni tra di loro, a partire dall'ultimo
        //(è +1 perchè è l'output, non incluso nel numero di livelli nascosti)
        for (int k=hiddenLayersNumber+1; k>0; k--) {
            //Per ogni neurone di questo livello:
            if (logging)
                System.out.println("Opero al livello " + (k));
            
            for (Neuron thisNeuron : network[k]) {
                //Collega ogni neurone del livello precedente
                if (logging)
                    System.out.println("\tOpero sul neurone " + thisNeuron.getNome());
                
                for (Neuron prevNeuron : network[k-1]) {
                    if (logging)
                        System.out.println("\t\tLo collego al neurone " + prevNeuron.getNome());
                    //Collegamento
                    thisNeuron.connect(prevNeuron);
                }
            }
            if (logging)
                System.out.println();
        }
    }
    
    //Imposta il valore della soglia del neurone selezionato
    public void setSoglia(int layer, int dest, double newSoglia) {
        //Controlla che il layer del neurone selezionato esista (+2 ai livelli
        //per contare quelli di input e output. Parte da 1 per non contare quelli di input)
        if (layer >= 1 && layer < hiddenLayersNumber+2) {
            //Controlla che il neurone esiste in quel layer
            if (dest >= 0 && dest < network[layer].length) {
                if (logging) {
                    System.out.printf("Imposto la soglia del neurone %s a %.3f",
                        network[layer][dest].getNome(), newSoglia);
                    System.out.printf("\nPrima era: %.3f\n\n", network[layer][dest].getSoglia());
                }
                network[layer][dest].setSoglia(newSoglia);
            }
            else
                System.out.println("Il neurone " + dest +
                        " non è stato trovato nel livello " + layer);
        }
        else
            {
                System.out.printf("Errore impostazione soglia: si sta chiedendo "
                        + "di impostare la soglia a un neurone al livello %d, "
                        + "e ci sono %d livelli.\n", layer, hiddenLayersNumber+2);
            }
    }
    
    //Imposta il peso degli input di un dato neurone
    public void setPesi(int layer, int dest, double[] newPesi) {
        //Controlla che il layer del neurone selezionato esista (+2 ai livelli
        //per contare quelli di input e output)
        if (layer >= 0 && layer < hiddenLayersNumber+2) {
            //Controlla che il neurone esiste in quel layer
            if (dest >= 0 && dest <= network[layer].length) {
                //Viene creato un ArrayList temporaneo da passare al neurone.
                //Viene fatto perchè il metodo non accetta array normali.
                ArrayList<Double> pesiDaPassare = new ArrayList<>(0);
                for (double tmp : newPesi)
                    //Viene riempito l'ArrayList con l'array originale
                    pesiDaPassare.add(tmp);
                    
                if (logging) {
                    System.out.print("Cambio i pesi in entrata al neurone " +
                            network[layer][dest].getNome() + ", con i seguenti: ");
                
                    for (double weight : newPesi)
                        System.out.printf("%.3f ", weight);

                    System.out.println();
                    
                    System.out.print("Prima erano: ");
                    for (double weight : network[layer][dest].getInputWeights())
                        System.out.printf("%.3f ", weight);
                    System.out.println("\n");
                }
                
                //Passa l'array con i pesi passati al neurone. Si usa l'ArrayList
                //perchè il metodo non accetta un array normale
                network[layer][dest].setInputWeights(pesiDaPassare);
            }
            else
                System.err.println("Il neurone " + dest +
                        " non è stato trovato nel livello " + layer);
        }
        else
            System.err.println("Il livello " + layer + " non esiste");
    }
    
    //Imposta il valore in input di un neurone d'input
    public void setValoreInput(int inputNeuron, double valore) {
        //Controlla che il neurone specificato esista
        if (inputNeuron>= 0 && inputNeuron < inputNeuronsNumber) {
            if (logging) {
                System.out.printf("Imposto il valore di input del neurone %s a %.3f\n", network[0][inputNeuron].getNome(), valore);
                System.out.printf("Prima era: %.3f\n\n", network[0][inputNeuron].getInputNeuronValue());
            }
            //Assegna il valore di input al neurone di input specificato
            network[0][inputNeuron].setInputNeuronValue(valore);
        }
    }
    
    //Ritorna le informazioni della rete
    public String getInfo() {
        //Neurone temporaneo
        Neuron questo;
        //Stringa finale da stampare
        String str = "\n";
        
        for (int k=0; k<network.length; k++)
            //Per tutti i livelli:
            for (int c=0; c<network[k].length; c++) {
                //Per tutti i neuroni di questo livello:
                questo = network[k][c];
                //Aggiungo le informazioni di questo neurone alla stringa (nome e pesi)
                str += String.format("Neurone %s: |", questo.getNome());
                //Aggiunge i pesi
                str += " (Pesi: ";
                for (double weight : questo.getInputWeights())
                    str += String.format("%.3f; ", weight);
                str += "\b\b) |"; //Fatto per togliere lo spazio e la virgola
                
                if (network[k][c].isInputNeuron()) //E' un neurone di input
                    str += String.format(" (Input. Valore: %.3f) |", questo.getInputNeuronValue());
                else {
                    if (k == network.length-1) //E' un neurone di output (è ultimo)
                        str += " (Output) |";
                    
                    //Aggiunge la soglia
                    str += String.format(" Soglia: %.3f", questo.getSoglia());
                }
                
                str += "|\n";
            }
        
        return str;
    }
    
    //Ritorna i pesi di un neurone
    public ArrayList<Double> getPesi(int layer, int dest) {
        //Controlla che il layer del neurone selezionato esista (+2 ai livelli
        //per contare quelli di input e output)
        if (layer >= 0 && layer < hiddenLayersNumber+2) {
            //Controlla che il neurone esiste in quel layer
            if (dest >= 0 && dest <= network[layer].length) {
                return network[layer][dest].getInputWeights();
            }
            else
                System.err.println("Il neurone " + dest +
                        " non è stato trovato nel livello " + layer);
        }
        else
            System.err.println("Il livello " + layer + " non esiste");
        return null;
    }
    
    //Ritorna la soglia di un neurone
    public double getSoglia(int layer, int dest) {
        //Controlla che il layer del neurone selezionato esista (+2 ai livelli
        //per contare quelli di input e output)
        if (layer >= 0 && layer < hiddenLayersNumber+2) {
            //Controlla che il neurone esiste in quel layer
            if (dest >= 0 && dest <= network[layer].length) {
                return network[layer][dest].getSoglia();
            }
            else
                System.err.println("Il neurone " + dest +
                        " non è stato trovato nel livello " + layer);
        }
        else
            System.err.println("Il livello " + layer + " non esiste");
        return 0;
    }
    
    //Avvia l'esecuzione: per ogni neurone di output, inizia a computare a ritroso
    public double[] compute() {
        //Il risultato è un array di double: ogni valore è ciò che risulta un
        //neurone di output quindi l'array è grande tanto quanti sono gli output
        double result[] = new double[outputNeuronsNumber];
        for (int k=0; k<outputNeuronsNumber; k++) {
            //Il risultato è la computazione di ogni neurone di output (cioè dell'ultimo livello)
            result[k] = network[hiddenLayersNumber+1][k].compute();
        }
        return result;
    }
    
    //Ritorna il numero di neuroni in input
    public int getInputNeuronsNumber() {
        return inputNeuronsNumber;
    }
    
    //Ritorna il numero di neuroni in output
    public int getOutputNeuronsNumber() {
        return outputNeuronsNumber;
    }
    
    //Ritorna il numero di neuroni a livello nascosto
    public int getHiddenNeuronsNumber() {
        return hiddenNeuronsNumber;
    }
    
    //Ritorna il numero di livelli nascosti
    public int getHiddenLayersNumber() {
        return hiddenLayersNumber;
    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }
    
    //Ritorna il numero totale di neuroni, includendo anche quelli di input e output
    public int getTotalNeuronsNumber() {
        return inputNeuronsNumber + (hiddenLayersNumber*hiddenNeuronsNumber) + outputNeuronsNumber;
    }
    
    public int getTotalLayersNumber() {
        return hiddenLayersNumber + 2;
    }
    
    public Neuron[][] getNetwork() {
        return network;
    }
    
    public int getLayerSize(int layer) {
        return network[layer].length;
    }
}