package controller;

import java.util.ArrayList;
import java.util.Iterator;

import main.GamePanel;
import object.IConsumable;
import object.IItem;
import object.InventorySlot;
import object.NoItem;
import object.RecipeItem;
import object.SeedItem;
import object.ToolItem;
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

    private void removeItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            IItem removedItem = inventory.get(index).getItem();
            inventory.remove(index); // Ini menghapus seluruh slot
            System.out.println("Item " + removedItem.getName() + " dihapus dari inventory pada indeks: " + index + " (seluruh slot)");

            // Logika untuk active item jika item yang DIHAPUS (seluruh slot) adalah item aktif
            if (gp.player.getActiveItem() == removedItem) {
                 gp.player.setActiveItem(new NoItem(gp));
                 // Anda mungkin ingin memberi tahu pemain bahwa item dilepas
                 // gp.ui.setDialog(removedItem.getName() + " dilepas dari tangan.");
            }

            if (selectedSlot >= inventory.size() && !inventory.isEmpty()) {
                selectedSlot = inventory.size() - 1;
            } else if (inventory.isEmpty()) {
                selectedSlot = 0;
                gp.player.setActiveItem(new NoItem(gp)); // Pastikan NoItem aktif jika inventory kosong
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
            IItem itemInSlot = slot.getItem(); 
            if (itemInSlot.getName().equalsIgnoreCase(itemName)) {
                int available = slot.getQuantity();
                int toRemoveFromStack = Math.min(available, quantity - removedCount);
                slot.setQuantity(slot.getQuantity() - toRemoveFromStack);
                removedCount += toRemoveFromStack;
                System.out.println("Menghapus " + toRemoveFromStack + "x " + itemInSlot.getName() + " (counter: " + removedCount + "/" + quantity + ")");

                if (slot.isEmpty()) {
                    iterator.remove(); 
    
                    if (gp.player.getActiveItem() == itemInSlot) {
                        gp.player.setActiveItem(new NoItem(gp)); 
                        gp.ui.setDialog("Item unequipped.");
                    }
                }
            }
        }
        if (removedCount < quantity) {
            System.out.println("Warning: Tidak bisa menhapus " + quantity + " dari " + itemName + ". Only " + removedCount + " found.");
        }

        if (selectedSlot >= inventory.size() && inventory.size() > 0) {
            selectedSlot = inventory.size() - 1;
        } else if (inventory.isEmpty()) {
            selectedSlot = 0;
            gp.player.setActiveItem(new NoItem(gp));
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

                    if (gp.player.getActiveItem() == itemInSlot) {
                        gp.player.setActiveItem(new NoItem(gp)); 
                        gp.ui.setDialog("Item unequipped.");
                    }
                }
            }
        }
        if (removedCount < quantity) {
            System.out.println("Warning: Tidak bisa menghapus " + quantity + " items dari kategori " + category + ". Only " + removedCount + " found.");
        }

        if (selectedSlot >= inventory.size() && inventory.size() > 0) {
            selectedSlot = inventory.size() - 1;
        } else if (inventory.isEmpty()) {
            selectedSlot = 0;
            gp.player.setActiveItem(new NoItem(gp)); 
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

            if (item instanceof ToolItem) {
                if (gp.player.getActiveItem() == item) {
                    gp.player.setActiveItem(new NoItem(gp)); 
                    gp.ui.setDialog("Unequipped " + item.getName() + "."); 
                    System.out.println("Melepas equip: " + item.getName());
                } else {

                    System.out.println("Menggunakan: " + item.getName());
                    gp.player.setActiveItem(item); 
                    gp.ui.setDialog("Equipped " + item.getName() + "."); 
                }
            } else if (item instanceof RecipeItem){
                System.out.println("Mennggunakan resep: " + item.getName());
                RecipeItem recipeItem = (RecipeItem) item;

                recipeItem.use(gp.player,gp);

                if (gp.player.isRecipeUnlocked(recipeItem.getRecipeToUnlock()) && !recipeItem.getName().equals("Unknown Recipe")){
                    removeItem(index);
                }
            } else if (item instanceof IConsumable) {
                System.out.println("Mengonsumsi: " + item.getName());
                eatingController.consume((IConsumable) item);

                if (gp.player.getActiveItem() == item) {
                    gp.player.setActiveItem(new NoItem(gp));
                }
                removeItem(index); 
            } else if (item instanceof SeedItem){
                if (gp.player.getActiveItem() == item) {
                    gp.player.setActiveItem(new NoItem(gp)); 
                    gp.ui.setDialog("Unequipped " + item.getName() + "."); 
                    System.out.println("Melepas equip: " + item.getName());
                } else {
                System.out.println("Menggunakan: " + item.getName());
                    gp.player.setActiveItem(item); 
                    gp.ui.setDialog("Equipped " + item.getName() + "."); }
            }
            else {
                gp.ui.setDialog("You can't use " + item.getName() + " this way.");
                System.out.println("Item ini bisa dijual: " + item.getName());
            }
        }
    }

    public void sellItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            InventorySlot slot = inventory.get(index);
            if (slot == null || slot.getItem() == null || slot.isEmpty()) {
                System.out.println("Tidak ada item di slot yang dipilih untuk dijual atau slot kosong.");
                return;
            }
            IItem itemToSell = slot.getItem();


            boolean successfullyAddedToBin = gp.shippingBinController.addItem(itemToSell);

            if (successfullyAddedToBin) {
                System.out.println("Menempatkan 1x " + itemToSell.getName() + " di shipping bin.");

                boolean wasActiveItem = (gp.player.getActiveItem() == itemToSell);

                slot.decrementQuantity();

                if (slot.isEmpty()) { 
                    inventory.remove(index); 
                    System.out.println("Stack " + itemToSell.getName() + " habis, slot dihapus dari inventory.");
                    if (wasActiveItem) {
                        gp.player.setActiveItem(new NoItem(gp));
                        gp.ui.setDialog(itemToSell.getName() + " dijual (stack habis), item dilepas.");
                    }

                  
                    if (selectedSlot >= inventory.size() && !inventory.isEmpty()) {
                        selectedSlot = inventory.size() - 1;
                    } else if (inventory.isEmpty()) {
                        selectedSlot = 0;
               
                        if(gp.player.getActiveItem() != null && !(gp.player.getActiveItem() instanceof NoItem)){
                            gp.player.setActiveItem(new NoItem(gp));
                        }
                    }
                } else {

                    System.out.println("Sisa " + itemToSell.getName() + " di inventory: " + slot.getQuantity());
              
                }
                
  
                if (inventoryScreen != null) {
                    inventoryScreen.adjustScrollToSelectedItem();
                }

            } else {
                System.out.println("Gagal menjual " + itemToSell.getName() + ". Shipping bin mungkin penuh.");
                gp.ui.setDialog("Gagal menjual " + itemToSell.getName() + ". Shipping bin mungkin penuh.");

            }
        } else {
            System.out.println("Indeks inventory tidak valid untuk menjual item.");
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
                if (gp.player.getActiveItem() == discardedItem) {
                    gp.player.setActiveItem(new NoItem(gp));
                }
            }

            if (selectedSlot >= inventory.size() && inventory.size() > 0) {
                selectedSlot = inventory.size() - 1;
            } else if (inventory.isEmpty()) {
                selectedSlot = 0;
                gp.player.setActiveItem(new NoItem(gp));
            }
            if (inventoryScreen != null) {
                inventoryScreen.adjustScrollToSelectedItem();
            }
        }
    }

    public void update() {
    }

    public boolean hasToolEquipped(String toolName) {
        IItem activeItem = gp.player.getActiveItem();
        return activeItem != null && !(activeItem instanceof NoItem) && activeItem.getName().equalsIgnoreCase(toolName);
    }

    public void removeSpecificSlot(InventorySlot slotToRemove) {
        Iterator<InventorySlot> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            InventorySlot slot = iterator.next();
            if (slot == slotToRemove) { 
                iterator.remove();
                System.out.println("Slot inventory dihapus.");
                return;
            }
        }
    }


    public void removeItemInstance(IItem itemInstanceToRemove) {
        Iterator<InventorySlot> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            InventorySlot slot = iterator.next();
            if (slot.getItem() == itemInstanceToRemove) {
                iterator.remove();
                System.out.println("Instance item " + itemInstanceToRemove.getName() + " dihapus dari inventory.");
                return; 
            }
        }
        System.err.println("Warning: Attempted to remove item instance " + itemInstanceToRemove.getName() + " but it was not found.");
    }
}