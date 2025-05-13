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
        tile = new Tile[200];
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
        for (int i = 0; i <= 194; i++) {
            String fileName = String.format("%03d", i);
            boolean collision = (i >= 20 && i <= 32) || (i >= 65 && i <= 72) || (i >= 183 && i <= 190);
            
            setup(i, fileName, collision);
        }
   
        // Set teleport properties
        tile[33].teleport = true;
        tile[33].destMap = "/maps/rumah.txt";
        tile[33].destX = 26;
        tile[33].destY = 36;
        tile[33].sourceMap = "/maps/farmmm.txt";
   
        tile[109].teleport = true;
        tile[109].destMap = "/maps/farmmm.txt";
        tile[109].destX = 19;
        tile[109].destY = 19;
        tile[109].sourceMap = "/maps/rumah.txt";
   
        tile[116].teleport = true;
        tile[116].destMap = "/maps/river.txt";
        tile[116].destX = 23;
        tile[116].destY = 5;
        tile[116].sourceMap = "/maps/farmmm.txt";
   
        tile[179].teleport = true;
        tile[179].destMap = "/maps/farmmm.txt";
        tile[179].destX = 31;
        tile[179].destY = 47;
        tile[179].sourceMap = "/maps/river.txt";
   
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
    
    public void checkTeleport() {

        int playerCol = gp.player.wX / gp.tileSize;
        int playerRow = gp.player.wY / gp.tileSize;
        

        int tileNum = mapTileNum[playerCol][playerRow];
        if (tile[tileNum] != null && tile[tileNum].teleport) {
            if (tile[tileNum].sourceMap == null || tile[tileNum].sourceMap.equals(currentMap)) {
                teleportPlayer(tile[tileNum].destMap, tile[tileNum].destX, tile[tileNum].destY);
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