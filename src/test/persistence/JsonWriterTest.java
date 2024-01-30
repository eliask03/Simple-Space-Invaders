package persistence;

import model.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            GameSpace gs = new GameSpace("Save State");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterGameSpace1() {
        try {
            GameSpace gs = new GameSpace("Save State");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(gs);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            gs = reader.read();
            assertEquals("Save State", gs.getName());
            assertEquals(0, gs.getPlayers().size());
            assertEquals(0, gs.getNpcs().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGameSpace2() {
        try {
            GameSpace gs = new GameSpace("Save State");
            Player player = new Player(100,100,100,100,new ArrayList<>(),100,100);
            player.reload();
            NonPlayableCharacter npc = new NonPlayableCharacter(100,100,0,0);
            gs.addPlayer(player);
            gs.addNpc(npc);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(gs);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            gs = reader.read();
            assertEquals("Save State", gs.getName());
            List<Player> players = gs.getPlayers();
            assertEquals(1, players.size());
            List<NonPlayableCharacter> npcs = gs.getNpcs();
            assertEquals(1, npcs.size());
            assertEquals(player.getHealth(), players.get(0).getHealth());
            assertEquals(npc.getHealth(), npcs.get(0).getHealth());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}