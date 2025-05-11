package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entity.Player;
import environment.EnvironmentManager;
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

    //FPS
    int fps = 60;

    public TileManager tileM = new TileManager(this);

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Collision colCheck = new Collision(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this,keyH);
    public SuperObj obj[] = new SuperObj[100];
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    EnvironmentManager eManager = new EnvironmentManager(this);


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
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setup(){
        aSetter.setObj();

        eManager.setup();
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
        player.update();

        tileM.checkTeleport();
        eManager.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);
        for (int i = 0; i < obj.length; i++){
            if(obj[i] != null){
                obj[i].draw(g2, this);
            }
        }
        player.draw(g2);

        eManager.draw(g2);

        g2.dispose();
    }

}
