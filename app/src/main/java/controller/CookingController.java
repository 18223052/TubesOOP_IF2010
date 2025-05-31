package controller;

import entity.Ingredient;
import entity.Recipe;
import object.FoodItem;
import object.FuelItem;
import object.IItem;
import object.InventorySlot; 
import main.GamePanel;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Kelas CookingController merupakan kelas
 * yang mengontrol semua hal mengenai proses masak memasak
 * 
 * Kelas terkait : - CookingQueueEntry (urutan proses memasak)
 *                 - CookingMenu (Interface memasak)
 *                 - Recipe (patokan memasak)
 */

public class CookingController {
    private GamePanel gp;
    private Queue<CookingQueueEntry> cookingQueue;

    public CookingController(GamePanel gp) {
        this.gp = gp;
        this.cookingQueue = new LinkedList<>();
    }

    /**
     * Constraint resep yang harus terbuka dengan beberapa parameter
     * ada lima parameter utama : 
     * - dibeli dari store
     * - fishing counter >= 10
     * - item constraint (cek item spesifik ada di inventory)
     * - menangkap ikan spesifik
     * - memanen pertama kali
     */

    public boolean isRecipeUnlocked(Recipe recipe){
        switch (recipe.requirementType) {
            case NONE:
                return true; 
            case BOUGHT_FROM_STORE:
                boolean unlocked = gp.player.isRecipeUnlocked(recipe.id);
                return unlocked;
            case FISH_CAUGHT_COUNT:

                return gp.player.getFishCaughtCount() >= recipe.requirementAmount;
            case CAUGHT_SPECIFIC_FISH:

                return gp.player.hasCaughtFish(recipe.requirementValue); 
            case HAS_ITEM_IN_INVENTORY:

                return gp.player.inventory.hasItem(recipe.requirementValue);
            case FIRST_HARVEST:

                return gp.player.hasHarvestedFirstTime();
            default:
                return false; 
        }
    }

    public List<Recipe> getCookableRecipe() {
        List<Recipe> cookableRecipeList = new ArrayList<>();
        List<Recipe> allRecipes = Recipe.getRecipeList();

        InventoryController playerInventory = gp.player.inventory;

        for (Recipe recipe : allRecipes) {
            // System.out.println("Processing recipe: " + recipe.id + " (Requirement: " + recipe.requirementType + ")"); // DEBUG
            if (isRecipeUnlocked(recipe) && hasEnoughIngredients(recipe, playerInventory)) {
                cookableRecipeList.add(recipe);
            }
        }
        return cookableRecipeList;
    }


    /**
     * Constraint check untuk memasak seperti bahan (bahan makananan dan fuel)
     */

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

            return false;
        }

        return hasEnoughFuel(playerInventory, 1);
    }

    public boolean hasEnoughFuel(InventoryController playerInventory, int fuelAmountNeeded) {

        int totalAvailableFuelValue =0;
        for (InventorySlot slot : playerInventory.getInventorySlots()){
            IItem item = slot.getItem();

            if (item instanceof FuelItem){
                FuelItem fuel = (FuelItem) item;
                totalAvailableFuelValue += (fuel.getCurrentFuelValue()*slot.getQuantity());
            }
        }
        return totalAvailableFuelValue >= fuelAmountNeeded;
        // int totalFuelCapacity = 0;

        // for (InventorySlot slot : playerInventory.getInventorySlots()) {
        //     IItem item = slot.getItem();
        //     if (item.getCategory().equalsIgnoreCase("fuel")) {
        //         if (item.getName().equalsIgnoreCase("coal")) {
        //             totalFuelCapacity += (slot.getQuantity() * 2);
        //         } else if (item.getName().equalsIgnoreCase("firewood")) {
        //             totalFuelCapacity += (slot.getQuantity() * 1); 
        //         }
        //     }
        // }
        // return totalFuelCapacity >= fuelAmountNeeded;
    }


    /**
     * Proses memasak
     */

     public boolean cookRecipe(Recipe recipe) {
        if (recipe == null) {
            System.out.println("Resep null.");
            return false;
        }
        

        if (!isRecipeUnlocked(recipe)) {
            System.out.println("Resep " + recipe.title + " belum terbuka.");
            gp.ui.setDialog("Resep " + recipe.title + " belum terbuka.");
            gp.repaint();
            return false;
        }

        InventoryController playerInventory = gp.player.inventory;

        if (gp.player.getEnergy() < 10){
            System.out.println("Energi tidak cukup untuk memasak " + recipe.title);
            gp.ui.setDialog("Energi tidak cukup untuk memasak " + recipe.title);
            gp.repaint();
            return false;
        }


        if (!hasEnoughIngredients(recipe, playerInventory)) {
            System.out.println("Bahan atau sumber daya (fuel) tidak cukup untuk memasak " + recipe.title);
            gp.ui.setDialog("Bahan atau fuel tidak cukup untuk memasak " + recipe.title); // Tampilkan pesan di sini
            gp.repaint();
            return false;
        }

        gp.player.deductEnergy(10);
        System.out.println("Energi player dikurangi 10 untuk memasak");

        for (Ingredient requiredIngredient : recipe.ingredients) {
            String ingredientName = requiredIngredient.name;
            int amountToConsume = requiredIngredient.amount;

            if (ingredientName.equalsIgnoreCase("Any Fish")) {
                playerInventory.removeCategoryItems("fish", amountToConsume);
                System.out.println("Mengonsumsi " + amountToConsume + " ikan untuk resep.");
            } else {
                playerInventory.removeItems(ingredientName, amountToConsume);
                System.out.println("Mengonsumsi " + amountToConsume + "x " + ingredientName + " untuk resep.");
            }
        }

        consumeFuel(playerInventory, 1);

        long currentTotalGameMinutes = gp.gameTime.getTotalGameMinutes();
        int cookingDuration = 60;
        cookingQueue.add(new CookingQueueEntry(recipe, currentTotalGameMinutes, cookingDuration, gp));

        System.out.println("Memasak " + recipe.title + " telah dimulai! Akan selesai pada menit ke-" + (currentTotalGameMinutes + cookingDuration) + " in-game time.");
        gp.ui.setDialog("Memulai memasak " + recipe.title + "!"); 
        gp.repaint(); 
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
    int remainingFuelToConsume = fuelAmountNeeded;

    // Kumpulkan semua FuelItem dari inventory (Setiap item di sini adalah instance unik)
    List<FuelItem> availableFuelItems = new ArrayList<>();
    for (InventorySlot slot : playerInventory.getInventorySlots()) {
        if (slot.getItem() instanceof FuelItem) {
            availableFuelItems.add((FuelItem) slot.getItem());
        }
    }

    // Urutkan fuelItems untuk memprioritaskan konsumsi (misal: Firewood dulu, lalu Coal)
    // Urutkan berdasarkan nilai maxFuelValue (nilai awal fuel yang lebih kecil akan datang duluan)
    // Perbaikan: gunakan f1.getMaxFuelValue() dan f2.getMaxFuelValue()
    availableFuelItems.sort((f1, f2) -> Integer.compare(f1.getMaxFuelValue(), f2.getMaxFuelValue())); // <--- REVISI DI SINI

    // Iterasi melalui FuelItem yang ditemukan
    Iterator<FuelItem> fuelIterator = availableFuelItems.iterator();
    while (fuelIterator.hasNext() && remainingFuelToConsume > 0) {
        FuelItem currentFuelItem = fuelIterator.next();

        // Hitung berapa unit fuel yang akan diambil dari item ini
        int fuelToTakeFromThisItem = Math.min(remainingFuelToConsume, currentFuelItem.getCurrentFuelValue());

        if (fuelToTakeFromThisItem > 0) {
            currentFuelItem.deductFuel(fuelToTakeFromThisItem); // Kurangi nilai fuel dari objek FuelItem
            remainingFuelToConsume -= fuelToTakeFromThisItem;    // Kurangi kebutuhan fuel

            System.out.println("Mengonsumsi " + fuelToTakeFromThisItem + " fuel dari " + currentFuelItem.getName() +
                               ". Sisa fuel di item: " + currentFuelItem.getCurrentFuelValue() +
                               ". Sisa kebutuhan fuel: " + remainingFuelToConsume);

            if (currentFuelItem.isFuelEmpty()) {
                playerInventory.removeItemInstance(currentFuelItem);
                System.out.println(currentFuelItem.getName() + " habis dan dihapus dari inventory.");
            }
        }
    }

    if (remainingFuelToConsume > 0) {
        System.err.println("Warning: Gagal mengonsumsi semua fuel yang dibutuhkan! (Initial hasEnoughFuel check error)");
    }
}
}