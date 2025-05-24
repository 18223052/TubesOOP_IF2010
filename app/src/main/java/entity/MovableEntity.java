package entity;

import java.awt.image.BufferedImage;

public interface MovableEntity {
    void update();
    BufferedImage getAnimationImage();
}
