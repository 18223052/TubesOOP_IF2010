package object;

import main.GamePanel;

/**
 * Implementation for tool items like hoe, watering can, etc.
 */
public class ToolItem extends BaseItem {
    private String toolType;
    
    public ToolItem(String toolType, int buyPrice, int sellPrice, GamePanel gp) {
        super(toolType, buyPrice, sellPrice, gp, "tools");
        this.toolType = toolType;
        setStackable(false); // Tools are not stackable
    }
    
    /**
     * Returns the type of this tool
     */
    public String getToolType() {
        return toolType;
    }
    
    /**
     * Performs the tool's action. Subclasses can override this method
     * to implement specific tool behaviors.
     */
    public void use() {
        System.out.println("Using " + getName());
    }
}
