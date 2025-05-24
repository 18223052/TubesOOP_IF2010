package object;

import main.GamePanel;

/**
 * Implementation for food and consumable items
 */
public class FoodItem extends BaseItem implements IConsumable {
    private int energyValue;
    
    public FoodItem(String foodName, int buyPrice, int sellPrice, int energyValue, GamePanel gp) {
        super(foodName, buyPrice, sellPrice, gp, "consumables");
        this.energyValue = energyValue;
    }
    
    @Override
    public int getEnergyRestoration() {
        return energyValue;
    }
    
    @Override
    public void consume(GamePanel gp){
        System.out.println("Mengonsumsi " + getName() + " dan menambah " + energyValue + " energi.");
        gp.player.addEnergy(energyValue);
        gp.gameTime.addTime(5);
    }
}
