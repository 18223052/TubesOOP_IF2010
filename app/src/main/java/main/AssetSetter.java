package main;
import entity.*;

import object.Bed;
import object.LakeFishingSpot;
import object.LandTile;
import object.OceanFishingSpot;
import object.PondFishingSpot;
import object.RiverFishingSpot;
import object.ShippingBin;
import object.Store;
import object.Stove;
import object.TV;

public class AssetSetter {
    
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    
    public void clearObjects(){
        gp.obj.clear();
    }
    
    public void clearNPCs() {
        System.out.println("DEBUG (AssetSetter): clearNPCs() dipanggil. Mengosongkan " + gp.npc.length + " slot NPC.");
        for(int i = 0; i < gp.npc.length; i++) {
            gp.npc[i] = null;
        }
        System.out.println("DEBUG (AssetSetter): clearNPCs() selesai.");
    }

    public void setObj(){

        if(gp.currMap.equals("/maps/farmmm.txt")) {
            

            System.out.println("DEBUG (AssetSetter): setObj() dipanggil untuk peta: " + gp.currMap);
            // inisialisasi shippingbin
            ShippingBin shippingBin1 = new ShippingBin(gp);
            shippingBin1.wX = 20 * gp.tileSize;
            shippingBin1.wY = 20 * gp.tileSize;
            gp.obj.add(shippingBin1); 

            // inisialisasi landtile
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

            // inisialisasi pond
            PondFishingSpot pond1 = new PondFishingSpot(gp, 20*gp.tileSize, 29*gp.tileSize);
            gp.obj.add(pond1);

        }
        if(gp.currMap.equals("/maps/farmmm2.txt")) {
            // inisialisasi ShippingBin
            ShippingBin shippingBin2 = new ShippingBin(gp);
            shippingBin2.wX = 20 * gp.tileSize;
            shippingBin2.wY = 30 * gp.tileSize;
            gp.obj.add(shippingBin2); 

            // inisialisasi landtile
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

            // inisialisasi pond
            PondFishingSpot pond2 = new PondFishingSpot(gp, 18*gp.tileSize, 21*gp.tileSize);
            gp.obj.add(pond2);
        }

        if(gp.currMap.equals("/maps/farmmm3.txt")) {

            // inisialisasi ShippingBin
            ShippingBin shippingBin3 = new ShippingBin(gp);
            shippingBin3.wX = 33 * gp.tileSize;
            shippingBin3.wY = 30 * gp.tileSize;
            gp.obj.add(shippingBin3); 

            // inisialisasi landtile
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

            // inisialisasi pond
            PondFishingSpot pond3 = new PondFishingSpot(gp, 36*gp.tileSize, 29*gp.tileSize);
            gp.obj.add(pond3);
        }

        else if(gp.currMap.equals("/maps/worldmap.txt")) {

            // Store
            Store store = new Store(gp);
            store.wX = 33 * gp.tileSize;
            store.wY = 30 * gp.tileSize;
            gp.obj.add(store); 

            // Ocean (DIPOJOK ATAS KANAN)
            OceanFishingSpot ocean1 = new OceanFishingSpot(gp, 35*gp.tileSize, 9*gp.tileSize);
            gp.obj.add(ocean1);

            OceanFishingSpot ocean2 = new OceanFishingSpot(gp, 36*gp.tileSize, 8*gp.tileSize);
            gp.obj.add(ocean2);

            OceanFishingSpot ocean3 = new OceanFishingSpot(gp, 37*gp.tileSize, 9*gp.tileSize);
            gp.obj.add(ocean3);
        }

        else if(gp.currMap.equals("/maps/rumah.txt")) {
            // Bed
            Bed bed1 = new Bed(gp);
            bed1.wX = 18 * gp.tileSize;
            bed1.wY = 15 * gp.tileSize;
            gp.obj.add(bed1); 

            // Stove
            Stove stove = new Stove(gp);
            stove.wX = 34 * gp.tileSize;
            stove.wY = 14 * gp.tileSize;
            gp.obj.add(stove); 

            Bed bed2 = new Bed(gp);
            bed2.wX = 16 * gp.tileSize;
            bed2.wY = 15 * gp.tileSize;
            gp.obj.add(bed2); 

            // TV
            TV tv = new TV(gp);
            tv.wX = 19 * gp.tileSize;
            tv.wY = 20 * gp.tileSize;
            gp.obj.add(tv); 
        }

        else if (gp.currMap.equals("/maps/river.txt")){
            RiverFishingSpot river1 = new RiverFishingSpot(gp, 20*gp.tileSize, 7*gp.tileSize);
            gp.obj.add(river1);
            RiverFishingSpot river2 = new RiverFishingSpot(gp, 21*gp.tileSize, 7*gp.tileSize);
            gp.obj.add(river2);
            RiverFishingSpot river3 = new RiverFishingSpot(gp, 22*gp.tileSize, 7*gp.tileSize);
            gp.obj.add(river3);
            RiverFishingSpot river4 = new RiverFishingSpot(gp, 23*gp.tileSize, 7*gp.tileSize);
            gp.obj.add(river4);
            
        }

        else if (gp.currMap.equals("/maps/lake.txt")){
            LakeFishingSpot lake1 =  new LakeFishingSpot(gp, 26*gp.tileSize, 8*gp.tileSize);
            gp.obj.add(lake1);
            LakeFishingSpot lake2 = new LakeFishingSpot(gp, 25*gp.tileSize, 8*gp.tileSize);
            gp.obj.add(lake2);
        }

        System.out.println("DEBUG (AssetSetter): setObj() selesai. Total objek: " + gp.obj.size());
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

            gp.npc[0] = new NPC_abigail(gp, gp.itemFactory);
            gp.npc[0].wX = gp.tileSize*19;
            gp.npc[0].wY = gp.tileSize*29;

            // gp.npc[2] = new NPC_perry(gp);
            // gp.npc[2].wX = gp.tileSize*20;
            // gp.npc[2].wY = gp.tileSize*29;

            // gp.npc[3] = new NPC_dasco(gp);
            // gp.npc[3].wX = gp.tileSize*21;
            // gp.npc[3].wY = gp.tileSize*29;
        }
    }
}