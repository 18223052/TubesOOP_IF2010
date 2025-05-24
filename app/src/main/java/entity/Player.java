package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import controller.InventoryController;
import interactable.Interactable;
import main.GamePanel;
import main.KeyHandler;

import object.IItem; 

public class Player extends Character { 

    KeyHandler keyH;

    public int screenX;
    public int screenY;
    public int hasKey = 0;

    public Rectangle interactionBox;
    private int interactionDistance;
    private int interactionTileRow;
    private int interactionTileCol;

    public InventoryController inventory;
    public final int maxEnergy = 100;

    private String name;
    private String gender;
    private int energy;
    private String farmMap;
    private int gold;
    // private NPC partner; 

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solid = new Rectangle();
        solid.x = 0;
        solid.y = 16;
        solid.width = 32;
        solid.height = 32;

        interactionBox = new Rectangle();
        interactionBox.width = gp.tileSize;
        interactionBox.height = gp.tileSize;
        interactionDistance = gp.tileSize;

        inventory = new InventoryController(gp);

        setDefaultValues();
        getPlayerImage();
        updateInteractionBox();
    }

    public String getName(){
        return name;
    }

    public String getFarmMap(){
        return farmMap;
    }

    public String getGender(){
        return gender;
    }

    // public NPC getPartnerName(){
    //     return farmMap;
    // }
    public int getGold(){
        return gold;
    }

    public int getEnergy(){
        return energy;
    }


    public void setDefaultValues() {
        wX = gp.tileSize * 23;
        wY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        energy = 50;
        gold = 100;
        name = "bobi";
        farmMap = "anjoy";
        // partner = null;
    }

    private IItem activeItem = null;

    public void setActiveItem(IItem item) {
        this.activeItem = item;
        System.out.println("Active item set to: " + (activeItem != null ? activeItem.getName() : "none"));
    }

    public IItem getActiveItem() {
        return activeItem;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public void addEnergy(int amount) {
        this.energy += amount;
        if (this.energy > 100) {
            this.energy = 100;
        }
    }

    public void getPlayerImage() {
        u1 = setup("/player/u1");
        u2 = setup("/player/u2");
        r1 = setup("/player/r1");
        r2 = setup("/player/r2");
        l1 = setup("/player/l1");
        l2 = setup("/player/l2");
        d1 = setup("/player/d1");
        d2 = setup("/player/d2");
    }

    private void calculatePlayerTilePosition() {
        int playerCol = (wX + gp.tileSize / 2) / gp.tileSize;
        int playerRow = (wY + gp.tileSize / 2) / gp.tileSize;

        interactionTileCol = playerCol;
        interactionTileRow = playerRow;

        switch (direction) {
            case "up":
                interactionTileRow = playerRow - 1;
                break;
            case "down":
                interactionTileRow = playerRow + 1;
                break;
            case "left":
                interactionTileCol = playerCol - 1;
                break;
            case "right":
                interactionTileCol = playerCol + 1;
                break;
        }
    }

    public void updateInteractionBox() {
        calculatePlayerTilePosition();

        interactionBox.x = interactionTileCol * gp.tileSize;
        interactionBox.y = interactionTileRow * gp.tileSize;
        interactionBox.width = gp.tileSize;
        interactionBox.height = gp.tileSize;
    }

    // Refactored checkInteraction to use Interactable interface
    public int checkInteraction(Interactable[] interactables) {
        int index = 999;
        for (int i = 0; i < interactables.length; i++) {
            if (interactables[i] != null) {
                updateInteractionBox(); // Ensure interactionBox is up-to-date
                if (interactables[i].isInteractable(interactionBox)) {
                    index = i;
                    break; // Found an interaction, no need to check further
                }
            }
        }
        return index;
    }

    public int[] getInteractionTile() {
        updateInteractionBox();
        return new int[]{interactionTileCol, interactionTileRow};
    }

    public boolean canInteractWithTile(int tileType) {
        updateInteractionBox();
        int tileNum = gp.tileM.mapTileNum[interactionTileCol][interactionTileRow];
        return tileNum == tileType;
    }

    @Override // Override the update method from Character
    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            iscollision = false;
            gp.colCheck.checkTile(this);

            if (!iscollision) { // Simplified from iscollision == false
                switch (direction) {
                    case "up":
                        wY -= speed;
                        break;
                    case "down":
                        wY += speed;
                        break;
                    case "left":
                        wX -= speed;
                        break;
                    case "right":
                        wX += speed;
                        break;
                }
            }

            super.update(); // Call the Character's update for animation
        }

        if (keyH.interactPressed) {
            keyH.interactPressed = false; // Reset immediately


            int npcIndex = checkInteraction(gp.npc); // Assuming gp.npc is now Interactable[]
            int objIndex = checkInteraction(gp.obj); // Assuming gp.obj is now Interactable[]

            if (npcIndex != 999) {
                gp.npc[npcIndex].onInteract(gp,this); // Let the NPC handle its interaction
            } else if (objIndex != 999) {
                gp.obj[objIndex].onInteract(gp,this); // Let the Object handle its interaction
            }
        }
        updateInteractionBox();
    }

    // interactNPC and interactOBJ methods are moved to the respective NPC/SuperObj classes via onInteract

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2); // Use the Character's draw for animation

        drawInteractionBox(g2);
    }

    public void drawInteractionBox(Graphics2D g2) {
        int screenX = interactionBox.x - wX + this.screenX;
        int screenY = interactionBox.y - wY + this.screenY;

        g2.setColor(Color.WHITE);
        g2.drawRect(screenX, screenY, interactionBox.width, interactionBox.height);

        // debug
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.drawString("Tile: " + interactionTileCol + "," + interactionTileRow,
                screenX + 5, screenY + 15);
    }

    public Rectangle getInteractionBox() {
        return interactionBox;
    }

    public InventoryController getInventory() {
        return inventory;
    }
}