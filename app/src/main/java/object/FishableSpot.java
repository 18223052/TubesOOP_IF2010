package object;

import entity.Player;
import main.GamePanel;

public class FishableSpot extends SuperObj {
    protected FishLocation fishingLocationType;

    public FishableSpot(GamePanel gp, int width, int height, FishLocation locationType){
        super(gp, width, height);
        this.fishingLocationType = locationType;
        this.collision = true;
    }
    
    public FishLocation getFishingLocationType(){
        return fishingLocationType;
    }

    @Override
    public void onInteract(GamePanel gp, Player player){
        // INI ISI SAMA FISHING CONTROLLER 
    }


    
}
