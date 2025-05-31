package GUI.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Map;
import java.awt.FontMetrics;

import entity.NPC;
import main.GamePanel;
import object.FishItem;
import entity.Player;


public class EndGameStatsScreen {
    GamePanel gp;
    Font titleFont, statsFont, smallStatsFont, instructionFont;

    public EndGameStatsScreen(GamePanel gp, Font baseFont) {
        this.gp = gp;
        this.titleFont = baseFont.deriveFont(Font.BOLD, 38F);
        this.statsFont = baseFont.deriveFont(Font.PLAIN, 24F);
        this.smallStatsFont = baseFont.deriveFont(Font.PLAIN, 20F);
        this.instructionFont = baseFont.deriveFont(Font.BOLD, 26F);
    }

    private void drawCustomSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(Graphics2D g2, String text) {
        if (g2 == null || text == null) return 0;
        FontMetrics fm = g2.getFontMetrics();
        if (fm == null) return 0;
        int length = fm.stringWidth(text);
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }


    public void draw(Graphics2D g2) {
        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth - (gp.tileSize * 2);
        int frameHeight = gp.screenHeight - (gp.tileSize * 2);

        drawCustomSubWindow(g2, frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.WHITE);

        // Define column properties
        int paddingX = gp.tileSize * 2; 
        int col1X = frameX + paddingX;
        int col2X = frameX + frameWidth / 2 + gp.tileSize;

        int currentY = frameY + gp.tileSize * 2; 
        int lineHeight = 30; 
        int smallLineHeight = 25; 

        g2.setFont(titleFont);
        String title = "--- Game Statistics ---";
        g2.drawString(title, getXforCenteredText(g2, title), currentY);
        currentY += lineHeight * 2; 


        int col1Y = currentY; 

        // FINANCIAL STATS
        g2.setFont(statsFont);
        g2.drawString("FINANCIAL OVERVIEW", col1X, col1Y);
        col1Y += smallLineHeight;

        g2.setFont(smallStatsFont);
        g2.drawString("  Total Income: " + gp.player.getGoldEarnedFromSelling() + "g", col1X, col1Y);
        col1Y += smallLineHeight;
        g2.drawString("  Total Expenditure: " + gp.player.getTotalExpenditure() + "g", col1X, col1Y);
        col1Y += smallLineHeight;

        int seasonsToAverage = gp.gameTime.getGameDay() / 10;
        if (seasonsToAverage == 0 && gp.gameTime.getGameDay() > 0) {
            seasonsToAverage = 1;
        }
        if (seasonsToAverage == 0) seasonsToAverage = 1;

        long avgIncome = (gp.player.getGoldEarnedFromSelling()) / seasonsToAverage;
        g2.drawString("  Average Season Income: " + avgIncome + "g", col1X, col1Y);
        col1Y += smallLineHeight;

        long avgExpenditure = gp.player.getTotalExpenditure() / seasonsToAverage;
        g2.drawString("  Average Season Expenditure: " + avgExpenditure + "g", col1X, col1Y);
        col1Y += lineHeight * 1.5;

        // GENERAL STATS
        g2.setFont(statsFont);
        g2.drawString("GENERAL PROGRESS", col1X, col1Y);
        col1Y += smallLineHeight;

        g2.setFont(smallStatsFont);
        g2.drawString("  Total Days Played: " + gp.gameTime.getGameDay() + " days", col1X, col1Y);
        col1Y += smallLineHeight;
        g2.drawString("  Crops Harvested: " + gp.player.getharvestCount(), col1X, col1Y);
        col1Y += smallLineHeight;
        g2.drawString("  Total Fish Caught: " + gp.player.getFishCaughtCount(), col1X, col1Y);
        col1Y += smallLineHeight;

        Map<FishItem.FishCategory, Integer> fishCounts = gp.player.getAllFishCountsPerCategory();
        if (fishCounts != null) {
            g2.drawString("    Common: " + fishCounts.getOrDefault(FishItem.FishCategory.COMMON, 0), col1X + gp.tileSize / 2, col1Y);
            col1Y += smallLineHeight;
            g2.drawString("    Regular: " + fishCounts.getOrDefault(FishItem.FishCategory.REGULAR, 0), col1X + gp.tileSize / 2, col1Y);
            col1Y += smallLineHeight;
            g2.drawString("    Legendary: " + fishCounts.getOrDefault(FishItem.FishCategory.LEGENDARY, 0), col1X + gp.tileSize / 2, col1Y);
            col1Y += smallLineHeight;
        }


        int col2Y = currentY; 

        g2.setFont(statsFont);
        g2.drawString("NPC INTERACTIONS", col2X, col2Y);
        col2Y += smallLineHeight;

        g2.setFont(smallStatsFont);
        g2.drawString("  Chats: " + gp.player.getChattingCount(), col2X, col2Y);
        col2Y += smallLineHeight;
        g2.drawString("  Gifts Given: " + gp.player.getGiftingCount(), col2X, col2Y);
        col2Y += smallLineHeight;
        g2.drawString("  Visits: " + gp.player.getVisitingCount(), col2X, col2Y);
        col2Y += lineHeight * 1.5;

        // NPC Status
        g2.setFont(statsFont);
        g2.drawString("NPC RELATIONSHIP STATUS:", col2X, col2Y);
        col2Y += smallLineHeight;
        g2.setFont(smallStatsFont);

        Collection<NPC> allGameNPCs = gp.allGameNPCs.values();
        int npcCountForLayout = 0;

        for (NPC npc : allGameNPCs) {
            if (npc != null) {
                String npcLine = npc.getName() + ": " + npc.getRelationshipStatus();
                g2.drawString("  " + npcLine, col2X, col2Y);
                col2Y += smallLineHeight;
                npcCountForLayout++;
            }
        }

        if (npcCountForLayout == 0) {
            g2.drawString("  No NPC data available.", col2X, col2Y);
            col2Y += smallLineHeight;
        }

        g2.setFont(instructionFont);
        String continueText = "Press [E] to Continue Playing...";

        int maxYContent = Math.max(col1Y, col2Y);
        int finalInstructionY = frameY + frameHeight - gp.tileSize; 

        if (finalInstructionY < maxYContent + lineHeight) {
            finalInstructionY = maxYContent + lineHeight;
        }

        g2.drawString(continueText, getXforCenteredText(g2, continueText), finalInstructionY);
    }
}