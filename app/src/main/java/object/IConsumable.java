package object;

import main.GamePanel;


public interface IConsumable {
    int getEnergyRestoration();
    void consume(GamePanel gp);
}