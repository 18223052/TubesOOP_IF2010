import java.util.List;

public class FishConcreteBuilder implements FishBuilder {
    private Fish fish;

    public FishConcreteBuilder() {
        reset();
    }

    @Override
    public void reset() {
        fish = new Fish();
    }

    @Override
    public void setName(String name) {
        fish.setName(name);
    }

    @Override
    public void setSeasons(List<String> seasons) {
        fish.setSeasons(seasons);
    }

    @Override
    public void setTimeRanges(List<String> timeRanges) {
        fish.setTimeRanges(timeRanges);
    }

    @Override
    public void setWeathers(List<String> weathers) {
        fish.setWeathers(weathers);
    }

    @Override
    public void setLocations(List<String> locations) {
        fish.setLocations(locations);
    }

    @Override
    public void setType(Fish.FishType type) {
        fish.setType(type);
    }

    @Override
    public Fish getResult() {
        Fish result = fish;
        reset();
        return result;
    }
}
