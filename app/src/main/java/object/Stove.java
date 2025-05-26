package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;
public class Stove extends SuperObj {

    private static final int STOVE_WIDTH = 2;
    private static final int STOVE_HEIGHT = 4;

    public Stove(GamePanel gp){
        super(gp,STOVE_WIDTH, STOVE_HEIGHT);
        collision = true;

        try{

            tiles = new BufferedImage[STOVE_WIDTH * STOVE_HEIGHT];
            tiles[0] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/117.png"));
            tiles[1] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/118.png"));
            tiles[2] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/121.png"));
            tiles[3] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/122.png"));
            tiles[4] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/125.png"));
            tiles[5] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/126.png"));
            tiles[6] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/129.png"));
            tiles[7] = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/130.png"));

            img = setup("/tutor_tiles/117");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onInteract(GamePanel gp, Player player){
        gp.currNPC = null;
        gp.gameState = GamePanel.cookingState;
        gp.ui.cookingMenu.selectRecipe = 0;
        gp.ui.cookingMenu.doneCooking = false;
        gp.ui.cookingMenu.hasIngradients = true;
        gp.ui.cookingMenu.cookingMenuSelection = 0;
    }

}
