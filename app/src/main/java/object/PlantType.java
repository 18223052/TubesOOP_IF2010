package object;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import environment.Season;

public enum PlantType {
    NONE(Collections.emptySet(), 0, 0, 0), 
    TOMATO(EnumSet.of(Season.SPRING, Season.FALL), 1 * 60 * 1000L, 4320 * 60 * 1000L, 24 * 60 * 60 * 1000L), 
    POTATO(EnumSet.of(Season.SPRING), 4 * 60 * 1000, 8 * 60 * 1000, 24 * 60 * 60 * 1000), 
    PARSNIP(EnumSet.of(Season.SPRING), 3 * 60 * 1000, 6 * 60 * 1000, 24 * 60 * 60 * 1000),
    CAULIFLOWER(EnumSet.of(Season.SPRING), 7 * 60 * 1000, 14 * 60 * 1000, 24 * 60 * 60 * 1000),
    WHEAT(EnumSet.of(Season.SUMMER, Season.FALL), 2 * 60 * 1000, 4 * 60 * 1000, 24 * 60 * 60 * 1000),
    BLUEBERRY(EnumSet.of(Season.SUMMER), 8 * 60 * 1000, 16 * 60 * 1000, 24 * 60 * 60 * 1000),
    HOT_PEPPER(EnumSet.of(Season.SUMMER), 5 * 60 * 1000, 10 * 60 * 1000, 24 * 60 * 60 * 1000),
    MELON(EnumSet.of(Season.SUMMER), 9 * 60 * 1000, 18 * 60 * 1000, 24 * 60 * 60 * 1000),
    CRANBERRY(EnumSet.of(Season.FALL), 7 * 60 * 1000, 14 * 60 * 1000, 24 * 60 * 60 * 1000),
    PUMPKIN(EnumSet.of(Season.FALL), 9 * 60 * 1000, 18 * 60 * 1000, 24 * 60 * 60 * 1000),
    GRAPE(EnumSet.of(Season.FALL), 6 * 60 * 1000, 12 * 60 * 1000, 24 * 60 * 60 * 1000); 

    private final Set<Season> growthSeasons;
    private final long growthTimeStage1Millis; 
    private final long growthTimeStage2Millis; 
    private final long wiltingTimeMillis; 
    PlantType(Set<Season> growthSeasons, long growthTimeStage1Millis, long growthTimeStage2Millis, long wiltingTimeMillis) {
        this.growthSeasons = Collections.unmodifiableSet(growthSeasons);
        this.growthTimeStage1Millis = growthTimeStage1Millis;
        this.growthTimeStage2Millis = growthTimeStage2Millis;
        this.wiltingTimeMillis = wiltingTimeMillis;
    }

    public Set<Season> getGrowthSeasons() {
        return growthSeasons;
    }

    public boolean canGrowInSeason(Season currentSeason) {
        return growthSeasons.contains(currentSeason);
    }

    public long getGrowthTimeStage1Millis() {
        return growthTimeStage1Millis;
    }

    public long getGrowthTimeStage2Millis() {
        return growthTimeStage2Millis;
    }

    public long getWiltingTimeMillis() {
        return wiltingTimeMillis;
    }
}