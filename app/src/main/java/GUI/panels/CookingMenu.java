// src/ui/panels/CookingMenu.java
package GUI.panels;

import main.GamePanel;
import entity.Ingredient;
import entity.Recipe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

public class CookingMenu extends BaseUIPanel {

    // These need to be managed by the UI class or passed in (or handled by controller)
    // For now, they'll still be manipulated by the main UI.java and KeyHandler,
    // but the drawing logic is here.
    public int cookingMenuSelection = 0; 
    public int selectRecipe = 0; // 0 for list, 1+ for specific recipe details, -1 for failed
    public boolean doneCooking = false;
    public boolean hasIngradients = true;


    public CookingMenu(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2) {
        String text;
        List<Recipe> allRecipes = Recipe.getRecipeList();
        List<Recipe> cookableRecipes = gp.cookingController.getCookableRecipe();

        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth / 2 - gp.tileSize;
        int frameHeight = gp.screenHeight - (gp.tileSize * 2);

        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight); // Use utility

        g2.setFont(uiFont);
        g2.setColor(Color.WHITE);

        int textX = frameX + gp.tileSize / 2;
        int textY = frameY + gp.tileSize;
        int lineHeight = g2.getFontMetrics().getHeight() + 5;

        if (doneCooking) {
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "Congratulations!";
            drawCenteredString(g2, text, frameX, textY, frameWidth); // Use utility
            textY += lineHeight * 2;

            g2.setFont(uiFont);
            text = "You succeeded in cooking:";
            g2.drawString(text, textX, textY);
            textY += lineHeight;

            if (selectRecipe > 0 && selectRecipe <= allRecipes.size()) {
                g2.setColor(Color.CYAN);
                g2.drawString(allRecipes.get(selectRecipe - 1).title, textX + gp.tileSize, textY);
                g2.setColor(Color.WHITE);
            } else {
                g2.drawString("a delicious meal!", textX + gp.tileSize, textY);
            }

            textY += lineHeight * 2;
            g2.drawString("Press ENTER to return.", textX, textY);

        } else if (selectRecipe == 0) { // Recipe List View
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "RECIPE LIST";
            drawCenteredString(g2, text, frameX, textY, frameWidth); // Use utility
            textY += lineHeight * 1.5;

            g2.setFont(uiFont);
            if (allRecipes.isEmpty()) {
                g2.drawString("No recipes available in the game.", textX, textY);
            } else {
                for (int i = 0; i < allRecipes.size(); i++) {
                    Recipe currentListedRecipe = allRecipes.get(i);
                    String recipeTitleText = (i + 1) + ". " + currentListedRecipe.title;

                    if (i == cookingMenuSelection) {
                        g2.setColor(Color.YELLOW);
                        g2.drawString(">" + recipeTitleText, textX - 10, textY);
                    } else {
                        if (cookableRecipes.contains(currentListedRecipe)) {
                            g2.setColor(Color.GREEN);
                        } else {
                            g2.setColor(Color.LIGHT_GRAY); // Cannot cook
                        }
                        g2.drawString(recipeTitleText, textX, textY);
                    }
                    textY += lineHeight * 0.9;
                    g2.setColor(Color.WHITE);
                }
            }
            textY += lineHeight * 1.5;
            g2.drawString("W/S or UP/DOWN: Select", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("ENTER: View Details", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("K or ESC: Exit", textX, textY); // K is not defined in KeyHandler
        } else if (selectRecipe == -1) { // Not enough ingredients
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "Failed to Cook!";
            drawCenteredString(g2, text, frameX, textY, frameWidth); // Use utility
            textY += lineHeight * 2;

            g2.setFont(uiFont);
            g2.setColor(Color.ORANGE);
            g2.drawString("You don't have enough", textX, textY);
            textY += lineHeight;
            g2.drawString("ingredients for this recipe.", textX, textY);
            g2.setColor(Color.WHITE);

            textY += lineHeight * 2;
            g2.drawString("Press ENTER to return.", textX, textY);

        } else { // Recipe Details View
            if (selectRecipe <= 0 || selectRecipe > allRecipes.size()) {
                this.selectRecipe = 0; // Reset to list view if invalid
                return;
            }
            Recipe currentRecipeToDisplay = allRecipes.get(selectRecipe - 1);

            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = currentRecipeToDisplay.title;
            drawCenteredString(g2, text, frameX, textY, frameWidth); // Use utility
            textY += lineHeight * 1.5;

            boolean canViewRecipeDetails = true;
            // Special condition example: "Spakbor Salad" needs "Hot Pepper" to view details
            if (currentRecipeToDisplay.title.equals("Spakbor Salad") && !gp.inventoryController.hasItem("Hot Pepper")) {
                canViewRecipeDetails = false;
            }

            if (!canViewRecipeDetails) {
                g2.setFont(uiFont);
                g2.setColor(Color.PINK);
                textY += lineHeight;
                g2.drawString("You need a 'Hot Pepper' in your", textX, textY);
                textY += lineHeight;
                g2.drawString("inventory to view this recipe's details.", textX, textY);
                g2.setColor(Color.WHITE);
            } else {
                // Display Ingredients
                g2.setFont(uiFont.deriveFont(Font.BOLD));
                g2.drawString("Ingredients:", textX, textY);
                textY += lineHeight;
                g2.setFont(uiFont);

                boolean hasAllIngredientsForThisRecipe = true; // Flag for "Cook" button state
                for (Ingredient ingredient : currentRecipeToDisplay.ingredients) {
                    String ingredientName = ingredient.name;
                    int requiredAmount = ingredient.amount;
                    int ownedAmount;

                    if (ingredientName.equalsIgnoreCase("Any Fish")) {
                        ownedAmount = gp.inventoryController.getItemCountByCategory("fish");
                    } else {
                        ownedAmount = gp.inventoryController.getItemCount(ingredientName);
                    }

                    String ingredientLine = "- " + ingredientName + " " + requiredAmount + "x (Have: " + ownedAmount + ")";

                    if (ownedAmount < requiredAmount) {
                        g2.setColor(Color.RED); // Not enough ingredients
                        hasAllIngredientsForThisRecipe = false;
                    } else {
                        g2.setColor(Color.WHITE); // Enough ingredients
                    }
                    g2.drawString(ingredientLine, textX + 15, textY);
                    textY += lineHeight * 0.9;
                }
                g2.setColor(Color.WHITE); // Reset color

                textY += lineHeight * 0.5;
                g2.setFont(uiFont.deriveFont(Font.BOLD));
                g2.drawString("Produces:", textX, textY);
                textY += lineHeight * 0.9;
                g2.setFont(uiFont);
                g2.setColor(Color.CYAN);

                String outputName = currentRecipeToDisplay.getOutputItemName();
                g2.drawString(outputName, textX + 15, textY);
                g2.setColor(Color.WHITE);
                textY += lineHeight * 0.9;

                textY += lineHeight * 1.5;
                if (hasAllIngredientsForThisRecipe) {
                    g2.setColor(Color.GREEN);
                    g2.drawString("ENTER: Cook", textX, textY);
                } else {
                    g2.setColor(Color.GRAY); // Gray out if cannot cook
                    g2.drawString("ENTER: Cook (Not enough ingredients)", textX, textY);
                }
                g2.setColor(Color.WHITE);
            }

            textY += lineHeight * 0.9;
            g2.drawString("0 or BACKSPACE: Back to List", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("K or ESC: Exit Menu", textX, textY); // K is not defined in KeyHandler
        }
    }

    // These methods should be controlled by KeyHandler, not the UI's draw logic
    // but the variables are public, so they can be set externally.
    public void moveSelectionUp() {
        List<Recipe> allRecipes = Recipe.getRecipeList();
        if (allRecipes.isEmpty()) {
            return;
        }
        cookingMenuSelection--;
        if (cookingMenuSelection < 0) {
            cookingMenuSelection = allRecipes.size() - 1;
        }
    }

    public void moveSelectionDown() {
        List<Recipe> allRecipes = Recipe.getRecipeList();
        if (allRecipes.isEmpty()) {
            return;
        }
        cookingMenuSelection++;
        if (cookingMenuSelection >= allRecipes.size()) {
            cookingMenuSelection = 0;
        }
    }

    public void selectChosenRecipe() {
        List<Recipe> allRecipes = Recipe.getRecipeList();
        if (allRecipes.isEmpty() || cookingMenuSelection < 0 || cookingMenuSelection >= allRecipes.size()) {
            return;
        }
        this.selectRecipe = cookingMenuSelection + 1;
        this.doneCooking = false;
    }

    public void attemptToCookSelectedRecipe() {
        List<Recipe> allRecipes = Recipe.getRecipeList();
        if (selectRecipe <= 0 || selectRecipe > allRecipes.size()) {
            System.err.println("AttemptToCook: Invalid selectRecipe index: " + selectRecipe);
            this.selectRecipe = 0;
            return;
        }
        Recipe recipeToCook = allRecipes.get(selectRecipe - 1);
        boolean cookingSuccessful = gp.cookingController.cookRecipe(recipeToCook);

        if (cookingSuccessful) {
            this.doneCooking = true;
        } else {
            this.selectRecipe = -1; // Indicate failure
            this.doneCooking = false;
        }
    }
}