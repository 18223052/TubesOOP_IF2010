package entity;

import java.awt.image.BufferedImage;
import main.GamePanel;

public abstract class Character extends Entity implements MovableEntity { 
    public int speed;
    public String direction;

    protected int spriteCounter = 0;
    protected int spriteNum = 1;

    public Character(GamePanel gp) {
        super(gp);
    }

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
        BufferedImage image = null;
        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? u1 : u2;
                break;
            case "down":
                image = (spriteNum == 1) ? d1 : d2;
                break;
            case "left":
                image = (spriteNum == 1) ? l1 : l2;
                break;
            case "right":
                image = (spriteNum == 1) ? r1 : r2;
                break;
        }
        return image;
    }

    @Override
    public BufferedImage getDisplayImage() {
        return getAnimationImage(); 
    }
}