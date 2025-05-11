package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    StringBuilder inputBuffer = new StringBuilder();
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    public boolean interactPressed;
    public int singleNumPress;
    public String multiNumPress = "";

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if (Character.isDigit(ch)) {
            inputBuffer.append(ch);
            singleNumPress = ch - '0';
        } else {
            singleNumPress = -1;
            multiNumPress = inputBuffer.toString();
            inputBuffer.setLength(0);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_E) {
            interactPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        if (code == KeyEvent.VK_P) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;

            } else if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
            }
        }

        if (code == KeyEvent.VK_C) {

            // for debugging cooking
            // if (gp.gameState == gp.playState) {
            //     gp.gameState = gp.cookingState;

            // } else if (gp.gameState == gp.cookingState) {
            //     gp.gameState = gp.playState;
            // }

            // real cooking
            int playerCol = gp.player.wX / gp.tileSize;
            int playerRow = gp.player.wY / gp.tileSize;
            int tileNum = gp.tileM.mapTileNum[playerCol][playerRow];
            if ((tileNum == 125 || tileNum == 126) && gp.tileM.currentMap == gp.tileM.tile[33].destMap) {
                if (gp.gameState == gp.playState) {
                    gp.gameState = gp.cookingState;
                } else if (gp.gameState == gp.cookingState) {
                    gp.gameState = gp.playState;
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            interactPressed = false;
        }
    }

}
