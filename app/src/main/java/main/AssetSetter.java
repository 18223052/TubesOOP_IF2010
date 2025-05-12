package main;
import entity.NPC_mayortadi;
import object.Bed;
import object.ShippingBin;
import object.Soil;

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
        // First check which map is active
        if(gp.currMap.equals("/maps/farmmm.txt")) {
            // Set objects for farm map
            gp.obj[0] = new ShippingBin();
            gp.obj[0].wX = 20 * gp.tileSize;
            gp.obj[0].wY = 20 * gp.tileSize;

            gp.obj[0] = new Soil();
            gp.obj[0].wX = 23 * gp.tileSize;
            gp.obj[0].wY = 24 * gp.tileSize;

            // System.out.println("Objek ShippingBin diinisialisasi pada koordinat: " + gp.obj[0].wX + ", " + gp.obj[0].wY);
        }
        else if(gp.currMap.equals("/maps/river.txt")) {
            // Set objects for river map
            // gp.obj[0] = new Pond();
            // gp.obj[0].wX = 21 * gp.tileSize;
            // gp.obj[0].wY = 30 * gp.tileSize;
            // System.out.println("Objek ShippingBin diinisialisasi pada koordinat: " + gp.obj[0].wX + ", " + gp.obj[0].wY);
        }
        else if(gp.currMap.equals("/maps/rumah.txt")) {
            gp.obj[0] = new Bed();
            gp.obj[0].wX = 19 * gp.tileSize;
            gp.obj[0].wY = 16 * gp.tileSize;
            System.out.println("Objek Bed diinisialisasi pada koordinat: " + gp.obj[0].wX + ", " + gp.obj[0].wY);
            // Set objects for house map
            // Add your house-specific objects here
        }
        // Add more map conditions as needed
    }

    public void setNPC(){
        if (gp.currMap.equals("/maps/farmmm.txt")) {
            // No semicolon here - the bug is fixed
            gp.npc[0] = new NPC_mayortadi(gp);
            gp.npc[0].wX = gp.tileSize*16;
            gp.npc[0].wY = gp.tileSize*20;
        }
        else if(gp.currMap.equals("/maps/river.txt")) {
            // Set NPCs for river map
            // Example: gp.npc[0] = new NPC_fisherman(gp);
            // gp.npc[0].wX = gp.tileSize*23;
            // gp.npc[0].wY = gp.tileSize*8;
        }
        // Add more map conditions as needed
    }
}