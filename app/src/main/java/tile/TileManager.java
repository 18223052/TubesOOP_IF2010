package tile;

import java.awt.Graphics2D;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {

    GamePanel gp;
    Tile[] tile;  // Array untuk menyimpan tile
    int[][] mapTileNum;  // Array untuk peta tile

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[200];  // Misalnya ada 200 tile yang disiapkan
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];  // Ukuran peta
        getTileImages();  // Memuat gambar tile dari folder
        loadMap("/maps/farm.csv");  // Ganti dengan path ke file CSV
    }

    // Memuat gambar untuk setiap tile dari folder
    public void getTileImages() {
        try {
            // Mengelompokkan tile berdasarkan folder, misalnya folder "House", "WaterTile", dll.
            loadTileFolder("/tiles/Chest", 0);
            loadTileFolder("/tiles/FarmLand", 10);
            loadTileFolder("/tiles/Fences", 20);
            loadTileFolder("/tiles/House", 30);
            loadTileFolder("/tiles/oak_tree", 40);
            loadTileFolder("/tiles/PathTile", 50);
            loadTileFolder("/tiles/WaterTile", 60);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Memuat tile dari setiap folder dan mengaitkan dengan ID yang sesuai
    public void loadTileFolder(String folderPath, int startId) throws IOException {
        File folder = new File(getClass().getResource(folderPath).getFile());
        File[] tileFiles = folder.listFiles((dir, name) -> name.endsWith(".png"));
        
        // Pastikan ada file di dalam folder
        if (tileFiles != null) {
            for (int i = 0; i < tileFiles.length; i++) {
                String tileFilePath = folderPath + "/" + tileFiles[i].getName();
                tile[startId + i] = new Tile();
                tile[startId + i].image = ImageIO.read(getClass().getResourceAsStream(tileFilePath));
            }
        }
    }

    // Membaca file CSV yang berisi ID tile
    public void loadMap(String filePath) {
        try {
            List<int[]> data = CSVReader.readCSV(filePath);  // Membaca data dari CSV

            for (int row = 0; row < gp.maxWorldRow; row++) {
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    mapTileNum[col][row] = data.get(row)[col];  // Menyimpan ID tile di mapTileNum
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Menggambar peta berdasarkan ID tile yang disimpan di mapTileNum
    public void draw(Graphics2D g2) {
        int worldcol = 0;
        int worldrow = 0;

        while (worldcol < gp.maxWorldCol && worldrow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldcol][worldrow];

            // Menghitung posisi dunia dan layar (kamera)
            int worldX = worldcol * gp.tileSize;
            int worldY = worldrow * gp.tileSize;
            int screenX = worldX - gp.player.wX + gp.player.screenX;
            int screenY = worldY - gp.player.wY + gp.player.screenY;

            // Menggambar tile
            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            worldcol++;

            if (worldcol == gp.maxWorldCol) {
                worldcol = 0;
                worldrow++;
            }
        }
    }
}
