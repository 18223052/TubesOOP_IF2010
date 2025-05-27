package controller;

import entity.Ingredient;
import entity.Recipe;
import object.FoodItem;
import object.IItem;
import object.InventorySlot; 
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

    public CookingController(GamePanel gp) {
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

        if (gp.player.getEnergy() < 10) {
            System.out.println("Energi tidak cukup untuk memasak " + recipe.title);
            return false;
        }

        // Check for fuel BEFORE cooking attempts
        return hasEnoughFuel(playerInventory, 1);
    }

    public boolean hasEnoughFuel(InventoryController playerInventory, int fuelAmountNeeded) {
        int totalFuelCapacity = 0;

        // Iterate through InventorySlot objects
        for (InventorySlot slot : playerInventory.getInventorySlots()) {
            IItem item = slot.getItem();
            if (item.getCategory().equalsIgnoreCase("fuel")) {
                if (item.getName().equalsIgnoreCase("coal")) {
                    totalFuelCapacity += (slot.getQuantity() * 2); // Coal gives 2 fuel capacity per item
                } else if (item.getName().equalsIgnoreCase("firewood")) {
                    totalFuelCapacity += (slot.getQuantity() * 1); // Firewood gives 1 fuel capacity per item
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

        // Double-check conditions immediately before cooking
        if (!hasEnoughIngredients(recipe, playerInventory)) {
            // hasEnoughIngredients already prints specific messages for energy/fuel
            System.out.println("Bahan tidak cukup untuk memasak " + recipe.title); // Redundant if hasEnoughIngredients logs it
            return false;
        }
        
        // Energy check is already part of hasEnoughIngredients, but good to ensure
        if (gp.player.getEnergy() < 10) {
            System.out.println("Energi tidak cukup untuk memasak " + recipe.title);
            return false;
        }

        // Fuel check is already part of hasEnoughIngredients
        if (!hasEnoughFuel(playerInventory, 1)) {
            System.out.println("Bahan bakar tidak cukup untuk memasak " + recipe.title);
            return false;
        }


        // Deduct energy first, as it's a direct player cost
        gp.player.deductEnergy(10);
        System.out.println("Energi player dikurangi 10 untuk memasak");

        // Consume ingredients
        for (Ingredient requiredIngredient : recipe.ingredients) {
            String ingredientName = requiredIngredient.name;
            int amountToConsume = requiredIngredient.amount;

            if (ingredientName.equalsIgnoreCase("Any Fish")) {
                // Consume "Any Fish" by removing items one by one until amountToConsume is met
                // The getItemCountByCategory already ensures we have enough.
                playerInventory.removeCategoryItems("fish", amountToConsume);
                System.out.println("Mengonsumsi " + amountToConsume + " ikan untuk resep.");
            } else {
                // Use the InventoryController's removeItems method for stackable items
                playerInventory.removeItems(ingredientName, amountToConsume);
                System.out.println("Mengonsumsi " + amountToConsume + "x " + ingredientName + " untuk resep.");
            }
        }

        // Consume fuel after ingredients
        consumeFuel(playerInventory, 1);

        long currentTotalGameMinutes = gp.gameTime.getTotalGameMinutes();
        int cookingDuration = 60;
        cookingQueue.add(new CookingQueueEntry(recipe, currentTotalGameMinutes, cookingDuration, gp));

        System.out.println("Memasak " + recipe.title + " telah dimulai! Akan selesai pada menit ke-" + (currentTotalGameMinutes + cookingDuration) + " in-game time.");
        return true;
    }

    public void update() {
        long currentTotalGameMinutes = gp.gameTime.getTotalGameMinutes();

        Iterator<CookingQueueEntry> iterator = cookingQueue.iterator();
        while (iterator.hasNext()) {
            CookingQueueEntry entry = iterator.next();
            if (currentTotalGameMinutes >= entry.getCompletionTimeInMinutes()) {
                FoodItem cookedFood = entry.createCookedFoodItem();
                if (cookedFood != null) {
                    gp.player.inventory.addItem(cookedFood);
                    System.out.println("Memasak selesai!, menambahkan " + cookedFood.getName());
                } else {
                    System.out.println("Gagal membuat item masakan.");
                }
                iterator.remove();
            } else {
                // Assuming items in the queue are ordered by completion time,
                // if the current item is not ready, subsequent items won't be either.
                break;
            }
        }
    }

    public boolean isCookingProgress() {
        return !cookingQueue.isEmpty();
    }

    public CookingQueueEntry getNextCookingItem() {
        return cookingQueue.peek();
    }

    private void consumeFuel(InventoryController playerInventory, int fuelAmountNeeded) {
        int consumedFuelValue = 0; // Tracks the total fuel value consumed

        // Prioritize Coal first
        Iterator<InventorySlot> inventoryIterator = playerInventory.getInventorySlots().iterator();
        while (inventoryIterator.hasNext() && consumedFuelValue < fuelAmountNeeded) {
            InventorySlot slot = inventoryIterator.next();
            IItem item = slot.getItem();

            if (item.getName().equalsIgnoreCase("coal")) {
                int coalFuelValue = 2; // Fuel value of one coal
                int numToConsume = (fuelAmountNeeded - consumedFuelValue + coalFuelValue - 1) / coalFuelValue; // Calculate how many coal needed
                numToConsume = Math.min(numToConsume, slot.getQuantity()); // Don't consume more than available

                if (numToConsume > 0) {
                    playerInventory.removeItems("coal", numToConsume); // Use the correct remove method
                    consumedFuelValue += (numToConsume * coalFuelValue);
                    System.out.println("Mengonsumsi " + numToConsume + " Coal untuk memasak. Total fuel consumed: " + consumedFuelValue);
                }
            }
        }

        // Then use Firewood if more fuel is still needed
        // Re-iterate the inventory or process remaining fuel items if coal wasn't enough
        inventoryIterator = playerInventory.getInventorySlots().iterator(); // Reset iterator
        while (inventoryIterator.hasNext() && consumedFuelValue < fuelAmountNeeded) {
            InventorySlot slot = inventoryIterator.next();
            IItem item = slot.getItem();

            if (item.getName().equalsIgnoreCase("firewood")) {
                int firewoodFuelValue = 1; // Fuel value of one firewood
                int numToConsume = (fuelAmountNeeded - consumedFuelValue + firewoodFuelValue - 1) / firewoodFuelValue; // Calculate how many firewood needed
                numToConsume = Math.min(numToConsume, slot.getQuantity()); // Don't consume more than available

                if (numToConsume > 0) {
                    playerInventory.removeItems("firewood", numToConsume); // Use the correct remove method
                    consumedFuelValue += (numToConsume * firewoodFuelValue);
                    System.out.println("Mengonsumsi " + numToConsume + " Firewood untuk memasak. Total fuel consumed: " + consumedFuelValue);
                }
            }
        }

        if (consumedFuelValue < fuelAmountNeeded) {
            System.err.println("Warning: fuel tidak cukup untuk memasak. (This message implies a prior logic error)");
            // This case should ideally not happen if hasEnoughFuel was checked correctly beforehand
        }
    }
}