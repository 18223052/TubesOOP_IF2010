package main;
import entity.NPC_mayortadi;

import object.Bed;
import object.ShippingBin;
import object.Store;
import object.Stove;
import object.TV;

public class AssetSetter {
    
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    
    public void clearObjects() {
        for(int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }
    }
    
    public void clearNPCs() {
        for(int i = 0; i < gp.npc.length; i++) {
            gp.npc[i] = null;
        }
    }

    public void setObj(){

        if(gp.currMap.equals("/maps/farmmm.txt")) {
   
            gp.obj[0] = new ShippingBin(gp);
            gp.obj[0].wX = 20 * gp.tileSize;
            gp.obj[0].wY = 20 * gp.tileSize;
        }

        if(gp.currMap.equals("/maps/farmmm2.txt")) {
            gp.obj[0] = new ShippingBin(gp);
            gp.obj[0].wX = 20 * gp.tileSize;
            gp.obj[0].wY = 30 * gp.tileSize;
        }
        if(gp.currMap.equals("/maps/farmmm3.txt")) {
            gp.obj[0] = new ShippingBin(gp);
            gp.obj[0].wX = 33 * gp.tileSize;
            gp.obj[0].wY = 30 * gp.tileSize;
        }

        else if(gp.currMap.equals("/maps/worldmap.txt")) {
            gp.obj[0] = new Store(gp);
            gp.obj[0].wX = 33 * gp.tileSize;
            gp.obj[0].wY = 30 * gp.tileSize;
        }
        else if(gp.currMap.equals("/maps/rumah.txt")) {

            
            // Adding the bed
            gp.obj[0] = new Bed(gp);
            gp.obj[0].wX = 18 * gp.tileSize;
            gp.obj[0].wY = 15 * gp.tileSize;

            gp.obj[1] = new Stove(gp);
            gp.obj[1].wX = 34 * gp.tileSize;
            gp.obj[1].wY = 14 * gp.tileSize;

            gp.obj[2] = new Bed(gp);
            gp.obj[2].wX = 16 * gp.tileSize;
            gp.obj[2].wY = 15 * gp.tileSize;

            gp.obj[3] = new TV(gp);
            gp.obj[3].wX = 19 * gp.tileSize;
            gp.obj[3].wY = 20 * gp.tileSize;
        }

    }

    public void setNPC(){
        if (gp.currMap.equals("/maps/farmmm.txt")) {
            gp.npc[0] = new NPC_mayortadi(gp, gp.itemFactory);
            gp.npc[0].wX = gp.tileSize*16;
            gp.npc[0].wY = gp.tileSize*20;
        }
        else if(gp.currMap.equals("/maps/worldmap.txt")) {
            // Set NPCs for river map
            // gp.npc[0] = new NPC_caroline(gp);
            // gp.npc[0].wX = gp.tileSize*18;
            // gp.npc[0].wY = gp.tileSize*29;

            // gp.npc[1] = new NPC_abigail(gp);
            // gp.npc[1].wX = gp.tileSize*19;
            // gp.npc[1].wY = gp.tileSize*29;

            // gp.npc[2] = new NPC_perry(gp);
            // gp.npc[2].wX = gp.tileSize*20;
            // gp.npc[2].wY = gp.tileSize*29;

            // gp.npc[3] = new NPC_dasco(gp);
            // gp.npc[3].wX = gp.tileSize*21;
            // gp.npc[3].wY = gp.tileSize*29;
        }
        // Add more map conditions as needed
    }
}