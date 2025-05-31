package GUI.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics; // Added this import
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import main.GamePanel;

public class BaseUIPanel {
    protected GamePanel gp;
    protected Font uiFont;

    protected Font titleTextFont;
    protected Font menuTextFont;


    protected final Color BACKGROUND_COLOR = new Color(25, 25, 35, 240);
    protected final Color FRAME_BORDER_COLOR = new Color(100, 120, 150, 200);
    protected final Color SLOT_BORDER_COLOR = new Color(80, 90, 110);
    protected final Color SLOT_BACKGROUND_COLOR = new Color(45, 50, 65);
    protected final Color SELECTED_SLOT_COLOR = new Color(255, 215, 0, 180); // Goldish
    protected final Color HEADER_COLOR = new Color(220, 220, 240); // Light gray/blue for headers
    protected final Color TEXT_PRIMARY = Color.WHITE;
    protected final Color TEXT_SECONDARY = new Color(200, 200, 220); // Off-white/light gray

    protected final int CORNER_RADIUS = 12; 
    public BaseUIPanel(GamePanel gp, Font uiFont){
        this.gp = gp;
        this.uiFont = uiFont;
    }

    protected void drawSubWindow(Graphics2D g2, int x, int y, int width, int height) {

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRoundRect(x + 4, y + 4, width, height, CORNER_RADIUS, CORNER_RADIUS);

 
        GradientPaint gradient = new GradientPaint(
            x, y, BACKGROUND_COLOR,
            x, y + height, new Color(35, 35, 50, 240) 
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(x, y, width, height, CORNER_RADIUS, CORNER_RADIUS);


        g2.setColor(FRAME_BORDER_COLOR);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, width, height, CORNER_RADIUS, CORNER_RADIUS);

   
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x + 1, y + 1, width - 2, height - 2, CORNER_RADIUS - 1, CORNER_RADIUS - 1);
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

  
    protected void drawItemSlot(Graphics2D g2, int x, int y, int size, boolean isSelected) {
      
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(x + 2, y + 2, size, size, 6, 6);

        if (isSelected) {
            g2.setColor(SELECTED_SLOT_COLOR);
            g2.fillRoundRect(x - 3, y - 3, size + 6, size + 6, 8, 8);
        }

        g2.setColor(SLOT_BACKGROUND_COLOR);
        g2.fillRoundRect(x, y, size, size, 6, 6);

 
        g2.setColor(isSelected ? new Color(255, 215, 0) : SLOT_BORDER_COLOR);
        g2.setStroke(new BasicStroke(isSelected ? 2f : 1f));
        g2.drawRoundRect(x, y, size, size, 6, 6);

   
        g2.setColor(new Color(255, 255, 255, isSelected ? 40 : 20));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x + 1, y + 1, size - 2, size - 2, 5, 5);
    }


    protected void drawQuantityBadge(Graphics2D g2, int x, int y, int quantity) {
        String quantityStr = String.valueOf(quantity);
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(quantityStr);
        int badgeWidth = Math.max(textWidth + 8, 18);

       
        GradientPaint badgeGradient = new GradientPaint(
            x, y, new Color(220, 50, 50),
            x, y + 16, new Color(180, 30, 30)
        );
        g2.setPaint(badgeGradient);
        g2.fillRoundRect(x, y, badgeWidth, 16, 8, 8);

        
        g2.setColor(new Color(255, 255, 255, 180));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, badgeWidth, 16, 8, 8);

      
        g2.setColor(Color.WHITE);
        g2.drawString(quantityStr, x + (badgeWidth - textWidth) / 2, y + 12);
    }

    protected Color getCategoryColor(String category) {
        
        return switch (category.toLowerCase()) {
            case "tools" -> new Color(120, 180, 255);
            case "consumables" -> new Color(255, 120, 120);
            case "crops" -> new Color(120, 255, 120);
            case "fish" -> new Color(180, 120, 255);
            case "seeds" -> new Color(255, 180, 120);
            case "fuel" -> new Color(255, 255, 120);
            case "ring" -> new Color(255, 182, 193);
            default -> new Color(200, 200, 200); 
        };
    }
}