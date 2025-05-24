package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;
public class ShippingBin extends SuperObj {
    
    public ShippingBin(GamePanel gp){
        super(gp);
        collision = true;

        try{
            tiles = new BufferedImage[1];
            tiles[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/035.png"));
            
            img = setup("/tutor_tiles/035");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onInteract(GamePanel gp, Player player){
        System.out.println("DEBUG INTERACTABLE SHIPPING BIN");
        gp.gameState = gp.shippingBinState;
        gp.player.getInventory();
    }
}
