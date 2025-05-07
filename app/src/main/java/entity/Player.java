package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;

    public int screenX; // indicate where player draw at begin
    public int screenY;
    
    // Interaction box for player actions
    private Rectangle interactionBox;
    private int interactionDistance;
    private int interactionTileRow;
    private int interactionTileCol;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2- (gp.tileSize/2);

        solid = new Rectangle();
        solid.x = 0;
        solid.y = 16;
        solid.width = 32;
        solid.height = 32;
        
        // Initialize interaction box
        interactionBox = new Rectangle();
        interactionBox.width = gp.tileSize;
        interactionBox.height = gp.tileSize;
        interactionDistance = gp.tileSize; // 1 tile distance
        
        setDefaultValues();
        getPlayerImage();
        updateInteractionBox(); // Initialize interaction box position
    }

    public void setDefaultValues(){
        wX = gp.tileSize * 23;
        wY = gp.tileSize * 21;
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
    
    // Calculate player's current tile position
    private void calculatePlayerTilePosition() {
        int playerCol = wX / gp.tileSize;
        int playerRow = wY / gp.tileSize;
        
        // Calculate interaction tile based on direction
        interactionTileCol = playerCol;
        interactionTileRow = playerRow;
        
        switch(direction) {
            case "up":
                interactionTileRow = playerRow - 1;
                break;
            case "down":
                interactionTileRow = playerRow + 1;
                break;
            case "left":
                interactionTileCol = playerCol - 1;
                break;
            case "right":
                interactionTileCol = playerCol + 1;
                break;
        }
    }
    
    // Update interaction box position based on tile grid
    public void updateInteractionBox() {
        // First calculate which tile we're targeting
        calculatePlayerTilePosition();
        
        // Now set interaction box to align perfectly with that tile
        interactionBox.x = interactionTileCol * gp.tileSize;
        interactionBox.y = interactionTileRow * gp.tileSize;
        interactionBox.width = gp.tileSize;
        interactionBox.height = gp.tileSize;
    }
    
    // Check for interaction with objects
    public int checkInteraction(Entity[] objects) {
        int index = 999; // Default value meaning no interaction
        
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                // Make sure interaction box is updated
                updateInteractionBox();
                
                // Get the object's solid area
                Rectangle objectSolid = objects[i].solid;
                int objectWorldX = objects[i].wX;
                int objectWorldY = objects[i].wY;
                
                // Create temporary rectangle for the object's current position
                Rectangle objectRect = new Rectangle(
                    objectWorldX + objectSolid.x,
                    objectWorldY + objectSolid.y,
                    objectSolid.width,
                    objectSolid.height
                );
                
                // Check if the interaction box intersects with the object
                if (interactionBox.intersects(objectRect)) {
                    // If player is facing the object
                    index = i;
                }
            }
        }
        
        return index;
    }
    
    // Check which tile the player is interacting with
    public int[] getInteractionTile() {
        updateInteractionBox();
        return new int[] {interactionTileCol, interactionTileRow};
    }
    
    // Method to check if a specific tile can be interacted with (e.g. for planting)
    public boolean canInteractWithTile(int tileType) {
        updateInteractionBox();
        
        int tileNum = gp.tileM.mapTileNum[interactionTileCol][interactionTileRow];
        
        // Check if this tile is of the type we want to interact with
        return tileNum == tileType;
    }

    public void update(){
        //update player coordinate
        if(keyH.upPressed == true || keyH.downPressed == true || 
           keyH.leftPressed == true || keyH.rightPressed == true){
            
            if (keyH.upPressed == true){
                direction = "up";
            } else if (keyH.downPressed == true){
                direction = "down";
            } else if (keyH.leftPressed == true){
                direction = "left";
            } else if (keyH.rightPressed == true){
                direction = "right";
            }
            
            iscollision = false;
            gp.colCheck.checkTile(this);

            if (iscollision == false) {
                switch(direction){
                    case "up":
                        wY -= speed; 
                        break;
                    case "down":
                        wY += speed;
                        break;
                    case "left":
                        wX -= speed;
                        break;
                    case "right":
                        wX += speed;
                        break;
                }
            }

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
        
        // Update interaction box position after player moves
        updateInteractionBox();
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        if (direction.equals("up")){
            if (spriteNum == 1){
                image = u1;
            }
            if (spriteNum == 2){
                image = u2;
            }
        } else if (direction.equals("down")){
            if (spriteNum == 1){
                image = d1;
            }
            if (spriteNum == 2){
                image = d2;
            }
        } else if (direction.equals("left")){
            if (spriteNum == 1){
                image = l1;
            }
            if (spriteNum == 2){
                image = l2;
            }
        } else if (direction.equals("right")){
            if (spriteNum == 1){
                image = r1;
            }
            if (spriteNum == 2){
                image = r2;
            }
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        
        // Draw interaction box (for debugging, can be commented out in production)
        drawInteractionBox(g2);
    }
    
    // Draw the interaction box for visualization/debugging
    public void drawInteractionBox(Graphics2D g2) {
        // Calculate the screen position from world position
        int screenX = interactionBox.x - wX + this.screenX;
        int screenY = interactionBox.y - wY + this.screenY;
        
        // Draw with a different color for visibility
        g2.setColor(Color.GREEN);
        g2.drawRect(screenX, screenY, interactionBox.width, interactionBox.height);
    }
    
    // Getter for interaction box (if needed elsewhere)
    public Rectangle getInteractionBox() {
        return interactionBox;
    }
}