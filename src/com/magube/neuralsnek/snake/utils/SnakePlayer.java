package com.magube.neuralsnek.snake.utils;

import java.util.ArrayList;

public class SnakePlayer {

    //Matrice che rappresenta le coordinate del serpente.
    //Ogni riga della matrice è un blocco del serpente, e le due colonne le coordinate x ed y
    private ArrayList<int[]> player;

    //0: su    1: destra    2: giù    3: sinistra
    private int direzioneTesta;     //Usato per la direzione in cui muovere il corpo
    private int direzioneCoda;      //Usato per la direzione in cui aggiungere la coda quando si mangia

    public SnakePlayer() {
        player = new ArrayList<>(0);
        
        player.add(new int[]{15, 15});
        player.add(new int[]{15, 16});
        player.add(new int[]{15, 17});
        System.out.println("Ciao sono grande " + player.size());
    }

    public ArrayList<int[]> getSnakePlayer() {
        return player;
    }

    public void muovi() {
        for (int blocco = player.size() - 1; blocco > 0; blocco--) {
            //Per ogni blocco a partire dall'ultimo, imposta le sue coord a quelle del blocco prima
            player.get(blocco)[0] = player.get(blocco - 1)[0];
            player.get(blocco)[1] = player.get(blocco - 1)[1];
        }

        //La testa avanza di 1 rispetto alla sua direzione
        int[] testa = player.get(0);
        switch (direzioneTesta) {
            case 0:    //su, stessa x, y-1
                testa[1]--;
                player.set(0, testa);
                break;

            case 1:    //destra, x+1, stessa y
                testa[0]++;
                player.set(0, testa);
                break;

            case 2:    //giù, stessa x, y+1
                testa[1]++;
                player.set(0, testa);
                break;

            case 3:    //sinistra, x-1, stessa y
                testa[0]--;
                player.set(0, testa);
                break;
        }
    }

    public void setHead(int x, int y) {
        if (player.size() == 0) {
            player.add(new int[]{x, y});
        } else {
            System.err.println("Errore: testa già presente");
        }
    }

    public void addBlock() {
        int[] ultimoPezzo = player.get(player.size() - 1);
        switch (direzioneCoda) {

            case 0:     //Su:       stessa x, y+1
                player.add(new int[]{ultimoPezzo[0], ultimoPezzo[1] + 1});
                break;

            case 1:     //Destra    x+1, stessa y
                player.add(new int[]{ultimoPezzo[0] + 1, ultimoPezzo[1]});
                break;

            case 2:     //Giù       stessa x, y-1
                player.add(new int[]{ultimoPezzo[0], ultimoPezzo[1] - 1});
                break;

            case 3:     //Sinistra  x-1, stessa y
                player.add(new int[]{ultimoPezzo[0] - 1, ultimoPezzo[1]});
                break;
        }
    }

    public int getDirezioneTesta() {
        return direzioneTesta;
    }

    public void setDirezioneTesta(int direzioneTesta) {
        this.direzioneTesta = direzioneTesta;
    }

    public int getDirezioneCoda() {
        return direzioneCoda;
    }

    public void setDirezioneCoda(int direzioneCoda) {
        this.direzioneCoda = direzioneCoda;
    }
}
