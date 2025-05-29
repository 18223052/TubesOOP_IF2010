package environment;

public class GameTime implements Runnable {
    private int gameMinute = 0;
    private int gameHour = 6;
    private int gameDay = 1;

    // Season
    private final int totalDaysPerSeason = 10;

    private boolean paused = false;
    private final Object pauseLock = new Object();

    private long totalGameMillis = 0;
    private long lastUpdateTimeMillis = System.nanoTime() / 1_000_000;

    private Thread thread;
    private boolean isTimePointOnly = false;

    public GameTime() {
        this.isTimePointOnly = false;

        this.gameHour = 6;
        this.gameMinute = 0;
        this.gameDay = 1;
        updateTotalGameMillis();
        thread = new Thread(this);
        thread.start();
    }

    public GameTime(int hour, int minute){
        this.gameHour = hour;
        this.gameMinute = minute;
        this.isTimePointOnly = true;
    }

    @Override
    public void run() {
        if (isTimePointOnly){
            return;
        }

        long lastGameMinuteUpdate = getTotalGameMinutes();
        while (true) {
            try {
                Thread.sleep(1000);  // 1 detik = 5 menit game (atur sesuai kebutuhan)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                return;
            }

            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            }
            if(!paused){
            addTime(5);
            if (getTotalGameMinutes() != lastGameMinuteUpdate){
                updateTotalGameMillis();
                lastGameMinuteUpdate = getTotalGameMinutes();
            }
            }
        }
    }

    private void updateTotalGameMillis(){
        this.totalGameMillis = (long) gameDay * 24 * 60 * 60 * 1000 + // Total hari ke milidetik
                               (long) gameHour * 60 * 60 * 1000 +     // Total jam ke milidetik
                               (long) gameMinute * 60 * 1000;  
    }

    public synchronized void addTime(int minutes) {

        if (isTimePointOnly) return;

        gameMinute += minutes;
        if (gameMinute >= 60) {
            gameMinute = 0;
            gameHour++;
        }
        if (gameHour >= 24) {
            gameHour = 0;
            gameDay++;
        }
    }

    public int getGameMinute() {
        return gameMinute;
    }

    public boolean getIsTimePointOnly() {
        return isTimePointOnly;
    }

    public int getGameHour() {
        return gameHour;
    }

    public int getGameDay() {
        return gameDay;
    }

    public void pause() {
        if (isTimePointOnly) return;
        synchronized (pauseLock) {
            paused = true;
        }
    }

    public void resume() {
        if (isTimePointOnly) return;
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public synchronized void setTime(int hour, int minute) {
        this.gameHour = hour;
        this.gameMinute = minute;
        updateTotalGameMillis();
    }

    public synchronized void setGameDay(int day) {
        if (day > 0) {
            this.gameDay = day;
        }
    }

    public void add1Day(){
        this.gameDay += 1;
        this.gameHour = 6;
        this.gameMinute = 0;
        updateTotalGameMillis();
    }

    public void nextDay() {
        gameDay++;
        gameHour = 0;   
        gameMinute = 0;
        updateTotalGameMillis();
    }

    public void nextDaySleep() {
        gameDay++;
        gameHour = 6;      
        gameMinute = 0;
        updateTotalGameMillis();
    }


    public Season getCurrentSeason() {

        if (gameDay <= 0) return Season.SPRING;
        int seasonIndex = (gameDay - 1) / totalDaysPerSeason;
        return Season.values()[seasonIndex % Season.values().length];
    }

    public String getSeasonName() {
        return getCurrentSeason().name();
    }


    public int getDayInCurrentSeason() {
        if (gameDay <= 0) return 1;
        return (this.gameDay - 1) % this.totalDaysPerSeason + 1; 
    }

    public synchronized long getTotalGameMinutes(){
        return (long)(gameDay -1) *24*60 + (long)gameHour *60 +gameMinute;
    }

    public synchronized long getCurrentGameTime(){
        return totalGameMillis;
    }

    public GameTime getCurrentTime(){
        return this;
    }

    public synchronized boolean isNewDay(long previousTimeMillis){
        if (previousTimeMillis == -1){
            return false;
        }
        long previousDay = previousTimeMillis /(24*60*60*1000L);
        long currentDay = totalGameMillis/ (24*60*60*1000L);

        return currentDay > previousDay;
    }
}
