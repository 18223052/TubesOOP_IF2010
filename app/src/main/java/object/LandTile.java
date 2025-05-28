package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import entity.Player;
import main.GamePanel;

public class LandTile extends SuperObj {
    private TileState currentState;
    private PlantType plantedCropType; 
    private int growthStage; 
    public long lastWateredTime; 
    public boolean isWatered; 
    private long plantedTime; 

    private BufferedImage landImg;
    private BufferedImage soilImg;

    private Map<PlantType, BufferedImage[]> plantedImagesMap;
    private Map<PlantType, BufferedImage[]> wateredImagesMap;

    public LandTile(GamePanel gp){
        super(gp);
        name = "Land Tile";
        currentState = TileState.LAND;
        plantedCropType = PlantType.NONE;
        plantedCropType = PlantType.NONE;
        isWatered = false;
        collision = false;

        plantedImagesMap = new HashMap<>();
        wateredImagesMap = new HashMap<>();

        loadImages();
        updateImage();

    }

    private void loadImages(){
        try{
            landImg = setup("/tutor_tiles/017");
            soilImg = setup("/tutor_tiles/204");

            BufferedImage[] tomatoPlanted = new BufferedImage[3];
            tomatoPlanted[0] = setup("/seeds/awal");
            tomatoPlanted[1] = setup("/seeds/ketiga");
            tomatoPlanted[2] = setup("/seeds/kedua");
            plantedImagesMap.put(PlantType.TOMATO, tomatoPlanted);

            BufferedImage[] tomatoWatered = new BufferedImage[3];
            tomatoWatered[0] = setup("/seeds/awal");
            tomatoWatered[1] = setup("/seeds/ketiga");
            tomatoWatered[2] = setup("/seeds/kedua");
            wateredImagesMap.put(PlantType.TOMATO, tomatoWatered);

        } catch (Exception e){
            System.err.println("Error loading LandTile images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public TileState getCurrentState() { return currentState; }
    public PlantType getPlantedCropType() { return plantedCropType; }
    public int getGrowthStage() { return growthStage; }
    public boolean isWatered() { return isWatered; }
    public boolean isHarvestable() { return currentState == TileState.HARVESTABLE; }

    public void setCurrentState(TileState newState) { this.currentState = newState; }
    public void setPlantedCropType(PlantType newType) { this.plantedCropType = newType; }
    public void setGrowthStage(int newGrowth) { this.growthStage = newGrowth; }



    public void setWatered(boolean watered) {
        isWatered = watered;
        lastWateredTime = gp.gameTime.getCurrentGameTime(); 
        updateImage();
    }

    public void toSoil() {
        this.currentState = TileState.SOIL;
        this.plantedCropType = PlantType.NONE;
        this.growthStage = 0;
        this.isWatered = false;
        System.out.println("Tile changed to SOIL at " + wX/gp.tileSize + "," + wY/gp.tileSize);
        updateImage(); 
    }

    public void toLand() {
        // Ini adalah aksi "Recover Land"
        this.currentState = TileState.LAND;
        this.plantedCropType = PlantType.NONE;
        this.growthStage = 0;
        this.isWatered = false;
        System.out.println("Tile changed to LAND at " + wX/gp.tileSize + "," + wY/gp.tileSize);
        updateImage(); 
    }

    public boolean plant(String cropTypeString) {
        PlantType type;
        try {
            type = PlantType.valueOf(cropTypeString.toUpperCase().replace(" ", "_")); 
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid crop type string: " + cropTypeString + ". Ensure it matches a PlantType enum name.");
            return false;
        }
        
        // Cek Musim
        if (!type.canGrowInSeason(gp.gameTime.getCurrentSeason())) {
            System.out.println("Cannot plant " + type.name() + " in " + gp.gameTime.getCurrentSeason() + " season.");
            gp.ui.setDialog("It's not the right season to plant " + type.name() + "!");
            return false;
        }

        if (this.currentState == TileState.SOIL && type != PlantType.NONE) {
            this.plantedCropType = type;
            this.currentState = TileState.PLANTED;
            this.growthStage = 0; 
            this.isWatered = false; 
            this.plantedTime = gp.gameTime.getCurrentGameTime(); 
            System.out.println("Planted " + type.name() + " at " + wX/gp.tileSize + "," + wY/gp.tileSize);
            updateImage();
            return true;
        }
        System.out.println("Cannot plant here. Tile state: " + currentState);
        return false;
    }

    public void grow() {
        if (currentState == TileState.PLANTED || currentState == TileState.WATERED) {
            if (plantedCropType == PlantType.NONE) {
                return; 
            }

            long timeElapsed = gp.gameTime.getCurrentGameTime() - plantedTime;
            
            // Ambil durasi pertumbuhan dari PlantType
            long growthDurationStage1 = plantedCropType.getGrowthTimeStage1Millis();
            long growthDurationStage2 = plantedCropType.getGrowthTimeStage2Millis();

            // Logika pertumbuhan berdasarkan waktu dan PlantType
            if (growthStage < 2 && timeElapsed >= growthDurationStage2) {
                growthStage = 2; // Siap panen
                currentState = TileState.HARVESTABLE;
                System.out.println(plantedCropType.name() + " at " + wX/gp.tileSize + "," + wY/gp.tileSize + " is HARVESTABLE!");
                updateImage();
            } else if (growthStage < 1 && timeElapsed >= growthDurationStage1) {
                growthStage = 1; // Tumbuh muda
                System.out.println(plantedCropType.name() + " at " + wX/gp.tileSize + "," + wY/gp.tileSize + " is growing.");
                updateImage();
            }
        }
    }

    public CropItem harvest() {
        if (this.currentState == TileState.HARVESTABLE) {
            String cropNameForFactory = plantedCropType.name().replace("_", " ");
            CropItem harvestedCrop = gp.itemFactory.createCrop(cropNameForFactory); 
            
            this.currentState = TileState.SOIL; // Setelah panen, kembali ke tanah gembur
            this.plantedCropType = PlantType.NONE;
            this.growthStage = 0;
            this.isWatered = false;
            System.out.println("Harvested " + harvestedCrop.getName() + " from " + wX/gp.tileSize + "," + wY/gp.tileSize);
            updateImage();
            return harvestedCrop;
        }
        System.out.println("Nothing to harvest here.");
        return null;
    }


    @Override
    public void onInteract(GamePanel gp, Player player) {
        System.out.println("Interacting with LandTile at " + wX/gp.tileSize + "," + wY/gp.tileSize);
        gp.farmingController.handleTileInteraction(player, this);
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        super.draw(g2, gp);
    }

    public void updateImage() {
        switch (currentState) {
            case LAND:
                img = landImg;
                break;
            case SOIL:
                img = soilImg;
                break;
            case PLANTED:
            case WATERED: 
            case HARVESTABLE:
                if (plantedCropType != PlantType.NONE) {
                    BufferedImage[] imagesToUse;
                    if (isWatered) {
                        imagesToUse = wateredImagesMap.get(plantedCropType);
                    } else {
                        imagesToUse = plantedImagesMap.get(plantedCropType);
                    }

                    if (imagesToUse != null && growthStage < imagesToUse.length) {
                        img = imagesToUse[growthStage];
                    } else {
                        System.err.println("Warning: Image not found for " + plantedCropType + " at stage " + growthStage + ". Using soilImg.");
                        img = soilImg; 
                    }
                } else {
                    img = soilImg;
                }
                break;
        }
    }

    public long getLastWateredTime(){
        return lastWateredTime;
    }

    public void setLastWateredTime(long newLastWatered){
        this.lastWateredTime = newLastWatered;
    }

    public long getPlantedTime(){
        return plantedTime;
    }

    public void setPlantedTime(long newPlantedTime){
        this.plantedTime = newPlantedTime;
    }
    
}
