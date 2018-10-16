package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.Utils;
import java.awt.Color;
import java.awt.event.KeyEvent;

//Il bottone di riavvia non deve essere focusable, altrimenti non vengono
//ascoltati i tasti di movimento
public class GameWindow extends javax.swing.JFrame {

    private SnakePlayer player;
    private Apple apple;
    private GameThread gameThread;

    private boolean paused;
    private boolean playable;

    public GameWindow(boolean playable) {
        initComponents();
        this.getContentPane().setBackground(new Color(70, 70, 70)); //Grigio scuro
        this.playable = playable;

        initGame();
    }

    private void initGame() {
        labelPerso.setVisible(false);
        labelPunti.setText("Punteggio: 0");

        if (playable) {
            labelComandi.setText("Premi W per iniziare");
        } else {
            labelComandi.setVisible(false);
        }

        if (playable) {
            paused = true;
        }

        player = new SnakePlayer();
        apple = new Apple(player.getPlayerCoords());
        gameThread = new GameThread(canvas, player, apple, labelPunti, labelPerso, playable);
        canvas.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvas = new com.magube.neuralsnek.snake.PlayMapPanel();
        labelPerso = new javax.swing.JLabel();
        labelPunti = new javax.swing.JLabel();
        butRiavvia = new javax.swing.JButton();
        labelComandi = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Titolo");
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 701, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 481, Short.MAX_VALUE)
        );

        labelPerso.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        labelPerso.setForeground(new java.awt.Color(255, 0, 0));
        labelPerso.setText("PERSO!");

        labelPunti.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        labelPunti.setForeground(new java.awt.Color(255, 255, 255));
        labelPunti.setText("Punteggio: 0");

        butRiavvia.setText("RIavvia");
        butRiavvia.setFocusable(false);
        butRiavvia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butRiavviaActionPerformed(evt);
            }
        });

        labelComandi.setForeground(new java.awt.Color(255, 255, 255));
        labelComandi.setText("Premi W per iniziare");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelPerso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelComandi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(butRiavvia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelPunti)
                        .addGap(44, 44, 44))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(canvas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPerso)
                    .addComponent(labelPunti)
                    .addComponent(butRiavvia)
                    .addComponent(labelComandi))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (playable) {
            //Se il gioco è in pausa, con il tasto W inizia
            if (paused && evt.getKeyCode() == KeyEvent.VK_W) {
                gameThread.start();
                labelComandi.setText("W, A, S, D per muoversi");
                paused = false;
            } //Altrimenti controlla il tasto
            else {
                switch (evt.getKeyCode()) {
                    case KeyEvent.VK_W:
                        //Va in alto
                        gameThread.move(0);
                        break;

                    case KeyEvent.VK_D:
                        //Va a destra
                        gameThread.move(1);
                        break;

                    case KeyEvent.VK_S:
                        //Va in basso
                        gameThread.move(2);
                        break;

                    case KeyEvent.VK_A:
                        //Va a sinistra
                        gameThread.move(3);
                        break;
                }
            }
        } else {
            gameThread.start();
        }
    }//GEN-LAST:event_formKeyPressed

    private void butRiavviaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butRiavviaActionPerformed
        gameThread.stop();
        initGame();
    }//GEN-LAST:event_butRiavviaActionPerformed

    public GameThread getGameThread() {
        return gameThread;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butRiavvia;
    private com.magube.neuralsnek.snake.PlayMapPanel canvas;
    private javax.swing.JLabel labelComandi;
    private javax.swing.JLabel labelPerso;
    private javax.swing.JLabel labelPunti;
    // End of variables declaration//GEN-END:variables
}
