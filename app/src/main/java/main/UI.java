package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import entity.Ingredient;
import entity.Recipe;

public class UI {

    GamePanel gp;
    Font arial_40;
    Graphics2D g2;
    Recipe recipe;

    int selectRecipe = 0;
    boolean hasIngradients, doneCooking, hasHotPapper;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        // g2.drawString("Key = " + gp.player.hasKey, 50, 50);

        if (gp.gameState == gp.playState) {
        } else if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        } else if (gp.gameState == gp.cookingState) {
            drawCookingMenu();
        }

    }

    public void drawPauseScreen() {
        this.g2.setFont(this.g2.getFont().deriveFont(0, 60.0F));
        String text = "PAUSED";
        int x = getXforCenteredText(text, gp.screenWidth);
        // this.gp.getClass();
        int y = 576 / 2;
        this.g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text, int screenWidth) {
        int length = (int) this.g2.getFontMetrics().getStringBounds(text, this.g2).getWidth();
        // this.gp.getClass();
        int x = screenWidth / 2 - length / 2;
        return x;
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        this.g2.setColor(c);
        this.g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        this.g2.setColor(c);
        this.g2.setStroke(new BasicStroke(5.0F));
        this.g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawCookingMenu() {
        // ganti hasIngradients menjadi false untuk test tidak memiliki ingradients
        hasIngradients = true;
        String text;
        List<Recipe> recipes = recipe.getRecipeList();

        int x = 20;
        int y = x;
        int width = gp.screenWidth / 3;
        int height = gp.screenHeight / 2 + gp.tileSize + y;
        drawSubWindow(x, y, width, height);

        if (doneCooking) {
            text = "Congratulations";
            x += y;
            y += gp.tileSize;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
            this.g2.drawString(text, x, y);
            y += 20;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));

            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("You succeeded in cooking", x, y);
            y += this.g2.getFontMetrics().getHeight();
            this.g2.drawString(recipes.get(selectRecipe - 1).title, x, y);
            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("Press 0 to recipe menu", x, y);

            if (gp.keyH.singleNumPress == 0) {
                selectRecipe = 0;
                doneCooking = false;
            }
        } else if (selectRecipe == 0) {
            text = "RECIPE LIST";
            x += y;
            y += gp.tileSize;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
            this.g2.drawString(text, x, y);
            y += 20;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));

            int i = 0;
            for (Recipe recipe : recipes) {
                i++;
                y += this.g2.getFontMetrics().getHeight();
                this.g2.drawString(i + ". " + recipe.title, x, y);
            }
            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("Press number you want to cook", x, y);
            y += this.g2.getFontMetrics().getHeight();
            this.g2.drawString("and hit enter.", x, y);

            if (gp.keyH.enterPressed && !gp.keyH.multiNumPress.equals("")) {
                if (Integer.parseInt(gp.keyH.multiNumPress) >= 1 && Integer.parseInt(gp.keyH.multiNumPress) < 11) {
                    selectRecipe = Integer.parseInt(gp.keyH.multiNumPress);
                    gp.keyH.multiNumPress = "";
                }
                // gp.keyH.keyPressed = "";
            }
        } else if (selectRecipe == -1) {
            text = "Failed to Cook";
            x += y;
            y += gp.tileSize;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
            this.g2.drawString(text, x, y);
            y += 20;
            this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));

            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("Please complete the ingredients", x, y);
            y += this.g2.getFontMetrics().getHeight();
            this.g2.drawString("to be able to cook.", x, y);
            y += this.g2.getFontMetrics().getHeight() * 2;
            this.g2.drawString("Press 0 to recipe menu", x, y);

            if (gp.keyH.singleNumPress == 0) {
                selectRecipe = 0;
            }
        } else {
            if (selectRecipe == 8 && !hasHotPapper) {
                text = "Failed to Open Recipe";
                x += y;
                y += gp.tileSize;
                this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
                this.g2.drawString(text, x, y);
                y += 20;
                this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));

                y += this.g2.getFontMetrics().getHeight() * 2;
                this.g2.drawString("You must have a hot papper ", x, y);
                y += this.g2.getFontMetrics().getHeight();
                this.g2.drawString("to view the recipe.", x, y);
                y += this.g2.getFontMetrics().getHeight() * 2;
                this.g2.drawString("Press 0 to recipe menu", x, y);

                if (gp.keyH.singleNumPress == 0) {
                    selectRecipe = 0;
                }
            } else {
                text = recipes.get(selectRecipe - 1).title + " Ingredient";
                x += y;
                y += gp.tileSize;
                this.g2.setFont(this.g2.getFont().deriveFont(0, 20.0F));
                this.g2.drawString(text, x, y);
                y += 20;
                int i = 0;
                this.g2.setFont(this.g2.getFont().deriveFont(0, 15.0F));
                for (Ingredient ingredient : recipes.get(selectRecipe - 1).ingredients) {
                    i++;
                    y += this.g2.getFontMetrics().getHeight();
                    this.g2.drawString(i + ". " + ingredient.name + " " + ingredient.amount + "x", x, y);
                }
                y += this.g2.getFontMetrics().getHeight() * 2;
                this.g2.drawString("Press 1 to cook", x, y);
                y += this.g2.getFontMetrics().getHeight();
                this.g2.drawString("Press 0 to recipe menu", x, y);
                if (gp.keyH.singleNumPress == 0) {
                    selectRecipe = 0;
                } else if (gp.keyH.singleNumPress == 1) {
                    hasIngradients = selectRecipe == 2 || selectRecipe == 5 || selectRecipe == 6 || selectRecipe == 9;

                    if (hasIngradients) {
                        doneCooking = true;
                        // do event on successfully cooked
                    } else {
                        selectRecipe = -1;
                    }
                }
            }

        }

        // System.out.println(gp.keyH.enterPressed);
        // System.out.println(gp.keyH.keyPressed);
    }
}
