package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Bed extends SuperObj {
    
    public Bed(){
        name = "Bed";

        try{
            img = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/086.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
