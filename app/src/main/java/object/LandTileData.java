package object;

public class LandTileData {

    public int worldX;
    public int worldY;

    // Data state yang perlu disimpan
    public TileState currentState;
    public PlantType plantedCropType;
    public int growthStage;
    public long lastWateredTime;
    public boolean isWatered;
    public long plantedTime;


    public LandTileData() {}

    public LandTileData(int worldX, int worldY, TileState currentState, PlantType plantedCropType,
                        int growthStage, long lastWateredTime, boolean isWatered, long plantedTime) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.currentState = currentState;
        this.plantedCropType = plantedCropType;
        this.growthStage = growthStage;
        this.lastWateredTime = lastWateredTime;
        this.isWatered = isWatered;
        this.plantedTime = plantedTime;
    }
}