package object;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class RiverFishingSpot extends FishableSpot {
    
    public RiverFishingSpot(GamePanel gp, int wX, int wY){
        super(gp, 1, 1, FishLocation.FOREST_RIVER);
        this.name = "forest river";
        this.wX = wX;
        this.wY = wY;
        collision = true;
        getImages();
    }

    private void getImages(){
        try{
            tiles = new BufferedImage[1];
            tiles[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/200.png"));
            
            img = setup("/tutor_tiles/200");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
