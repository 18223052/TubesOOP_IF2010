// package main;

// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.ObjectInputStream;
// import java.io.ObjectOutputStream;
// import java.util.ArrayList;
// import java.util.List;

// import object.LandTile;
// import object.TileSaveData;
// import object.SuperObj; // Pastikan SuperObj bisa diakses

// public class SaveLoadManager {
//     GamePanel gp;
//     private final String SAVE_FILE_PATH = "savegame.dat"; // Nama file simpanan

//     public SaveLoadManager(GamePanel gp) {
//         this.gp = gp;
//     }

//     // --- SAVE GAME ---
//     public void saveGame() {
//         try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
//             // 1. Simpan data LandTile
//             List<TileSaveData> tileDataList = new ArrayList<>();
//             for (SuperObj obj : gp.obj) { // Asumsi gp.obj menyimpan semua objek di map
//                 if (obj instanceof LandTile) {
//                     LandTile tile = (LandTile) obj;
//                     // Hanya simpan tile yang statenya berubah dari default LAND
//                     if (tile.getCurrentState() != object.TileState.LAND || tile.getPlantedCropType() != object.PlantType.NONE) {
//                         tileDataList.add(new TileSaveData(
//                             tile.wX / gp.tileSize, // Kolom
//                             tile.wY / gp.tileSize, // Baris
//                             tile.getCurrentState(),
//                             tile.getPlantedCropType(),
//                             tile.getGrowthStage(),
//                             tile.isWatered(),
//                             // Anda perlu menambahkan getter untuk plantedTime di LandTile
//                             // atau memastikan Anda memiliki cara untuk mengambilnya
//                             // Untuk saat ini, asumsikan LandTile.getPlantedTime() ada
//                             tile.getPlantedTime() 
//                         ));
//                     }
//                 }
//             }
//             oos.writeObject(tileDataList); // Tulis list data tile

//             // 2. Simpan data pemain
//             oos.writeObject(gp.player.getEnergy()); // Contoh: simpan energi
//             oos.writeObject(gp.player.getInventory().getAllItems()); // Contoh: simpan inventory (pastikan item Serializable)
//             oos.writeObject(gp.player.getWorldX());
//             oos.writeObject(gp.player.getWorldY());
//             oos.writeObject(gp.player.getCurrentMap()); // Simpan map saat ini
            
//             // 3. Simpan data waktu game
//             oos.writeObject(gp.gameTime.getCurrentGameTime());
//             oos.writeObject(gp.gameTime.getGameDay());
//             oos.writeObject(gp.gameTime.getCurrentSeason());

//             System.out.println("Game saved successfully!");
//         } catch (IOException e) {
//             System.err.println("Error saving game: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     // --- LOAD GAME ---
//     @SuppressWarnings("unchecked") // Untuk mengatasi peringatan unchecked cast
//     public void loadGame() {
//         try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
//             // 1. Muat data LandTile
//             List<TileSaveData> loadedTileData = (List<TileSaveData>) ois.readObject();

//             // Reset semua LandTile ke kondisi default LAND sebelum mengaplikasikan data simpanan
//             for (SuperObj obj : gp.obj) {
//                 if (obj instanceof LandTile) {
//                     LandTile tile = (LandTile) obj;
//                     tile.toLand(); // Reset ke LAND dulu
//                 }
//             }

//             // Aplikasikan data yang dimuat ke LandTile yang ada di gp.obj
//             for (TileSaveData data : loadedTileData) {
//                 for (SuperObj obj : gp.obj) {
//                     if (obj instanceof LandTile) {
//                         LandTile tile = (LandTile) obj;
//                         if (tile.wX / gp.tileSize == data.col && tile.wY / gp.tileSize == data.row) {
//                             // Perbarui state tile
//                             tile.setCurrentState(data.state); // Anda perlu setter untuk currentState
//                             tile.setPlantedCropType(data.cropType); // Setter untuk plantedCropType
//                             tile.setGrowthStage(data.growthStage); // Setter untuk growthStage
//                             tile.setWatered(data.watered);
//                             tile.setPlantedTime(data.plantedTime); // Setter untuk plantedTime
//                             // Panggil updateImage() agar gambar tile diperbarui setelah state diatur
//                             // Anda mungkin perlu membuat updateImage() menjadi public atau membuat metode updater
//                             // Jika updateImage() sudah private, buat public void refreshTileVisual() di LandTile
//                             // yang memanggil updateImage()
//                             // tile.updateImage(); // Asumsi updateImage() bisa diakses atau ada metode refresh
//                             // Atau, Anda bisa membuat metode di LandTile seperti:
//                             tile.applySaveData(data.state, data.cropType, data.growthStage, data.watered, data.plantedTime);
//                             break; // Tile ditemukan, lanjut ke data berikutnya
//                         }
//                     }
//                 }
//             }
//             System.out.println("LandTile states loaded.");

//             // 2. Muat data pemain
//             gp.player.setEnergy((int) ois.readObject()); // Set energy
//             gp.player.getInventory().setAllItems((List<IItem>) ois.readObject()); // Set inventory
//             gp.player.setWX((int) ois.readObject());
//             gp.player.setWorldY((int) ois.readObject());
//             gp.player.setCurrentMap((String) ois.readObject()); // Set current map

//             // 3. Muat data waktu game
//             gp.gameTime.setCurrentGameTime((long) ois.readObject());
//             gp.gameTime.setCurrentDay((int) ois.readObject());
//             gp.gameTime.setCurrentSeason((environment.Season) ois.readObject()); // Sesuaikan tipe data Season

//             System.out.println("Game loaded successfully!");

//         } catch (IOException | ClassNotFoundException e) {
//             System.err.println("Error loading game: " + e.getMessage());
//             e.printStackTrace();
//             // Jika file tidak ditemukan, ini adalah kondisi awal game, tidak perlu panik
//             if (e instanceof java.io.FileNotFoundException) {
//                 System.out.println("No save file found. Starting new game.");
//             }
//         }
//     }
// }