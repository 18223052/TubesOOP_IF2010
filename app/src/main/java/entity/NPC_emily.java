package entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import main.GamePanel;
import object.Item;

public class NPC_emily extends Entity {

    private String name = "Emily";
    private int heartPoints;
    private ArrayList<Item> lovedItems;
    private ArrayList<Item> hatedItems;
    private ArrayList<Item> likedItems;
    private String relationshipStatus;
    private final int MAX_HEART_POINTS = 150;

    public NPC_emily(GamePanel gp, ArrayList<Item> lovedItems, ArrayList<Item> likedItems, ArrayList<Item> hatedItems) {
        super(gp);
        direction = "down";
        this.lovedItems = lovedItems;
        this.likedItems = likedItems;
        this.hatedItems = hatedItems;
        
        // Set up dialogues
        setDialogue();
        

        solid = new Rectangle(8, 16, 32, 32); 
        
        getImage();
    }

    public void getImage() {
        u1 = setup("/player/police_diem");
        u2 = setup("/player/police_diem");
        r1 = setup("/player/police_diem");
        r2 = setup("/player/police_diem");
        l1 = setup("/player/police_diem");
        l2 = setup("/player/police_diem");
        d1 = setup("/player/police_diem");
        d2 = setup("/player/police_diem");
    }
    

    @Override
    public void setDialogue() {
        dialogues[0] = "Hello there! I'm Mayor Tadi. Welcome to our town!";
        dialogues[1] = "I hope you're enjoying your time on the farm.";
        dialogues[2] = "If you need any help, just ask around town.";
        dialogues[3] = "We have a shipping bin near the center of town where you can sell your produce.";
        dialogues[4] = "Good luck with your farming!";
    }
    

    public String getName() {
        return name;
    }

    public int getHeartPoints(){
        return heartPoints;
    }

    public ArrayList<Item> getLovedItems (){
        return lovedItems;
    }

    public ArrayList<Item> getHatedItems (){
        return hatedItems;
    }
}