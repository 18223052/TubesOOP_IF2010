package controller;

import java.util.ArrayList;
import java.util.Iterator;

import main.GamePanel;
import object.IConsumable;
import object.IItem;

public class InventoryController {

    private GamePanel gp;
    private ArrayList<IItem> inventory;
    private EatingController eatingController;
    private int selectedSlot;
    private final int maxInventorySize = 20;

    // Add category filtering
    private String currentFilter = "all"; // "all", "tools", "consumables", "crops", etc.

    public InventoryController(GamePanel gp) {
        this.gp = gp;
        this.inventory = new ArrayList<>();
        this.selectedSlot = 0;
        this.eatingController = new EatingController(gp);
    }

    public String getInventoryFilter() {
        return currentFilter;
    }

    public int getInventoryMaxSize() {
        return maxInventorySize;
    }

    public void addItem(IItem item) {
        if (inventory.size() < maxInventorySize || item.isStackable()) {

            if (item.isStackable()) {
                boolean itemStacked = false;
                inventory.add(item);
                System.out.println("Added stackable item: " + item.getName() + " to inventory. Total: " + getItemCount(item.getName()));
                itemStacked = true;

                // boolean itemExists = false;
                // for (IItem i : inventory) {
                //     if (i.getName().equals(item.getName())) {
                //         itemExists = true;
                //         break;
                //     }
                // }
                // if (!itemExists) {
                //     inventory.add(item);
                // }
            } else {
                inventory.add(item);
                System.out.println("Added non-stackable item: " + item.getName() + " to inventory");
            }
            System.out.println("Added item: " + item.getName() + " to inventory");
        } else {
            System.out.println("Inventory is full!");
        }
    }

    public void removeItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem removed = inventory.remove(index);
            System.out.println("Removed item: " + removed.getName() + " from inventory");

            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            } else if (inventory.isEmpty()) {
                selectedSlot = 0;
            }
        }
    }

    public void removeItems(String itemName, int quantity) {
        int removedCount = 0;
        // pake iterator
        Iterator<IItem> iterator = inventory.iterator();
        while (iterator.hasNext() && removedCount < quantity) {
            IItem item = iterator.next();
            if (item.getName().equalsIgnoreCase(itemName)) {
                iterator.remove(); // Hapus item saat ini
                removedCount++;
                System.out.println("Removed " + item.getName() + " (count: " + removedCount + "/" + quantity + ")");
            }
        }
        if (removedCount < quantity) {
            System.out.println("Warning: Could not remove " + quantity + " of " + itemName + ". Only " + removedCount + " found.");
        }
        // ni kondisi kalo dah kehapus, selected slot nya harus dipastiin masi jalan
        if (selectedSlot >= inventory.size() && inventory.size() > 0) {
            selectedSlot = inventory.size() - 1;
        } else if (inventory.isEmpty()) {
            selectedSlot = 0;
        }
    }

    public void removeCategoryItems(String category, int quantity) {
        int removedCount = 0;
        Iterator<IItem> iterator = inventory.iterator();
        while (iterator.hasNext() && removedCount < quantity) {
            IItem item = iterator.next();
            if (item.getCategory().equalsIgnoreCase(category)) {
                iterator.remove();
                removedCount++;
                System.out.println("Removed " + item.getName() + " from category " + category);
            }
        }
        if (removedCount < quantity) {
            System.out.println("Warning: Could not remove " + quantity + " items from category " + category + ". Only " + removedCount + " found.");
        }
        // Update selected slot
        if (selectedSlot >= inventory.size() && inventory.size() > 0) {
            selectedSlot = inventory.size() - 1;
        } else if (inventory.isEmpty()) {
            selectedSlot = 0;
        }
    }

    /**
     * Mendapatkan jumlah item tertentu di inventaris.
     */
    public int getItemCount(String itemName) {
        int count = 0;
        for (IItem item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Mendapatkan jumlah total item dalam kategori tertentu. Berguna untuk "Any
     * Fish".
     */
    public int getItemCountByCategory(String category) {
        int count = 0;
        for (IItem item : inventory) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                count++;
            }
        }
        return count;
    }

    public IItem getSelectedItem() {
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

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setFilter(String category) {
        this.currentFilter = category;
    }

    public void useItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem item = inventory.get(index);

            if (item.getCategory().equals("tools")) {
                System.out.println("Equipped: " + item.getName());
                gp.player.setActiveItem(item);
            } else if (item instanceof IConsumable) {
                System.out.println("Consumed: " + item.getName());
                eatingController.consume((IConsumable) item);
                removeItem(index);
            } else {
                System.out.println("This item can be sold: " + item.getName());
            }
        }
    }

    public void sellItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem item = inventory.get(index);
            int goldGained = item.getSellPrice();
            gp.shippingBinController.addItem(item);
            // Add gold to player
            // gp.player.addGold(goldGained);
            System.out.println("Selling " + item.getName() + " for " + goldGained + " gold");

            removeItem(index);
        }
    }

    public boolean hasItem(String itemName) {
        for (IItem item : inventory) {
            if (item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void discardItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem removed = inventory.remove(index);
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

    public ArrayList<IItem> getInventory() {
        return inventory;
    }

    public boolean hasToolEquipped(String toolName) {
        IItem activeItem = gp.player.getActiveItem();
        return activeItem != null && activeItem.getName().equalsIgnoreCase(toolName);
    }
}
