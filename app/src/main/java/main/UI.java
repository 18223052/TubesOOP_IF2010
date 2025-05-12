package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import entity.NPC_mayortadi;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font retroFont, arial_20;
    private Font maruMonica;


    public String currentDialog = "";
    
    public UI(GamePanel gp) {
        this.gp = gp;

        try {

            InputStream inputStream = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            this.maruMonica = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(inputStream));
            

            arial_20 = maruMonica.deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();

            maruMonica = new Font("Arial", Font.PLAIN, 16);
            arial_20 = maruMonica.deriveFont(20f);
        }
    }
    
    private void setupDefaultGraphics(Graphics2D g2) {
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        setupDefaultGraphics(g2);

        if (gp.gameState == gp.playState) {
   
        }
        if (gp.gameState == gp.pauseState) {
            drawPause();
        }
        if (gp.gameState == gp.dialogState) {
            drawDialogScreen();
        }
        if (gp.gameState == gp.inventoryState) {

            drawInventoryScreen(g2);
        }
        if (gp.gameState == gp.statsState){
            drawCharacterScreen();
        }
    }

    public void drawInventoryScreen(Graphics2D g2) {

        gp.inventoryController.draw(g2);  }

    public void drawCharacterScreen(){
      

        final int frameX = gp.tileSize*2;
        final int frameY = gp.tileSize;
        final int frameWidth= gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 32;


        g2.drawString("Name", textX, textY);
        textY += lineHeight;
        g2.drawString("Gender", textX, textY);
        textY += lineHeight;
        g2.drawString("Farm Map", textX, textY);
        textY += lineHeight;
        g2.drawString("Energy", textX, textY);
        textY += lineHeight;
        g2.drawString("Gold", textX, textY);
        textY += lineHeight;
        g2.drawString("Partner", textX, textY);
        textY += lineHeight;
        // values
        int tailX = (frameX + frameWidth) - 30;
        // reset
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.getName());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getGender());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getFarmMap());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getGold());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getEnergy());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.getPartner());
        textX = getXforAllignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
    }

    public void drawPause() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSE";
        int x = getXCenterText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public void drawDialogScreen() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 8; 
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 3;
    
        drawSubWindow(x, y, width, height);
        
        g2.setFont(maruMonica.deriveFont(20f));
        g2.setColor(Color.WHITE);
        
        int textX = x + 20;
        int textY = y + 30;
        int lineHeight = 30;
        

        if (gp.currNPC != null) {
            String npcName = "";
            if (gp.currNPC instanceof NPC_mayortadi) {
                npcName = ((NPC_mayortadi)gp.currNPC).getName();
                

                g2.setColor(new Color(255, 255, 150));
                g2.drawString(npcName, textX, textY);
                g2.setColor(Color.WHITE); 
                

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
                            
                          
                            if (textY > y + height - 30) {
                                
                                g2.drawString("...", width - 50, textY);
                                break;
                            }
                        }
                    }
                    
                    
                    if (line.length() > 0) {
                        g2.drawString(line.toString(), textX, textY);
                        textY += lineHeight;
                    }
                    
                    
                    textY += 5;
                }
            } else {
                
                g2.drawString("...", textX, textY);
            }
            
           
            g2.setFont(maruMonica.deriveFont(Font.ITALIC, 16f));
            g2.drawString("Press [E] to continue", x + width - 180, y + height - 20);
        } else {
 
            g2.drawString("No one to talk to...", textX, textY);
        }
    }
    
 
    public void setDialog(String dialog) {
        this.currentDialog = dialog;
    }
    
    public void drawSubWindow(int x, int y, int width, int height) {
   
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new java.awt.BasicStroke(5)); 
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public int getXCenterText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    public int getXforAllignToRight(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }

    public void drawInventory(){
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight= gp.tileSize*5;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    }

    public void drawItemPickup(String itemName) {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        String msg = "Picked up: " + itemName;
        int x = getXCenterText(msg);
        int y = gp.tileSize * 5;
        
 
        g2.setColor(Color.BLACK);
        g2.drawString(msg, x+2, y+2);
        

        g2.setColor(Color.WHITE);
        g2.drawString(msg, x, y);
    }
}