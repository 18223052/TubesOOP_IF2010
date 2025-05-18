package object;

import java.awt.image.BufferedImage;

public interface IItem {
    String getName();
    String getCategory();
    BufferedImage getImage();

    int getBuyPrice();
    int getSellPrice();

    boolean isStackable();
}
