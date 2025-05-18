package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed, enterPressed;
    public boolean inventoryPressed;
    public boolean useItemPressed, discardItemPressed, sellItemPressed;
    public boolean filterPressed;
    private int currentFilterIndex = 0;
    private String[] filters = {"all", "tools", "consumables", "crops", "fish"};
    StringBuilder inputBuffer = new StringBuilder();
    public int singleNumPress;
    public String multiNumPress = "";

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if (Character.isDigit(ch)) {
            inputBuffer.append(ch);
            singleNumPress = ch - '0';
        } else {
            singleNumPress = -1;
            multiNumPress = inputBuffer.toString();
            inputBuffer.setLength(0);
        }
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
            inventoryState(code);
        }
        // COOKING STATE
        else if(gp.gameState == gp.cookingState) {
            cookingState(code);
        }

    }

    // public void titleState(int code){

    // }

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
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }

    public void pauseState (int code){
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }
    }

    public void dialogueState(int code){
        if (code == KeyEvent.VK_E) {
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }
    }

    public void cookingState(int code) {
        // Exit cooking with E key
        if (code == KeyEvent.VK_E) {
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }
        
        // Handle number keys (0-9)
        if (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9) {
            int digit = code - KeyEvent.VK_0;
            singleNumPress = digit;
            
            // Build multi-digit number input
            if (digit != 0 || !multiNumPress.isEmpty()) {
                multiNumPress += digit;
            }
        }
        
        // Handle Enter key
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        
        // Handle backspace to remove digits
        if (code == KeyEvent.VK_BACK_SPACE) {
            if (!multiNumPress.isEmpty()) {
                multiNumPress = multiNumPress.substring(0, multiNumPress.length() - 1);
            }
        }
    }

    public void statsState (int code){
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }
    }

    public void inventoryState(int code){
        if (code == KeyEvent.VK_I) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.inventoryState;
            } else if (gp.gameState == gp.inventoryState) {
                gp.gameState = gp.playState;
                gp.resumeGameThread();
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

    public void titleState(int code){

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
