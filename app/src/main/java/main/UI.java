package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import java.util.Objects;

import controller.InventoryController;

import java.util.List;

import entity.Ingredient;
import entity.NPC_mayortadi;
import entity.Ingredient;
import entity.Recipe;
import object.IConsumable;
import object.IFishAttributes;
import object.IItem;
import entity.Recipe;

public class UI {

    GamePanel gp;
    Font arial_40;
    Graphics2D g2;
    Font retroFont, arial_20;
    Recipe recipe;
    private Font maruMonica;
    private InventoryController inventory;

    int selectRecipe = 0;
    boolean hasIngradients, doneCooking, hasHotPapper;

    public String currentDialog = "";

    public UI(GamePanel gp) {
        this.gp = gp;
        this.inventory = gp.inventoryController;
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
        if (gp.gameState == gp.statsState){
            drawCharacterScreen();
        }
        if (gp.gameState == gp.cookingState){
            drawCookingMenu();
        }
    }


    public void drawCharacterScreen(){
      

        final int frameX = gp.tileSize*2;
        final int frameY = gp.tileSize;
        final int frameWidth= gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
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

        value = String.valueOf(gp.player.getPartner());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
    }

    public void drawPause() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
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
            if (gp.currNPC instanceof NPC_mayortadi) {
                npcName = ((NPC_mayortadi)gp.currNPC).getName();
                

                g2.setColor(new Color(255, 255, 150));
                g2.drawString(npcName, textX, textY);
                g2.setColor(Color.WHITE); 
                

                textY += lineHeight;
            }
            

            if (currentDialog != null && !currentDialog.isEmpty()) {

                String[] paragraphs = currentDialog.split("\n");
                
                for (String paragraph : paragraphs) {

                    String[] words = paragraph.split(" ");
                    StringBuilder line = new StringBuilder();
                    
                    for (String word : words) {

                        if (g2.getFontMetrics().stringWidth(line + " " + word) < width - 40) {
                            if (line.length() > 0) {
                                line.append(" ");
                            }
                            line.append(word);
                        } else {

                            g2.drawString(line.toString(), textX, textY);
                            textY += lineHeight;
                            line = new StringBuilder(word);
                            
                          
                            if (textY > y + height - 30) {
                                
                                g2.drawString("...", width - 50, textY);
                                break;
                            }
                        }
                    }
                    
                    
                    if (line.length() > 0) {
                        g2.drawString(line.toString(), textX, textY);
                        textY += lineHeight;
                    }
                    
                    
                    textY += 5;
                }
            } else {
                
                g2.drawString("...", textX, textY);
            }
            
           
            g2.setFont(maruMonica.deriveFont(Font.ITALIC, 16f));
            g2.drawString("Press [E] to continue", x + width - 180, y + height - 20);
        } else {
 
            g2.drawString("No one to talk to...", textX, textY);
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
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
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

    public void drawCookingMenu() {
        // ganti hasIngradients menjadi false untuk test tidak memiliki ingradients
        hasIngradients = true;
        String text;
        List<Recipe> recipes = recipe.getRecipeList();

        int x = 20;
        int y = x;
        int width = gp.screenWidth / 3;
        int height = gp.screenHeight / 2 + gp.tileSize + y;
        drawSubWindow(x, y, width, height);

        if (doneCooking) {
            text = "Congratulations";
            x += y;
            y += gp.tileSize;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
            this.g2.drawString(text, x, y);
            y += 20;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));

            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("You succeeded in cooking", x, y);
            y += this.g2.getFontMetrics().getHeight();
            this.g2.drawString(recipes.get(selectRecipe - 1).title, x, y);
            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("Press 0 to recipe menu", x, y);

            if (gp.keyH.singleNumPress == 0) {
                selectRecipe = 0;
                doneCooking = false;
            }
        } else if (selectRecipe == 0) {
            text = "RECIPE LIST";
            x += y;
            y += gp.tileSize;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
            this.g2.drawString(text, x, y);
            y += 20;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));

            int i = 0;
            for (Recipe recipe : recipes) {
                i++;
                y += this.g2.getFontMetrics().getHeight();
                this.g2.drawString(i + ". " + recipe.title, x, y);
            }
            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("Press number you want to cook", x, y);
            y += this.g2.getFontMetrics().getHeight();
            this.g2.drawString("and hit enter.", x, y);

            if (gp.keyH.enterPressed && !gp.keyH.multiNumPress.equals("")) {
                if (Integer.parseInt(gp.keyH.multiNumPress) >= 1 && Integer.parseInt(gp.keyH.multiNumPress) < 11) {
                    selectRecipe = Integer.parseInt(gp.keyH.multiNumPress);
                    gp.keyH.multiNumPress = "";
                }
                // gp.keyH.keyPressed = "";
            }
        } else if (selectRecipe == -1) {
            text = "Failed to Cook";
            x += y;
            y += gp.tileSize;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
            this.g2.drawString(text, x, y);
            y += 20;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));

            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("Please complete the ingredients", x, y);
            y += this.g2.getFontMetrics().getHeight();
            this.g2.drawString("to be able to cook.", x, y);
            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("Press 0 to recipe menu", x, y);

            if (gp.keyH.singleNumPress == 0) {
                selectRecipe = 0;
            }
        } else {
            if (selectRecipe == 8 && !hasHotPapper) {
                text = "Failed to Open Recipe";
                x += y;
                y += gp.tileSize;
                this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
                this.g2.drawString(text, x, y);
                y += 20;
                this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));

                y += this.g2.getFontMetrics().getHeight() * 2;
                this.g2.drawString("You must have a hot papper ", x, y);
                y += this.g2.getFontMetrics().getHeight();
                this.g2.drawString("to view the recipe.", x, y);
                y += this.g2.getFontMetrics().getHeight() * 2;
                this.g2.drawString("Press 0 to recipe menu", x, y);

                if (gp.keyH.singleNumPress == 0) {
                    selectRecipe = 0;
                }
            } else {
                text = recipes.get(selectRecipe - 1).title + " Ingredient";
                x += y;
                y += gp.tileSize;
                this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
                this.g2.drawString(text, x, y);
                y += 20;
                int i = 0;
                this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));
                for (Ingredient ingredient : recipes.get(selectRecipe - 1).ingredients) {
                    i++;
                    y += this.g2.getFontMetrics().getHeight();
                    this.g2.drawString(i + ". " + ingredient.name + " " + ingredient.amount + "x", x, y);
                }
                y += this.g2.getFontMetrics().getHeight() * 2;
                this.g2.drawString("Press 1 to cook", x, y);
                y += this.g2.getFontMetrics().getHeight();
                this.g2.drawString("Press 0 to recipe menu", x, y);
                if (gp.keyH.singleNumPress == 0) {
                    selectRecipe = 0;
                } else if (gp.keyH.singleNumPress == 1) {
                    hasIngradients = selectRecipe == 2 || selectRecipe == 5 || selectRecipe == 6 || selectRecipe == 9;

                    if (hasIngradients) {
                        doneCooking = true;
                        // do event on successfully cooked
                    } else {
                        selectRecipe = -1;
                    }
                }
            }

        }
        

        // System.out.println(gp.keyH.enterPressed);
        // System.out.println(gp.keyH.keyPressed);
    }

    public void drawInventory(){
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
        
        for (int i = 0; i < Math.min(inventory.getInventory().size(), inventory.getInventoryMaxSize()); i++) {
            IItem item = inventory.getInventory().get(i);
            
            // Apply filter
            if (!inventory.getInventoryFilter().equals("all") && !item.getCategory().equals(inventory.getInventoryFilter())) {
                continue;
            }
            
            if (i == inventory.getSelectedSlot()) {
                g2.setColor(new Color(255, 255, 0, 128)); 
                g2.fillRect(slotX - 5, slotY - 5, gp.tileSize + 10, gp.tileSize + 10);
            }
            g2.setColor(Color.white);
            g2.drawRect(slotX, slotY, gp.tileSize, gp.tileSize); 
    
            // Draw item image
            if (item.getImage() != null) {
                g2.drawImage(item.getImage(), slotX + 5, slotY + 5, gp.tileSize - 10, gp.tileSize - 10, null);
            } else {
                // Draw placeholder if no image available
                g2.setColor(Color.gray);
                g2.fillRect(slotX + 5, slotY + 5, gp.tileSize - 10, gp.tileSize - 10);
            }
    
            // Draw category indicator
            g2.setFont(new Font("Arial", Font.ITALIC, 8));
            g2.setColor(getCategoryColor(item.getCategory()));
            g2.drawString(item.getCategory(), slotX, slotY + gp.tileSize + 22);
    
            itemCount++;
            slotX += gp.tileSize + 5;
            if (itemCount % 4 == 0) { 
                slotX = frameX + 10;
                slotY += gp.tileSize + 25;  
            }
        }
        
        // Draw selected item details
        if (!inventory.getInventory().isEmpty() && inventory.getSelectedSlot() < inventory.getInventory().size()) {
            drawItemDetails(g2, inventory.getInventory().get(inventory.getSelectedSlot()), frameX, frameY + frameHeight + 10);
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
            int textX = buttonX + (buttonWidth/2) - (g2.getFontMetrics().stringWidth(categories[i])/2);
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

        // Show energy value for consumables
        if (item instanceof IConsumable) {
            g2.drawString("Energy: +" + ((IConsumable)item).getEnergyRestoration(), x + 10, y + 80);
        }
        
        // Show additional attributes for fish
        if (item instanceof IFishAttributes) {
            IFishAttributes fishItem = (IFishAttributes) item;
            g2.setFont(new Font("Arial", Font.ITALIC, 10));
            g2.drawString("Season: " + fishItem.getSeason() + " | Weather: " + fishItem.getWeather(), x + 10, y + 80);
        }
        
        g2.setFont(new Font("Arial", Font.ITALIC, 11));
        g2.drawString("E: Use/Equip", x + 10, y + 100);
    }
    
    /**
     * Returns color associated with item category
     */
    private Color getCategoryColor(String category) {
        switch (category) {
            case "tools": return new Color(150, 150, 255);
            case "consumables": return new Color(255, 150, 150);
            case "crops": return new Color(150, 255, 150);
            case "fish": return new Color(150, 200, 255);
            case "seeds": return new Color(255, 255, 150);
            default: return Color.gray;
        }
    }
    

    private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
    
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new java.awt.BasicStroke(5));  // Border thickness
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawTimeHUD() {
        String season = gp.gameTime.getSeasonName();
        g2.setFont(arial_20);
        g2.setColor(Color.white);
        String time = String.format("Day %d - %02d:%02d", gp.currentDay, gp.currentHour, gp.currentMinute);
        String seasonText = "Season: " + season;
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        g2.drawString(time, x, y);
        g2.drawString(seasonText, x, y + 30);   
    }


}
