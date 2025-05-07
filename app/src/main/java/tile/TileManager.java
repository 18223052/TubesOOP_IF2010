package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[200];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/farmmm.txt");
    }

    public void getTileImage() {
        try {
            // for (int i = 0; i <= 190; i++) {
            //     tile[i] = new Tile();
                
            //     // Format the number to ensure 3 digits (e.g., "052", "053", ...)
            //     String filename = String.format("/tutor_tiles/%03d.png", i); 
                
            //     // Load the image for the tile
            //     tile[i].image = ImageIO.read(getClass().getResourceAsStream(filename));
                
            //     // Set the collision flag (You can modify this logic based on your requirement)
            //     if (i >= 16 && i <= 35 || i >= 50 && i <= 64) { // Example collision logic
            //         tile[i].collision = false;
            //     } else {
            //         tile[i].collision = false;
            //     }
            // }
            
            // Tile 0 - 15: Semua collision = false
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/000.png"));
            tile[0].collision = false;
    
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/001.png"));
            tile[1].collision = false;
    
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/002.png"));
            tile[2].collision = false;
    
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/003.png"));
            tile[3].collision = false;
    
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/004.png"));
            tile[4].collision = false;
    
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/005.png"));
            tile[5].collision = false;
    
            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/006.png"));
            tile[6].collision = false;
    
            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/007.png"));
            tile[7].collision = false;
    
            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/008.png"));
            tile[8].collision = false;
    
            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/009.png"));
            tile[9].collision = false;
    
            tile[10] = new Tile();
            tile[10].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/010.png"));
            tile[10].collision = false;
    
            tile[11] = new Tile();
            tile[11].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/011.png"));
            tile[11].collision = false;
    
            tile[12] = new Tile();
            tile[12].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/012.png"));
            tile[12].collision = false;
    
            tile[13] = new Tile();
            tile[13].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/013.png"));
            tile[13].collision = false;
    
            tile[14] = new Tile();
            tile[14].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/014.png"));
            tile[14].collision = false;
    
            tile[15] = new Tile();
            tile[15].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/015.png"));
            tile[15].collision = false;
    
            // Tile 16 - 35: Collision = true
            tile[16] = new Tile();
            tile[16].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/016.png"));
            tile[16].collision = true;
    
            tile[17] = new Tile();
            tile[17].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/017.png"));
            tile[17].collision = false;
    
            tile[18] = new Tile();
            tile[18].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/018.png"));
            tile[18].collision = true;
    
            tile[19] = new Tile();
            tile[19].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/019.png"));
            tile[19].collision = false;
    
            tile[20] = new Tile();
            tile[20].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/020.png"));
            tile[20].collision = true;
    
            tile[21] = new Tile();
            tile[21].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/021.png"));
            tile[21].collision = true;
    
            tile[22] = new Tile();
            tile[22].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/022.png"));
            tile[22].collision = true;
    
            tile[23] = new Tile();
            tile[23].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/023.png"));
            tile[23].collision = true;
    
            tile[24] = new Tile();
            tile[24].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/024.png"));
            tile[24].collision = true;
    
            tile[25] = new Tile();
            tile[25].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/025.png"));
            tile[25].collision = true;
    
            tile[26] = new Tile();
            tile[26].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/026.png"));
            tile[26].collision = true;
    
            tile[27] = new Tile();
            tile[27].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/027.png"));
            tile[27].collision = true;
    
            tile[28] = new Tile();
            tile[28].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/028.png"));
            tile[28].collision = true;
    
            tile[29] = new Tile();
            tile[29].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/029.png"));
            tile[29].collision = true;
    
            tile[30] = new Tile();
            tile[30].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/030.png"));
            tile[30].collision = true;
    
            tile[31] = new Tile();
            tile[31].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/031.png"));
            tile[31].collision = true;
    
            tile[32] = new Tile();
            tile[32].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/032.png"));
            tile[32].collision = true;
    
            tile[33] = new Tile();
            tile[33].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/033.png"));
            tile[33].collision = false;
    
            tile[34] = new Tile();
            tile[34].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/034.png"));
            tile[34].collision = false;
    
            tile[35] = new Tile();
            tile[35].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/035.png"));
            tile[35].collision = true;
    
            // Tile 36 - 50: Semua collision = false
            tile[36] = new Tile();
            tile[36].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/036.png"));
            tile[36].collision = false;
    
            tile[37] = new Tile();
            tile[37].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/037.png"));
            tile[37].collision = false;
    
            tile[38] = new Tile();
            tile[38].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/38.png"));
            tile[38].collision = false;
    
            tile[39] = new Tile();
            tile[39].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/39.png"));
            tile[39].collision = false;
    
            tile[40] = new Tile();
            tile[40].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/40.png"));
            tile[40].collision = false;
    
            tile[41] = new Tile();
            tile[41].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/41.png"));
            tile[41].collision = false;
    
            tile[42] = new Tile();
            tile[42].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/42.png"));
            tile[42].collision = false;
    
            tile[43] = new Tile();
            tile[43].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/43.png"));
            tile[43].collision = false;
    
            tile[44] = new Tile();
            tile[44].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/44.png"));
            tile[44].collision = false;
    
            tile[45] = new Tile();
            tile[45].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/45.png"));
            tile[45].collision = false;
    
            tile[46] = new Tile();
            tile[46].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/46.png"));
            tile[46].collision = false;
    
            tile[47] = new Tile();
            tile[47].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/47.png"));
            tile[47].collision = false;
    
            tile[48] = new Tile();
            tile[48].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/48.png"));
            tile[48].collision = false;
    
            tile[49] = new Tile();
            tile[49].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/49.png"));
            tile[49].collision = false;
    
            tile[50] = new Tile();
            tile[50].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/50.png"));
            tile[50].collision = false;

            tile[51] = new Tile();
            tile[51].image = ImageIO.read(getClass().getResourceAsStream("/tutor_tiles/51.png"));
            tile[51].collision = false;
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    public void loadMap(String filePath){
        try{
            InputStream io = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(io));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine();

                while(col < gp.maxWorldCol){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col ++;
                }
                if (col == gp.maxWorldCol){
                    col = 0;
                    row ++;
                }
            }
            br.close();

        } catch (Exception e){

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
            
            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
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