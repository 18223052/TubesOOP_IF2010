package GUI.panels;

import main.GamePanel;
import entity.Ingredient;
import entity.Recipe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.RenderingHints; // Import for antialiasing

public class CookingMenu extends BaseUIPanel {

    public int cookingMenuSelection = 0;
    public int selectRecipe = 0; // 0 for list, >0 for detail, -1 for fail message, -2 for locked message
    public boolean doneCooking = false;
    // public boolean hasIngradients = true; // Properti ini tidak lagi diperlukan

    public CookingMenu(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String text;
        List<Recipe> allRecipes = Recipe.getRecipeList();
        // cookableRecipes tidak lagi digunakan secara langsung di draw() karena pengecekan dilakukan per resep
        // List<Recipe> cookableRecipes = gp.cookingController.getCookableRecipe(); // Tidak perlu lagi di sini

        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth / 2 - gp.tileSize;
        int frameHeight = gp.screenHeight - (gp.tileSize * 2);

        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setFont(uiFont);
        g2.setColor(TEXT_PRIMARY);

        int textX = frameX + gp.tileSize / 2;
        int textY = frameY + gp.tileSize;
        int lineHeight = g2.getFontMetrics().getHeight() + 5;

        if (doneCooking) {
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "Selamat!";
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 2;

            g2.setFont(uiFont);
            text = "Anda berhasil memasak:";
            g2.drawString(text, textX, textY);
            textY += lineHeight;

            if (selectRecipe > 0 && selectRecipe <= allRecipes.size()) {
                g2.setColor(SELECTED_SLOT_COLOR);
                g2.drawString(allRecipes.get(selectRecipe - 1).title, textX + gp.tileSize, textY);
                g2.setColor(TEXT_PRIMARY);
            } else {
                g2.drawString("hidangan lezat!", textX + gp.tileSize, textY);
            }

            textY += lineHeight * 2;
            g2.drawString("Tekan ENTER untuk kembali.", textX, textY);

        } else if (selectRecipe == 0) { // Recipe List View
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "DAFTAR RESEP";
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 1.5;

            g2.setFont(uiFont);
            if (allRecipes.isEmpty()) {
                g2.drawString("Tidak ada resep yang tersedia.", textX, textY);
            } else {
                for (int i = 0; i < allRecipes.size(); i++) {
                    Recipe currentListedRecipe = allRecipes.get(i);
                    String recipeTitleText = (i + 1) + ". " + currentListedRecipe.title;

                    boolean isUnlocked = gp.cookingController.isRecipeUnlocked(currentListedRecipe);
                    boolean hasIngredients = gp.cookingController.hasEnoughIngredients(currentListedRecipe, gp.player.inventory);

                    if (i == cookingMenuSelection) {
                        g2.setColor(SELECTED_SLOT_COLOR);
                        g2.drawString(">" + recipeTitleText, textX - 10, textY);
                    } else {
                        if (!isUnlocked) {
                            g2.setColor(new Color(150, 150, 150)); 
                            g2.drawString(recipeTitleText + " (Terkunci)", textX, textY);
                        } else if (!hasIngredients) {
                            g2.setColor(new Color(255, 180, 0)); 
                            g2.drawString(recipeTitleText + " (Bahan Kurang)", textX, textY);
                        } else {
                            g2.setColor(new Color(120, 255, 120)); 
                            g2.drawString(recipeTitleText, textX, textY);
                        }
                    }
                    textY += lineHeight * 0.9;
                    g2.setColor(TEXT_PRIMARY); 
                }
            }
            textY += lineHeight * 1.5;
            g2.drawString("W/S atau ATAS/BAWAH: Pilih", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("ENTER: Lihat Detail", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("K atau ESC: Keluar", textX, textY);

        } else if (selectRecipe == -1) { 
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "Gagal Memasak!";
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 2;

            g2.setFont(uiFont);
            g2.setColor(new Color(255, 180, 0));
            g2.drawString("Anda tidak memiliki cukup", textX, textY);
            textY += lineHeight;
            g2.drawString("bahan atau sumber daya.", textX, textY);
            g2.setColor(TEXT_PRIMARY);

            textY += lineHeight * 2;
            g2.drawString("Tekan ENTER untuk kembali.", textX, textY);

        } else if (selectRecipe == -2) { // Recipe is locked
            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = "Resep Terkunci!";
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 2;

            g2.setFont(uiFont);
            g2.setColor(new Color(255, 100, 100));
            g2.drawString("Anda belum memenuhi syarat", textX, textY);
            textY += lineHeight;
            g2.drawString("untuk melihat/memasak resep ini.", textX, textY);
            g2.setColor(TEXT_PRIMARY);

            textY += lineHeight * 2;
            g2.drawString("Tekan ENTER untuk kembali.", textX, textY);

        } else { // Recipe Details View
            if (selectRecipe <= 0 || selectRecipe > allRecipes.size()) {
                this.selectRecipe = 0; 
                return;
            }
            Recipe currentRecipeToDisplay = allRecipes.get(selectRecipe - 1);

            g2.setFont(uiFont.deriveFont(Font.BOLD, 24f));
            text = currentRecipeToDisplay.title;
            drawCenteredString(g2, text, frameX, textY, frameWidth);
            textY += lineHeight * 1.5;

            boolean isUnlockedForDetails = gp.cookingController.isRecipeUnlocked(currentRecipeToDisplay);

            if (!isUnlockedForDetails) {
                g2.setFont(uiFont);
                g2.setColor(new Color(255, 100, 100)); // Red for missing condition
                textY += lineHeight;
                g2.drawString("Resep ini terkunci. Anda tidak", textX, textY);
                textY += lineHeight;
                g2.drawString("dapat melihat detailnya.", textX, textY);
                g2.setColor(TEXT_PRIMARY);
            } else {
                // Display Ingredients
                g2.setFont(uiFont.deriveFont(Font.BOLD));
                g2.drawString("Bahan-bahan:", textX, textY);
                textY += lineHeight;
                g2.setFont(uiFont);

                boolean hasAllIngredientsForThisRecipe = true;
                for (Ingredient ingredient : currentRecipeToDisplay.ingredients) {
                    String ingredientName = ingredient.name;
                    int requiredAmount = ingredient.amount;
                    int ownedAmount;

                    if (ingredientName.equalsIgnoreCase("Any Fish")) {
                        ownedAmount = gp.player.inventory.getItemCountByCategory("fish");
                    } else {
                        ownedAmount = gp.player.inventory.getItemCount(ingredientName);
                    }

                    String ingredientLine = "- " + ingredientName + " " + requiredAmount + "x (Milik: " + ownedAmount + ")";

                    if (ownedAmount < requiredAmount) {
                        g2.setColor(new Color(255, 120, 120));
                        hasAllIngredientsForThisRecipe = false;
                    } else {
                        g2.setColor(TEXT_PRIMARY);
                    }
                    g2.drawString(ingredientLine, textX + 15, textY);
                    textY += lineHeight * 0.9;
                }
                g2.setColor(TEXT_PRIMARY);

                textY += lineHeight * 0.5;
                g2.setFont(uiFont.deriveFont(Font.BOLD));
                g2.drawString("Menghasilkan:", textX, textY);
                textY += lineHeight * 0.9;
                g2.setFont(uiFont);
                g2.setColor(new Color(180, 220, 255));

                String outputName = currentRecipeToDisplay.getOutputItemName();
                g2.drawString(outputName, textX + 15, textY);
                g2.setColor(TEXT_PRIMARY);
                textY += lineHeight * 0.9;

                textY += lineHeight * 1.5;
                if (hasAllIngredientsForThisRecipe) {
                    g2.setColor(new Color(100, 255, 100));
                    g2.drawString("ENTER: Masak", textX, textY);
                } else {
                    g2.setColor(TEXT_SECONDARY);
                    g2.drawString("ENTER: Masak (Bahan tidak cukup)", textX, textY);
                }
                g2.setColor(TEXT_PRIMARY);
            }

            textY += lineHeight * 0.9;
            g2.drawString("0 atau BACKSPACE: Kembali ke Daftar", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("K atau ESC: Keluar Menu", textX, textY);
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
        Recipe chosenRecipe = allRecipes.get(cookingMenuSelection);
        if (gp.cookingController.isRecipeUnlocked(chosenRecipe)) {
            this.selectRecipe = cookingMenuSelection + 1; // Go to detail view
            this.doneCooking = false;
        } else {
            this.selectRecipe = -2; // Indicate locked recipe
            this.doneCooking = false;
        }
    }

    public void attemptToCookSelectedRecipe() {
        List<Recipe> allRecipes = Recipe.getRecipeList();
        if (selectRecipe <= 0 || selectRecipe > allRecipes.size()) {
            System.err.println("AttemptToCook: Invalid selectRecipe index: " + selectRecipe);
            this.selectRecipe = 0;
            return;
        }
        Recipe recipeToCook = allRecipes.get(selectRecipe - 1);

        // Pengecekan pertama: Apakah resepnya sudah terbuka?
        if (!gp.cookingController.isRecipeUnlocked(recipeToCook)) {
            System.out.println("Resep " + recipeToCook.title + " belum terbuka.");
            this.selectRecipe = -2; // Set state untuk pesan "terkunci"
            this.doneCooking = false;
            return;
        }

        // Jika resep terbuka, baru coba masak
        boolean cookingSuccessful = gp.cookingController.cookRecipe(recipeToCook);

        if (cookingSuccessful) {
            this.doneCooking = true;
        } else {
            this.selectRecipe = -1; // Indicate general cooking failure (ingredients, energy, fuel)
            this.doneCooking = false;
        }
    }

    public void resetMenuState() {
        this.selectRecipe = 0; // Kembali ke tampilan daftar resep
        this.doneCooking = false; // Reset status memasak selesai
        this.cookingMenuSelection = 0; // Reset seleksi ke resep pertama
        // Jika ada pesan error/status sebelumnya, pastikan juga direset di sini
        // Misalnya, jika Anda memiliki variabel String untuk pesan yang ditampilkan
    }
}