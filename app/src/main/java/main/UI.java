package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

// Import all new UI panel classes
import GUI.panels.HUD;
import GUI.panels.PauseScreen;
import GUI.panels.DialogBox;
import GUI.panels.InventoryScreen;
import GUI.panels.CharacterScreen;
import GUI.panels.CookingMenu;
import GUI.panels.ShippingBinMenu;
import GUI.panels.StoreMenu;
import GUI.panels.TitleScreen;
import GUI.panels.NPCContextMenu;
import GUI.panels.NameInputScreen;

public class UI {

    GamePanel gp;
    public Graphics2D g2;

    // Deklarasikan font dengan nama yang lebih spesifik
    public Font generalFont, dialogFont, inventoryFont, cookingFont, smallFont;

    // References to your new UI panel objects
    private HUD hud;
    private PauseScreen pauseScreen;
    private DialogBox dialogBox;
    private InventoryScreen inventoryScreen;
    private CharacterScreen characterScreen;
    public CookingMenu cookingMenu;
    private ShippingBinMenu shippingBinMenu;
    private StoreMenu storeMenu;
    private NPCContextMenu npcContextMenu;
    public TitleScreen titleScreen;
    private NameInputScreen nameInputScreen;

    public String currentDialog = "";

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream inputStream = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            Font baseMaruMonica = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(inputStream));

            // Inisialisasi setiap font dengan ukuran yang berbeda
            this.generalFont = baseMaruMonica.deriveFont(Font.PLAIN, 30f);
            this.dialogFont = baseMaruMonica.deriveFont(Font.PLAIN, 28f);  
            this.inventoryFont = baseMaruMonica.deriveFont(Font.PLAIN, 26f); 
            this.cookingFont = baseMaruMonica.deriveFont(Font.PLAIN, 20f); 
            this.smallFont = baseMaruMonica.deriveFont(Font.PLAIN, 18f);   

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            // Fallback fonts jika font kustom gagal dimuat
            this.generalFont = new Font("Arial", Font.PLAIN, 30);
            this.dialogFont = new Font("Arial", Font.PLAIN, 28);
            this.inventoryFont = new Font("Arial", Font.PLAIN, 26);
            this.cookingFont = new Font("Arial", Font.PLAIN, 30);
            this.smallFont = new Font("Arial", Font.PLAIN, 18);
        }

        // Inisialisasi semua panel UI, passing font yang sesuai
        hud = new HUD(gp, generalFont);
        pauseScreen = new PauseScreen(gp, generalFont);
        dialogBox = new DialogBox(gp, dialogFont);
        inventoryScreen = new InventoryScreen(gp, inventoryFont); 
        characterScreen = new CharacterScreen(gp, generalFont); 
        cookingMenu = new CookingMenu(gp, cookingFont); 
        shippingBinMenu = new ShippingBinMenu(gp, inventoryFont);
        storeMenu = new StoreMenu(gp, inventoryFont); 
        npcContextMenu = new NPCContextMenu(gp, generalFont); 
        titleScreen = new TitleScreen(gp, generalFont);
        nameInputScreen = new NameInputScreen(gp, generalFont);
    }

    private void setupDefaultGraphics(Graphics2D g2) {

        g2.setFont(generalFont);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        setupDefaultGraphics(g2); // Tetap panggil ini untuk pengaturan global (anti-aliasing, warna)

        switch (gp.gameState) {
            case GamePanel.titleState: // Tambahkan case untuk titleState
                titleScreen.draw(g2);
                break;
            case GamePanel.nameInputState: // Tambahkan case untuk nameInputState
                nameInputScreen.draw(g2);
                break;
            case GamePanel.playState:
                hud.draw(g2);
                break;
            case GamePanel.pauseState:
                pauseScreen.draw(g2);
                break;
            case GamePanel.dialogState:
                dialogBox.draw(g2, currentDialog, gp.currNPC != null ? gp.currNPC.getName() : null, gp.currNPC != null && gp.currNPC.hasStore());
                break;
            case GamePanel.inventoryState:
                inventoryScreen.draw(g2, gp.isGifting);
                break;
            case GamePanel.statsState:
                characterScreen.draw(g2);
                break;
            case GamePanel.cookingState:
                cookingMenu.draw(g2);
                break;
            case GamePanel.shippingBinState:
                shippingBinMenu.draw(g2);
                inventoryScreen.draw(g2, false);
                break;
            case GamePanel.storeState:
                storeMenu.draw(g2);
                inventoryScreen.draw(g2, false); 
                break;
            case GamePanel.npcContextMenuState:
                npcContextMenu.draw(g2, gp.currNPC);
                break;
            case GamePanel.fishingState:
                dialogBox.draw(g2, currentDialog, null, false);
                break;
        }
    }

    public void setDialog(String dialog) {
        this.currentDialog = dialog;
    }

    public void clearDialog() {
        this.currentDialog = "";
    }

    public int getCommandNum() {
        if (titleScreen != null) {
            return titleScreen.commandNum;
        }
        return -1;
    }

    public void setCommandNum(int num) {
        if (titleScreen != null) {
            titleScreen.commandNum = num;
        }
    }
}