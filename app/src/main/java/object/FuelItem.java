package object;

import main.GamePanel;

public abstract class FuelItem extends BaseItem {
    public int currentFuelValue;
    public int maxFuelValue;

    public FuelItem(String name, int buyPrice, int sellPrice, GamePanel gp, int initialFuelValue) {
        super(name, buyPrice, sellPrice, gp, "fuel"); 
        this.currentFuelValue = initialFuelValue;
        this.maxFuelValue = initialFuelValue;
        setStackable(false);
    }

    public int getCurrentFuelValue() {
        return currentFuelValue;
    }

    public void deductFuel(int amount) {
        this.currentFuelValue = Math.max(0, this.currentFuelValue - amount);
    }

    public boolean isFuelEmpty() {
        return currentFuelValue <= 0;
    }

    public int getMaxFuelValue() {
        return maxFuelValue;
    }
}

