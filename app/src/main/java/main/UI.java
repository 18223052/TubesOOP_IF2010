package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import controller.CookingController;
import controller.InventoryController;
import controller.ShippingBinController;
import controller.StoreController;
import entity.Ingredient;
import entity.NPC_mayortadi;
import entity.Recipe;
import object.IConsumable;
import object.IFishAttributes;
import object.IItem;

public class UI {

    GamePanel gp;
    Font arial_40;
    Graphics2D g2;
    Font retroFont, arial_20;
    Recipe recipe;
    private Font maruMonica;
    private InventoryController inventory;
    private CookingController cookingController;
    private ShippingBinController shippingBin;
    private StoreController store;

    public int cookingMenuSelection = 0; // 0-indexed: represents the index in getCookableRecipe() list

    // Your existing variables for cooking state feedback
    public int selectRecipe = 0; // 0: showing list, >0: specific recipe selected, -1: not enough ingredients
    public boolean doneCooking = false;
    public boolean hasIngradients = true;

    public String currentDialog = "";

    public UI(GamePanel gp) {
        this.gp = gp;
        this.inventory = gp.inventoryController;
        this.cookingController = gp.cookingController; 
        this.shippingBin = gp.shippingBinController;
        this.store = gp.storeController;
        arial_40 = new Font("Arial", Font.PLAIN, 40);

        try {

            InputStream inputStream = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            this.maruMonica = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(inputStream));

            arial_20 = maruMonica.deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();

            maruMonica = new Font("Arial", Font.PLAIN, 16);
            arial_20 = maruMonica.deriveFont(20f);
        }
    }

    private void setupDefaultGraphics(Graphics2D g2) {
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        setupDefaultGraphics(g2);

        if (gp.gameState == gp.playState) {
            drawTimeHUD();
            gp.repaint();
        }
        if (gp.gameState == gp.pauseState) {
            drawPause();
        }
        if (gp.gameState == gp.dialogState) {
            drawDialogScreen();
        }
        if (gp.gameState == gp.inventoryState) {

            drawInventory();
        }
        if (gp.gameState == gp.statsState) {
            drawCharacterScreen();
        }
        if (gp.gameState == gp.cookingState) {
            drawCookingMenu();
        }
        if (gp.gameState == gp.shippingBinState) {
            drawInventory();
            drawShippingBin();
        }
        if (gp.gameState == gp.storeState) {
            drawInventory();
            drawStore();
        }
    }

    public void drawCharacterScreen() {

        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 32;

        g2.drawString("Name", textX, textY);
        textY += lineHeight;
        g2.drawString("Gender", textX, textY);
        textY += lineHeight;
        g2.drawString("Farm Map", textX, textY);
        textY += lineHeight;
        g2.drawString("Energy", textX, textY);
        textY += lineHeight;
        g2.drawString("Gold", textX, textY);
        textY += lineHeight;
        g2.drawString("Partner", textX, textY);
        textY += lineHeight;
        // values
        int tailX = (frameX + frameWidth) - 30;
        // reset
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.getName());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getGender());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getFarmMap());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getEnergy());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getGold());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // value = String.valueOf(gp.player.getPartner());
        // textX = getXforAllignToRight(value, tailX);
        // g2.drawString(value, textX, textY);
        // textY += lineHeight;
    }

    public void drawPause() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSE";
        int x = getXCenterText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text, int screenWidth) {
        int length = (int) this.g2.getFontMetrics().getStringBounds(text, this.g2).getWidth();
        // this.gp.getClass();
        int x = screenWidth / 2 - length / 2;
        return x;
    }

    public void drawDialogScreen() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 8;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 3;

        drawSubWindow(x, y, width, height);

        g2.setFont(maruMonica.deriveFont(20f));
        g2.setColor(Color.WHITE);


        int textX = x + 20;
        int textY = y + 30;
        int lineHeight = 30;

        if (gp.currNPC != null) {
            String npcName = "";
            // Example: Get name based on NPC type or a common getName() method
            if (gp.currNPC instanceof NPC_mayortadi) {
                npcName = ((NPC_mayortadi) gp.currNPC).getName();
            }
            // else if (gp.currNPC instanceof SomeOtherNPC) {
            // npcName = ((SomeOtherNPC) gp.currNPC).getName();
            // }
            // Or if your base Entity class has a name:
            // npcName = gp.currNPC.name;


            if (npcName != null && !npcName.isEmpty()) {
                g2.setColor(new Color(255, 255, 150)); // Yellow for NPC name
                g2.drawString(npcName, textX, textY);
                g2.setColor(Color.WHITE);
                textY += lineHeight; // Move Y down for the dialog content
            }
        }

        // Display the current dialog text (works for both NPC and TV/object messages)
        if (currentDialog != null && !currentDialog.isEmpty()) {
            String[] paragraphs = currentDialog.split("\n");

            for (String paragraph : paragraphs) {
                String[] words = paragraph.split(" ");
                StringBuilder line = new StringBuilder();

                for (String word : words) {
                    if (g2.getFontMetrics().stringWidth(line + " " + word) < width - 40) { // 40 for padding
                        if (line.length() > 0) {
                            line.append(" ");
                        }
                        line.append(word);
                    } else {
                        g2.drawString(line.toString(), textX, textY);
                        textY += lineHeight;
                        line = new StringBuilder(word);

                        if (textY > y + height - 30 - lineHeight) { // Check if next line will overflow (30 for bottom padding)
                            g2.drawString("...", textX + width - 50, textY); // Indicate more text
                            break; // Break word loop
                        }
                    }
                }

                if (line.length() > 0) { // Draw the last line of the paragraph
                    if (textY <= y + height - 30) { // Check if it fits
                        g2.drawString(line.toString(), textX, textY);
                        textY += lineHeight;
                    } else if (!line.toString().equals("...")) { // if it didn't fit, but wasn't already ellipsis
                        // This case means the very first line of a paragraph already overflows,
                        // or the last part of a word-wrapped line overflows.
                        // Consider if "..." should be drawn here too.
                        // For simplicity, we might just let it clip or be partially drawn.
                    }
                }
                if (textY > y + height - 30 - lineHeight && paragraphs.length > 1 && !paragraph.equals(paragraphs[paragraphs.length-1])) {
                    break; // Break paragraph loop if overflowed
                }
                textY += 5; // Small gap between paragraphs
            }
            // Prompt to continue
            g2.setFont(maruMonica.deriveFont(Font.ITALIC, 16f));
            g2.drawString("Press [E] to continue", x + width - 180, y + height - 20);
            g2.drawString("Press [S] to Shop", textX, y + height - 20);
        } else {
            // Fallback if currentDialog is null or empty for some reason
            g2.drawString("...", textX, textY);
            g2.setFont(maruMonica.deriveFont(Font.ITALIC, 16f));
            g2.drawString("Press [E] to exit", x + width - 180, y + height - 20);
        }
    }

    public void setDialog(String dialog) {
        this.currentDialog = dialog;
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new java.awt.BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXCenterText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    public int getXforAllignToRight(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }

    public void moveCookingSelectionUp() {
        List<Recipe> allRecipes = Recipe.getRecipeList();
        if (allRecipes.isEmpty()) {
            return;
        }
        cookingMenuSelection--;
        if (cookingMenuSelection < 0) {
            cookingMenuSelection = allRecipes.size() - 1;
        }
    }

    public void moveCookingSelectionDown() {
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
        this.doneCooking = false; // Reset status
    }

    public void attemptToCookSelectedRecipe() {
        List<Recipe> allRecipes = Recipe.getRecipeList();

        if (selectRecipe <= 0 || selectRecipe > allRecipes.size()) {
            System.err.println("AttemptToCook: Invalid selectRecipe index: " + selectRecipe);
            this.selectRecipe = 0; // Kembali ke list jika ada kesalahan
            return;
        }

        Recipe recipeToCook = allRecipes.get(selectRecipe - 1);

        if (gp.cookingController.cookRecipe(recipeToCook)) {
            this.doneCooking = true; // Berhasil memasak

        } else {
            // Gagal memasak (kemungkinan karena bahan tidak cukup, dicek lagi di controller)
            this.selectRecipe = -1; // state kalo masaknya amburadul
            this.doneCooking = false;
        }
    }

    public void drawCookingMenu() {
        String text;
        List<Recipe> allRecipes = Recipe.getRecipeList();
        List<Recipe> cookableRecipes = gp.cookingController.getCookableRecipe();

        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        // int frameWidth = gp.screenWidth / 3; 
        int frameWidth = gp.screenWidth / 2 - gp.tileSize;
        int frameHeight = gp.screenHeight - (gp.tileSize * 2);

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(arial_20);
        g2.setColor(Color.WHITE);

        int textX = frameX + gp.tileSize / 2; // Padding dari kiri window
        int textY = frameY + gp.tileSize;    // Padding dari atas window
        int lineHeight = g2.getFontMetrics().getHeight() + 5; // Spasi antar baris

        if (doneCooking) { // State: Berhasil memasak
            g2.setFont(arial_20.deriveFont(Font.BOLD, 24f));
            text = "Congratulations!";
            drawCenteredString(text, frameX, textY, frameWidth); // Helper method untuk center text
            textY += lineHeight * 2;

            g2.setFont(arial_20);
            text = "You succeeded in cooking:";
            g2.drawString(text, textX, textY);
            textY += lineHeight;

            // Tampilkan nama resep yang berhasil dimasak
            if (selectRecipe > 0 && selectRecipe <= allRecipes.size()) {
                g2.setColor(Color.CYAN); // Warna berbeda untuk hasil
                g2.drawString(allRecipes.get(selectRecipe - 1).title, textX + gp.tileSize, textY);
                g2.setColor(Color.WHITE);
            } else {
                // Ini seharusnya tidak terjadi jika doneCooking=true dan selectRecipe valid
                g2.drawString("a delicious meal!", textX + gp.tileSize, textY);
            }

            textY += lineHeight * 2;
            g2.drawString("Press ENTER to return.", textX, textY);

        } else if (selectRecipe == 0) { // State: Menampilkan Daftar Resep
            g2.setFont(arial_20.deriveFont(Font.BOLD, 24f));
            text = "RECIPE LIST";
            drawCenteredString(text, frameX, textY, frameWidth);
            textY += lineHeight * 1.5;

            g2.setFont(arial_20);
            if (allRecipes.isEmpty()) {
                g2.drawString("No recipes available in the game.", textX, textY);
            } else {
                for (int i = 0; i < allRecipes.size(); i++) {
                    Recipe currentListedRecipe = allRecipes.get(i);
                    String recipeTitleText = (i + 1) + ". " + currentListedRecipe.title;

                    if (i == cookingMenuSelection) { // Resep yang sedang dipilih
                        g2.setColor(Color.YELLOW);
                        g2.drawString(">" + recipeTitleText, textX - 10, textY); // Tambah panah
                    } else {

                        if (cookableRecipes.contains(currentListedRecipe)) {
                            g2.setColor(Color.GREEN); // Bisa dimasak
                        } else {
                            g2.setColor(Color.LIGHT_GRAY); // nda bisa masak
                        }
                        g2.drawString(recipeTitleText, textX, textY);
                    }
                    textY += lineHeight * 0.9;
                    g2.setColor(Color.WHITE); // Reset warna untuk iterasi berikutnya
                }
            }
            textY += lineHeight * 1.5;
            g2.drawString("W/S or UP/DOWN: Select", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("ENTER: View Details", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("K or ESC: Exit", textX, textY);

        } else if (selectRecipe == -1) { //bahan ora cekap
            g2.setFont(arial_20.deriveFont(Font.BOLD, 24f));
            text = "Failed to Cook!";
            drawCenteredString(text, frameX, textY, frameWidth);
            textY += lineHeight * 2;

            g2.setFont(arial_20);
            g2.setColor(Color.ORANGE);
            g2.drawString("You don't have enough", textX, textY);
            textY += lineHeight;
            g2.drawString("ingredients for this recipe.", textX, textY);
            g2.setColor(Color.WHITE);

            textY += lineHeight * 2;
            g2.drawString("Press ENTER to return.", textX, textY);

        } else {
            if (selectRecipe <= 0 || selectRecipe > allRecipes.size()) {

                this.selectRecipe = 0;

                return;
            }
            Recipe currentRecipeToDisplay = allRecipes.get(selectRecipe - 1);

            g2.setFont(arial_20.deriveFont(Font.BOLD, 24f));
            text = currentRecipeToDisplay.title;
            drawCenteredString(text, frameX, textY, frameWidth);
            textY += lineHeight * 1.5;
            boolean hasAllIngredientsForThisRecipe = true; // Flag untuk tombol "Cook"
            // --- Logika khusus "Spakbor Salad" (Hot Pepper) ---
            boolean canViewRecipeDetails = true;
            if (currentRecipeToDisplay.title.equals("Spakbor Salad") && !gp.inventoryController.hasItem("Hot Pepper")) {
                canViewRecipeDetails = false;
            }

            if (!canViewRecipeDetails) {
                g2.setFont(arial_20);
                g2.setColor(Color.PINK);
                textY += lineHeight;
                g2.drawString("You need a 'Hot Pepper' in your", textX, textY);
                textY += lineHeight;
                g2.drawString("inventory to view this recipe's details.", textX, textY);
                g2.setColor(Color.WHITE);
            } else {
                // --- Tampilkan Bahan ---
                g2.setFont(arial_20.deriveFont(Font.BOLD));
                g2.drawString("Ingredients:", textX, textY);
                textY += lineHeight;
                g2.setFont(arial_20);

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
                        g2.setColor(Color.RED); // Bahan kurang
                        hasAllIngredientsForThisRecipe = false;
                    } else {
                        g2.setColor(Color.WHITE); // Bahan cukup
                    }
                    g2.drawString(ingredientLine, textX + 15, textY);
                    textY += lineHeight * 0.9;
                }
                g2.setColor(Color.WHITE); // Reset warna

                textY += lineHeight * 0.5;
                g2.setFont(arial_20.deriveFont(Font.BOLD));
                g2.drawString("Produces:", textX, textY);
                textY += lineHeight * 0.9;
                g2.setFont(arial_20);
                g2.setColor(Color.CYAN);

                String outputName = currentRecipeToDisplay.getOutputItemName();
                // FoodItem outputPreview = gp.itemFactory.createFood(outputName);
                // if (outputPreview != null) {
                //     g2.drawString(outputPreview.getName(), textX + 15, textY);
                //     // Anda juga bisa menggambar icon kecil dari outputPreview.getImage() di sini
                // } else {
                g2.drawString(outputName, textX + 15, textY); // Fallback jika createFood gagal atau tidak perlu preview
                // }
                g2.setColor(Color.WHITE);
                textY += lineHeight * 0.9;
            }

            textY += lineHeight * 1.5;
            if (canViewRecipeDetails) {

                if (hasAllIngredientsForThisRecipe) {
                    g2.setColor(Color.GREEN);
                    g2.drawString("ENTER: Cook", textX, textY);
                } else {
                    g2.setColor(Color.GRAY); // Warna abu-abu jika tidak bisa masak
                    g2.drawString("ENTER: Cook (Not enough ingredients)", textX, textY);
                }
                g2.setColor(Color.WHITE);
            }

            textY += lineHeight * 0.9;
            g2.drawString("0 or BACKSPACE: Back to List", textX, textY);
            textY += lineHeight * 0.9;
            g2.drawString("K or ESC: Exit Menu", textX, textY);
        }
    }

    public void drawInventory() {
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;

        // Draw main window
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        // Draw category filters
        drawCategoryFilters(g2, frameX, frameY - 30, frameWidth);

        // Draw inventory info
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.white);
        g2.drawString("Items: " + inventory.getInventory().size() + "/" + inventory.getInventoryMaxSize(), frameX + 10, frameY + 20);

        // Draw inventory slots
        int slotX = frameX + 10;
        int slotY = frameY + 35;
        int itemCount = 0;

        for (int i = 0; i < inventory.getInventory().size(); i++) { // Iterasi melalui inventaris aktual
            IItem item = inventory.getInventory().get(i);

            // Filter berdasarkan kategori
            if (!inventory.getInventoryFilter().equals("all") && !item.getCategory().equalsIgnoreCase(inventory.getInventoryFilter())) {
                continue;
            }

            // Hanya gambar item yang terlihat di layar inventaris (asumsi 4 kolom)
            int col = itemCount % 4;
            int row = itemCount / 4;

            int currentSlotX = frameX + 10 + (col * (gp.tileSize + 5));
            int currentSlotY = frameY + 35 + (row * (gp.tileSize + 25));

            if (i == inventory.getSelectedSlot()) {
                g2.setColor(new Color(255, 255, 0, 128));
                g2.fillRect(currentSlotX - 5, currentSlotY - 5, gp.tileSize + 10, gp.tileSize + 10);
            }

            g2.setColor(Color.white);
            g2.drawRect(currentSlotX, currentSlotY, gp.tileSize, gp.tileSize);

            if (item.getImage() != null) {
                g2.drawImage(item.getImage(), currentSlotX + 5, currentSlotY + 5, gp.tileSize - 10, gp.tileSize - 10, null);
            } else {
                g2.setColor(Color.gray);
                g2.fillRect(currentSlotX + 5, currentSlotY + 5, gp.tileSize - 10, gp.tileSize - 10);
            }

            // Draw category indicator
            g2.setFont(new Font("Arial", Font.ITALIC, 8));
            g2.setColor(getCategoryColor(item.getCategory()));
            g2.drawString(item.getCategory(), currentSlotX, currentSlotY + gp.tileSize + 22);

            itemCount++;
        }

        if (inventory.getSelectedItem() != null) {
            drawItemDetails(g2, inventory.getSelectedItem(), frameX, frameY + frameHeight + 10);
        }
    }

    private void drawCategoryFilters(Graphics2D g2, int x, int y, int width) {
        String[] categories = {"all", "tools", "consumables", "crops", "fish", "seeds"};
        int buttonWidth = width / categories.length;

        for (int i = 0; i < categories.length; i++) {
            int buttonX = x + (i * buttonWidth);

            // Highlight active filter
            if (categories[i].equals(inventory.getInventoryFilter())) {
                g2.setColor(new Color(255, 255, 100, 200));
            } else {
                g2.setColor(new Color(100, 100, 100, 200));
            }
            g2.fillRect(buttonX, y, buttonWidth, 25);

            g2.setColor(Color.white);
            g2.drawRect(buttonX, y, buttonWidth, 25);

            g2.setFont(new Font("Arial", Font.BOLD, 10));
            int textX = buttonX + (buttonWidth / 2) - (g2.getFontMetrics().stringWidth(categories[i]) / 2);
            g2.drawString(categories[i], textX, y + 15);
        }
    }

    /**
     * Draws the details of the selected item
     */
    private void drawItemDetails(Graphics2D g2, IItem item, int x, int y) {
        int detailWidth = gp.tileSize * 6;
        int detailHeight = gp.tileSize * 2;

        drawSubWindow(g2, x, y, detailWidth, detailHeight);

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.yellow);
       
        g2.drawString(item.getName(), x + 10, y + 20);

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.white);
        g2.drawString("Category: " + item.getCategory(), x + 10, y + 40);
        g2.drawString("Buy: " + item.getBuyPrice() + "g | Sell: " + item.getSellPrice() + "g", x + 10, y + 60);

        if (item instanceof IConsumable) {
            g2.drawString("Energy: +" + ((IConsumable) item).getEnergyRestoration(), x + 10, y + 80);
        }

        if (item instanceof IFishAttributes) {
            IFishAttributes fishItem = (IFishAttributes) item;
            g2.setFont(new Font("Arial", Font.ITALIC, 10));
            int currentY = y + 80;
            if (item instanceof IConsumable) {
                currentY += 15;
            }
            g2.drawString("Season: " + fishItem.getSeason() + " | Weather: " + fishItem.getWeather(), x + 10, currentY);
        }

        g2.setFont(new Font("Arial", Font.ITALIC, 11));
        if (gp.gameState == gp.inventoryState) {
            g2.drawString("E: Use/Equip", x + 10, y + 100);
        }
    }

    /**
     * Returns color associated with item category
     */
    private Color getCategoryColor(String category) {
        switch (category) {
            case "tools":
                return new Color(150, 150, 255);
            case "consumables":
                return new Color(255, 150, 150);
            case "crops":
                return new Color(150, 255, 150);
            case "fish":
                return new Color(150, 200, 255);
            case "seeds":
                return new Color(255, 255, 150);
            default:
                return Color.gray;
        }
    }

    private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new java.awt.BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private void drawCenteredString(String text, int frameX, int y, int frameWidth) {
        if (g2 == null || g2.getFontMetrics() == null) {
            return;
        }
        int textWidth = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        g2.drawString(text, frameX + (frameWidth - textWidth) / 2, y);
    }

    public void drawTimeHUD() {
        String season = gp.gameTime.getSeasonName();
        g2.setFont(arial_20);
        g2.setColor(Color.white);
        String time = String.format("Day %d - %02d:%02d", gp.gameTime.getGameDay(), gp.gameTime.getGameHour(), gp.gameTime.getGameMinute());
        String seasonText = "Season: " + season;
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        g2.drawString(time, x, y);
        g2.drawString(seasonText, x, y + 30);
    }

    public void drawShippingBin() {
        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 7;

        drawSubWindow(g2, frameX, frameY - 40, frameWidth, 35);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Shipping Bin", frameX + (frameWidth / 3) + 10, frameY - 17);
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.white);
        g2.drawString("Selling Items : " + shippingBin.sellingItems().size() + "/" + shippingBin.sellingItemsMaxSize(), frameX + 12, frameY + 22);
        g2.drawString("Total Price : " + shippingBin.goldEarned + "g", (frameWidth - frameX) - 20, frameY + 22);

        int itemCount = 0;
        for (int i = 0; i < shippingBin.sellingItems().size(); i++) {
            IItem item = shippingBin.sellingItems().get(i);
            int col = itemCount % 4;
            int row = itemCount / 4;
            int currentSlotX = frameX + 10 + (col * (gp.tileSize + 5));
            int currentSlotY = frameY + 35 + (row * (gp.tileSize + 25));
            if (i == shippingBin.getSelectedSlot()) {
                g2.setColor(new Color(255, 255, 0, 128));
                g2.fillRect(currentSlotX - 5, currentSlotY - 5, gp.tileSize + 10, gp.tileSize + 10);
            }
            g2.setColor(Color.white);
            g2.drawRect(currentSlotX, currentSlotY, gp.tileSize, gp.tileSize);
            if (item.getImage() != null) {
                g2.drawImage(item.getImage(), currentSlotX + 5, currentSlotY + 5, gp.tileSize - 10, gp.tileSize - 10, null);
            } else {
                g2.setColor(Color.gray);
                g2.fillRect(currentSlotX + 5, currentSlotY + 5, gp.tileSize - 10, gp.tileSize - 10);
            }
            g2.setFont(new Font("Arial", Font.ITALIC, 8));
            g2.setColor(getCategoryColor(item.getCategory()));
            g2.drawString(item.getCategory(), currentSlotX, currentSlotY + gp.tileSize + 22);
            itemCount++;
        }
        if (shippingBin.getSelectedItem() != null) {
            drawItemDetails(g2, shippingBin.getSelectedItem(), frameX, frameY + frameHeight + 10);
        }
    }

    public void drawStore() {
        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;

        drawSubWindow(g2, frameX, frameY - 40, frameWidth, 35);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Store", frameX + (frameWidth / 3) + 20, frameY - 17);
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.white);
        g2.drawString("Available Items : " + store.storeItems().size() + "/" + store.storeItemsMaxSize(), frameX + 12, frameY + 22);
        g2.drawString("Your Gold : " + gp.player.getGold() + "g", (frameWidth - frameX) - 10, frameY + 22);

        int itemCount = 0;
        for (int i = 0; i < store.storeItems().size(); i++) {
            IItem item = store.storeItems().get(i);
            int col = itemCount % 4;
            int row = itemCount / 4;
            int currentSlotX = frameX + 10 + (col * (gp.tileSize + 5));
            int currentSlotY = frameY + 35 + (row * (gp.tileSize + 25));
            if (i == store.getSelectedSlot()) {
                if (item.getBuyPrice() <= gp.player.getGold()) {
                    g2.setColor(new Color(0, 213, 190, 128));
                } else {
                    g2.setColor(new Color(255, 100, 103, 128));
                }
                g2.fillRect(currentSlotX - 5, currentSlotY - 5, gp.tileSize + 10, gp.tileSize + 10);
            } else {
                if (item.getBuyPrice() <= gp.player.getGold()) {
                    g2.setColor(new Color(0, 213, 190, 128));
                } else {
                    g2.setColor(new Color(255, 100, 103, 128));
                }
            }
            g2.drawRect(currentSlotX, currentSlotY, gp.tileSize, gp.tileSize);
            if (item.getImage() != null) {
                g2.drawImage(item.getImage(), currentSlotX + 5, currentSlotY + 5, gp.tileSize - 10, gp.tileSize - 10, null);
            } else {
                g2.setColor(Color.gray);
                g2.fillRect(currentSlotX + 5, currentSlotY + 5, gp.tileSize - 10, gp.tileSize - 10);
            }
            g2.setFont(new Font("Arial", Font.ITALIC, 8));
            g2.setColor(getCategoryColor(item.getCategory()));
            g2.drawString(item.getCategory(), currentSlotX, currentSlotY + gp.tileSize + 22);
            itemCount++;
        }
        if (store.getSelectedItem() != null) {
            drawItemDetails(g2, store.getSelectedItem(), frameX, frameY + frameHeight + 10);
        }
    }

}
