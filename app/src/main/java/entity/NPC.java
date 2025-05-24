package entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import interactable.Interactable;
import main.GamePanel;
import object.BaseItem;

public abstract class NPC extends Character implements Interactable {

    protected String name;
    protected int heartPoints;
    protected ArrayList<BaseItem> lovedItems;
    protected ArrayList<BaseItem> hatedItems;
    protected String relationshipStatus;
    protected final int MAX_HEART_POINTS = 150;

    public String[] dialogues = new String[20];
    public int dialogIndex = 0;


    public NPC(GamePanel gp){
        super(gp);
        direction = "down";
        solid = new Rectangle(8,16,32,32);
        setDialogue();
    }

    public abstract void setDialogue();

    public abstract void getImage();

    public void speak() {
        if (dialogues[dialogIndex] == null){
            dialogIndex =0;
        }
        gp.ui.currentDialog = dialogues[dialogIndex];
        dialogIndex ++;
        gp.gameState = gp.dialogState;
    }

    @Override
    public boolean isInteractable(Rectangle interactionBox){
        Rectangle npcSolid = new Rectangle(wX + solid.x, wY + solid.y, solid.width, solid.height);
        return interactionBox.intersects(npcSolid);
    }

    @Override
    public void onInteract(GamePanel gp, Player player){
        gp.currNPC = this;
        speak();
    }

    public String getName(){
        return name;
    }

    public int getHeartPoints(){
        return heartPoints;
    }

    public ArrayList<BaseItem> getLovedItems(){
        return lovedItems;
    }

    public ArrayList<BaseItem> getHatedItems(){
        return hatedItems;
    }

    public boolean hasStore(){
        return false;
    }
    
}
