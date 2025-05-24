package object;

import environment.GameTime;
import environment.Season;
import environment.WeatherType;
// import object.FishItem.FishCategory;

public interface IFishAttributes {
    String getSeason();
    String getWeather();
    String getTime();
    // FishCategory getFishCategory();
}
