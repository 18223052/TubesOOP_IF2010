package entity;

import java.awt.image.BufferedImage;

public class Entity {
    
    public int x,y;
    public int speed;

    public BufferedImage u1, u2, d1, d2, l1, l2, r1, r2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

}
