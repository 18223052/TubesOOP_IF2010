package environment;

public class GameTime implements Runnable {
    private int gameMinute = 0;
    private int gameHour = 6;
    private int gameDay = 1;

    // Season
    private final int totalDaysPerSeason = 10;
    private Season currentSeason = Season.SPRING;
    private Season lastSeason = Season.SPRING;

    private boolean paused = false;
    private final Object pauseLock = new Object();

    private Thread thread;

    public GameTime() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);  // 1 detik = 5 menit game (atur sesuai kebutuhan)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            addTime(5);
        }
    }

    private synchronized void addTime(int minutes) {
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

    public int getGameHour() {
        return gameHour;
    }

    public int getGameDay() {
        return gameDay;
    }

    public void pause() {
        synchronized (pauseLock) {
            paused = true;
        }
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public synchronized void setTime(int hour, int minute) {
        this.gameHour = hour;
        this.gameMinute = minute;
    }

    public synchronized void setGameDay(int day) {
        if (day > 0) {
            this.gameDay = day;
        }
    }

    public void add1Day(){
        this.gameDay += 1;
    }

    public void nextDay() {
        gameDay++;
        gameHour = 0;   
        gameMinute = 0;
    }

    public void nextDaySleep() {
        gameDay++;
        gameHour = 6;      // Mulai pagi
        gameMinute = 0;
    }


    public Season getCurrentSeason() {
        int seasonIndex = (gameDay - 1) / 10;
        return Season.values()[seasonIndex % 4];
    }

    public String getSeasonName() {
        return getCurrentSeason().name();
    }


    public int getDayInCurrentSeason() {
        return (this.gameDay - 1) % this.totalDaysPerSeason + 1; 
}
}
