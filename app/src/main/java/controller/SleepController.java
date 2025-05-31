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

    // State apabila pingsan
    boolean isFainted = false;
    
    public SleepController(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
    }
    
    public void startSleep() {
        if (!isSleeping) {
            if (canSleep()) {
                System.out.println("Player akan tidur...");
                isSleeping = true;
                inTransition = true;
                fadingIn = true;
                transitionStartTime = System.nanoTime();
                transitionAlpha = 0f;
                isFainted = false;
                gp.gameState = GamePanel.sleepState;
            } else {
                showCannotSleepMessage();
            }
        }
    }

    public void forceSleep() {
        if (!isSleeping) {
            gp.setGameState(GamePanel.sleepState);
            System.out.println("Player pingsan...");
            isSleeping = true;
            inTransition = true;
            fadingIn = true;
            transitionStartTime = System.nanoTime();
            transitionAlpha = 0f;
            isFainted = true;
            // gp.setGameState(GamePanel.sleepState);
        }
    }
    
    public boolean canSleep() {
        if (gp.eManager.isLightingSetup()) {
            return gp.eManager.getLighting().isNight();
        }
        return false;
    }
    
    private void showCannotSleepMessage() {
        System.out.println("Kamu hanya bisa tidur di malam hari!");
        gp.ui.setDialog("Kamu hanya bisa tidur di malam hari!");
        gp.setGameState(GamePanel.dialogState);
    
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
                    gp.farmingController.updatePlantGrowth(); 
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
            gp.gameTime.nextDaySleep();
        }
        int currEnergy = gp.player.getEnergy();
        
        if (currEnergy <= 0) {
            gp.player.addEnergy(30); 
            System.out.println("Energi sangat rendah, ditambahkan 10. Energi sekarang: " + gp.player.getEnergy());
        } else if (currEnergy < 0.1 * Player.MAX_ENERGY) { 
            gp.player.addEnergy(50); 
            System.out.println("Energi rendah, ditambahkan 50. Energi sekarang: " + gp.player.getEnergy());
        } else { 
            gp.player.addEnergy(Player.MAX_ENERGY); 
            System.out.println("Energi diisi penuh. Energi sekarang: " + gp.player.getEnergy());
        }
        
        System.out.println("TimeSkip 1 hari!");
    }
    
    private void finishSleep() {
        isSleeping = false;
        inTransition = false;
        transitionAlpha = 0f;
        

        gp.gameState = GamePanel.playState;
        
        System.out.println("Player bangun!");
    }
    
    public void draw(Graphics2D g2) {
        if (isSleeping && inTransition) {

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transitionAlpha));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            

            if (transitionAlpha > 0.5f) {
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 30));
                String sleepText = isFainted? "Pingsan..." : "Tidur...";
                int textWidth = g2.getFontMetrics().stringWidth(sleepText);
                g2.drawString(sleepText, (gp.screenWidth - textWidth) / 2, gp.screenHeight / 2);
            }
            

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
    
    public boolean isSleeping() {
        return isSleeping;
    }
}