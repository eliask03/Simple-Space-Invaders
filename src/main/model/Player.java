package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import ui.KeyHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

// represents the player in the game.
public class Player implements Writable {

    private int health;
    private int damage;
    private int level;
    private int exp;
    private ArrayList<Bullet> bullets;
    private int posX;
    private int posY;

    public Player(int damage, int health, int level, int exp, ArrayList<Bullet> bullets, int x, int y) {
        this.damage = damage;
        this.health = health;
        this.level = level;
        this.exp = exp;
        this.bullets = bullets;
        this.posX = x;
        this.posY = y;
    }

    // modifies: this
    // effects: moves the player based on an input
    public void movePlayer(String i) {
        if (i == "up") {
            posY -= 8;
        } else if (i == "down") {
            posY += 8;
        } else if (i == "right") {
            posX += 8;
        } else if (i == "left") {
            posX -= 8;
        }
    }

    // effects: returns current player damage
    public int getDamage() {
        return damage;
    }

    // effects: returns current player health
    public int getHealth() {
        return health;
    }

    // effects: returns current player exp
    public int getExp() {
        return exp;
    }

    // effects: returns current player level
    public int getLevel() {
        return level;
    }

    // modifies: health
    // effects: takes in a damage value and subtracts it from health
    public void takeDamage(int damage) {
        health = health - damage;
    }

    // modifies: exp
    // effects: adds 2 to the current exp value
    public void addExp() {
        exp += 2;
    }

    // modifies: exp, level, damage, health
    // effects: once the player exp level becomes ten or greater,
    // the player levels up and gets a health and damage boost.
    // when the player levels up the player's exp goes back to zero
    public boolean levelUp() {
        while (exp >= 10) {
            exp = exp - 10;
            level = level + 1;
            damage = damage + 25;
            health = health + 25;
            return true;
        }
        return false;
    }

    //effects: returns the amount of bullets the player has
    public int getBullets() {
        return bullets.size();
    }

    // effects: returns the list of bullets
    public ArrayList<Bullet> getBulletList() {
        return bullets;
    }

    // effects: returns the player x position
    public int getPosX() {
        return posX;
    }

    // effects: returns the player y position
    public int getPosY() {
        return posY;
    }

    // modifies: this
    // effects: player gets a new bullet
    public void reload() {
        if (bullets.size() < 8) {
            bullets.add(new Bullet(posX,posY));
            EventLog.getInstance().logEvent(new Event("Reloaded to " + getBullets() + " total bullets"));
        }
    }

    // modifies: this
    // effects: player spends a bullet
    public void useBullet() {
        bullets.remove(0);
        EventLog.getInstance().logEvent(new Event("Bullet spent, new total bullets: " + getBullets()));
    }

    // modifies: this
    // effects: handles bullet updating for player bullets
    public void updateBullets() {
        Iterator<Bullet> iterator = getBulletList().iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (bullet.getPosX() > 1000) {
                iterator.remove();
                EventLog.getInstance().logEvent(new Event("Bullet spent, new total bullets: " + getBullets()));
            }
            bullet.updateBullet(getPosX(), getPosY());
        }
    }

    // effect: writes player info to JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("health", health);
        json.put("damage", damage);
        json.put("level",level);
        json.put("exp", exp);
        json.put("bullets", bulletsToJson());
        json.put("posX",posX);
        json.put("posY",posY);
        return json;
    }

    // effects: creates a JSON array with bullets
    private JSONArray bulletsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Bullet b : bullets) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }
}
