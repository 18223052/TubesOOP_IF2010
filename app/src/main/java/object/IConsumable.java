package object;

import main.GamePanel;

/**
 * Interface for items that can be consumed by the player
 */
public interface IConsumable {
    int getEnergyRestoration();
    void consume(GamePanel gp);
}