package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;

// a bullet which the player uses to attack
public class Bullet implements Writable {
    private int posX;
    private int posY;
    private boolean shot;

    public Bullet(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.shot = false;
    }

    // effects: returns the status of the bullet
    public boolean isShot() {
        return shot;
    }

    // effects: returns bullet x position
    public int getPosX() {
        return this.posX;
    }

    // effects: returns bullet y position
    public int getPosY() {
        return this.posY;
    }

    // modifies: this
    // effects: shoots the bullet
    public void getShot() {
        this.shot = true;
    }

    // modifies: this
    // effects: updates the bullet position
    public void updateBullet(int x, int y) {
        if (!this.shot) {
            posX = x;
            posY = y;
        } else {
            posX = posX + 20;
        }
    }

    // effects: writes bullet as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("xpos", posX);
        json.put("ypos", posY);
        return json;
    }
}
