package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class SuperObj {
    public BufferedImage img;
    public String name;
    public boolean collision = false;
    public int wX, wY;

    public void draw (Graphics2D g2, GamePanel gp){
        int screenX = wX - gp.player.wX + gp.player.screenX;
        int screenY = wY - gp.player.wY + gp.player.screenY;

        if (
            wX + gp.tileSize > gp.player.wX - gp.player.screenX && 
            wX - gp.tileSize < gp.player.wX + gp.player.screenX &&
            wY + gp.tileSize > gp.player.wY - gp.player.screenY &&
            wY - gp.tileSize < gp.player.wX + gp.player.screenY) {
                g2.drawImage(img, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
    }

}
