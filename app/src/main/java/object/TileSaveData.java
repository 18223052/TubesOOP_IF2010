package object;

import java.io.Serializable; 

public class TileSaveData implements Serializable {
    private static final long serialVersionUID = 1L; 

    public int col;
    public int row;
    public TileState state;
    public PlantType cropType;
    public int growthStage;
    public boolean watered;
    public long plantedTime; 

    public TileSaveData(int col, int row, TileState state, PlantType cropType, int growthStage, boolean watered, long plantedTime) {
        this.col = col;
        this.row = row;
        this.state = state;
        this.cropType = cropType;
        this.growthStage = growthStage;
        this.watered = watered;
        this.plantedTime = plantedTime;
    }


}