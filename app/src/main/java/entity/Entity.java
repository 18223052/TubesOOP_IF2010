package entity;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Entity {

    GamePanel gp;
    public int wX, wY;
    public int speed;

    public BufferedImage u1, u2, d1, d2, l1, l2, r1, r2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    public Rectangle solid = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean iscollision = false;
    

    public String[] dialogues = new String[20]; 
    public int dialogIndex = 0;
    
    public Entity(GamePanel gp){
        this.gp = gp;
    }
    
    public void setDialogue() {

    }
    
    public void speak() {
        if (dialogues[dialogIndex] == null) {
            dialogIndex = 0; 
        }
        

        gp.ui.currentDialog = dialogues[dialogIndex];
        dialogIndex++;
        

        gp.gameState = gp.dialogState;
    }
    

    public void update(){
        spriteCounter++;
        if(spriteCounter > 10){
            if (spriteNum == 1){
                spriteNum = 2;
            }
            else if (spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        
        int screenX = wX - gp.player.wX + gp.player.screenX;
        int screenY = wY - gp.player.wY + gp.player.screenY;
        

        if (wX + gp.tileSize > gp.player.wX - gp.player.screenX && 
            wX - gp.tileSize < gp.player.wX + gp.player.screenX && 
            wY + gp.tileSize > gp.player.wY - gp.player.screenY && 
            wY - gp.tileSize < gp.player.wY + gp.player.screenY) {
            
            switch(direction) {
                case "up":
                    if (spriteNum == 1) { image = u1; }
                    if (spriteNum == 2) { image = u2; }
                    break;
                case "down":
                    if (spriteNum == 1) { image = d1; }
                    if (spriteNum == 2) { image = d2; }
                    break;
                case "left":
                    if (spriteNum == 1) { image = l1; }
                    if (spriteNum == 2) { image = l2; }
                    break;
                case "right":
                    if (spriteNum == 1) { image = r1; }
                    if (spriteNum == 2) { image = r2; }
                    break;
            }
            
            g2.drawImage(image, screenX, screenY, null);
        }
    }
    
    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return image;
    }
}