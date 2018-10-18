package com.magube.neuralsnek.brain;

import java.util.ArrayList;

public class Neuron {

    private double soglia; //Punto di discontinuità (prima specie) della funzione
    private double inputNeuronValue; //Valore di input (utilizzato nei neuroni di input)
    private final boolean isInputNeuron;  //Flag per controllare che sia input
    private final boolean isOutputNeuron;  //Flag per controllare che sia output

    private String nome; //Nome del neurone nella forma LayerX_NeuronY
    private final ArrayList<Neuron> previousNeurons = new ArrayList<>(0); //ArrayList con i neuroni del livello precedente
    //ArrayList con tutti i pesi dei valori in entrata (stessa lunghezza di previousNeurons
    private ArrayList<Double> inputWeights = new ArrayList<>(0);
    boolean logging;

    public Neuron(boolean logging, boolean isInput, boolean isOutput) {
        //Se viene passato con due flag viene assegnato al valore che indica se è input
        this.isInputNeuron = isInput;
        this.isOutputNeuron = isOutput;
        this.logging = logging;
        nome = "";
    }

    //Aggiunge all'array contenente i neuroni precedenti il neurone passato
    public void connect(Neuron neur) {
        previousNeurons.add(neur);
        if (logging) {
            System.out.print("\t\t\t\t" + this.getNome() + " è connesso a ");
            for (Neuron elem : previousNeurons) {
                System.out.print(elem.getNome() + " ");
            }
            System.out.println();
        }
    }

    //Calcola il risultato in base agli input e i pesi
    public double compute() {
        //Se è un neurone di input, ritorna semplicemente il valore che ha
        //in ingresso moltiplicato per il primo (e unico) peso che ha.
        if (isInputNeuron) {
            if (logging) {
                System.out.println(this.getNome() + ": Sono di input "
                        + "quindi ritorno il mio valore per il peso: "
                        + inputNeuronValue + " * " + inputWeights.get(0) + " = "
                        + (inputNeuronValue * inputWeights.get(0)));
            }

            return inputNeuronValue * inputWeights.get(0);
        } else if ((previousNeurons.size() == inputWeights.size())) {
            //Se ha lo stesso numero di pesi e input, cioè è impostato correttamente
            double result = 0;

            for (int k = 0; k < inputWeights.size(); k++) {
                double risultatoPrecedente = previousNeurons.get(k).compute() * inputWeights.get(k);
                result += risultatoPrecedente;
                if (logging) {
                    System.out.println("\n" + this.getNome() + ": Moltiplico "
                            + risultatoPrecedente + " per " + inputWeights.get(k) + "\n");
                }
            }

            if (!isOutputNeuron) {
                if (logging) {
                    System.out.printf("%s: Risulta " + result, this.getNome());
                }

                if (result >= soglia) {
                    if (logging) {
                        System.out.println(" quindi ritorno 1");
                    }
                    return 1;
                } else {
                    if (logging) {
                        System.out.println(" quindi ritorno 0");
                    }
                    return 0;
                }
            } else {
                if (logging) {
                    System.out.printf("%s: Sono di output, quindi risulto il mio valore: %.3f \n", this.getNome(), result);
                }

                return result;
            }
        } else {
            System.out.printf("Errore nel neutrone " + this.getNome()
                    + ": %d connessioni ma ci sono %d pesi\n",
                    previousNeurons.size(), inputWeights.size());
            return 0;
        }
    }

    //Imposta il valore d'ingresso del neurone di input
    public void setInputNeuronValue(double inputNeuronValue) {
        this.inputNeuronValue = inputNeuronValue;
    }

    //Ritorna vero se è un neurone di input
    public boolean isInputNeuron() {
        return isInputNeuron;
    }

    //Ritorna il nome
    public String getNome() {
        return nome;
    }

    //Ritorna il valore di input nel caso fosse un neurone di input
    public double getInputNeuronValue() {
        return inputNeuronValue;
    }

    //Ritorna la soglia
    public double getSoglia() {
        return soglia;
    }

    //Imposta una nuova soglia
    public void setSoglia(double soglia) {
        this.soglia = soglia;
    }

    //Imposta un nuovo nome
    public void setName(String name) {
        this.nome = name;
    }

    //Ritorna i valori dei pesi
    public ArrayList<Double> getInputWeights() {
        return inputWeights;
    }

    //Imposta dei nuovi pesi
    public void setInputWeights(ArrayList<Double> inputWeights) {
        this.inputWeights.clear();
        this.inputWeights = inputWeights;
    }

    //Ritorna la lista dei neuroni precedenti
    public ArrayList<Neuron> getPreviousNeurons() {
        return previousNeurons;
    }
}
