package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    private String currentMap;
    
    // // Constants for special tiles
    // public static final int POND_TILE = 18;
    // public static final int BED_TILE = 86;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[250];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        currentMap = gp.currMap;
        

        getTileImage();

    }
    
    // Call this after all components are initialized
    public void setup() {
        loadMap(currentMap);
    }

    public void getTileImage() {
        // Initialize all tile types
        for (int i = 0; i <= 201; i++) {
            String fileName = String.format("%03d", i);
            boolean collision = (i >= 20 && i <= 32) || (i >= 65 && i <= 72) || (i >= 183 && i <= 190);
            
            setup(i, fileName, collision);
        }

        tile[16].collision = true;
        tile[19].collision = true;
        tile[35].collision = true;
        tile[62].collision = true;
        tile[63].collision = true;
        tile[117].collision = true;
        tile[118].collision = true;
        tile[141].collision = true;
        tile[142].collision = true;
        tile[145].collision = true;
        tile[146].collision = true;
        tile[169].collision = true;
        tile[170].collision = true;

    }
    
    public void setup(int idx, String imagePath, boolean collision){
        UtilityTool uTool = new UtilityTool();
        try{
            tile[idx] = new Tile();
            tile[idx].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/" + imagePath + ".png"));
            tile[idx].image = uTool.scaleImage(tile[idx].image, gp.tileSize, gp.tileSize);
            tile[idx].collision = collision;
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void checkTeleport(int checkCol, int checkRow) { 
        // dari map farmm ke map lain
        if (currentMap.equals("/maps/farmmm.txt")) {
            if (checkCol == 18 && checkRow == 17) {

                teleportPlayer("/maps/rumah.txt", 26, 36);
                return;
            }
            if ((checkCol == 28 || checkCol == 29 || checkCol == 30 || checkCol == 31) && checkRow == 49) { // Contoh teleportasi dari rumah.txt ke farmmm.txt (balikan dari yang sebelumnya)
                teleportPlayer("/maps/river.txt", 22, 1);
                return;
            }
            if (checkCol == 49 && checkRow == 32) {

                teleportPlayer("/maps/worldmap.txt", 1, 30);
                return;
            }
        }

         // dari map rumah ke map lain
        if (currentMap.equals("/maps/rumah.txt")) {
            if ((checkCol == 26 || checkCol == 25 || checkCol == 27 || checkCol == 24) && checkRow == 38) {
                teleportPlayer("/maps/farmmm.txt", 18, 20);
                return;
            }
        } 
        // dari map river ke map lain
        if (currentMap.equals("/maps/river.txt")) {
            if ((checkCol == 21 || checkCol == 22 || checkCol == 23 || checkCol == 24) && checkRow == 0) {
                teleportPlayer("/maps/farmmm.txt", 31, 48);
                return;
            }
        }

        // dari map world ke map lain
        if (currentMap.equals("/maps/worldmap.txt")) {
            if (checkCol == 0 && checkRow == 30) {
                teleportPlayer("/maps/farmmm.txt", 48, 32);
                return;
            }
        }
        
    
    }

    
    public void teleportPlayer(String mapPath, int destX, int destY) {

        currentMap = mapPath;
        gp.currMap = mapPath;
        

        loadMap(mapPath);
        
        // Set player position
        gp.player.wX = destX * gp.tileSize;
        gp.player.wY = destY * gp.tileSize;
        

        gp.changeMap();
    }
    
    public void loadMap(String filePath) {
        try {
            InputStream io = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(io));
    
            int col = 0;
            int row = 0;
    
            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                
                if (line == null) {
                    break; 
                }
    
                while(col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");
                    
                    if (col >= numbers.length) {
                        break; 
                    }
                    
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
            
            // Update the current map
            currentMap = filePath;
            
            // gp.setupExceptEnvironment();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldcol = 0;
        int worldrow = 0;

        while (worldcol < gp.maxWorldCol && worldrow < gp.maxWorldRow){
            int tileNum = mapTileNum[worldcol][worldrow];

            // Camera setting
            int worldX = worldcol * gp.tileSize;
            int worldY = worldrow * gp.tileSize;
            int screenX = worldX - gp.player.wX + gp.player.screenX;
            int screenY = worldY - gp.player.wY + gp.player.screenY;
            
            // Only draw tiles visible on screen
            if (screenX > -gp.tileSize && screenX < gp.screenWidth && 
                screenY > -gp.tileSize && screenY < gp.screenHeight) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            
            worldcol++;

            if (worldcol == gp.maxWorldCol){
                worldcol = 0;
                worldrow++;
            }
        }
    }
}