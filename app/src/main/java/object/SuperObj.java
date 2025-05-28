package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import entity.Player;
import interactable.Interactable;
import main.GamePanel;
import main.UtilityTool;

public abstract class SuperObj implements Interactable {
    protected GamePanel gp;
    public BufferedImage img;
    public BufferedImage[] tiles;
    public String name;
    public boolean collision = false;
    public int wX, wY;
    public int width = 1; 
    public int height = 1; 
    public Rectangle solidArea;


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

    public String getName(){
        return name;
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

    private boolean isOnScreen(GamePanel gp) {
        return wX + (width * gp.tileSize) > gp.player.wX - gp.player.screenX && 
               wX < gp.player.wX + gp.player.screenX &&
               wY + (height * gp.tileSize) > gp.player.wY - gp.player.screenY &&
               wY < gp.player.wY + gp.player.screenY;
    }

    @Override
    public boolean isInteractable(Rectangle interactionBox) {

        Rectangle objectBounds = new Rectangle(
            wX,
            wY,
            width * gp.tileSize,
            height * gp.tileSize
        );
        
       
        return objectBounds.intersects(interactionBox);
    }

    @Override
    public abstract void onInteract(GamePanel gp, Player player);

    protected BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        String fullPath = imagePath + ".png"; 

        try {
            InputStream is = getClass().getResourceAsStream(fullPath);
            if (is == null) {
                System.err.println("Error: InputStream for " + fullPath + " is null. File not found on classpath.");
            }
            image = javax.imageio.ImageIO.read(is);
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }


}