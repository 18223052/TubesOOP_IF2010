package object;

import entity.Player;
import main.GamePanel;

public interface IUsable {
    void use(Player player, GamePanel gp);
}