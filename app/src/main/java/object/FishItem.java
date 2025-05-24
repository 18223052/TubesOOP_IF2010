package object;

import environment.GameTime;
import environment.Season;
import environment.WeatherType;
import main.GamePanel;


public class FishItem extends BaseItem implements IFishAttributes, IConsumable {
    private Season season;
    private WeatherType weather;
    private GameTime time;
    private int rarity;
    private int energyValue;
    private FishCategory fishCategory;
    private int calculatedSellPrice;
    private static final int TOTAL_SEASON = 4;
    private static final int TOTAL_WEATHER_VARIATION = 2;
    private static final int TOTAL_LOCATION = 5;

    private static final int C_COMMON = 10;
    private static final int C_REGULAR = 5;
    private static final int C_LEGENDARY = 25;

    public enum FishCategory {
        COMMON,
        REGULAR,
        LEGENDARY
    }
    
    public FishItem(String fishType, int buyPrice,Season season, 
                    WeatherType weather, GameTime timeOfDay, int rarity, GamePanel gp, FishCategory fishCategory ) {
        super(fishType, buyPrice, 0, gp, "fish");
        this.season = season;
        this.weather = weather;
        this.time = timeOfDay;
        this.rarity = rarity;
        this.energyValue = 1;
        this.fishCategory = fishCategory;
    }
    
    @Override
    public void consume(GamePanel gp){
        System.out.println("Mengonsumsi " + getName() + " dan menambah " + energyValue + " energi.");
        gp.player.addEnergy(energyValue);
        gp.gameTime.addTime(5);
    }


    @Override
    public Season getSeason() {
        return season;
    }
    
    @Override
    public WeatherType getWeather() {
        return weather;
    }
    
    @Override
    public GameTime getTime() {
        return time;
    }
    
    @Override
    public FishCategory getFishCategory() {
        return fishCategory;
    }
    
    @Override
    public int getEnergyRestoration() {
        return energyValue;
    }
    
}
