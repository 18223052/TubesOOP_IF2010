package controller;

import java.util.ArrayList;

import main.GamePanel;
import object.IItem;

public class StoreController {

    private GamePanel gp;
    private ArrayList<IItem> storeItems;
    private int selectedSlot;
    private final int storeItemsSize = 12;
    public int goldSpent = 0;

    public StoreController(GamePanel gp) {
        this.gp = gp;
        this.storeItems = new ArrayList<>();
        this.selectedSlot = 0;
    }

    public int storeItemsMaxSize() {
        return storeItemsSize;
    }

    public void addItem(IItem item) {
        storeItems.add(item);
        System.out.println("Added item: " + item.getName() + "(" + getItemCount(item.getName()) + ") to storeItems");
    }

    /**
     * Mendapatkan jumlah total item dalam kategori tertentu. Berguna untuk "Any
     * Fish".
     */
    public int getItemCount(String itemName) {
        int count = 0;
        for (IItem item : storeItems) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                count++;
            }
        }
        return count;
    }

    public IItem getSelectedItem() {
        if (storeItems.isEmpty()) {
            return null;
        }
        return storeItems.get(selectedSlot);
    }

    public void moveSelectionUp() {
        if (selectedSlot - 4 >= 0) {
            selectedSlot -= 4;
        }
    }

    public void moveSelectionDown() {
        if (selectedSlot + 4 < storeItems.size()) {
            selectedSlot += 4;
        }
    }

    public void moveSelectionLeft() {
        if (selectedSlot % 4 != 0) {
            selectedSlot--;
        }
    }

    public void moveSelectionRight() {
        if ((selectedSlot + 1) % 4 != 0 && selectedSlot + 1 < storeItems.size()) {
            selectedSlot++;
        }
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void sellItem(int index) {
        if (index >= 0 && index < storeItems.size()) {
            IItem item = storeItems.get(index);
            int itemPrice = item.getBuyPrice();

            if (gp.player.deductGold(itemPrice)){
                System.out.println("Player bought: " + item.getName() + " for " + itemPrice + " gold.");

                gp.player.inventory.addItem(item);

                goldSpent += itemPrice;
                selectedSlot = 0;
            }

            else {
                System.out.println("your money is not enough to buy " + item.getName());
            }
        }
    }

    public ArrayList<IItem> storeItems() {
        return storeItems;
    }
}
