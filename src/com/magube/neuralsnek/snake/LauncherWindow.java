package com.magube.neuralsnek.snake;

import com.magube.neuralsnek.snake.brain.SnekManager;
import javax.swing.UIManager;

public class LauncherWindow extends javax.swing.JFrame {

    public LauncherWindow() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        butNewGame = new javax.swing.JButton();
        butNewNet = new javax.swing.JButton();
        butLoad = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Snake");
        setResizable(false);

        butNewGame.setText("Gioca a Snake");
        butNewGame.setFocusPainted(false);
        butNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butNewGameActionPerformed(evt);
            }
        });

        butNewNet.setText("Nuovo addestramento");
        butNewNet.setFocusPainted(false);
        butNewNet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butNewNetActionPerformed(evt);
            }
        });

        butLoad.setText("Carica addestramento");
        butLoad.setFocusPainted(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(butLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(butNewNet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(butNewGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(butNewGame)
                .addGap(18, 18, 18)
                .addComponent(butNewNet)
                .addGap(18, 18, 18)
                .addComponent(butLoad)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void butNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butNewGameActionPerformed
        GameWindow game = new GameWindow(true);
        game.setLocationRelativeTo(null);
        game.setTitle("NeuralSnek - Gioca");
        game.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_butNewGameActionPerformed

    private void butNewNetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butNewNetActionPerformed
        SnekManager manager = new SnekManager();
        manager.start();
    }//GEN-LAST:event_butNewNetActionPerformed

    public static void main(String args[]) {
        try {
            // Utilizza il Look and Feel nativo del sistema operativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LauncherWindow finestra = new LauncherWindow();
                finestra.setLocationRelativeTo(null);
                finestra.setVisible(true);
                
                SnekManager manager = new SnekManager();
                manager.start();
                finestra.dispose();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butLoad;
    private javax.swing.JButton butNewGame;
    private javax.swing.JButton butNewNet;
    // End of variables declaration//GEN-END:variables
}
