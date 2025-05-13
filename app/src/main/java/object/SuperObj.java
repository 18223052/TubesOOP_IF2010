package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class SuperObj {
    public BufferedImage img;
    public BufferedImage[] tiles; // Array for storing multiple tiles for larger objects
    public String name;
    public boolean collision = false;
    public int wX, wY;
    public int width = 1; 
    public int height = 1; 
    public Rectangle solidArea;
    public GamePanel gp;


    public SuperObj(GamePanel gp){
        this.gp = gp;
        this.solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
    }


    public SuperObj(GamePanel gp, int width, int height) {
        this.gp = gp;
        this.width = width;
        this.height = height;

        this.solidArea = new Rectangle(0, 0, width * gp.tileSize, height * gp.tileSize);
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = wX - gp.player.wX + gp.player.screenX;
        int screenY = wY - gp.player.wY + gp.player.screenY;


        if (isOnScreen(gp)) {
            if (tiles != null && tiles.length > 0) {

                int tileIndex = 0;
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        if (tileIndex < tiles.length) {
                            g2.drawImage(
                                tiles[tileIndex],
                                screenX + (col * gp.tileSize),
                                screenY + (row * gp.tileSize),
                                gp.tileSize,
                                gp.tileSize,
                                null
                            );
                            tileIndex++;
                        }
                    }
                }
            } else if (img != null) {

                g2.drawImage(img, screenX, screenY, width * gp.tileSize, height * gp.tileSize, null);
            }
        }
    }

    // Check if object is visible on screen
    private boolean isOnScreen(GamePanel gp) {
        return wX + (width * gp.tileSize) > gp.player.wX - gp.player.screenX && 
               wX < gp.player.wX + gp.player.screenX &&
               wY + (height * gp.tileSize) > gp.player.wY - gp.player.screenY &&
               wY < gp.player.wY + gp.player.screenY;
    }

    // Method to check if an interaction box intersects with this object
    public boolean isInteractable(Rectangle interactionBox) {
        // Calculate the object's bounds based on its position and size
        Rectangle objectBounds = new Rectangle(
            wX,
            wY,
            width * gp.tileSize,
            height * gp.tileSize
        );
        
        // Check if the player's interaction box intersects with any part of this object
        return objectBounds.intersects(interactionBox);
    }
}