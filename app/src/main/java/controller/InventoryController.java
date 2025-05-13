package controller;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.Ingredient;
import entity.Recipe;
import main.GamePanel;
import object.Item;

public class InventoryController {

    GamePanel gp;
    private ArrayList<Item> inventory;
    private int selectedSlot;
    private final int maxInventorySize = 20; 
    
    // Add category filtering
    private String currentFilter = "all"; // "all", "tools", "consumables", "crops", etc.

    public InventoryController(GamePanel gp) {
        this.gp = gp;
        this.inventory = new ArrayList<>();
        this.selectedSlot = 0;  
    }


    public void addItem(Item item) {
        if (inventory.size() < maxInventorySize) {

            if (item.stackable) {
                boolean itemExists = false;
                for (Item i : inventory) {
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
            Item removed = inventory.remove(index);
            System.out.println("Removed item: " + removed.getName() + " from inventory");

            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            }
        }
    }

    // Get the currently selected item
    public Item getSelectedItem() {
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
            Item item = inventory.get(index);
            
            if (item.category.equals("tools")) {

                System.out.println("Equipped: " + item.getName());
                gp.player.setActiveItem(item);
            } else if (item.category.equals("consumables")) {

                System.out.println("Consumed: " + item.getName());
                
                // Add energy or other effects here
                if (item.getName().contains("Energy")) {
                    // Example: Add 20 energy
                    // gp.player.addEnergy(20);
                }
                

                removeItem(index);
            } else if (item.category.equals("crops") || item.category.equals("fish")) {

                System.out.println("You can eat or sell: " + item.getName());
            } else {

                System.out.println("This item can be sold: " + item.getName());
            }
        }
    }
    

    public void sellItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            Item item = inventory.get(index);
            int goldGained = item.getSellPrice();
            
            // Add gold to player
            // gp.player.addGold(goldGained);
            System.out.println("Sold " + item.getName() + " for " + goldGained + " gold");

            removeItem(index);
        }
    }


    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void discardItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            Item removed = inventory.remove(index);
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

    public void draw(Graphics2D g2) {
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
    
      
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);
        
      
        drawCategoryFilters(g2, frameX, frameY - 30, frameWidth);
    

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.white);
        g2.drawString("Items: " + inventory.size() + "/" + maxInventorySize, frameX + 10, frameY + 20);
        

        int slotX = frameX + 10;
        int slotY = frameY + 35;
        int itemCount = 0;
        
        for (int i = 0; i < Math.min(inventory.size(), maxInventorySize); i++) {
            Item item = inventory.get(i);
            
            // Apply filter
            if (!currentFilter.equals("all") && !item.category.equals(currentFilter)) {
                continue;
            }
            
            if (i == selectedSlot) {
                g2.setColor(new Color(255, 255, 0, 128)); 
                g2.fillRect(slotX - 5, slotY - 5, gp.tileSize + 10, gp.tileSize + 10);
            }
            g2.setColor(Color.white);
            g2.drawRect(slotX, slotY, gp.tileSize, gp.tileSize); 
    
     
            if (item.img != null) {
                g2.drawImage(item.img, slotX + 5, slotY + 5, gp.tileSize - 10, gp.tileSize - 10, null);
            } else {
          
                g2.setColor(Color.gray);
                g2.fillRect(slotX + 5, slotY + 5, gp.tileSize - 10, gp.tileSize - 10);
            }
    

            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            g2.setColor(Color.white);
            g2.drawString(item.getName(), slotX, slotY + gp.tileSize + 12);
            
            // Draw category indicator
            g2.setFont(new Font("Arial", Font.ITALIC, 8));
            g2.setColor(getCategoryColor(item.category));
            g2.drawString(item.category, slotX, slotY + gp.tileSize + 22);
    
            itemCount++;
            slotX += gp.tileSize + 5;
            if (itemCount % 4 == 0) { 
                slotX = frameX + 10;
                slotY += gp.tileSize + 25;  
            }
        }
        

        if (!inventory.isEmpty() && selectedSlot < inventory.size()) {
            drawItemDetails(g2, inventory.get(selectedSlot), frameX, frameY + frameHeight + 10);
        }
    }
    
    private void drawCategoryFilters(Graphics2D g2, int x, int y, int width) {
        String[] categories = {"all", "tools", "consumables", "crops", "fish"};
        int buttonWidth = width / categories.length;
        
        for (int i = 0; i < categories.length; i++) {
            int buttonX = x + (i * buttonWidth);
            
            // Highlight active filter
            if (categories[i].equals(currentFilter)) {
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
    
    private void drawItemDetails(Graphics2D g2, Item item, int x, int y) {
        int detailWidth = gp.tileSize * 6;
        int detailHeight = gp.tileSize * 2;
        
        drawSubWindow(g2, x, y, detailWidth, detailHeight);
        
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.yellow);
        g2.drawString(item.getName(), x + 10, y + 20);
        
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.white);
        g2.drawString("Category: " + item.category, x + 10, y + 40);
        g2.drawString("Buy: " + item.getBuyPrice() + "g | Sell: " + item.getSellPrice() + "g", x + 10, y + 60);
        
        g2.setFont(new Font("Arial", Font.ITALIC, 11));
        g2.drawString("E: Use/Equip | Q: Discard | S: Sell", x + 10, y + 80);
    }
    
    private Color getCategoryColor(String category) {
        switch (category) {
            case "tools": return new Color(150, 150, 255);
            case "consumables": return new Color(255, 150, 150);
            case "crops": return new Color(150, 255, 150);
            case "fish": return new Color(150, 200, 255);
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
    
    // Getter for inventory
    public ArrayList<Item> getInventory() {
        return inventory;
    }


    // cooking utils
    public boolean hasIngredientsForRecipe(Recipe recipe){
        for (Ingredient i : recipe.ingredients){
            int reqAmount = i.amount;
            int availableAmount = getItemCount(i.name);

            if (i.name.toLowerCase().contains("any fish")) {
                availableAmount = getItemCountByCategory("fish");
            }
            
            if (availableAmount < reqAmount) {
                return false;
            }
        }
        return true;
    }

        public boolean consumeIngredientsForRecipe(Recipe recipe) {
        if (!hasIngredientsForRecipe(recipe)) {
            return false;
        }
        

        Map<String, Integer> toConsume = new HashMap<>();
        
        for (Ingredient ingredient : recipe.ingredients) {
            String ingredientName = ingredient.name;
            int amount = ingredient.amount;
            
            // kondisi ikan apa aja
            if (ingredientName.toLowerCase().contains("any fish")) {

                int consumed = 0;
                for (int i = inventory.size() - 1; i >= 0 && consumed < amount; i--) {
                    Item item = inventory.get(i);
                    if (item.category.equals("fish")) {
                        inventory.remove(i);
                        consumed++;
                        System.out.println("Consumed: " + item.getName() + " for recipe");
                    }
                }
            } else {
                
                toConsume.put(ingredientName, amount);
            }
        }
        

        for (Map.Entry<String, Integer> entry : toConsume.entrySet()) {
            String itemName = entry.getKey();
            int amountToConsume = entry.getValue();
            
            for (int i = inventory.size() - 1; i >= 0 && amountToConsume > 0; i--) {
                Item item = inventory.get(i);
                if (item.getName().equalsIgnoreCase(itemName)) {
                    inventory.remove(i);
                    amountToConsume--;
                    System.out.println("Consumed: " + item.getName() + " for recipe");
                }
            }
        }
        
        if (selectedSlot >= inventory.size() && inventory.size() > 0) {
            selectedSlot = inventory.size() - 1;
        }
        
        return true;
    }
    
    /**
     * masak dan langsung masukin ke inventory
     */
    public boolean cookRecipe(Recipe recipe, GamePanel gp) {
        if (!consumeIngredientsForRecipe(recipe)) {
            return false;
        }
        

        String recipeName = recipe.title.toLowerCase().replace(" ", "_");
        Item cookedItem = gp.itemFactory.createConsumable(recipeName);
        if (cookedItem == null) {

            cookedItem = new object.Item(recipe.title, 0, 50);
            cookedItem.category = "consumables";
        }
        
        addItem(cookedItem);
        System.out.println("Successfully cooked: " + recipe.title);
        return true;
    }
    
    // === UTILITY METHODS ===
    
    /**
     * Get the count of a specific item in inventory
     */
    public int getItemCount(String itemName) {
        int count = 0;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Get the total count of items in a specific category
     */
    public int getItemCountByCategory(String category) {
        int count = 0;
        for (Item item : inventory) {
            if (item.category.equals(category)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Get all items of a specific category
     */
    public ArrayList<Item> getItemsByCategory(String category) {
        ArrayList<Item> categoryItems = new ArrayList<>();
        for (Item item : inventory) {
            if (item.category.equals(category)) {
                categoryItems.add(item);
            }
        }
        return categoryItems;
    }
    
    /**
     * Check if player has a specific tool equipped
     */
    public boolean hasToolEquipped(String toolName) {
        Item activeItem = gp.player.getActiveItem();
        return activeItem != null && activeItem.getName().equalsIgnoreCase(toolName);
    }
}