package com.magube.neuralsnek.snake;

import java.util.ArrayList;

public class SnakePlayer {

    //Matrice che rappresenta le coordinate del serpente.
    //Ogni riga della matrice è un blocco del serpente, e le due colonne le coordinate x ed y
    private final ArrayList<int[]> player;

    //0: su    1: destra    2: giù    3: sinistra
    private int direzioneTesta;     //Usato per la direzione in cui muovere il corpo

    public SnakePlayer() {
        player = new ArrayList<>(0);

        player.add(new int[]{18, 12});
        player.add(new int[]{18, 13});
        player.add(new int[]{18, 14});
    }

    public ArrayList<int[]> getSnakePlayer() {
        return player;
    }

    public int[] muovi() {
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
        return player.get(0);   //Ritorna la posizione nuova della testa
    }

    /**
     * Ritorna vero se la prossima mossa snake colpisce una parte del suo corpo
     *
     * @return true se colpisce se stesso
     */
    public boolean checkMove() {
        //La testa avanza di 1 rispetto alla sua direzione
        int[] newCoords = player.get(0).clone();
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

        for (int thisBlocco = 1; thisBlocco < player.size(); thisBlocco++) {
            //Controlla che la prossima posizione non sia uguale a quella di un blocco del serpente
            int thisX = player.get(thisBlocco)[0], thisY = player.get(thisBlocco)[1];
            if ((newCoords[0] == thisX) && (newCoords[1] == thisY)) {
                return true;
            }
        }

        return false;
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
        player.add(new int[]{ultimoPezzo[0], ultimoPezzo[1]});
    }

    public int getDirezioneTesta() {
        return direzioneTesta;
    }

    public void setDirezioneTesta(int direzioneTesta) {
        this.direzioneTesta = direzioneTesta;
    }
}
