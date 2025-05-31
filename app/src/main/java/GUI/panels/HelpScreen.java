package GUI.panels;

import main.GamePanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class HelpScreen extends BaseUIPanel {

    private Font helpContentFont;
    private Font smallTextFont; 

    public HelpScreen(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
        this.helpContentFont = uiFont.deriveFont(Font.PLAIN, 28F); 
        this.smallTextFont = uiFont.deriveFont(Font.PLAIN, 20F); 
    }

    public void draw(Graphics2D g2) {

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int frameX = gp.tileSize * 2;
        int frameY = gp.tileSize * 1;
        int frameWidth = gp.screenWidth - (gp.tileSize * 4);
        int frameHeight = gp.screenHeight - (gp.tileSize * 2);
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight);


        g2.setFont(titleTextFont);
        g2.setColor(HEADER_COLOR); 
        String title = "BANTUAN";
        int x = getXforCenteredText(g2, title, gp.screenWidth); 
        int y = frameY + gp.tileSize; 
        g2.drawString(title, x, y);


        g2.setFont(helpContentFont);
        g2.setColor(TEXT_PRIMARY); 

        String[] helpLines = {
            "Selamat datang di Spakbor Hills!",
            "",
            "Kontrol Game:",
            "  - W / UP    : Bergerak ke atas",
            "  - A / LEFT  : Bergerak ke kiri",
            "  - S / DOWN  : Bergerak ke bawah",
            "  - D / RIGHT : Bergerak ke kanan",
            "  - E/ENTER     : Berinteraksi",
            "  - ESC       : Buka menu / Kembali",
            ""
        };


        int textStartY = y + gp.tileSize; 


        int currentTextY = textStartY;
        for (String line : helpLines) {


            int lineX;
            if (line.startsWith("  - ")) { 

                int centeredX = getXforCenteredText(g2, line, gp.screenWidth);
                lineX = centeredX + gp.tileSize / 2;
            } else {
                lineX = getXforCenteredText(g2, line, gp.screenWidth);
            }

            g2.drawString(line, lineX, currentTextY);
            currentTextY += g2.getFontMetrics(helpContentFont).getHeight() + 8; 
        }


        g2.setFont(smallTextFont);
        g2.setColor(TEXT_PRIMARY); 
        String returnMessage = "Tekan ESC untuk Kembali ke Layar Judul.";
        int returnMessageX = getXforCenteredText(g2, returnMessage, gp.screenWidth);
        int returnMessageY = frameY + frameHeight - (gp.tileSize / 2); 
        g2.drawString(returnMessage, returnMessageX, returnMessageY);
    }

    private int getXforCenteredText(Graphics2D g2, String text, int totalWidth) {
        FontMetrics fm = g2.getFontMetrics();
        int length = fm.stringWidth(text);
        return (totalWidth - length) / 2;
    }
}