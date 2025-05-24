package entity;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.io.IOException;

public abstract class Entity {

    GamePanel gp;
    public int wX, wY;
    
    public Rectangle solid = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean iscollision = false;
    
    public BufferedImage u1, u2, d1, d2, l1, l2, r1, r2;


    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    public Entity(GamePanel gp){
        this.gp = gp;
    }
    
    public abstract BufferedImage getDisplayImage();

    public BufferedImage setup (String imagePath){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public void draw (Graphics2D g2){
        BufferedImage image = getDisplayImage();

        int screenX = wX - gp.player.wX + gp.player.screenX;
        int screenY = wY - gp.player.wY + gp.player.screenY;

         if (wX + gp.tileSize > gp.player.wX - gp.player.screenX &&
            wX - gp.tileSize < gp.player.wX + gp.player.screenX &&
            wY + gp.tileSize > gp.player.wY - gp.player.screenY &&
            wY - gp.tileSize < gp.player.wY + gp.player.screenY) {
            
            g2.drawImage(image, screenX, screenY, null);
        }

   }
}