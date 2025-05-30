package controller;

import entity.NPC;
import main.GamePanel;
import object.IItem;

public class NPCController {
    
    private GamePanel gp;

    public NPCController(GamePanel gp){
        this.gp = gp;
    }

    public void giftItemToNPC(IItem itemToGift) {
        if (gp.currNPC == null) { // Added safety check
            gp.ui.setDialog("Error: No NPC targeted for gifting.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (itemToGift == null) {
            gp.ui.setDialog("You have no item selected to gift."); // Changed message slightly
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        int pointsToAdd = 0;
        String reactionMessage;

        // Determine reaction based on item preferences
        if (gp.currNPC.isLovedItem(itemToGift)) {
            pointsToAdd = 25;
            reactionMessage = gp.currNPC.getName() + " loves " + itemToGift.getName() + "!"; // Adjusted wording slightly
        } else if (gp.currNPC.isLikedItem(itemToGift)) {
            pointsToAdd = 20;
            reactionMessage = gp.currNPC.getName() + " likes " + itemToGift.getName() + "!"; // Adjusted wording slightly
        } else {
            if (gp.currNPC.doesHateAllUnlistedItems()) { // Assuming this means they hate neutral items too
                pointsToAdd = -25; // Or a smaller negative for merely disliked/neutral if hated is different
                reactionMessage = gp.currNPC.getName() + " really dislikes " + itemToGift.getName() + "!";
            } else { // Neutral reaction
                pointsToAdd = 5; // Or 0, or a small positive for politeness
                reactionMessage = gp.currNPC.getName() + " accepts " + itemToGift.getName() + ".";
            }
        }

        gp.currNPC.addHeartPoints(pointsToAdd);

        String finalDialog;
        if (gp.currNPC instanceof entity.NPC_mayortadi && gp.currNPC.getName().equals("Mayor Tadi")) {

            finalDialog = ((entity.NPC_mayortadi) gp.currNPC).getGiftReaction(itemToGift, pointsToAdd);
        } else {
            finalDialog = reactionMessage;
        }
        gp.ui.setDialog(finalDialog); 

        gp.player.inventory.removeItems(itemToGift.getName(), 1);

        gp.setGameState(GamePanel.dialogState); 
        gp.repaint(); 
    }

    public void attemptPropose(NPC targetNPC) {
        System.out.println("Energy sebelum: " + gp.player.getEnergy());
        if (targetNPC == null || gp.player == null || gp.ui == null || gp.gameTime == null) {
            System.err.println("NPCController: Proposal failed, critical component is null.");
            if (gp.ui != null) {
                gp.ui.setDialog("Error: Cannot process proposal at this time."); 
                if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState); 
                gp.repaint();
                gp.currNPC = targetNPC; 
            }
            return;
        }

        gp.gameTime.addTime(60); 

        if (!gp.player.hasItem("ring")) { 
            gp.ui.setDialog("You need a ring to propose."); 
            gp.player.changeEnergy(-20); 
            if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState); 
            gp.repaint();
            gp.currNPC = targetNPC; 
            return;
        }

        if (targetNPC.isProposable(gp.player) && NPC.gender_female.equals(targetNPC.getGender())) { 
            System.out.println("Selamat atas pernikahanmu Na Hee Do");
            targetNPC.becomeFiance(gp.player, gp.gameTime.getGameDay()); 
            gp.player.setFiance(targetNPC); 
            gp.player.changeEnergy(-10); 
            gp.ui.setDialog(targetNPC.getName() + " joyfully accepts! You are now engaged."); 
            if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState); 
            gp.repaint();
            gp.currNPC = targetNPC; 
            gp.update();
        } else {
            System.out.println(gp.gameState);
            System.out.println("Kamu terlalu baik buat aku");
            gp.player.changeEnergy(-20); 
            String reason = "They are not ready for such a commitment right now.";
        
            if (targetNPC.getHeartPoints() < targetNPC.getMaxHeartPoint()) {
                reason = targetNPC.getName() + " needs to feel a stronger connection."; 
            } else if (!NPC.STATUS_SINGLE.equals(targetNPC.getRelationshipStatus())) { 
                reason = targetNPC.getName() + " is not available for a relationship."; 
            } else if (gp.player.hasFiance() || gp.player.hasSpouse()) { 
                reason = "You are already in a relationship!";
            }
            gp.ui.setDialog("Proposal to " + targetNPC.getName() + " was declined. " + reason); 
            if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState); 
            gp.repaint();
            gp.currNPC = targetNPC;
        }
        System.out.println("Energy sesudah: " + gp.player.getEnergy());
    }

    public void attemptMarry() {

        if (gp.player == null || gp.player.getFiance() == null || gp.ui == null || gp.gameTime == null) { 
            System.err.println("NPCController: Marriage failed, critical component null or player not engaged.");
            if (gp.ui != null) {
                gp.ui.setDialog(gp.player != null && gp.player.getFiance() == null ? "You are not engaged to anyone." : "Error: Cannot process marriage."); //
                if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState); 
            }
            return;
        }

        NPC fiance = gp.player.getFiance(); 

        if (!gp.player.hasItem("ring")) { 
            gp.ui.setDialog("You need the Ring for the wedding ceremony!"); 
            if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState); 
            return;
        }
        
        System.out.println("DEBUG (AttemptMarry): gp.currMap saat ini: " + gp.currMap);
        if (fiance.canMarryPlayer(gp.player, gp.gameTime.getGameDay())) { 
            fiance.marryPlayer(gp.player); 
            gp.player.setSpouse(fiance); 
            gp.player.changeEnergy(-80); 

            if (gp.gameTime.getGameHour() >= 22) { 
                gp.gameTime.nextDay(); 
            }
            gp.gameTime.setTime(22, 0); 

            String homeMapPath = "/maps/rumah.txt"; 
            int homeSpawnTileX = 26; 
            int homeSpawnTileY = 36; 

            gp.tileM.teleportPlayer(homeMapPath, homeSpawnTileX, homeSpawnTileY); 
            gp.repaint();

            

            gp.ui.setDialog("Congratulations! You are now married to " + fiance.getName() + //
                            ".\nYou spend the rest of the day with your new spouse until 10:00 PM."); //
            if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState); //
            gp.currNPC = fiance; //
        } else {
            int daysRemaining = 0;
            if (fiance.getDayBecameFiance() > 0 && fiance.getDayBecameFiance() >= gp.gameTime.getGameDay()) { //
                daysRemaining = (fiance.getDayBecameFiance() + 1) - gp.gameTime.getGameDay(); //
            }

            if (daysRemaining > 0) {
                 gp.ui.setDialog("It's too soon for the wedding. Wait " + daysRemaining + " more day(s)."); //
            } else {
                gp.ui.setDialog("The wedding with " + fiance.getName() + " cannot happen right now."); //
            }
            if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState); //
            gp.currNPC = fiance; //
        }
    }
}
