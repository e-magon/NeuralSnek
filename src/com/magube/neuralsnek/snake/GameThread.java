package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.SnakePlayer;
import com.magube.neuralsnek.snake.utils.Utils;

public class GameThread extends Thread {

    private final PlayMapPanel gameCanvas;
    private final SnakePlayer player;

    boolean moved = false;

    public GameThread(PlayMapPanel gameCanvas, SnakePlayer player) {
        this.gameCanvas = gameCanvas;
        this.player = player;
    }

    @Override
    public void run() {
        while (true) {
            player.muovi();
            gameCanvas.repaint();
            Utils.sleep(100);
            moved = false;
        }
    }

    public void move(int direzione) {
        if (!moved) {
            player.setDirezioneTesta(direzione);
            moved = true;
        }
    }
}
