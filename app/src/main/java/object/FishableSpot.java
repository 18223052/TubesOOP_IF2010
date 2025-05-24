package object;

import main.GamePanel;

public class FishableSpot extends SuperObj {
    protected FishLocation fishingLocationType;

    public FishableSpot(GamePanel gp, int width, int height, int height, FishLocation locationType){
        super(gp, width, height);
        this.fishingLocationType = locationType;
        this.collision = true;
    }
    
    
}
