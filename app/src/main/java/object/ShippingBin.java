package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class ShippingBin extends SuperObj {
    
    public ShippingBin(GamePanel gp){
        super(gp);
        name = "ShippingBin";

        try{
            img[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/035.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
