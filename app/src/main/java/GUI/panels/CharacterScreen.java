// src/ui/panels/CharacterScreen.java
package GUI.panels;

import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class CharacterScreen extends BaseUIPanel {

    public CharacterScreen(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2) {
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight); // Use utility

        g2.setColor(Color.white);
        g2.setFont(uiFont.deriveFont(32F)); // Use common font

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 32;

        g2.drawString("Name", textX, textY);
        textY += lineHeight;
        g2.drawString("Gender", textX, textY);
        textY += lineHeight;
        g2.drawString("Farm Map", textX, textY);
        textY += lineHeight;
        g2.drawString("Energy", textX, textY);
        textY += lineHeight;
        g2.drawString("Gold", textX, textY);
        textY += lineHeight;
        g2.drawString("Partner", textX, textY);
        textY += lineHeight;
        // values
        int tailX = (frameX + frameWidth) - 30;
        // reset
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.getName());
        textX = getXforAllignToRight(value, tailX, g2); // Use utility
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getGender());
        textX = getXforAllignToRight(value, tailX, g2); // Use utility
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getFarmMap());
        textX = getXforAllignToRight(value, tailX, g2); // Use utility
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getEnergy());
        textX = getXforAllignToRight(value, tailX, g2); // Use utility
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getGold());
        textX = getXforAllignToRight(value, tailX, g2); // Use utility
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // value = String.valueOf(gp.player.getPartner());
        // textX = getXforAllignToRight(value, tailX, g2);
        // g2.drawString(value, textX, textY);
        // textY += lineHeight;
    }
}