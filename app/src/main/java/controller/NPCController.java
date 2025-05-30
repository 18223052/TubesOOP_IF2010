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
        System.out.println("Energy sebelum: " + gp.player.getEnergy());
        if (targetNPC == null || gp.player == null || gp.ui == null || gp.gameTime == null) {
            System.err.println("NPCController: Proposal failed, critical component is null.");
            if (gp.ui != null) {
                gp.ui.setDialog("Error: Cannot process proposal at this time."); 
                if (gp.gameState != gp.dialogState) gp.setGameState(gp.dialogState); 
                gp.repaint();
                gp.currNPC = targetNPC; 
            }
            return;
        }

        gp.gameTime.addTime(60); 

        if (!gp.player.hasItem("ring")) { 
            gp.ui.setDialog("You need a ring to propose."); 
            gp.player.changeEnergy(-20); 
            if (gp.gameState != gp.dialogState) gp.setGameState(gp.dialogState); 
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
            if (gp.gameState != gp.dialogState) gp.setGameState(gp.dialogState); 
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
            if (gp.gameState != gp.dialogState) gp.setGameState(gp.dialogState); 
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
                if (gp.gameState != gp.dialogState) gp.setGameState(gp.dialogState); 
            }
            return;
        }

        NPC fiance = gp.player.getFiance(); 

        if (!gp.player.hasItem("ring")) { 
            gp.ui.setDialog("You need the Ring for the wedding ceremony!"); 
            if (gp.gameState != gp.dialogState) gp.setGameState(gp.dialogState); 
            return;
        }

        if (fiance.canMarryPlayer(gp.player, gp.gameTime.getGameDay())) { 
            fiance.marryPlayer(gp.player); 
            gp.player.setSpouse(fiance); 
            gp.player.changeEnergy(-80); 

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
            
            // gp.aSetter.setNPC(); // Pertimbangkan jika NPC perlu di-reset di peta baru
            // gp.aSetter.setObject(); // Pertimbangkan jika objek perlu di-reset di peta baru

            gp.ui.setDialog("Congratulations! You are now married to " + fiance.getName() + //
                            ".\nYou spend the rest of the day with your new spouse until 10:00 PM."); //
            if (gp.gameState != gp.dialogState) gp.setGameState(gp.dialogState); //
            gp.currNPC = fiance; //
        } else {
            int daysRemaining = 0;
            // Asumsi ada getter publik getDayBecameFiance() di NPC.java
            if (fiance.getDayBecameFiance() > 0 && fiance.getDayBecameFiance() >= gp.gameTime.getGameDay()) { //
                daysRemaining = (fiance.getDayBecameFiance() + 1) - gp.gameTime.getGameDay(); //
            }

            if (daysRemaining > 0) {
                 gp.ui.setDialog("It's too soon for the wedding. Wait " + daysRemaining + " more day(s)."); //
            } else {
                gp.ui.setDialog("The wedding with " + fiance.getName() + " cannot happen right now."); //
            }
            if (gp.gameState != gp.dialogState) gp.setGameState(gp.dialogState); //
            gp.currNPC = fiance; //
        }
    }
}
