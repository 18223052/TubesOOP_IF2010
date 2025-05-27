package object;

import java.util.ArrayList;
import java.util.List;

import environment.GameTime;
import environment.Season;
import environment.WeatherType;
import main.GamePanel;
import object.FishItem.FishCategory;
import environment.TimeRange;

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
 public FishItem createFish(String fishType) {
        String name = fishType;
        int buyPrice = 0;
        Season[] seasons = null;
        WeatherType[] weathers = null;
        List<TimeRange> timeRangesParam = null;
        FishLocation[] locations = null;
        FishCategory category = FishCategory.COMMON;
        int energyValue = 1;

        switch (fishType.toLowerCase()) {

            case "salmon":
                name = "Salmon";
                seasons = new Season[]{Season.FALL};
                weathers = null;
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(18, 0)));
                locations = new FishLocation[]{FishLocation.FOREST_RIVER};
                category = FishCategory.REGULAR;
                break;


            case "bullhead":
                name = "Bull Head";
                seasons = new Season[]{Season.SPRING, Season.FALL, Season.SUMMER, Season.WINTER};
                weathers = null; 
                timeRangesParam = null;
                locations = new FishLocation[]{FishLocation.MOUNTAIN_LAKE};
                category = FishCategory.COMMON;
                break;
            
            case "carp":
                name = "Carp";
                seasons = new Season[]{Season.SPRING, Season.FALL, Season.SUMMER, Season.WINTER};
                weathers = null;
                timeRangesParam = null; 
                locations = new FishLocation[]{FishLocation.MOUNTAIN_LAKE, FishLocation.POND};
                category = FishCategory.COMMON;
                break;
            
            case "chub":
                name = "Chub";
                seasons = new Season[]{Season.SPRING, Season.FALL, Season.SUMMER, Season.WINTER};
                weathers = null; 
                timeRangesParam = null;  
                locations = new FishLocation[]{FishLocation.FOREST_RIVER, FishLocation.MOUNTAIN_LAKE};
                category = FishCategory.COMMON;
                break;
            
            case "largemouthbass": 
                name = "Largemouth Bass";
                seasons = null; 
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(18, 0)));
                weathers = null; 
                locations = new FishLocation[]{FishLocation.MOUNTAIN_LAKE};
                category = FishCategory.REGULAR; 
                break;

            case "rainbowtrout": 
                name = "Rainbow Trout";
                seasons = new Season[]{Season.SUMMER};
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(18, 0)));
                weathers = new WeatherType[]{WeatherType.SUNNY};
                locations = new FishLocation[]{FishLocation.FOREST_RIVER, FishLocation.MOUNTAIN_LAKE};
                category = FishCategory.REGULAR; 
                break;

            case "sturgeon":
                name = "Sturgeon";
                seasons = new Season[]{Season.SUMMER, Season.WINTER}; 
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(18, 0)));
                weathers = null; // Any Weather
                locations = new FishLocation[]{FishLocation.MOUNTAIN_LAKE};
                category = FishCategory.REGULAR; 
                break;

            // case "midnightcarp": 
            //     name = "Midnight Carp";
            //     seasons = new Season[]{Season.WINTER, Season.FALL};
            //     timeRangesParam = new ArrayList<>();
            //     timeRangesParam.add(new TimeRange(new GameTime(20, 0), new GameTime(2, 0)));
            //     weathers = null; 
            //     locations = new FishLocation[]{FishLocation.MOUNTAIN_LAKE, FishLocation.POND};
            //     category = FishCategory.REGULAR;
            //     break;

            // case "flounder":
            //     name = "Flounder";
            //     seasons = new Season[]{Season.SPRING, Season.SUMMER};
            //     timeRangesParam = new ArrayList<>();
            //     timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(22, 0)));
            //     weathers = null; 
            //     locations = new FishLocation[]{FishLocation.OCEAN};
            //     category = FishCategory.REGULAR; 
            //     break;

            case "halibut":
                name = "Halibut";
                seasons = null; 
                // Original time: 06.00-11.00, 19.00-02.00. Using the longer/later slot.
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(18, 0)));
                timeRangesParam.add(new TimeRange(new GameTime(19, 0), new GameTime(2, 0)));
                weathers = null; // Any Weather
                locations = new FishLocation[]{FishLocation.OCEAN};
                category = FishCategory.REGULAR; // Default
                break;

            case "octopus": // 7. Octopus
                name = "Octopus";
                seasons = new Season[]{Season.SUMMER};
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(22, 0)));
                weathers = null; // Any Weather
                locations = new FishLocation[]{FishLocation.OCEAN};
                category = FishCategory.REGULAR; // Default, could be higher
                break;

            case "pufferfish": // 8. Pufferfish
                name = "Pufferfish";
                seasons = new Season[]{Season.SUMMER};
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(0, 0), new GameTime(16, 0)));
                weathers = new WeatherType[]{WeatherType.SUNNY};
                locations = new FishLocation[]{FishLocation.OCEAN};
                category = FishCategory.REGULAR; // Can be tricky, adjust if needed
                 // Placeholder
                break;

            case "sardine": // 9. Sardine
                name = "Sardine";
                seasons = null; // Any Season
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(18, 0)));
                weathers = null; // Any Weather
                locations = new FishLocation[]{FishLocation.OCEAN};
                category = FishCategory.COMMON; // Sardines are usually common
                
                break;

            // case "supercucumber": // 10. Super Cucumber
            //     name = "Super Cucumber";
            //     seasons = new Season[]{Season.SUMMER, Season.FALL, Season.WINTER};
            //     timeRangesParam = new ArrayList<>();
            //     timeRangesParam.add(new TimeRange(new GameTime(18, 0), new GameTime(2, 0)));
            //     weathers = null;
            //     locations = new FishLocation[]{FishLocation.OCEAN};
            //     category = FishCategory.REGULAR; 
                
            //     break;

            case "catfish": // 11. Catfish
                name = "Catfish";
                seasons = new Season[]{Season.SPRING, Season.SUMMER, Season.FALL};
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(6, 0), new GameTime(22, 0)));
                weathers = new WeatherType[]{WeatherType.RAINY};
                locations = new FishLocation[]{FishLocation.FOREST_RIVER, FishLocation.POND};
                category = FishCategory.REGULAR; 
                break;

            case "angler": // 8. Pufferfish
                name = "Angler";
                seasons = new Season[]{Season.FALL};
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(8, 0), new GameTime(20, 0)));
                weathers = null;
                locations = new FishLocation[]{FishLocation.POND};
                category = FishCategory.LEGENDARY; 
                break;

            case "crimsonfish": // 9. Sardine
                name = "Crimsonfish";
                seasons = new Season[]{Season.SUMMER};
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(8, 0), new GameTime(20, 0)));
                weathers = null; 
                locations = new FishLocation[]{FishLocation.FOREST_RIVER};
                category = FishCategory.LEGENDARY; // Sardines are usually common
                break;

            case "glacierfish": // 10. Super Cucumber
                name = "Glacierfish";
                seasons = new Season[]{Season.WINTER};
                timeRangesParam = new ArrayList<>();
                timeRangesParam.add(new TimeRange(new GameTime(8, 0), new GameTime(20, 0)));
                weathers = null;
                locations = new FishLocation[]{FishLocation.FOREST_RIVER};
                category = FishCategory.LEGENDARY;  
                break;

            // case "legend": // 11. Catfish
            //     name = "Legend";
            //     seasons = new Season[]{Season.SPRING};
            //     timeRangesParam = new ArrayList<>();
            //     timeRangesParam.add(new TimeRange(new GameTime(8, 0), new GameTime(20, 0)));
            //     weathers = new WeatherType[]{WeatherType.RAINY};
            //     locations = new FishLocation[]{FishLocation.MOUNTAIN_LAKE};
            //     category = FishCategory.LEGENDARY; 
            //     break;

            default:
                System.err.println("Warning: Attempted to create unknown fish type: " + fishType + ". Creating a default common fish.");
                name = "Minnow";
                seasons = new Season[]{Season.SPRING};
                timeRangesParam = new ArrayList<>(); // Default ke satu slot waktu atau null jika "Any time"
                timeRangesParam.add(new TimeRange(new GameTime(6,0), new GameTime(18,0)));
                weathers = null;
                locations = new FishLocation[]{FishLocation.POND};
                category = FishCategory.COMMON;
                energyValue = 5;
                break;
        }
        return new FishItem(name, buyPrice, seasons, weathers, timeRangesParam, locations, category, energyValue, gp);
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
