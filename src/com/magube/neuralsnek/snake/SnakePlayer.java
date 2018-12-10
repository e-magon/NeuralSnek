package com.magube.neuralsnek.snake;

import java.util.ArrayList;

public class SnakePlayer {

    //Matrice che rappresenta le coordinate del serpente.
    //Ogni riga della matrice è un blocco del serpente, e le due colonne le coordinate x ed y
    private final ArrayList<int[]> playerCoords;

    //0: su    1: destra    2: giù    3: sinistra
    private int direzioneTesta;     //Usato per la direzione in cui muovere il corpo

    public SnakePlayer() {
        playerCoords = new ArrayList<>(0);

        setHead(18, 12);
        setDirezioneTesta(0);
        playerCoords.add(new int[]{18, 13});
        playerCoords.add(new int[]{18, 14});
    }

    public int[] muovi() {
        for (int blocco = playerCoords.size() - 1; blocco > 0; blocco--) {
            //Per ogni blocco a partire dall'ultimo, imposta le sue coord a quelle del blocco prima
            playerCoords.get(blocco)[0] = playerCoords.get(blocco - 1)[0];
            playerCoords.get(blocco)[1] = playerCoords.get(blocco - 1)[1];
        }

        //La testa avanza di 1 rispetto alla sua direzione
        int[] testa = playerCoords.get(0);

        switch (direzioneTesta) {
            case 0:    //su, stessa x, y-1
                testa[1]--;
                playerCoords.set(0, testa);
                break;

            case 1:    //destra, x+1, stessa y
                testa[0]++;
                playerCoords.set(0, testa);
                break;

            case 2:    //giù, stessa x, y+1
                testa[1]++;
                playerCoords.set(0, testa);
                break;

            case 3:    //sinistra, x-1, stessa y
                testa[0]--;
                playerCoords.set(0, testa);
                break;
        }
        return playerCoords.get(0);   //Ritorna la posizione nuova della testa
    }

    /**
     * Ritorna vero se nella prossima mossa snake colpisce una parte del suo
     * corpo
     *
     * @return true se colpisce se stesso
     */
    public synchronized boolean checkSelfCollision() {
        //La testa avanza di 1 rispetto alla sua direzione
        int[] newCoords = playerCoords.get(0).clone();

        switch (direzioneTesta) {
            case 0:    //su, stessa x, y-1
                newCoords[1]--;
                break;

            case 1:    //destra, x+1, stessa y
                newCoords[0]++;
                break;

            case 2:    //giù, stessa x, y+1
                newCoords[1]++;
                break;

            case 3:    //sinistra, x-1, stessa y
                newCoords[0]--;
                break;
        }

        //Non controlla l'ultimo blocco perchè, essendo la coda,
        //nel frame dopo anche lei si sarà spostata via dalla posizione attuale
        for (int thisBlocco = 1; thisBlocco < playerCoords.size() - 1; thisBlocco++) {
            //Controlla che la prossima posizione della testa non sia uguale a quella di un blocco del serpente
            int thisX = playerCoords.get(thisBlocco)[0], thisY = playerCoords.get(thisBlocco)[1];
            if ((newCoords[0] == thisX) && (newCoords[1] == thisY)) {
                return true;
            }
        }

        return false;
    }

    public void setHead(int x, int y) {
        if (playerCoords.size() == 0) {
            playerCoords.add(new int[]{x, y});
        } else {
            System.err.println("Errore: testa già presente");
        }
    }

    public void addBlock() {
        int[] ultimoPezzo = playerCoords.get(playerCoords.size() - 1);
        playerCoords.add(new int[]{ultimoPezzo[0], ultimoPezzo[1]});
    }

    public synchronized int getDirezioneTesta() {
        return direzioneTesta;
    }

    public synchronized void setDirezioneTesta(int direzioneTesta) {
        this.direzioneTesta = direzioneTesta;
    }

    public ArrayList<int[]> getCoords() {
        return playerCoords;
    }
}
