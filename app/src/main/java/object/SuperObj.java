package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class SuperObj {
    public BufferedImage[] img;
    public String name;
    public boolean collision = false;
    public int wX, wY;
    public int width = 1; 
    public int height = 1; 
    public Rectangle solidArea;
    public GamePanel gp;

    public SuperObj(GamePanel gp){
        this.gp = gp;
        this.solidArea = new Rectangle(0,0,gp.tileSize, gp.tileSize);
    }

    public SuperObj(GamePanel gp, int width, int height) {
        this.gp = gp;
        this.width = width;
        this.height = height;
        this.solidArea = new Rectangle(0, 0, width * gp.tileSize, height * gp.tileSize);
    }

    public void draw (Graphics2D g2, GamePanel gp){
        int screenX = wX - gp.player.wX + gp.player.screenX;
        int screenY = wY - gp.player.wY + gp.player.screenY;

        if (
            wX + gp.tileSize > gp.player.wX - gp.player.screenX && 
            wX - gp.tileSize < gp.player.wX + gp.player.screenX &&
            wY + gp.tileSize > gp.player.wY - gp.player.screenY &&
            wY - gp.tileSize < gp.player.wX + gp.player.screenY) {
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int index = i + j * width;
                        if (img[index] != null) {
                            g2.drawImage(img[index], screenX + i * gp.tileSize, screenY + j * gp.tileSize, null);
                        }
                    }
                }
        }
    }
}
