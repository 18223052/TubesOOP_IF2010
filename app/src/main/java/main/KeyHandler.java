package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import object.IItem;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed, enterPressed;
    public boolean giftPressed, confirmPressed, cancelPressed;
    public boolean inventoryPressed; 
    public boolean useItemPressed, discardItemPressed, sellItemPressed;
    public boolean filterPressed;
    public int singleNumPress; 
    public String multiNumPress = ""; 
    public boolean giftKeyPressed; 
    

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (gp.gameState == GamePanel.nameInputState){
            char c = e.getKeyChar();
 
            if (Character.isLetterOrDigit(c) || c == ' '){
                if (gp.playerNameInput.length() < 12){ 
                    gp.playerNameInput += String.valueOf(c);
                    // Clear error message when user starts typing
                    gp.ui.nameInputScreen.setErrorMessage(""); 
                }
            } else if (c == KeyEvent.VK_BACK_SPACE){
                if (gp.playerNameInput.length() > 0){
                    gp.playerNameInput = gp.playerNameInput.substring(0,gp.playerNameInput.length() - 1);
                    // Clear error message when user starts typing
                    gp.ui.nameInputScreen.setErrorMessage(""); 
                }
            }

            gp.repaint(); 
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();


        if (gp.gameState == GamePanel.titleState) {
            handleTitleState(code);
        } else if (gp.gameState == GamePanel.nameInputState) {
            handleNameInputState(code);
        }
        else if (gp.gameState == GamePanel.helpState) {
            handleHelpState(code);
        } else if (gp.gameState == GamePanel.creditState) {
            handleCreditsState(code);
        }
        else if (gp.gameState == GamePanel.playState) {
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


    public void handleTitleState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.titleScreen.moveSelectionUp();
            gp.repaint();
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.titleScreen.moveSelectionDown();
            gp.repaint();
        }
        if (code == KeyEvent.VK_ENTER) {
            int selectedCommand = gp.ui.getCommandNum();
            if (selectedCommand == 0) {
                gp.setGameState(GamePanel.nameInputState); 
            } else if (selectedCommand == 1) {
                gp.setGameState(GamePanel.helpState);
                gp.repaint();
            } else if (selectedCommand == 2) {
                gp.setGameState(GamePanel.creditState);
                gp.repaint();
            } else if (selectedCommand == 3) {
                System.exit(0);
            }
        }
    }

    public void handleHelpState(int code) {

        if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
            gp.setGameState(GamePanel.titleState);
        }
    }


    public void handleCreditsState(int code) {

        if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
            gp.setGameState(GamePanel.titleState);
        }
    }

    public void handleNameInputState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            if (!gp.playerNameInput.trim().isEmpty()) {
                gp.ui.nameInputScreen.setErrorMessage(""); // Clear error on successful input
                gp.startGame(); 
            } else {
                gp.ui.nameInputScreen.setErrorMessage("Nama tidak boleh kosong!"); // Set error message
                gp.repaint();
            }
        }
    }



    public void handlePlayState(int code) {
        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_P -> gp.setGameState(GamePanel.pauseState); 
            case KeyEvent.VK_E -> interactPressed = true;
            case KeyEvent.VK_I -> {
                gp.setGameState(GamePanel.inventoryState);
                gp.isGifting = false;
            
            }
            case KeyEvent.VK_C -> gp.setGameState(GamePanel.statsState); 
            case KeyEvent.VK_ENTER -> enterPressed = true;
            case KeyEvent.VK_SLASH -> {
                gp.openTimeCheatDialog();
            }
        }
    }

    public void handlePauseState(int code) {
        if (code == KeyEvent.VK_P) {
            gp.setGameState(GamePanel.playState); 
        }
    }

    public void handleDialogState(int code) {
        if (code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER) {
            gp.setGameState(GamePanel.playState); 
            gp.isTimePaused = false;
            if (gp.ui != null) { 
            gp.ui.clearDialog();
            }
            gp.repaint();
        }
    }

    public void handleStatsState(int code) {
        if (code == KeyEvent.VK_C) {
            gp.setGameState(GamePanel.playState); 
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

                if (gp.gameState == GamePanel.sleepState) {

                    return; 
                }

            } else {
                gp.ui.setDialog("No item selected to gift.");
            }

          
            gp.isGifting = false;
            gp.setGameState(GamePanel.dialogState);


            break;
        case KeyEvent.VK_ESCAPE:
            gp.isGifting = false;
            gp.setGameState(GamePanel.playState);
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
            case KeyEvent.VK_I -> toggleInventoryState();
            case KeyEvent.VK_W, KeyEvent.VK_UP -> gp.inventoryController.moveSelectionUp();
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> gp.inventoryController.moveSelectionDown();
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> gp.inventoryController.moveSelectionLeft();
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> gp.inventoryController.moveSelectionRight();
            case KeyEvent.VK_E -> {
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
        
        }
        inventoryPressed = true; 
    }

    private void toggleShippingBinState() {
        if (gp.gameState == GamePanel.playState) {
            gp.setGameState(GamePanel.shippingBinState);
        } else if (gp.gameState == GamePanel.shippingBinState) {
            gp.setGameState(GamePanel.playState);
        
        }
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
            case KeyEvent.VK_ENTER -> {
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
                interactPressed = true; 
                if (gp.currNPC != null) {
                    gp.npcController.initiateChatWithCurrentNPC();
                
                }
                break;
            case KeyEvent.VK_G:
                giftKeyPressed = true; 
                gp.isGifting = true;
                gp.setGameState(GamePanel.inventoryState);
                gp.inventoryController.setSelectedSlot(0); 
                gp.repaint();
            
                break;
            case KeyEvent.VK_ESCAPE:
                gp.setGameState(GamePanel.playState);
                gp.currNPC = null; 
                gp.isGifting = false;
            
                break;
            case KeyEvent.VK_P: // Propose
                if (gp.npcController != null && gp.currNPC != null) {
                    if (gp.currNPC.isProposable(gp.player)){
                        System.out.println("DEBUG PROPOSAL: Bisa dilamar");
                        gp.npcController.attemptPropose(gp.currNPC);
                    } else {
                        System.out.println("DEBUG PROPOSAL: Gabisa dilamar");
                        gp.npcController.attemptPropose(gp.currNPC);
                    }

                    if (gp.player.inventory.hasItem("ring")) {
                        if (gp.currNPC.isProposable(gp.player)){
                            gp.npcController.attemptPropose(gp.currNPC); 
                            System.out.println("Melamar...");
                        } 
                    } else {

                        if (gp.ui != null) { //
                            gp.ui.setDialog("You need a Ring to propose."); 

                            gp.setGameState(GamePanel.dialogState); //

                        }
                    }
                }
                break;

            case KeyEvent.VK_M: // Marry
                if (gp.npcController != null) {

                    boolean canTryToMarry = gp.player.getFiance() == gp.currNPC && 
                                             gp.player.inventory.hasItem("ring"); 


                    if (canTryToMarry) {
                        gp.npcController.attemptMarry();
                    } else {
                        if (gp.ui != null) { 
                            String message = "Cannot marry " + gp.currNPC.getName() + " at this time."; 
                            if (gp.player.getFiance() != gp.currNPC) { 
                                message = "This is not your fiance!";
                            } else if (!gp.player.inventory.hasItem("ring")) { 
                                message = "You need the Proposal Ring for the ceremony!";
                            }

                            gp.ui.setDialog(message); 
                            gp.setGameState(GamePanel.dialogState); 
                            gp.repaint();
                        }
                    }
                }
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
        
        }
    }

    public void handleCookingState(int code) {
        boolean stateChanged = false;

        switch (code) {
            case KeyEvent.VK_E:
            case KeyEvent.VK_ESCAPE:
                gp.setGameState(GamePanel.playState);
                // Reset cooking menu state variables
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
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_E -> interactPressed = false;
            case KeyEvent.VK_F -> filterPressed = false;
            case KeyEvent.VK_DELETE -> discardItemPressed = false;
            case KeyEvent.VK_ENTER -> {
                enterPressed = false;
                sellItemPressed = false; 
                useItemPressed = false; 
            }
            case KeyEvent.VK_I -> inventoryPressed = false; 
            case KeyEvent.VK_G -> giftKeyPressed = false; 
        }
    }
}