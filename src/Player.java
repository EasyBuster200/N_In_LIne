import java.io.Serializable;

public class Player implements Serializable {

    private int movesMade, gamesWon;
    private Colour colour;
    private final String name;

    public Player(String name, Colour colour, int gamesWon, int moveMade) { //Pre existing player, with a set number of games won
        this.name = name;
        this.colour = colour;
        movesMade = moveMade; //For saved games I may have to also keep the movesMade variable
        this.gamesWon = gamesWon;
    }

    public Player(String name, Colour colour) { //New Player with no games won or movesMade
        this(name,colour, 0, 0);
    }

    public String getName() {return name;}

    public String getColour() {return colour.getCode();}
    
    public void setColour(Colour colour) {this.colour = colour;}

    public int getGamesWon() {return gamesWon;}

    public int getMovesMade() {return movesMade;}

    public void moveMade() {movesMade++;}

    public void resetMovesMade() {movesMade = 0;}

}
