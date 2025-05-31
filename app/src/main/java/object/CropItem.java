package object;

import main.GamePanel;


public class CropItem extends BaseItem implements IConsumable {
    private final int energyValue = 3;

    public CropItem(String cropName, int buyPrice, int sellPrice, GamePanel gp) {
        super(cropName, buyPrice, sellPrice, gp, "crops");
        sellable = true;
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
