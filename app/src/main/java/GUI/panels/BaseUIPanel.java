package GUI.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.GamePanel;

public class BaseUIPanel {
    protected GamePanel gp;
    protected Font uiFont;

    public BaseUIPanel(GamePanel gp, Font uiFont){
        this.gp = gp;
        this.uiFont = uiFont;
    }

    protected void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210); 
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35); 

        c = new Color(255, 255, 255); // White border
        g2.setColor(c);
        g2.setStroke(new java.awt.BasicStroke(5)); // Thicker border
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    protected int getXforAllignToRight(String text, int tailX, Graphics2D g2) {
        int length = (int) g2.getFontMetrics(uiFont).getStringBounds(text, g2).getWidth();
        return tailX - length;
    }

    protected void drawCenteredString(Graphics2D g2, String text, int frameX, int textY, int frameWidth) {
        if (g2 == null || g2.getFontMetrics() == null) {
            return;
        }
        g2.setFont(uiFont); 
        int textWidth = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        g2.drawString(text, frameX + (frameWidth - textWidth) / 2, textY);
    }

    protected Color getCategoryColor(String category) {
        switch (category) {
            case "tools":       return new Color(150, 150, 255); // Blue-ish
            case "consumables": return new Color(255, 150, 150); // Red-ish
            case "crops":       return new Color(150, 255, 150); // Green-ish
            case "fish":        return new Color(150, 200, 255); // Light Blue-ish
            case "seeds":       return new Color(255, 255, 150); // Yellow-ish
            case "fuel":        return new Color(200, 150, 100); // Brown-ish
            default:            return Color.gray;
        }
    }
}
