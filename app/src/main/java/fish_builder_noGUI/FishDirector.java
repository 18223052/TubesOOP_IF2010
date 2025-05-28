import java.util.List;

//isi: semua jenis ikan yang bisa dibuat
public class FishDirector {
    public void constructBullhead(FishBuilder builder) {
        builder.reset();
        builder.setName("Bullhead");
        builder.setSeasons(List.of("Any"));
        builder.setTimeRanges(List.of("0000-2400"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Mountain Lake"));
        builder.setType(Fish.FishType.COMMON);
    }

    public void constructCarp(FishBuilder builder) {
        builder.reset();
        builder.setName("Carp");
        builder.setSeasons(List.of("Any"));
        builder.setTimeRanges(List.of("0000-2400"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Mountain Lake", "Pond"));
        builder.setType(Fish.FishType.COMMON);
    }

    public void constructChub(FishBuilder builder) {
        builder.reset();
        builder.setName("Chub");
        builder.setSeasons(List.of("Any"));
        builder.setTimeRanges(List.of("0000-2400"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Forest River", "Mountain Lake"));
        builder.setType(Fish.FishType.COMMON);
    }

    public void constructHalibut(FishBuilder builder) {
        builder.reset();
        builder.setName("Halibut");
        builder.setSeasons(List.of("Any"));
        builder.setTimeRanges(List.of("0600-1100", "1900-0200"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Ocean"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructLegend(FishBuilder builder) {
        builder.reset();
        builder.setName("Legend");
        builder.setSeasons(List.of("Spring"));
        builder.setTimeRanges(List.of("0800-2000"));
        builder.setWeathers(List.of("Rainy"));
        builder.setLocations(List.of("Mountain Lake"));
        builder.setType(Fish.FishType.LEGENDARY);
    }

    public void constructAngler(FishBuilder builder) {
        builder.reset();
        builder.setName("Angler");
        builder.setSeasons(List.of("Fall"));
        builder.setTimeRanges(List.of("0800-2000"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Pond"));
        builder.setType(Fish.FishType.LEGENDARY);
    }

    public void constructCrimsonfish(FishBuilder builder) {
        builder.reset();
        builder.setName("Crimsonfish");
        builder.setSeasons(List.of("Summer"));
        builder.setTimeRanges(List.of("0800-2000"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Ocean"));
        builder.setType(Fish.FishType.LEGENDARY);
    }

    public void constructGlacierfish(FishBuilder builder) {
        builder.reset();
        builder.setName("Glacierfish");
        builder.setSeasons(List.of("Winter"));
        builder.setTimeRanges(List.of("0800-2000"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Forest River"));
        builder.setType(Fish.FishType.LEGENDARY);
    }

    // Tambahkan semua ikan REGULAR lainnya di bawah ini

    public void constructLargemouthBass(FishBuilder builder) {
        builder.reset();
        builder.setName("Largemouth Bass");
        builder.setSeasons(List.of("Any"));
        builder.setTimeRanges(List.of("0600-1800"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Mountain Lake"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructRainbowTrout(FishBuilder builder) {
        builder.reset();
        builder.setName("Rainbow Trout");
        builder.setSeasons(List.of("Summer"));
        builder.setTimeRanges(List.of("0600-1800"));
        builder.setWeathers(List.of("Sunny"));
        builder.setLocations(List.of("Forest River", "Mountain Lake"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructSturgeon(FishBuilder builder) {
        builder.reset();
        builder.setName("Sturgeon");
        builder.setSeasons(List.of("Summer", "Winter"));
        builder.setTimeRanges(List.of("0600-1800"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Mountain Lake"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructMidnightCarp(FishBuilder builder) {
        builder.reset();
        builder.setName("Midnight Carp");
        builder.setSeasons(List.of("Winter", "Fall"));
        builder.setTimeRanges(List.of("2000-0200"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Mountain Lake", "Pond"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructFlounder(FishBuilder builder) {
        builder.reset();
        builder.setName("Flounder");
        builder.setSeasons(List.of("Spring", "Summer"));
        builder.setTimeRanges(List.of("0600-2200"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Ocean"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructOctopus(FishBuilder builder) {
        builder.reset();
        builder.setName("Octopus");
        builder.setSeasons(List.of("Summer"));
        builder.setTimeRanges(List.of("0600-2200"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Ocean"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructPufferfish(FishBuilder builder) {
        builder.reset();
        builder.setName("Pufferfish");
        builder.setSeasons(List.of("Summer"));
        builder.setTimeRanges(List.of("0000-1600"));
        builder.setWeathers(List.of("Sunny"));
        builder.setLocations(List.of("Ocean"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructSardine(FishBuilder builder) {
        builder.reset();
        builder.setName("Sardine");
        builder.setSeasons(List.of("Any"));
        builder.setTimeRanges(List.of("0600-1800"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Ocean"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructSuperCucumber(FishBuilder builder) {
        builder.reset();
        builder.setName("Super Cucumber");
        builder.setSeasons(List.of("Summer", "Fall", "Winter"));
        builder.setTimeRanges(List.of("1800-0200"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Ocean"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructCatfish(FishBuilder builder) {
        builder.reset();
        builder.setName("Catfish");
        builder.setSeasons(List.of("Spring", "Summer", "Fall"));
        builder.setTimeRanges(List.of("0600-2200"));
        builder.setWeathers(List.of("Rainy"));
        builder.setLocations(List.of("Forest River", "Pond"));
        builder.setType(Fish.FishType.REGULAR);
    }

    public void constructSalmon(FishBuilder builder) {
        builder.reset();
        builder.setName("Salmon");
        builder.setSeasons(List.of("Fall"));
        builder.setTimeRanges(List.of("0600-1800"));
        builder.setWeathers(List.of("Any"));
        builder.setLocations(List.of("Forest River"));
        builder.setType(Fish.FishType.REGULAR);
    }
}
