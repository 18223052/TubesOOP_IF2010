package main;

import java.awt.Rectangle;

import entity.Entity;
import object.SuperObj;

public class Collision {

    GamePanel gp;

    public Collision(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.wX + entity.solid.x;
        int entityRightWorldX = entity.wX + entity.solid.x + entity.solid.width;
        int entityTopWorldY = entity.wY + entity.solid.y;
        int entityBottomWorldY = entity.wY + entity.solid.y + entity.solid.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction){
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.iscollision = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.iscollision = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.iscollision = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true){
                    entity.iscollision = true;
                }
                break;
            
        }
    }

    // public boolean checkObject(Entity entity, SuperObj obj) {
    //     if (obj == null) return false;
        
    //     // Get entity's solid area position
    //     entity.solidAreaDefaultX = entity.wX + entity.solidAreaDefaultX;
    //     entity.solidAreaDefaultY = entity.wY + entity.solidAreaDefaultY;
        
    //     // Get object's solid area position
    //     Rectangle objArea = new Rectangle(
    //         obj.wX + obj.solidArea.x,
    //         obj.wY + obj.solidArea.y,
    //         obj.solidArea.width,
    //         obj.solidArea.height
    //     );
        
    //     entity.solidAreaDefaultX = entity.solidAreaDefaultX;
    //     entity.solidAreaDefaultY = entity.solidAreaDefaultY;
        
    //     return entity.solidArea.intersects(objArea);
    // }
}
