package interactable;

import java.awt.Rectangle;

import entity.Player;
import main.GamePanel;

public interface Interactable {
    boolean isInteractable(Rectangle interactionBox);
    void onInteract(GamePanel gp, Player player);
}
