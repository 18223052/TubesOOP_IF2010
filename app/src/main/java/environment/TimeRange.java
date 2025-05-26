package environment;

public class TimeRange {
    public GameTime start;
    public GameTime end;

    public TimeRange (GameTime start, GameTime end){
        this.start = start;
        this.end = end;
    }

    public double getDurationHours(){
        if (start == null || end == null) {
            return 24.0;
        }
        
        int startTotalMinutes = start.getGameHour()*60 + start.getGameMinute();
        int endTotalMinutes = end.getGameHour()*60 + end.getGameMinute();
        double durationInHours;

        if (endTotalMinutes < startTotalMinutes) { 
            durationInHours = ((24 * 60 - startTotalMinutes) + endTotalMinutes) / 60.0;
        } else { // Di hari yang sama
            durationInHours = (endTotalMinutes - startTotalMinutes) / 60.0;
        }

        if (durationInHours <= 0) {
            return 24.0; // Default jika durasi tidak valid atau 0.
        }
        return durationInHours;
    }
}
