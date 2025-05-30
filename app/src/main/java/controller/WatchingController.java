package controller;

import environment.WeatherType;
import main.GamePanel;

public class WatchingController {
    GamePanel gp;
    
    public WatchingController(GamePanel gp){
        this.gp = gp;
    }

    public void watchTV(){
        int currentDayInSeason = gp.gameTime.getDayInCurrentSeason();
        WeatherType currWeather = gp.weatherManager.getWeatherForDay(currentDayInSeason);
        String weatherForecast = "Today's forecast : " + currWeather.name() + ".";

        gp.ui.setDialog(weatherForecast);

        gp.gameState = GamePanel.dialogState;
        gp.repaint();
        gp.currNPC = null;
        gp.keyH.enterPressed = false;

        gp.gameTime.addTime(15);
        gp.player.deductEnergy(5);
        
    }
}
