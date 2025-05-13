package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TV extends SuperObj {
   private static final int TV_WIDTH = 2;
    private static final int TV_HEIGHT = 2;
    
    public TV(GamePanel gp){
        super(gp, TV_WIDTH, TV_HEIGHT);
        name = "TV";
        collision = true;

        try{
            tiles =new BufferedImage[TV_WIDTH*TV_HEIGHT];
            tiles[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/169.png"));
            tiles[1] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/170.png"));
            tiles[2] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/173.png"));
            tiles[3] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/174.png"));

            img = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/169.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    } 
}
