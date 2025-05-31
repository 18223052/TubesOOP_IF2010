package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


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
import GUI.panels.PlayerStatsPanel;

// --- Impor HelpScreen dan CreditsScreen ---
import GUI.panels.HelpScreen;
import GUI.panels.CreditsScreen;


public class UI {

    GamePanel gp;
    public Graphics2D g2;


    public Font generalFont, dialogFont, inventoryFont, cookingFont, smallFont;


    private HUD hud;
    private PlayerStatsPanel playerStatsPanel;
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

    // --- Deklarasikan HelpScreen dan CreditsScreen ---
    public HelpScreen helpScreen;
    public CreditsScreen creditsScreen;


    public String currentDialog = "";

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream inputStream = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            Font baseMaruMonica = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(inputStream));


            this.generalFont = baseMaruMonica.deriveFont(Font.PLAIN, 30f);
            this.dialogFont = baseMaruMonica.deriveFont(Font.PLAIN, 28f);
            this.inventoryFont = baseMaruMonica.deriveFont(Font.PLAIN, 26f);
            this.cookingFont = baseMaruMonica.deriveFont(Font.PLAIN, 20f);
            this.smallFont = baseMaruMonica.deriveFont(Font.PLAIN, 18f);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();

            this.generalFont = new Font("Arial", Font.PLAIN, 30);
            this.dialogFont = new Font("Arial", Font.PLAIN, 28);
            this.inventoryFont = new Font("Arial", Font.PLAIN, 26);
            this.cookingFont = new Font("Arial", Font.PLAIN, 30);
            this.smallFont = new Font("Arial", Font.PLAIN, 18);
        }


        hud = new HUD(gp, generalFont);
        playerStatsPanel = new PlayerStatsPanel(gp, generalFont);
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
        helpScreen = new HelpScreen(gp, generalFont);
        creditsScreen = new CreditsScreen(gp, generalFont);
    }

    private void setupDefaultGraphics(Graphics2D g2) {
        g2.setFont(generalFont);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        setupDefaultGraphics(g2);

        switch (gp.gameState) {
            case GamePanel.titleState:
                titleScreen.draw(g2);
                break;
            case GamePanel.nameInputState:
                nameInputScreen.draw(g2);
                break;
            case GamePanel.playState:
                hud.draw(g2);
                playerStatsPanel.draw(g2);
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
            case GamePanel.helpState:
                helpScreen.draw(g2);
                break;
            case GamePanel.creditState:
                creditsScreen.draw(g2);
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