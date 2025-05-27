// src/ui/panels/NPCContextMenu.java
package GUI.panels;

import main.GamePanel;
import entity.NPC;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class NPCContextMenu extends BaseUIPanel {

    public NPCContextMenu(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2, NPC currNPC) {
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 8;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 3;

        drawSubWindow(g2, x, y, width, height); // Use utility from BaseUIPanel

        g2.setFont(uiFont.deriveFont(20f));
        g2.setColor(Color.WHITE);

        int textX = x + 20;
        int textY = y + 30;
        int lineHeight = 30;

        if (currNPC != null) {
            String npcName = currNPC.getName();
            g2.setColor(new Color(255, 255, 150));
            g2.drawString(npcName + " Options:", textX, textY);
            g2.setColor(Color.WHITE);
            textY += lineHeight * 1.25;

            // Options
            g2.drawString("[E] Talk", textX, textY);
            textY += lineHeight;
            g2.drawString("[G] Give Gift", textX, textY);
            textY += lineHeight;

            g2.drawString("[ESC] Back", textX, textY);
        }
    }
}