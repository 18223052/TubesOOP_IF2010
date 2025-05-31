package object;

import entity.Player;
import main.GamePanel;


public class SeedItem extends BaseItem implements IUsable {
    private String cropType;
    
    public SeedItem(String cropType, int buyPrice, int sellPrice, GamePanel gp) {
        super(cropType + "seed", buyPrice, sellPrice, gp, "seeds");
        this.cropType = cropType;
        this.sellable = true;
    }
    

    public String getCropType() {
        return cropType;
    }

    @Override
    public void use(Player player, GamePanel gp) {
        System.out.println("Using " + getName() + " (seed) on target tile.");

        int[] interactionTileCoords = player.getInteractionTile();
        int targetCol = interactionTileCoords[0];
        int targetRow = interactionTileCoords[1];

        LandTile targetLandTile = null;
        for (SuperObj obj : gp.obj) {
            if (obj instanceof LandTile) {
                LandTile land = (LandTile) obj;
                if (land.wX / gp.tileSize == targetCol && land.wY / gp.tileSize == targetRow) {
                    targetLandTile = land;
                    break;
                }
            }
        }

        if (targetLandTile != null) {
            gp.farmingController.plant(player, targetLandTile, this); 
        } else {
            gp.ui.setDialog("You can only plant on farmable land.");
            System.out.println("Farming: No LandTile found at interaction point for planting.");
        }
    }
}