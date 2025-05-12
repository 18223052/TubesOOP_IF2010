package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import object.ItemFactory;
import object.SuperObj;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize*scale; // 48 x 48
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize*maxWorldCol;
    public final int worldHeight = tileSize*maxScreenRow;

    public String currMap = "/maps/farmmm.txt";

    public UI ui;
    //FPS
    int fps = 60;

    public TileManager tileM = new TileManager(this);

    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public Collision colCheck = new Collision(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this,keyH);
    public SuperObj obj[] = new SuperObj[100];
    public SuperObj currObj;
    public Entity npc[] = new Entity[6];
    public Entity currNPC;

    //gamestate
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int inventoryState = 4;
    public final int statsState = 5;
    public final int sleepState = 6;

    public InventoryController inventoryController;
    public ItemFactory itemFactory;
    public EnvironmentManager eManager = new EnvironmentManager(this);
    // public SleepController sc = new SleepController(this, eManager);

    //Posisi player
    int pX = 100;
    int pY = 100;
    int pSpeed = 4; //4 pixel untuk kecepatan karakter


    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        ui = new UI(this);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setup() {

        setupExceptEnvironment();
        if (eManager == null) {
            eManager = new EnvironmentManager(this);
        }
        eManager.setup();

    }

    public void setupExceptEnvironment() {
        // Clear existing objects and NPCs first
        aSetter.clearObjects();
        aSetter.clearNPCs();
        
        // Add new ones based on current map
        aSetter.setObj();
        aSetter.setNPC();
        

        inventoryController = new InventoryController(this);
        itemFactory = new ItemFactory(this);

        player.inventory = inventoryController;
        gameState = playState;

        addStartingItems();

    }
    
    private void addStartingItems() {
        // Add a few test items to the player's inventory
        player.inventory.addItem(itemFactory.createTool("Hoe"));
        player.inventory.addItem(itemFactory.createTool("WateringCan"));
        player.inventory.addItem(itemFactory.createTool("FishingPole"));
        // player.addItem(itemFactory.createSeed("Tomato"));
        // player.addItem(itemFactory.createSeed("Potato"));
        // player.addItem(itemFactory.createCrop("Tomato"));
        // player.addItem(itemFactory.createConsumable("Energy Drink"));
    }

    @Override
    // using delta method
    public void run(){

        double drawInterval = 1000000000/fps; //0.01666 detik
        double delta = 0;
        long lastTime = System.nanoTime();
        long currTime;
        long timer = 0;
        int drawCnt = 0;

        while (gameThread != null){

            currTime = System.nanoTime();
            delta += (currTime - lastTime) / drawInterval;

            lastTime = currTime;

            if (delta >=1){
                update();
                repaint();
                delta --;
            }

            if (timer >= 100000000){
                System.out.println("FPS: " + drawCnt);
                drawCnt = 0;
                timer = 0;
            }

        }

    }

    public void update(){

        if (gameState == playState){
            player.update();

            tileM.checkTeleport();
            eManager.update();
            // sc.update();
        }
        if (gameState == pauseState){
            //nothing
        }
        if (gameState == inventoryState){
            inventoryController.update();
        }
        // if (sc.isSleeping) {
        //     long elapsedTime = System.nanoTime() - sc.sleepStartTime;
        //     if (elapsedTime >= 3_000_000_000L) { // Tidur selama 3 detik
        //         sc.skipDay(); // Setelah 3 detik, lanjutkan ke hari berikutnya
        //         sc.isSleeping = false;
        //         gameState = playState; // Kembali ke gameplay
        //     }
        }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
    
        // if (sc.isSleeping) {
        //     sc.displaySleepScreen();  // Pastikan layar tidur ditampilkan saat tidur
        // } else {
            tileM.draw(g2); // Jika tidak tidur, gambar map
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
    
            // npc
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2);
                }
            }

            if (gameState == inventoryState) {
                inventoryController.draw(g2);
            }
            player.draw(g2);
            ui.draw(g2);
            eManager.draw(g2);
        // }
        g2.dispose();
    }

}
