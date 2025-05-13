package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Soil extends SuperObj {
        public Soil(GamePanel gp){
        super(gp);
        name = "Soil";

        try{
            tiles = new BufferedImage[1];
            tiles[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/017.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
