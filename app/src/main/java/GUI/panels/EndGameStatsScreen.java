package GUI.panels; // Sesuaikan dengan package Anda

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;
import java.awt.FontMetrics;

import entity.NPC;
import main.GamePanel;
import object.FishItem; //
import entity.Player; //


public class EndGameStatsScreen {
    GamePanel gp;
    Font titleFont, statsFont, smallStatsFont, instructionFont;

    public EndGameStatsScreen(GamePanel gp, Font baseFont) {
        this.gp = gp;
        this.titleFont = baseFont.deriveFont(Font.BOLD, 32F);
        this.statsFont = baseFont.deriveFont(Font.PLAIN, 22F);
        this.smallStatsFont = baseFont.deriveFont(Font.PLAIN, 18F);
        this.instructionFont = baseFont.deriveFont(Font.BOLD, 24F);
    }

    // Metode untuk menggambar sub-window (ambil dari UI.java lama atau implementasi baru)
    private void drawCustomSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new java.awt.BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
    
    // Helper untuk text di tengah (ambil dari UI.java lama atau implementasi baru)
    public int getXforCenteredText(Graphics2D g2, String text) {
        if (g2 == null || text == null) return 0;
        FontMetrics fm = g2.getFontMetrics();
        if (fm == null) return 0;
        int length = fm.stringWidth(text);
        int x = gp.screenWidth / 2 - length / 2; //
        return x;
    }


    public void draw(Graphics2D g2) {
        int frameX = gp.tileSize; 
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth - (gp.tileSize * 2); //
        int frameHeight = gp.screenHeight - (gp.tileSize * 2); //
        
        drawCustomSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.WHITE);
        int x = frameX + gp.tileSize;
        int y = frameY + gp.tileSize;
        int lineHeight = 26;
        int smallLineHeight = 20;

        g2.setFont(titleFont);
        String title = "--- Milestone Reached! Game Statistics ---";
        g2.drawString(title, getXforCenteredText(g2, title), y);
        y += lineHeight * 1.5;

        g2.setFont(statsFont);

        // Total Income (menggunakan goldEarnedFromSelling dari Player.java)
        g2.drawString("Total Income: " + gp.player.getGoldEarnedFromSelling() + "g", x, y); //
        y += lineHeight;

        // Total Expenditure
        g2.drawString("Total Expenditure: " + gp.player.getTotalExpenditure() + "g", x, y); //
        y += lineHeight;

        // Average Season Income & Expenditure
        int seasonsToAverage = gp.gameTime.getGameDay()/10;
        if (seasonsToAverage == 0 && gp.gameTime.getGameDay() > 0) { //
            seasonsToAverage = 1; 
        }
        if (seasonsToAverage == 0) seasonsToAverage = 1;

        long avgIncome = (gp.player.getGoldEarnedFromSelling()) / seasonsToAverage; //
        g2.drawString("Average Season Income: " + avgIncome + "g", x, y);
        y += lineHeight;

        long avgExpenditure = gp.player.getTotalExpenditure() / seasonsToAverage; //
        g2.drawString("Average Season Expenditure: " + avgExpenditure + "g", x, y);
        y += lineHeight;

        // Total Days Played
        g2.drawString("Total Days Played: " + gp.gameTime.getGameDay(), x, y); //
        y += lineHeight * 1.5;

        // NPCs Status
        g2.drawString("NPCs Status:", x, y);
        y += lineHeight;
        g2.setFont(smallStatsFont);
        int npcDrawnCount = 0;
        if (gp.npc != null) { //
            for (NPC npc : gp.npc) { //
                if (npc != null && npcDrawnCount < 4) { // Batasi jumlah NPC
                    g2.drawString("  " + npc.getName() + ":", x, y); //
                    y += smallLineHeight;
                    g2.drawString("    Relation: " + npc.getRelationshipStatus(), x + gp.tileSize / 2, y); //
                    y += smallLineHeight;
                    npcDrawnCount++;
                }
            }
        }
        if (npcDrawnCount == 0) {
            g2.drawString("  No NPC data available.", x,y);
            y += smallLineHeight;
        }
        g2.setFont(statsFont);
        y += lineHeight * 0.5;

        g2.drawString("    Chats: " + gp.player.getChattingCount(), x + gp.tileSize / 2, y);
        y += smallLineHeight;
        g2.drawString("    Gifts Given: " + gp.player.getGiftingCount(), x + gp.tileSize / 2, y); 
        y += smallLineHeight; 
        g2.drawString("    Visits: " + gp.player.getVisitingCount(), x + gp.tileSize / 2, y);
        y += smallLineHeight; 

        // Crops Harvested
        g2.drawString("Crops Harvested: " + gp.player.getharvestCount(), x, y); //
        y += lineHeight;

        // Fish Caught
        g2.drawString("Total Fish Caught: " + gp.player.getFishCaughtCount(), x, y); //
        y += lineHeight;
        g2.setFont(smallStatsFont);
        Map<FishItem.FishCategory, Integer> fishCounts = gp.player.getAllFishCountsPerCategory(); //
        if (fishCounts != null) {
            g2.drawString("  Common: " + fishCounts.getOrDefault(FishItem.FishCategory.COMMON, 0), x + gp.tileSize / 2, y); //
            y += smallLineHeight;
            g2.drawString("  Regular: " + fishCounts.getOrDefault(FishItem.FishCategory.REGULAR, 0), x + gp.tileSize / 2, y); //
            y += smallLineHeight;
            g2.drawString("  Legendary: " + fishCounts.getOrDefault(FishItem.FishCategory.LEGENDARY, 0), x + gp.tileSize / 2, y); //
        }
        y += lineHeight * 1.5;

        g2.setFont(instructionFont);
        String continueText = "Press [E] to Continue Playing...";
        g2.drawString(continueText, getXforCenteredText(g2, continueText), frameY + frameHeight - gp.tileSize / 2);
    }
}