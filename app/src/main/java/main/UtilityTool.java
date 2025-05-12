package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool {
    
    public BufferedImage scaleImage(BufferedImage ori, int width, int height){
        BufferedImage scaledImage = new BufferedImage (width, height, ori.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(ori,0,0,width,height,null);

        return scaledImage;
    }
}
