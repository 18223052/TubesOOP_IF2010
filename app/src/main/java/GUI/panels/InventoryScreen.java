package GUI.panels;

import main.GamePanel;
import object.IConsumable;
import object.IFishAttributes;
import object.IItem;
import object.InventorySlot;

import java.awt.BasicStroke; // Needed for BasicStroke
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape; // Needed for Shape
import java.util.ArrayList;

public class InventoryScreen extends BaseUIPanel {

    // Inventory UI Constants
    private final int INV_FRAME_X;
    private final int INV_FRAME_Y;
    private final int INV_FRAME_WIDTH;
    private final int INV_FRAME_HEIGHT;
    private final int INV_SLOT_SIZE;
    private final int INV_SLOT_PADDING = 8;
    private final int INV_COLS = 4;

    // private final Color BACKGROUND_COLOR = new Color(25, 25, 35, 240);
    // private final Color FRAME_BORDER_COLOR = new Color(100, 120, 150, 200);
    // private final Color SLOT_BORDER_COLOR = new Color(80, 90, 110);
    // private final Color SLOT_BACKGROUND_COLOR = new Color(45, 50, 65);
    // private final Color SELECTED_SLOT_COLOR = new Color(255, 215, 0, 180);
    // private final Color HEADER_COLOR = new Color(220, 220, 240);
    // private final Color TEXT_PRIMARY = Color.WHITE;
    // private final Color TEXT_SECONDARY = new Color(200, 200, 220);

    // Item Details UI Constants
    private final int DETAIL_FRAME_WIDTH;
    private final int DETAIL_FRAME_HEIGHT;
    private final int DETAIL_PADDING = 15;
    private final int LINE_HEIGHT_SMALL = 18;
    private final int LINE_HEIGHT_MEDIUM = 22;
    private final int LINE_HEIGHT_LARGE = 26;

    private int scrollOffsetRow = 0; 
    private int visibleRows; 

    public InventoryScreen(GamePanel gp, Font uiFont) {
        super(gp, uiFont);

        INV_FRAME_X = gp.tileSize * 9;
        INV_FRAME_Y = gp.tileSize - 20; 
        INV_FRAME_WIDTH = gp.tileSize * 6;
        INV_FRAME_HEIGHT = gp.tileSize * 7;
        INV_SLOT_SIZE = (INV_FRAME_WIDTH - 40 - (INV_SLOT_PADDING * (INV_COLS - 1))) / INV_COLS;

        int availableHeightForSlots = INV_FRAME_HEIGHT - (55 + 10); 
        visibleRows = availableHeightForSlots / (INV_SLOT_SIZE + INV_SLOT_PADDING);

        if (availableHeightForSlots % (INV_SLOT_SIZE + INV_SLOT_PADDING) > (INV_SLOT_SIZE / 2)) { 
             visibleRows++;
        }


        DETAIL_FRAME_WIDTH = gp.tileSize * 6;
        DETAIL_FRAME_HEIGHT = gp.tileSize * 4;


        gp.inventoryController.setInventoryScreen(this);
    }

    public void draw(Graphics2D g2, boolean isGifting) {
       
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawSubWindow(g2, INV_FRAME_X, INV_FRAME_Y, INV_FRAME_WIDTH, INV_FRAME_HEIGHT);


        drawInventoryHeader(g2);


        drawEnhancedInventorySlots(g2);

        InventorySlot selectedSlot = gp.inventoryController.getSelectedSlotItem();
        if (selectedSlot != null) {
            drawEnhancedItemDetails(g2, selectedSlot.getItem(), selectedSlot.getQuantity(),
                    INV_FRAME_X, INV_FRAME_Y + INV_FRAME_HEIGHT + 15, isGifting);
        }


        if (isGifting) {
            drawEnhancedGiftingInstructions(g2);
        }
    }


    private void drawInventoryHeader(Graphics2D g2) {

        g2.setColor(new Color(60, 70, 90, 150));
        g2.fillRoundRect(INV_FRAME_X + 5, INV_FRAME_Y + 5, INV_FRAME_WIDTH - 10, 40, 8, 8);


        g2.setFont(uiFont.deriveFont(Font.BOLD, 16f));
        g2.setColor(HEADER_COLOR);
        g2.drawString("INVENTORY", INV_FRAME_X + 15, INV_FRAME_Y + 25);


        String slotText = gp.inventoryController.getInventorySlots().size() + "/" +
                gp.inventoryController.getInventoryMaxSize();
        g2.setFont(uiFont.deriveFont(Font.PLAIN, 12f));
        g2.setColor(TEXT_SECONDARY);
        int textWidth = g2.getFontMetrics().stringWidth(slotText);
        g2.drawString(slotText, INV_FRAME_X + INV_FRAME_WIDTH - textWidth - 15, INV_FRAME_Y + 25);

        // Decorative line
        g2.setColor(new Color(100, 120, 150, 100));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(INV_FRAME_X + 15, INV_FRAME_Y + 35, INV_FRAME_X + INV_FRAME_WIDTH - 15, INV_FRAME_Y + 35);
    }

    private void drawEnhancedInventorySlots(Graphics2D g2) {

        ArrayList<InventorySlot> allSlots = gp.inventoryController.getInventorySlots();
        int startY = INV_FRAME_Y + 55;


        int startIndex = scrollOffsetRow * INV_COLS;
        int endIndex = Math.min(allSlots.size(), startIndex + (visibleRows * INV_COLS));

        Shape oldClip = g2.getClip(); 
        g2.setClip(INV_FRAME_X + 10, startY, INV_FRAME_WIDTH - 20, INV_FRAME_HEIGHT - (startY - INV_FRAME_Y) - 10);


        for (int i = startIndex; i < endIndex; i++) {
            InventorySlot slot = allSlots.get(i);
            IItem item = slot.getItem();

            int col = (i - startIndex) % INV_COLS; 
            int row = (i - startIndex) / INV_COLS; 

            int slotX = INV_FRAME_X + 20 + (col * (INV_SLOT_SIZE + INV_SLOT_PADDING));
            int slotY = startY + (row * (INV_SLOT_SIZE + INV_SLOT_PADDING));


            boolean isSelected = gp.inventoryController.getSelectedSlotItem() == slot;
            drawItemSlot(g2, slotX, slotY, INV_SLOT_SIZE, isSelected); 

            // Draw item image
            if (item.getImage() != null) {
                int imageSize = INV_SLOT_SIZE - 16;
                int imageX = slotX + 8;
                int imageY = slotY + 8;

                // Item shadow
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(imageX + 2, imageY + 2, imageSize, imageSize, 4, 4);

                g2.drawImage(item.getImage(), imageX, imageY, imageSize, imageSize, null);
            }

    
            if (item.isStackable() && slot.getQuantity() > 1) {
                drawQuantityBadge(g2, slotX + INV_SLOT_SIZE - 20, slotY - 5, slot.getQuantity());
            }


        }
        g2.setClip(oldClip);
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
        if(item.getBuyPrice() != -1 && item.getSellPrice() != -1){
            g2.drawString("Beli: " + item.getBuyPrice() + "g  |  Jual: " + item.getSellPrice() + "g",
                    x + DETAIL_PADDING, currentY);
            currentY += LINE_HEIGHT_SMALL;
        }

    
        if (item.isStackable()) {
            g2.setColor(TEXT_PRIMARY);
            g2.drawString("Kuantitas: " + quantity, x + DETAIL_PADDING, currentY);
            currentY += LINE_HEIGHT_SMALL;
        }


        if (item instanceof IConsumable) {
            g2.setColor(new Color(100, 255, 100)); // Green for energy
            g2.drawString("Energi: +" + ((IConsumable) item).getEnergyRestoration(),
                    x + DETAIL_PADDING, currentY);
            currentY += LINE_HEIGHT_MEDIUM;
        }

        if (item instanceof IFishAttributes) {
            IFishAttributes fishItem = (IFishAttributes) item;
            g2.setFont(uiFont.deriveFont(Font.ITALIC, 11f)); 
            g2.setColor(new Color(150, 200, 255)); 
            g2.drawString("Musim: " + fishItem.getSeason() + "  Cuaca: " + fishItem.getWeather(),
                    x + DETAIL_PADDING, currentY);
            currentY += LINE_HEIGHT_MEDIUM;
        }

        currentY += 10; 
        g2.setFont(uiFont.deriveFont(Font.BOLD, 12f));
        if (isGifting) {
            g2.setColor(new Color(100, 255, 100)); 
            g2.drawString("[E] Berikan kepada " + gp.currNPC.getName(), x + DETAIL_PADDING, currentY);
        } else {
            if (!(item.getName().equalsIgnoreCase("ring")) && !(item.getCategory().equalsIgnoreCase("FUEL")) && !(item.getName().equalsIgnoreCase("Fathimah Nurhumaida (18223052)"))){
                g2.setColor(new Color(100, 255, 100)); 
                g2.drawString("[E] Gunakan/Lengkapi", x + DETAIL_PADDING, currentY);
            }
        }
        currentY += LINE_HEIGHT_LARGE;

        g2.setColor(new Color(255, 150, 150)); 
        g2.drawString("[ESC] Kembali", x + DETAIL_PADDING, currentY);
    }

    private void drawEnhancedGiftingInstructions(Graphics2D g2) {
        int x = gp.tileSize;
        int y = gp.tileSize * 8;
        int width = gp.screenWidth / 2 - gp.tileSize;
        int height = gp.tileSize * 2;

 
        drawSubWindow(g2, x, y, width, height);

        g2.setFont(uiFont.deriveFont(Font.BOLD, 18f));
        g2.setColor(SELECTED_SLOT_COLOR);

        int textX = x + 25;
        int textY = y + 35;
        int lineHeight = 25;

        g2.drawString("Memberi hadiah kepada " + gp.currNPC.getName(), textX, textY);
        textY += lineHeight;

        g2.setFont(uiFont.deriveFont(Font.PLAIN, 14f));
        g2.setColor(TEXT_PRIMARY);
        g2.drawString("Pilih item untuk diberikan", textX, textY);
        textY += lineHeight;

        g2.setColor(new Color(100, 255, 100)); 
        g2.drawString("[E] Konfirmasi Hadiah", textX, textY);
        textY += lineHeight;

        g2.setColor(new Color(255, 150, 150)); 
        g2.drawString("[ESC] Batalkan", textX, textY);
    }

    public void scrollDown() {
        int totalRows = (int) Math.ceil((double) gp.inventoryController.getInventorySlots().size() / INV_COLS);
        int maxScrollOffset = Math.max(0, totalRows - visibleRows);
        if (scrollOffsetRow < maxScrollOffset) {
            scrollOffsetRow++;
        }
    }

    public void scrollUp() {
        if (scrollOffsetRow > 0) {
            scrollOffsetRow--;
        }
    }


    public void adjustScrollToSelectedItem() {
        InventorySlot selectedSlotObj = gp.inventoryController.getSelectedSlotItem();
        if (selectedSlotObj == null) {
            scrollOffsetRow = 0; 
            return;
        }

        ArrayList<InventorySlot> allSlots = gp.inventoryController.getInventorySlots();
        int selectedIndex = allSlots.indexOf(selectedSlotObj);

        if (selectedIndex == -1) { 
            return;
        }

        int selectedRow = selectedIndex / INV_COLS;


        if (selectedRow < scrollOffsetRow) {
            scrollOffsetRow = selectedRow;
        }
   
        else if (selectedRow >= scrollOffsetRow + visibleRows) {
            scrollOffsetRow = selectedRow - visibleRows + 1;
        }


        if (scrollOffsetRow < 0) {
            scrollOffsetRow = 0;
        }
      
        int totalRows = (int) Math.ceil((double) allSlots.size() / INV_COLS);
        int maxScrollOffset = Math.max(0, totalRows - visibleRows);
        if (scrollOffsetRow > maxScrollOffset) {
            scrollOffsetRow = maxScrollOffset;
        }
    }
}