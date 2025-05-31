package controller;

import entity.Recipe;
import main.GamePanel;
import object.FoodItem;


/**
 * Kelas CookingQueueEntry merupakan kelas
 * yang mengontrol urutan masuknya foodItem ke inventory setelah memasak
 * 
 */

public class CookingQueueEntry {
    private Recipe recipe;
    private long completionTimeInMinutes; 
    private GamePanel gp; 
    public CookingQueueEntry(Recipe recipe, long startTimeInMinutes, int durationInMinutes, GamePanel gp) {
        this.recipe = recipe;
        this.completionTimeInMinutes = startTimeInMinutes + durationInMinutes;
        this.gp = gp;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public long getCompletionTimeInMinutes() {
        return completionTimeInMinutes;
    }

    public FoodItem createCookedFoodItem() {
        if (gp != null && gp.itemFactory != null) {
            return gp.itemFactory.createFood(recipe.getOutputItemName());
        }
        return null;
    }
    
}
