package main;

import javax.swing.JPanel;

import controller.InventoryController;
import controller.SleepController;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import environment.GameTime;
import environment.Lighting;
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

    // Game Time
    public GameTime gameTime = new GameTime();
    public int currentMinute = gameTime.getGameMinute();
    public int currentHour = gameTime.getGameHour();
    public int currentDay = gameTime.getGameDay();
    public boolean isTimePaused = false;



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
    private final Object pauseLock = new Object();


    public KeyHandler keyH;
    public UI ui;
    public TileManager tileM;
    public Collision colCheck;
    public AssetSetter aSetter;
    public Player player;
    public InventoryController inventoryController;
    public ItemFactory itemFactory;
    public EnvironmentManager eManager;
    public SleepController sleepController;
    
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
        
        inventoryController = new InventoryController(this);

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
        // inventoryController = new InventoryController(this);
        itemFactory = new ItemFactory(this);
        sleepController = new SleepController(this, player);
        

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
        inventoryController.addItem(itemFactory.createTool("pickaxe"));
        inventoryController.addItem(itemFactory.createFood("salmon"));
        inventoryController.addItem(itemFactory.createFood("veggiesoup"));
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
        

        while (gameThread != null) {
            currTime = System.nanoTime();
            delta += (currTime - lastTime) / drawInterval;
            timer += (currTime - lastTime);
            lastTime = currTime;

            if (gameState != playState && gameState !=sleepState){
                pauseGameThread();
                repaint();
                synchronized(pauseLock){
                    try{
                        pauseLock.wait();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                resumeGameThread();
                continue;
            }

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            if (timer >= 1000000000) {
                // System.out.println("FPS: " + drawCnt);
                timer = 0;
            }
        }
    }

    public void pauseGameThread(){
        isTimePaused = true;
        if (eManager.isLightingSetup()){
            eManager.getLighting().onPause();
        }

        if (gameTime != null) {
            gameTime.pause();
        }
        synchronized (pauseLock){}
    }

    public void resumeGameThread(){
        isTimePaused = false;
        synchronized(pauseLock){
            pauseLock.notifyAll();
        }

        if (eManager.isLightingSetup()){
            eManager.getLighting().onResume();
        }

        if (gameTime != null) {
            gameTime.resume();
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            tileM.checkTeleport();
            eManager.update();

            // Game time update every n frames
            currentMinute = gameTime.getGameMinute();
            currentHour = gameTime.getGameHour();
            currentDay = gameTime.getGameDay();

            if (eManager != null && eManager.isLightingSetup()) {
                Lighting lighting = eManager.getLighting();

                if (currentHour >= 5 && currentMinute >= 0) {
                    lighting.triggerTransition(Lighting.DAWN); // Transisi terang
                }
                else if (currentHour >= 6 && currentMinute >= 0) {
                    lighting.triggerTransition(Lighting.DAY); // Langsung terang penuh
                }
                else if (currentHour >= 17 && currentMinute >= 0) {
                    lighting.triggerTransition(Lighting.DUSK); // Transisi gelap
                }
                else if (currentHour >= 18 && currentMinute >= 0) {
                    lighting.triggerTransition(Lighting.NIGHT); // Langsung gelap penuh
                }
            }

        } else if (gameState == inventoryState) {
            inventoryController.update();
        } else if (gameState == sleepState){
            sleepController.update();
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


                if (eManager != null) {
                    eManager.draw(g2);
                }
                
                if (ui != null) {
                    ui.draw(g2);
                }

                if (gameState == sleepState && sleepController != null){
                    sleepController.draw(g2);
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
    
    public void openTimeCheatDialog() {
        isTimePaused = true;
        if (gameTime != null) gameTime.pause();

        javax.swing.SwingUtilities.invokeLater(() -> {
            // Input hari (boleh kosong)
            String dayInput = javax.swing.JOptionPane.showInputDialog(
                this,
                "Masukkan hari baru (kosongkan jika tidak ingin mengubah):",
                "Cheat Day",
                javax.swing.JOptionPane.PLAIN_MESSAGE
            );

            // Input waktu (boleh kosong)
            String timeInput = javax.swing.JOptionPane.showInputDialog(
                this,
                "Masukkan waktu baru (format: HH:MM, kosongkan jika tidak ingin mengubah):",
                "Cheat Time",
                javax.swing.JOptionPane.PLAIN_MESSAGE
            );

            boolean valid = true;

            // Proses input hari
            if (dayInput != null && !dayInput.trim().isEmpty()) {
                try {
                    int day = Integer.parseInt(dayInput.trim());
                    if (day > 0) {
                        gameTime.setGameDay(day);
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                            this, "Hari harus angka positif.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        valid = false;
                    }
                } catch (NumberFormatException e) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this, "Input hari bukan angka valid.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                    valid = false;
                }
            }

            // Proses input waktu
            if (timeInput != null && !timeInput.trim().isEmpty()) {
                if (timeInput.matches("\\d{1,2}:\\d{2}")) {
                    String[] parts = timeInput.split(":");
                    try {
                        int hour = Integer.parseInt(parts[0]);
                        int minute = Integer.parseInt(parts[1]);
                        if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
                            gameTime.setTime(hour, minute);
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(
                                this, "Format waktu tidak valid.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE
                            );
                            valid = false;
                        }
                    } catch (NumberFormatException e) {
                        javax.swing.JOptionPane.showMessageDialog(
                            this, "Input waktu bukan angka valid.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        valid = false;
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(
                        this, "Format waktu harus HH:MM.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                    valid = false;
                }
            }

            if (valid) {
                System.out.println("Cheat berhasil diterapkan.");
            }

            isTimePaused = false;
            if (gameTime != null) gameTime.resume();
        });
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