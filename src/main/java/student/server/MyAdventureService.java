package student.server;

import student.adventure.AdventureGame;
import student.adventure.AdvetureGameAPI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class MyAdventureService implements AdventureService{
    private static AdventureService service = new MyAdventureService();
    private int gameID;
    private Map<Integer, AdvetureGameAPI> currGamesTracker;
    //private AdvetureGameAPI gameEngine;
    private GameStatus statusGame;
    //AdvetureGameAPI.deserialize("src/main/resources/adventureMap.json");

    @Override
    public void reset() {
        gameID = 0;
        currGamesTracker.clear();;
    }

    @Override
    public int newGame() throws AdventureException {
        AdvetureGameAPI gameEngine = new AdvetureGameAPI();
        currGamesTracker.put(gameID, gameEngine);
        gameID++;
        return gameID-1 ;

    }

    @Override
    public GameStatus getGame(int id) {
       return currGamesTracker.get(id).getStatusOfGame();
    }

    @Override
    public boolean destroyGame(int id) {
        if (currGamesTracker.containsKey(id)) {
            currGamesTracker.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public void executeCommand(int id, Command command) {
        AdvetureGameAPI currentGame = currGamesTracker.get(id);
        currentGame.setNewGameId(id);
        currentGame.executeCommands(command);
    }

    @Override
    public SortedMap<String, Integer> fetchLeaderboard() {
        return null;
    }
}
