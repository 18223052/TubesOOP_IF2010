
package GUI.panels;

import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PauseScreen extends BaseUIPanel {

    public PauseScreen(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2) {
        g2.setFont(uiFont.deriveFont(Font.PLAIN, 80F)); 
        String text = "PAUSE";
        int x = getXforCenteredText(text, gp.screenWidth, g2); 
        int y = gp.screenHeight / 2;

        g2.setColor(Color.white); 
        g2.drawString(text, x, y);
    }

    private int getXforCenteredText(String text, int screenWidth, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return screenWidth / 2 - length / 2;
    }
}