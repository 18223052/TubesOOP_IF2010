package environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WeatherManager {
    private final int daysPerSeason = 10;
    private WeatherType[] weatherPerDay;
    private Random rand = new Random();

    public WeatherManager() {
        generateWeatherForSeason();
    }

    public void generateWeatherForSeason() {
        weatherPerDay = new WeatherType[daysPerSeason];
        ArrayList<Integer> rainyDays = new ArrayList<>();

        // Pilih minimal 2 hari secara acak untuk hujan
        while (rainyDays.size() < 2) {
            int day = rand.nextInt(daysPerSeason);
            if (!rainyDays.contains(day)) {
                rainyDays.add(day);
            }
        }

        // Isi array berdasarkan hasil random
        for (int i = 0; i < daysPerSeason; i++) {
            weatherPerDay[i] = rainyDays.contains(i) || rand.nextDouble() < 0.2 ? WeatherType.RAINY : WeatherType.SUNNY;
        }
    }

    // In WeatherManager.java
    public WeatherType getWeatherForDay(int dayInSeason) { // dayInSeason should be 1-10
        if (dayInSeason < 1 || dayInSeason > daysPerSeason) {
            System.err.println("WeatherManager: Invalid dayInSeason requested: " + dayInSeason + ". Defaulting to SUNNY.");
            return WeatherType.SUNNY; // Default fallback
        }
        return weatherPerDay[dayInSeason - 1]; // Access array with 0-9 index
    }

}
