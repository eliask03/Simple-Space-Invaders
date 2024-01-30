package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// manages the game window and game
public class GamePanel extends JPanel implements Runnable {

    private static final String JSON_STORE = "./data/workroom.json";

    private int gameState;
    private int score;
    private int acc;


    Random rand = new Random();
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private GameSpace gameSpace;

    Player player;
    NonPlayableCharacter enemy0;
    NonPlayableCharacter enemy1;
    NonPlayableCharacter enemy2;
    NonPlayableCharacter enemy3;
    NonPlayableCharacter enemy4;
    ArrayList<NonPlayableCharacter> enemies;

    // effects: sets game window to requested specifications
    public GamePanel() {
        this.setPreferredSize(new Dimension(768, 576));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    // modifies: this
    // effects: starts the game thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // modifies: this
    // effects: initiates all values before game begins
    public void init() {
        this.score = 0;
        this.acc = 0;
        this.gameState = 0;
        player = new Player(25, 200, 1, 0, new ArrayList<>(), 100, 100);

        enemy0 = new NonPlayableCharacter(200, 50, 1000, 100);
        enemy1 = new NonPlayableCharacter(200, 50, 1500, 400);
        enemy2 = new NonPlayableCharacter(200, 50, 2000, 250);
        enemy3 = new NonPlayableCharacter(200, 50, 2500, 300);
        enemy4 = new NonPlayableCharacter(200, 50, 3000, 100);


        enemies = new ArrayList<NonPlayableCharacter>();
        enemies.add(enemy0);
        enemies.add(enemy1);
        enemies.add(enemy2);
        enemies.add(enemy3);
        enemies.add(enemy4);

        gameSpace = new GameSpace("Save State");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
    }


    // modifies: this
    // effects: handles the updating/ changing of the window
    @Override
    public void run() {

        double drawInterval = 1000000000 / 60;
        double nextDrawTime = System.nanoTime() + drawInterval;
        init();

        while (gameThread != null) {


            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    // modifies: this
    // effects: updates the list of enemies
    public void updateEnemies() {
        Iterator<NonPlayableCharacter> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            NonPlayableCharacter npc = iterator.next();
            if (npc.getHealth() <= 0) {
                iterator.remove();
                this.score += 1;
            }
            npc.updateNPC();
            handleCollision(npc);
        }
    }

    // effects: renders the enemies on the screen
    public void renderEnemies(Graphics g) {
        for (NonPlayableCharacter npc : enemies) {
            renderNPC(g,npc);
        }
    }

    // modifies: this
    // effects: handles the update on each window refresh
    public void update() {
        handlePause();
        if (gameState == 0) {
            updatePlayer(keyH, player);
            if (enemies.size() > 0) {
                updateEnemies();
            }
            generateEnemies();
        }
        if (gameState == 1) {
            handleSaveLoad();
        }
    }

    // modifies: this
    // effects: handles controls for save and load
    public void handleSaveLoad() {
        if (keyH.isDownPressed()) {
            saveGameSpace();
        } else if (keyH.isLeftPressed()) {
            loadGameSpace();
        }
    }

    // effects: handles controls for pausing the game
    public void handlePause() {
        if (keyH.isPausePressed()) {
            pause();
        }
    }

    // modifies: this
    // effects: pauses the game
    public void pause() {
        if (gameState == 0) {
            gameState = 1;
        } else if (gameState == 1) {
            gameState = 0;
        }
    }

    // modifies: this
    // effects: generates enemies increasingly further down the map
    public void generateEnemies() {
        if (enemies.size() < 10) {
            int randomNumber = rand.nextInt(501) + 50;
            enemies.add(new NonPlayableCharacter(200, 25, 3500 + acc, randomNumber));
            acc += 500;
        }
    }

    // modifies: this
    // effects: handles bullet collision with enemies
    public void handleCollision(NonPlayableCharacter npc) {
        for (Bullet bullet : player.getBulletList()) {
            if (collideX(bullet.getPosX(), npc) && collideY(bullet.getPosY(), npc)) {
                npc.takeDamage(npc.getDamage());
            }
        }
    }

    // effects: returns true if position is in npc x range
    public boolean collideX(int x, NonPlayableCharacter npc) {
        if (x >= npc.getPosX() && x <= (npc.getPosX() + 30)) {
            return true;
        }
        return false;
    }

    // effects: returns false if position is in npc x range
    public boolean collideY(int y, NonPlayableCharacter npc) {
        if (y >= npc.getPosY() && y <= (npc.getPosY() + 30)) {
            return true;
        }
        return false;
    }

    // effects: handles all rendering in game window
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g2.drawString("Bullets: " + player.getBullets(), 100, 30);
        g2.drawString("Score: " + score, 250, 30);

        renderBullets(g);
        renderPlayer(g, player);
        renderEnemies(g);

        if (gameState == 1) {
            g2.drawString("Resume Game: P", 350,228);
            g2.drawString("Save Game: S", 350,258);
            g2.drawString("Load Game: A", 350, 288);
        }

        g2.dispose();

    }

    // EFFECTS: saves the GameSpace to file
    private void saveGameSpace() {
        gameSpace.addPlayer(player);
        gameSpace.setScore(this.score);
        for (NonPlayableCharacter enemy : enemies) {
            gameSpace.addNpc(enemy);
        }
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
            enemies = gameSpace.getNpcs();
            player = gameSpace.getPlayers().get(0);
            score = gameSpace.getScore();
            System.out.println("Loaded " + gameSpace.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // effects: renders a single bullet
    public void renderBullet(Graphics g, Bullet b) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.BLUE);
        g2.fillOval(b.getPosX(),b.getPosY(),10,10);
    }

    // effects: renders a list of bullets
    public void renderBullets(Graphics g) {
        for (Bullet bullet: player.getBulletList()) {
            renderBullet(g,bullet);
        }
    }

    // effects: renders the player
    public void renderPlayer(Graphics g,Player p) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.GREEN);
        g2.fillRect(p.getPosX(), p.getPosY(), 30, 30);
    }

    // effects: renders an npc
    public void renderNPC(Graphics g, NonPlayableCharacter npc) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.ORANGE);
        g2.fillRect(npc.getPosX(), npc.getPosY(), 30, 30);
    }

    // modifies: this
    // effects: handles player movement
    public void controlPlayer(KeyHandler k,Player player) {
        if (k.isLeftPressed()) {
            player.movePlayer("left");
        } else if (k.isRightPressed()) {
            player.movePlayer("right");
        } else if (k.isUpPressed()) {
            player.movePlayer("up");
        } else if (k.isDownPressed()) {
            player.movePlayer("down");
        } else if (k.isSpacePressed()) {
            if (player.getBulletList().size() > 0) {
                player.getBulletList().get(0).getShot();
            }
        } else if (k.isReloadPressed()) {
            player.reload();
        }
    }

    // modifies: this
    // effects: updates player
    public void updatePlayer(KeyHandler k, Player player) {
        controlPlayer(k,player);
        player.updateBullets();
    }
}
