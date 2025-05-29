package object;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

import java.util.Objects;


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
        
        // Construct the image path
        String imagePath = "/items/" + name.toLowerCase().replace(" ", "_") + ".png";
        
        try {

            // DEBUG
            System.out.println("Attempting to load image: " + imagePath);
        
            InputStream is = getClass().getResourceAsStream(imagePath);
            
            if (is == null) {
                System.err.println("ERROR: Image file not found (InputStream is null) at path: " + imagePath + " for item: " + name);
                return null; 
            }
            
            image = ImageIO.read(is); 
            is.close(); 
            
            if (image == null) {
                System.err.println("ERROR: Image could not be decoded from path: " + imagePath + " for item: " + name + ". Check file format.");
                return null;
            }

            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch(IOException e) {
            System.err.println("IOException while loading image " + imagePath + " for item " + name + ": " + e.getMessage());
            e.printStackTrace();
            return null; 
        } catch (Exception e) { 
            System.err.println("An unexpected error occurred while loading image " + imagePath + " for item " + name + ": " + e.getMessage());
            e.printStackTrace();
            return null; 
        }
        
        return image;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseItem baseItem = (BaseItem) o;
        return name.equals(baseItem.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name);
    }
}