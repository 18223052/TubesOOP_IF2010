package controller;

import entity.Ingredient;
import entity.Recipe;
import object.FoodItem;
import object.IItem;
import object.ItemFactory;
import main.GamePanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CookingController {
    private GamePanel gp;
    private ItemFactory itemFactory;

    public CookingController(GamePanel gp){
        this.gp = gp;
        this.itemFactory = new ItemFactory(gp);
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
        return true;
    }

    public boolean cookRecipe(Recipe recipe) {
        if (recipe == null) {
            System.out.println("Recipe is null, cannot cook.");
            return false;
        }

        InventoryController playerInventory = gp.player.inventory;


        if (!hasEnoughIngredients(recipe, playerInventory)) {
            System.out.println("Not enough ingredients to cook " + recipe.title);
            return false;
        }

        // Kedua, konsumsi bahan
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
                // Hapus item yang ditandai dari inventaris asli
                for(IItem itemToRemove : itemsToRemoveFromOriginal) {
                    playerInventory.getInventory().remove(itemToRemove); 
                                                                      
                }
                 if (removedFishCount < amountToConsume) {
                    System.err.println("Error logic: Not enough fish were removed for Any Fish ingredient.");

                    return false;
                }


            } else {
                playerInventory.removeItems(ingredientName, amountToConsume); 
            }
        }


        String cookedFoodName = recipe.getOutputItemName(); 
        // String cookedFoodName = formatRecipeTitleToFoodName(recipe.title); 

        FoodItem cookedFood = itemFactory.createFood(cookedFoodName); 
        if (cookedFood != null) {
            playerInventory.addItem(cookedFood);
            System.out.println("Successfully cooked " + recipe.title + "! Added " + cookedFood.getName() + " to inventory.");
            return true;
        } else {
            System.err.println("Error: Could not create food item '" + cookedFoodName + "' for recipe " + recipe.title + ". ItemFactory might not recognize this food name.");
            return false;
        }
    }

}
