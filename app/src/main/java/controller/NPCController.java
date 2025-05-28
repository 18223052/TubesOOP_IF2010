package controller;

import entity.NPC;
import main.GamePanel;
import object.IItem;

public class NPCController {
    
    private GamePanel gp;

    public NPCController(GamePanel gp){
        this.gp = gp;
    }

    public void giftItemToNPC(IItem itemToGift){
        if (itemToGift == null){
            gp.ui.currentDialog = "Kamu tidak memiliki barang yang akan dijadikan gift";
            gp.gameState = GamePanel.dialogState;
            return;
        }
        
        int pointsToAdd =0;
        String reactionMessage;

        if (gp.currNPC.isLovedItem(itemToGift)){
            pointsToAdd = 25;
            reactionMessage = gp.currNPC.getName() + " mencintai " + itemToGift.getName() + "!";
        } else if (gp.currNPC.isLikedItem(itemToGift)){
            pointsToAdd = 20;
            reactionMessage = gp.currNPC.getName() + " menyukai " + itemToGift.getName() + "!";
        } else {
            if (gp.currNPC.doesHateAllUnlistedItems()){
                pointsToAdd = -25;
                reactionMessage = gp.currNPC.getName() + " sangat amat tidak menyukai" + itemToGift.getName() + "!";
            } else {
                pointsToAdd = 0;
                reactionMessage = gp.currNPC.getName() + " menerima " + itemToGift.getName() + "!";
            }
        }

        gp.currNPC.addHeartPoints(pointsToAdd);

        if (gp.currNPC instanceof entity.NPC_mayortadi && gp.currNPC.getName().equals("Mayor Tadi")){
            gp.ui.currentDialog = ((entity.NPC_mayortadi) gp.currNPC).getGiftReaction(itemToGift, pointsToAdd);
        } else {
           gp.ui.currentDialog = reactionMessage;
        }

        gp.player.inventory.removeItems(itemToGift.getName(), 1);
    }

    public void attemptPropose(NPC targetNPC) {
    System.out.println("=== PROPOSAL DEBUG START ===");
    System.out.println("Current game state: " + gp.gameState);
    System.out.println("Target NPC: " + (targetNPC != null ? targetNPC.getName() : "NULL"));
    System.out.println("Energy sebelum: " + gp.player.getEnergy());
    
    // Validation checks dengan debug
    if (targetNPC == null) {
        System.out.println("ERROR: Target NPC is null");
        return;
    }
    if (gp.player == null) {
        System.out.println("ERROR: Player is null");
        return;
    }
    if (gp.ui == null) {
        System.out.println("ERROR: UI is null");
        return;
    }
    if (gp.gameTime == null) {
        System.out.println("ERROR: GameTime is null");
        return;
    }

    System.out.println("All components valid, proceeding...");
    gp.gameTime.addTime(60); 

    if (!gp.player.inventory.hasItem("ring")) { 
        System.out.println("Player doesn't have ring");
        gp.ui.setDialog("You need a ring to propose."); 
        gp.player.deductEnergy(20); 
        
        // Debug state change
        System.out.println("Setting game state to dialogState...");
        System.out.println("Current state before: " + gp.gameState);
        gp.setGameState(GamePanel.dialogState);
        System.out.println("Current state after: " + gp.gameState);
        
        gp.currNPC = targetNPC; 
        System.out.println("=== PROPOSAL DEBUG END (NO RING) ===");
        return;
    }

    if (targetNPC.isProposable(gp.player)) { 
        System.out.println("Proposal accepted!");
        targetNPC.becomeFiance(gp.player, gp.gameTime.getGameDay()); 
        gp.player.setFiance(targetNPC); 
        gp.player.deductEnergy(10); 
        gp.ui.setDialog(targetNPC.getName() + " joyfully accepts! You are now engaged."); 
        
        // Debug state change
        System.out.println("Setting game state to dialogState...");
        System.out.println("Current state before: " + gp.gameState);
        gp.setGameState(GamePanel.dialogState);
        System.out.println("Current state after: " + gp.gameState);
        
        gp.currNPC = targetNPC; 
    } else {
        System.out.println("Proposal rejected!");
        gp.player.deductEnergy(20);

        String declineMessage = targetNPC.getProposalDeclineMessage();
        System.out.println("Decline message: " + declineMessage);
        gp.ui.setDialog(declineMessage);

        // Debug state change
        System.out.println("Setting game state to dialogState...");
        System.out.println("Current state before: " + gp.gameState);
        gp.setGameState(GamePanel.dialogState);
        System.out.println("Current state after: " + gp.gameState);
        
        gp.currNPC = targetNPC;
    }
    
    System.out.println("Energy sesudah: " + gp.player.getEnergy());
    System.out.println("=== PROPOSAL DEBUG END ===");
}

    public void attemptMarry() {
        // Validation checks
        if (gp.player == null || gp.player.getFiance() == null || gp.ui == null || gp.gameTime == null) { 
            System.err.println("NPCController: Marriage failed, critical component null or player not engaged.");
            if (gp.ui != null) {
                String message = (gp.player != null && gp.player.getFiance() == null) ? 
                            "You are not engaged to anyone." : 
                            "Error: Cannot process marriage.";
                gp.ui.setDialog(message);
                forceDialogState(null);
            }
            return;
        }

        NPC fiance = gp.player.getFiance(); 

        // Check if player has proposal ring
        if (!gp.player.inventory.hasItem("Proposal Ring")) { 
            gp.ui.setDialog("You need the Proposal Ring for the wedding ceremony!"); 
            forceDialogState(fiance);
            return;
        }

        // Attempt marriage
        if (fiance.canMarryPlayer(gp.player, gp.gameTime.getGameDay())) { 
            // Successful marriage
            fiance.marryPlayer(gp.player); 
            gp.player.setSpouse(fiance); 
            gp.player.deductEnergy(80); 

            // Set time and move to home
            if (gp.gameTime.getGameHour() >= 22) { 
                gp.gameTime.nextDay(); 
            }
            gp.gameTime.setTime(22, 0); 

            String homeMapPath = "/maps/rumah.txt"; 
            int homeSpawnTileX = 10; 
            int homeSpawnTileY = 10; 

            gp.currMap = homeMapPath; 
            gp.tileM.loadMap(homeMapPath); 
            gp.player.wX = homeSpawnTileX * gp.tileSize; 
            gp.player.wY = homeSpawnTileY * gp.tileSize; 

            gp.ui.setDialog("Congratulations! You are now married to " + fiance.getName() + 
                            ".\nYou spend the rest of the day with your new spouse until 10:00 PM."); 
            forceDialogState(fiance);
        } else {
            // Marriage not possible yet
            int daysRemaining = 0;
            if (fiance.getDayBecameFiance() > 0 && fiance.getDayBecameFiance() >= gp.gameTime.getGameDay()) { 
                daysRemaining = (fiance.getDayBecameFiance() + 1) - gp.gameTime.getGameDay(); 
            }

            String message = (daysRemaining > 0) ? 
                            "It's too soon for the wedding. Wait " + daysRemaining + " more day(s)." :
                            "The wedding with " + fiance.getName() + " cannot happen right now.";
            
            gp.ui.setDialog(message);
            forceDialogState(fiance);
        }
    }

    /**
     * Helper method to force dialog state and ensure proper state transition
     */
    private void forceDialogState(NPC targetNPC) {
        // Set current NPC
        gp.currNPC = targetNPC;
        
        // Force game state to dialog state
        gp.setGameState(GamePanel.dialogState);
        
        // Resume game thread (dialog state allows time to run)
        if (gp.isTimePaused) {
            gp.resumeGameThread();
        }
        
        // Optional: Add small delay to ensure state change is processed
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
