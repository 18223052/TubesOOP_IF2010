package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;


public class Bed extends SuperObj {
    private static final int BED_WIDTH = 2;
    private static final int BED_HEIGHT = 2;
    
    public Bed(GamePanel gp){
        super(gp, BED_WIDTH, BED_HEIGHT);
        collision = true;

        try{
            tiles =new BufferedImage[BED_WIDTH*BED_HEIGHT];
            tiles[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/081.png"));
            tiles[1] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/082.png"));
            tiles[2] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/085.png"));
            tiles[3] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/086.png"));

            img = setup("/tutor_tiles/081");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onInteract(GamePanel gp, Player player){
        System.out.println("DEBUG INTERACTABLE BED");
        gp.sleepController.startSleep();
    }
}
