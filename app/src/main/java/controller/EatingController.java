package controller;

import main.GamePanel;
import object.IConsumable;
import object.InventorySlot; 


/**
 * Kelas EatingController merupakan kelas
 * yang mengontrol cara player memakan makanan dari inventory
 * 
 */

public class EatingController {

    private GamePanel gp;

    public EatingController(GamePanel gp) {
        this.gp = gp;
    }

    public void consume(InventorySlot inventorySlot) {
        if (inventorySlot == null || !(inventorySlot.getItem() instanceof IConsumable)) {
            System.out.println("Invalid item for consumption or not a consumable.");
            return;
        }

        IConsumable consumableItem = (IConsumable) inventorySlot.getItem();
        String itemName = inventorySlot.getItem().getName();

        System.out.println("Mulai proses konsumsi " + itemName + "...");


        consumableItem.consume(gp);

        System.out.println("Consumed item: " + itemName +
                           " and restored " + consumableItem.getEnergyRestoration() + " energy.");

        gp.inventoryController.removeItems(itemName, 1);

    }
}