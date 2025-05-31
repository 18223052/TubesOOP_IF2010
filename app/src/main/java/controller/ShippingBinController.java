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

    public boolean addItem(IItem item) {
        if (sellingItems.size() < sellingItemsSize) {
            sellingItems.add(item);
            goldEarned = goldEarned + item.getSellPrice(); 
            System.out.println("Added item: " + item.getName() + " (" + getItemCount(item.getName()) + ") to sellingItems. Gold to be earned: " + item.getSellPrice());
            return true; 
        } else {
            System.out.println("Shipping bin is full! Cannot add " + item.getName() + ".");

            return false; 
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
        int newSlot = selectedSlot + 4;
        if (newSlot < sellingItemsSize && newSlot < sellingItems.size() + 4) { 
             if (newSlot < sellingItems.size()) {
                selectedSlot = newSlot;
             } else if (sellingItems.size() > (selectedSlot / 4) * 4 ) { 

                int potentialMaxSlot = Math.min(sellingItems.size() -1 + (4 - ( (sellingItems.size()-1) % 4)) %4, sellingItemsSize -1) ;
                if(selectedSlot + 4 <= potentialMaxSlot ) {
                     selectedSlot +=4;
                     if(selectedSlot >= sellingItems.size()) selectedSlot = sellingItems.size() -1; 
                     if(selectedSlot < 0) selectedSlot = 0; 
                }

             }
        } else if (selectedSlot + 4 < sellingItemsSize && sellingItems.size() > 0) {

            int col = selectedSlot % 4;
            int lastRowWithItem = (sellingItems.size() -1) / 4;
            int potentialSlot = lastRowWithItem * 4 + col;
            if (potentialSlot >= sellingItems.size()) { 
                selectedSlot = sellingItems.size() -1;
            } else {
                selectedSlot = potentialSlot;
            }
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
        } else if ((selectedSlot + 1) % 4 != 0 && selectedSlot + 1 < sellingItemsSize && (selectedSlot+1)/4 == selectedSlot/4) {

             if (selectedSlot + 1 < sellingItems.size()){ 
                 selectedSlot++;
             }
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

        if (goldEarned > 0) {
            gp.player.incrementGoldEarnedCount(goldEarned);
            gp.player.addGold(goldEarned);
            System.out.println("Player earned " + goldEarned + " gold from shipped items.");
            goldEarned = 0;
        }
        this.sellingItems = new ArrayList<>();
        this.selectedSlot = 0; 

    }

    public ArrayList<IItem> sellingItems() {
        return sellingItems;
    }
}
