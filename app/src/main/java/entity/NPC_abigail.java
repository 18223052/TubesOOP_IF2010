package entity;

import main.GamePanel;
import object.BaseItem;
import object.ItemFactory;


public class NPC_abigail extends NPC {

    public NPC_abigail(GamePanel gp,ItemFactory itemFactory) {
        super(gp);
        name = "Abigail";
        gender = NPC.gender_female;
        heartPoints =0;
        setHatesAllUnlistedItems(false);
        getCharacterImage();
        initializeGiftPreference(itemFactory);
        setDialogue();
    }

    @Override
    public void getCharacterImage() {
        d1 = setup("/player/abigail_diem");
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
        if (dialogues[0] == null && gp.player != null) { 
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
        lovedItems.add(itemFactory.createCrop("blueberry"));
        lovedItems.add(itemFactory.createCrop("melon"));
        lovedItems.add(itemFactory.createCrop("pumpkin"));
        lovedItems.add(itemFactory.createCrop("grape"));
        lovedItems.add(itemFactory.createCrop("cranberry"));

        likedItems.add(itemFactory.createFood("Baguette"));
        likedItems.add(itemFactory.createFood("Pumpkin Pie"));
        likedItems.add(itemFactory.createFood("Wine"));

        hatedItems.add(itemFactory.createCrop("hotpepper"));
        hatedItems.add(itemFactory.createCrop("cauliflower"));
        hatedItems.add(itemFactory.createCrop("parsnip"));
        hatedItems.add(itemFactory.createCrop("wheat"));
        
    }

    public String getGiftReaction(BaseItem item, int pointsAwarded){
        if (pointsAwarded == 25) return name + " absolutely loves this! Thank you so much!";
        if (pointsAwarded == 20) return name + " likes this. It's a thoughtful gift.";
        if (pointsAwarded == -25) return name + " seems to dislike this... Oh dear.";
        return name + " accepts the gift. \"Thank you.\"";
    }
}