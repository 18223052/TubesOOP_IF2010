package controller;

import java.util.ArrayList;
import java.util.Iterator;

import main.GamePanel;
import object.IConsumable;
import object.IItem;
import object.InventorySlot; 

public class InventoryController {

    private GamePanel gp;
    private ArrayList<InventorySlot> inventory; 
    private EatingController eatingController;
    private int selectedSlot;
    private final int maxInventorySize = 20;

    // Add category filtering
    private String currentFilter = "all"; // "all", "tools", "consumables", "crops", "fuel"
    private final int INV_COLS = 4; 

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

    // Modified addItem method to handle stacking using InventorySlot
    public void addItem(IItem newItem) {
        if (newItem.isStackable()) {
            boolean itemStacked = false;
            // Try to find an existing stack of the same item
            for (InventorySlot slot : inventory) {
                if (slot.getItem().getName().equalsIgnoreCase(newItem.getName()) && !slot.isFull()) {
                    slot.incrementQuantity(); // Increment quantity in the slot
                    System.out.println("Menambah stackable item: " + newItem.getName() + " ke inventory. Total: " + slot.getQuantity());
                    itemStacked = true;
                    break;
                }
            }
            if (!itemStacked) {
                // If no existing stack or existing stack is full, add a new slot if space is available
                if (inventory.size() < maxInventorySize) {
                    inventory.add(new InventorySlot(newItem, 1)); // Start a new stack with quantity 1
                    System.out.println("Menambah stackable item baru: " + newItem.getName() + " ke inventory");
                } else {
                    System.out.println("Inventory penuh, tidak bisa menambah stackable item baru!");
                }
            }
        } else {
            // Non-stackable items are added directly to a new slot if space is available
            if (inventory.size() < maxInventorySize) {
                inventory.add(new InventorySlot(newItem, 1)); // Non-stackable items always have quantity 1
                System.out.println("Menambah non-stackable item: " + newItem.getName() + " ke inventory");
            } else {
                System.out.println("Inventory penuh, tidak bisa menambah non-stackable item!");
            }
        }
        System.out.println("Current inventory slot count: " + inventory.size());
    }

    public void removeItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            if (slot.getItem().isStackable() && slot.getQuantity() > 1) {
                slot.decrementQuantity(); // Decrease quantity in the slot
                System.out.println("Mengurangi kuantitas item: " + slot.getItem().getName() + ". Sisa: " + slot.getQuantity());
            } else {
                InventorySlot removed = inventory.remove(index);
                System.out.println("Menghapus item: " + removed.getItem().getName() + " dari inventory");
            }

            // Adjust selected slot
            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            } else if (inventory.isEmpty()) {
                selectedSlot = 0;
            }
        }
    }

    public void removeItems(String itemName, int quantity) {
        int removedCount = 0;
        Iterator<InventorySlot> iterator = inventory.iterator();
        while (iterator.hasNext() && removedCount < quantity) {
            InventorySlot slot = iterator.next();
            if (slot.getItem().getName().equalsIgnoreCase(itemName)) {
                int available = slot.getQuantity();
                int toRemoveFromStack = Math.min(available, quantity - removedCount);
                slot.setQuantity(slot.getQuantity() - toRemoveFromStack);
                removedCount += toRemoveFromStack;
                System.out.println("Menghapus " + toRemoveFromStack + "x " + slot.getItem().getName() + " (counter: " + removedCount + "/" + quantity + ")");

                if (slot.isEmpty()) {
                    iterator.remove(); // Remove the slot if quantity drops to 0
                }
            }
        }
        if (removedCount < quantity) {
            System.out.println("Warning: Tidak bisa menhapus " + quantity + " dari " + itemName + ". Only " + removedCount + " found.");
        }
        // Adjust selected slot
        if (selectedSlot >= inventory.size() && inventory.size() > 0) {
            selectedSlot = inventory.size() - 1;
        } else if (inventory.isEmpty()) {
            selectedSlot = 0;
        }
    }

    public void removeCategoryItems(String category, int quantity) {
        int removedCount = 0;
        Iterator<InventorySlot> iterator = inventory.iterator();
        while (iterator.hasNext() && removedCount < quantity) {
            InventorySlot slot = iterator.next();
            if (slot.getItem().getCategory().equalsIgnoreCase(category)) {
                int available = slot.getQuantity();
                int toRemoveFromStack = Math.min(available, quantity - removedCount);
                slot.setQuantity(slot.getQuantity() - toRemoveFromStack);
                removedCount += toRemoveFromStack;
                System.out.println("Menghapus " + toRemoveFromStack + "x " + slot.getItem().getName() + " dari kategori " + category);

                if (slot.isEmpty()) {
                    iterator.remove();
                }
            }
        }
        if (removedCount < quantity) {
            System.out.println("Warning: Tidak bisa menghapus " + quantity + " items dari kategori " + category + ". Only " + removedCount + " found.");
        }
        // Adjust selected slot
        if (selectedSlot >= inventory.size() && inventory.size() > 0) {
            selectedSlot = inventory.size() - 1;
        } else if (inventory.isEmpty()) {
            selectedSlot = 0;
        }
    }

    /**
     * Mendapatkan jumlah item tertentu di inventaris (total across all stacks).
     */
    public int getItemCount(String itemName) {
        int count = 0;
        for (InventorySlot slot : inventory) {
            if (slot.getItem().getName().equalsIgnoreCase(itemName)) {
                count += slot.getQuantity();
            }
        }
        return count;
    }

    /**
     * Mendapatkan jumlah total item dalam kategori tertentu (total across all stacks).
     */
    public int getItemCountByCategory(String category) {
        int count = 0;
        for (InventorySlot slot : inventory) {
            if (slot.getItem().getCategory().equalsIgnoreCase(category)) {
                count += slot.getQuantity();
            }
        }
        return count;
    }

    public InventorySlot getSelectedSlotItem() { // Renamed to clarify it returns the slot
        if (inventory.isEmpty() || selectedSlot >= inventory.size()) {
            return null;
        }
        return inventory.get(selectedSlot);
    }

    // Helper to get the actual IItem from the selected slot
    public IItem getSelectedItem() {
        InventorySlot selectedSlotObj = getSelectedSlotItem();
        return (selectedSlotObj != null) ? selectedSlotObj.getItem() : null;
    }

    public void moveSelectionUp() {
        int targetSlot = selectedSlot - INV_COLS;
        if (targetSlot >= 0) {
            selectedSlot = targetSlot;
        } else {
            int lastRowStart = (inventory.size() - 1) / INV_COLS * INV_COLS;
            int offsetInRow = selectedSlot % INV_COLS;
            selectedSlot = Math.min(lastRowStart + offsetInRow, inventory.size() - 1);
            if (selectedSlot < 0) selectedSlot = 0;
        }
    }

    public void moveSelectionDown() {
        int targetSlot = selectedSlot + INV_COLS;
        if (targetSlot < inventory.size()) {
            selectedSlot = targetSlot;
        } else {
            int offsetInRow = selectedSlot % INV_COLS;
            selectedSlot = offsetInRow;
            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            } else if (inventory.isEmpty()) {
                selectedSlot = 0;
            }
        }
    }

    public void moveSelectionLeft() {
        if (selectedSlot > 0) {
            selectedSlot--;
        } else {
            selectedSlot = inventory.size() > 0 ? inventory.size() - 1 : 0;
        }
    }

    public void moveSelectionRight() {
        if (selectedSlot < inventory.size() - 1) {
            selectedSlot++;
        } else {
            selectedSlot = 0;
        }
    }

    public int getSelectedSlotIndex() { // Renamed to clarify it's the index
        return selectedSlot;
    }

    public void setSelectedSlot(int slot) {
        this.selectedSlot = slot;
    }

    public void setFilter(String category) {
        this.currentFilter = category;
        adjustSelectedSlotToFilter();
    }

    private void adjustSelectedSlotToFilter() {
        InventorySlot originalSelectedSlotObj = getSelectedSlotItem();
        selectedSlot = 0; // Reset to the first slot for the new filter
        ArrayList<InventorySlot> filteredInventory = getFilteredInventorySlots(); // Get filtered slots

        if (filteredInventory.isEmpty()) {
            selectedSlot = 0;
            return;
        }

        if (originalSelectedSlotObj != null && filteredInventory.contains(originalSelectedSlotObj)) {
            selectedSlot = filteredInventory.indexOf(originalSelectedSlotObj);
        } else {
            selectedSlot = 0;
        }
    }

    // New method to get the filtered inventory slots for display purposes
    public ArrayList<InventorySlot> getFilteredInventorySlots() {
        ArrayList<InventorySlot> filtered = new ArrayList<>();
        if (currentFilter.equals("all")) {
            return inventory;
        }
        for (InventorySlot slot : inventory) {
            if (slot.getItem().getCategory().equalsIgnoreCase(currentFilter)) {
                filtered.add(slot);
            }
        }
        return filtered;
    }

    public void useItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            IItem item = slot.getItem(); // Get the actual IItem from the slot

            if (item.getCategory().equals("tools")) {
                System.out.println("Menggunakan: " + item.getName());
                gp.player.setActiveItem(item); // Player still uses the IItem
            } else if (item instanceof IConsumable) {
                System.out.println("Mengonsumsi: " + item.getName());
                eatingController.consume((IConsumable) item);
                removeItem(index); // This will now correctly decrement or remove the slot
            } else {
                System.out.println("Item ini bisa dijual: " + item.getName());
            }
        }
    }

    public void sellItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            IItem item = slot.getItem(); // Get the actual IItem from the slot
            int goldGained = item.getSellPrice(); // Sell price is per unit
            gp.shippingBinController.addItem(item); // Shipping bin receives one item
            // Add gold to player
            // gp.player.addGold(goldGained);
            System.out.println("Menjual " + item.getName() + " untuk " + goldGained + " gold");

            removeItem(index); // This will now correctly decrement or remove
        }
    }

    public boolean hasItem(String itemName) {
        for (InventorySlot slot : inventory) {
            if (slot.getItem().getName().equals(itemName) && slot.getQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    public void discardItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            if (slot.getItem().isStackable() && slot.getQuantity() > 1) {
                slot.decrementQuantity();
                System.out.println("Discarded one " + slot.getItem().getName() + ". Remaining: " + slot.getQuantity());
            } else {
                InventorySlot removed = inventory.remove(index);
                System.out.println("Discarded item: " + removed.getItem().getName());
            }

            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            } else if (inventory.isEmpty()) {
                selectedSlot = 0;
            }
        }
    }

    public void update() {
        
    }

    public ArrayList<InventorySlot> getInventorySlots() {
        return inventory;
    }

    public boolean hasToolEquipped(String toolName) {
        IItem activeItem = gp.player.getActiveItem();
        return activeItem != null && activeItem.getName().equalsIgnoreCase(toolName);
    }
}