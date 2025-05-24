package object;

import main.GamePanel;

/**
 * Implementation for a seed item
 */
public class SeedItem extends BaseItem {
    private String cropType;
    
    public SeedItem(String cropType, int buyPrice, int sellPrice, GamePanel gp) {
        super(cropType + " Seed", buyPrice, sellPrice, gp, "seeds");
        this.cropType = cropType;
    }
    
    /**
     * Returns the type of crop this seed will produce
     */
    public String getCropType() {
        return cropType;
    }
}