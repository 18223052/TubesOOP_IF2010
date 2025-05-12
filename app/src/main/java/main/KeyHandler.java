package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed;
    public boolean inventoryPressed;
    public boolean useItemPressed, discardItemPressed, sellItemPressed;
    public boolean filterPressed;
    private int currentFilterIndex = 0;
    private String[] filters = {"all", "tools", "consumables", "crops", "fish"};
    
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        // PLAY STATE
        if(gp.gameState == gp.playState) {
            playState(code);
        }
        // PAUSE STATE
        else if(gp.gameState == gp.pauseState) {
            pauseState(code);
        }
        // DIALOG STATE
        else if(gp.gameState == gp.dialogState) {
            dialogueState(code);
        }
        else if (gp.gameState == gp.statsState ){
            statsState(code);
        }
        // INVENTORY STATE
        else if(gp.gameState == gp.inventoryState) {
            // Close inventory if TAB is pressed
            if (code == KeyEvent.VK_I) {
                if (gp.gameState == gp.playState) {
                    gp.gameState = gp.inventoryState;
                } else if (gp.gameState == gp.inventoryState) {
                    gp.gameState = gp.playState;
                }
                inventoryPressed = true;
            }
    
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.inventoryController.moveSelectionUp();
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.inventoryController.moveSelectionDown();
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                gp.inventoryController.moveSelectionLeft();
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                gp.inventoryController.moveSelectionRight();
            }
            if (code == KeyEvent.VK_E) {
                gp.inventoryController.useItem(gp.inventoryController.getSelectedSlot());
                useItemPressed = true;
            }
            if (code == KeyEvent.VK_Q) {
                gp.inventoryController.discardItem(gp.inventoryController.getSelectedSlot());
                discardItemPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                gp.inventoryController.sellItem(gp.inventoryController.getSelectedSlot());
                sellItemPressed = true;
            }
            if (code == KeyEvent.VK_F) {
                currentFilterIndex = (currentFilterIndex + 1) % filters.length;
                gp.inventoryController.setFilter(filters[currentFilterIndex]);
                filterPressed = true;
            }
        }
    }
    
    public void titleState(int code){

    }

    public void playState (int code){
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }
        if (code == KeyEvent.VK_E) {
            interactPressed = true;
        }
        if (code == KeyEvent.VK_I) {
            gp.gameState = gp.inventoryState;
        }
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.statsState;
        }
    }

    public void pauseState (int code){
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }

    public void dialogueState(int code){
        if (code == KeyEvent.VK_E) {
            gp.gameState = gp.playState;
        }
    }

    public void statsState (int code){
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}