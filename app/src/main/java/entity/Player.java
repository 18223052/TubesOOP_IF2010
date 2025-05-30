package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList; // Import ArrayList
import java.util.List;

import controller.InventoryController;
import interactable.Interactable;
import main.GamePanel;
import main.KeyHandler;
import object.IItem;
import object.NoItem;

public class Player extends Character {

    KeyHandler keyH;

    public int screenX;
    public int screenY;
    public int hasKey = 0;

    public Rectangle interactionBox;
    private int interactionTileRow;
    private int interactionTileCol;

    public InventoryController inventory;
    public static final int MAX_ENERGY = 100;
    public static final int MIN_ENERGY_BEFORE_SLEEP  = -20;
    public static final int MIN_ENERGY_FOR_ACTION  = -20;

    private String name;
    private String gender;
    private int energy;
    private String farmMap;
    private int gold;
    // private NPC partner;
    private NPC fiance = null;
    private NPC spouse = null;

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

        this.gender = "Male";

        inventory = new InventoryController(gp);

        setActiveItem(new NoItem(gp));

        setDefaultValues();
        getCharacterImage();
        updateInteractionBox();
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFarmMap(){
        return farmMap;
    }

    public void setFarmMap(String farmMap) {
        this.farmMap = farmMap;
    }

    public String getGender(){
        return gender;
    }

    public int getGold(){
        return gold;
    }

    public int getEnergy(){
        return energy;
    }

    public int getMaxEnergy(){
        return MAX_ENERGY;
    }


    public void setDefaultValues() {
        wX = gp.tileSize * 23;
        wY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        energy = 100;
        gold = 100;
        // name = "bobi";
        // farmMap = "anjoy";
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

    public String getPartner(){
        if (this.fiance != null || this.spouse != null){
            if (this.fiance == null){
                return this.spouse.getName();
            } else {
                return this.fiance.getName();
            }
        }
        return null;
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

    public void setEnergy(int energy){
        this.energy = energy;
        if (this.energy > MAX_ENERGY){
            this.energy = MAX_ENERGY;
        }
    }

    public void changeEnergy(int amount) {
        this.energy += amount;
        if (this.energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        }
    }

    public boolean deductEnergy(int amount){
        this.energy -= amount;

        if (this.energy <= MIN_ENERGY_BEFORE_SLEEP){
            System.out.println("Energy mencapai " + MIN_ENERGY_BEFORE_SLEEP + "! Player harus tidur.");
            gp.sleepController.forceSleep();
        }
        System.out.println("Energi player berkurang " + amount + ". Energi tersisa: " + this.energy);
        return true;
    }

    // // Propose & Marry
    //  public NPC getFiance() {
    //     return this.fiance;
    // }

    // public void setFiance(NPC npc) {
    //     this.fiance = npc;
    //     if (npc != null) { 
    //         this.spouse = null;
    //     }
    // }

    // public boolean hasFiance() {
    //     return this.fiance != null;
    // }

    // public NPC getSpouse() {
    //     return this.spouse;
    // }

    // public void setSpouse(NPC npc) {
    //     this.spouse = npc;
    //     if (npc != null) { 
    //         if (this.fiance == npc) { 
    //             this.fiance = null; 
    //         }
    //     }
    // }

    // public boolean hasSpouse() {
    //     return this.spouse != null;
    // }


    // untuk masak
    private List<String> unlockedRecipeIds = new ArrayList<>();

    public boolean isRecipeUnlocked(String recipeId){
        return unlockedRecipeIds.contains(recipeId);
    }

    public void unlockRecipe(String recipeId){
        if (!unlockedRecipeIds.contains(recipeId)){
            unlockedRecipeIds.add(recipeId);
            System.out.println("Resep '"+recipeId + "' terbuka!");
            System.out.println("Current unlocked recipes: " + unlockedRecipeIds); // Tambahkan ini
        }
    }

    private int totalFishCaught = 0;

    public int getFishCaughtCount(){
        return totalFishCaught;
    }

    public void incrementFishCaughtCount(){
        totalFishCaught ++;
    }

    private List<String> caughtFishTypes = new ArrayList<>();

    public boolean hasCaughtFish(String fishName){
        return caughtFishTypes.contains(fishName);
    }

    public void addCaughtFishType(String fishName){
        if(!caughtFishTypes.contains(fishName)){
            caughtFishTypes.add(fishName);
        }
    }

    private boolean hasHarvested = false;

    public boolean hasHarvestedFirstTime() {
        return hasHarvested;
    }

    public void setHasHarvested(boolean harvested) {
        this.hasHarvested = harvested;
    }


    @Override 
    public void getCharacterImage() {
        
        u1 = setup("/player/u1");
        u2 = setup("/player/u2");
        r1 = setup("/player/r1");
        r2 = setup("/player/r2");
        l1 = setup("/player/l1");
        l2 = setup("/player/l2");
        d1 = setup("/player/d1");
        d2 = setup("/player/d2");

        defaultImage = d1;
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

    public <T extends Interactable> int checkInteraction(ArrayList<T> interactables) {
        int index = 999;

        for (int i = 0; i < interactables.size(); i++) {
            T obj = interactables.get(i); 
            if (obj != null) {
                updateInteractionBox();
                if (obj.isInteractable(interactionBox)) {
                    index = i; 
                    break; 
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

    @Override
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

            if (!iscollision) { 
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

            super.update(); 
        }

        if (keyH.interactPressed) {
            keyH.interactPressed = false; 

            
            int npcIndex = checkInteraction(convertArrayToArrayList(gp.npc)); 
            int objIndex = checkInteraction(gp.obj); 

            if (npcIndex != 999) {
                gp.npc[npcIndex].onInteract(gp,this);
            } else if (objIndex != 999) {
                gp.obj.get(objIndex).onInteract(gp,this);
            }
        }
        updateInteractionBox();
    }


    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if (activeItem != null && !(activeItem instanceof NoItem) && activeItem.getImage() != null) {
            BufferedImage itemImage = activeItem.getImage();

            int currentScreenX = wX - gp.player.wX + gp.player.screenX;
            int currentScreenY = wY - gp.player.wY + gp.player.screenY;

            int itemOffsetX = 0;
            int itemOffsetY = 0;
            int itemWidth = (int)(gp.tileSize * 0.55); // Lebar default item
            int itemHeight = (int)(gp.tileSize * 0.55); // Tinggi default item


            int drawX = currentScreenX;
            int drawY = currentScreenY;
            int drawWidth = itemWidth;
            int drawHeight = itemHeight;

            int sizeDifferenceX = gp.tileSize - itemWidth;
            int sizeDifferenceY = gp.tileSize - itemHeight;
            int centerOffsetX = sizeDifferenceX / 2;
            int centerOffsetY = sizeDifferenceY / 2;


            switch (direction) {
                case "up":

                    itemOffsetX = -gp.tileSize / 4; // Geser sedikit ke kiri
                    itemOffsetY = -gp.tileSize / 2; // Geser lebih jauh ke atas

                    break;
                case "down":

                    itemOffsetX = 0;
                    itemOffsetY = gp.tileSize / 2;
                    break;
                case "left":
                    itemOffsetX = -gp.tileSize / 2;
                    itemOffsetY = 0;

                    break;
                case "right":
                    itemOffsetX = gp.tileSize / 4;
                    itemOffsetY = 0;
                    drawX = currentScreenX + itemOffsetX + itemWidth; 
                    drawWidth = -itemWidth; 
                    break;
            }

            int finalDrawX = drawX + itemOffsetX + centerOffsetX;
            int finalDrawY = drawY + itemOffsetY + centerOffsetY;

            g2.drawImage(itemImage,
                        finalDrawX,       
                        finalDrawY,     
                        finalDrawX + drawWidth,
                        finalDrawY + drawHeight, 
                        0,                         
                        0,                        
                        itemImage.getWidth(),     
                        itemImage.getHeight(),     
                        null);
        }
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

   
    private <T extends Interactable> ArrayList<T> convertArrayToArrayList(T[] array) {
        ArrayList<T> list = new ArrayList<>();
        if (array != null) {
            for (T element : array) {
                if (element != null) {
                    list.add(element);
                }
            }
        }
        return list;
    }

    // Propose & Marry

    public NPC getFiance() {
        return this.fiance;
    }

    public void setFiance(NPC npc) {
        this.fiance = npc;
        if (npc != null) { // Jika menetapkan tunangan baru
            this.spouse = null; // Pastikan tidak bisa memiliki pasangan dan tunangan sekaligus
        }
    }

    public boolean hasFiance() {
        return this.fiance != null;
    }

    public NPC getSpouse() {
        return this.spouse;
    }

    public void setSpouse(NPC npc) {
        this.spouse = npc;
        if (npc != null) { // Jika menetapkan pasangan baru
            if (this.fiance == npc) { // Jika menikah dengan tunangan saat ini
                this.fiance = null; // Hapus status tunangan
            }
        }
    }

    public boolean hasSpouse() {
        return this.spouse != null;
    }

    // public boolean hasItem(String itemName) {
    //     if (this.inventory != null) { 
    //         return this.inventory.hasItem(itemName);
    //     }
    //     System.err.println("Peringatan: InventoryController adalah null di Player.hasItem()");
    //     return false;
    // }
}