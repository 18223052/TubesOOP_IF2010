package main;
import entity.NPC_abigail;
import entity.*;

import object.Bed;
import object.LandTile;
import object.ShippingBin;
import object.Store;
import object.Stove;
import object.TV;
import object.SuperObj; // Pastikan ini di-import

public class AssetSetter {
    
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    
    public void clearObjects() {
        // Jika gp.obj adalah ArrayList, gunakan metode clear()
        gp.obj.clear();
    }
    
    public void clearNPCs() {
        // Untuk NPC, karena masih array, biarkan seperti ini
        for(int i = 0; i < gp.npc.length; i++) {
            gp.npc[i] = null;
        }
    }

    public void setObj(){

        if(gp.currMap.equals("/maps/farmmm.txt")) {
            // ShippingBin
            SuperObj shippingBin1 = new ShippingBin(gp);
            shippingBin1.wX = 20 * gp.tileSize;
            shippingBin1.wY = 20 * gp.tileSize;
            gp.obj.add(shippingBin1); // Tambahkan ke ArrayList

            // LandTile

            for (int x = 27; x <= 34; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 35 * gp.tileSize;
                System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }

            for (int x = 27; x <= 34; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 36 * gp.tileSize;
                System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }
        }
        if(gp.currMap.equals("/maps/farmmm2.txt")) {
            // ShippingBin
            SuperObj shippingBin2 = new ShippingBin(gp);
            shippingBin2.wX = 20 * gp.tileSize;
            shippingBin2.wY = 30 * gp.tileSize;
            gp.obj.add(shippingBin2); // Tambahkan ke ArrayList

            for (int x = 28; x <= 35; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 28 * gp.tileSize;
                System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }

            for (int x = 28; x <= 35; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 29 * gp.tileSize;
                System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }
        }

        if(gp.currMap.equals("/maps/farmmm3.txt")) {
            // ShippingBin
            SuperObj shippingBin3 = new ShippingBin(gp);
            shippingBin3.wX = 33 * gp.tileSize;
            shippingBin3.wY = 30 * gp.tileSize;
            gp.obj.add(shippingBin3); 


            for (int x = 14; x <= 21; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 28 * gp.tileSize;
                System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }

            for (int x = 14; x <= 21; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 29 * gp.tileSize;
                System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }
        }

        else if(gp.currMap.equals("/maps/worldmap.txt")) {
            // Store
            SuperObj store = new Store(gp);
            store.wX = 33 * gp.tileSize;
            store.wY = 30 * gp.tileSize;
            gp.obj.add(store); // Tambahkan ke ArrayList
        }

        else if(gp.currMap.equals("/maps/rumah.txt")) {
            // Bed
            SuperObj bed1 = new Bed(gp);
            bed1.wX = 18 * gp.tileSize;
            bed1.wY = 15 * gp.tileSize;
            gp.obj.add(bed1); // Tambahkan ke ArrayList

            // Stove
            SuperObj stove = new Stove(gp);
            stove.wX = 34 * gp.tileSize;
            stove.wY = 14 * gp.tileSize;
            gp.obj.add(stove); // Tambahkan ke ArrayList

            // Bed (mungkin ini bed lain, atau penempatan yang salah sebelumnya)
            SuperObj bed2 = new Bed(gp);
            bed2.wX = 16 * gp.tileSize;
            bed2.wY = 15 * gp.tileSize;
            gp.obj.add(bed2); // Tambahkan ke ArrayList

            // TV
            SuperObj tv = new TV(gp);
            tv.wX = 19 * gp.tileSize;
            tv.wY = 20 * gp.tileSize;
            gp.obj.add(tv); // Tambahkan ke ArrayList
        }
    }

    public void setNPC(){
        // Bagian ini tidak berubah karena gp.npc masih array
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

            gp.npc[1] = new NPC_abigail(gp, gp.itemFactory);
            gp.npc[1].wX = gp.tileSize*19;
            gp.npc[1].wY = gp.tileSize*29;

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