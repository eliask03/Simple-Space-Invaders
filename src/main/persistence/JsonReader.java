package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import model.Bullet;
import model.GameSpace;
import model.NonPlayableCharacter;
import model.Player;
import org.json.*;

// Represents a reader that reads GameSpace from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads GameSpace from file and returns it;
    // throws IOException if an error occurs reading data from file
    public GameSpace read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGameSpace(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses GameSpace from JSON object and returns it
    private GameSpace parseGameSpace(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        GameSpace gs = new GameSpace(name);
        int score = jsonObject.getInt("score");
        gs.setScore(score);
        addCharacters(gs, jsonObject);
        return gs;
    }

    // MODIFIES: gs
    // EFFECTS: parses player and npc from JSON object and adds them to GameSpace
    private void addCharacters(GameSpace gs, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("players");
        JSONArray jsonArray1 = jsonObject.getJSONArray("NPCs");
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            addPlayer(gs, nextThingy);
        }
        for (Object json : jsonArray1) {
            JSONObject nextThingy = (JSONObject) json;
            addNPC(gs, nextThingy);
        }

    }

    // MODIFIES: gs
    // EFFECTS: parses player from JSON object and adds it to GameSpace
    private void addPlayer(GameSpace gs, JSONObject jsonObject) {
        int health = jsonObject.getInt("health");
        int damage = jsonObject.getInt("damage");
        int level = jsonObject.getInt("level");
        int exp = jsonObject.getInt("exp");
        JSONArray jsonArray = jsonObject.getJSONArray("bullets");
        ArrayList<Bullet> bullets = new ArrayList<>();
        int posX = jsonObject.getInt("posX");
        int posY = jsonObject.getInt("posY");
        for (Object json : jsonArray) {
            JSONObject nextObject = (JSONObject) json;
            int x = nextObject.getInt("xpos");
            int y = nextObject.getInt("ypos");
            bullets.add(new Bullet(x,y));
        }

        Player player = new Player(damage, health, level, exp, bullets,posX,posY);
        gs.addPlayer(player);
    }

    // MODIFIES: gs
    // EFFECTS: parses npc from JSON object and adds it to GameSpace
    private void addNPC(GameSpace gs, JSONObject jsonObject) {
        int health = jsonObject.getInt("health");
        int damage = jsonObject.getInt("damage");
        int posX = jsonObject.getInt("posX");
        int posY = jsonObject.getInt("posY");

        NonPlayableCharacter npc = new NonPlayableCharacter(health, damage,posX,posY);
        gs.addNpc(npc);
    }
}