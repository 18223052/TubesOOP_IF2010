package controller;

import entity.NPC;
import entity.Player;
import main.GamePanel;
import object.IItem;

public class NPCController {
    
    private GamePanel gp;

    public NPCController(GamePanel gp){
        this.gp = gp;
    }

    public void initiateChatWithCurrentNPC() {
        if (gp.currNPC == null || gp.player == null || gp.ui == null || gp.gameTime == null) {
            System.err.println("NPCController: Chat initiation failed, critical component is null.");
            if (gp.ui != null) {
                gp.ui.setDialog("Error: Tidak dapat memulai percakapan saat ini.");
                if (gp.gameState != GamePanel.dialogState) {
                    gp.setGameState(GamePanel.dialogState);
                }
            }
            return;
        }

        int energyAfterChat = gp.player.getEnergy() - NPC.CHAT_ENERGY_COST;

        if (energyAfterChat < Player.MIN_ENERGY_BEFORE_SLEEP) {
            gp.ui.setDialog("Kamu terlalu lelah untuk melakukan chat sekarang.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        gp.player.deductEnergy(NPC.CHAT_ENERGY_COST);

        if (gp.gameState != GamePanel.sleepState) {
            gp.gameTime.addTime(10); // Gunakan konstanta
            gp.currNPC.addHeartPoints(10); // Gunakan konstanta
            gp.currNPC.recordChatSession();
            gp.currNPC.speak();
            System.out.println("Total times chatted with " + gp.currNPC.getName() + ": " + gp.currNPC.getTotalTimesChatted());
        } else {
            System.out.println("NPCController: Chat dibatalkan karena pemain pingsan setelah pengurangan energi.");
        }
    }


public void giftItemToNPC(IItem itemToGift) {

    if (gp.currNPC == null) {
        gp.ui.setDialog("Error: Tidak ada NPC yang ditargetkan untuk diberi hadiah.");
        gp.setGameState(GamePanel.dialogState);
        return;
    }

    if (itemToGift == null) {
        gp.ui.setDialog("Kamu tidak memiliki item yang dipilih untuk diberikan.");
        gp.setGameState(GamePanel.dialogState);
        return;
    }

    if (!gp.player.inventory.hasItem(itemToGift.getName())) {
        gp.ui.setDialog("Kamu tidak memiliki item di inventaris.");
        gp.setGameState(GamePanel.dialogState);
        return;
    }

    int predictedEnergyAfterGift = gp.player.getEnergy() - NPC.GIFT_ENERGY_COST;

    if (predictedEnergyAfterGift < Player.MIN_ENERGY_BEFORE_SLEEP) {
        gp.ui.setDialog("Kamu terlalu lelah untuk memberi hadiah sekarang. Pulang dan istirahatlah.");
        gp.setGameState(GamePanel.dialogState);
        gp.repaint();
        return; 
    }



  
    gp.player.deductEnergy(NPC.GIFT_ENERGY_COST);

   
    if (gp.gameState == GamePanel.sleepState) {
        System.out.println("NPCController: Pemberian hadiah dibatalkan karena pemain pingsan setelah pengurangan energi.");

        gp.repaint(); 
        return; 
    }

    int pointsToAdd = 0;
    String reactionMessage;

    if (gp.currNPC.isLovedItem(itemToGift)) {
        pointsToAdd = 25;
        reactionMessage = gp.currNPC.getName() + " loves " + itemToGift.getName() + "!";
    } else if (gp.currNPC.isLikedItem(itemToGift)) {
        pointsToAdd = 20;
        reactionMessage = gp.currNPC.getName() + " likes " + itemToGift.getName() + "!";
    } else {
        if (gp.currNPC.doesHateAllUnlistedItems()) {
            pointsToAdd = -25;
            reactionMessage = gp.currNPC.getName() + " really dislikes " + itemToGift.getName() + "!";
        } else {
            pointsToAdd = 5;
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

    gp.gameTime.addTime(10);
    gp.player.incrementGiftingCount();
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

    final int PROPOSE_ENERGY_COST = 20; 

    int predictedEnergyAfterPropose = gp.player.getEnergy() - PROPOSE_ENERGY_COST;

    if (predictedEnergyAfterPropose < Player.MIN_ENERGY_BEFORE_SLEEP) {
        gp.ui.setDialog("Kamu terlalu lelah untuk melamar sekarang. Pulang dan istirahatlah.");
        gp.setGameState(GamePanel.dialogState);
        gp.repaint();
        gp.currNPC = targetNPC;
        return;
    }


    // Tambah waktu di awal
    gp.gameTime.addTime(60);

    gp.player.deductEnergy(PROPOSE_ENERGY_COST); 

    if (gp.gameState == GamePanel.sleepState) {
        System.out.println("NPCController: Proposal dibatalkan karena pemain pingsan setelah pengurangan energi.");
        gp.repaint(); 
        return; 
    }


    if (!gp.player.inventory.hasItem("ring")) {
        gp.ui.setDialog("You need a ring to propose.");

        if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState);
        gp.repaint();
        gp.currNPC = targetNPC;
        return;
    }

    if (targetNPC.isProposable(gp.player) && NPC.gender_female.equals(targetNPC.getGender())) {
        System.out.println("Selamat atas pernikahanmu Na Hee Do");
        targetNPC.becomeFiance(gp.player, gp.gameTime.getGameDay());
        gp.player.setFiance(targetNPC);
        
        gp.ui.setDialog(targetNPC.getName() + " joyfully accepts! You are now engaged.");
        if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState);
        gp.repaint();
        gp.currNPC = targetNPC;
        gp.update(); 
    } else {
        System.out.println(gp.gameState);
        System.out.println("Kamu terlalu baik buat aku");

        String reason = "They are not ready for such a commitment right now.";

        if (targetNPC.getHeartPoints() < targetNPC.getMaxHeartPoint()) {
            reason = targetNPC.getName() + " needs to feel a stronger connection.";
        } else if (!NPC.STATUS_SINGLE.equals(targetNPC.getRelationshipStatus())) {
            reason = targetNPC.getName() + " is not available for a relationship.";
        } else if (gp.player.hasFiance() || gp.player.hasSpouse()) {
            reason = "You are already in a relationship!";
        }
        gp.ui.setDialog("Proposal to " + targetNPC.getName() + " was declined. " + reason);
        if (gp.gameState != GamePanel.dialogState) {
            gp.setGameState(GamePanel.playState);
            gp.repaint();
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
        }
        
        gp.currNPC = targetNPC;
    }
    System.out.println("Energy sesudah: " + gp.player.getEnergy());
}

    public void attemptMarry() {

    if (gp.player == null || gp.player.getFiance() == null || gp.ui == null || gp.gameTime == null) {
        System.err.println("NPCController: Marriage failed, critical component null or player not engaged.");
        if (gp.ui != null) {
            gp.ui.setDialog(gp.player != null && gp.player.getFiance() == null ? "You are not engaged to anyone." : "Error: Cannot process marriage.");
            if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState);
        }
        return;
    }

    NPC fiance = gp.player.getFiance();

    final int MARRY_ENERGY_COST = 80; 

    int predictedEnergyAfterMarry = gp.player.getEnergy() - MARRY_ENERGY_COST;

    if (predictedEnergyAfterMarry < Player.MIN_ENERGY_BEFORE_SLEEP) {
        gp.ui.setDialog("Kamu terlalu lelah untuk menikah sekarang. Kamu butuh energi untuk upacara!");
        if (gp.gameState != GamePanel.dialogState){}
        gp.setGameState(GamePanel.dialogState);
        gp.repaint();
        return; // Hentikan proses pernikahan
    }
 

    if (!gp.player.inventory.hasItem("ring")) {
        gp.ui.setDialog("You need the Ring for the wedding ceremony!");

        if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState);
        return;
    }

    System.out.println("DEBUG (AttemptMarry): gp.currMap saat ini: " + gp.currMap);
    if (fiance.canMarryPlayer(gp.player, gp.gameTime.getGameDay())) {

        gp.player.deductEnergy(MARRY_ENERGY_COST);


        if (gp.gameState == GamePanel.sleepState) {
            System.out.println("NPCController: Pernikahan dibatalkan karena pemain pingsan setelah pengurangan energi.");

            gp.repaint(); 
            return; 
        }


        fiance.marryPlayer(gp.player);
        gp.player.setSpouse(fiance);


        if (gp.gameTime.getGameHour() >= 22) {
            gp.gameTime.nextDay();
        }
        gp.gameTime.setTime(22, 0);

        String homeMapPath = "/maps/rumah.txt";
        int homeSpawnTileX = 26;
        int homeSpawnTileY = 36;

        gp.tileM.teleportPlayer(homeMapPath, homeSpawnTileX, homeSpawnTileY);
        gp.repaint();

        gp.ui.setDialog("Congratulations! You are now married to " + fiance.getName() +
                        ".\nYou spend the rest of the day with your new spouse until 10:00 PM.");
        if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState);
        gp.currNPC = fiance;
    } else {

        int daysRemaining = 0;
        if (fiance.getDayBecameFiance() > 0 && fiance.getDayBecameFiance() >= gp.gameTime.getGameDay()) {
            daysRemaining = (fiance.getDayBecameFiance() + 1) - gp.gameTime.getGameDay();
        }

        if (daysRemaining > 0) {
            gp.ui.setDialog("It's too soon for the wedding. Wait " + daysRemaining + " more day(s).");
        } else {
            gp.ui.setDialog("The wedding with " + fiance.getName() + " cannot happen right now.");
        }

        if (gp.gameState != GamePanel.dialogState) gp.setGameState(GamePanel.dialogState);
        gp.currNPC = fiance;
    }
}
}
