package object;

import main.GamePanel;

public class MiscItem extends BaseItem{
    
    public MiscItem(String name, int buyPrice, int sellPrice, GamePanel gp, String category, boolean sellable){
        super(name, buyPrice, sellPrice, gp, category);
        this.sellable = sellable;
    }
}
