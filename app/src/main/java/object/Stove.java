package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Stove extends SuperObj {
    public Stove(GamePanel gp){
        super(gp,2,3);
        name = "stove";

        try{
            img[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/117.png"));
            img[1] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/118.png"));
            img[2] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/121.png"));
            img[3] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/122.png"));
            img[4] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/125.png"));
            img[5] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/126.png"));
            img[6] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/129.png"));
            img[7] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/130.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void startCooking(GamePanel gp) {
        System.out.println("Starting cooking!");
        gp.gameState = gp.cookingState;
    }

}
