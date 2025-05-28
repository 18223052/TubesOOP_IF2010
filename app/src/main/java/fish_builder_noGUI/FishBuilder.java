import java.util.List;

public interface FishBuilder {
    void reset();
    void setName(String name);
    void setSeasons(List<String> seasons);
    void setTimeRanges(List<String> timeRanges);
    void setWeathers(List<String> weathers);
    void setLocations(List<String> locations);
    void setType(Fish.FishType type);
    Fish getResult();
}
