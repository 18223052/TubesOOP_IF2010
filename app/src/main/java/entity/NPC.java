package entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import interactable.Interactable;
import main.GamePanel;
import object.BaseItem;
import object.ItemFactory;

public abstract class NPC extends Character implements Interactable {

    protected String name;
    protected int heartPoints;
    protected ArrayList<BaseItem> lovedItems;
    protected ArrayList<BaseItem> likedItems;
    protected ArrayList<BaseItem> hatedItems;
    protected String relationshipStatus;
    protected int dayBecameFiance;

    protected final int MAX_HEART_POINTS = 150;
    protected final int MIN_HEART_POINTS = 0;

    public String[] dialogues = new String[20];
    public int dialogIndex = 0;

    protected boolean hatesAllUnlistedItems;


    public NPC(GamePanel gp){
        super(gp);
        this.direction = "down";
        this.solid = new Rectangle(8,16,32,32);
        this.lovedItems = new ArrayList<>();
        this.likedItems = new ArrayList<>();
        this.hatedItems = new ArrayList<>();
        this.relationshipStatus = "Neutral";
        this.dayBecameFiance = -1;
        this.hatesAllUnlistedItems = false;
        // setDialogue();
    }

    public abstract void setDialogue();

    public abstract void getImage();

    public abstract void initializeGiftPreference(object.ItemFactory itemFactory);

    public void speak() {

        setDialogue();
        if (dialogues[dialogIndex] == null){
            dialogIndex =0;
            if (dialogues[dialogIndex] == null){
                gp.ui.currentDialog = "Hmmm..";
                gp.gameState = gp.dialogState;
                return;
            }
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
        this.dialogIndex = 0;
        gp.gameState = gp.npcContextMenuState;
        gp.resumeGameThread();
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


    public int getDayBecameFiance() {
        return dayBecameFiance;
    }

    public int getMaxHeartPoints() {
        return MAX_HEART_POINTS;
    }

    public void setRelationshipStatus(String status) {
        this.relationshipStatus = status;
        this.dialogIndex = 0; 
        setDialogue();
    }

    public void setDayBecameFiance(int day) {
        this.dayBecameFiance = day;
    }

    public boolean doesHateAllUnlistedItems(){
        return hatesAllUnlistedItems;
    }

    protected void setHatesAllUnlistedItems(boolean value) { 
        this.hatesAllUnlistedItems = value;
    }

    public void addHeartPoints(int points) {
        this.heartPoints += points;
        if (this.heartPoints > MAX_HEART_POINTS) {
            this.heartPoints = MAX_HEART_POINTS;
        }
        if (this.heartPoints < MIN_HEART_POINTS) {
            this.heartPoints = MIN_HEART_POINTS;
        }
        System.out.println(name + " now has " + heartPoints + " heart points.");
    }

    public boolean hasStore() {
        return false; 
    }

    protected boolean checkItemInList(ArrayList<BaseItem> list, BaseItem itemToCheck) {
        for (BaseItem item : list) {
            if (item.equals(itemToCheck)) { 
                return true;
            }
        }
        return false;
    }

    public boolean isLovedItem(BaseItem item) {
        return checkItemInList(this.lovedItems, item);
    }

    public boolean isLikedItem(BaseItem item) {
        return checkItemInList(this.likedItems, item);
    }

    public boolean isExplicitlyHatedItem(BaseItem item) {
        return checkItemInList(this.hatedItems, item);
    }
    
}
