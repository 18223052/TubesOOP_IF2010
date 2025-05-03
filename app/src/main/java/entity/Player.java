package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX; // indicate where player draw at begin
    public final int screenY;
    

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2- (gp.tileSize/2);
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){

        wX = gp.tileSize * 23;
        wY = gp.tileSize *21;
        speed = 4;
        direction = "up";
    }

    public void getPlayerImage(){
        try {
            u1 = ImageIO.read(getClass().getResourceAsStream("/player/u1.png"));
            u2 = ImageIO.read(getClass().getResourceAsStream("/player/u2.png"));
            d1 = ImageIO.read(getClass().getResourceAsStream("/player/d1.png"));
            d2 = ImageIO.read(getClass().getResourceAsStream("/player/d2.png"));
            l1 = ImageIO.read(getClass().getResourceAsStream("/player/l1.png"));
            l2 = ImageIO.read(getClass().getResourceAsStream("/player/l2.png"));
            r1 = ImageIO.read(getClass().getResourceAsStream("/player/r1.png"));
            r2 = ImageIO.read(getClass().getResourceAsStream("/player/r2.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update(){
        //update player coordinate

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){
            if (keyH.upPressed == true){
                direction = "up";
                wY -= speed; 
            } else if (keyH.downPressed == true){
                direction = "down";
                wY += speed;
            } else if (keyH.leftPressed == true){
                direction = "left";
                wX -= speed;
            } else if (keyH.rightPressed == true){
                direction = "right";
                wX += speed;
            }
    
            spriteCounter ++;
            if(spriteCounter > 10){
                if (spriteNum == 1){
                    spriteNum = 2;
                }
                else if (spriteNum == 2){
                    spriteNum =1;
                }
                spriteCounter = 0;
            }
        }

    }

    public void draw(Graphics2D g2){
        // g2.setColor(Color.white);
        // g2.fillRect(x,y,gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        if (direction.equals("up")){
            if (spriteNum == 1 ){
                image = u1;
            }
            if (spriteNum == 2 ){
                image = u2;
            }
        } else if (direction.equals("down")){
            if (spriteNum == 1 ){
                image = d1;
            }
            if (spriteNum == 2 ){
                image = d2;
            }
        } else if (direction.equals("left")){
            if (spriteNum == 1 ){
                image = l1;
            }
            if (spriteNum == 2 ){
                image = l2;
            }
        } else if (direction.equals("right")){
            if (spriteNum == 1 ){
                image = r1;
            }
            if (spriteNum == 2 ){
                image = r2;
            }
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
