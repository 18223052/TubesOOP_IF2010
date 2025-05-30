package object;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import environment.Season;

public enum PlantType {
    NONE(Collections.emptySet(), 0, 0, 0), 
    TOMATO(EnumSet.of(Season.SPRING, Season.FALL), 3 * 1440 / 2 * 60 * 1000L, 3 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L), 
    POTATO(EnumSet.of(Season.SPRING), 3 * 1440 / 2 * 60 * 1000L, 3 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L), 
    PARSNIP(EnumSet.of(Season.SPRING), 1 * 1440 / 2 * 60 * 1000L, 1 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L),
    CAULIFLOWER(EnumSet.of(Season.SPRING), 5 * 1440 / 2 * 60 * 1000L, 5 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L),
    WHEAT(EnumSet.of(Season.SUMMER, Season.FALL), 1 * 1440 / 2 * 60 * 1000L, 1 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L),
    BLUEBERRY(EnumSet.of(Season.SUMMER), 7 * 1440 / 2 * 60 * 1000L, 7 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L),
    HOT_PEPPER(EnumSet.of(Season.SUMMER), 1 * 1440 / 2 * 60 * 1000L, 1 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L),
    MELON(EnumSet.of(Season.SUMMER), 4 * 1440 / 2 * 60 * 1000L, 4 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L),
    CRANBERRY(EnumSet.of(Season.FALL), 2 * 1440 / 2 * 60 * 1000L, 2 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L),
    PUMPKIN(EnumSet.of(Season.FALL), 7 * 1440 / 2 * 60 * 1000L, 7 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L),
    GRAPE(EnumSet.of(Season.FALL), 3 * 1440 / 2 * 60 * 1000L, 3 * 1440 * 60 * 1000L, 24 * 60 * 60 * 1000L); 

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