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

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // System.out.println("Pressed: " + code); // Debug

        // State transitions based on game state
        if (gp.gameState == gp.playState) {
            gp.isTimePaused = false;
            handlePlayState(code);
        } else if (gp.gameState == gp.pauseState) {
            gp.isTimePaused = true;
            handlePauseState(code);
        } else if (gp.gameState == gp.dialogState) {
            handleDialogState(code);
        } else if (gp.gameState == gp.statsState) {
            handleStatsState(code);
        } else if (gp.gameState == gp.inventoryState) {
            handleInventoryState(code);
        } else if (gp.gameState == gp.cookingState) {
            handleCookingState(code);
        }
    }

    public void handlePlayState(int code) {
        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_P -> gp.gameState = gp.pauseState;
            case KeyEvent.VK_E -> interactPressed = true;
            case KeyEvent.VK_I -> gp.gameState = gp.inventoryState;
            case KeyEvent.VK_C -> gp.gameState = gp.statsState;
            case KeyEvent.VK_ENTER -> enterPressed = true;
            case KeyEvent.VK_SLASH -> {
                gp.openTimeCheatDialog();
                break;
            }
        }
    }

    public void handlePauseState(int code) {
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }
    }

    public void handleDialogState(int code) {
        if (code == KeyEvent.VK_E) {
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }
    }

    public void handleStatsState(int code) {
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }
    }

public void handleInventoryState(int code) {
    switch (code) {
        case KeyEvent.VK_I -> toggleInventoryState();
        case KeyEvent.VK_W, KeyEvent.VK_UP -> {
            gp.inventoryController.moveSelectionUp();
            gp.repaint();
        }
        case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
            gp.inventoryController.moveSelectionDown();
            gp.repaint();
        }
        case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
            gp.inventoryController.moveSelectionLeft();
            gp.repaint();
        }
        case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
            gp.inventoryController.moveSelectionRight();
            gp.repaint();
        }
        case KeyEvent.VK_E -> {
            gp.inventoryController.useItem(gp.inventoryController.getSelectedSlot());
            useItemPressed = true;
            gp.repaint(); 
        }
        case KeyEvent.VK_F -> {
            toggleFilter();
            filterPressed = true;
            gp.repaint(); // Trigger a screen update after changing the filter
        }    
    }
}


    private void toggleInventoryState() {
        if (gp.gameState == gp.playState) {
            gp.gameState = gp.inventoryState;
        } else if (gp.gameState == gp.inventoryState) {
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }
        inventoryPressed = true;
    }

    private void toggleFilter() {
        currentFilterIndex = (currentFilterIndex + 1) % filters.length;
        gp.inventoryController.setFilter(filters[currentFilterIndex]);
    }

    public void handleCookingState(int code) {
        if (code == KeyEvent.VK_E) {
            gp.gameState = gp.playState;
            gp.resumeGameThread();
        }

        if (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9) {
            int digit = code - KeyEvent.VK_0;
            singleNumPress = digit;

            // Build multi-digit number input
            if (digit != 0 || !multiNumPress.isEmpty()) {
                multiNumPress += digit;
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        if (code == KeyEvent.VK_BACK_SPACE) {
            if (!multiNumPress.isEmpty()) {
                multiNumPress = multiNumPress.substring(0, multiNumPress.length() - 1);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
        }
    }
}
