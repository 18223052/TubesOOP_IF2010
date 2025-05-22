package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import controller.InventoryController;
import main.GamePanel;
import main.KeyHandler;
import object.Bed;
import object.IItem;
import object.ShippingBin;
import object.Soil;
import object.Stove;
import object.SuperObj;
import object.TV;

public class Player extends Entity {

    // GamePanel gp;
    KeyHandler keyH;

    public int screenX;
    public int screenY;
    public int hasKey = 0;

    // Interaction box for player actions
    public Rectangle interactionBox;
    private int interactionDistance;
    private int interactionTileRow;
    private int interactionTileCol;

    public InventoryController inventory;
    // private final int maxInventorySize = 16;
    public final int maxEnergy = 100;

    private String name;
    private String gender;
    private int energy;
    private String farmMap;
    private int gold;
    private Entity partner;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        // this.gp = gp;
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

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getEnergy() {
        return energy;
    }

    public int getGold() {
        return gold;
    }

    public Entity getPartner() {
        return partner;
    }

    public String getFarmMap() {
        return farmMap;
    }

    public void setDefaultValues() {
        wX = gp.tileSize * 23;
        wY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        energy = 50;
        gold = 0;
        name = "bobi";
        farmMap = "anjoy";
        partner = null;

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

    public int checkInteraction(Entity[] objects) {
        int index = 999;

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                updateInteractionBox();

                Rectangle objectSolid = objects[i].solid;
                int objectWorldX = objects[i].wX;
                int objectWorldY = objects[i].wY;

                Rectangle objectRect = new Rectangle(
                        objectWorldX + objectSolid.x,
                        objectWorldY + objectSolid.y,
                        objectSolid.width,
                        objectSolid.height
                );

                if (interactionBox.intersects(objectRect)) {
                    index = i;
                }
            }
        }

        return index;
    }

    public int checkInteraction(SuperObj[] obj) {
        int idx = 999;

        updateInteractionBox();

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] == null) {
                continue;
            }

            if (obj[i].width > 1 || obj[i].height > 1) {
                if (obj[i].isInteractable(interactionBox)) {
                    return i;
                }
            } else {
                Rectangle objectSolid = new Rectangle(obj[i].wX, obj[i].wY, gp.tileSize, gp.tileSize);
                if (interactionBox.intersects(objectSolid)) {
                    idx = i;
                }
            }
        }
        return idx;
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

    // public 
    public void update() {

        if (keyH.upPressed == true || keyH.downPressed == true
                || keyH.leftPressed == true || keyH.rightPressed == true) {

            if (keyH.upPressed == true) {
                direction = "up";
            } else if (keyH.downPressed == true) {
                direction = "down";
            } else if (keyH.leftPressed == true) {
                direction = "left";
            } else if (keyH.rightPressed == true) {
                direction = "right";
            }

            iscollision = false;
            gp.colCheck.checkTile(this);

            if (iscollision == false) {
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

            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

        }

        if (keyH.interactPressed) {
            // checkInteractionWithTile();
            keyH.interactPressed = false;

            int npcIndex = checkInteractionWithNPC();
            int objIndex = checkInteractionWithOBJ();
            if (npcIndex != 999) {
                interactNPC(npcIndex);
            } else if (objIndex != 999) {
                interactOBJ(objIndex);
            }

            // } else {
            //     checkInteractionWithTile();
            // }
            keyH.interactPressed = false;
        }

        updateInteractionBox();
    }

    public int checkInteractionWithNPC() {
        return checkInteraction(gp.npc);
    }

    public int checkInteractionWithOBJ() {
        return checkInteraction(gp.obj);
    }

    public void interactNPC(int i) {
        if (i != 999) {
            gp.currNPC = gp.npc[i];
            gp.currNPC.speak();
            gp.gameState = gp.dialogState;
        }
    }

    public void interactOBJ(int i) {
        SuperObj obj = gp.obj[i];

        if (obj != null) {
            gp.currObj = obj;
            if (obj instanceof Bed) {
                System.out.println("DEBUG INTERACTABLE bed");
                gp.sleepController.startSleep();
                // addEnergy(maxEnergy);
            }
            if (obj instanceof Soil) {
                if (activeItem != null && activeItem.getName().equals("Hoe")) {
                    System.out.println("DEBUG INTERACTABLE soil");
                } else {
                    System.out.println("gapake");
                }
            }
            if (obj instanceof Stove) {
                System.out.println("DEBUG INTERACTABLE stove");
                // Cast to Stove and start cooking
                gp.gameState = gp.cookingState;
                gp.ui.selectRecipe = 0;
                gp.ui.doneCooking = false;
                gp.ui.hasIngradients = true;
                gp.ui.cookingMenuSelection = 0;
            }
            if (obj instanceof ShippingBin) {
                System.out.println("DEBUG INTERACTABLE ShippingBin");
                gp.gameState = gp.shippingBinState;
                getInventory();
                // Cast to Stove and start cooking
            }
            if (obj instanceof TV) {
                System.out.println("DEBUG INTERACTABLE TV");
                gp.watchingController.watchTV();
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        if (direction.equals("up")) {
            if (spriteNum == 1) {
                image = u1;
            }
            if (spriteNum == 2) {
                image = u2;
            }
        } else if (direction.equals("down")) {
            if (spriteNum == 1) {
                image = d1;
            }
            if (spriteNum == 2) {
                image = d2;
            }
        } else if (direction.equals("left")) {
            if (spriteNum == 1) {
                image = l1;
            }
            if (spriteNum == 2) {
                image = l2;
            }
        } else if (direction.equals("right")) {
            if (spriteNum == 1) {
                image = r1;
            }
            if (spriteNum == 2) {
                image = r2;
            }
        }

        g2.drawImage(image, screenX, screenY, null);

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
