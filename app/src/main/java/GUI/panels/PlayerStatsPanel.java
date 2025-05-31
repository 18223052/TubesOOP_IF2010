package GUI.panels;

import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO; 

public class PlayerStatsPanel extends BaseUIPanel {

    private BufferedImage goldIcon;
    private BufferedImage energyIcon;

    public PlayerStatsPanel(GamePanel gp, Font uiFont) {
        super(gp, uiFont);
        loadIcons();
    }

    private void loadIcons() {
        try {

            goldIcon = ImageIO.read(getClass().getResourceAsStream("/items/gold.png"));
            energyIcon = ImageIO.read(getClass().getResourceAsStream("/items/energy.png")); 

        } catch (Exception e) {
            System.err.println("Gagal memuat ikon status pemain: " + e.getMessage());

        }
    }

    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        int panelX = gp.tileSize / 2; 
        int panelY = gp.tileSize / 2; 
        int currentX = panelX;
        int currentY = panelY;

        g2.setFont(uiFont);


        if (gp.player != null) {
            int maxEnergy = gp.player.getMaxEnergy();
            int currentEnergy = gp.player.getEnergy();

    
            if (energyIcon != null) {
                g2.drawImage(energyIcon, currentX, currentY, 32, 32, null);
            } else {
                g2.setColor(new Color(0, 0, 0, 150)); 
                g2.drawString("Energy:", currentX + 2, currentY + 18);
                g2.setColor(new Color(200, 200, 255)); 
                g2.drawString("Energy:", currentX, currentY + 20);
            }


            int barWidth = 200;
            int barHeight = 20;
            int barX = currentX + (energyIcon != null ? 40 : 70); 
            int barY = currentY + (barHeight / 2);

            // Background bar (kosong)
            g2.setColor(new Color(50, 50, 50, 200)); 
            g2.fillRoundRect(barX, barY, barWidth, barHeight, 10, 10);

            // Isi bar energi
            double energyRatio = (double) currentEnergy / maxEnergy;
            int filledWidth = (int) (barWidth * energyRatio);

            Color energyBarColor;
            if (currentEnergy <= 20) {
                energyBarColor = new Color(255, 50, 50);
            } else if (currentEnergy <= 50) {
                energyBarColor = new Color(255, 200, 0); 
            } else {
                energyBarColor = new Color(136, 231, 136); 
            }
            g2.setColor(energyBarColor);
            g2.fillRoundRect(barX, barY, filledWidth, barHeight, 10, 10);

            // Border bar
            g2.setColor(new Color(150, 150, 150, 200)); 
            g2.drawRoundRect(barX, barY, barWidth, barHeight, 10, 10);

            // Teks persentase/nilai energi di tengah bar
            String energyValue = currentEnergy + "/" + maxEnergy;
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F)); 
            int textEnergyWidth = (int) g2.getFontMetrics().getStringBounds(energyValue, g2).getWidth();
            g2.drawString(energyValue, barX + barWidth / 2 - textEnergyWidth / 2, barY + barHeight - 5);
            g2.setFont(uiFont); 

        } else {
            g2.setColor(Color.RED);
            g2.drawString("Player data not available!", currentX, currentY);
        }

        // --- Informasi Gold ---
        currentY += 50; 

        if (gp.player != null) {
            String goldText = String.format("%d Gold", gp.player.getGold());

            // Ikon Gold
            if (goldIcon != null) {
                g2.drawImage(goldIcon, currentX, currentY, 32, 32, null);
            }

            // Teks Gold
            g2.setColor(new Color(0, 0, 0, 150)); 
            g2.drawString(goldText, currentX + (goldIcon != null ? 40 : 0) + 2, currentY + 18);
            g2.setColor(new Color(255, 200, 0)); 
            g2.drawString(goldText, currentX + (goldIcon != null ? 40 : 0), currentY + 20);
        } else {
            g2.setColor(Color.RED);
            g2.drawString("Gold data not available!", currentX, currentY);
        }
    }
}