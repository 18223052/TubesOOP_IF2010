package controller;

import entity.Ingredient;
import entity.Recipe;
import object.FoodItem;
import object.IItem;
import object.ItemFactory;
import main.GamePanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CookingController {
    private GamePanel gp;
    private ItemFactory itemFactory;
    private Queue<CookingQueueEntry> cookingQueue;

    public CookingController(GamePanel gp){
        this.gp = gp;
        this.itemFactory = new ItemFactory(gp);
        this.cookingQueue = new LinkedList<>();
    }

    public List<Recipe> getCookableRecipe() {
        List<Recipe> cookableRecipeList = new ArrayList<>();
        List<Recipe> allRecipes = Recipe.getRecipeList(); 

        InventoryController playerInventory = gp.player.inventory; 

        for (Recipe recipe : allRecipes) {
            if (hasEnoughIngredients(recipe, playerInventory)) {
                cookableRecipeList.add(recipe);
            }
        }
        return cookableRecipeList;
    }

    public boolean hasEnoughIngredients(Recipe recipe, InventoryController playerInventory) {

        for (Ingredient requiredIngredient : recipe.ingredients) {
            String ingredientName = requiredIngredient.name;
            int requiredAmount = requiredIngredient.amount;
            int availableAmount;

            if (ingredientName.equalsIgnoreCase("Any Fish")) {
                availableAmount = playerInventory.getItemCountByCategory("fish");
            } else {
                availableAmount = playerInventory.getItemCount(ingredientName);
            }

            if (availableAmount < requiredAmount) {
                return false; 
            }
        }

        if (gp.player.getEnergy()<10){
            System.out.println("Energi tidak cukup untuk memasak " + recipe.title);
            return false;
        }

        return hasEnoughFuel(playerInventory, 1); 
    }

    public boolean hasEnoughFuel (InventoryController playerInventory, int fuelAmountNeeded){
        int totalFuelCapacity = 0;

        for (IItem item : playerInventory.getInventory()){
            if (item.getCategory().equalsIgnoreCase("fuel")){
                if (item.getName().equalsIgnoreCase("firewood")){
                    totalFuelCapacity +=1;
                } else if (item.getName().equalsIgnoreCase("coal")){
                    totalFuelCapacity +=2;
                }
            }
        }

        return totalFuelCapacity >= fuelAmountNeeded;
    }

    public boolean cookRecipe(Recipe recipe) {
        if (recipe == null) {
            System.out.println("Resep null.");
            return false;
        }

        InventoryController playerInventory = gp.player.inventory;

        if (!hasEnoughIngredients(recipe, playerInventory)) {
            System.out.println("Bahan tidak cukup untuk memasak " + recipe.title);
            return false; 
        }

        if (gp.player.getEnergy() < 10) {
            System.out.println("Energi tidak cukup untuk memasak " + recipe.title);
            return false;
        }

        gp.player.deductEnergy(10);
        System.out.println("Energi player dikurangi 10 untuk memasak");

        for (Ingredient requiredIngredient : recipe.ingredients) {
            String ingredientName = requiredIngredient.name;
            int amountToConsume = requiredIngredient.amount;

            if (ingredientName.equalsIgnoreCase("Any Fish")) {
                int removedFishCount = 0;
                List<IItem> currentItems = new ArrayList<>(playerInventory.getInventory());
                Iterator<IItem> iterator = currentItems.iterator();
                List<IItem> itemsToRemoveFromOriginal = new ArrayList<>();

                while (iterator.hasNext() && removedFishCount < amountToConsume) {
                    IItem item = iterator.next();
                    if (item.getCategory().equalsIgnoreCase("fish")) {
                        itemsToRemoveFromOriginal.add(item); 
                        removedFishCount++;
                        System.out.println("Marked for consumption: " + item.getName() + " for recipe (Any Fish)");
                    }
                }

                for(IItem itemToRemove : itemsToRemoveFromOriginal) {
                    playerInventory.getInventory().remove(itemToRemove); 
                }
                if (removedFishCount < amountToConsume) {
                    System.err.println("Error logic: debug gacukup ikan padahal udah hasEnoghIngredient sudah true");
                    return false; 
                }
            } else {
                playerInventory.removeItems(ingredientName, amountToConsume); 
            }
        }


        consumeFuel(playerInventory, 1); 

        long currentTotalGameMinutes = gp.gameTime.getTotalGameMinutes();
        int cookingDuration = 60;
        cookingQueue.add (new CookingQueueEntry(recipe, currentTotalGameMinutes, cookingDuration, gp));

        // gp.gameTime.addTime(60);
        // System.out.println("SkipTime sebanyak 60 menit untuk memasak.");

        System.out.println("Memasak " + recipe.title + " telah dimulai! Akan selesai pada menit ke-" + (currentTotalGameMinutes + cookingDuration) + " in-game time.");
        return true;
    }
    
    public void update(){
        long currentTotalGameMinutes = gp.gameTime.getTotalGameMinutes();

        Iterator<CookingQueueEntry> iterator = cookingQueue.iterator();
        while (iterator.hasNext()) {
            CookingQueueEntry entry = iterator.next();
            if (currentTotalGameMinutes >= entry.getCompletionTimeInMinutes()){
                FoodItem cookedFood = entry.createCookedFoodItem();
                if (cookedFood != null){
                    gp.player.inventory.addItem(cookedFood);
                    System.out.println("Memasak selesai!, menambahkan " + cookedFood.getName());
                } else {
                    System.out.println("Gagal membuat item masakan.");
                }
                iterator.remove();
            } else {
                break;
            }
        }
    }

    public boolean isCookingProgress(){
        return ! cookingQueue.isEmpty();
    }

    public CookingQueueEntry getNextCookingItem(){
        return cookingQueue.peek();
    }

    private void consumeFuel (InventoryController playerInventory, int fuelAmountNeeded){
        int consumedFuel =0;

        List<IItem> fuelItemsInInventory = new ArrayList<>();
        for (IItem item : playerInventory.getInventory()){
            if (item.getCategory().equalsIgnoreCase("fuel")){
                fuelItemsInInventory.add(item);
            }
        }

        // ini make coal
        Iterator<IItem> fuelIterator = fuelItemsInInventory.iterator();
        while (fuelIterator.hasNext() && consumedFuel < fuelAmountNeeded){
            IItem item = fuelIterator.next();
            if (item.getName().equalsIgnoreCase("coal")){

                if (playerInventory.getInventory().remove(item)) {
                    consumedFuel +=2;
                    System.out.println("Mengonsumsi 1 Coal untuk memasak.");
                }
            }
        }


        fuelItemsInInventory.clear();
        for (IItem item : playerInventory.getInventory()){
            if (item.getCategory().equalsIgnoreCase("fuel")){
                fuelItemsInInventory.add(item);
            }
        }
        fuelIterator = fuelItemsInInventory.iterator();


        while (fuelIterator.hasNext() && consumedFuel < fuelAmountNeeded){
            IItem item = fuelIterator.next();
            if (item.getName().equalsIgnoreCase("firewood")){

                if (playerInventory.getInventory().remove(item)) {
                    consumedFuel +=1;
                    System.out.println("Mengonsumsi 1 firewood untuk memasak.");
                }
            }
        }

        if (consumedFuel < fuelAmountNeeded){
            System.err.println("warning: fuel tidak cukup untuk memasak. (This message implies a prior logic error)");
        }
    }

    


}