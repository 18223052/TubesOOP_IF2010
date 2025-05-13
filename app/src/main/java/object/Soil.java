package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Soil extends SuperObj {
        public Soil(GamePanel gp){
        super(gp);
        name = "Soil";

        try{
            img[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/017.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
