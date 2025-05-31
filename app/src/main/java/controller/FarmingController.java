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


    public void handleTileInteraction(Player player, LandTile targetTile) {
        IItem activeItem = player.getActiveItem();

        if (activeItem == null) {
            System.out.println("Tidak ada item aktif yang dipilih.");
            gp.ui.setDialog("Tidak ada item aktif yang dipilih.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            System.out.println("Farming: tidak ada item aktif.");
            return;
        }

        System.out.println("Farming: Player menggunakan " + activeItem.getName() + " active. Kelas aktual: " + activeItem.getClass().getName() + ". Berinteraksi dengan " + targetTile.name);

        if (player.getEnergy() <= Player.MIN_ENERGY_BEFORE_SLEEP) {
            System.out.println("Energi terlalu rendah (" + player.getEnergy() + "). Player harus tidur.");
            gp.ui.setDialog("Energi habis! Kamu harus tidur.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
           
            if (gp.gameState != GamePanel.sleepState) { 
                gp.sleepController.forceSleep(); 
            }
            return;
        }


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
                gp.ui.setDialog("Tidak ada yang bisa dilakukan " + targetTile.name + " menggunakan tangan.");
                gp.setGameState(GamePanel.dialogState);
                gp.repaint();
                System.out.println("Farming: Hand interaction on non-harvestable tile.");
            }
        } else {
            System.out.println("Player tidak bisa menggunakan " + activeItem.getName() + " disini.");
            gp.ui.setDialog("Player tidak bisa menggunakan " + activeItem.getName() + " disini.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            System.out.println("Farming: Tidak bisa menggunakan " + activeItem.getName() + " di " + targetTile.name);
        }
    }


    public void tillage(Player player, LandTile targetTile) {
        if (!(player.getActiveItem() instanceof Hoe)) {
            System.out.println("Kamu butuh Hoe untuk menggarap tanah.");
            gp.ui.setDialog("Kamu butuh Hoe untuk menggarap tanah.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (targetTile.getCurrentState() != TileState.LAND) {
            System.out.println("Tile ini bukan tanah polos.");
            gp.ui.setDialog("Tile ini bukan tanah polos.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (player.getEnergy() - ENERGY_COST_PER_TILE < Player.MIN_ENERGY_BEFORE_SLEEP) {
             System.out.println("Energi tidak cukup untuk menggarap tanah tanpa otomatis tidur! Energi tersisa: " + player.getEnergy());
             gp.ui.setDialog("Energi tidak cukup untuk menggarap tanah!");
             gp.setGameState(GamePanel.dialogState);
             gp.repaint();
             return;
        }

        player.deductEnergy(ENERGY_COST_PER_TILE);

        if (gp.gameState != GamePanel.sleepState) {
            targetTile.toSoil();
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("Kamu berhasil menggarap tanah.");
            gp.saveGame();
        } else {
            System.out.println("Aksi menggarap tanah dibatalkan karena pemain harus tidur.");
        }
    }


    public void recoverLand(Player player, LandTile targetTile) {
        if (!(player.getActiveItem() instanceof Pickaxe)) {
            System.out.println("Kamu butuh pickaxe untuk memulihkan tanah.");
            gp.ui.setDialog("Kamu butuh pickaxe untuk memulihkan tanah.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (targetTile.getCurrentState() == TileState.LAND) {
            System.out.println("Tile ini merupakan tanah polos.");
            gp.ui.setDialog("Tile ini merupakan tanah polos.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (player.getEnergy() - ENERGY_COST_PER_TILE < Player.MIN_ENERGY_BEFORE_SLEEP) {
            System.out.println("Energi tidak cukup untuk memulihkan tanah tanpa otomatis tidur! Energi tersisa: " + player.getEnergy());
            gp.ui.setDialog("Energi tidak cukup untuk memulihkan tanah!");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        player.deductEnergy(ENERGY_COST_PER_TILE);
        if (gp.gameState != GamePanel.sleepState) {
            targetTile.toLand();
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("Kamu berhasil memulihkan tanah.");
        } else {
            System.out.println("Aksi memulihkan tanah dibatalkan karena pemain harus tidur.");
        }
    }


    public void plant(Player player, LandTile targetTile, SeedItem seed) {
        if (!(player.getActiveItem() instanceof SeedItem) ||
             !((SeedItem)player.getActiveItem()).getCropType().equals(seed.getCropType())) {
            System.out.println("Pilih benih yang tepat untuk bercocok tanam.");
            gp.ui.setDialog("Pilih benih yang tepat untuk bercocok tanam.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (targetTile.getCurrentState() != TileState.SOIL) {
            System.out.println("Kamu bisa menanam hanya di tanah yang sudah digarap.");
            gp.ui.setDialog("Kamu bisa menanam hanya di tanah yang sudah digarap.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (!player.getInventory().hasItem(seed.getName())) {
            System.out.println("Kamu tidak memiliki benih ini!");
            gp.ui.setDialog("Kamu tidak memiliki benih ini!");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (player.getEnergy() - ENERGY_COST_PER_TILE < Player.MIN_ENERGY_BEFORE_SLEEP) {
            System.out.println("Energi tidak cukup untuk menanam tanpa otomatis tidur! Energi tersisa: " + player.getEnergy());
            gp.ui.setDialog("Energi tidak cukup untuk menanam!");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        player.deductEnergy(ENERGY_COST_PER_TILE);
        if (gp.gameState != GamePanel.sleepState) {
            if (targetTile.plant(seed.getCropType())) {
                player.getInventory().removeItems(seed.getName(), 1);
                gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
                System.out.println("Kamu menanam sebuah " + seed.getName() + ".");
            } else {
                System.out.println("Gagal menanam tanaman pada tile.");
                gp.ui.setDialog("Gagal menanam tanaman.");
                gp.setGameState(GamePanel.dialogState);
                gp.repaint();
            }
        } else {
            System.out.println("Aksi menanam dibatalkan karena pemain harus tidur.");
        }
    }


    public void water(Player player, LandTile targetTile) {
        if (!(player.getActiveItem() instanceof WateringCan)) {
            System.out.println("Kamu butuh penyiram tanaman untuk menyiram tanaman.");
            gp.ui.setDialog("Kamu butuh penyiram tanaman untuk menyiram tanaman.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (targetTile.getCurrentState() != TileState.PLANTED &&
            targetTile.getCurrentState() != TileState.WATERED &&
            targetTile.getCurrentState() != TileState.HARVESTABLE) {
            System.out.println("Tidak ada tanaman yang perlu disiram disini.");
            gp.ui.setDialog("Tidak ada tanaman yang perlu disiram disini.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (targetTile.isWatered()) {
            System.out.println("Tanaman sudah disiram.");
            gp.ui.setDialog("Tanaman sudah disiram.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (player.getEnergy() - ENERGY_COST_PER_TILE < Player.MIN_ENERGY_BEFORE_SLEEP) {
            System.out.println("Energi tidak cukup untuk menyiram tanaman tanpa otomatis tidur! Energi tersisa: " + player.getEnergy());
            gp.ui.setDialog("Energi tidak cukup untuk menyiram tanaman!");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        player.deductEnergy(ENERGY_COST_PER_TILE);
        if (gp.gameState != GamePanel.sleepState) {
            targetTile.setWatered(true);
            gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
            System.out.println("Kamu berhasil menyiram tanaman " + targetTile.getPlantedCropType().name().toLowerCase() + ".");
        } else {
            System.out.println("Aksi menyiram dibatalkan karena pemain harus tidur.");
        }
    }


    public void harvest(Player player, LandTile targetTile) {
        IItem activeItem = player.getActiveItem();
        if (!(activeItem.getName().equals("Hand") || activeItem.getName().equals("No Item"))) {
            System.out.println("Kamu membutuhkan tangan untuk memanen tanaman.");
            gp.ui.setDialog("Kamu membutuhkan tangan untuk memanen tanaman.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (targetTile.getCurrentState() != TileState.HARVESTABLE) {
            System.out.println("Tidak ada yang bisa dipanen di sini.");
            gp.ui.setDialog("Tidak ada yang bisa dipanen di sini.");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        if (player.getEnergy() - ENERGY_COST_PER_TILE < Player.MIN_ENERGY_BEFORE_SLEEP) {
            System.out.println("Energi tidak cukup untuk memanen tanpa otomatis tidur! Energi tersisa: " + player.getEnergy());
            gp.ui.setDialog("Energi tidak cukup untuk memanen!");
            gp.setGameState(GamePanel.dialogState);
            gp.repaint();
            return;
        }

        player.deductEnergy(ENERGY_COST_PER_TILE);
        if (gp.gameState != GamePanel.sleepState) {
            CropItem harvestedCrop = targetTile.harvest();
            if (harvestedCrop != null) {
                player.inventory.addItem((IItem)harvestedCrop);
                gameTime.addTime(TIME_COST_PER_TILE_MINUTES);
                System.out.println("Kamu berhasil memanen " + harvestedCrop.getName() + "!");
                player.setHasHarvested(true);
                player.incrementHarvestCount();
            } else {
                System.out.println("Panen gagal secara tidak terduga.");
                gp.ui.setDialog("Panen gagal secara tidak terduga.");
                gp.setGameState(GamePanel.dialogState);
                gp.repaint();
            }
        } else {
            System.out.println("Aksi memanen dibatalkan karena pemain harus tidur.");
        }
    }


    public void updatePlantGrowth() {
        for (SuperObj obj : gp.obj) {
            if (obj instanceof LandTile) {
                LandTile tile = (LandTile) obj;
                tile.grow();
            }
        }
    }
}