package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Bed extends SuperObj {
    
    public Bed(GamePanel gp){
        super(gp, 2, 2);
        name = "Bed";
        collision = true;

        try{
            img[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/086.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
