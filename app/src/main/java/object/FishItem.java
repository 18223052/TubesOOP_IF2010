package object;

import java.util.List;

import environment.GameTime;
import environment.Season;
import environment.WeatherType;
import environment.TimeRange;
import main.GamePanel;


public class FishItem extends BaseItem implements IFishAttributes, IConsumable {
    private Season[] availSeason;
    private WeatherType[] catchableWeather;
    private List<TimeRange> timeRanges;
    private FishLocation[] availLocation;
    private int energyValue;
    private FishCategory fishCategory;


    private static final int TOTAL_SEASON = 4;
    private static final double TOTAL_HOURS = 24.0;
    private static final int TOTAL_WEATHER_VARIATION = 2;
    private static final int TOTAL_LOCATION = 4;

    private static final int C_COMMON = 10;
    private static final int C_REGULAR = 5;
    private static final int C_LEGENDARY = 25;

    public enum FishCategory {
        COMMON,
        REGULAR,
        LEGENDARY
    }
    
    public FishItem(String fishType, int buyPrice,
                        Season[] seasons, WeatherType[] weathers,
                        List<TimeRange> timeRanges,
                        FishLocation[] locations, FishCategory fishCategory,
                        int energyValue, GamePanel gp) {
            super(fishType, buyPrice, 0, gp, "fish");
            this.availSeason = seasons;
            this.catchableWeather = weathers;
            this.timeRanges = timeRanges;
            this.availLocation = locations;
            this.fishCategory = fishCategory;
            this.energyValue = energyValue;
            this.setSellPrice(calculateSellPrice());
        }

    private int calculateSellPrice(){
        double numSeasons = (availSeason == null || availSeason.length ==0) ?
                            TOTAL_SEASON : availSeason.length;
        if (numSeasons == 0) numSeasons = TOTAL_SEASON;

        double totalDurationHours;
        if (this.timeRanges == null || this.timeRanges == null){
            totalDurationHours = TOTAL_HOURS;
        } else {
            totalDurationHours = 0;
            for (TimeRange range : this.timeRanges){
                totalDurationHours += range.getDurationHours();
            }
            if (totalDurationHours <= 0 || totalDurationHours > TOTAL_HOURS){
                totalDurationHours = TOTAL_HOURS;
            }
        }
        if (totalDurationHours == 0) totalDurationHours = TOTAL_HOURS;

        double numWeatherVariations = (catchableWeather == null ||catchableWeather.length == 0)?
                                    TOTAL_WEATHER_VARIATION : catchableWeather.length;
        if (numWeatherVariations == 0) numWeatherVariations = TOTAL_WEATHER_VARIATION;

        double numLocations = (availLocation == null || availLocation.length == 0)?
                            TOTAL_LOCATION : availLocation.length;
        if (numLocations == 0) numLocations = TOTAL_LOCATION;

        double cValue;
        switch (this.fishCategory){
            case COMMON:
                cValue = C_COMMON;
                break;
            case REGULAR:
                cValue = C_REGULAR;
                break;
            case LEGENDARY:
                cValue = C_LEGENDARY;
                break;
            default:
                cValue =-1;
                break;
        }

        double price = (TOTAL_SEASON / numSeasons) *
                       (TOTAL_HOURS / totalDurationHours) *
                       (TOTAL_WEATHER_VARIATION / numWeatherVariations) *
                       (TOTAL_LOCATION / numLocations) *
                       cValue;

        return (int) Math.round(price);
    }

    
    @Override
    public void consume(GamePanel gp){
        System.out.println("Mengonsumsi " + getName() + " dan menambah " + energyValue + " energi.");
        gp.player.addEnergy(energyValue);
        gp.gameTime.addTime(5);
    }


    @Override
    public Season[] getSeason() {
        return availSeason;
    }

    public Season getPrimarySeason() {
        return (availSeason != null && availSeason.length > 0) ? availSeason[0] : null;
    }
    
    @Override
    public WeatherType[] getWeather() {
        return catchableWeather;
    }

        
    public WeatherType getPrimaryWeather() {
        return (catchableWeather != null && catchableWeather.length > 0) ? catchableWeather[0] : null;
    }
    
    @Override
    public GameTime getTime() { 
        if (timeRanges != null && !timeRanges.isEmpty() && timeRanges.get(0) != null) {
            return timeRanges.get(0).start;
        }
        return null;
    }

    public List<TimeRange> getTimeRanges(){
        return timeRanges;
    }

    public FishLocation[] getAvailLocation(){
        return availLocation;
    }

    public FishLocation getPrimaryLocation() {
        return (availLocation != null && availLocation.length > 0) ? availLocation[0] : null;
    }
    
    @Override
    public FishCategory getFishCategory() { 
        return fishCategory;
    }
    
    @Override
    public int getEnergyRestoration() {
        return energyValue;
    }
    
    public boolean isCatchable(Season currSeason, WeatherType currWeatherType, GameTime currTime, FishLocation currLocation ){
        boolean seasonMatch = false;
        if (availSeason == null || availSeason.length == 0) seasonMatch = true;
        else for (Season s : availSeason) if (s == currSeason) { seasonMatch = true; break; }
        if (!seasonMatch) return false;

        boolean weatherMatch = false;
        if (catchableWeather == null || catchableWeather.length == 0) weatherMatch = true;
        else for (WeatherType w : catchableWeather) if (w == currWeatherType) { weatherMatch = true; break; }
        if (!weatherMatch) return false;

        boolean locationMatch = false;
        if (availLocation == null || availLocation.length == 0) locationMatch = true;
        else for (FishLocation l : availLocation) if (l == currLocation) { locationMatch = true; break; }
        if (!locationMatch) return false;


        boolean timeMatch = false;
        if (this.timeRanges == null || this.timeRanges.isEmpty()) {
            timeMatch = true;
        } else {
            // Use getGameHour() and getGameMinute() for currTime, catchStartTime, catchEndTime
            int currentTotalMinutes = currTime.getGameHour() * 60 + currTime.getGameMinute();

            for (TimeRange range : this.timeRanges){
                if (range.start == null || range.end == null){
                    timeMatch = true;
                    break;
                }
                int startTotalMinutes = range.start.getGameHour() * 60 + range.start.getGameMinute();
                int endTotalMinutes = range.end.getGameHour() * 60 + range.end.getGameMinute();

                if (endTotalMinutes < startTotalMinutes) {
                    if (currentTotalMinutes >= startTotalMinutes || currentTotalMinutes < endTotalMinutes) {
                        timeMatch = true;
                        break;
                    }
                } else {
                    if (currentTotalMinutes >= startTotalMinutes && currentTotalMinutes < endTotalMinutes) {
                        timeMatch = true;
                        break;
                    }
                }
            }
        }
        return timeMatch;
    }
}

