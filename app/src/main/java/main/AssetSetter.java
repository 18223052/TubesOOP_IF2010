package main;
import object.Pond;
import object.ShippingBin;

public class AssetSetter {
    
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObj(){

        gp.obj[0] = new ShippingBin();
        gp.obj[0].wX = 16 * gp.tileSize;
        gp.obj[0].wY = 20 * gp.tileSize;

        gp.obj[1] = new Pond();
        gp.obj[1].wX = 21 * gp.tileSize;
        gp.obj[1].wY = 30 * gp.tileSize;


    }
}
