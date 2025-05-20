package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import main.GamePanel;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.Shape;
import java.awt.AlphaComposite;
import environment.GameTime;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;
    GameTime gameTime;
    
    // Konstanta waktu dalam nanodetik (10 detik per transisi)
    final long TRANSITION_DURATION = 10_000_000_000L;
    
    // Alpha filter untuk efek kegelapan (0f = terang, 1f = gelap)
    private float filterAlpha = 0f;
    
    // Target alpha untuk transisi yang sedang berlangsung
    private float targetAlpha = 0f;
    private float startAlpha = 0f;

    // Day State
    public static final int DAY = 0;
    public static final int DUSK = 1;
    public static final int NIGHT = 2;
    public static final int DAWN = 3;
    private int dayState = DAY;
    private int previousDayState = DAY;
    private long pauseStartTime;
    private boolean isPaused = false;

    // Waktu untuk pelacakan transisi dan state
    private long transitionStartTime;
    private boolean inTransition = false;
    
    public Lighting(GamePanel gp, int circleSize) {
        this.gp = gp;
        
        // Inisialisasi filter kegelapan
        createLightFilter(circleSize);
        
        // Inisialisasi waktu awal
        transitionStartTime = System.nanoTime();
    }
    
    private void createLightFilter(int circleSize) {
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();

        // Membuat area layar
        Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));

        // Posisi lingkaran cahaya
        int centerX = gp.player.screenX + (gp.tileSize)/2;
        int centerY = gp.player.screenY + (gp.tileSize)/2;

        double x = centerX - (circleSize/2);
        double y = centerY - (circleSize/2);

        // Membuat bentuk lingkaran untuk area cahaya
        Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);
        Area lightArea = new Area(circleShape);
        screenArea.subtract(lightArea);

        // Membuat efek gradasi
        Color color[] = new Color[8];
        float fraction[] = new float[8];

        color[0] = new Color(0,0,0.1f,0f);
        color[1] = new Color(0,0,0.1f,0.42f);
        color[2] = new Color(0,0,0.1f,0.52f);
        color[3] = new Color(0,0,0.1f,0.61f);
        color[4] = new Color(0,0,0.1f,0.69f);
        color[5] = new Color(0,0,0.1f,0.76f);
        color[6] = new Color(0,0,0.1f,0.82f);
        color[7] = new Color(0,0,0.1f,0.87f);
        // color[8] = new Color(0,0,0,0.91f);
        // color[9] = new Color(0,0,0,0.94f);
        // color[10] = new Color(0,0,0,0.96f);
        // color[11] = new Color(0,0,0,0.98f);

        fraction[0] = 0f;
        fraction[1] = 0.4f;
        fraction[2] = 0.5f;
        fraction[3] = 0.6f;
        fraction[4] = 0.65f;
        fraction[5] = 0.7f;
        fraction[6] = 0.75f;
        fraction[7] = 0.8f;
        // fraction[8] = 0.85f;
        // fraction[9] = 0.9f;
        // fraction[10] = 0.95f;
        // fraction[11] = 1f;

        // Menerapkan gradient paint
        RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, (circleSize/2), fraction, color);
        g2.setPaint(gPaint);
        g2.fill(lightArea);
        g2.fill(screenArea);
        g2.dispose();
    }
    
    public void updateEnvironment() {
        // Hentikan update environment jika game tidak dalam playState
        if (gp.gameState != gp.playState) {
            return;
        }
        
        long currentTime = System.nanoTime();
        
        // Jika sedang dalam transisi
        if (inTransition) {
            updateTransition(currentTime);
        } 
        // Jika tidak dalam transisi, cek apakah sudah waktunya untuk memulai transisi berikutnya
        // else {
        //     long elapsedSinceLastTransition = currentTime - transitionStartTime;
            
        //     if (elapsedSinceLastTransition >= TRANSITION_DURATION) {
        //         startNextTransition(currentTime);
        //     }
        // }
    }
    
    private void updateTransition(long currentTime) {
        long elapsedTime = currentTime - transitionStartTime;
        float progress = Math.min((float) elapsedTime / TRANSITION_DURATION, 1.0f);
        
        // Gunakan fungsi easing untuk transisi yang lebih halus
        float easedProgress = easeInOutQuad(progress);
        
        // Perbarui alpha berdasarkan progress
        filterAlpha = startAlpha + (targetAlpha - startAlpha) * easedProgress;
        
        // Jika transisi selesai
        if (progress >= 1.0f) {
            filterAlpha = targetAlpha; // Pastikan tepat nilai target
            inTransition = false;
            previousDayState = dayState;
            
            // Perbarui waktu awal untuk perhitungan state berikutnya
            transitionStartTime = currentTime;
            
            System.out.println("Transisi selesai: " + getDayStateName(previousDayState) + " -> " + getDayStateName(dayState) + ", Alpha: " + filterAlpha);
        }
    }
    
    private void startNextTransition(long currentTime) {
        previousDayState = dayState;
        
        // Tentukan state berikutnya
        switch (dayState) {
            case DAY:
                dayState = DUSK;
                startAlpha = 0f;
                targetAlpha = 1f;
                break;
                
            case DUSK:
                dayState = NIGHT;
                startAlpha = 1f;
                targetAlpha = 1f; // Tetap gelap penuh
                break;
                
            case NIGHT:
                dayState = DAWN;
                startAlpha = 1f;
                targetAlpha = 0f;
                break;
                
            case DAWN:
                dayState = DAY;
                startAlpha = 0f;
                targetAlpha = 0f; // Tetap terang penuh
                break;
        }
        
        // Mulai transisi baru
        transitionStartTime = currentTime;
        inTransition = true;
        
        System.out.println("Memulai transisi: " + getDayStateName(previousDayState) +" -> " + getDayStateName(dayState) + ", Alpha: " + startAlpha + " -> " + targetAlpha);
    }
    
    // Fungsi easing untuk transisi yang lebih halus
    private float easeInOutQuad(float x) {
        return x < 0.5 ? 2 * x * x : 1 - (float)Math.pow(-2 * x + 2, 2) / 2;
    }
    
    // Helper method untuk mendapatkan nama state
    public String getDayStateName(int state) {
        switch(state) {
            case DAY: return "DAY";
            case DUSK: return "DUSK";
            case NIGHT: return "NIGHT";
            case DAWN: return "DAWN";
            default: return "UNKNOWN";
        }
    }

    public void triggerTransition(int targetState) {
        if (dayState == targetState || inTransition) return;

        previousDayState = dayState;
        dayState = targetState;

        switch (targetState) {
            case DUSK:  // Fade to dark
                startAlpha = filterAlpha;
                targetAlpha = 1f;
                inTransition = true;
                transitionStartTime = System.nanoTime();
                break;
            case DAWN:  // Fade to light
                startAlpha = filterAlpha;
                targetAlpha = 0f;
                inTransition = true;
                transitionStartTime = System.nanoTime();
                break;
            case NIGHT: // Langsung gelap
                filterAlpha = 1f;
                startAlpha = 1f;
                targetAlpha = 1f;
                inTransition = false;
                break;
            case DAY:   // Langsung terang
                filterAlpha = 0f;
                startAlpha = 0f;
                targetAlpha = 0f;
                inTransition = false;
                break;
        }

        System.out.println("Triggered transition: " + getDayStateName(previousDayState) + " -> " + getDayStateName(dayState));
    }



    public void skipDay() {

    dayState = DAY;
    previousDayState = DAWN;
    filterAlpha = 0f;
    targetAlpha = 0f;
    startAlpha = 0f;
    inTransition = false;
    

    transitionStartTime = System.nanoTime();
    
    System.out.println("Day skipped! Current state: " + getDayStateName(dayState));
}


    public boolean isNight() {
        return dayState == NIGHT;
    }


    public int getCurrentDayState() {
        return dayState;
    }
    
    public void onPause(){
        if (!isPaused){
            pauseStartTime = System.nanoTime();
            isPaused = true;
        }
    }

    public void onResume(){
        if (isPaused){
            long pausedDuration = System.nanoTime() - pauseStartTime;
            transitionStartTime += pausedDuration;
            isPaused = false;
        }
    }

    public void draw(Graphics2D g2) {

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        g2.drawImage(darknessFilter, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        }
}