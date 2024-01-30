package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

public class GameSpace implements Writable {

    private List<Player> players;
    private List<NonPlayableCharacter> npcs;
    private String name;
    private int score;



    public GameSpace(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.npcs = new ArrayList<>();
        this.score = 0;
    }

    // modifies: this
    // effects: sets the score
    public void setScore(int score) {
        this.score = score;
    }

    // modifies: this
    // effects: adds player to player list
    public void addPlayer(Player player) {
        players.add(player);
    }

    // modifies: this
    // effects: adds npc to npc list
    public void addNpc(NonPlayableCharacter npc) {
        npcs.add(npc);
    }

    // effects: writes GameSpace as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("players", playersToJson());
        json.put("NPCs", npcsToJson());
        json.put("score", score);

        return json;
    }

    private JSONArray playersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Player p : players) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }

    private JSONArray npcsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (NonPlayableCharacter npc : npcs) {
            jsonArray.put(npc.toJson());
        }

        return jsonArray;
    }

    // effects: returns the name
    public String getName() {
        return name;
    }

    // effects: returns the players
    public List<Player> getPlayers() {
        return players;
    }

    // effects: returns the npc list
    public ArrayList<NonPlayableCharacter> getNpcs() {
        return (ArrayList<NonPlayableCharacter>) npcs;
    }

    // effects: returns the score
    public int getScore() {
        return score;
    }
}
