package controller;


import java.util.ArrayList;

import entity.Ingredient;
import entity.Recipe;
import main.GamePanel;
import object.FoodItem;
import object.IConsumable;
import object.IFishAttributes;
import object.IItem;

public class InventoryController {

    private GamePanel gp;
    private ArrayList<IItem> inventory;
    private EatingController eatingController;
    private int selectedSlot;
    private final int maxInventorySize = 20; 
    
    // Add category filtering
    private String currentFilter = "all"; // "all", "tools", "consumables", "crops", etc.

    public InventoryController(GamePanel gp) {
        this.gp = gp;
        this.inventory = new ArrayList<>();
        this.selectedSlot = 0;
        this.eatingController = new EatingController(gp);
    }

    public String getInventoryFilter(){
        return currentFilter;
    }

    public int getInventoryMaxSize(){
        return maxInventorySize;
    }


    public void addItem(IItem item) {
        if (inventory.size() < maxInventorySize) {

            if (item.isStackable()) {
                boolean itemExists = false;
                for (IItem i : inventory) {
                    if (i.getName().equals(item.getName())) {

                        itemExists = true;
                        break;
                    }
                }
                if (!itemExists) {
                    inventory.add(item);
                }
            } else {

                inventory.add(item);
            }
            System.out.println("Added item: " + item.getName() + " to inventory");
        } else {
            System.out.println("Inventory is full!");
        }
    }


    public void removeItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem removed = inventory.remove(index);
            System.out.println("Removed item: " + removed.getName() + " from inventory");

            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            }
        }
    }

    // Get the currently selected item
    public IItem getSelectedItem() {
        if (inventory.isEmpty()) {
            return null;
        }
        return inventory.get(selectedSlot);
    }


    public void moveSelectionUp() {
        if (selectedSlot - 4 >= 0) {
            selectedSlot -= 4;
        }
    }

    public void moveSelectionDown() {
        if (selectedSlot + 4 < inventory.size()) {
            selectedSlot += 4;
        }
    }

    public void moveSelectionLeft() {
        if (selectedSlot % 4 != 0) {
            selectedSlot--;
        }
    }

    public void moveSelectionRight() {
        if ((selectedSlot + 1) % 4 != 0 && selectedSlot + 1 < inventory.size()) {
            selectedSlot++;
        }
    }

    public int getSelectedSlot(){
        return selectedSlot;
    }
    

    public void setFilter(String category) {
        this.currentFilter = category;
    }


    public void useItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem item = inventory.get(index);
            
            if (item.getCategory().equals("tools")) {
                System.out.println("Equipped: " + item.getName());
                gp.player.setActiveItem(item);
            } else if (item instanceof IConsumable) {
                System.out.println("Consumed: " + item.getName());
                eatingController.consume((IConsumable) item);
                removeItem(index);
            } else {
                System.out.println("This item can be sold: " + item.getName());
            }
        }
    }
    

    public void sellItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem item = inventory.get(index);
            int goldGained = item.getSellPrice();
            
            // Add gold to player
            // gp.player.addGold(goldGained);
            System.out.println("Sold " + item.getName() + " for " + goldGained + " gold");

            removeItem(index);
        }
    }


    public boolean hasItem(String itemName) {
        for (IItem item : inventory) {
            if (item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void discardItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem removed = inventory.remove(index);
            System.out.println("Discarded item: " + removed.getName());

            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            }
        }
    }

    // Update the inventory state (you can use this to refresh the UI or item actions)
    public void update() {
        // Updated controls logic in KeyHandler will call the movement methods
    }    

    public ArrayList<IItem> getInventory() {
        return inventory;
    }

    // // === COOKING RELATED METHODS ===
    
    // /**
    //  * Checks if player has the ingredients for a recipe
    //  */
    // public boolean hasIngredientsForRecipe(Recipe recipe) {
    //     for (Ingredient i : recipe.ingredients) {
    //         int reqAmount = i.amount;
    //         int availableAmount = getItemCount(i.name);

    //         if (i.name.toLowerCase().contains("any fish")) {
    //             availableAmount = getItemCountByCategory("fish");
    //         }
            
    //         if (availableAmount < reqAmount) {
    //             return false;
    //         }
    //     }
    //     return true;
    // }

    // /**
    //  * Consumes ingredients for a recipe
    //  */
    // public boolean consumeIngredientsForRecipe(Recipe recipe) {
    //     if (!hasIngredientsForRecipe(recipe)) {
    //         return false;
    //     }
        
    //     Map<String, Integer> toConsume = new HashMap<>();
        
    //     for (Ingredient ingredient : recipe.ingredients) {
    //         String ingredientName = ingredient.name;
    //         int amount = ingredient.amount;
            
    //         // Special case for "any fish"
    //         if (ingredientName.toLowerCase().contains("any fish")) {
    //             int consumed = 0;
    //             for (int i = inventory.size() - 1; i >= 0 && consumed < amount; i--) {
    //                 IItem item = inventory.get(i);
    //                 if (item.getCategory().equals("fish")) {
    //                     inventory.remove(i);
    //                     consumed++;
    //                     System.out.println("Consumed: " + item.getName() + " for recipe");
    //                 }
    //             }
    //         } else {
    //             toConsume.put(ingredientName, amount);
    //         }
    //     }
        
    //     // Consume specific ingredients
    //     for (Map.Entry<String, Integer> entry : toConsume.entrySet()) {
    //         String itemName = entry.getKey();
    //         int amountToConsume = entry.getValue();
            
    //         for (int i = inventory.size() - 1; i >= 0 && amountToConsume > 0; i--) {
    //             IItem item = inventory.get(i);
    //             if (item.getName().equalsIgnoreCase(itemName)) {
    //                 inventory.remove(i);
    //                 amountToConsume--;
    //                 System.out.println("Consumed: " + item.getName() + " for recipe");
    //             }
    //         }
    //     }
        
    //     // Update selected slot if needed
    //     if (selectedSlot >= inventory.size() && inventory.size() > 0) {
    //         selectedSlot = inventory.size() - 1;
    //     }
        
    //     return true;
    // }
    
    // /**
    //  * Cooks a recipe and adds the result to inventory
    //  */
    // public boolean cookRecipe(Recipe recipe) {
    //     if (!consumeIngredientsForRecipe(recipe)) {
    //         return false;
    //     }
        
    //     String recipeName = recipe.title.toLowerCase().replace(" ", "");
    //     FoodItem cookedItem = gp.itemFactory.createFood(recipeName);
        
    //     if (cookedItem == null) {
    //         // Create generic consumable if specific recipe not found in factory
    //         cookedItem = new FoodItem(recipe.title, 0, 50, 30, gp);
    //     }
        
    //     addItem(cookedItem);
    //     System.out.println("Successfully cooked: " + recipe.title);
    //     return true;
    // }
    
    // // === UTILITY METHODS ===
    
    // /**
    //  * Gets the count of a specific item in inventory
    //  */
    // public int getItemCount(String itemName) {
    //     int count = 0;
    //     for (IItem item : inventory) {
    //         if (item.getName().equalsIgnoreCase(itemName)) {
    //             count++;
    //         }
    //     }
    //     return count;
    // }
    
    // /**
    //  * Gets the total count of items in a specific category
    //  */
    // public int getItemCountByCategory(String category) {
    //     int count = 0;
    //     for (IItem item : inventory) {
    //         if (item.getCategory().equals(category)) {
    //             count++;
    //         }
    //     }
    //     return count;
    // }
    
    // /**
    //  * Gets all items of a specific category
    //  */
    // public ArrayList<IItem> getItemsByCategory(String category) {
    //     ArrayList<IItem> categoryItems = new ArrayList<>();
    //     for (IItem item : inventory) {
    //         if (item.getCategory().equals(category)) {
    //             categoryItems.add(item);
    //         }
    //     }
    //     return categoryItems;
    // }
    
    // /**
    //  * Checks if player has a specific tool equipped
    //  */
    public boolean hasToolEquipped(String toolName) {
        IItem activeItem = gp.player.getActiveItem();
        return activeItem != null && activeItem.getName().equalsIgnoreCase(toolName);
    }
}