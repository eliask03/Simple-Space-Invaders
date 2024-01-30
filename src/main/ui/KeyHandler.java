package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// handles all key inputs in the game
public class KeyHandler implements KeyListener {

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean spacePressed;
    private boolean downPressed;
    private boolean upPressed;
    private boolean pausePressed;
    private boolean reloadPressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isPausePressed() {
        return pausePressed;
    }

    public boolean isSpacePressed() {
        return spacePressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isReloadPressed() {
        return reloadPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    //modifies: this
    //effects: sets pressed value to true when key is pressed
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
        if (code == KeyEvent.VK_R) {
            reloadPressed = true;
        }
        if (code == KeyEvent.VK_P) {
            pausePressed = true;
        }
    }

    //modifies: this
    //effects: sets pressed value to false when key is released
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
        if (code == KeyEvent.VK_R) {
            reloadPressed = false;
        }
        if (code == KeyEvent.VK_P) {
            pausePressed = false;
        }
    }
}
