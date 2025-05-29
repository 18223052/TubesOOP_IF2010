package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.GamePanel;

public abstract class Character extends Entity implements MovableEntity { 
    public int speed;
    public String direction;

    protected int spriteCounter = 0;
    protected int spriteNum = 1;

    protected BufferedImage u1,u2, d1, d2, l1, l2, r1, r2;
    protected BufferedImage defaultImage;

    public Character(GamePanel gp) {
        super(gp);
    }

    public abstract void getCharacterImage();

    @Override
    public void update() {
        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    @Override
    public BufferedImage getAnimationImage() {
        BufferedImage image = defaultImage;
        switch (direction) {
            case "up":
                image = (spriteNum == 1 && u1 != null) ? u1 : ((u2 != null) ? u2 : defaultImage);
                break;
            case "down":
                image = (spriteNum == 1 && d1 != null) ? d1 : ((d2 != null) ? d2 : defaultImage);
                break;
            case "left":
                image = (spriteNum == 1 && l1 != null) ? l1 : ((l2 != null) ? l2 : defaultImage);
                break;
            case "right":
                image = (spriteNum == 1 && r1 != null) ? r1 : ((r2 != null) ? r2 : defaultImage);
                break;
        }
        if (image == null) {
            image = defaultImage;
        }
        return image;
    }

    @Override
    public BufferedImage getDisplayImage() {
        return getAnimationImage(); 
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getDisplayImage(); 

        int screenX = wX - gp.player.wX + gp.player.screenX;
        int screenY = wY - gp.player.wY + gp.player.screenY;

        // Gambar karakter
        if (wX + gp.tileSize > gp.player.wX - gp.player.screenX &&
            wX - gp.tileSize < gp.player.wX + gp.player.screenX &&
            wY + gp.tileSize > gp.player.wY - gp.player.screenY &&
            wY - gp.tileSize < gp.player.wY + gp.player.screenY) {
            if (image != null) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}