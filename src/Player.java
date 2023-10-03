public class Player {
    private int movesMade, gamesWon;
    private Colour colour;
    private final String name;

    public Player(String name, Colour colour, int gamesWon) { //Pre existing player, with a set number of games won
        this.name = name;
        this.colour = colour;
        movesMade = 0;
        this.gamesWon = gamesWon;
    }

    public Player(String name, Colour colour) { //New Player with no games won
        this(name,colour, 0);
    }
}
