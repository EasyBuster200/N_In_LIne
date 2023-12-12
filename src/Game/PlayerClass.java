package Game;
public class PlayerClass implements Player {

    private int movesMade, gamesWon;
    private Colour colour;
    private final String name;

    public PlayerClass(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
        movesMade = 0; 
        this.gamesWon = 0;
    }

    @Override
    public String getName() {return name;}

    @Override
    public String getColour() {return colour.getCode();}
    
    @Override
    public void setColour(Colour colour) {this.colour = colour;}

    @Override
    public int getGamesWon() {return gamesWon;}

    @Override
    public int getMovesMade() {return movesMade;}

    @Override
    public void moveMade() {movesMade++;}

    @Override
    public void resetMovesMade() {movesMade = 0;}

    @Override
    public void wonGame() {gamesWon++;}

    @Override
    public int compareTo(Player p2) {
        if (gamesWon < p2.getGamesWon()) 
            return -1;

        else if (gamesWon > p2.getGamesWon())
            return 1;

        else
            return name.compareTo(p2.getName());
    }

}
