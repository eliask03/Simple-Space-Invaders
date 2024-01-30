package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MyModelTest {
    Player player1;
    NonPlayableCharacter enemy1;
    GameSpace gameSpace;
    Bullet bullet;

    @BeforeEach
    void run() {
        player1 = new Player(25,100,1,0, new ArrayList<Bullet>(),100,100);
        enemy1 = new NonPlayableCharacter(200,25,100,100);
    }

    @Test
    void testNPC() {
        enemy1.takeDamage(50);
        assertEquals(150, enemy1.getHealth());
        assertEquals(25, enemy1.getDamage());
        assertEquals(100,enemy1.getPosX());
        assertEquals(100,enemy1.getPosY());
        enemy1.updateNPC();
        assertEquals(98,enemy1.getPosX());
        assertEquals(100,enemy1.getPosY());
    }

    @Test
    void testPlayer1() {
        assertFalse(player1.levelUp());
        player1.takeDamage(25);
        assertEquals(75, player1.getHealth());
        player1.addExp();
        player1.addExp();
        player1.addExp();
        player1.addExp();
        player1.addExp();
        player1.levelUp();
        assertEquals(2,player1.getLevel());
        assertEquals(50, player1.getDamage());
        assertEquals(0,player1.getExp());
        assertEquals(0,player1.getBullets());
        player1.reload();
        player1.reload();
        player1.reload();
        player1.useBullet();
        assertEquals(2,player1.getBullets());
        player1.useBullet();
        player1.useBullet();
        assertEquals(new ArrayList<Bullet>(), player1.getBulletList());
    }

    @Test
    void testPlayer2() {
        assertEquals(100,player1.getPosX());
        assertEquals(100,player1.getPosY());
        player1.movePlayer("left");
        assertEquals(92,player1.getPosX());
        player1.movePlayer("right");
        assertEquals(100,player1.getPosX());
        player1.movePlayer("up");
        assertEquals(92,player1.getPosY());
        player1.movePlayer("down");
        assertEquals(100,player1.getPosY());
        player1.movePlayer("bruh");
        assertEquals(100,player1.getPosX());
        assertEquals(100,player1.getPosY());
        player1.reload();
        player1.reload();
        player1.reload();
        player1.reload();
        player1.reload();
        player1.reload();
        player1.reload();
        player1.reload();
        player1.reload();
        assertEquals(8,player1.getBullets());
    }

    @Test
    void testGameSpace() {
        gameSpace = new GameSpace("bruh");
        assertEquals("bruh",gameSpace.getName());
        assertEquals(0,gameSpace.getPlayers().size());
        assertEquals(0,gameSpace.getNpcs().size());
        gameSpace.addPlayer(player1);
        gameSpace.addNpc(enemy1);
        gameSpace.setScore(5);
        assertEquals(1,gameSpace.getPlayers().size());
        assertEquals(1,gameSpace.getNpcs().size());
        assertEquals(5,gameSpace.getScore());
    }

    @Test
    void testBullet() {
        bullet = new Bullet(0,0);
        assertEquals(0,bullet.getPosX());
        assertEquals(bullet.getPosY(),0);
        bullet.updateBullet(0,0);
        assertEquals(0,bullet.getPosX());
        assertEquals(bullet.getPosY(),0);
        bullet.getShot();
        assertTrue(bullet.isShot());
        bullet.updateBullet(0,0);
        assertEquals(20,bullet.getPosX());
        assertEquals(bullet.getPosY(),0);
    }
}