package main;

import entity.Character; 


public class Collision {

    GamePanel gp;

    public Collision(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Character character) { 
        int entityLeftWorldX = character.wX + character.solid.x;
        int entityRightWorldX = character.wX + character.solid.x + character.solid.width;
        int entityTopWorldY = character.wY + character.solid.y;
        int entityBottomWorldY = character.wY + character.solid.y + character.solid.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;
        character.iscollision = false; 

        switch (character.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - character.speed) / gp.tileSize;
                // Ensure array bounds are not exceeded
                if (entityLeftCol >= 0 && entityTopRow >= 0 && entityLeftCol < gp.tileM.mapTileNum.length && entityTopRow < gp.tileM.mapTileNum[0].length &&
                    entityRightCol >= 0 && entityTopRow >= 0 && entityRightCol < gp.tileM.mapTileNum.length && entityTopRow < gp.tileM.mapTileNum[0].length) {
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        character.iscollision = true;
                    }
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + character.speed) / gp.tileSize;
                if (entityLeftCol >= 0 && entityBottomRow >= 0 && entityLeftCol < gp.tileM.mapTileNum.length && entityBottomRow < gp.tileM.mapTileNum[0].length &&
                    entityRightCol >= 0 && entityBottomRow >= 0 && entityRightCol < gp.tileM.mapTileNum.length && entityBottomRow < gp.tileM.mapTileNum[0].length) {
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        character.iscollision = true;
                    }
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - character.speed) / gp.tileSize;
                 if (entityLeftCol >= 0 && entityTopRow >= 0 && entityLeftCol < gp.tileM.mapTileNum.length && entityTopRow < gp.tileM.mapTileNum[0].length &&
                    entityLeftCol >= 0 && entityBottomRow >= 0 && entityLeftCol < gp.tileM.mapTileNum.length && entityBottomRow < gp.tileM.mapTileNum[0].length) {
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        character.iscollision = true;
                    }
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + character.speed) / gp.tileSize;
                if (entityRightCol >= 0 && entityTopRow >= 0 && entityRightCol < gp.tileM.mapTileNum.length && entityTopRow < gp.tileM.mapTileNum[0].length &&
                    entityRightCol >= 0 && entityBottomRow >= 0 && entityRightCol < gp.tileM.mapTileNum.length && entityBottomRow < gp.tileM.mapTileNum[0].length) {
                    tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        character.iscollision = true;
                    }
                }
                break;
        }
    }
}