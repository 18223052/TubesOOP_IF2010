package controller;

import main.GamePanel;
import object.IConsumable;

public class EatingController{

    private GamePanel gp;

    public EatingController(GamePanel gp){
        this.gp = gp;
    }

    public void consume(IConsumable item){

        System.out.println("Mulai proses konsumsi..");

        item.consume(gp);
        

        System.out.println("Consumed item and restored " + item.getEnergyRestoration() + " energy.");
    }
}