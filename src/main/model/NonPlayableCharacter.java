package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;

// NonPlayableCharacter or NPC represents the enemy
// in the game for the player to play against. NPC
// has the parameters health and damage.
public class NonPlayableCharacter implements Writable {
    private int health;
    private int damage;
    private int posX;
    private int posY;

    public NonPlayableCharacter(int health, int damage, int x, int y) {
        this.damage = damage;
        this.health = health;
        this.posX = x;
        this.posY = y;
    }

    public void updateNPC() {
        this.posX -= 2;
    }

    // modifies: health
    // effects: takes in a damage value and subtracts it from health
    public void takeDamage(int damage) {

        health = health - damage;
        System.out.println("Enemy Health is: " + health);
    }

    // effects: returns current NPC damage
    public int getDamage() {

        return damage;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    // effects: returns current NPC health
    public int getHealth() {
        return health;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("health", health);
        json.put("damage", damage);
        json.put("posX", posX);
        json.put("posY",posY);
        return json;
    }
}
