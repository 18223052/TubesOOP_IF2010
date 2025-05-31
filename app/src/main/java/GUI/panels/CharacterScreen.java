// src/ui/panels/CharacterScreen.java
package GUI.panels;

import main.GamePanel;
// Asumsi Player class memiliki metode getTeleportCount(), getTotalExpenditure(),
// getharvestCount(), dan getFishCaughtCount().
// Juga asumsi GameTime memiliki getGameDay() dan NPC memiliki getTotalTimesChatted().
// import entity.Player; // jika diperlukan untuk tipe eksplisit

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class CharacterScreen extends BaseUIPanel {

    public CharacterScreen(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2) {
        final int frameX = gp.tileSize - 10;
        final int frameY = gp.tileSize - 20;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 11; 
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight); 

        g2.setColor(Color.white);
        g2.setFont(uiFont.deriveFont(28F)); 

        int textXLabels = frameX + 20; 
        int currentTextY = frameY + gp.tileSize;
        final int lineHeight = 32; 


        g2.drawString("Name", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Gender", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Farm Map", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Energy", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Gold", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Partner", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Favorite", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Expenditure", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Total days", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Chatting", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Gifting", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Visiting", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Crops", textXLabels, currentTextY);
        currentTextY += lineHeight;
        g2.drawString("Fish", textXLabels, currentTextY);

   
        int tailX = (frameX + frameWidth) - 30; 
        currentTextY = frameY + gp.tileSize; 
        String value;
        int valueTextX; 

        // Name
        value = String.valueOf(gp.player.getName());
        valueTextX = getXforAllignToRight(value, tailX, g2); 
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Gender
        value = String.valueOf(gp.player.getGender());
        valueTextX = getXforAllignToRight(value, tailX, g2); 
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Farm Map
        value = String.valueOf(gp.player.getFarmMap());
        valueTextX = getXforAllignToRight(value, tailX, g2); 
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Energy
        value = String.valueOf(gp.player.getEnergy());
        valueTextX = getXforAllignToRight(value, tailX, g2); 
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Gold
        value = String.valueOf(gp.player.getGold());
        valueTextX = getXforAllignToRight(value, tailX, g2); 
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Partner (Name)
        value = String.valueOf(gp.player.getPartner()); 
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;
        
        // Favorite Item
        value = "salmon"; 
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Expenditure
        value = String.valueOf(gp.player.getTotalExpenditure()); 
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Total days
        value = String.valueOf(gp.gameTime.getGameDay()); 
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Chatting freq
        if (gp.currNPC != null) { 
            value = String.valueOf(gp.currNPC.getTotalTimesChatted());
        } else {
            value = "0"; 
        }
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // gifting count
        value = String.valueOf(gp.player.getGiftingCount()); 
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;

        // Visiting freq (Teleport count)
        value = String.valueOf(gp.player.getVisitingCount()); // Menggunakan metode yang sudah kita buat
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;
        
        // // Crops harvested
        value = String.valueOf(gp.player.getharvestCount()); 
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
        currentTextY += lineHeight;


        // Fish caught
        value = String.valueOf(gp.player.getFishCaughtCount()); // Asumsi metode ini ada
        valueTextX = getXforAllignToRight(value, tailX, g2);
        g2.drawString(value, valueTextX, currentTextY);
    }
}