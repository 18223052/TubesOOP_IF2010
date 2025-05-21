package entity;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    public String id;
    public String title;
    public Ingredient[] ingredients;
    public String outputItemName;

    // Parameterized constructor
    public Recipe(String id, String title, Ingredient[] ingredients, String outputItemName) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.outputItemName = outputItemName;
    }

    public String getOutputItemName(){
        return this.outputItemName;
    }

    public static List<Recipe> getRecipeList() {
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(
                "recipe_1",
                "Fish n' Chips",
                new Ingredient[]{
                    new Ingredient("Any Fish", 2),
                    new Ingredient("Wheat", 1),
                    new Ingredient("Potato", 1),},
                "Fish n' Chips"
        ));

        recipes.add(new Recipe(
                "recipe_2",
                "Baguette",
                new Ingredient[]{
                    new Ingredient("Wheat", 3),},
                "Baguette"
        ));

        recipes.add(new Recipe(
                "recipe_3",
                "Sashimi",
                new Ingredient[]{
                    new Ingredient("Salmon", 3),},
                "Sashimi"
        ));

        recipes.add(new Recipe(
                "recipe_4",
                "Fugu",
                new Ingredient[]{
                    new Ingredient("Puerfish", 1),},
                "Fugu"
        ));

        recipes.add(new Recipe(
                "recipe_5",
                "Wine",
                new Ingredient[]{
                    new Ingredient("Grape", 2),},
                "Wine"
        ));

        recipes.add(new Recipe(
                "recipe_6",
                "Pumpkin Pie",
                new Ingredient[]{
                    new Ingredient("Egg", 1),
                    new Ingredient("Wheat", 1),
                    new Ingredient("Pumpkin", 1),},
                "Pumpkin Pie"
        ));

        recipes.add(new Recipe(
                "recipe_7",
                "Veggie Soup",
                new Ingredient[]{
                    new Ingredient("Cauliflower", 1),
                    new Ingredient("Parsnip", 1),
                    new Ingredient("Potato", 1),
                    new Ingredient("Tomato", 1),},
                "Veggie Soup"
        ));

        recipes.add(new Recipe(
                "recipe_8",
                "Fish Stew",
                new Ingredient[]{
                    new Ingredient("Any fish", 2),
                    new Ingredient("Hot pepper", 1),
                    new Ingredient("Cauliflower", 2)},
                "Fish Stew"
        ));

        recipes.add(new Recipe(
                "recipe_9",
                "Spakbor Salad",
                new Ingredient[]{
                    new Ingredient("Melon", 1),
                    new Ingredient("Cranberry", 1),
                    new Ingredient("Blueberry", 1),
                    new Ingredient("Tomato", 1)},
                "Spakbor Salad"
        ));

        recipes.add(new Recipe(
                "recipe_10",
                "Fish Sandwich",
                new Ingredient[]{
                    new Ingredient("Any fish", 1),
                    new Ingredient("Wheat", 2),
                    new Ingredient("Tomato", 1),
                    new Ingredient("Hot Pepper", 1)},
                "Fish Sandwich"
        ));

        recipes.add(new Recipe(
                "recipe_11",
                "The Legends of Spakbor",
                new Ingredient[]{
                    new Ingredient("Legend fish", 1),
                    new Ingredient("Potato", 2),
                    new Ingredient("Parsnip", 1),
                    new Ingredient("Tomato", 1),
                    new Ingredient("Eggplant", 1)},
                "The Legends of Spakbor"
        ));

        return recipes;
    }
}
