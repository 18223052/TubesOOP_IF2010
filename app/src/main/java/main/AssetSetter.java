package main;
import entity.NPC_mayortadi;
import object.Bed;
import object.ShippingBin;
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
        else if(gp.currMap.equals("/maps/river.txt")) {
            // Set objects for river map
            // Add river-specific objects here
        }
        else if(gp.currMap.equals("/maps/rumah.txt")) {

            
            // Adding the bed
            gp.obj[0] = new Bed(gp);
            gp.obj[0].wX = 18 * gp.tileSize;
            gp.obj[0].wY = 15 * gp.tileSize;
            
            // Adding the stove (which is now a multi-tile object)
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
            gp.npc[0] = new NPC_mayortadi(gp);
            gp.npc[0].wX = gp.tileSize*16;
            gp.npc[0].wY = gp.tileSize*20;
        }
        else if(gp.currMap.equals("/maps/river.txt")) {
            // Set NPCs for river map
        }
        // Add more map conditions as needed
    }
}