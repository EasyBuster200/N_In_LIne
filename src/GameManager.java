import java.io.Serializable;
import java.util.Iterator;

public interface GameManager extends Serializable {

    /**
     * Creates a new player object with the given name and colour
     * @param name != null
     * @param Colour != null
     * @throws NameAlreadyResgisteredException if the given name is already registered
     */
    void registerUser(String name, Colour Colour) throws NameAlreadyResgisteredException;

    /**
     * Creates a new game with a board of size nLines x nColumns, a nChips number of chips in a line needed to win,
     * and P1 + P2 as the players.
     * @param nLines >= nchips
     * @param nColumns >= nchips
     * @param nChips >= 2
     * @param P1 != null
     * @param P2 != null
     * @throws NoSuchPlayerException if any of the given player names isn't registered
     * @throws NoRegisteredPlayersException if there are no registered players
     */
    void newGame(int nLines, int nColumns, int nChips, String P1, String P2) throws NoSuchPlayerException, NoRegisteredPlayersException;

    /**
     * Runs the game with the given number, until either a player wins or theres a tie
     * @param gameNumber >= 0
     * @return null if the game tied, the winner otherwise
     * @throws NoSuchGameException if there is no game resgistered under the given number
     */
    Player runGame(int gameNumber) throws NoSuchGameException;

    /**
     * @return an Iterator with all the saved games
     * @throws NoSavedGamesException if there are no games saved
     */
    Iterator<Game> savedGames() throws NoSavedGamesException;

    /**
     * @return an Iterator with the players ordered by their number of wins
     * @throws NoRegisteredPlayersException if there are no registeres players
     */
    Iterator<Player> getScoreCard() throws NoRegisteredPlayersException;
}
