package entity;

import main.GamePanel;
import object.IItem;
import object.ItemFactory;


public class NPC_mayortadi extends NPC {

    public NPC_mayortadi(GamePanel gp,ItemFactory itemFactory) {
        super(gp);
        name = "Mayor Tadi";
        direction = "down";
        gender = NPC.gender_male;
        heartPoints = MAX_HEART_POINTS; //Debug
        setHatesAllUnlistedItems(true);
        getCharacterImage();
        initializeGiftPreference(itemFactory);
        setDialogue();
    }

    @Override
    public void getCharacterImage() {
        d1 = setup("/player/police_diem");
        u1 = d1; u2 = d1; l1 = d1; l2 = d1; r1 = d1; r2 = d1; d2 = d1;
        defaultImage = d1;
    }
    
    @Override
    public void update(){}

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
                    dialogues[0] = "Hello there! I'm Mayor Tadi. Welcome to our town!";
                    dialogues[1] = "I hope you're enjoying your time on the farm.";
                } else if (heartPoints < 100) {
                    dialogues[0] = "It's good to see you, farmer!";
                    dialogues[1] = "The town appreciates all your hard work.";
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
        return true;
    }

    @Override
    public void initializeGiftPreference(ItemFactory itemFactory){
        lovedItems.add(itemFactory.createFish("salmon"));
        // likedItems.add(itemFactory.createFish("crimsonfish"));
        // likedItems.add(itemFactory.createFish("glacierfish"));
        // lovedItems.add(itemFactory.createFood("legend"));
        // hatedItems.add(itemFactory.createFood("legend"));
    }

    public String getGiftReaction(IItem item, int pointsAwarded){
        if (pointsAwarded == 25) return name + " absolutely loves this! Thank you so much!";
        if (pointsAwarded == 20) return name + " likes this. It's a thoughtful gift.";
        if (pointsAwarded == -25) return name + " seems to dislike this... Oh dear.";
        return name + " accepts the gift. \"Thank you.\"";
    }
}