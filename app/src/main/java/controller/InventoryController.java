package controller;

import java.util.ArrayList;
import java.util.Iterator;

import main.GamePanel;
import object.IConsumable;
import object.IItem;
import object.IUsable;
import object.InventorySlot;
import object.NoItem;
import GUI.panels.InventoryScreen;

public class InventoryController {

    private GamePanel gp;
    private ArrayList<InventorySlot> inventory;
    private EatingController eatingController;
    private int selectedSlot;
    private final int maxInventorySize = 20;

    private final int INV_COLS = 4;

    private InventoryScreen inventoryScreen;


    public InventoryController(GamePanel gp) {
        this.gp = gp;
        this.inventory = new ArrayList<>();
        this.selectedSlot = 0;
        this.eatingController = new EatingController(gp);
        if (gp.player != null && gp.player.getActiveItem() == null) {
            gp.player.setActiveItem(new NoItem(gp));
        }
    }

    public void setInventoryScreen(InventoryScreen inventoryScreen) {
        this.inventoryScreen = inventoryScreen;
    }

    public int getInventoryMaxSize() {
        return maxInventorySize;
    }

    public void addItem(IItem newItem) {
        if (newItem.isStackable()) {
            boolean itemStacked = false;
            for (InventorySlot slot : inventory) {
                if (slot.getItem().getName().equalsIgnoreCase(newItem.getName()) && !slot.isFull()) {
                    slot.incrementQuantity();
                    System.out.println("Menambah stackable item: " + newItem.getName() + " ke inventory. Total: " + slot.getQuantity());
                    itemStacked = true;
                    break;
                }
            }
            if (!itemStacked) {
                if (inventory.size() < maxInventorySize) {
                    inventory.add(new InventorySlot(newItem, 1));
                    System.out.println("Menambah stackable item baru: " + newItem.getName() + " ke inventory");
                } else {
                    System.out.println("Inventory penuh, tidak bisa menambah stackable item baru!");
                }
            }
        } else {
            if (inventory.size() < maxInventorySize) {
                inventory.add(new InventorySlot(newItem, 1));
                System.out.println("Menambah non-stackable item: " + newItem.getName() + " ke inventory");
            } else {
                System.out.println("Inventory penuh, tidak bisa menambah non-stackable item!");
            }
        }
        System.out.println("Current inventory slot count: " + inventory.size());
        if (inventoryScreen != null) {
            inventoryScreen.adjustScrollToSelectedItem();
        }
    }

    public void removeItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            IItem removedItem = slot.getItem(); // Keep track of the item being removed

            if (slot.getItem().isStackable() && slot.getQuantity() > 1) {
                slot.decrementQuantity();
                System.out.println("Mengurangi kuantitas item: " + slot.getItem().getName() + ". Sisa: " + slot.getQuantity());
            } else {
                inventory.remove(index);
                System.out.println("Menghapus item: " + removedItem.getName() + " dari inventory");
                // If the item being removed was the active item, set active item to NoItem
                if (gp.player.getActiveItem() == removedItem) {
                    gp.player.setActiveItem(new NoItem(gp)); // Set active item to NoItem
                    gp.ui.setDialog("Item unequipped.");
                }
            }

            // Adjust selected slot
            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            } else if (inventory.isEmpty()) {
                selectedSlot = 0; // If inventory is empty, reset selected slot to 0
                gp.player.setActiveItem(new NoItem(gp)); // If inventory becomes empty, ensure NoItem is active
            }
            if (inventoryScreen != null) {
                inventoryScreen.adjustScrollToSelectedItem();
            }
        }
    }

    public void removeItems(String itemName, int quantity) {
        int removedCount = 0;
        Iterator<InventorySlot> iterator = inventory.iterator();
        while (iterator.hasNext() && removedCount < quantity) {
            InventorySlot slot = iterator.next();
            IItem itemInSlot = slot.getItem(); // Get item from slot
            if (itemInSlot.getName().equalsIgnoreCase(itemName)) {
                int available = slot.getQuantity();
                int toRemoveFromStack = Math.min(available, quantity - removedCount);
                slot.setQuantity(slot.getQuantity() - toRemoveFromStack);
                removedCount += toRemoveFromStack;
                System.out.println("Menghapus " + toRemoveFromStack + "x " + itemInSlot.getName() + " (counter: " + removedCount + "/" + quantity + ")");

                if (slot.isEmpty()) {
                    iterator.remove(); // Remove the slot if quantity drops to 0
                    // If the item just removed was the active item, and it's completely gone:
                    if (gp.player.getActiveItem() == itemInSlot) {
                        gp.player.setActiveItem(new NoItem(gp)); // Set active item to NoItem
                        gp.ui.setDialog("Item unequipped.");
                    }
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
            gp.player.setActiveItem(new NoItem(gp)); // If inventory becomes empty, ensure NoItem is active
        }
        if (inventoryScreen != null) {
            inventoryScreen.adjustScrollToSelectedItem();
        }
    }

    public void removeCategoryItems(String category, int quantity) {
        int removedCount = 0;
        Iterator<InventorySlot> iterator = inventory.iterator();
        while (iterator.hasNext() && removedCount < quantity) {
            InventorySlot slot = iterator.next();
            IItem itemInSlot = slot.getItem();
            if (itemInSlot.getCategory().equalsIgnoreCase(category)) {
                int available = slot.getQuantity();
                int toRemoveFromStack = Math.min(available, quantity - removedCount);
                slot.setQuantity(slot.getQuantity() - toRemoveFromStack);
                removedCount += toRemoveFromStack;
                System.out.println("Menghapus " + toRemoveFromStack + "x " + itemInSlot.getName() + " dari kategori " + category);

                if (slot.isEmpty()) {
                    iterator.remove();
                    // If the item just removed was the active item, and it's completely gone:
                    if (gp.player.getActiveItem() == itemInSlot) {
                        gp.player.setActiveItem(new NoItem(gp)); // Set active item to NoItem
                        gp.ui.setDialog("Item unequipped.");
                    }
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
            gp.player.setActiveItem(new NoItem(gp)); // If inventory becomes empty, ensure NoItem is active
        }
        if (inventoryScreen != null) {
            inventoryScreen.adjustScrollToSelectedItem();
        }
    }

    public int getItemCount(String itemName) {
        int count = 0;
        for (InventorySlot slot : inventory) {
            if (slot.getItem().getName().equalsIgnoreCase(itemName)) {
                count += slot.getQuantity();
            }
        }
        return count;
    }

    public boolean hasItem(String itemName) {
        for (InventorySlot slot : inventory) {
            if (slot.getItem().getName().equals(itemName) && slot.getQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    public int getItemCountByCategory(String category) {
        int count = 0;
        for (InventorySlot slot : inventory) {
            if (slot.getItem().getCategory().equalsIgnoreCase(category)) {
                count += slot.getQuantity();
            }
        }
        return count;
    }

    public InventorySlot getSelectedSlotItem() {
        if (inventory.isEmpty() || selectedSlot < 0 || selectedSlot >= inventory.size()) {
            return null;
        }
        return inventory.get(selectedSlot);
    }

    public IItem getSelectedItem() {
        InventorySlot selectedSlotObj = getSelectedSlotItem();
        // If no item is selected or inventory is empty, return NoItem
        return (selectedSlotObj != null) ? selectedSlotObj.getItem() : new NoItem(gp);
    }

    public void moveSelectionUp() {
        if (inventory.isEmpty()) return;
        int targetSlot = selectedSlot - INV_COLS;
        if (targetSlot >= 0) {
            selectedSlot = targetSlot;
        } else {
            int lastRowStart = (inventory.size() - 1) / INV_COLS * INV_COLS;
            int offsetInRow = selectedSlot % INV_COLS;
            selectedSlot = Math.min(lastRowStart + offsetInRow, inventory.size() - 1);
        }
        if (inventoryScreen != null) {
            inventoryScreen.adjustScrollToSelectedItem();
        }
    }

    public void moveSelectionDown() {
        if (inventory.isEmpty()) return;
        int targetSlot = selectedSlot + INV_COLS;
        if (targetSlot < inventory.size()) {
            selectedSlot = targetSlot;
        } else {
            int offsetInRow = selectedSlot % INV_COLS;
            selectedSlot = Math.min(offsetInRow, inventory.size() - 1);
        }
        if (inventoryScreen != null) {
            inventoryScreen.adjustScrollToSelectedItem();
        }
    }

    public void moveSelectionLeft() {
        if (inventory.isEmpty()) return;
        if (selectedSlot > 0) {
            selectedSlot--;
        } else {
            selectedSlot = inventory.size() - 1;
        }
        if (inventoryScreen != null) {
            inventoryScreen.adjustScrollToSelectedItem();
        }
    }

    public void moveSelectionRight() {
        if (inventory.isEmpty()) return;
        if (selectedSlot < inventory.size() - 1) {
            selectedSlot++;
        } else {
            selectedSlot = 0;
        }
        if (inventoryScreen != null) {
            inventoryScreen.adjustScrollToSelectedItem();
        }
    }

    public int getSelectedSlotIndex() {
        return selectedSlot;
    }

    public void setSelectedSlot(int slot) {
        this.selectedSlot = slot;
        if (inventoryScreen != null) {
            inventoryScreen.adjustScrollToSelectedItem();
        }
    }

    public ArrayList<InventorySlot> getFilteredInventorySlots() {
        return inventory;
    }

    public ArrayList<InventorySlot> getInventorySlots() {
        return inventory;
    }

    public void useItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            IItem item = slot.getItem();

            if (item instanceof IUsable) {
                System.out.println("Menggunakan: " + item.getName());
                gp.player.setActiveItem(item); // Set the player's active item
                gp.ui.setDialog("Equipped " + item.getName() + "."); // Inform player
            } else if (item instanceof IConsumable) {
                System.out.println("Mengonsumsi: " + item.getName());
                eatingController.consume((IConsumable) item);
                // After consumption, if this item was the active item, set to NoItem
                if (gp.player.getActiveItem() == item) {
                    gp.player.setActiveItem(new NoItem(gp));
                }
                removeItem(index); // Remove after consumption
            } else {
                gp.ui.setDialog("You can't use " + item.getName() + " this way.");
                System.out.println("Item ini bisa dijual: " + item.getName());
            }
        }
    }

    public void sellItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            IItem item = slot.getItem();
            int goldGained = item.getSellPrice();
            gp.shippingBinController.addItem(item);
            System.out.println("Menjual " + item.getName() + " untuk " + goldGained + " gold");

            // If the item being sold was the active item, set active item to NoItem
            if (gp.player.getActiveItem() == item) {
                gp.player.setActiveItem(new NoItem(gp));
            }
            removeItem(index); // Remove after selling
        }
    }

    public void discardItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            IItem discardedItem = slot.getItem();

            if (slot.getItem().isStackable() && slot.getQuantity() > 1) {
                slot.decrementQuantity();
                System.out.println("Discarded one " + slot.getItem().getName() + ". Remaining: " + slot.getQuantity());
            } else {
                inventory.remove(index);
                System.out.println("Discarded item: " + discardedItem.getName());
                // If the item discarded was the active item, set active item to NoItem
                if (gp.player.getActiveItem() == discardedItem) {
                    gp.player.setActiveItem(new NoItem(gp));
                }
            }

            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            } else if (inventory.isEmpty()) {
                selectedSlot = 0;
                gp.player.setActiveItem(new NoItem(gp)); // If inventory becomes empty, ensure NoItem is active
            }
            if (inventoryScreen != null) {
                inventoryScreen.adjustScrollToSelectedItem();
            }
        }
    }

    public void update() {
        // No specific update logic needed in InventoryController itself,
        // as it's primarily event-driven.
    }

    public boolean hasToolEquipped(String toolName) {
        IItem activeItem = gp.player.getActiveItem();
        // Check if the active item is not NoItem AND its name matches the toolName
        return activeItem != null && !(activeItem instanceof NoItem) && activeItem.getName().equalsIgnoreCase(toolName);
    }
}