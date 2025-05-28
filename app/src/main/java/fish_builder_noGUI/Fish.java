import java.util.List;

public class Fish {
    private String name;
    private List<String> seasons;
    private List<String> timeRanges;
    private List<String> weathers;
    private List<String> locations;
    private FishType type;

    public enum FishType {
        COMMON, REGULAR, LEGENDARY
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeasons(List<String> seasons) {
        this.seasons = seasons;
    }

    public void setTimeRanges(List<String> timeRanges) {
        this.timeRanges = timeRanges;
    }

    public void setWeathers(List<String> weathers) {
        this.weathers = weathers;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public void setType(FishType type) {
        this.type = type;
    }

    public int calculateSellPrice() {
        int seasonCount = seasons.contains("Any") ? 4 : seasons.size();
        int totalHours = timeRanges.stream().mapToInt(Fish::calculateHours).sum();
        int weatherCount = weathers.contains("Any") ? 2 : weathers.size();
        int locationCount = locations.size();
        int C = switch (type) {
            case COMMON -> 10;
            case REGULAR -> 5;
            case LEGENDARY -> 25;
        };
        return seasonCount * totalHours * weatherCount * locationCount * C;
    }

    private static int calculateHours(String range) {
        String[] parts = range.split("-");
        int start = Integer.parseInt(parts[0]);
        int end = Integer.parseInt(parts[1]);
        if (end < start) end += 2400;
        int startHour = start / 100;
        int startMin = start % 100;
        int endHour = end / 100;
        int endMin = end % 100;
        int startTotalMin = startHour * 60 + startMin;
        int endTotalMin = endHour * 60 + endMin;
        return (endTotalMin - startTotalMin) / 60;
    }

    //tambahan buat kalo mau cek
    public void describe() {
        System.out.println("Nama Ikan: " + name);
        System.out.println("Jenis: " + type);
        System.out.println("Season: " + seasons);
        System.out.println("Time: " + timeRanges);
        System.out.println("Weather: " + weathers);
        System.out.println("Lokasi: " + locations);
        System.out.println("Harga Jual: " + calculateSellPrice() + "g");
    }
}
