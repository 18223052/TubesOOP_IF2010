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
    Tile[] tile;
    int mapTileNum [][];

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/farm.txt");
    }

    public void getTileImage(){
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/035.png"));
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/od/House.png"));
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/016.png"));
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/017.png"));
        } catch (IOException e){
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
                // x = 0;
                worldrow++;
                // y += gp.tileSize;
            }
        }
    }
    
    
}
