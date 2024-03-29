package Game;

import java.io.Serializable;
import java.util.Iterator;
import java.util.UUID;
import Exceptions.NameAlreadyResgisteredException;
import Exceptions.NoRegisteredPlayersException;
import Exceptions.NoSavedGamesException;
import Exceptions.NoSuchGameException;
import Exceptions.NoSuchPlayerException;
/**
 * @author EasyBuster (Duarte Coelho)
 * Interface for a class responsible for managing and running all the games / players registered in the system
 */
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
     * @return 
     * @throws NoSuchPlayerException if any of the given player names isn't registered
     * @throws NoRegisteredPlayersException if there are no registered players
     */
    Game newGame(int nLines, int nColumns, int nChips, String P1, String P2) throws NoSuchPlayerException, NoRegisteredPlayersException;

    /**
     * Runs the game with the given number, until either a player wins or theres a tie
     * @param gameNumber >= 0
     * @return null if the game tied, the winner otherwise
     * @throws NoSuchGameException if there is no game resgistered under the given number
     */
    Game getGame(UUID gameId) throws NoSuchGameException;

    /**
     * @return an Iterator with all the saved games
     * @throws NoSavedGamesException if there are no games saved
     */
    Iterator<Game> savedGames() throws NoSavedGamesException;

    /**
     * @return an Iterator with the players ordered by their number of wins, by order of insertion otherwise
     * @throws NoRegisteredPlayersException if there are no registeres players
     */
    Iterator<Player> getScoreCard() throws NoRegisteredPlayersException;

    /**
     * @return Iterator with all the players in the system
     * @throws NoRegisteredPlayersException when there are no registered players in the system
     */
    Iterator<Player> getPlayersIterator() throws NoRegisteredPlayersException;

    /**
     * Checks if there are any registered players in the system
     * @return <code> true <code> if there are registered players
     * <code> false <code> otherwise.
     */
    boolean hasPlayers();

    /**
     * Removes the game with the given ID
     * @param gameId ID of the game to be removed
     */
    void removeGame(UUID gameId);
}
