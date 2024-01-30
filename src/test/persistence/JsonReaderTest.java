package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import ui.Game;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            GameSpace gs = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyGameSpace() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWorkRoom.json");
        try {
            GameSpace gs = reader.read();
            assertEquals("Save State", gs.getName());
            assertEquals(0, gs.getPlayers().size());
            assertEquals(0, gs.getNpcs().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralGameSpace() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWorkRoom.json");
        try {
            GameSpace gs = reader.read();
            assertEquals("Save State", gs.getName());
            List<Player> players = gs.getPlayers();
            List<NonPlayableCharacter> npcs = gs.getNpcs();
            assertEquals(1, players.size());
            assertEquals(1, npcs.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}