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
    private long lastGrowthUpdateTime;

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
        lastWateredTime = -1;
        plantedTime = -1;
        lastGrowthUpdateTime = gp.gameTime.getCurrentGameTime();

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
        this.lastWateredTime = -1;
        this.plantedTime = -1;
        System.out.println("Tile changed to SOIL at " + wX/gp.tileSize + "," + wY/gp.tileSize);
        updateImage(); 
    }

    public void toLand() {
        // Ini adalah aksi "Recover Land"
        this.currentState = TileState.LAND;
        this.plantedCropType = PlantType.NONE;
        this.growthStage = 0;
        this.isWatered = false;
        this.lastWateredTime = -1;
        this.plantedTime = -1;
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
            this.isWatered = true; 
            this.plantedTime = gp.gameTime.getCurrentGameTime();
            this.lastWateredTime = gp.gameTime.getCurrentGameTime();
            System.out.println("Planted " + type.name() + " at " + wX/gp.tileSize + "," + wY/gp.tileSize);
            updateImage();
            return true;
        }
        System.out.println("Cannot plant here. Tile state: " + currentState);
        return false;
    }

    public void grow() {
        if (plantedCropType == PlantType.NONE || currentState == TileState.LAND || currentState == TileState.SOIL) {
            return;
        }

        // This wilting logic is correct IF isWatered is managed externally
        if (!isWatered && plantedTime != -1) {
            long timeSinceLastWatered = gp.gameTime.getCurrentGameTime() - lastWateredTime;
            if (timeSinceLastWatered >= plantedCropType.getWiltingTimeMillis()) {
                System.out.println(plantedCropType.name() + " mati");
                gp.ui.setDialog("Your " + plantedCropType.name() + " wilted!"); // Added UI dialog
                toSoil();
                return;
            }
        }
        
        long timeElapsed = gp.gameTime.getCurrentGameTime() - plantedTime;

        long growthDurationStage1 = plantedCropType.getGrowthTimeStage1Millis();
        long growthDurationStage2 = plantedCropType.getGrowthTimeStage2Millis();

        // Plants only grow if they are currently watered
        if (isWatered) { // Changed from `isWatered || currentState == TileState.PLANTED`
            if (growthStage < 2 && timeElapsed >= growthDurationStage2) {
                growthStage = 2;
                currentState = TileState.HARVESTABLE;
                System.out.println(plantedCropType.name() + " siap dipanen!");
                updateImage();
            } else if (growthStage < 1 && timeElapsed >= growthDurationStage1) {
                growthStage = 1;
                System.out.println(plantedCropType.name() + " sedang bertumbuh!");
                updateImage();
            }
        } else {
            // If not watered, the plant does not grow, but it can still wilt over time.
        }
    }

    public CropItem harvest() {
        if (this.currentState == TileState.HARVESTABLE) {
            String cropNameForFactory = plantedCropType.name().replace("_", " ");
            CropItem harvestedCrop = gp.itemFactory.createCrop(cropNameForFactory); 
            
            this.currentState = TileState.SOIL;
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

    public void setLastGrowthUpdateTime(long time) {
        this.lastGrowthUpdateTime = time;
    }
    
}
