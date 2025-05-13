package main;

import javax.swing.JPanel;

import controller.InventoryController;

import java.awt.Dimension;
import java.awt.Font;
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
    // Constants
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48 x 48
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxScreenRow;

    // Current map
    public String currMap = "/maps/farmmm.txt";

    // Game state constants
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int inventoryState = 4;
    public final int statsState = 5;
    public final int sleepState = 6;
    public final int cookingState = 7;
    public int gameState;


    public KeyHandler keyH;
    public UI ui;
    public TileManager tileM;
    public Collision colCheck;
    public AssetSetter aSetter;
    public Player player;
    public InventoryController inventoryController;
    public ItemFactory itemFactory;
    public EnvironmentManager eManager;
    
    // Arrays for game objects and NPCs
    public SuperObj obj[] = new SuperObj[100];
    public Entity npc[] = new Entity[6];
    
    // Current interactive objects
    public SuperObj currObj;
    public Entity currNPC;
    
    // Game thread
    private Thread gameThread;
    private int fps = 60;
    
    // // Player starting position
    // private int pX = 100;
    // private int pY = 100;
    // private int pSpeed = 4;


    private boolean isComplete = false;
    public GamePanel() {
 
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        

        this.keyH = new KeyHandler(this);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        

        gameState = playState;

        tileM = new EmptyTileManager(this);
    }
    

    public void setup() {

        colCheck = new Collision(this);
        aSetter = new AssetSetter(this);
        ui = new UI(this);
        

        player = new Player(this, keyH);
        

        tileM = new TileManager(this);
        
 
        eManager = new EnvironmentManager(this);
        inventoryController = new InventoryController(this);
        itemFactory = new ItemFactory(this);
        

        player.inventory = inventoryController;
        

        tileM.setup();  
        setupMap();     
        addStartingItems();
        eManager.setup();

        isComplete = true;
    }
    
    // Setup objects dan NPC's
    public void setupMap() {
        aSetter.clearObjects();
        aSetter.clearNPCs();
        aSetter.setObj();
        aSetter.setNPC();
    }
    

    public void changeMap() {
        setupMap();
    }
    
    // starting item buat item awal
    private void addStartingItems() {
        inventoryController.addItem(itemFactory.createTool("hoe"));
        inventoryController.addItem(itemFactory.createTool("wateringcan"));
        inventoryController.addItem(itemFactory.createTool("fishingpole"));
    }


    public void startGameThread() {

        setup();
        
        // game loop in action gas gas
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Game loop using delta time
    @Override
    public void run() {
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currTime;
        long timer = 0;
        int drawCnt = 0;

        while (gameThread != null) {
            currTime = System.nanoTime();
            delta += (currTime - lastTime) / drawInterval;
            timer += (currTime - lastTime);
            lastTime = currTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCnt++;
            }

            if (timer >= 1000000000) {
                // System.out.println("FPS: " + drawCnt);
                drawCnt = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            tileM.checkTeleport();
            eManager.update();
        } else if (gameState == inventoryState) {
            inventoryController.update();
        }
        // nambah gamestate lain kali
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if (!isComplete) {
            drawLoadingScreen(g2);
        } else {

            try {
                tileM.draw(g2);
                
                // Draw objects
                for (int i = 0; i < obj.length; i++) {
                    if (obj[i] != null) {
                        obj[i].draw(g2, this);
                    }
                }
                
                // Draw NPCs
                for (int i = 0; i < npc.length; i++) {
                    if (npc[i] != null) {
                        npc[i].draw(g2);
                    }
                }
                
                // Draw player if initialized
                if (player != null) {
                    player.draw(g2);
                }
                
                // Draw UI elements
                if (gameState == inventoryState && inventoryController != null) {
                    inventoryController.draw(g2);
                }

                if (eManager != null) {
                    eManager.draw(g2);
                }
                
                if (ui != null) {
                    ui.draw(g2);
                }
                
            } catch (Exception e) {

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.drawString("Rendering error: " + e.getMessage(), 50, 50);
                e.printStackTrace();
            }
        }
        
        g2.dispose();
    }

    private void drawLoadingScreen(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        String loadingText = "Loading...";
        int textWidth = g2.getFontMetrics().stringWidth(loadingText);
        g2.drawString(loadingText, (screenWidth - textWidth) / 2, screenHeight / 2);
    }
    

    private class EmptyTileManager extends TileManager {
        public EmptyTileManager(GamePanel gp) {
            super(gp);
        }
        
        @Override
        public void draw(Graphics2D g2) {
            // placeholder
        }
        
        @Override
        public void setup() {
            // Do nothing
        }
        
        @Override
        public void loadMap(String filePath) {
            // Do nothing
        }
    }
}