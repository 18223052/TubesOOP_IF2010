package object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import environment.GameTime;
import environment.Season;
import environment.TimeRange;
import environment.WeatherType;
import main.GamePanel;
import object.FishItem.FishCategory;


public class ItemFactory {

    private GamePanel gp;
    private List<FishItem> allFishItems;
    
    public ItemFactory(GamePanel gp) {
        this.gp = gp;
        this.allFishItems = new ArrayList<>();
        loadAllFishItems();
    }
    

    public SeedItem createSeed(String seedType) {
        int buyPrice = 0;
        int sellPrice = 0;

        switch (seedType.toLowerCase()) {
            case "tomato":
                buyPrice = 50;
                sellPrice = 20;
                break;
            case "parsnip":
                buyPrice = 20;
                sellPrice = 10;
            break;
            case "melon":
                buyPrice = 80;
                sellPrice = 50;
                break;
            case "blueberry":
                buyPrice = 80;
                sellPrice = 50;
            break;
            case "cauliflower":
                buyPrice = 80;
                sellPrice = 50;
                break;
            case "potato":
                buyPrice = 50;
                sellPrice = 20;
            break;
            case "cranberry":
                buyPrice = 100;
                sellPrice = 70;
                break;
            case "hotpepper":
                buyPrice = 40;
                sellPrice = 10;
            break;
            case "pumpkin":
                buyPrice = 150;
                sellPrice = 120;
                break;
            case "wheat":
                buyPrice = 60;
                sellPrice = 30;
            break;
            case "grape":
                buyPrice = 60;
                sellPrice = 30;
                break;

            default:
                System.err.println("Unknown seed type: " + seedType + ". Creating a default seed.");
                buyPrice = 10;
                sellPrice = 5;
                seedType = "Unknown";
                break;
        }

        return new SeedItem(seedType, buyPrice, sellPrice, gp);
    }
    

    public IItem createTool(String toolName) {
        switch (toolName.toLowerCase()) {
            case "hoe":
                return new Hoe(gp);
            case "pickaxe":
                return new Pickaxe(gp);
            case "wateringcan":
                return new WateringCan(gp); 
            case "fishingpole":
                return new Fishingpole(gp);
            default:
                System.err.println("Unknown tool type: " + toolName);
                return null;
        }
    }
    
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
            case "eggplant":
                buyPrice = 20;
                sellPrice = 10;
                break;
            case "egg":
                buyPrice = 20;
                sellPrice = 10;
                break;
            default:
                buyPrice = 50;
                sellPrice = 25;
                break;
        }

        return new CropItem(cropName, buyPrice, sellPrice, gp);
    }
    
    public FoodItem createFood(String foodName) {
        int buyPrice = 0;
        int sellPrice = 0;
        int energyValue = 0;

        switch (foodName.toLowerCase()) {
            case "Fish n Chips":
                buyPrice = 150;
                sellPrice = 135;
                energyValue = 50;
                break;
            case "Baguette":
                buyPrice = 100;
                sellPrice = 80;
                energyValue = 25;
                break;
            case "Sashimi":
                buyPrice = 300;
                sellPrice = 275;
                energyValue = 70;
                break;
            case "Fugu":
                buyPrice = 0;
                sellPrice = 135;
                energyValue = 50;
                break;
            case "Wine":
                buyPrice = 100;
                sellPrice = 90;
                energyValue = 20;
                break;
            case "Pumpkin Pie":
                buyPrice = 120;
                sellPrice = 100;
                energyValue = 35;
                break;
            case "Veggie Soup":
                buyPrice = 140;
                sellPrice = 120;
                energyValue = 40;
                break;
            case "Fish Stew":
                buyPrice = 280;
                sellPrice = 260;
                energyValue = 70;
                break;
            case "Spakbor Salad":
                buyPrice = 0;
                sellPrice = 250;
                energyValue = 70;
                break;
            case "Fish Sandwich":
                buyPrice = 200;
                sellPrice = 180;
                energyValue = 55;
                break;
            case "The Legend of Spakbor":
                buyPrice = 0;
                sellPrice = 2000;
                energyValue = 100;
                break;
            case "Cooked Pigs Head":
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
    
    public void loadAllFishItems(){
        allFishItems.clear();

         allFishItems.add(new FishItem(
            "Minnow", 0,
            new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER},
            null,
            Arrays.asList(new TimeRange(new GameTime(0,0), new GameTime(23,59))), 
            new FishLocation[]{FishLocation.POND, FishLocation.FOREST_RIVER},
            FishItem.FishCategory.COMMON,1, gp
        ));
        allFishItems.add(new FishItem(
            "Bull Head", 0,
            new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER},
            null,
            null, 
            new FishLocation[]{FishLocation.MOUNTAIN_LAKE},
            FishItem.FishCategory.COMMON, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Carp", 0,
            new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER},
            null, 
            null, 
            new FishLocation[]{FishLocation.MOUNTAIN_LAKE, FishLocation.POND},
            FishItem.FishCategory.COMMON, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Chub", 0,
            new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER},
            null,
            null, 
            new FishLocation[]{FishLocation.FOREST_RIVER, FishLocation.MOUNTAIN_LAKE},
            FishItem.FishCategory.COMMON,1, gp
        ));
        allFishItems.add(new FishItem(
            "Sardine", 0,
            null, 
            null, 
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(18, 0))), 
            new FishLocation[]{FishLocation.OCEAN},
            FishItem.FishCategory.COMMON,1, gp
        ));


        allFishItems.add(new FishItem(
            "Salmon", 0,
            new Season[]{Season.FALL},
            null, 
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(18, 0))),
            new FishLocation[]{FishLocation.FOREST_RIVER},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Largemouth Bass", 0,
            null, 
            null, 
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(18, 0))),
            new FishLocation[]{FishLocation.MOUNTAIN_LAKE},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Rainbow Trout", 0,
            new Season[]{Season.SUMMER},
            new WeatherType[]{WeatherType.SUNNY},
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(18, 0))),
            new FishLocation[]{FishLocation.FOREST_RIVER, FishLocation.MOUNTAIN_LAKE},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Sturgeon", 0,
            new Season[]{Season.SUMMER, Season.WINTER},
            null, 
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(18, 0))),
            new FishLocation[]{FishLocation.MOUNTAIN_LAKE},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Midnight Carp", 0,
            new Season[]{Season.WINTER, Season.FALL},
            null, 
            Arrays.asList(new TimeRange(new GameTime(20, 0), new GameTime(2, 0))), // Note: This time range spans midnight
            new FishLocation[]{FishLocation.MOUNTAIN_LAKE, FishLocation.POND},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Flounder", 0,
            new Season[]{Season.SPRING, Season.SUMMER},
            null, 
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(22, 0))),
            new FishLocation[]{FishLocation.OCEAN},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Halibut", 0,
            null, 
            null, 
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(11, 0)), 
                          new TimeRange(new GameTime(19, 0), new GameTime(2, 0))),
            new FishLocation[]{FishLocation.OCEAN},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Octopus", 0,
            new Season[]{Season.SUMMER},
            null, 
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(22, 0))),
            new FishLocation[]{FishLocation.OCEAN},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Pufferfish", 0,
            new Season[]{Season.SUMMER},
            new WeatherType[]{WeatherType.SUNNY},
            Arrays.asList(new TimeRange(new GameTime(0, 0), new GameTime(16, 0))),
            new FishLocation[]{FishLocation.OCEAN},
            FishItem.FishCategory.REGULAR, 1, gp
        ));
        allFishItems.add(new FishItem(
            "Super Cucumber", 0,
            new Season[]{Season.SUMMER, Season.FALL, Season.WINTER},
            null, 
            Arrays.asList(new TimeRange(new GameTime(18, 0), new GameTime(2, 0))), // Spans midnight
            new FishLocation[]{FishLocation.OCEAN},
            FishItem.FishCategory.REGULAR, 1, gp
        ));

        allFishItems.add(new FishItem(
            "Catfish", 0,
            new Season[]{Season.SPRING, Season.SUMMER, Season.FALL},
            new WeatherType[]{WeatherType.RAINY},
            Arrays.asList(new TimeRange(new GameTime(6, 0), new GameTime(22, 0))),
            new FishLocation[]{FishLocation.FOREST_RIVER, FishLocation.POND},
            FishItem.FishCategory.REGULAR, 1, gp
        ));


        allFishItems.add(new FishItem(
            "Legendary Bass", 0,
            new Season[]{Season.SUMMER},
            new WeatherType[]{WeatherType.SUNNY},
            Arrays.asList(new TimeRange(new GameTime(6,0), new GameTime(8,0)),
                          new TimeRange(new GameTime(20,0), new GameTime(22,0))),
            new FishLocation[]{FishLocation.MOUNTAIN_LAKE},
            FishItem.FishCategory.LEGENDARY, 1, gp
        ));
    }

    public FishItem createFish(String fishType) {

        for (FishItem fish : allFishItems) {
            if (fish.getName().equalsIgnoreCase(fishType)) {

                return new FishItem(fish.getName(), 0, fish.getSeason(),
                                    fish.getWeather(), fish.getTimeRanges(), fish.getAvailLocation(),
                                    fish.getFishCategory(), fish.getEnergyRestoration(), gp);
            }
        }

        System.err.println("Warning: Attempted to create unknown fish type: " + fishType + ". Creating a default common fish.");
        return new FishItem("Minnow", 0,
            new Season[]{Season.SPRING}, null,
            Arrays.asList(new TimeRange(new GameTime(6,0), new GameTime(18,0))),
            new FishLocation[]{FishLocation.POND},
            FishCategory.COMMON, 1, gp);
    }

        public List<FishItem> getAllFishItems() {
        return new ArrayList<>(allFishItems); 
    }

    public FuelItem createFuelItem(String fuelName) { 
        switch (fuelName.toLowerCase()) {
            case "firewood":
                return new Firewood(gp);
            case "coal":
                return new Coal(gp);
            default:

                System.err.println("Warning: Attempted to create unknown fuel item: " + fuelName + ". Returning null.");
                return null;
        }
    }

    public MiscItem createMiscItem (String itemName){
        switch(itemName.toLowerCase()){
            case "ring":
                return new MiscItem("ring", 250, 0, gp, itemName);
            case "18223052":
                return new MiscItem("Fathimah Nurhumaida (18223052)", 0, 0, gp, "BEBAN", true );
            default:
                System.err.println("Warning: Attempted to create unknown fuel item: " + itemName+ ". Returning null.");
                return null; // Atau throw an exception
        }
    }

    public RecipeItem createRecipeItem(String recipeName){
        int buyPrice = 0;
        int sellPrice = 0;
        String recipeToUnlock = "";

        switch (recipeName.toLowerCase()) {
            case "recipe_fish_n_chips": 
                buyPrice = 10;
                sellPrice = 10;
                recipeToUnlock = "Fish n Chips"; 
                break;
            case "recipe_fish_sandwich":
                buyPrice = 10;
                sellPrice = 10;
                recipeToUnlock = "Fish Sandwich";
                break;
        }
        return new RecipeItem(recipeName, buyPrice, sellPrice, gp, recipeToUnlock);
    }
}
