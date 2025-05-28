package GUI.panels;

import main.GamePanel;
import object.IItem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints; // Import for antialiasing

public class ShippingBinMenu extends BaseUIPanel {

    // Constants for layout (can be adjusted)
    private final int FRAME_X;
    private final int FRAME_Y;
    private final int FRAME_WIDTH;
    private final int FRAME_HEIGHT;
    private final int SLOTS_PER_ROW = 4;
    private final int SLOT_SIZE;
    private final int SLOT_GAP_X = 5;
    private final int SLOT_GAP_Y = 25;

    public ShippingBinMenu(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
        FRAME_X = gp.tileSize;
        FRAME_Y = gp.tileSize;
        FRAME_WIDTH = gp.tileSize * 6;
        FRAME_HEIGHT = gp.tileSize * 7;
        SLOT_SIZE = gp.tileSize;
    }

    public void draw(Graphics2D g2) {
        // Enable antialiasing for smoother graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw title bar for Shipping Bin
        // Use the enhanced drawSubWindow for the title bar too
        drawSubWindow(g2, FRAME_X, FRAME_Y - 40, FRAME_WIDTH, 35);
        g2.setFont(uiFont.deriveFont(Font.BOLD, 14)); // Use uiFont
        g2.setColor(HEADER_COLOR); // Use theme header color
        drawCenteredString(g2, "Kotak Pengiriman", FRAME_X, FRAME_Y - 17, FRAME_WIDTH); // Updated text

        // Draw main Shipping Bin window
        drawSubWindow(g2, FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
        g2.setFont(uiFont.deriveFont(Font.BOLD, 12)); // Use uiFont
        g2.setColor(TEXT_PRIMARY); // Use theme primary text color
        g2.drawString("Item Dijual : " + gp.shippingBinController.sellingItems().size() + "/" + gp.shippingBinController.sellingItemsMaxSize(), FRAME_X + 12, FRAME_Y + 22); // Updated text
        String totalPriceText = "Total Harga : " + gp.shippingBinController.goldEarned + "g"; // Updated text

        int totalPriceX = getXforAllignToRight(totalPriceText, FRAME_X + FRAME_WIDTH - 8, g2); // padding 12 dari kanan
        g2.drawString(totalPriceText, totalPriceX, FRAME_Y + 22);


        int itemCount = 0;
        for (int i = 0; i < gp.shippingBinController.sellingItems().size(); i++) {
            IItem item = gp.shippingBinController.sellingItems().get(i);
            int col = itemCount % SLOTS_PER_ROW;
            int row = itemCount / SLOTS_PER_ROW;

            int currentSlotX = FRAME_X + 10 + (col * (SLOT_SIZE + SLOT_GAP_X));
            int currentSlotY = FRAME_Y + 35 + (row * (SLOT_SIZE + SLOT_GAP_Y));

            // Use drawItemSlot from BaseUIPanel
            boolean isSelected = (i == gp.shippingBinController.getSelectedSlot());
            drawItemSlot(g2, currentSlotX, currentSlotY, SLOT_SIZE, isSelected);

            if (item.getImage() != null) {
                g2.drawImage(item.getImage(), currentSlotX + 5, currentSlotY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10, null);
            } else {
                g2.setColor(SLOT_BACKGROUND_COLOR); // Use theme slot background color
                g2.fillRect(currentSlotX + 5, currentSlotY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10);
            }

            // Removed category text below item slots as requested
            // g2.setFont(new Font("Arial", Font.ITALIC, 8));
            // g2.setColor(getCategoryColor(item.getCategory()));
            // g2.drawString(item.getCategory(), currentSlotX, currentSlotY + SLOT_SIZE + 12);

            itemCount++;
        }

        // Add instructions for interaction
        g2.setFont(uiFont.deriveFont(Font.BOLD, 12f));
        g2.setColor(new Color(100, 255, 100)); // Green for sell action
        g2.drawString("[E] Jual", FRAME_X + 15, FRAME_Y + FRAME_HEIGHT - 30); // Updated text

        g2.setColor(new Color(255, 150, 150)); // Red for back action
        g2.drawString("[ESC] Kembali", FRAME_X + 15, FRAME_Y + FRAME_HEIGHT - 15); // Updated text
    }
}