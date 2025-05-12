package tile;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import javax.imageio.ImageIO;

import main.AssetSetter;
import main.GamePanel;
import main.UtilityTool;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    private String currentMap = "/maps/farmmm.txt";
    public AssetSetter aSetter = new AssetSetter(gp);
    //debug
    public static final int POND_TILE = 18;
    public static final int BED_TILE = 86;


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[200];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/farmmm.txt");
    }

    public void getTileImage() {

        for (int i = 0; i <= 194; i++) {
            String fileName = String.format("%03d", i); // Format angka untuk memastikan 3 digit (misal 000, 001, ..., 194)
            boolean collision = (i >= 20 && i <= 32) || (i >= 65 && i <= 72) || (i >= 183 && i <= 190);
            

            setup(i, fileName, collision);
        }
   

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
        // Dapatkan posisi player dalam koordinat tile
        int playerCol = gp.player.wX / gp.tileSize;
        int playerRow = gp.player.wY / gp.tileSize;
        

        int tileNum = mapTileNum[playerCol][playerRow];
        if (tile[tileNum] != null && tile[tileNum].teleport) {

            if (tile[tileNum].sourceMap == null || tile[tileNum].sourceMap.equals(currentMap)) {
                teleportPlayer(tile[tileNum].destMap, tile[tileNum].destX, tile[tileNum].destY);
                // gp.eManager.saveLightingState();
            }

            teleportPlayer(tile[tileNum].destMap, tile[tileNum].destX, tile[tileNum].destY);
        }
    }
    

    public void teleportPlayer(String mapPath, int destX, int destY) {
        // Simpan posisi kamera sebelumnya
        // int prevWorldX = gp.player.wX;
        // int prevWorldY = gp.player.wY;
        
        // Load map baru
        loadMap(mapPath);
        

        gp.player.wX = destX * gp.tileSize;
        gp.player.wY = destY * gp.tileSize;
        
        gp.player.screenX = gp.screenWidth/2 - (gp.tileSize/2);
        gp.player.screenY = gp.screenHeight/2 - (gp.tileSize/2);
        

        gp.currMap = mapPath;
        

        gp.aSetter.clearObjects();
        gp.aSetter.clearNPCs();
        gp.setup();
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
            
            // Perbarui currentMap
            currentMap = filePath;

            gp.setupExceptEnvironment();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g2){
        int worldcol =0 ;
        int worldrow = 0;
        // int x = 0;
        // int y = 0;

        while (worldcol < gp.maxWorldCol && worldrow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldcol][worldrow];

            //camera setting
            int worldX = worldcol * gp.tileSize;
            int worldY = worldrow * gp.tileSize;
            int screenX = worldX - gp.player.wX + gp.player.screenX;
            int screenY = worldY - gp.player.wY + gp.player.screenY;
            
            g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            worldcol++;
            // x += gp.tileSize;

            if (worldcol == gp.maxWorldCol){
                worldcol = 0;
                // x = 0;c
                worldrow++;
                // y += gp.tileSize;
            }
        }
    }
    
    
}