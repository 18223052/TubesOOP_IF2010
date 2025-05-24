package controller;

import java.util.ArrayList;

import main.GamePanel;
import object.IItem;

public class ShippingBinController {

    private GamePanel gp;
    private ArrayList<IItem> sellingItems;
    private int selectedSlot;
    private final int sellingItemsSize = 16;
    public int goldEarned = 0;

    public ShippingBinController(GamePanel gp) {
        this.gp = gp;
        this.sellingItems = new ArrayList<>();
        this.selectedSlot = 0;
    }

    public int sellingItemsMaxSize() {
        return sellingItemsSize;
    }

    public void addItem(IItem item) {
        if (sellingItems.size() < sellingItemsSize) {
            sellingItems.add(item);
            goldEarned = goldEarned + item.getSellPrice();
            System.out.println("Added item: " + item.getName() + "(" + getItemCount(item.getName()) + ") to sellingItems");
        } else {
            System.out.println("Item already exists in sellingItems!");
        }
    }

    /**
     * Mendapatkan jumlah total item dalam kategori tertentu. Berguna untuk "Any
     * Fish".
     */
    public int getItemCount(String itemName) {
        int count = 0;
        for (IItem item : sellingItems) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                count++;
            }
        }
        return count;
    }

    public IItem getSelectedItem() {
        if (sellingItems.isEmpty()) {
            return null;
        }
        return sellingItems.get(selectedSlot);
    }

    public void moveSelectionUp() {
        if (selectedSlot - 4 >= 0) {
            selectedSlot -= 4;
        }
    }

    public void moveSelectionDown() {
        if (selectedSlot + 4 < sellingItems.size()) {
            selectedSlot += 4;
        }
    }

    public void moveSelectionLeft() {
        if (selectedSlot % 4 != 0) {
            selectedSlot--;
        }
    }

    public void moveSelectionRight() {
        if ((selectedSlot + 1) % 4 != 0 && selectedSlot + 1 < sellingItems.size()) {
            selectedSlot++;
        }
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public boolean hasItem(String itemName) {
        for (IItem item : sellingItems) {
            if (item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

 
    public void update() {
        this.sellingItems = new ArrayList<>();
        this.selectedSlot = 0;
        gp.player.addGold(goldEarned);
        goldEarned = 0;

    }

    public ArrayList<IItem> sellingItems() {
        return sellingItems;
    }
}
