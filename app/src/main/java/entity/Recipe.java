package entity;

import java.util.ArrayList;
import java.util.List;



public class Recipe {

    public String id;
    public String title;
    public Ingredient[] ingredients;
    public String outputItemName;
    public RecipeRequirementType requirementType; 
    public String requirementValue; 
    public int requirementAmount; 

    // Constructor baru
    public Recipe(String id, String title, Ingredient[] ingredients, String outputItemName,
                 RecipeRequirementType requirementType, String requirementValue, int requirementAmount) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.outputItemName = outputItemName;
        this.requirementType = requirementType;
        this.requirementValue = requirementValue;
        this.requirementAmount = requirementAmount;
    }

    public String getOutputItemName(){
        return this.outputItemName;
    }

    public static List<Recipe> getRecipeList() {
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(
                "Fish n Chips",
                "Fish n' Chips",
                new Ingredient[]{
                    new Ingredient("Any Fish", 2),
                    new Ingredient("Wheat", 1),
                    new Ingredient("Potato", 1),},
                "Fish n' Chips",
                RecipeRequirementType.BOUGHT_FROM_STORE,
                "recipe_fish_n_chips",
                0

        ));

        recipes.add(new Recipe(
                "recipe_2",
                "Baguette",
                new Ingredient[]{
                    new Ingredient("Wheat", 3),},
                "Baguette",
                RecipeRequirementType.NONE,
                null,
                0
        ));

        recipes.add(new Recipe(
                "recipe_3",
                "Sashimi",
                new Ingredient[]{
                        new Ingredient("Salmon", 3),
                },
                "Sashimi",
                RecipeRequirementType.FISH_CAUGHT_COUNT, 
                null,
                10 
        ));

        recipes.add(new Recipe(
                "recipe_4",
                "Fugu",
                new Ingredient[]{
                        new Ingredient("Puerfish", 1),
                },
                "Fugu",
                RecipeRequirementType.CAUGHT_SPECIFIC_FISH, // Memancing pufferfish
                "Pufferfish", 
                1 
        ));

        recipes.add(new Recipe(
                "recipe_5",
                "Wine",
                new Ingredient[]{
                        new Ingredient("Grape", 2),
                },
                "Wine",
                RecipeRequirementType.NONE, // Bawaan
                null,
                0
        ));

        recipes.add(new Recipe(
                "recipe_6",
                "Pumpkin Pie",
                new Ingredient[]{
                        new Ingredient("Egg", 1),
                        new Ingredient("Wheat", 1),
                        new Ingredient("Pumpkin", 1),
                },
                "Pumpkin Pie",
                RecipeRequirementType.NONE, // Bawaan
                null,
                0
        ));

        recipes.add(new Recipe(
                "recipe_7",
                "Veggie Soup",
                new Ingredient[]{
                        new Ingredient("Cauliflower", 1),
                        new Ingredient("Parsnip", 1),
                        new Ingredient("Potato", 1),
                        new Ingredient("Tomato", 1),
                },
                "Veggie Soup",
                RecipeRequirementType.FIRST_HARVEST, // Setelah panen pertama
                null,
                0
        ));

        recipes.add(new Recipe(
                "recipe_8",
                "Fish Stew",
                new Ingredient[]{
                        new Ingredient("Any fish", 2),
                        new Ingredient("Hot pepper", 1),
                        new Ingredient("Cauliflower", 2)
                },
                "Fish Stew",
                RecipeRequirementType.HAS_ITEM_IN_INVENTORY, // Memiliki "Hot Pepper"
                "hotpepper", // Nama item
                1 // Cukup 1 Hot Pepper
        ));

        recipes.add(new Recipe(
                "recipe_9",
                "Spakbor Salad",
                new Ingredient[]{
                        new Ingredient("Melon", 1),
                        new Ingredient("Cranberry", 1),
                        new Ingredient("Blueberry", 1),
                        new Ingredient("Tomato", 1)
                },
                "Spakbor Salad",
                RecipeRequirementType.NONE, // Bawaan
                null,
                0
        ));

        recipes.add(new Recipe(
                "Fish Sandwich",
                "recipe_fish_sandwich",
                new Ingredient[]{
                        new Ingredient("Any fish", 1),
                        new Ingredient("Wheat", 2),
                        new Ingredient("Tomato", 1),
                        new Ingredient("Hot Pepper", 1)
                },
                "Fish Sandwich",
                RecipeRequirementType.BOUGHT_FROM_STORE, // Resep ini perlu dibeli
                "recipe_fish_sandwich", 
                0
        ));

        recipes.add(new Recipe(
                "recipe_11",
                "The Legends of Spakbor",
                new Ingredient[]{
                        new Ingredient("Legend fish", 1),
                        new Ingredient("Potato", 2),
                        new Ingredient("Parsnip", 1),
                        new Ingredient("Tomato", 1),
                        new Ingredient("Eggplant", 1)
                },
                "The Legends of Spakbor",
                RecipeRequirementType.CAUGHT_SPECIFIC_FISH, // Memancing "Legend fish"
                "Legend fish",
                1 // Cukup sekali memancing Legend fish
        ));

        return recipes;
    }
}
