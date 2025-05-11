package main;

import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed;
    
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
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
            if (code == KeyEvent.VK_TAB) {
                gp.gameState = gp.playState;
            }
            // Navigation within inventory
            if (code == KeyEvent.VK_W) {
                gp.inventoryController.moveSelectionUp();
            }
            if (code == KeyEvent.VK_S) {
                gp.inventoryController.moveSelectionDown();
            }
            if (code == KeyEvent.VK_A) {
                gp.inventoryController.moveSelectionLeft();
            }
            if (code == KeyEvent.VK_D) {
                gp.inventoryController.moveSelectionRight();
            }
            if (code == KeyEvent.VK_E) {
                // Use selected item
                gp.inventoryController.useItem(gp.inventoryController.getSelectedSlot());
            }
            if (code == KeyEvent.VK_Q) {
                // Drop/discard selected item
                gp.inventoryController.discardItem(gp.inventoryController.getSelectedSlot());
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
        if (code == KeyEvent.VK_TAB) {
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