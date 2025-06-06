package object;

import entity.Player;
import main.GamePanel;

/**
 * Implementasi untuk item tools seperti hoe,pickaxe,fishingpole
 */
public class ToolItem extends BaseItem implements IUsable {
    private String toolType;
    
    public ToolItem(String toolType, int buyPrice, int sellPrice, GamePanel gp) {
        super(toolType, buyPrice, sellPrice, gp, "tools");
        this.toolType = toolType;
        setStackable(false); 
        this.sellable = false;
    }
    

    public String getToolType() {
        return toolType;
    }
    

    @Override
    public void use(Player player, GamePanel gp) {
        System.out.println("Using " + getName() + " on target tile (delegating to FarmingController).");
        

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

            switch (toolType.toLowerCase()) {
                case "hoe":
                    gp.farmingController.tillage(player, targetLandTile);
                    break;
                case "pickaxe":
                    gp.farmingController.recoverLand(player, targetLandTile);
                    break;
                case "wateringcan":
                    gp.farmingController.water(player, targetLandTile);
                    break;

                default:
                    gp.ui.setDialog("This tool has no specific action on land.");
                    System.out.println("Farming: Tool " + getName() + " not configured for land interaction.");
            }
        } else {
            gp.ui.setDialog("No farmable tile found in front of you.");
            System.out.println("Farming: No LandTile found at interaction point.");

        }
    }
}
