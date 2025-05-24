package environment;

import main.GamePanel;
import java.awt.Graphics2D;

public class EnvironmentManager {
    GamePanel gp;
    Lighting lighting;
    private boolean isSetup = false;
    public GameTime gameTime = new GameTime();

    public EnvironmentManager(GamePanel gp){
        this.gp = gp;
    }

    public void setup(){
        lighting = new Lighting(gp, 400);
        isSetup = true;
    }

    public void update(){
        if (isSetup && lighting != null) {
            lighting.updateEnvironment();
        }
    }

    public void draw(Graphics2D g2){
        if (isSetup && lighting != null) {
            lighting.draw(g2);
        }
    }
    
    // Metode untuk mengecek apakah lighting sudah diinisialisasi
    public boolean isLightingSetup() {
        return isSetup && lighting != null;
    }

    public Lighting getLighting() {
        return lighting;
    }

    public void onPause() {
        if (gameTime != null) {
            gameTime.pause();
        }
    }

    public void onResume() {
        if (gameTime != null) {
            gameTime.resume();
        }
        // if (lighting != null) lighting.onResume();
    }

    public GameTime getGameTime() {
        return gameTime;
    }
}