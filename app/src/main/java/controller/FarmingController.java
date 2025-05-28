package controller;

import entity.Player;
import main.GamePanel;
import environment.GameTime;
import object.LandTile;
import object.Pickaxe; 
import object.TileState;
import object.Hoe; 
import object.IItem;
import object.WateringCan; 
import object.SeedItem;
import object.SuperObj;
import object.CropItem; 

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
        System.out.println("Tidak ada item aktif yang dipilih.");
        System.out.println("Farming: tidak ada item aktif.");
        return;
    }

    System.out.println("Farming: Player menggunakan " + activeItem.getName() + " active. Kelas aktual: " + activeItem.getClass().getName() + ". Berinteraksi dengan " + targetTile.name);

    if (activeItem instanceof Hoe) {
        System.out.println("Farming: Terdeteksi Hoe instance."); 
        tillage(player, targetTile);
    } else if (activeItem instanceof Pickaxe) {
        System.out.println("Farming: Terdeteksi Pickaxe instance."); 
        recoverLand(player, targetTile);
    } else if (activeItem instanceof SeedItem) {
        System.out.println("Farming: Terdeteksi SeedItem instance."); 
        plant(player, targetTile, (SeedItem) activeItem);
    } else if (activeItem instanceof WateringCan) {
        System.out.println("Farming: Terdeteksi WateringCan instance."); 
        water(player, targetTile);
    } else if (activeItem.getName().equals("Hand") || activeItem.getName().equals("No Item")) {
        if (targetTile.isHarvestable()) {
            harvest(player, targetTile);
        } else {
            System.out.println("Tidak ada yang bisa dilakukan " + targetTile.name + " menggunakan tangan.");
            System.out.println("Farming: Hand interaction on non-harvestable tile.");
        }
    } else {
        System.out.println("Player tidak bisa menggunakan " + activeItem.getName() + " disini.");
        System.out.println("Farming: Tidak bisa menggunakan " + activeItem.getName() + " di " + targetTile.name);
    }
}
    /**
     * Attempts to till a LandTile into Soil.
     * @param player The player performing the action.
     * @param targetTile The LandTile to till.
     */
    public void tillage(Player player, LandTile targetTile) {
        if (!(player.getActiveItem() instanceof Hoe)) {
            System.out.println("Kamu butuh Hoe untuk menggarap tanah.");
            return;
        }

        if (targetTile.getCurrentState() != TileState.LAND) {
            System.out.println("Tile ini bukan tanah polos.");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Energi tidak cukup untuk menggarap tanah!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            targetTile.toSoil();
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("Kamu berhasil menggarap tanah.");
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
            System.out.println("Kamu butuh pickaxe untuk memulihkan tanah.");
            return;
        }

        // Recovery can be done on soil, planted, watered, or harvestable tile
        if (targetTile.getCurrentState() == TileState.LAND) {
            System.out.println("Tile ini merupakan tanah polos.");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Energi tidak cukup untuk memulihkan tanah!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            targetTile.toLand();
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("Kamu berhasil memulihkan tanah.");
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
            System.out.println("Pilih benih yang tepat untuk bercocok tanam.");
            return;
        }

        if (targetTile.getCurrentState() != TileState.SOIL) {
            System.out.println("Kamu bisa menanam hanya di tanah yang sudah digarap.");
            return;
        }

        if (!player.getInventory().hasItem(seed.getName())) {
            System.out.println("Kamu tidak memiliki benih ini!");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Energi tidak cukup untuk menanam!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            if (targetTile.plant(seed.getCropType())) {
                player.getInventory().removeItems(seed.getName(), 1);
                gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
                System.out.println("Kamu menanam sebuah " + seed.getName() + ".");
            } else {

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
            System.out.println("Kamu butuh penyiram tanaman untuk menyiram tanaman.");
            return;
        }

        if (targetTile.getCurrentState() != TileState.PLANTED &&
            targetTile.getCurrentState() != TileState.WATERED &&
            targetTile.getCurrentState() != TileState.HARVESTABLE) {
            System.out.println("Tidak ada tanaman yang perlu disiram disini.");
            return;
        }

        if (targetTile.isWatered()) {
            System.out.println("Tanaman sudah disiram.");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Energi tidak cukup untuk menyiram tanaman!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            targetTile.setWatered(true);
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("Kamu berhasil menyiram tanaman " + targetTile.getPlantedCropType().name().toLowerCase() + ".");
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
            System.out.println("Kamu membutuhkan tangan untuk memanen tanaman.");
            return;
        }

        if (targetTile.getCurrentState() != TileState.HARVESTABLE) {
            System.out.println("Tidak ada yang bisa dipanen di sini.");
            return;
        }

        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            System.out.println("Energi tidak cukup untuk memanen!");
            return;
        }

        if (player.deductEnergy(ENERGY_COST_PER_TILE)) {
            CropItem harvestedCrop = targetTile.harvest();
            if (harvestedCrop != null) {
                player.inventory.addItem((IItem)harvestedCrop);
                gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
                System.out.println("Kamu berhasil memanen " + harvestedCrop.getName() + "!");
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