package Game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.UUID;
import Exceptions.NameAlreadyResgisteredException;
import Exceptions.NoRegisteredPlayersException;
import Exceptions.NoSavedGamesException;
import Exceptions.NoSuchGameException;
import Exceptions.NoSuchPlayerException;

/**
 * Implementation of the interface GameManager
 */
public class GameManagerClass implements GameManager {

    //Variables
    private TreeMap<String, Player> players; //Player name --> Player object
    private HashMap<UUID, Game> games; //Game ID --> Game object

    public GameManagerClass() {
        players = new TreeMap<>();
        games = new HashMap<>();
    }

    @Override
    public void registerUser(String name, Colour Colour) throws NameAlreadyResgisteredException {
        if (players.keySet().contains(name))
            throw new NameAlreadyResgisteredException();

        players.put(name, new PlayerClass(name, Colour));
    }

    @Override
    public Game newGame(int nLines, int nColumns, int nChips, String name1, String name2) throws NoSuchPlayerException, NoRegisteredPlayersException {
        Player P1 = getPlayer(name1);
        Player P2 = getPlayer(name2);

        GameClass newGame = new GameClass(nLines, nColumns, nChips, P1, P2);
        games.put(newGame.getGameId(), newGame);
        
        return newGame;
    }

    @Override
    public Game getGame(UUID gameID) throws NoSuchGameException {
        if (!games.keySet().contains(gameID))
            throw new NoSuchGameException();

        else 
            return games.get(gameID);

        }
        
    @Override
    public Iterator<Game> savedGames() throws NoSavedGamesException {
        if(games.isEmpty())
            throw new NoSavedGamesException();

            return games.values().iterator();
    }

    @Override
    public Iterator<Player> getScoreCard() throws NoRegisteredPlayersException { 
        if(players.isEmpty())
            throw new NoRegisteredPlayersException();

        return players.values().iterator();
    }

    @Override
    public Iterator<Player> getPlayersIterator() throws NoRegisteredPlayersException {
        if(players.isEmpty())
            throw new NoRegisteredPlayersException();

        return players.values().iterator();
    }

    @Override
    public boolean hasPlayers() {
        return !players.isEmpty();
    }

    @Override
    public void removeGame(UUID gameId) {
        games.remove(gameId); 
    }  

    /**
     * Returns the player object from the name of the player
     * @param name players name
     * @return player object corresponding to the given name
     * @throws NoSuchPlayerException when there are no players registered with the given name 
     * @throws NoRegisteredPlayersException when there are no registered players in the system
     */
    private Player getPlayer(String name) throws NoSuchPlayerException, NoRegisteredPlayersException {
        if(players.isEmpty())
            throw new NoRegisteredPlayersException();

        if(!players.containsKey(name))    
            throw new NoSuchPlayerException(name);

        return players.get(name);    
    }

}
