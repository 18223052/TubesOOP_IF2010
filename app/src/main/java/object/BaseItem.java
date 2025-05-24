package object;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

/**
 * Base abstract class for all items in the game
 * Implements common functionality for all items
 */
public abstract class BaseItem implements IItem {
    protected String name;
    protected int buyPrice;
    protected int sellPrice;
    protected BufferedImage img;
    protected boolean stackable = true;
    protected String category;
    protected GamePanel gp;
    
    public BaseItem(String name, int buyPrice, int sellPrice, GamePanel gp, String category) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.gp = gp;
        this.category = category;
        this.img = loadImage();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getBuyPrice() {
        return buyPrice;
    }
    
    @Override
    public int getSellPrice() {
        return sellPrice;
    }
    
    @Override
    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    @Override
    public String getCategory() {
        return category;
    }
    
    @Override
    public boolean isStackable() {
        return stackable;
    }
    
    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }
    
    @Override
    public BufferedImage getImage() {
        return img;
    }
    
    protected BufferedImage loadImage() {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/" + name.toLowerCase().replace(" ", "_") + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return image;
    }
}