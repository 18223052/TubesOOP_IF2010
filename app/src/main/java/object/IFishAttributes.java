package object;

import environment.GameTime;
import environment.Season;
import environment.WeatherType;
import object.FishItem.FishCategory;

public interface IFishAttributes {
    Season getSeason();
    WeatherType getWeather();
    GameTime getTime();
    FishCategory getFishCategory();
}
