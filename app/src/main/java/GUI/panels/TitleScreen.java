package GUI.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.GamePanel;

public class TitleScreen extends BaseUIPanel {
    public int commandNum = 0; // 0 for "START GAME", 1 for "HELP", 2 for "CREDITS", 3 for "QUIT"
    private Font titleTextFont;
    private Font menuTextFont;
    
    public TitleScreen(GamePanel gp, Font uiFont){
        super(gp,uiFont);
        this.titleTextFont = uiFont.deriveFont(Font.BOLD, 96F);
        this.menuTextFont = uiFont.deriveFont(Font.BOLD, 48F);
    }
    
    public void draw(Graphics2D g2){
        g2.setColor(new Color(0,0,0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Title
        g2.setFont(titleTextFont);
        String text = "Spakbor Hills";
        int x = getXforCenteredText(g2, text, gp.screenWidth);
        int y = gp.tileSize * 3;
        
        // Title shadow
        g2.setColor(Color.gray);
        g2.drawString(text, x+5, y+5);
        
        // Title text
        g2.setColor(TEXT_PRIMARY);
        g2.drawString(text, x, y);
        
        // Menu options
        g2.setFont(menuTextFont);
        
        // START GAME
        text = "START GAME";
        x = getXforCenteredText(g2, text, gp.screenWidth);
        y += gp.tileSize * 3;
        g2.drawString(text, x, y);
        if (commandNum == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }
        
        // HELP
        text = "HELP";
        x = getXforCenteredText(g2, text, gp.screenWidth);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }
        
        // CREDITS
        text = "CREDITS";
        x = getXforCenteredText(g2, text, gp.screenWidth);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2){
            g2.drawString(">", x - gp.tileSize, y);
        }
        
        // QUIT
        text = "QUIT";
        x = getXforCenteredText(g2, text, gp.screenWidth);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 3){
            g2.drawString(">", x - gp.tileSize, y);
        }
    }
    
    // Helper method 
    private int getXforCenteredText(Graphics2D g2, String text, int frameWidth) {
        FontMetrics fm = g2.getFontMetrics();
        int length = fm.stringWidth(text);
        int x = (frameWidth - length) / 2;
        return x;
    }
    

    public void moveSelectionUp() {
        commandNum--;
        if (commandNum < 0) {
            commandNum = 3; 
        }
    }
    
    public void moveSelectionDown() {
        commandNum++;
        if (commandNum > 3) { 
            commandNum = 0; 
        }
    }
}