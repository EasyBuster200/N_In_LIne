import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class GameManagerClass implements GameManager {

    private TreeMap<String, Player> players; //Player name --> Player object
    private HashMap<Integer, Game> games; //Game number --> Game object

    public GameManagerClass() {
        players = new TreeMap<>();
        games = new HashMap<>();
    }

    @Override
    public void registerUser(String name, Colour Colour) throws NameAlreadyResgisteredException {
        if (players.keySet().contains(name))
            throw new NameAlreadyResgisteredException();

        players.put(name, new Player(name, Colour));
    }

    @Override
    public void newGame(int nLines, int nColumns, int nChips, String name1, String name2) throws NoSuchPlayerException, NoRegisteredPlayersException {
        Player P1 = getPlayer(name1);
        Player P2 = getPlayer(name2);

        games.put(games.size() + 1, new GameClass(nLines, nColumns, nChips ,P1, P2));
        
    }

    @Override
    public Player runGame(int gameNumber) throws NoSuchGameException {
        if (!games.keySet().contains(gameNumber))
            throw new NoSuchGameException();

        Game game = games.get(gameNumber);

        while(!game.isOver()) 
            game.printBoard();

        if(game.tied())
            return null;
        else
            return game.winner();
        }
        
    @Override
    public Iterator<Game> savedGames() throws NoSavedGamesException {
        if(games.isEmpty())
            throw new NoSavedGamesException();

            return games.values().iterator();
    }

    @Override
    public Iterator<Player> getScoreCard() throws NoRegisteredPlayersException {
        Player[] orderedPlayers = new Player[players.size()];
        //TODO
        return null;
    }

    private Player getPlayer(String name) throws NoSuchPlayerException, NoRegisteredPlayersException {
        if(players.isEmpty())
            throw new NoRegisteredPlayersException();

        if(!players.containsKey(name))
            throw new NoSuchPlayerException(name);

        return players.get(name);
    }

    
}
