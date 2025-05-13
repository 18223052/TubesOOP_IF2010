package object;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Item {
    public String name;
    public int buyPrice;
    public int sellPrice;
    public int addEnergy;
    public BufferedImage img;
    public boolean stackable = true;
    public String category;

    // khusus item yang pake atribut tambahan
    public String season;
    public String weather;
    public String time;
    public int rarity;
    

    public Item(String name, int buyPrice, int sellPrice){
        this.name = name;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public Item(String name, int buyPrice, int sellPrice, int addEnergy){
        this.name = name;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.addEnergy = addEnergy;
    }

    public BufferedImage getImage(GamePanel gp) {
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


    public String getName(){
        return name;
    }

    public int getBuyPrice(){
        return buyPrice;
    }

    public int getSellPrice(){
        return sellPrice;
    }
}
