package controller;

import java.util.ArrayList;

import GUI.panels.StoreMenu;
import main.GamePanel;
import object.IItem;

public class StoreController {

    private GamePanel gp;
    private ArrayList<IItem> storeItems;
    private int selectedSlot;
    private final int storeItemsSize = 50;
    public int goldSpent = 0;
    private StoreMenu storeMenu;

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
    if (!storeItems.isEmpty()) {
        int prevSelectedSlot = selectedSlot; 
        if (selectedSlot - 4 >= 0) {
            selectedSlot -= 4;
        }

        if (selectedSlot != prevSelectedSlot && storeMenu != null) {
            storeMenu.adjustScrollToSelectedItem();
        }
    }
}

public void moveSelectionDown() {
    if (!storeItems.isEmpty()) {
        int prevSelectedSlot = selectedSlot;
        if (selectedSlot + 4 < storeItems.size()) {
            selectedSlot += 4;
        }

        if (selectedSlot != prevSelectedSlot && storeMenu != null) {
            storeMenu.adjustScrollToSelectedItem();
        }
    }
}

public void moveSelectionLeft() {
    if (!storeItems.isEmpty()) {
        int prevSelectedSlot = selectedSlot;
        if (selectedSlot % 4 != 0) {
            selectedSlot--;
        }

        if (selectedSlot != prevSelectedSlot && storeMenu != null) {
            storeMenu.adjustScrollToSelectedItem();
        }
    }
}

public void moveSelectionRight() {
    if (!storeItems.isEmpty()) {
        int prevSelectedSlot = selectedSlot;
        // Pastikan tidak melampaui batas kanan kolom dan tidak melampaui jumlah item
        if ((selectedSlot + 1) % 4 != 0 && selectedSlot + 1 < storeItems.size()) {
            selectedSlot++;
        }

        if (selectedSlot != prevSelectedSlot && storeMenu != null) {
            storeMenu.adjustScrollToSelectedItem();
        }
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

    public void setStoreMenu(StoreMenu storeMenu) {
        this.storeMenu = storeMenu;
        // Setelah StoreMenu diset, pastikan scroll diawal disesuaikan
        if (!storeItems.isEmpty()) {
            storeMenu.adjustScrollToSelectedItem();
        }
    }
}
