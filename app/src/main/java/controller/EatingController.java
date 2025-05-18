package controller;

import main.GamePanel;
import object.IConsumable;

public class EatingController{

    private GamePanel gp;

    public EatingController(GamePanel gp){
        this.gp = gp;
    }

    public void consume(IConsumable item){
        
        int energyRestoration = item.getEnergyRestoration();

        gp.player.addEnergy(energyRestoration);

        System.out.println("Consumed item and restored " + energyRestoration + " energy.");
    }
}