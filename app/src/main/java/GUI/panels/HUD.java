package GUI.panels;

import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class HUD extends BaseUIPanel {

    public HUD(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2) {
        g2.setFont(uiFont);
        g2.setColor(Color.white);
        String time = String.format("Day %d - %02d:%02d", gp.gameTime.getGameDay(), gp.gameTime.getGameHour(), gp.gameTime.getGameMinute());
        String seasonText = "Season: " + gp.gameTime.getSeasonName();
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        g2.drawString(time, x, y);
        g2.drawString(seasonText, x, y + 30);
    }
}