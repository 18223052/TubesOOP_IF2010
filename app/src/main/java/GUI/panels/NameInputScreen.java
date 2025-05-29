package GUI.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.GamePanel;

public class NameInputScreen extends BaseUIPanel{
    private Font inputPromptFont;
    private Font inputFieldFont;
    private Font instructionTextFont;

    public NameInputScreen(GamePanel gp, Font uiFont){
        super(gp, uiFont);

        this.inputPromptFont = uiFont.deriveFont(Font.BOLD, 40F);
        this.inputFieldFont = uiFont.deriveFont(Font.PLAIN, 36F);
        this.instructionTextFont = uiFont.deriveFont(Font.PLAIN, 24F);
    }

    public void draw(Graphics2D g2){
    g2.setColor(new Color(0,0,0));
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

    g2.setFont(inputPromptFont);
    g2.setColor(TEXT_PRIMARY); // Use a visible color, e.g., white
    String text = "Masukkan Nama:";
    int x = getXforCenteredText(g2, text, gp.screenWidth);
    int y = gp.screenHeight/2 - gp.tileSize;
    g2.drawString(text, x, y);

    g2.setColor(SLOT_BACKGROUND_COLOR.brighter());
    int inputRectX  = gp.screenWidth / 2 - 150;
    int inputRectY = gp.screenHeight/  2;
    int inputRectWidth = 300;
    int inputRectHeight = 48;

    g2.setColor(SLOT_BORDER_COLOR);
    g2.setStroke(new BasicStroke(2f));
    g2.drawRoundRect(inputRectX, inputRectY, inputRectWidth, inputRectHeight, CORNER_RADIUS, CORNER_RADIUS);

    g2.setFont(inputFieldFont);
    // Change this line to draw text in a visible color
    g2.setColor(Color.WHITE); // Or TEXT_PRIMARY, or new Color(200, 200, 200); // A light gray
    // Ensure TEXT_PRIMARY is something visible, if not, explicitly use Color.WHITE or similar.

    String inputText = gp.playerNameInput;
    inputText += "_";

    x = getXforCenteredText(g2, inputText, gp.screenWidth);
    y = gp.screenHeight / 2 + (int)(gp.tileSize * 0.75);
    g2.drawString(inputText, x, y);

    g2.setFont(instructionTextFont);
    g2.setColor(TEXT_SECONDARY); 
    text = "Tekan ENTER";
    x = getXforCenteredText(g2, text, gp.screenWidth);
    y = gp.screenHeight / 2 + gp.tileSize * 2;
    g2.drawString(text, x, y);
}

    private int getXforCenteredText(Graphics2D g2, String text, int frameWidth) {
        FontMetrics fm = g2.getFontMetrics();
        int length = fm.stringWidth(text);
        int x = (frameWidth - length) / 2;
        return x;
    }

}
