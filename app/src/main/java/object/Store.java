package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;

public class Store extends SuperObj{
    
    public Store(GamePanel gp){
        super(gp);
        collision = true;

        try{
            tiles = new BufferedImage[1];
            tiles[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/197.png"));

            img = setup("/tutor_tiles/197");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onInteract (GamePanel gp, Player player){
        // System.out.println("DEBUG INTERACTABLE STORE");
        gp.gameState = GamePanel.storeState;
        
    }
}
