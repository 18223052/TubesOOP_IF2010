package tile;

import java.awt.image.BufferedImage;

public class Tile {
    
    public BufferedImage image;
    public boolean collision = false;


    public boolean teleport = false;
    public String destMap = null;
    public int destX = 0;
    public int destY = 0;
    public String sourceMap = null;
}
