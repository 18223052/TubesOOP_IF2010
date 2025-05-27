package GUI.panels;

import main.GamePanel;
import object.IConsumable;
import object.IFishAttributes;
import object.IItem;
import object.InventorySlot; // Import the new InventorySlot class

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class InventoryScreen extends BaseUIPanel {

    // Inventory UI Constants
    private final int INV_FRAME_X;
    private final int INV_FRAME_Y;
    private final int INV_FRAME_WIDTH;
    private final int INV_FRAME_HEIGHT;
    private final int INV_SLOT_SIZE;
    private final int INV_SLOT_PADDING_X = 5;
    private final int INV_SLOT_PADDING_Y = 25;
    private final int INV_COLS = 4; // Moved to InventoryController

    // Item Details UI Constants
    private final int DETAIL_FRAME_WIDTH;
    private final int DETAIL_FRAME_HEIGHT;
    private final int DETAIL_PADDING_X = 10;
    private final int DETAIL_LINE_HEIGHT_SMALL = 12;
    private final int DETAIL_LINE_HEIGHT_MEDIUM = 15;
    private final int DETAIL_LINE_HEIGHT_LARGE = 18;


    public InventoryScreen(GamePanel gp, Font uiFont) {
        super(gp, uiFont);

        INV_FRAME_X = gp.tileSize * 9;
        INV_FRAME_Y = gp.tileSize;
        INV_FRAME_WIDTH = gp.tileSize * 6;
        INV_FRAME_HEIGHT = gp.tileSize * 6;
        INV_SLOT_SIZE = gp.tileSize;

        DETAIL_FRAME_WIDTH = gp.tileSize * 6;
        DETAIL_FRAME_HEIGHT = gp.tileSize * 25 / 10;
    }

    public void draw(Graphics2D g2, boolean isGifting) {
        // Draw main inventory frame
        drawSubWindow(g2, INV_FRAME_X, INV_FRAME_Y, INV_FRAME_WIDTH, INV_FRAME_HEIGHT);

        // Draw category filters
        drawCategoryFilters(g2, INV_FRAME_X, INV_FRAME_Y - 30, INV_FRAME_WIDTH);


        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.white);
        // Display actual number of distinct item stacks vs. max inventory size
        g2.drawString("Slots Used: " + gp.inventoryController.getInventorySlots().size() + "/" + gp.inventoryController.getInventoryMaxSize(),
                      INV_FRAME_X + 10, INV_FRAME_Y + 20);

        // Draw inventory slots and items
        drawInventorySlots(g2);

        // Draw item details if an item is selected
        // We now get the selected slot object, then its item
        InventorySlot selectedSlot = gp.inventoryController.getSelectedSlotItem();
        if (selectedSlot != null) {
            drawItemDetails(g2, selectedSlot.getItem(), selectedSlot.getQuantity(), INV_FRAME_X, INV_FRAME_Y + INV_FRAME_HEIGHT + 10, isGifting);
        }

        // Draw gifting instructions if in gifting mode
        if (isGifting) {
            drawGiftingInstructions(g2);
        }
    }

    private void drawInventorySlots(Graphics2D g2) {
        ArrayList<InventorySlot> filteredInventorySlots = gp.inventoryController.getFilteredInventorySlots();

        for (int i = 0; i < filteredInventorySlots.size(); i++) {
            InventorySlot slot = filteredInventorySlots.get(i);
            IItem item = slot.getItem(); // Get the IItem from the slot

            int col = i % INV_COLS;
            int row = i / INV_COLS;

            int currentSlotX = INV_FRAME_X + 10 + (col * (INV_SLOT_SIZE + INV_SLOT_PADDING_X));
            int currentSlotY = INV_FRAME_Y + 35 + (row * (INV_SLOT_SIZE + INV_SLOT_PADDING_Y));

            // Highlight selected slot based on the *filtered* inventory's index
            if (gp.inventoryController.getSelectedSlotItem() == slot) { // Compare the InventorySlot objects
                g2.setColor(new Color(255, 255, 0, 128));
                g2.fillRect(currentSlotX - 5, currentSlotY - 5, INV_SLOT_SIZE + 10, INV_SLOT_SIZE + 10);
            }

            g2.setColor(Color.white);
            g2.drawRect(currentSlotX, currentSlotY, INV_SLOT_SIZE, INV_SLOT_SIZE);

            if (item.getImage() != null) {
                g2.drawImage(item.getImage(), currentSlotX + 5, currentSlotY + 5, INV_SLOT_SIZE - 10, INV_SLOT_SIZE - 10, null);
            } else {
                g2.setColor(Color.gray);
                g2.fillRect(currentSlotX + 5, currentSlotY + 5, INV_SLOT_SIZE - 10, INV_SLOT_SIZE - 10);
            }

            // Draw quantity for stackable items
            if (item.isStackable() && slot.getQuantity() > 1) { // Get quantity from slot
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.setColor(Color.white);
                String quantityStr = String.valueOf(slot.getQuantity());
                int textWidth = g2.getFontMetrics().stringWidth(quantityStr);
                g2.drawString(quantityStr, currentSlotX + INV_SLOT_SIZE - textWidth - 5, currentSlotY + INV_SLOT_SIZE - 5);
            }

            // Draw category indicator below the item
            g2.setFont(new Font("Arial", Font.ITALIC, 8));
            g2.setColor(getCategoryColor(item.getCategory()));
            g2.drawString(item.getCategory(), currentSlotX, currentSlotY + INV_SLOT_SIZE + 12);
        }
    }


    private void drawCategoryFilters(Graphics2D g2, int x, int y, int width) {
        String[] categories = {"all", "tools", "consumables", "crops", "fish", "seeds", "fuel"};
        int buttonWidth = width / categories.length;

        for (int i = 0; i < categories.length; i++) {
            int buttonX = x + (i * buttonWidth);

            // Highlight active filter
            if (categories[i].equals(gp.inventoryController.getInventoryFilter())) {
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
    private void drawItemDetails(Graphics2D g2, IItem item, int quantity, int x, int y, boolean isGifting) {
        drawSubWindow(g2, x, y, DETAIL_FRAME_WIDTH, DETAIL_FRAME_HEIGHT);

        int currentY = y + DETAIL_PADDING_X + 10;

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.yellow);
        g2.drawString(item.getName(), x + DETAIL_PADDING_X, currentY);
        currentY += DETAIL_LINE_HEIGHT_SMALL;

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.white);
        g2.drawString("Category: " + item.getCategory(), x + DETAIL_PADDING_X, currentY);
        currentY += DETAIL_LINE_HEIGHT_SMALL;
        g2.drawString("Buy: " + item.getBuyPrice() + "g | Sell: " + item.getSellPrice() + "g", x + DETAIL_PADDING_X, currentY);
        currentY += DETAIL_LINE_HEIGHT_SMALL;

        // Display quantity in item details if stackable
        if (item.isStackable()) {
            g2.drawString("Quantity: " + quantity, x + DETAIL_PADDING_X, currentY);
            currentY += DETAIL_LINE_HEIGHT_SMALL;
        }

        if (item instanceof IConsumable) {
            g2.drawString("Energy: +" + ((IConsumable) item).getEnergyRestoration(), x + DETAIL_PADDING_X, currentY);
            currentY += DETAIL_LINE_HEIGHT_MEDIUM;
        }

        if (item instanceof IFishAttributes) {
            IFishAttributes fishItem = (IFishAttributes) item;
            g2.setFont(new Font("Arial", Font.ITALIC, 10));
            g2.drawString("Season: " + fishItem.getSeason() + " | Weather: " + fishItem.getWeather(), x + DETAIL_PADDING_X, currentY);
            currentY += DETAIL_LINE_HEIGHT_MEDIUM;
        }

        g2.setFont(new Font("Arial", Font.ITALIC, 11));
        if (isGifting) {
            g2.drawString("E: Gift to " + gp.currNPC.getName(), x + DETAIL_PADDING_X, currentY);
        } else {
            g2.drawString("E: Use/Equip", x + DETAIL_PADDING_X, currentY);
        }
        currentY += DETAIL_LINE_HEIGHT_LARGE;
        g2.drawString("ESC: Back", x + DETAIL_PADDING_X, currentY);
    }

    private void drawGiftingInstructions(Graphics2D g2) {
        int x = gp.tileSize;
        int y = gp.tileSize * 8;
        int width = gp.screenWidth / 2 - gp.tileSize;
        int height = gp.tileSize * 2;

        drawSubWindow(g2, x, y, width, height);

        g2.setFont(uiFont.deriveFont(20f));
        g2.setColor(Color.WHITE);

        int textX = x + 20;
        int textY = y + 30;
        int lineHeight = 30;

        g2.drawString("Gifting to " + gp.currNPC.getName(), textX, textY);
        textY += lineHeight;
        g2.drawString("Select an item to gift.", textX, textY);
        textY += lineHeight;
        g2.drawString("Press [E] to Gift", textX, textY);
        textY += lineHeight;
        g2.drawString("Press [ESC] to Cancel", textX, textY);
    }

    // // Helper method to get category color
    // private Color getCategoryColor(String category) {
    //     return switch (category.toLowerCase()) {
    //         case "tools" -> new Color(150, 200, 255);
    //         case "consumables" -> new Color(255, 150, 150);
    //         case "crops" -> new Color(150, 255, 150);
    //         case "fish" -> new Color(200, 150, 255);
    //         case "seeds" -> new Color(255, 200, 150);
    //         case "fuel" -> new Color(255, 255, 150);
    //         default -> Color.WHITE;
    //     };
    // }
}