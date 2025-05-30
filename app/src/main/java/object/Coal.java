package object;

import main.GamePanel;

public class Coal extends FuelItem {
    public Coal(GamePanel gp) {
        super("Coal", 20, 10, gp, 2);
    }
}