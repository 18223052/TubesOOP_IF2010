package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize*scale; // 32 x 32
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int worldWidth = tileSize*maxWorldCol;
    public final int worldHeight = tileSize*maxScreenRow;

    //FPS
    int fps = 60;

    TileManager tileM = new TileManager(this);

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this,keyH);


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
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);
        player.draw(g2);
        g2.dispose();
    }

}
