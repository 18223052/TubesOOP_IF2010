package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import object.BaseItem;
import object.IItem;
import object.InventorySlot; // Import InventorySlot if you need to access its properties directly

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed, enterPressed;
    public boolean giftPressed, confirmPressed, cancelPressed;
    public boolean inventoryPressed; // This boolean seems to be used as a flag for state transitions.
    public boolean useItemPressed, discardItemPressed, sellItemPressed;
    public boolean filterPressed;
    public int singleNumPress; // Seems unused, consider removing if not in use.
    public String multiNumPress = ""; // Seems unused, consider removing if not in use.
    public boolean giftKeyPressed; // This boolean also seems to be used as a flag for state transitions.


    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used for game actions, typically for text input
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Use GamePanel.STATE_NAME for clarity
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
        } else if (gp.gameState == GamePanel.npcContextMenuState) {
            handleNpcContextMenuState(code);
        }
    }

    public void handlePlayState(int code) {
        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_P -> gp.gameState = GamePanel.pauseState;
            case KeyEvent.VK_E -> interactPressed = true;
            case KeyEvent.VK_I -> {
                gp.setGameState(GamePanel.inventoryState);
                gp.isGifting = false;
                gp.resumeGameThread();
            }
            case KeyEvent.VK_C -> gp.gameState = GamePanel.statsState;
            case KeyEvent.VK_ENTER -> enterPressed = true;
            case KeyEvent.VK_SLASH -> {
                gp.openTimeCheatDialog();
                // No break needed here due to the return in openTimeCheatDialog()
                // If it doesn't return, add a break;
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
    }

    public void handleStatsState(int code) {
        if (code == KeyEvent.VK_C) {
            gp.setGameState(GamePanel.playState);
            gp.resumeGameThread();
        }
    }

    public void handleInventoryState(int code) {
        if (gp.isGifting) {
            handleGiftingInventoryState(code);
        } else { 
            handleNormalInventoryState(code);
        }
        gp.repaint(); 
    }

    private void handleGiftingInventoryState(int code) {
        switch (code) {
            case KeyEvent.VK_E:
                IItem selectedItem = gp.inventoryController.getSelectedItem();
                if (selectedItem != null) { 
                    gp.npcController.giftItemToNPC(selectedItem);
                } else {
                    gp.ui.setDialog("No item selected to gift.");
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
                break;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN:
                gp.inventoryController.moveSelectionDown();
                break;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT:
                gp.inventoryController.moveSelectionLeft();
                break;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT:
                gp.inventoryController.moveSelectionRight();
                break;
        }
    }

    private void handleNormalInventoryState(int code) {
        switch (code) {
            case KeyEvent.VK_I -> toggleInventoryState(); // Close inventory
            case KeyEvent.VK_W, KeyEvent.VK_UP -> gp.inventoryController.moveSelectionUp();
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> gp.inventoryController.moveSelectionDown();
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> gp.inventoryController.moveSelectionLeft();
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> gp.inventoryController.moveSelectionRight();
            case KeyEvent.VK_E -> {
                // Ensure a slot is selected before attempting to use it
                if (gp.inventoryController.getSelectedSlotItem() != null) {
                    gp.inventoryController.useItem(gp.inventoryController.getSelectedSlotIndex());
                }
                useItemPressed = true; 
            }
            case KeyEvent.VK_DELETE -> {
                if (gp.inventoryController.getSelectedSlotItem() != null) {
                    gp.inventoryController.discardItem(gp.inventoryController.getSelectedSlotIndex());
                }
                discardItemPressed = true; 
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
        inventoryPressed = true; // Signal that inventory state was toggled
    }

    private void toggleShippingBinState() {
        // This method seems to toggle between playState and shippingBinState
        // The original code implies that pressing 'E' might also *open* it from playState.
        // Re-evaluate if this should strictly be for closing the shipping bin.
        if (gp.gameState == GamePanel.playState) {
            gp.setGameState(GamePanel.shippingBinState);
        } else if (gp.gameState == GamePanel.shippingBinState) {
            gp.setGameState(GamePanel.playState);
            gp.resumeGameThread();
        }
        // inventoryPressed = true; // This flag name is confusing here, maybe a more generic 'uiToggled'
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
            // case KeyEvent.VK_F -> {
            //     toggleFilter();
            //     filterPressed = true;
            //     gp.repaint(); // Trigger a screen update after changing the filter
            // }
            case KeyEvent.VK_ENTER -> {
                // Ensure a slot is selected before attempting to sell
                if (gp.inventoryController.getSelectedSlotItem() != null) {
                    gp.inventoryController.sellItem(gp.inventoryController.getSelectedSlotIndex());
                }
                sellItemPressed = true;
                gp.repaint();
            }

        }
    }


    public void handleStoreState(int code) {
        switch (code) {
            case KeyEvent.VK_E -> toggleStoreState(); 
            case KeyEvent.VK_W, KeyEvent.VK_UP -> gp.storeController.moveSelectionUp();
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> gp.storeController.moveSelectionDown();
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> gp.storeController.moveSelectionLeft();
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> gp.storeController.moveSelectionRight();
            // case KeyEvent.VK_F -> {
            //     toggleFilter(); 
            //     filterPressed = true;
            // }
            case KeyEvent.VK_ENTER -> {
                if (gp.storeController.getSelectedItem() != null) { 
                    gp.storeController.sellItem(gp.storeController.getSelectedSlot()); 
                }
                sellItemPressed = true;
            }
        }
        gp.repaint(); 
    }

    public void handleNpcContextMenuState(int code) {
        switch (code) {
            case KeyEvent.VK_E:
                interactPressed = true; // Set flag
                if (gp.currNPC != null) {
                    gp.currNPC.speak();
                    gp.resumeGameThread();
                }
                break;
            case KeyEvent.VK_G:
                giftKeyPressed = true; // Set flag
                gp.isGifting = true;
                gp.setGameState(GamePanel.inventoryState);
                gp.inventoryController.setSelectedSlot(0); // Reset selection when entering gifting mode
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
                }
                break;
        }
    }

    private void toggleStoreState() {
        if (gp.gameState == GamePanel.storeState) {
            gp.setGameState(GamePanel.playState);
            gp.resumeGameThread();
        }
        // inventoryPressed = true; // This flag is still ambiguously named
    }

    // private void toggleFilter() {
    //     currentFilterIndex = (currentFilterIndex + 1) % filters.length;
    //     gp.inventoryController.setFilter(filters[currentFilterIndex]);
    //     // No repaint here, as handleInventoryState/handleShippingBinState/handleStoreState call it.
    // }

    public void handleCookingState(int code) {
        boolean stateChanged = false;

        switch (code) {
            case KeyEvent.VK_E:
            case KeyEvent.VK_ESCAPE:
                gp.setGameState(GamePanel.playState);
                gp.resumeGameThread();
                // Reset cooking menu state variables
                gp.ui.cookingMenu.selectRecipe = 0;
                gp.ui.cookingMenu.cookingMenuSelection = 0;
                gp.ui.cookingMenu.doneCooking = false;
                return; // Exit method immediately

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

            case KeyEvent.VK_0: // This key might be for inputting quantity, verify its intent
            case KeyEvent.VK_BACK_SPACE:
                if (gp.ui.cookingMenu.selectRecipe != 0) { // Go back from recipe details to recipe list
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
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_E -> interactPressed = false;
            // Reset action flags to avoid continuous action
            case KeyEvent.VK_F -> filterPressed = false;
            case KeyEvent.VK_DELETE -> discardItemPressed = false;
            case KeyEvent.VK_ENTER -> {
                enterPressed = false;
                sellItemPressed = false; // Reset sell flag
                useItemPressed = false; // Reset use flag
            }
            case KeyEvent.VK_I -> inventoryPressed = false; // Reset inventory toggle flag
            case KeyEvent.VK_G -> giftKeyPressed = false; // Reset gift key flag
        }
    }
}