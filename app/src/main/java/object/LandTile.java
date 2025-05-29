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

        // TOMATO
        BufferedImage[] tomatoPlanted = new BufferedImage[3];
        tomatoPlanted[0] = setup("/seeds/awal");
        tomatoPlanted[1] = setup("/seeds/ketiga");
        tomatoPlanted[2] = setup("/seeds/kedua");
        plantedImagesMap.put(PlantType.TOMATO, tomatoPlanted);

        BufferedImage[] tomatoWatered = new BufferedImage[3];
        tomatoWatered[0] = setup("/seeds/awal_basah");
        tomatoWatered[1] = setup("/seeds/ketiga_basah");
        tomatoWatered[2] = setup("/seeds/kedua_basah");
        wateredImagesMap.put(PlantType.TOMATO, tomatoWatered);

        // // POTATO
        // BufferedImage[] potatoPlanted = new BufferedImage[3];
        // potatoPlanted[0] = setup("/seeds/awal");
        // potatoPlanted[1] = setup("/seeds/ketiga");
        // potatoPlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.POTATO, potatoPlanted);

        // BufferedImage[] potatoWatered = new BufferedImage[3];
        // potatoWatered[0] = setup("/seeds/awal_basah");
        // potatoWatered[1] = setup("/seeds/ketiga_basah");
        // potatoWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.POTATO, potatoWatered);

        // // PARSNIP
        // BufferedImage[] parsnipPlanted = new BufferedImage[3];
        // parsnipPlanted[0] = setup("/seeds/awal");
        // parsnipPlanted[1] = setup("/seeds/ketiga");
        // parsnipPlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.PARSNIP, parsnipPlanted);

        // BufferedImage[] parsnipWatered = new BufferedImage[3];
        // parsnipWatered[0] = setup("/seeds/awal_basah");
        // parsnipWatered[1] = setup("/seeds/ketiga_basah");
        // parsnipWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.PARSNIP, parsnipWatered);

        // // CAULIFLOWER
        // BufferedImage[] cauliflowerPlanted = new BufferedImage[3];
        // cauliflowerPlanted[0] = setup("/seeds/awal");
        // cauliflowerPlanted[1] = setup("/seeds/ketiga");
        // cauliflowerPlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.CAULIFLOWER, cauliflowerPlanted);

        // BufferedImage[] cauliflowerWatered = new BufferedImage[3];
        // cauliflowerWatered[0] = setup("/seeds/awal_basah");
        // cauliflowerWatered[1] = setup("/seeds/ketiga_basah");
        // cauliflowerWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.CAULIFLOWER, cauliflowerWatered);

        // // WHEAT
        // BufferedImage[] wheatPlanted = new BufferedImage[3];
        // wheatPlanted[0] = setup("/seeds/awal");
        // wheatPlanted[1] = setup("/seeds/ketiga");
        // wheatPlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.WHEAT, wheatPlanted);

        // BufferedImage[] wheatWatered = new BufferedImage[3];
        // wheatWatered[0] = setup("/seeds/awal_basah");
        // wheatWatered[1] = setup("/seeds/ketiga_basah");
        // wheatWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.WHEAT, wheatWatered);

        // // BLUEBERRY
        // BufferedImage[] blueberryPlanted = new BufferedImage[3];
        // blueberryPlanted[0] = setup("/seeds/awal");
        // blueberryPlanted[1] = setup(" /seeds/ketiga");
        // blueberryPlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.BLUEBERRY, blueberryPlanted);

        // BufferedImage[] blueberryWatered = new BufferedImage[3];
        // blueberryWatered[0] = setup("/seeds/awal_basah");
        // blueberryWatered[1] = setup("/seeds/ketiga_basah");
        // blueberryWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.BLUEBERRY, blueberryWatered);

        // HOT_PEPPER
        BufferedImage[] hotPepperPlanted = new BufferedImage[3];
        hotPepperPlanted[0] = setup("/seeds/hotpepper_awal");
        hotPepperPlanted[1] = setup("/seeds/hotpepper_kedua");
        hotPepperPlanted[2] = setup("/seeds/hotpepper_ketiga");
        plantedImagesMap.put(PlantType.HOT_PEPPER, hotPepperPlanted);

        BufferedImage[] hotPepperWatered = new BufferedImage[3];
        hotPepperWatered[0] = setup("/seeds/hotpepper_awal_basah");
        hotPepperWatered[1] = setup("/seeds/hotpepper_kedua_basah");
        hotPepperWatered[2] = setup("/seeds/hotpepper_ketiga_basah");
        wateredImagesMap.put(PlantType.HOT_PEPPER, hotPepperWatered);

        // // MELON
        // BufferedImage[] melonPlanted = new BufferedImage[3];
        // melonPlanted[0] = setup("/seeds/awal");
        // melonPlanted[1] = setup("/seeds/ketiga");
        // melonPlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.MELON, melonPlanted);

        // BufferedImage[] melonWatered = new BufferedImage[3];
        // melonWatered[0] = setup("/seeds/awal_basah");
        // melonWatered[1] = setup("/seeds/ketiga_basah");
        // melonWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.MELON, melonWatered);

        // // CRANBERRY
        // BufferedImage[] cranberryPlanted = new BufferedImage[3];
        // cranberryPlanted[0] = setup("/seeds/awal");
        // cranberryPlanted[1] = setup("/seeds/ketiga");
        // cranberryPlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.CRANBERRY, cranberryPlanted);

        // BufferedImage[] cranberryWatered = new BufferedImage[3];
        // cranberryWatered[0] = setup("/seeds/awal_basah");
        // cranberryWatered[1] = setup("/seeds/ketiga_basah");
        // cranberryWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.CRANBERRY, cranberryWatered);

        // // PUMPKIN
        // BufferedImage[] pumpkinPlanted = new BufferedImage[3];
        // pumpkinPlanted[0] = setup("/seeds/awal");
        // pumpkinPlanted[1] = setup("/seeds/ketiga");
        // pumpkinPlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.PUMPKIN, pumpkinPlanted);

        // BufferedImage[] pumpkinWatered = new BufferedImage[3];
        // pumpkinWatered[0] = setup("/seeds/awal_basah");
        // pumpkinWatered[1] = setup("/seeds/ketiga_basah");
        // pumpkinWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.PUMPKIN, pumpkinWatered);

        // // GRAPE
        // BufferedImage[] grapePlanted = new BufferedImage[3];
        // grapePlanted[0] = setup("/seeds/awal");
        // grapePlanted[1] = setup("/seeds/ketiga");
        // grapePlanted[2] = setup("/seeds/kedua");
        // plantedImagesMap.put(PlantType.GRAPE, grapePlanted);

        // BufferedImage[] grapeWatered = new BufferedImage[3];
        // grapeWatered[0] = setup("/seeds/awal_basah");
        // grapeWatered[1] = setup("/seeds/ketiga_basah");
        // grapeWatered[2] = setup("/seeds/kedua_basah");
        // wateredImagesMap.put(PlantType.GRAPE, grapeWatered);

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
            gp.setGameState(GamePanel.dialogState);
            return false;
        }

        if (this.currentState == TileState.SOIL && type != PlantType.NONE) {
            this.plantedCropType = type;
            this.currentState = TileState.PLANTED;
            this.growthStage = 0; 
            this.isWatered = false; 
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
                gp.ui.setDialog("Your " + plantedCropType.name() + " wilted!"); 
                gp.setGameState(GamePanel.dialogState);
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
