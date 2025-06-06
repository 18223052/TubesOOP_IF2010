package GUI.panels;

import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.FontMetrics;

public class HUD extends BaseUIPanel {

    public HUD(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2) {
        g2.setFont(uiFont);
        g2.setColor(Color.white);

        String time = String.format("Day %d - %02d:%02d", gp.gameTime.getGameDay(), gp.gameTime.getGameHour(), gp.gameTime.getGameMinute());
        String seasonText = "Season: " + gp.gameTime.getSeasonName();

        FontMetrics fm = g2.getFontMetrics(uiFont);

        int margin = gp.tileSize / 2;

        int verticalOffset = 20; 


        int timeTextWidth = fm.stringWidth(time);
        int timeX = gp.screenWidth - timeTextWidth - margin;
        int timeY = (gp.tileSize / 2) + verticalOffset; 


        int seasonTextWidth = fm.stringWidth(seasonText);
        int seasonX = gp.screenWidth - seasonTextWidth - margin;
        int seasonY = timeY + 30;

        g2.drawString(time, timeX, timeY);
        g2.drawString(seasonText, seasonX, seasonY);
    }
}