package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// game application that shows off simple game features
public class Game {

    private static final String JSON_STORE = "./data/workroom.json";
    private GameSpace gameSpace;
    private Player player;
    private NonPlayableCharacter enemy;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // effects: runs game
    public Game() {
        gameSpace = new GameSpace("Save State");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        runGame();
    }

    // modifies: this
    // effects: keeps game running until a case occurs where
    // it is instructed to end. The structure of this function
    // was taken from AccountNotRobust - TellerApp.java
    private void runGame() {
        boolean keepGoing = true;
        String command = null;

        start();

        while (keepGoing) {
            while (player.levelUp()) {
                System.out.println("Player leveled up to " + player.getLevel());
            }
            displayMenu();

            command = input.next();

            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
            if (player.getHealth() <= 0 || enemy.getHealth() <= 0) {
                keepGoing = false;
                System.out.println("One of enemy or player was destroyed!");
            }
        }
        System.out.println("\nGame Over!");
    }

    /* standard player values
    this.damage = 25;
    this.health = 100;
    this.level = 1;
    this.exp = 0;
    this.bullets = new ArrayList<>(); */

    // modifies: this
    // effects: sets up objects necessary for game to run
    private void start() {
        player = new Player(25,100,1,0,new ArrayList<Bullet>(),100,100);
        enemy = new NonPlayableCharacter(200, 25,0,0);
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // effects: sets a display
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> attack enemy");
        System.out.println("\tg -> get attacked by enemy");
        System.out.println("\tr -> player reload");
        System.out.println("\th -> get player health");
        System.out.println("\tl -> get player level");
        System.out.println("\td -> get player damage");
        System.out.println("\te -> get enemy health");
        System.out.println("\ts -> save GameSpace");
        System.out.println("\tx -> load GameSpace");
        System.out.println("\tq -> quit");
    }

    // modifies: this
    // effects: depending on if the input is one or zero,
    // player attacks enemy, or enemy attacks player
    private void attack(int i) {
        if (i == 0) {
            enemy.takeDamage(player.getDamage());
            player.addExp();
        }
        if (i == 1) {
            player.takeDamage(enemy.getDamage());
        }
    }

    // modifies: this
    // effects: handles attack
    private void handleAttack(int i) {
        if (i == 0) {
            if (player.getBullets() > 0) {
                attack(0);
                player.useBullet();
                System.out.println("player attacks enemy!");
                System.out.println("enemy health is " + enemy.getHealth());
                System.out.println("player exp is " + player.getExp());
                System.out.println("player has " + player.getBullets() + " bullets left");
            } else {
                System.out.println("Player is out of bullets, reload!");
            }
        } else if (i == 1) {
            attack(1);
            System.out.println("enemy attacks player!");
            System.out.println("player health is " + player.getHealth());
        }
    }

    // modifies: this
    // effects: processes all different possible commands
    // and computes the necessary information
    private void processCommand(String command) {
        if (command.equals("a")) {
            handleAttack(0);
        } else if (command.equals("g")) {
            handleAttack(1);
        } else if (command.equals("h")) {
            System.out.println("player health is " + player.getHealth());
        } else if (command.equals("l")) {
            System.out.println("player level is " + player.getLevel());
        } else if (command.equals("d")) {
            System.out.println("player damage is " + player.getDamage());
        } else if (command.equals("e")) {
            System.out.println("enemy health is " + enemy.getHealth());
        } else if (command.equals("r")) {
            player.reload();
            System.out.println("Player reloaded and has " + player.getBullets() + " bullets");
        } else if (command.equals("s")) {
            saveGameSpace();
        } else if (command.equals("x")) {
            loadGameSpace();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: saves the GameSpace to file
    private void saveGameSpace() {
        gameSpace.addPlayer(player);
        gameSpace.addNpc(enemy);
        try {
            jsonWriter.open();
            jsonWriter.write(gameSpace);
            jsonWriter.close();
            System.out.println("Saved " + gameSpace.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads GameSpace from file
    private void loadGameSpace() {
        try {
            gameSpace = jsonReader.read();
            enemy = gameSpace.getNpcs().get(0);
            player = gameSpace.getPlayers().get(0);
            System.out.println("Loaded " + gameSpace.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
