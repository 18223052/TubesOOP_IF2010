package GUI.panels;

import main.GamePanel;
import entity.NPC;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints; // Import for antialiasing

public class NPCContextMenu extends BaseUIPanel {

    public NPCContextMenu(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
    }

    public void draw(Graphics2D g2, NPC currNPC) {

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

        if (currNPC != null) {
            String npcName = currNPC.getName();
            g2.setColor(SELECTED_SLOT_COLOR); 
            g2.drawString(npcName + " Pilihan:", textX, textY); 
            g2.setColor(TEXT_PRIMARY); 
            textY += lineHeight * 1.25;

            // Options
            g2.drawString("[E] Bicara", textX, textY); // Updated text
            textY += lineHeight;
            g2.drawString("[G] Beri Hadiah", textX, textY); // Updated text
            textY += lineHeight;

            boolean canDisplayProposeOption = gp.player.inventory.hasItem("ring") && gp.player.getFiance() != gp.currNPC && !gp.player.hasSpouse();

            if (canDisplayProposeOption) {
                if (gp.currNPC.isProposable(gp.player)) {
                    g2.setColor(Color.PINK);
                    g2.drawString("[P] Propose", textX, textY);
                } else {
                    g2.setColor(Color.ORANGE);
                    String proposeHint = "[P] Propose (?)";
                    if (!gp.currNPC.getRelationshipStatus().equals(NPC.STATUS_SINGLE) && !"neutral".equals(gp.currNPC.getRelationshipStatus())) {
                        proposeHint = "[P] Propose (Not Single)";
                    } else if (gp.currNPC.getHeartPoints() < gp.currNPC.getMaxHeartPoint()) { //
                        proposeHint = "[P] Propose (Low Hearts)";
                    }
                    g2.drawString(proposeHint, textX, textY);
                }
                g2.setColor(Color.WHITE);
                textY += lineHeight;
            } else if (gp.player != null && !gp.player.inventory.hasItem("ring") && gp.player.getFiance() != gp.currNPC && !gp.player.hasSpouse()) {
                g2.setColor(Color.GRAY);
                g2.drawString("[P] Propose (Need Ring)", textX, textY);
                g2.setColor(Color.WHITE);
                textY += lineHeight;
            }


            // --- Opsi Marry ---
            // Syarat: NPC adalah tunangan, bisa menikah hari ini, dan pemain punya cincin
            if (gp.player.getFiance() == gp.currNPC && //
                gp.currNPC.canMarryPlayer(gp.player, gp.gameTime.getGameDay())) { //

                if (gp.player.inventory.hasItem("ring")) {
                    g2.setColor(Color.CYAN);
                    g2.drawString("[M] Marry", textX, textY);
                } else {
                    g2.setColor(Color.GRAY);
                    g2.drawString("[M] Marry (Need Ring)", textX, textY);
                }
                g2.setColor(Color.WHITE);
                textY += lineHeight;
            }

            g2.setColor(new Color(255, 150, 150)); 
            g2.drawString("[ESC] Kembali", textX, textY); 
        }
    }
}