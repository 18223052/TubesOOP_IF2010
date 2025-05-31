package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

import entity.Player;

import java.util.Map;
import java.util.HashMap;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    private String currentMap;

    private Map<String, int[]> farmMapTeleportCoords;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[250];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        currentMap = gp.currMap;
        
        initializeFarmMapTeleportCoords();
        getTileImage();

    }
    
    public void setCurrentMap(String mapPath){
        this.currentMap = mapPath;
    }

    public void setup() {
        loadMap(currentMap);
    }

    public void getTileImage() {
        // Initialize all tile types
        for (int i = 0; i <= 203; i++) {
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
        tile[200].collision = true;


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
    
    private void initializeFarmMapTeleportCoords() {
        farmMapTeleportCoords = new HashMap<>();


        farmMapTeleportCoords.put("/maps/farmmm.txt", new int[]{
            18, 17, // Exit to Rumah
            28, 49, // Exit to River (using 28 for range, the row 49 is common)
            49, 32, // Exit to Worldmap
            // --- Return to Farm (these are the coordinates player lands on when returning to farmmm.txt) ---
            18, 20, // Return from Rumah to farmmm.txt
            31, 48, // Return from River to farmmm.txt (using 31 as specific col within range)
            48, 32  // Return from Worldmap to farmmm.txt
        });

        farmMapTeleportCoords.put("/maps/farmmm2.txt", new int[]{
            17, 29, // Exit to Rumah (specific to farmmm2)
            28, 49, // Exit to River (can be same or different)
            49, 32, // Exit to Worldmap (can be same or different)
            // --- Return to Farm (these are the coordinates player lands on when returning to farmmm2.txt) ---
            17, 30, // Return from Rumah to farmmm2.txt
            31, 48, // Return from River to farmmm2.txt
            48, 32  // Return from Worldmap to farmmm2.txt
        });

        farmMapTeleportCoords.put("/maps/farmmm3.txt", new int[]{
            30, 29, // Exit to Rumah (specific to farmmm3)
            28, 49, // Exit to River (can be same or different)
            49, 32, // Exit to Worldmap (can be same or different)
            // --- Return to Farm (these are the coordinates player lands on when returning to farmmm3.txt) ---
            30, 30, // Return from Rumah to farmmm3.txt
            31, 48, // Return from River to farmmm3.txt
            48, 32  // Return from Worldmap to farmmm3.txt
        });
    }
    
    public void checkTeleport(int checkCol, int checkRow) { 

        if (gp.currMap.startsWith("/maps/farmmm")) {
            int[] coords = farmMapTeleportCoords.get(gp.currMap);
            if (coords != null) {
                // To Rumah
                if (checkCol == coords[0] && checkRow == coords[1]) {
                    gp.prevFarmMap = gp.currMap; 
                    teleportPlayer("/maps/rumah.txt", 26, 36); 
                    return;
                }

                if ((checkCol >= coords[2] && checkCol <= coords[2] + 3) && checkRow == coords[3]) {
                    gp.prevFarmMap = gp.currMap; 
                    teleportPlayer("/maps/river.txt", 22, 1); 
                    return;
                }
                // To Worldmap
                if (checkCol == coords[4] && checkRow == coords[5]) {
                    gp.prevFarmMap = gp.currMap; 
                    teleportPlayer("/maps/worldmap.txt", 1, 30); 
                    return;
                }
            }
        }

        if (gp.currMap.equals("/maps/rumah.txt")) {
            if ((checkCol >= 24 && checkCol <= 27) && checkRow == 38) { 
                int[] coords = farmMapTeleportCoords.get(gp.prevFarmMap);
                if (coords != null) {
                    teleportPlayer(gp.prevFarmMap, coords[6], coords[7]); 
                } else {

                    System.err.println("Error: prevFarmMap not found or invalid. Defaulting to farmmm.txt.");
                    teleportPlayer("/maps/farmmm.txt", 18, 20);
                }
                return;
            }

        } 
        
        if (gp.currMap.equals("/maps/river.txt")) {
            if ((checkCol >= 21 && checkCol <= 24) && checkRow == 0) { 
                int[] coords = farmMapTeleportCoords.get(gp.prevFarmMap);
                if (coords != null) {
                    teleportPlayer(gp.prevFarmMap, coords[8], coords[9]);
                } else {
                    System.err.println("Error: prevFarmMap not found or invalid. Defaulting to farmmm.txt.");
                    teleportPlayer("/maps/farmmm.txt", 31, 48); 
                }
                return;
            }
        }

        if (gp.currMap.equals("/maps/worldmap.txt")) {
            if (checkCol == 0 && checkRow == 30) { 
                int[] coords = farmMapTeleportCoords.get(gp.prevFarmMap);
                if (coords != null) {
                    teleportPlayer(gp.prevFarmMap, coords[10], coords[11]); 
                } else {
                    System.err.println("Error: prevFarmMap not found or invalid. Defaulting to farmmm.txt.");
                    teleportPlayer("/maps/farmmm.txt", 48, 32); 
                }
                return;
            }
            

            if ((checkCol == 24 || checkCol == 25) && checkRow == 50){
                teleportPlayer("/maps/lake.txt", 25, 2);
                return;
            }

            if (checkCol == 18 && checkRow == 28){
                teleportPlayer("/maps/rumahabigail.txt", 28, 30);
                return;
            }

            if (checkCol == 21 && checkRow == 28){
                teleportPlayer("/maps/rumahmayortadi.txt", 28, 30);
                return;
            }

            if (checkCol == 21 && checkRow == 18){
                teleportPlayer("/maps/rumahdasco.txt", 28, 30);
                return;
            }

            if (checkCol == 18 && checkRow == 17){
                teleportPlayer("/maps/rumahperry.txt", 28, 30);
                return;
            }
        }

    if (gp.currMap.equals("/maps/lake.txt")){
        if ((checkCol == 25 || checkCol == 26) && checkRow == 0){
            teleportPlayer("/maps/worldmap.txt", 24 , 48);
            return;
        }
    }

    if (gp.currMap.equals("/maps/rumahperry.txt")){
        if ((checkCol == 27||checkCol == 28) && checkRow == 32){
            teleportPlayer("/maps/worldmap.txt", 18, 18);
        }
    }

    if (gp.currMap.equals("/maps/rumahdasco.txt")){
        if ((checkCol == 27||checkCol == 28) && checkRow == 32){
            teleportPlayer("/maps/worldmap.txt", 21, 18);
        }
    }

    if (gp.currMap.equals("/maps/rumahcaroline.txt")){
        if ((checkCol == 27||checkCol == 28) && checkRow == 32){
            teleportPlayer("/maps/worldmap.txt", 19, 21);
        }
    }

    if (gp.currMap.equals("/maps/rumahabigail.txt")){
        if ((checkCol == 27||checkCol == 28) && checkRow == 32){
            teleportPlayer("/maps/worldmap.txt", 18, 29);
        }
    }

    if (gp.currMap.equals("/maps/rumahmayortadi.txt")){
        if ((checkCol == 27||checkCol == 28) && checkRow == 32){
            teleportPlayer("/maps/worldmap.txt", 21, 29);
        }
    }

    }

    
    public void teleportPlayer(String newMapPath, int destX, int destY) {

        if (gp.player == null || gp.ui == null || gp.gameTime == null || gp.currMap == null || gp.keyH == null) {
            System.err.println("TeleportController: Teleportasi DIBATALKAN (silent) - komponen penting null."); // Log untuk developer
            if (gp.keyH != null) {
                gp.keyH.interactPressed = false; 
            }
            return; 
        }

        String mapPathYangAkanDisimpan = gp.currMap;
    

        boolean isTeleportCostly = !mapPathYangAkanDisimpan.equals("/maps/rumah.txt") && !newMapPath.equals("/maps/rumah.txt");

        if (isTeleportCostly) {
            final int TELEPORT_ENERGY_COST = 10; 
            int energyAfterTeleport = gp.player.getEnergy() - TELEPORT_ENERGY_COST;


            if (energyAfterTeleport < Player.MIN_ENERGY_BEFORE_SLEEP) {
                System.out.println("DEBUG (Teleport): Teleportasi DIBATALKAN (silent) - energi tidak cukup. Energi saat ini: " + gp.player.getEnergy()); // Log untuk developer
                
                gp.keyH.interactPressed = false; 

                return; 
            }
        }


        System.out.println("DEBUG (Teleport): Memproses teleportasi ke: " + newMapPath); 

        gp.player.wX = destX * gp.tileSize;
        gp.player.wY = destY * gp.tileSize;

        gp.changeMap(mapPathYangAkanDisimpan, newMapPath);

        gp.player.incrementVisitingCount();

        if (isTeleportCostly) {
            final int TELEPORT_ENERGY_COST = 10;
            final int TELEPORT_TIME_COST = 15;  
            gp.player.deductEnergy(TELEPORT_ENERGY_COST);
            gp.gameTime.addTime(TELEPORT_TIME_COST);
        }
        System.out.println("Teleportasi berhasil ke: " + newMapPath + " di (" + destX + "," + destY + ")");
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
            
     
            currentMap = filePath;
            
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldcol = 0;
        int worldrow = 0;

        while (worldcol < gp.maxWorldCol && worldrow < gp.maxWorldRow){
            int tileNum = mapTileNum[worldcol][worldrow];


            int worldX = worldcol * gp.tileSize;
            int worldY = worldrow * gp.tileSize;
            int screenX = worldX - gp.player.wX + gp.player.screenX;
            int screenY = worldY - gp.player.wY + gp.player.screenY;
            

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