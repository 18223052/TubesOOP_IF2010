package object;

import main.GamePanel;

/**
 * Implementation for crop items
 */
public class CropItem extends BaseItem implements IConsumable {
    // energyValue yang tetap
    private final int energyValue = 3;

    public CropItem(String cropName, int buyPrice, int sellPrice, GamePanel gp) {
        super(cropName, buyPrice, sellPrice, gp, "crops");
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
