package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Soil extends SuperObj {
        public Soil(){
        name = "Soil";

        try{
            img = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/017.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
