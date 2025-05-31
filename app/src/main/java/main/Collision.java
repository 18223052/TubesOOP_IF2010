package main;

import entity.Character;

public class Collision {

    GamePanel gp;

    public Collision(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Character character) {
        // Dapatkan koordinat dunia dari solid area karakter
        int entityLeftWorldX = character.wX + character.solid.x;
        int entityRightWorldX = character.wX + character.solid.x + character.solid.width;
        int entityTopWorldY = character.wY + character.solid.y;
        int entityBottomWorldY = character.wY + character.solid.y + character.solid.height;

        // Hitung koordinat tile saat ini (digunakan untuk pengecekan tile nanti jika perlu)
        // Variabel ini TIDAK diubah untuk arah, yang diubah adalah 'future' tile coordinate
        int currentEntityLeftCol = entityLeftWorldX / gp.tileSize;
        int currentEntityRightCol = entityRightWorldX / gp.tileSize;
        int currentEntityTopRow = entityTopWorldY / gp.tileSize;
        int currentEntityBottomRow = entityBottomWorldY / gp.tileSize;

        // Variabel untuk menyimpan tile yang akan dicek
        int tileNum1, tileNum2;

        // Reset status collision karakter
        character.iscollision = false;

        // Definisikan batas peta dalam tile (0 sampai 49 untuk peta 50x50)
        // Asumsi gp.tileM.mapTileNum[0].length adalah lebar peta (misal, 50)
        // dan gp.tileM.mapTileNum.length adalah tinggi peta (misal, 50)
        // NAMUN, perhatikan: gp.tileM.mapTileNum[col][row] berarti mapTileNum.length adalah jumlah kolom (lebar)
        // dan mapTileNum[0].length adalah jumlah baris (tinggi).
        // Mari kita asumsikan map Anda adalah gp.tileM.mapTileNum[MAX_COL][MAX_ROW]
        // jadi MAX_COL = 50 dan MAX_ROW = 50

        // Sebaiknya dapatkan ukuran peta dari TileManager atau GamePanel jika memungkinkan
        // Misalnya, jika TileManager Anda punya variabel:
        // public final int maxWorldCol = 50;
        // public final int maxWorldRow = 50;
        // Maka Anda bisa gunakan gp.tileM.maxWorldCol dan gp.tileM.maxWorldRow

        // Untuk contoh ini, kita hardcode batasnya, tapi lebih baik ambil dari TileManager
        int mapMaxCol = 49; // Indeks kolom terakhir (0 hingga 49 untuk 50 kolom)
        int mapMaxRow = 49; // Indeks baris terakhir (0 hingga 49 untuk 50 baris)


        // Variabel untuk menyimpan koordinat tile di posisi BERIKUTNYA
        int futureTopRow = currentEntityTopRow;
        int futureBottomRow = currentEntityBottomRow;
        int futureLeftCol = currentEntityLeftCol;
        int futureRightCol = currentEntityRightCol;


        switch (character.direction) {
            case "up":
                futureTopRow = (entityTopWorldY - character.speed) / gp.tileSize;

                if (futureTopRow < 0) {
                    character.iscollision = true;
                } else {

                    if (currentEntityLeftCol >= 0 && currentEntityLeftCol <= mapMaxCol &&
                        currentEntityRightCol >= 0 && currentEntityRightCol <= mapMaxCol) {
                        tileNum1 = gp.tileM.mapTileNum[currentEntityLeftCol][futureTopRow];
                        tileNum2 = gp.tileM.mapTileNum[currentEntityRightCol][futureTopRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            character.iscollision = true;
                        }
                    } else {
                       
                        character.iscollision = true; 
                    }
                }
                break;

            case "down":
                futureBottomRow = (entityBottomWorldY + character.speed) / gp.tileSize;

                if (futureBottomRow > mapMaxRow) { 
                    character.iscollision = true;
                } else {

                    if (currentEntityLeftCol >= 0 && currentEntityLeftCol <= mapMaxCol &&
                        currentEntityRightCol >= 0 && currentEntityRightCol <= mapMaxCol) {
                        tileNum1 = gp.tileM.mapTileNum[currentEntityLeftCol][futureBottomRow];
                        tileNum2 = gp.tileM.mapTileNum[currentEntityRightCol][futureBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            character.iscollision = true;
                        }
                    } else {
                        character.iscollision = true;
                    }
                }
                break;

            case "left":
                futureLeftCol = (entityLeftWorldX - character.speed) / gp.tileSize;

                if (futureLeftCol < 0) { 
                    character.iscollision = true;
                } else {
                    // Jika tidak keluar batas, cek collision tile seperti biasa
                    if (currentEntityTopRow >= 0 && currentEntityTopRow <= mapMaxRow &&
                        currentEntityBottomRow >= 0 && currentEntityBottomRow <= mapMaxRow) {
                        tileNum1 = gp.tileM.mapTileNum[futureLeftCol][currentEntityTopRow];
                        tileNum2 = gp.tileM.mapTileNum[futureLeftCol][currentEntityBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            character.iscollision = true;
                        }
                    } else {
                        character.iscollision = true;
                    }
                }
                break;

            case "right":
                futureRightCol = (entityRightWorldX + character.speed) / gp.tileSize;

                if (futureRightCol > mapMaxCol) { // Akan keluar batas kanan
                    character.iscollision = true;
                } else {
                    // Jika tidak keluar batas, cek collision tile seperti biasa
                    if (currentEntityTopRow >= 0 && currentEntityTopRow <= mapMaxRow &&
                        currentEntityBottomRow >= 0 && currentEntityBottomRow <= mapMaxRow) {
                        tileNum1 = gp.tileM.mapTileNum[futureRightCol][currentEntityTopRow];
                        tileNum2 = gp.tileM.mapTileNum[futureRightCol][currentEntityBottomRow];
                        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                            character.iscollision = true;
                        }
                    } else {
                        character.iscollision = true;
                    }
                }
                break;
        }
    }
}