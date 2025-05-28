package controller;

import entity.Player;
import main.GamePanel;
import environment.GameTime;
import object.LandTile;
import object.Pickaxe; // Uncommented
import object.TileState;
import object.Hoe; // Uncommented
import object.IItem;
import object.ToolItem;
import object.WateringCan; // Uncommented
import object.SeedItem;
import object.SuperObj;
import object.CropItem; // Uncommented

public class FarmingController {

    private GamePanel gp;
    private GameTime gameTime;

    private static final int ENERGY_COST_PER_TILE = 5;
    private static final int TIME_COST_PER_TILE_MINUTES = 5;

    public FarmingController(GamePanel gp, GameTime gameTime) {
        this.gp = gp;
        this.gameTime = gameTime;
    }

    /**
     * Entry point for player interaction with a LandTile.
     * Delegates to specific farming actions based on the active item.
     * @param player The player interacting.
     * @param targetTile The LandTile being interacted with.
     */
    public void handleTileInteraction(Player player, LandTile targetTile) {
    IItem activeItem = player.getActiveItem();

    if (activeItem == null) {
        System.out.println("No active item selected.");
        System.out.println("Farming: No active item.");
        return;
    }

    System.out.println("Farming: Player has " + activeItem.getName() + " active. Actual class: " + activeItem.getClass().getName() + ". Interacting with " + targetTile.name);

    if (activeItem instanceof Hoe) {
        System.out.println("Farming: Detected Hoe instance."); 
        tillage(player, targetTile);
    } else if (activeItem instanceof Pickaxe) {
        System.out.println("Farming: Detected Pickaxe instance."); 
        recoverLand(player, targetTile);
    } else if (activeItem instanceof SeedItem) {
        System.out.println("Farming: Detected SeedItem instance."); 
        plant(player, targetTile, (SeedItem) activeItem);
    } else if (activeItem instanceof WateringCan) {
        System.out.println("Farming: Detected WateringCan instance."); 
        water(player, targetTile);
    } else if (activeItem.getName().equals("Hand") || activeItem.getName().equals("No Item")) {
        if (targetTile.isHarvestable()) {
            harvest(player, targetTile);
        } else {
            System.out.println("Nothing to do with " + targetTile.name + " using bare hands.");
            System.out.println("Farming: Hand interaction on non-harvestable tile.");
        }
    } else {
        System.out.println("You can't use " + activeItem.getName() + " here.");
        System.out.println("Farming: Cannot use " + activeItem.getName() + " on " + targetTile.name);
    }
}
    /**
     * Attempts to till a LandTile into Soil.
     * @param player The player performing the action.
     * @param targetTile The LandTile to till.
     */
    public void tillage(Player player, LandTile targetTile) {
        if (!(player.getActiveItem() instanceof Hoe)) {
            System.out.println("You need a Hoe to till the land.");
            return;
        }

        if (targetTile.getCurrentState() != TileState.LAND) {
            System.out.println("This tile is not bare land.");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Not enough energy to till!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            targetTile.toSoil();
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("You tilled the land.");
            gp.saveGame();
        }
    }

    /**
     * Attempts to recover a LandTile back to its natural state.
     * @param player The player performing the action.
     * @param targetTile The LandTile to recover.
     */
    public void recoverLand(Player player, LandTile targetTile) {
        if (!(player.getActiveItem() instanceof Pickaxe)) {
            System.out.println("You need a Pickaxe to recover the land.");
            return;
        }

        // Recovery can be done on soil, planted, watered, or harvestable tile
        if (targetTile.getCurrentState() == TileState.LAND) {
            System.out.println("This tile is already bare land.");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Not enough energy to recover land!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            targetTile.toLand();
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("You recovered the land.");
        }
    }

    /**
     * Attempts to plant a seed on a LandTile.
     * @param player The player performing the action.
     * @param targetTile The LandTile to plant on.
     * @param seed The SeedItem to plant.
     */
    public void plant(Player player, LandTile targetTile, SeedItem seed) {
        if (!(player.getActiveItem() instanceof SeedItem) ||
             !((SeedItem)player.getActiveItem()).getCropType().equals(seed.getCropType())) {
            System.out.println("Select the correct seed to plant.");
            return;
        }

        if (targetTile.getCurrentState() != TileState.SOIL) {
            System.out.println("You can only plant on tilled soil.");
            return;
        }

        if (!player.getInventory().hasItem(seed.getName())) {
            System.out.println("You don't have this seed!");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Not enough energy to plant!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            if (targetTile.plant(seed.getCropType())) {
                player.getInventory().removeItems(seed.getName(), 1);
                gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
                System.out.println("You planted a " + seed.getName() + ".");
            } else {
                // LandTile.plant() is expected to already display why it failed (e.g., wrong season)
            }
        }
    }

    /**
     * Attempts to water a planted LandTile.
     * @param player The player performing the action.
     * @param targetTile The LandTile to water.
     */
    public void water(Player player, LandTile targetTile) {
        if (!(player.getActiveItem() instanceof WateringCan)) {
            System.out.println("You need a Watering Can to water plants.");
            return;
        }

        if (targetTile.getCurrentState() != TileState.PLANTED &&
            targetTile.getCurrentState() != TileState.WATERED &&
            targetTile.getCurrentState() != TileState.HARVESTABLE) {
            System.out.println("There is no plant to water here.");
            return;
        }

        if (targetTile.isWatered()) {
            System.out.println("This plant is already watered.");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Not enough energy to water!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            targetTile.setWatered(true);
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("You watered the " + targetTile.getPlantedCropType().name().toLowerCase() + ".");
        }
    }

    /**
     * Attempts to harvest crops from a LandTile.
     * @param player The player performing the action.
     * @param targetTile The LandTile to harvest from.
     */
    public void harvest(Player player, LandTile targetTile) {
        // Harvesting is done with bare hands ("Hand" or "No Item")
        IItem activeItem = player.getActiveItem();
        if (!(activeItem.getName().equals("Hand") || activeItem.getName().equals("No Item"))) {
            System.out.println("You need to use your hands to harvest.");
            return;
        }

        if (targetTile.getCurrentState() != TileState.HARVESTABLE) {
            System.out.println("Nothing to harvest yet from this tile.");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Not enough energy to harvest!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            CropItem harvestedCrop = targetTile.harvest();
            if (harvestedCrop != null) {
                player.inventory.addItem((IItem)harvestedCrop);
                gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
                System.out.println("You harvested " + harvestedCrop.getName() + "!");
            } else {
                System.out.println("Harvest failed unexpectedly.");
            }
        }
    }

    /**
     * Updates the growth stage of all planted LandTiles in the game world.
     * This method should be called periodically by GamePanel's update loop.
     */
    public void updatePlantGrowth() {
        for (SuperObj obj : gp.obj) {
            if (obj instanceof LandTile) {
                LandTile tile = (LandTile) obj;
                tile.grow();
            }
        }
    }
}