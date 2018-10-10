package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.utils.*;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class GameWindow extends javax.swing.JFrame {

    private final SnakePlayer player;
    private final Apple apple;
    private final GameThread gameThread;
    private Boolean isPerso;

    private boolean paused = true;

    public GameWindow() {
        initComponents();
        this.getContentPane().setBackground(new Color(70, 70, 70));

        labelPerso.setVisible(false);

        player = new SnakePlayer();
        apple = new Apple();
        gameThread = new GameThread(canvas, player, apple, isPerso, labelPerso);
        //gameThread.start();   Il gioco viene avviato dopo aver premuto un pulsante
    }

    private void initGame() {

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelPerso)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(canvas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPerso)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        //Quando viene premuta una freccia
        if (paused) {
            gameThread.start();
            paused = false;
        } else {
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
    }//GEN-LAST:event_formKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.magube.neuralsnek.snake.PlayMapPanel canvas;
    private javax.swing.JLabel labelPerso;
    // End of variables declaration//GEN-END:variables
}