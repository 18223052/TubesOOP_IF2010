// src/ui/panels/StoreMenu.java
package GUI.panels;

import main.GamePanel;
import object.IItem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class StoreMenu extends BaseUIPanel {

    // Constants for layout
    private final int FRAME_X;
    private final int FRAME_Y;
    private final int FRAME_WIDTH;
    private final int FRAME_HEIGHT;
    private final int SLOTS_PER_ROW = 4;
    private final int SLOT_SIZE;
    private final int SLOT_GAP_X = 5;
    private final int SLOT_GAP_Y = 25; 

    public StoreMenu(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
        FRAME_X = gp.tileSize;
        FRAME_Y = gp.tileSize;
        FRAME_WIDTH = gp.tileSize * 6;
        FRAME_HEIGHT = gp.tileSize * 5;
        SLOT_SIZE = gp.tileSize;
    }

    public void draw(Graphics2D g2) {
        // Draw title bar for Store
        drawSubWindow(g2, FRAME_X, FRAME_Y - 40, FRAME_WIDTH, 35);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.white);
        drawCenteredString(g2, "Store", FRAME_X, FRAME_Y - 17, FRAME_WIDTH);

        // Draw main Store window
        drawSubWindow(g2, FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.white);
        g2.drawString("Available Items : " + gp.storeController.storeItems().size() + "/" + gp.storeController.storeItemsMaxSize(), FRAME_X + 12, FRAME_Y + 22);
        g2.drawString("Your Gold : " + gp.player.getGold() + "g", (FRAME_X + FRAME_WIDTH) - g2.getFontMetrics().stringWidth(gp.player.getGold() + "g") - 10, FRAME_Y + 22);

        int itemCount = 0;
        for (int i = 0; i < gp.storeController.storeItems().size(); i++) {
            IItem item = gp.storeController.storeItems().get(i);
            int col = itemCount % SLOTS_PER_ROW;
            int row = itemCount / SLOTS_PER_ROW;

            int currentSlotX = FRAME_X + 10 + (col * (SLOT_SIZE + SLOT_GAP_X));
            int currentSlotY = FRAME_Y + 35 + (row * (SLOT_SIZE + SLOT_GAP_Y));

            if (i == gp.storeController.getSelectedSlot()) {
                if (item.getBuyPrice() <= gp.player.getGold()) {
                    g2.setColor(new Color(0, 213, 190, 128)); // Affordable highlight (teal)
                } else {
                    g2.setColor(new Color(255, 100, 103, 128)); // Not affordable highlight (reddish)
                }
                g2.fillRect(currentSlotX - 5, currentSlotY - 5, SLOT_SIZE + 10, SLOT_SIZE + 10);
            } else {
                // Background color even when not selected, to indicate affordability
                if (item.getBuyPrice() <= gp.player.getGold()) {
                    g2.setColor(new Color(0, 213, 190, 60)); // Lighter affordable (teal)
                } else {
                    g2.setColor(new Color(255, 100, 103, 60)); // Lighter not affordable (reddish)
                }
                g2.fillRect(currentSlotX, currentSlotY, SLOT_SIZE, SLOT_SIZE); // Fill entire slot
            }
            g2.setColor(Color.white); // Border color
            g2.drawRect(currentSlotX, currentSlotY, SLOT_SIZE, SLOT_SIZE);


            if (item.getImage() != null) {
                g2.drawImage(item.getImage(), currentSlotX + 5, currentSlotY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10, null);
            } else {
                g2.setColor(Color.gray);
                g2.fillRect(currentSlotX + 5, currentSlotY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10);
            }
            g2.setFont(new Font("Arial", Font.ITALIC, 8));
            g2.setColor(getCategoryColor(item.getCategory())); // Use utility from BaseUIPanel
            g2.drawString(item.getCategory(), currentSlotX, currentSlotY + SLOT_SIZE + 12); // Adjusted Y for text
            itemCount++;
        }

        // Removed the drawItemDetails call here to prevent duplicate item details when inventory is also drawn.
        // If Store needs its OWN item details panel, create a separate one for it, or draw it on the left.
        // For now, it's assumed the InventoryScreen's item details covers it.
    }
}