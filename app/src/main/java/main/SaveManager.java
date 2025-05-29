package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import object.LandTile;
import object.LandTileData;
import object.SuperObj;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class SaveManager {

    private GamePanel gp;
    private Gson gson;
    private Set<String> activeSaveFiles; 
    private Thread shutdownHook; 

    public SaveManager(GamePanel gp) {
        this.gp = gp;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.activeSaveFiles = new HashSet<>();
        

        setupShutdownHook();
    }

    /**
     * Setup shutdown hook yang akan menghapus semua save files saat program dihentikan
     */
    private void setupShutdownHook() {
        shutdownHook = new Thread(() -> {
            System.out.println("=== SHUTDOWN HOOK TRIGGERED ===");
            System.out.println("Program sedang dihentikan, menghapus save files...");
            
            for (String saveFilePath : activeSaveFiles) {
                File saveFile = new File(saveFilePath);
                if (saveFile.exists()) {
                    boolean deleted = saveFile.delete();
                    System.out.println("Menghapus file: " + saveFilePath + " - " + 
                                     (deleted ? "BERHASIL" : "GAGAL"));
                }
            }
            
            File saveDir = new File("saves");
            if (saveDir.exists() && saveDir.isDirectory()) {
                String[] files = saveDir.list();
                if (files == null || files.length == 0) {
                    boolean deleted = saveDir.delete();
                    System.out.println("Menghapus direktori saves: " + 
                                     (deleted ? "BERHASIL" : "GAGAL"));
                }
            }
            
            System.out.println("=== CLEANUP SELESAI ===");
        });
        

        Runtime.getRuntime().addShutdownHook(shutdownHook);
        System.out.println("DEBUG: Shutdown hook telah didaftarkan");
    }

    private String getSaveFilePath(String mapName) {
        String cleanMapName = mapName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String fileName = cleanMapName + "_land_tiles.json";
        return "saves/" + fileName;
    }

    /**
     * Menyimpan state semua LandTile dari current map ke file JSON.
     */
    public void saveGameState() {
        String currentMapFilePath = gp.currMap;
        String saveFilePath = getSaveFilePath(currentMapFilePath);

        System.out.println("=== SAVE DEBUG START ===");
        System.out.println("DEBUG: Menyimpan state untuk map: " + currentMapFilePath);
        System.out.println("DEBUG: Save file path: " + saveFilePath);
        System.out.println("DEBUG: Jumlah total objek di GamePanel.obj: " + gp.obj.size());

        List<LandTileData> allLandTileData = new ArrayList<>();

        for (SuperObj obj : gp.obj) {
            if (obj instanceof LandTile) {
                LandTile tile = (LandTile) obj;
                System.out.println("DEBUG: Menyimpan LandTile di wX=" + tile.wX + ", wY=" + tile.wY +
                                 ", State=" + tile.getCurrentState() + ", Crop=" + tile.getPlantedCropType());
               
                LandTileData data = new LandTileData(
                    tile.wX,
                    tile.wY,
                    tile.getCurrentState(),
                    tile.getPlantedCropType(),
                    tile.getGrowthStage(),
                    tile.getLastWateredTime(),
                    tile.isWatered(),
                    tile.getPlantedTime()
                );
                allLandTileData.add(data);
            }
        }

        System.out.println("DEBUG: Jumlah LandTile yang dikumpulkan untuk disimpan: " + allLandTileData.size());

        try {
            File saveDir = new File("saves");
            if (!saveDir.exists()) {
                boolean created = saveDir.mkdirs();
                System.out.println("DEBUG: Created saves directory: " + created);
            }

            try (FileWriter writer = new FileWriter(saveFilePath)) {
                gson.toJson(allLandTileData, writer);
                writer.flush();
                System.out.println("DEBUG: Game saved successfully to " + new File(saveFilePath).getAbsolutePath());
               
                activeSaveFiles.add(saveFilePath);
                System.out.println("DEBUG: File ditambahkan ke tracking list: " + saveFilePath);
               
                File savedFile = new File(saveFilePath);
                System.out.println("DEBUG: File exists after save: " + savedFile.exists());
                System.out.println("DEBUG: File size: " + savedFile.length() + " bytes");
            }
        } catch (IOException e) {
            System.err.println("ERROR: Error saving game state for map " + currentMapFilePath + ": " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== SAVE DEBUG END ===");
    }

    /**
     * Memuat state semua LandTile untuk current map dari file JSON.
     */
    public void loadGameState() {
        String currentMapFilePath = gp.currMap;
        String saveFilePath = getSaveFilePath(currentMapFilePath);

        System.out.println("=== LOAD DEBUG START ===");
        System.out.println("DEBUG: Memuat state untuk map: " + currentMapFilePath);
        System.out.println("DEBUG: Load file path: " + saveFilePath);
       
        File saveFile = new File(saveFilePath);
        System.out.println("DEBUG: File exists: " + saveFile.exists());
        if (saveFile.exists()) {
            System.out.println("DEBUG: File size: " + saveFile.length() + " bytes");
            activeSaveFiles.add(saveFilePath);
        }

        try (FileReader reader = new FileReader(saveFilePath)) {
            Type listType = new TypeToken<ArrayList<LandTileData>>() {}.getType();
            List<LandTileData> loadedLandTileData = gson.fromJson(reader, listType);

            if (loadedLandTileData == null || loadedLandTileData.isEmpty()) {
                System.out.println("DEBUG: No saved LandTile data found for map " + currentMapFilePath + ". Using default states.");
                System.out.println("=== LOAD DEBUG END ===");
                return;
            }

            System.out.println("DEBUG: Loaded " + loadedLandTileData.size() + " LandTile data entries");
            System.out.println("DEBUG: Current GamePanel.obj size: " + gp.obj.size());

            int tilesUpdated = 0;
            int tilesNotFound = 0;
           
            for (LandTileData data : loadedLandTileData) {
                System.out.println("LOAD_DEBUG: Mencoba memuat data untuk Map: " + currentMapFilePath + " | Data wX: " + data.worldX + ", Data wY: " + data.worldY + ", Data State: " + data.currentState);
                LandTile targetTile = findLandTileByCoordinates(data.worldX, data.worldY);
                if (targetTile != null) {
                    Object oldState = targetTile.getCurrentState();
                   
                    targetTile.setCurrentState(data.currentState);
                    targetTile.setPlantedCropType(data.plantedCropType);
                    targetTile.setGrowthStage(data.growthStage);
                    targetTile.lastWateredTime = data.lastWateredTime;
                    targetTile.isWatered = data.isWatered;
                    targetTile.setPlantedTime(data.plantedTime);
                    targetTile.updateImage();

                    System.out.println("LOAD_DEBUG: State SETELAH update: " + targetTile.getCurrentState());
                   
                    tilesUpdated++;
                    System.out.println("DEBUG: Updated LandTile at " + data.worldX + "," + data.worldY +
                                     " from " + oldState + " to " + data.currentState);
                } else {
                    tilesNotFound++;
                    System.err.println("WARNING: Could not find LandTile at " + data.worldX + "," + data.worldY);
                }
            }
           
            System.out.println("DEBUG: Load summary - Updated: " + tilesUpdated + ", Not found: " + tilesNotFound);
            System.out.println("DEBUG: Game loaded successfully from " + saveFilePath);
           
        } catch (IOException e) {
            System.out.println("DEBUG: Save file not found for map " + currentMapFilePath + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR: Unexpected error during game loading for map " + currentMapFilePath + ": " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== LOAD DEBUG END ===");
    }

    private LandTile findLandTileByCoordinates(int worldX, int worldY) {
        for (SuperObj obj : gp.obj) {
            if (obj instanceof LandTile) {
                LandTile tile = (LandTile) obj;
                if (tile.wX == worldX && tile.wY == worldY) {
                    return tile;
                }
            }
        }
        return null;
    }

    /**
     * Method untuk menghapus save files secara manual jika diperlukan
     */
    public void clearAllSaveFiles() {
        System.out.println("=== MANUAL CLEANUP START ===");
        for (String saveFilePath : new HashSet<>(activeSaveFiles)) {
            File saveFile = new File(saveFilePath);
            if (saveFile.exists()) {
                boolean deleted = saveFile.delete();
                System.out.println("Manual delete: " + saveFilePath + " - " + 
                                 (deleted ? "BERHASIL" : "GAGAL"));
                if (deleted) {
                    activeSaveFiles.remove(saveFilePath);
                }
            }
        }
        System.out.println("=== MANUAL CLEANUP END ===");
    }

    /**
     * Method untuk disable shutdown hook jika diperlukan (misal saat normal exit)
     */
    public void disableAutoDelete() {
        try {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
            System.out.println("DEBUG: Shutdown hook telah dinonaktifkan");
        } catch (IllegalStateException e) {
            System.out.println("DEBUG: Shutdown hook tidak dapat dinonaktifkan: " + e.getMessage());
        }
    }
}