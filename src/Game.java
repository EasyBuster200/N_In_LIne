import java.io.Serializable;
import Exceptions.OutOfBoundsException;

public interface Game extends Serializable {
    
    /**
     * @return true if the game is over, false otherwise
     */
    boolean isOver();

    /**
     * @return player that won the game
     */
    Player winner();

    /**
     * @return true if there was a tie, false otherwise
     */
    boolean tied();

    /**
     * Prints out the current state of the games board
     */
    void printBoard();

    /**
     * @return Player1
     */
    Player getP1();

    /**
     * @return Player2
     */
    Player getP2();

    /**
     * Recieves the positions of the players next move
     * @param pos >= 0 
     * @throws OutOfBoundsException if pos is bigger than the number of collumns in the game
     * @throws GameEndedException if the player/s decide to end the game
     */
    void nextMove(int pos) throws OutOfBoundsException, GameEndedException;
}
