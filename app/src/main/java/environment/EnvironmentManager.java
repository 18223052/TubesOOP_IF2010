package environment;

import main.GamePanel;
import java.awt.Graphics2D;

public class EnvironmentManager {
    GamePanel gp;
    Lighting lighting;
    private boolean isSetup = false;

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
}