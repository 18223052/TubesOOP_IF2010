package GUI.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import entity.Ingredient;
import entity.Recipe;
import main.GamePanel; // Import for antialiasing

public class CookingMenu extends BaseUIPanel {

    public int cookingMenuSelection = 0;
    public int selectRecipe = 0;
    public boolean doneCooking = false;
    public boolean hasIngradients = true;

    public CookingMenu(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2) {
        // Enable antialiasing for smoother graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String text;
        List<Recipe> allRecipes = Recipe.getRecipeList();
        List<Recipe> cookableRecipes = gp.cookingController.getCookableRecipe();

        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth / 2 - gp.tileSize;
        int frameHeight = gp.screenHeight - (gp.tileSize * 2);

        // Use the enhanced drawSubWindow from BaseUIPanel
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setFont(uiFont);
        g2.setColor(TEXT_PRIMARY); // Use theme primary text color

        int textX = frameX + gp.tileSize / 2;
        int textY = frameY + gp.tileSize;
        int lineHeight = g2.getFontMetrics().getHeight() + 5;

        if (doneCooking) {
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "Selamat!"; // Updated text
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 2;

            g2.setFont(uiFont);
            text = "Anda berhasil memasak:"; // Updated text
            g2.drawString(text, textX, textY);
            textY += lineHeight;

            if (selectRecipe > 0 && selectRecipe <= allRecipes.size()) {
                g2.setColor(SELECTED_SLOT_COLOR); // Use theme color for highlight
                g2.drawString(allRecipes.get(selectRecipe - 1).title, textX + gp.tileSize, textY);
                g2.setColor(TEXT_PRIMARY); // Reset to theme primary text color
            } else {
                g2.drawString("hidangan lezat!", textX + gp.tileSize, textY); // Updated text
            }

            textY += lineHeight * 2;
            g2.drawString("Tekan ENTER untuk kembali.", textX, textY); // Updated text

        } else if (selectRecipe == 0) { // Recipe List View
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "DAFTAR RESEP"; // Updated text
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 1.5;

            g2.setFont(uiFont);
            if (allRecipes.isEmpty()) {
                g2.drawString("Tidak ada resep yang tersedia.", textX, textY); // Updated text
            } else {
                for (int i = 0; i < allRecipes.size(); i++) {
                    Recipe currentListedRecipe = allRecipes.get(i);
                    String recipeTitleText = (i + 1) + ". " + currentListedRecipe.title;

                    if (i == cookingMenuSelection) {
                        g2.setColor(SELECTED_SLOT_COLOR); // Use theme color for selection
                        g2.drawString(">" + recipeTitleText, textX - 10, textY);
                    } else {
                        if (cookableRecipes.contains(currentListedRecipe)) {
                            g2.setColor(new Color(120, 255, 120)); // Green for cookable
                        } else {
                            g2.setColor(TEXT_SECONDARY); // Light gray for cannot cook
                        }
                        g2.drawString(recipeTitleText, textX, textY);
                    }
                    textY += lineHeight * 0.9;
                    g2.setColor(TEXT_PRIMARY); // Reset to theme primary text color
                }
            }
            textY += lineHeight * 1.5;
            g2.drawString("W/S atau ATAS/BAWAH: Pilih", textX, textY); // Updated text
            textY += lineHeight * 0.9;
            g2.drawString("ENTER: Lihat Detail", textX, textY); // Updated text
            textY += lineHeight * 0.9;
            g2.drawString("K atau ESC: Keluar", textX, textY); // Updated text

        } else if (selectRecipe == -1) { // Not enough ingredients
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "Gagal Memasak!"; // Updated text
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 2;

            g2.setFont(uiFont);
            g2.setColor(new Color(255, 180, 0)); // Orange for warning
            g2.drawString("Anda tidak memiliki cukup", textX, textY); // Updated text
            textY += lineHeight;
            g2.drawString("bahan untuk resep ini.", textX, textY); // Updated text
            g2.setColor(TEXT_PRIMARY); // Reset to theme primary text color

            textY += lineHeight * 2;
            g2.drawString("Tekan ENTER untuk kembali.", textX, textY); // Updated text

        } else { // Recipe Details View
            if (selectRecipe <= 0 || selectRecipe > allRecipes.size()) {
                this.selectRecipe = 0; // Reset to list view if invalid
                return;
            }
            Recipe currentRecipeToDisplay = allRecipes.get(selectRecipe - 1);

            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = currentRecipeToDisplay.title;
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 1.5;

            if (!hasRecipe(currentRecipeToDisplay)) {
                g2.setFont(uiFont);
                g2.setColor(new Color(255, 100, 100)); // Red for missing condition
                textY += lineHeight;
                g2.drawString("Anda tidak memiliki " + currentRecipeToDisplay.id, textX, textY); // Updated text
                textY += lineHeight;
                g2.setColor(TEXT_PRIMARY); // Reset to theme primary text color
            } else {
                // Display Ingredients
                g2.setFont(uiFont.deriveFont(Font.BOLD));
                g2.drawString("Bahan-bahan:", textX, textY); // Updated text
                textY += lineHeight;
                g2.setFont(uiFont);

                boolean hasAllIngredientsForThisRecipe = true;
                for (Ingredient ingredient : currentRecipeToDisplay.ingredients) {
                    String ingredientName = ingredient.name;
                    int requiredAmount = ingredient.amount;
                    int ownedAmount;

                    if (ingredientName.equalsIgnoreCase("Any Fish")) {
                        ownedAmount = gp.inventoryController.getItemCountByCategory("fish");
                    } else {
                        ownedAmount = gp.inventoryController.getItemCount(ingredientName);
                    }

                    String ingredientLine = "- " + ingredientName + " " + requiredAmount + "x (Milik: " + ownedAmount + ")"; // Updated text

                    if (ownedAmount < requiredAmount) {
                        g2.setColor(new Color(255, 120, 120)); // Red for insufficient
                        hasAllIngredientsForThisRecipe = false;
                    } else {
                        g2.setColor(TEXT_PRIMARY); // Primary text color
                    }
                    g2.drawString(ingredientLine, textX + 15, textY);
                    textY += lineHeight * 0.9;
                }
                g2.setColor(TEXT_PRIMARY); // Reset color

                textY += lineHeight * 0.5;
                g2.setFont(uiFont.deriveFont(Font.BOLD));
                g2.drawString("Menghasilkan:", textX, textY); // Updated text
                textY += lineHeight * 0.9;
                g2.setFont(uiFont);
                g2.setColor(new Color(180, 220, 255)); // Light blue for product name

                String outputName = currentRecipeToDisplay.getOutputItemName();
                g2.drawString(outputName, textX + 15, textY);
                g2.setColor(TEXT_PRIMARY);
                textY += lineHeight * 0.9;

                textY += lineHeight * 1.5;
                if (hasAllIngredientsForThisRecipe) {
                    g2.setColor(new Color(100, 255, 100)); // Green for cookable action
                    g2.drawString("ENTER: Masak", textX, textY); // Updated text
                } else {
                    g2.setColor(TEXT_SECONDARY); // Gray out if cannot cook
                    g2.drawString("ENTER: Masak (Bahan tidak cukup)", textX, textY); // Updated text
                }
                g2.setColor(TEXT_PRIMARY);
            }

            textY += lineHeight * 0.9;
            g2.drawString("0 atau BACKSPACE: Kembali ke Daftar", textX, textY); // Updated text
            textY += lineHeight * 0.9;
            g2.drawString("K atau ESC: Keluar Menu", textX, textY); // Updated text
        }
    }

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

        if (hasRecipe(recipeToCook)) {
            boolean cookingSuccessful = gp.cookingController.cookRecipe(recipeToCook);
            if (cookingSuccessful) {
                this.doneCooking = true;
            } else {
                this.selectRecipe = -1; // Indicate failure
                this.doneCooking = false;
            }
        }
    }

    private boolean hasRecipe(Recipe recipe) {
        boolean hasRecipe = recipe.hasRecipe;
        if (!hasRecipe) {
            if (recipe.id.equals("recipe_1") || recipe.id.equals("recipe_10")) {
                hasRecipe = gp.player.inventory.getItemCount(recipe.id) > 0;
            } else if (recipe.id.equals("recipe_3")) {
                hasRecipe = gp.player.fishCaught >= 10;
            } else if (recipe.id.equals("recipe_4")) {
                hasRecipe = gp.player.hasFishingPuerfish;
            } else if (recipe.id.equals("recipe_7")) {
                hasRecipe = gp.player.hasHarvested;
            } else if (recipe.id.equals("recipe_8")) {
                hasRecipe = gp.player.inventory.getItemCount("hotpepper") > 0;
            } else if (recipe.id.equals("recipe_11")) {
                hasRecipe = gp.player.hasFishingLegend;
            }
        }
        return hasRecipe;
    }
}
