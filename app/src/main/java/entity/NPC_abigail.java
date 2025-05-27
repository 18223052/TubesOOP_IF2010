package entity;

import main.GamePanel;
import object.BaseItem;
import object.ItemFactory;


public class NPC_abigail extends NPC {

    public NPC_abigail(GamePanel gp,ItemFactory itemFactory) {
        super(gp);
        name = "Abigail";
        gender = gender_female;
        setHatesAllUnlistedItems(true);
        getImage();
        initializeGiftPreference(itemFactory);
        setDialogue();
    }

    public void getImage() {
        u1 = setup("/player/abigail_diem");
        u2 = setup("/player/abigail_diem");
        r1 = setup("/player/abigail_diem");
        r2 = setup("/player/abigail_diem");
        l1 = setup("/player/abigail_diem");
        l2 = setup("/player/abigail_diem");
        d1 = setup("/player/abigail_diem");
        d2 = setup("/player/abigail_diem");
    }
    

    @Override
    public void setDialogue() {
        dialogues = new String[20];
        dialogIndex = 0;

        switch (relationshipStatus) {
            case "Fiance":
                dialogues[0] = "My dearest! I'm so happy we're engaged.";
                dialogues[1] = "The wedding will be wonderful, won't it?";
                break;
            case "Married":
                dialogues[0] = "My love, how was your day on the farm?";
                dialogues[1] = "Being married to you is a dream come true.";
                break;
            default:
                if (heartPoints < 20) {
                    dialogues[0] = "Hello there! I'm Abigail. Nice to meet you!";
                    dialogues[1] = "I hope you're enjoying your time on the farm.";
                } else if (heartPoints < 100) {
                    dialogues[0] = "It's good to see you, farmer!";
                    dialogues[1] = "Thank you for all of your hardwork";
                } else {
                    dialogues[0] = "Ah, my favorite farmer! So glad to see you.";
                    dialogues[1] = "You've become such an important part of our community.";
                }
                break;
        }
        if (dialogues[0] == null && gp.player != null) { // Tambahkan null check untuk gp.player
            dialogues[0] = "Hello, " + gp.player.getName() + ".";
        } else if (dialogues[0] == null) {
            dialogues[0] = "Hello there.";
        }
    }


    @Override
    // contoh
    public boolean hasStore(){
        return false;
    }

    @Override
    public void initializeGiftPreference(ItemFactory itemFactory){
        lovedItems.add(itemFactory.createFish("salmon"));
        // likedItems.add(itemFactory.createFish("crimsonfish"));
        // likedItems.add(itemFactory.createFish("glacierfish"));
        // lovedItems.add(itemFactory.createFood("legend"));
        // hatedItems.add(itemFactory.createFood("legend"));
    }

    public String getGiftReaction(BaseItem item, int pointsAwarded){
        if (pointsAwarded == 25) return name + " absolutely loves this! Thank you so much!";
        if (pointsAwarded == 20) return name + " likes this. It's a thoughtful gift.";
        if (pointsAwarded == -25) return name + " seems to dislike this... Oh dear.";
        return name + " accepts the gift. \"Thank you.\"";
    }
}