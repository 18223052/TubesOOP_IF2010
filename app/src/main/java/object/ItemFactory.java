package object;

import environment.GameTime;
import environment.Season;
import environment.WeatherType;
import main.GamePanel;

/**
 * Factory for creating different types of items
 * Following the Factory pattern to create appropriate item subclasses
 */
public class ItemFactory {
    private GamePanel gp;
    
    public ItemFactory(GamePanel gp) {
        this.gp = gp;
    }
    
    /**
     * Creates a seed item
     */
    public SeedItem createSeed(String seedType) {
        return new SeedItem(seedType, 10, 5, gp);
    }
    
    /**
     * Creates a tool item
     */
    public ToolItem createTool(String toolType) {
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
            case "pickaxe":
                buyPrice = 150;
                sellPrice = 75;
                break;
            default:
                buyPrice = 100;
                sellPrice = 50;
                break;
        }
        
        return new ToolItem(toolType, buyPrice, sellPrice, gp);
    }
    
    /**
     * Creates a crop item
     */
    public CropItem createCrop(String cropName) {
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
            default:
                buyPrice = 50;
                sellPrice = 25;
                break;
        }
        
        return new CropItem(cropName, buyPrice, sellPrice, gp);
    }
    
    /**
     * Creates a Food item
     */
    public FoodItem createFood(String foodName) {
        int buyPrice = 0;
        int sellPrice = 0;
        int energyValue = 0;
        
        switch (foodName.toLowerCase()) {
            case "fishnchips":
                buyPrice = 150;
                sellPrice = 135;
                energyValue = 50;
                break;
            case "baguette":
                buyPrice = 100;
                sellPrice = 80;
                energyValue = 30;
                break;
            case "sashimi":
                buyPrice = 300;
                sellPrice = 275;
                energyValue = 75;
                break;
            case "fugu":
                buyPrice = 0;
                sellPrice = 135;
                energyValue = 40;
                break;
            case "wine":
                buyPrice = 100;
                sellPrice = 90;
                energyValue = 25;
                break;
            case "pumpkinpie":
                buyPrice = 120;
                sellPrice = 100;
                energyValue = 45;
                break;
            case "veggiesoup":
                buyPrice = 140;
                sellPrice = 120;
                energyValue = 40;
                break;
            case "fishstew":
                buyPrice = 280;
                sellPrice = 260;
                energyValue = 70;
                break;
            case "spakborsalad":
                buyPrice = 0;
                sellPrice = 250;
                energyValue = 60;
                break;
            case "fishsandwich":
                buyPrice = 200;
                sellPrice = 180;
                energyValue = 55;
                break;
            case "thelegendofspakbor":
                buyPrice = 0;
                sellPrice = 2000;
                energyValue = 100;
                break;
            case "cookedpigshead":
                buyPrice = 1000;
                sellPrice = 0;
                energyValue = 85;
                break;
            default:
                buyPrice = 50;
                sellPrice = 30;
                energyValue = 20;
                break;
        }
        
        return new FoodItem(foodName, buyPrice, sellPrice, energyValue, gp);
    }
    
    /**
     * Creates a fish item
     */
    public FishItem createFish(String fishType, String season, String weather, String timeOfDay, int rarity) {
        int baseBuyPrice = 50;
        int baseSellPrice = 25;
        
        // Adjust price based on rarity
        int finalSellPrice = baseSellPrice + (rarity * 15);
        
        return new FishItem(fishType, baseBuyPrice, finalSellPrice, season, weather, timeOfDay, rarity, gp);
    }

    public MiscItem createMiscItem(String itemName) {
        int buyPrice = 0;
        int sellPrice =0 ;
        String category = "misc";

        switch (itemName.toLowerCase()){
            case "firewood":
                buyPrice = 10;
                sellPrice = 5;
                category = "fuel";
                break;
            case "coal" :
                buyPrice = 20;
                sellPrice = 10;
                category = "fuel";
                break;
            default:
                System.err.println("Warning: Attempted to create unknown miscellaneous item: " + itemName);
                buyPrice = 1; 
                sellPrice = 1;
                break;
        }
        return new MiscItem(itemName, buyPrice, sellPrice, gp, category);
    }
}
