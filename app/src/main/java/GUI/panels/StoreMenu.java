package GUI.panels;

import main.GamePanel;
import object.IConsumable;
import object.IFishAttributes;
import object.IItem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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

    // Item Details UI Constants (Copied from InventoryScreen)
    private final int DETAIL_FRAME_WIDTH;
    private final int DETAIL_FRAME_HEIGHT;
    private final int DETAIL_PADDING = 15;
    private final int LINE_HEIGHT_SMALL = 18;
    private final int LINE_HEIGHT_MEDIUM = 22;

    public StoreMenu(GamePanel gp, Font uiFont) {
        super(gp, uiFont);

        FRAME_X = gp.tileSize; 
        FRAME_Y = gp.tileSize + 40; 
        FRAME_WIDTH = gp.tileSize * 6;
        FRAME_HEIGHT = gp.tileSize * 5; 
        SLOT_SIZE = gp.tileSize;

  
        DETAIL_FRAME_WIDTH = gp.tileSize * 6;
        DETAIL_FRAME_HEIGHT = gp.tileSize * 4;
    }

    public void draw(Graphics2D g2) {

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

 
        drawSubWindow(g2, FRAME_X, FRAME_Y - 40, FRAME_WIDTH, 35);
        g2.setFont(uiFont.deriveFont(Font.BOLD, 16));
        g2.setColor(HEADER_COLOR);
        drawCenteredString(g2, "TOKO", FRAME_X, FRAME_Y - 17, FRAME_WIDTH);


        drawSubWindow(g2, FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
        g2.setFont(uiFont.deriveFont(Font.BOLD, 12));
        g2.setColor(TEXT_PRIMARY);
        g2.drawString("Item Tersedia : " + gp.storeController.storeItems().size() + "/" + gp.storeController.storeItemsMaxSize(), FRAME_X + 12, FRAME_Y + 22);
        String goldText = "Emas Anda : " + gp.player.getGold() + "g";
        g2.drawString(goldText, (FRAME_X + FRAME_WIDTH) - g2.getFontMetrics().stringWidth(goldText) - 10, FRAME_Y + 22);

        int itemCount = 0;
        for (int i = 0; i < gp.storeController.storeItems().size(); i++) {
            IItem item = gp.storeController.storeItems().get(i);
            int col = itemCount % SLOTS_PER_ROW;
            int row = itemCount / SLOTS_PER_ROW;

            int currentSlotX = FRAME_X + 10 + (col * (SLOT_SIZE + SLOT_GAP_X));
            int currentSlotY = FRAME_Y + 35 + (row * (SLOT_SIZE + SLOT_GAP_Y));

            boolean isSelected = (i == gp.storeController.getSelectedSlot());
            drawItemSlot(g2, currentSlotX, currentSlotY, SLOT_SIZE, isSelected);

            if (isSelected) {
                if (item.getBuyPrice() <= gp.player.getGold()) {
                    g2.setColor(new Color(0, 213, 190, 128));
                } else {
                    g2.setColor(new Color(255, 100, 103, 128)); 
                }
                g2.fillRect(currentSlotX - 3, currentSlotY - 3, SLOT_SIZE + 6, SLOT_SIZE + 6);
            } else {
                if (item.getBuyPrice() <= gp.player.getGold()) {
                    g2.setColor(new Color(0, 213, 190, 60)); 
                } else {
                    g2.setColor(new Color(255, 100, 103, 60)); 
                }
                g2.fillRect(currentSlotX, currentSlotY, SLOT_SIZE, SLOT_SIZE);
            }
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(currentSlotX, currentSlotY, SLOT_SIZE, SLOT_SIZE, 6, 6);

            if (item.getImage() != null) {
                g2.drawImage(item.getImage(), currentSlotX + 5, currentSlotY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10, null);
            } else {
                g2.setColor(SLOT_BACKGROUND_COLOR);
                g2.fillRect(currentSlotX + 5, currentSlotY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10);
            }
            itemCount++;
        }

        IItem selectedItem = gp.storeController.getSelectedItem();
        if (selectedItem != null) {
            drawEnhancedItemDetails(g2, selectedItem, 1, FRAME_X, FRAME_Y + FRAME_HEIGHT + 15, false); // Quantity always 1 for store
        }


        g2.setFont(uiFont.deriveFont(Font.BOLD, 12f));
        g2.setColor(new Color(100, 255, 100));
        g2.drawString("[E] Beli", FRAME_X + 15, FRAME_Y + FRAME_HEIGHT - 30);

        g2.setColor(new Color(255, 150, 150));
        g2.drawString("[ESC] Kembali", FRAME_X + 15, FRAME_Y + FRAME_HEIGHT - 15);
    }

    private void drawEnhancedItemDetails(Graphics2D g2, IItem item, int quantity, int x, int y, boolean isGifting) {
        drawSubWindow(g2, x, y, DETAIL_FRAME_WIDTH, DETAIL_FRAME_HEIGHT);

        int currentY = y + DETAIL_PADDING + 5;

        g2.setFont(uiFont.deriveFont(Font.BOLD, 18f));
        g2.setColor(SELECTED_SLOT_COLOR);
        g2.drawString(item.getName(), x + DETAIL_PADDING, currentY);
        currentY += LINE_HEIGHT_MEDIUM + 5;

        g2.setFont(uiFont.deriveFont(Font.PLAIN, 13f));
        g2.setColor(TEXT_SECONDARY);

        g2.setColor(getCategoryColor(item.getCategory()));
        g2.drawString("Kategori: " + item.getCategory().toUpperCase(), x + DETAIL_PADDING, currentY);
        currentY += LINE_HEIGHT_SMALL;

        g2.setColor(TEXT_SECONDARY);
        g2.drawString("Beli: " + item.getBuyPrice() + "g", x + DETAIL_PADDING, currentY); // Only show buy price in store
        currentY += LINE_HEIGHT_SMALL;

        if (item.isStackable()) {
            g2.setColor(TEXT_PRIMARY);
            g2.drawString("Kuantitas: " + quantity, x + DETAIL_PADDING, currentY);
            currentY += LINE_HEIGHT_SMALL;
        }

        if (item instanceof IConsumable) {
            g2.setColor(new Color(100, 255, 100));
            g2.drawString("Energi: +" + ((IConsumable) item).getEnergyRestoration(), x + DETAIL_PADDING, currentY);
            currentY += LINE_HEIGHT_MEDIUM;
        }

        if (item instanceof IFishAttributes) {
            IFishAttributes fishItem = (IFishAttributes) item;
            g2.setFont(uiFont.deriveFont(Font.ITALIC, 11f));
            g2.setColor(new Color(150, 200, 255));
            g2.drawString("Musim: " + fishItem.getSeason() + "   Cuaca: " + fishItem.getWeather(), x + DETAIL_PADDING, currentY);
            currentY += LINE_HEIGHT_MEDIUM;
        }

        currentY += 10;
        g2.setFont(uiFont.deriveFont(Font.BOLD, 12f));
        // Removed gifting specific instructions for store
    }
}