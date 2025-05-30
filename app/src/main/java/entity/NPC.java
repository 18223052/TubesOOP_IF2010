package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import interactable.Interactable;
import main.GamePanel;
import object.IItem;
import object.ItemFactory;

public abstract class NPC extends Character implements Interactable {

    protected String name;
    protected int heartPoints;
    protected ArrayList<IItem> lovedItems;
    protected ArrayList<IItem> likedItems;
    protected ArrayList<IItem> hatedItems;
    protected String relationshipStatus;
    protected int dayBecameFiance;

    protected final int MAX_HEART_POINTS = 150;
    protected final int MIN_HEART_POINTS = 0;

    public String[] dialogues = new String[20];
    public int dialogIndex = 0;

    protected boolean hatesAllUnlistedItems;

    public static final String STATUS_SINGLE = "Single";
    public static final String STATUS_FIANCE = "Fiance";
    public static final String STATUS_MARRIED = "Married";

    // Gender
    protected String gender;
    public static final String gender_male = "Male";
    public static final String gender_female = "Female";


    public NPC(GamePanel gp){
        super(gp);
        this.direction = "down";
        this.solid = new Rectangle(8,16,32,32);
        this.lovedItems = new ArrayList<>();
        this.likedItems = new ArrayList<>();
        this.hatedItems = new ArrayList<>();
        this.relationshipStatus = STATUS_SINGLE;
        this.dayBecameFiance = -1;
        this.hatesAllUnlistedItems = false;
        // setDialogue();
        this.heartPoints = 0;
        this.dayBecameFiance = -1;
    }

    public abstract void setDialogue();

    @Override
    public abstract void getCharacterImage();

    public abstract void initializeGiftPreference(object.ItemFactory itemFactory);

    public void speak() {
        setDialogue();
        if (dialogues[dialogIndex] == null){
            dialogIndex =0;
            if (dialogues[dialogIndex] == null){
                gp.ui.currentDialog = "Hmmm..";
                gp.setGameState(GamePanel.dialogState);
                return;
            }
        }
        gp.ui.currentDialog = dialogues[dialogIndex];
        dialogIndex ++;
        gp.setGameState(GamePanel.dialogState);
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
        gp.gameState = GamePanel.npcContextMenuState;
        gp.resumeGameThread();
    }

    public String getName(){
        return name;
    }

    public int getHeartPoints(){
        return heartPoints;
    }

    public ArrayList<IItem> getLovedItems(){
        return lovedItems;
    }

    public ArrayList<IItem> getHatedItems(){
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

    
    public String getGender(){
        return this.gender;
    }

    public boolean hasStore() {
        return false; 
    }

    protected boolean checkItemInList(ArrayList<IItem> list, IItem itemToCheck) {
        for (IItem item : list) {
            if (item.equals(itemToCheck)) { 
                return true;
            }
        }
        return false;
    }

    public boolean isLovedItem(IItem item) {
        return checkItemInList(this.lovedItems, item);
    }

    public boolean isLikedItem(IItem item) {
        return checkItemInList(this.likedItems, item);
    }

    public boolean isExplicitlyHatedItem(IItem item) {
        return checkItemInList(this.hatedItems, item);
    }

    public boolean isProposable(Player player){
        if (player == null) return false;
        return this.heartPoints >= MAX_HEART_POINTS && this.relationshipStatus.equals(STATUS_SINGLE) && !player.hasFiance() && !player.hasSpouse() && this.gender == gender_female;  // Pemain tidak sedang menikah dengan orang lain
    }

    public void becomeFiance(Player player, int currentDay) {
        this.relationshipStatus = NPC.STATUS_FIANCE;
        this.dayBecameFiance = currentDay;
        // player.setFiance(this); // Ini akan diatur dari sisi controller/player untuk menghindari circular dependency langsung
    }

    public boolean canMarryPlayer(Player player, int currentDay) {
        if (player == null) return false;
        return this.relationshipStatus.equals(STATUS_FIANCE) &&
            player.getFiance() == this && // Memastikan pemain bertunangan DENGAN NPC INI
            currentDay > this.dayBecameFiance; // Sudah lewat minimal 1 hari
    }

    public void marryPlayer(Player player) {
        this.relationshipStatus = STATUS_MARRIED;

    }

    public String getProposalDeclineMessage() {
        // Logika default berdasarkan kondisi umum penolakan
        if (this.getHeartPoints() < this.getMaxHeartPoint()) {
            // Jika heart points belum cukup
            return getName() + " berkata, 'Aku belum siap untuk komitmen sebesar itu. Kita butuh lebih banyak waktu bersama.'";
        } else if (!STATUS_SINGLE.equals(this.getRelationshipStatus())) {
            // Jika sudah terikat dengan orang lain (meskipun di game Anda mungkin hanya "single")
            return getName() + " berkata, 'Maaf, aku sudah terikat dengan orang lain.'";
        } else if (!gender_female.equals(this.getGender())) {
            // Contoh penolakan berdasarkan gender jika proposal hanya untuk wanita
            return getName() + " berkata, 'Aku menghargai perasaanmu, tapi aku tidak bisa menerimanya. Aku bukan tipe mu.'";
        } else {
            // Pesan default lainnya jika tidak ada kondisi di atas yang cocok
            return getName() + " berkata, 'Aku menghargai tawaranmu, tapi aku rasa kita lebih baik sebagai teman saja.'";
        }
    }

    public int getMaxHeartPoint(){
        return MAX_HEART_POINTS;
    }
    
    public String getRelationshipStatus(){
        return this.relationshipStatus;
    }

    @Override
    public void update(){

    }

    @Override
    public BufferedImage getDisplayImage(){
        if (defaultImage != null){
            return defaultImage;
        }
        return d1;
    }

    @Override
    public void draw (Graphics2D g2){
        super.draw(g2);
    }
    
}
