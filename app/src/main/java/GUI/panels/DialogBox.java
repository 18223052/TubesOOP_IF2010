package GUI.panels;

import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints; 

public class DialogBox extends BaseUIPanel {

    public DialogBox(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2, String currentDialog, String speakerName, boolean hasStore) {

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int x = gp.tileSize * 2;
        int y = gp.tileSize * 8;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 3;


        drawSubWindow(g2, x, y, width, height);

        g2.setFont(uiFont.deriveFont(20f));
        g2.setColor(TEXT_PRIMARY); 

        int textX = x + 20;
        int textY = y + 30;
        int lineHeight = 30;

        if (speakerName != null && !speakerName.isEmpty()) {
            g2.setColor(SELECTED_SLOT_COLOR); 
            g2.drawString(speakerName, textX, textY);
            g2.setColor(TEXT_PRIMARY); 
            textY += lineHeight;
        }


        if (currentDialog != null && !currentDialog.isEmpty()) {
            String[] paragraphs = currentDialog.split("\n");

            for (String paragraph : paragraphs) {
                String[] words = paragraph.split(" ");
                StringBuilder line = new StringBuilder();

                for (String word : words) {
                    if (g2.getFontMetrics().stringWidth(line + " " + word) < width - 40) {
                        if (line.length() > 0) {
                            line.append(" ");
                        }
                        line.append(word);
                    } else {
                        g2.drawString(line.toString(), textX, textY);
                        textY += lineHeight;
                        line = new StringBuilder(word);

                        if (textY > y + height - 30 - lineHeight) {
                            g2.drawString("...", textX + width - 50, textY);
                            break;
                        }
                    }
                }

                if (line.length() > 0) {
                    if (textY <= y + height - 30) {
                        g2.drawString(line.toString(), textX, textY);
                        textY += lineHeight;
                    }
                }
                if (textY > y + height - 30 - lineHeight && paragraphs.length > 1 && !paragraph.equals(paragraphs[paragraphs.length-1])) {
                    break;
                }
                textY += 5;
            }
            g2.setFont(uiFont.deriveFont(Font.ITALIC, 16f));
            g2.setColor(new Color(100, 255, 100)); 
            g2.drawString("Tekan [E] untuk melanjutkan", x + width - 210, y + height - 20); 

        } else {
            g2.drawString("...", textX, textY);
            g2.setFont(uiFont.deriveFont(Font.ITALIC, 16f));
            g2.setColor(new Color(255, 150, 150)); 
            g2.drawString("Tekan [ESC] untuk keluar", x + width - 210, y + height - 20); 
        }
    }
}