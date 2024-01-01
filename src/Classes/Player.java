package Classes;
import java.io.Serializable;

public interface Player extends Comparable<Player>, Serializable {

    /**
     * @return a String with the players name
     */
    String getName();

    /**
     * @return a String with the color code for the players colour
     */
    String getColour();

    /**
     * Changes the users current colour to colour
     * @param colour !=null
     */
    void setColour(Colour colour);

    /**
     * @return the number of games the user has won
     */
    int getGamesWon();

    /**
     * @return the number of moves made by the user
     */
    int getMovesMade();

    /**
     * Increments the moves made by user by 1
     */
    void moveMade();

    /**
     * Sets the number of moves made by the user back to 0
     */
    void resetMovesMade();

    /**
     * Increments the number of games won by the player by 1
     */
    void wonGame();
}