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
        // System.out.println("DEBUG (AssetSetter): clearNPCs() dipanggil. Mengosongkan " + gp.npc.length + " slot NPC.");
        for(int i = 0; i < gp.npc.length; i++) {
            gp.npc[i] = null;
        }
        // System.out.println("DEBUG (AssetSetter): clearNPCs() selesai.");
    }

    public void setObj(){

        if(gp.currMap.equals("/maps/farmmm.txt")) {
            

            // System.out.println("DEBUG (AssetSetter): setObj() dipanggil untuk peta: " + gp.currMap);
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
                // System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }

            for (int x = 27; x <= 34; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 36 * gp.tileSize;
                // System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
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
                // System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }

            for (int x = 28; x <= 35; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 29 * gp.tileSize;
                // System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
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
                // System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }

            for (int x = 14; x <= 21; x++) { 
                LandTile landTile = new LandTile(gp);
                landTile.wX = x * gp.tileSize;
                landTile.wY = 29 * gp.tileSize;
                // System.out.println("ASSET_DEBUG: Membuat LandTile di Map: " + gp.currMap + " | wX: " + landTile.wX + ", wY: " + landTile.wY);
                gp.obj.add(landTile);
            }

            // inisialisasi pond
            PondFishingSpot pond3 = new PondFishingSpot(gp, 36*gp.tileSize, 29*gp.tileSize);
            gp.obj.add(pond3);
        }

        else if(gp.currMap.equals("/maps/worldmap.txt")) {

            // Store
            Store store = new Store(gp);
            store.wX = 31 * gp.tileSize;
            store.wY = 28 * gp.tileSize;
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

        // System.out.println("DEBUG (AssetSetter): setObj() selesai. Total objek: " + gp.obj.size());
    }

        public void setNPC(){
        clearNPCs(); // Kosongkan array NPC aktif saat ini

        // Helper untuk menempatkan NPC dari allGameNPCs ke peta
        // Menggunakan slot gp.npc[0] untuk kesederhanaan, sesuaikan jika perlu lebih banyak NPC per peta
        int npcSlotIndex = 0; 

        if (gp.currMap.equals("/maps/farmmm.txt") || 
            gp.currMap.equals("/maps/farmmm2.txt") || 
            gp.currMap.equals("/maps/farmmm3.txt") || 
            gp.currMap.equals("/maps/rumahmayortadi.txt")) {
            // Asumsi Mayortadi muncul di farm dan rumahnya
            // Ganti "Mayortadi" dengan hasil dari NPC_mayortadi.getName() jika berbeda
            NPC mayor = gp.allGameNPCs.get("Mayortadi"); 
            if (mayor != null) {
                if (gp.currMap.equals("/maps/farmmm.txt") || gp.currMap.equals("/maps/farmmm2.txt") || gp.currMap.equals("/maps/farmmm3.txt")) {
                    mayor.wX = gp.tileSize*16; // Posisi di farm (sesuaikan per variasi farm jika perlu)
                    mayor.wY = gp.tileSize*20;
                } else { // rumahmayortadi.txt
                    mayor.wX = gp.tileSize*28;
                    mayor.wY = gp.tileSize*28;
                }
                gp.npc[npcSlotIndex++] = mayor;
            } else {
                System.err.println("AssetSetter: NPC 'Mayortadi' tidak ditemukan di allGameNPCs untuk map " + gp.currMap);
            }
        }
        else if(gp.currMap.equals("/maps/worldmap.txt")) {
            // Asumsi Emily muncul di worldmap (dekat tokonya mungkin)
            // Ganti "Emily" dengan hasil dari NPC_Emily.getName() jika berbeda
            NPC emily = gp.allGameNPCs.get("Emily"); 
            if (emily != null) {
                emily.wX = gp.tileSize*30;
                emily.wY = gp.tileSize*28;
                gp.npc[npcSlotIndex++] = emily;
            } else {
                System.err.println("AssetSetter: NPC 'Emily' tidak ditemukan di allGameNPCs untuk map " + gp.currMap);
            }
        }
        else if (gp.currMap.equals("/maps/rumahabigail.txt")){
            // Ganti "Abigail" dengan hasil dari NPC_abigail.getName() jika berbeda
            NPC abigail = gp.allGameNPCs.get("Abigail"); 
            if (abigail != null) {
                abigail.wX = gp.tileSize*28;
                abigail.wY = gp.tileSize*28;
                gp.npc[npcSlotIndex++] = abigail;
            } else {
                System.err.println("AssetSetter: NPC 'Abigail' tidak ditemukan di allGameNPCs untuk map " + gp.currMap);
            }
        }
        else if (gp.currMap.equals("/maps/rumahperry.txt")){
            // Ganti "Perry" dengan hasil dari NPC_Perry.getName() jika berbeda
            NPC perry = gp.allGameNPCs.get("Perry"); 
            if (perry != null) {
                perry.wX = gp.tileSize*28;
                perry.wY = gp.tileSize*28;
                gp.npc[npcSlotIndex++] = perry;
            } else {
                System.err.println("AssetSetter: NPC 'Perry' tidak ditemukan di allGameNPCs untuk map " + gp.currMap);
            }
        }
        else if (gp.currMap.equals("/maps/rumahcaroline.txt")){
            // Ganti "Caroline" dengan hasil dari NPC_Caroline.getName() jika berbeda
            NPC caroline = gp.allGameNPCs.get("Caroline"); 
            if (caroline != null) {
                caroline.wX = gp.tileSize*28;
                caroline.wY = gp.tileSize*28;
                gp.npc[npcSlotIndex++] = caroline;
            } else {
                System.err.println("AssetSetter: NPC 'Caroline' tidak ditemukan di allGameNPCs untuk map " + gp.currMap);
            }
        }
        else if (gp.currMap.equals("/maps/rumahdasco.txt")){
            // Ganti "Dasco" dengan hasil dari NPC_Dasco.getName() jika berbeda
            NPC dasco = gp.allGameNPCs.get("Dasco"); 
            if (dasco != null) {
                dasco.wX = gp.tileSize*28;
                dasco.wY = gp.tileSize*28;
                gp.npc[npcSlotIndex++] = dasco;
            } else {
                System.err.println("AssetSetter: NPC 'Dasco' tidak ditemukan di allGameNPCs untuk map " + gp.currMap);
            }
        }
    }
}