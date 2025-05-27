package controller;

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
}
