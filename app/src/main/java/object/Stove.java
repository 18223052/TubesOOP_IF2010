package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Stove extends SuperObj {
    public Stove(){
        name = "stove";

        try{
            img = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/126.png"));
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
