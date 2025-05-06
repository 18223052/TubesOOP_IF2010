package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class ShippingBin extends SuperObj {
    
    public ShippingBin(){
        name = "ShippingBin";

        try{
            img = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/035.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
