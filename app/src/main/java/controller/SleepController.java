package controller;

import main.GamePanel;
import java.awt.Graphics2D;

import entity.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.AlphaComposite;

public class SleepController {
    GamePanel gp;
    Player player;
    

    final long SLEEP_TRANSITION_DURATION = 3_000_000_000L;
    
    // Status tidur
    private boolean isSleeping = false;
    private boolean inTransition = false;
    private long transitionStartTime;
    
    // Alpha untuk efek blackout
    private float transitionAlpha = 0f;
    private boolean fadingIn = true; 
    
    public SleepController(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
    }
    
    public void startSleep() {
        if (!isSleeping) {
            if (canSleep()) {
                System.out.println("Player is going to sleep...");
                isSleeping = true;
                inTransition = true;
                fadingIn = true;
                transitionStartTime = System.nanoTime();
                transitionAlpha = 0f;
                int currEnergy = player.getEnergy();
                if (currEnergy < 0.1*player.maxEnergy){
                    player.addEnergy(50);
                } else if (currEnergy ==0){
                    player.addEnergy(10);
                } else {
                    player.addEnergy(player.maxEnergy);
                }
    
                gp.gameState = gp.sleepState;
            } else {

                showCannotSleepMessage();
            }
        }
    }
    
    public boolean canSleep() {
        if (gp.eManager.isLightingSetup()) {
            return gp.eManager.getLighting().isNight();
        }
        return false;
    }
    
    private void showCannotSleepMessage() {
        System.out.println("You can only sleep at night!");

        // if (gp.ui != null) {
        //     gp.ui.setDialog("You can only sleep at night!");
        //     gp.gameState = gp.dialogState;
        // }
        // gp.ui.setDialog("You can only sleep at night!");
        // gp.gameState = gp.dialogState;
    }
    
    public void update() {
        if (isSleeping && inTransition) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - transitionStartTime;
            float progress = Math.min((float) elapsedTime / SLEEP_TRANSITION_DURATION, 1.0f);
            
            if (fadingIn) {

                transitionAlpha = progress;
                
                if (progress >= 1.0f) {
                    skipToNextDay();
                    fadingIn = false;
                    transitionStartTime = currentTime;
                }
            } else {

                transitionAlpha = 1.0f - progress;
                
                if (progress >= 1.0f) {

                    finishSleep();
                }
            }
        }
    }
    
    private void skipToNextDay() {

        if (gp.eManager.isLightingSetup()) {
            gp.eManager.getLighting().skipDay();
        }
        

        gp.player.addEnergy(100);
        
        System.out.println("Skipped to next day!");
    }
    
    private void finishSleep() {
        isSleeping = false;
        inTransition = false;
        transitionAlpha = 0f;
        
        // Kembali ke play state
        gp.gameState = gp.playState;
        
        System.out.println("Player woke up!");
    }
    
    public void draw(Graphics2D g2) {
        if (isSleeping && inTransition) {
            // Draw blackout screen
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transitionAlpha));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            
            // Draw sleeping text
            if (transitionAlpha > 0.5f) {
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 30));
                String sleepText = "Sleeping...";
                int textWidth = g2.getFontMetrics().stringWidth(sleepText);
                g2.drawString(sleepText, (gp.screenWidth - textWidth) / 2, gp.screenHeight / 2);
            }
            
            // Reset composite
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
    
    public boolean isSleeping() {
        return isSleeping;
    }
}