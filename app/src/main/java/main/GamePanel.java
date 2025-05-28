package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.ArrayList; // Import ArrayList jika Anda memutuskan untuk menggunakannya

import javax.swing.JPanel;

import controller.CookingController;
import controller.FarmingController;
import controller.InventoryController;
import controller.ShippingBinController;
import controller.SleepController;
import controller.WatchingController;
import controller.NPCController;


import controller.StoreController;
import entity.NPC;
import entity.Player;
import environment.EnvironmentManager;
import environment.GameTime;
import environment.Lighting;
import environment.WeatherManager;
import environment.WeatherType;
import object.ItemFactory;
import object.SuperObj;
import object.TileState;
import object.LandTile; // Tambahkan import ini untuk LandTile
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

    // Weather
    public WeatherManager weatherManager;
    public WeatherType currentWeather;

    // Current map
    public String currMap;
    public String prevFarmMap = "/maps/farmmm.txt";
    private String[] farmMapVariations = {
        "/maps/farmmm.txt",
        "/maps/farmmm2.txt",
        "/maps/farmmm3.txt"
    };
    
    private Random random = new Random();

    // Game state constants
    public static final int playState = 1;
    public static final int pauseState = 2;
    public static final int dialogState = 3;
    public static final int inventoryState = 4;
    public static final int statsState = 5;
    public static final int sleepState = 6;
    public static final int cookingState = 7;
    public static final int shippingBinState = 8;
    public static final int storeState = 9;
    public static final int npcContextMenuState = 10;
    public int gameState;
    private final Object pauseLock = new Object();


    public KeyHandler keyH;
    public UI ui;
    public TileManager tileM;
    public Collision colCheck;
    public AssetSetter aSetter;
    public Player player;

    // controller
    public InventoryController inventoryController;
    public SleepController sleepController;
    public CookingController cookingController;
    public WatchingController watchingController;
    public ShippingBinController shippingBinController;
    public StoreController storeController;
    public NPCController npcController;
    public FarmingController farmingController;
    public boolean isGifting = false;

    public ItemFactory itemFactory;
    public EnvironmentManager eManager;
    public int interactionTileCol;
    public int interactionTileRow;
    
    // Arrays/List for game objects and NPCs
    // REVISI: Ubah ini menjadi ArrayList untuk fleksibilitas yang lebih baik
    public ArrayList<SuperObj> obj = new ArrayList<>(); // Ganti dari SuperObj[]
    public NPC npc[] = new NPC[6]; // NPC bisa tetap array jika jumlahnya tetap
    
    // Current interactive objects
    public SuperObj currObj;
    public NPC currNPC;

    public SaveManager saveManger; // Perhatikan nama variabel, konsistenkan jadi 'saveManager' (huruf kecil 'm')
    
    // Game thread
    private Thread gameThread;
    private int fps = 60;
    

    private boolean isComplete = false;
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        
        inventoryController = new InventoryController(this);
        cookingController = new CookingController(this);
        shippingBinController = new ShippingBinController(this);
        storeController = new StoreController(this);
        npcController = new NPCController(this);

        // Inisialisasi SaveManager di constructor
        saveManger = new SaveManager(this); // Perhatikan nama variabel 'saveManger'
        
        this.keyH = new KeyHandler(this);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        gameState = playState;

        int randIndex = random.nextInt(farmMapVariations.length);
        this.currMap = farmMapVariations[randIndex];
        this.prevFarmMap = this.currMap;
        System.out.println("Starting map: " + currMap);
        tileM = new EmptyTileManager(this);
    }
    

    public void setup() {
        colCheck = new Collision(this);
        aSetter = new AssetSetter(this);
        ui = new UI(this);
        player = new Player(this, keyH);
        tileM = new TileManager(this);
        eManager = new EnvironmentManager(this);
        itemFactory = new ItemFactory(this);
        sleepController = new SleepController(this, player);
        watchingController = new WatchingController(this);
        farmingController = new FarmingController(this, gameTime);

        // Weather
        weatherManager = new WeatherManager();
        currentWeather = weatherManager.getWeatherForDay(gameTime.getGameDay());

        player.inventory = inventoryController;
    
        tileM.setup();
        setupMap(); 
        addStartingItems();
        addStoreItems();
        eManager.setup();

        isComplete = true;

        // MUAT GAME STATE DI SINI SETELAH SEMUA OBJEK DI INISIALISASI
        // Ini akan menimpa state awal LandTile yang di-set oleh setupMap() jika ada save data
        saveManger.loadGameState(); 
    }
    
    // Setup objects dan NPC's
    public void setupMap() {
        aSetter.clearObjects(); // Pastikan method ini membersihkan 'obj' ArrayList
        aSetter.clearNPCs();
        aSetter.setObj(); // Pastikan method ini mengisi 'obj' ArrayList dengan objek baru
        aSetter.setNPC();
    }

    // Method untuk menyimpan game
    public void saveGame(){
        saveManger.saveGameState();
    }
    
    public void debugCurrentObjects() {
        System.out.println("=== CURRENT OBJECTS DEBUG ===");
        System.out.println("Current map: " + currMap);
        System.out.println("Total objects: " + obj.size());
        int landTileCount = 0;
        for (SuperObj o : obj) {
            if (o instanceof LandTile) {
                landTileCount++;
                LandTile lt = (LandTile) o;
                if (lt.getCurrentState() != TileState.LAND) {
                    System.out.println("Modified LandTile at " + lt.wX + "," + lt.wY + " - " + lt.getCurrentState());
                }
            }
        }
        System.out.println("LandTile count: " + landTileCount);
        System.out.println("=== END DEBUG ===");
    }


    // Ganti nama dari public void changeMap()
    public void changeMap(String petaLamaUntukDisimpan, String petaBaruUntukDimuat) {
        // A. Simpan state untuk peta LAMA
        String cacheCurrMapSaatIni = this.currMap; // Simpan currMap (seharusnya petaBaruUntukDimuat)
        this.currMap = petaLamaUntukDisimpan;      // Set SEMENTARA currMap ke peta LAMA agar SaveManager pakai nama file yg benar
        System.out.println("DEBUG: Akan menyimpan state untuk PETA LAMA: " + petaLamaUntukDisimpan);
        saveGame(); // Ini akan menggunakan this.currMap (petaLamaUntukDisimpan)
        this.currMap = cacheCurrMapSaatIni;        // KEMBALIKAN currMap ke peta BARU (petaBaruUntukDimuat)
        System.out.println("DEBUG: Selesai menyimpan data untuk PETA LAMA: " + petaLamaUntukDisimpan);

        // B. Pastikan this.currMap adalah peta BARU (seharusnya sudah dari TileManager)
        // System.out.println("DEBUG: Peta saat ini diset ke: " + this.currMap + " (Harusnya sama dengan petaBaruUntukDimuat: " + petaBaruUntukDimuat + ")");

        // C. Setup objek untuk peta BARU
        System.out.println("DEBUG: Melakukan setupMap() untuk PETA BARU: " + this.currMap);
        setupMap(); // AssetSetter akan menggunakan this.currMap (petaBaruUntukDimuat)

        // D. Load state untuk peta BARU
        System.out.println("DEBUG: Akan memuat state untuk PETA BARU: " + this.currMap);
        saveManger.loadGameState(); // SaveManager akan menggunakan this.currMap (petaBaruUntukDimuat)
        System.out.println("DEBUG: Selesai memuat state untuk PETA BARU: " + this.currMap);

        debugCurrentObjects(); // Untuk melihat objek di peta baru setelah load
    }
    
    // starting item buat item awal
    private void addStartingItems() {
        inventoryController.addItem(itemFactory.createTool("hoe"));
        inventoryController.addItem(itemFactory.createTool("wateringcan"));
        inventoryController.addItem(itemFactory.createTool("pickaxe"));
        inventoryController.addItem(itemFactory.createFood("salmon"));
        inventoryController.addItem(itemFactory.createFood("veggiesoup"));
        inventoryController.addItem(itemFactory.createFish("salmon"));
        inventoryController.addItem(itemFactory.createFish("salmon"));
        inventoryController.addItem(itemFactory.createFish("salmon"));
        inventoryController.addItem(itemFactory.createMiscItem("coal"));
        inventoryController.addItem(itemFactory.createSeed("tomato"));
        inventoryController.addItem(itemFactory.createMiscItem("ring"));
    }
 
    // starting item buat item awal
    private void addStoreItems() {
        storeController.addItem(itemFactory.createTool("hoe"));
        storeController.addItem(itemFactory.createTool("hoe"));
        storeController.addItem(itemFactory.createTool("wateringcan"));
        storeController.addItem(itemFactory.createTool("wateringcan"));
        storeController.addItem(itemFactory.createTool("pickaxe"));
        storeController.addItem(itemFactory.createTool("pickaxe"));
        storeController.addItem(itemFactory.createFood("veggiesoup"));
        storeController.addItem(itemFactory.createFood("veggiesoup"));
        storeController.addItem(itemFactory.createFood("salmon"));
        storeController.addItem(itemFactory.createFood("salmon"));
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

    // Optional: Method to check if game is currently paused
    public boolean isGamePaused() {
        synchronized (pauseLock) {
            return isTimePaused;
        }
    }

    // Optional: Force pause from external sources
    public void forceGamePause() {
        synchronized (pauseLock) {
            pauseGameThread();
        }
    }

    // Optional: Force resume from external sources  
    public void forceGameResume() {
        synchronized (pauseLock) {
            resumeGameThread();
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            interactionTileCol = player.interactionBox.x / tileSize;
            interactionTileRow = player.interactionBox.y / tileSize;
            tileM.checkTeleport(interactionTileCol, interactionTileRow);
            eManager.update();
            cookingController.update();
            
           
            farmingController.updatePlantGrowth(); 

            // Game time update every n frames
            currentMinute = gameTime.getGameMinute();
            currentHour = gameTime.getGameHour();
            currentDay = gameTime.getGameDay();

            if (eManager != null && eManager.isLightingSetup()) {
                Lighting lighting = eManager.getLighting();

                if (currentHour == 5 && currentMinute == 0) {
                    lighting.triggerTransition(Lighting.DAWN); // Transisi terang
                }
                else if (currentHour == 6 && currentMinute == 0) {
                    lighting.triggerTransition(Lighting.DAY); // Langsung terang penuh
                }
                else if (currentHour == 17 && currentMinute == 0) {
                    lighting.triggerTransition(Lighting.DUSK); // Transisi gelap
                }
                else if (currentHour == 18 && currentMinute == 0) {
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
                // Iterasi obj menggunakan ArrayList
                for (SuperObj objInstance : obj) { 
                    if (objInstance != null) {
                        objInstance.draw(g2, this);
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

    // public void nextDay() {
    //     gameTime.nextDay();
    //     currentWeather = weatherManager.getWeatherForDay(gameTime.getGameDay());

    //     if (currentWeather == WeatherType.RAINY) {
    //         // tileManager.waterAllSoilTiles(); // Anda bisa memanggil ini di sini jika diperlukan
    //     }

    //     // --- PENTING UNTUK STATE TANAMAN SAAT BERPINDAH HARI ---
    //     // Setelah gameTime.nextDay(), panggil saveGame() dan loadGameState()
    //     // Ini memastikan bahwa perubahan hari (yang mungkin memicu pertumbuhan tanaman)
    //     // akan disimpan dan kemudian dimuat ulang untuk memperbarui visual.
    //     // Atau, pastikan grow() di farmingController.updatePlantGrowth() sudah cukup
    //     // untuk menangani visual tanpa perlu load/save di sini.
    //     // Jika pertumbuhan tanaman sudah dihandle oleh updatePlantGrowth() berdasarkan waktu,
    //     // maka Anda tidak perlu save/load di sini, cukup pastikan grow() selalu dipanggil.

    //     // Jika Anda ingin efek save/load di setiap hari baru, maka:
    //     saveGame(); 
    //     saveManger.loadGameState(); // Muat kembali state setelah hari berganti
    // }


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
                saveGame(); // Simpan state dengan waktu baru
                saveManger.loadGameState(); // Muat kembali state untuk memperbarui visual dan logika
            }

            isTimePaused = false;
            if (gameTime != null) gameTime.resume();
        });
    }

    public void setGameState(int newState) {
        int oldState = this.gameState;
        this.gameState = newState;
        
        // Debug logging
        System.out.println("Game State Changed: " + getStateName(oldState) + " -> " + getStateName(newState));
        
        // Handle state-specific logic
        switch (newState) {
            case dialogState:
                // Ensure dialog state is properly set
                if (isTimePaused) {
                    resumeGameThread();
                }
                break;
            case npcContextMenuState:
                // Pause when entering NPC context menu
                if (!isTimePaused) {
                    pauseGameThread();
                }
                break;
            case playState:
                // Resume when returning to play state
                if (isTimePaused) {
                    resumeGameThread();
                }
                currNPC = null; // Clear current NPC when returning to play
                isGifting = false; // Clear gifting mode
                break;
        }
    }

    // Helper method untuk debug
    private String getStateName(int state) {
        switch (state) {
            case playState: return "PLAY";
            case pauseState: return "PAUSE";
            case dialogState: return "DIALOG";
            case inventoryState: return "INVENTORY";
            case statsState: return "STATS";
            case cookingState: return "COOKING";
            case shippingBinState: return "SHIPPING";
            case storeState: return "STORE";
            case npcContextMenuState: return "NPC_CONTEXT";
            case sleepState: return "SLEEP";
            default: return "UNKNOWN(" + state + ")";
        }
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