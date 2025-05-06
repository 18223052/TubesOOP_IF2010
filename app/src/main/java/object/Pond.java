package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Pond extends SuperObj {
    
    public Pond(){
        name = "Pond";

        try{
            img = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/pond.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
