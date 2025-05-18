package object;

import main.GamePanel;


public class FishItem extends BaseItem implements IFishAttributes, IConsumable {
    private String season;
    private String weather;
    private String time;
    private int rarity;
    private int energyValue;
    
    public FishItem(String fishType, int buyPrice, int sellPrice, String season, 
                    String weather, String timeOfDay, int rarity, GamePanel gp) {
        super(fishType, buyPrice, sellPrice, gp, "fish");
        this.season = season;
        this.weather = weather;
        this.time = timeOfDay;
        this.rarity = rarity;
        this.energyValue = calculateEnergyValue();
    }
    
    private int calculateEnergyValue() {
        // Base energy + bonus for rarity
        return 10 + (rarity * 5);
    }
    
    @Override
    public String getSeason() {
        return season;
    }
    
    @Override
    public String getWeather() {
        return weather;
    }
    
    @Override
    public String getTime() {
        return time;
    }
    
    @Override
    public int getRarity() {
        return rarity;
    }
    
    @Override
    public int getEnergyRestoration() {
        return energyValue;
    }
    
    // @Override
    // public void consume(GamePanel gp) {
    //     System.out.println("Consumed " + getName() + " and restored " + energyValue + " energy");
    //     // Implement energy restoration logic
    //     // gp.player.addEnergy(energyValue);
    // }
}
