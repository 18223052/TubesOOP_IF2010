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
        if (player.getActiveItem() == null || !player.getActiveItem().getName().equals("fishingpole")){
            gp.ui.setDialog("Kamu membutuhkan Fishing pole");
            gp.setGameState(GamePanel.dialogState);
            return;
        }

        gp.gameTime.pause();
        player.deductEnergy(5);

        gp.setGameState(GamePanel.fishingState);
        gp.fishingController.startFishing(getFishingLocationType());
    }

    
}
