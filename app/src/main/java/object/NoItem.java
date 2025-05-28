package object;

import main.GamePanel;

public class NoItem extends BaseItem {
    public NoItem(GamePanel gp){
        super("Hand", 0, 0, gp, "misc");
        setStackable(false);
    }
}
