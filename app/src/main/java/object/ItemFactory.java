package object;

import main.GamePanel;

public class ItemFactory {
    GamePanel gp;
    
    public ItemFactory(GamePanel gp) {
        this.gp = gp;
    }
    
    public Item createSeed(String seedType) {
        Item seed = new Item(seedType + " Seed", 10, 5);
        seed.category = "seeds";
        seed.img = seed.getImage(gp);
        return seed;
    }
    
    public Item createTool(String toolType) {
        int buyPrice = 0;
        int sellPrice = 0;
        
        switch (toolType.toLowerCase()) {
            case "hoe":
                buyPrice = 100;
                sellPrice = 50;
                break;
            case "wateringcan":
                buyPrice = 80;
                sellPrice = 40;
                break;
            case "fishingpole":
                buyPrice = 150;
                sellPrice = 75;
                break;
            case "Pickaxe":
                buyPrice = 150;
                sellPrice = 75;
                break;

        }
        
        Item tool = new Item(toolType, buyPrice, sellPrice);
        tool.stackable = false;  
        tool.category = "tools";
        tool.img = tool.getImage(gp);
        return tool;
    }
    

    public Item createCrop(String cropName) {
        int buyPrice = 0;
        int sellPrice = 0;
        
        switch (cropName.toLowerCase()) {
            case "tomato":
                buyPrice = 90;
                sellPrice = 60;
                break;
            case "potato":
                buyPrice = 0;
                sellPrice = 80;
                break;
            case "parsnip":
                buyPrice = 50;
                sellPrice = 35;
                break;
            case "cauliflower":
                buyPrice = 200;
                sellPrice = 150;
                break;
            case "wheat":
                buyPrice = 50;
                sellPrice = 30;
                break;
            case "blueberry":
                buyPrice = 150;
                sellPrice = 40;
                break;
            case "hotpepper":
                buyPrice = 0;
                sellPrice = 40;
                break;
            case "melon":
                buyPrice = 0;
                sellPrice = 250;
                break;
            case "cranberry":
                buyPrice = 0;
                sellPrice = 25;
                break;
            case "pumpkin":
                buyPrice = 300;
                sellPrice = 250;
                break;
            case "grape":
                buyPrice = 100;
                sellPrice = 10;
                break;

        }
        
        Item crop = new Item(cropName, buyPrice, sellPrice);
        crop.category = "crops";
        crop.img = crop.getImage(gp);
        return crop;
    }
    

    public Item createConsumable(String consumableName) {
        int buyPrice = 0;
        int sellPrice = 0;
        int addEnergy = 0;
        
        switch (consumableName.toLowerCase()) {
            case "fishnchips":
                buyPrice = 150;
                sellPrice = 135;
                addEnergy = 50;
                break;
            case "baguette":
                buyPrice = 100;
                sellPrice = 80;
                addEnergy = 25;
                break;
            case "sashimi":
                buyPrice = 300;
                sellPrice = 275;
                addEnergy = 70;
                break;
            case "fugu":
                buyPrice = 0;
                sellPrice = 135;
                addEnergy = 50;
                break;
            case "wine":
                buyPrice = 100;
                sellPrice = 90;
                addEnergy = 20;
                break;
            case "pumpkinpie":
                buyPrice = 120;
                sellPrice = 100;
                addEnergy = 35;
                break;
            case "veggiesoup":
                buyPrice = 140;
                sellPrice = 120;
                addEnergy = 40;
                break;
            case "fishstew":
                buyPrice = 280;
                sellPrice = 260;
                addEnergy = 70;
                break;
            case "spakborsalad":
                buyPrice = 0;
                sellPrice = 250;
                addEnergy = 70;
                break;
            case "fishsandwich":
                buyPrice = 200;
                sellPrice = 180;
                addEnergy = 50;
                break;
            case "thelegendofspakbor":
                buyPrice = 0;
                sellPrice = 2000;
                addEnergy = 100;
                break;
            case "cookedpigshead":
                buyPrice = 1000;
                sellPrice = 0;
                addEnergy = 100;
                break;
        }
        
        Item consumable = new Item(consumableName, buyPrice, sellPrice,addEnergy);
        consumable.category = "consumables";
        consumable.img = consumable.getImage(gp);
        return consumable;
    }

    public Item createFish(String fishType, String season, String weather, String timeOfDay, int rarity) {
        Item fish = new Item(fishType, 50, 25);
        fish.category = "fish";
        fish.season = season;
        fish.weather = weather;
        fish.time = timeOfDay;
        fish.rarity = rarity;
        fish.img = fish.getImage(gp);
        return fish;
    }
}