// Contoh sederhana CreditsScreen.java
package GUI.panels; // Sesuaikan dengan package Anda

import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class CreditsScreen {
    GamePanel gp;
    Font font;
    BufferedImage backgroundImage;

    public CreditsScreen(GamePanel gp, Font font) {
        this.gp = gp;
        this.font = font;

    }

    public void draw(Graphics2D g2) {

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(new Color(30, 30, 60));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        g2.setFont(font.deriveFont(Font.BOLD, 70F));
        String text = "CREDITS";
        int x = getXforCenteredText(text, g2);
        int y = gp.tileSize * 2;
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

 
        g2.setFont(font.deriveFont(Font.PLAIN, 35F));
        y += gp.tileSize * 2; 

        String[] studentCredits = {
            "Sebastian Albern Nugroho - 18223074",
            "Irdina Ilmuna Yosapat - 18223060",
            "M Ikhbar A - 18223050"
        };

        for (String line : studentCredits) {
            x = getXforCenteredText(line, g2);
            g2.drawString(line, x, y);
            y += gp.tileSize; 
        }

        String returnText = "Press ESC to return";
        g2.setFont(font.deriveFont(Font.PLAIN, 25F));
        x = getXforCenteredText(returnText, g2);
        y = gp.screenHeight - gp.tileSize; 
        g2.setColor(new Color(200, 200, 200));
        g2.drawString(returnText, x, y);
    }

    private int getXforCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}