// File: KeyHandler.java

package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import object.BaseItem;
import object.IItem;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed, enterPressed;
    public boolean giftPressed, confirmPressed, cancelPressed;
    public boolean inventoryPressed;
    public boolean useItemPressed, discardItemPressed, sellItemPressed;
    public boolean filterPressed;
    private int currentFilterIndex = 0;
    private String[] filters = {"all", "tools", "consumables", "crops", "fish", "seeds", "fuel"};
    StringBuilder inputBuffer = new StringBuilder();
    public int singleNumPress;
    public String multiNumPress = "";
    public boolean giftKeyPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Ganti gp.STATE_NAME menjadi GamePanel.STATE_NAME di semua tempat
        if (gp.gameState == GamePanel.playState) {
            gp.isTimePaused = false;
            handlePlayState(code);
        } else if (gp.gameState == GamePanel.pauseState) {
            gp.isTimePaused = true;
            handlePauseState(code);
        } else if (gp.gameState == GamePanel.dialogState) {
            handleDialogState(code);
        } else if (gp.gameState == GamePanel.statsState) { 
            handleStatsState(code);
        } else if (gp.gameState == GamePanel.inventoryState) { 
            handleInventoryState(code);
        } else if (gp.gameState == GamePanel.cookingState) { 
            handleCookingState(code);
        } else if (gp.gameState == GamePanel.shippingBinState) { 
            handleShippingBinState(code);
        } else if (gp.gameState == GamePanel.storeState) { 
            handleStoreState(code);
        } else if (gp.gameState == GamePanel.npcContextMenuState){ 
            handleNpcContextMenuState(code);
        }
    }

    public void handlePlayState(int code) {
        switch (code) {
            case KeyEvent.VK_W ->
                upPressed = true;
            case KeyEvent.VK_S ->
                downPressed = true;
            case KeyEvent.VK_A ->
                leftPressed = true;
            case KeyEvent.VK_D ->
                rightPressed = true;
            case KeyEvent.VK_P ->
                gp.gameState = GamePanel.pauseState; 
            case KeyEvent.VK_E ->
                interactPressed = true;
            case KeyEvent.VK_I -> {
                gp.setGameState(GamePanel.inventoryState); 
                gp.isGifting = false;
                gp.resumeGameThread();
            }
            case KeyEvent.VK_C ->
                gp.gameState = GamePanel.statsState; 
            case KeyEvent.VK_ENTER ->
                enterPressed = true;
            case KeyEvent.VK_SLASH -> {
                gp.openTimeCheatDialog();
                break;
            }
        }
    }

    public void handlePauseState(int code) {
        if (code == KeyEvent.VK_P) {
            gp.gameState = GamePanel.playState;
            gp.resumeGameThread();
        }
    }

    public void handleDialogState(int code) {
        if (code == KeyEvent.VK_E) {
            gp.setGameState(GamePanel.playState);
            gp.resumeGameThread();
        }
        // if (code == KeyEvent.VK_S) {
        //     if (gp.currNPC != null && gp.currNPC.hasStore()) {
        //         gp.setGameState(GamePanel.storeState); 
        //         gp.repaint();
        //     }
        // }
    }

    public void handleStatsState(int code) {
        if (code == KeyEvent.VK_C) {
            gp.setGameState(GamePanel.playState);
            gp.resumeGameThread();
        }
    }

    public void handleInventoryState(int code) {
        if (gp.isGifting) {
            switch (code) {
                case KeyEvent.VK_E:
                    IItem selectedItem = gp.inventoryController.getSelectedItem();
                    if (selectedItem instanceof BaseItem) {
                        gp.npcController.giftItemToNPC((BaseItem) selectedItem);
                    } else {
                        gp.ui.setDialog("That item cannot be gifted.");
                        gp.setGameState(GamePanel.dialogState); 
                    }
                    gp.isGifting = false;
                    gp.setGameState(GamePanel.dialogState); 
                    gp.resumeGameThread();
                    break;
                case KeyEvent.VK_ESCAPE:
                    gp.isGifting = false;
                    gp.setGameState(GamePanel.playState);
                    gp.resumeGameThread();
                    break;
                case KeyEvent.VK_W, KeyEvent.VK_UP:
                    gp.inventoryController.moveSelectionUp();
                    gp.repaint();
                    break;
                case KeyEvent.VK_S, KeyEvent.VK_DOWN:
                    gp.inventoryController.moveSelectionDown();
                    gp.repaint();
                    break;
                case KeyEvent.VK_A, KeyEvent.VK_LEFT:
                    gp.inventoryController.moveSelectionLeft();
                    gp.repaint();
                    break;
                case KeyEvent.VK_D, KeyEvent.VK_RIGHT:
                    gp.inventoryController.moveSelectionRight();
                    gp.repaint();
                    break;
                case KeyEvent.VK_F:
                    toggleFilter();
                    filterPressed = true;
                    gp.repaint();
                    break;
            }
        } else { // Normal inventory navigation and actions
            switch (code) {
                case KeyEvent.VK_I ->
                    toggleInventoryState();
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
                    gp.repaint();
                }
                case KeyEvent.VK_DELETE -> {
                    gp.inventoryController.discardItem(gp.inventoryController.getSelectedSlot());
                    discardItemPressed = true;
                    gp.repaint();
                    break;
                }
            }
        }
    }

    private void toggleInventoryState() {
        if (gp.gameState == GamePanel.playState) {
            gp.setGameState(GamePanel.inventoryState); 
        } else if (gp.gameState == GamePanel.inventoryState) { 
            gp.setGameState(GamePanel.playState);
            gp.resumeGameThread();
        }
        inventoryPressed = true;
    }

    public void handleShippingBinState(int code) {
        switch (code) {
            case KeyEvent.VK_E ->
                toggleShippingBinState();
            case KeyEvent.VK_W -> {
                gp.shippingBinController.moveSelectionUp();
                gp.repaint();
            }
            case KeyEvent.VK_S -> {
                gp.shippingBinController.moveSelectionDown();
                gp.repaint();
            }
            case KeyEvent.VK_A -> {
                gp.shippingBinController.moveSelectionLeft();
                gp.repaint();
            }
            case KeyEvent.VK_D -> {
                gp.shippingBinController.moveSelectionRight();
                gp.repaint();
            }
            case KeyEvent.VK_UP -> {
                gp.inventoryController.moveSelectionUp();
                gp.repaint();
            }
            case KeyEvent.VK_DOWN -> {
                gp.inventoryController.moveSelectionDown();
                gp.repaint();
            }
            case KeyEvent.VK_LEFT -> {
                gp.inventoryController.moveSelectionLeft();
                gp.repaint();
            }
            case KeyEvent.VK_RIGHT -> {
                gp.inventoryController.moveSelectionRight();
                gp.repaint();
            }
            case KeyEvent.VK_F -> {
                toggleFilter();
                filterPressed = true;
                gp.repaint(); // Trigger a screen update after changing the filter
            }
            case KeyEvent.VK_ENTER -> {
                gp.inventoryController.sellItem(gp.inventoryController.getSelectedSlot());
                sellItemPressed = true;
                gp.repaint();
            }

        }
    }

    private void toggleShippingBinState() {
        if (gp.gameState == GamePanel.playState) {
            gp.setGameState(GamePanel.shippingBinState); 
        } else if (gp.gameState == GamePanel.shippingBinState) { 
            gp.setGameState(GamePanel.playState);
            gp.resumeGameThread();
        }
        inventoryPressed = true;
    }

    public void handleStoreState(int code) {
        switch (code) {
            case KeyEvent.VK_E ->
                toggleStoreState();
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                gp.storeController.moveSelectionUp();
                gp.repaint();
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                gp.storeController.moveSelectionDown();
                gp.repaint();
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                gp.storeController.moveSelectionLeft();
                gp.repaint();
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                gp.storeController.moveSelectionRight();
                gp.repaint();
            }
            case KeyEvent.VK_F -> {
                toggleFilter();
                filterPressed = true;
                gp.repaint();
            }
            case KeyEvent.VK_ENTER -> {
                gp.storeController.sellItem(gp.storeController.getSelectedSlot());
                sellItemPressed = true;
                gp.repaint();
            }
        }
    }

    public void handleNpcContextMenuState(int code) {
        switch (code) {
            case KeyEvent.VK_E:
                interactPressed = true;
                if (gp.currNPC != null) {
                    gp.currNPC.speak();
                    gp.resumeGameThread();
                }
                break;
            case KeyEvent.VK_G:
                giftKeyPressed = true;
                gp.isGifting = true;
                gp.setGameState(GamePanel.inventoryState); 
                gp.inventoryController.setSelectedSlot(0);
                gp.resumeGameThread();
                break;
            case KeyEvent.VK_ESCAPE:
                gp.setGameState(GamePanel.playState); 
                gp.currNPC = null;
                gp.isGifting = false;
                gp.resumeGameThread();
                break;
            case KeyEvent.VK_S:
                if (gp.currNPC != null && gp.currNPC.hasStore()) {
                    gp.setGameState(GamePanel.storeState); 
                    gp.repaint();
                }
                break;
        }
    }

    private void toggleStoreState() {
        if (gp.gameState == GamePanel.storeState) { 
            gp.setGameState(GamePanel.playState);
            gp.resumeGameThread();
        }
        inventoryPressed = true;
    }

    private void toggleFilter() {
        currentFilterIndex = (currentFilterIndex + 1) % filters.length;
        gp.inventoryController.setFilter(filters[currentFilterIndex]);
    }

    public void handleCookingState(int code) {
        boolean stateChanged = false;

        switch (code) {
            case KeyEvent.VK_E:
            case KeyEvent.VK_ESCAPE:
                gp.setGameState(GamePanel.playState); 
                gp.resumeGameThread();
                gp.ui.cookingMenu.selectRecipe = 0;
                gp.ui.cookingMenu.cookingMenuSelection = 0;
                gp.ui.cookingMenu.doneCooking = false;
                return;

            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (gp.ui.cookingMenu.selectRecipe == 0) {
                    gp.ui.cookingMenu.moveSelectionUp();
                    stateChanged = true;
                }
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (gp.ui.cookingMenu.selectRecipe == 0) {
                    gp.ui.cookingMenu.moveSelectionDown();
                    stateChanged = true;
                }
                break;

            case KeyEvent.VK_ENTER:
                if (gp.ui.cookingMenu.doneCooking || gp.ui.cookingMenu.selectRecipe == -1) {
                    gp.ui.cookingMenu.selectRecipe = 0;
                    gp.ui.cookingMenu.doneCooking = false;
                } else if (gp.ui.cookingMenu.selectRecipe == 0) {
                    gp.ui.cookingMenu.selectChosenRecipe();
                } else if (gp.ui.cookingMenu.selectRecipe > 0) {
                    gp.ui.cookingMenu.attemptToCookSelectedRecipe();
                }
                stateChanged = true;
                break;

            case KeyEvent.VK_0:
            case KeyEvent.VK_BACK_SPACE:
                if (gp.ui.cookingMenu.selectRecipe != 0) {
                    gp.ui.cookingMenu.selectRecipe = 0;
                    gp.ui.cookingMenu.doneCooking = false;
                    stateChanged = true;
                }
                break;
        }

        if (stateChanged && gp.gameState == GamePanel.cookingState) { 
            gp.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W ->
                upPressed = false;
            case KeyEvent.VK_S ->
                downPressed = false;
            case KeyEvent.VK_A ->
                leftPressed = false;
            case KeyEvent.VK_D ->
                rightPressed = false;
            case KeyEvent.VK_E ->
                interactPressed = false;
        }
    }
}