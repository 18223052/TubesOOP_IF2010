package object;

import entity.Player;
import main.GamePanel;

public class RecipeItem extends BaseItem implements IUsable {
    private String recipeToUnlock;


    public RecipeItem(String name, int buyPrice, int sellPrice, GamePanel gp, String recipeToUnlock){
        super(name, buyPrice, sellPrice, gp, "recipe");
        this.recipeToUnlock = recipeToUnlock;
    }

    public String getRecipeToUnlock(){
        return recipeToUnlock;
    }

    @Override
    public void use(Player player, GamePanel gp){
        if (gp != null && player != null){
            System.out.println("RecipeItem trying to unlock ID: " + this.recipeToUnlock); 
            if(!player.isRecipeUnlocked(this.recipeToUnlock)){
                player.unlockRecipe(this.recipeToUnlock);
                System.out.println("Resep '" + this.recipeToUnlock + "' telah terbuka!");
                gp.ui.setDialog("Resep untuk " + this.recipeToUnlock + " telah dibuka!");
                gp.setGameState(GamePanel.dialogState);
                gp.repaint();
            } else {
                System.out.println("Anda sudah mengetahui resep ini: " + this.recipeToUnlock);
                gp.ui.setDialog("Anda sudah mengetahui resep ini.");
                gp.setGameState(GamePanel.dialogState);
                gp.repaint();
            }
        } else {
            System.out.println("Resep untuk " + recipeToUnlock + " terbuka !"); 
        }
    }
}
