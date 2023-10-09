import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import Exceptions.AreaTooSmallException;

public class Main {
    //Constants
    private static final String PLAYERS_FILE = "players_data.txt";

    //Menu
    private static final String TITLE = "    N em Linha \n";
    private static final String MENU = "\nAvailable Commands: \n 1 - Start New Game \n 2 - Load Game \n 3 - Setup New Player \n 4 - Score Card \n 5 - Exit\n\n-";
    private static final String INPUT_ERROR = "\nInput must be one of the available commands\n";
    private static final String PLAYER_SETPUT = "\t\t  Player Setup\n\tPlayer choose a colour and name:\n (%s)\n\n-";
    private static final String SAVED_GAMES_MENU = "\tSaved Games:\n";
    private static final String SAVED_GAME = "%d- %S vs %s";
    private static final String SET_BOLD_TEXT = "\033[0;1m";
    private static final String PLAYER_COLOURS = " " + Colour.getColours();

    //Commands
    private static final int START_NEW_GAME = 1;
    private static final int LOAD_GAME = 2;
    private static final int NEW_PLAYER = 3;
    private static final int SCORE_CARD = 4;
    private static final int EXIT = 5;
    
    //Variables
    
    public static void main(String[] args) throws Exception {
        //TODO: Missing method to read save file
        interperter();
        //TODO: Missing method to create/update the save file
    }

    private static void interperter() {
        Scanner in = new Scanner(System.in);
        GameManager gm = new GameManagerClass();
        boolean running = true;
        System.out.print(TITLE);

        while (running) {
            System.out.print(MENU);

            int cmd; 
            
            try {
                cmd = in.nextInt(); in.nextLine();
                
                switch (cmd) {
                    case START_NEW_GAME -> startNewGame(in, gm);
                    case LOAD_GAME -> loadSavedGame(in, gm);
                    case NEW_PLAYER -> registerNewPlayer(in, gm);
                    case SCORE_CARD -> printScoreCard(gm);
                    case EXIT ->  running = false; //TODO: Make a function that prints the exit message while at the same time saving any information needed to files (Maybe ask if the current game is to be saved, overwrittting the previous save)

                }
            } catch (InputMismatchException e) {
                in.nextLine();
                System.out.print(INPUT_ERROR);
            }

        }

        in.close();
    }

    
    private static void startNewGame(Scanner in, GameManager gm) { 
        try {
            System.out.print("Please enter the number of lines, followed by the number of columns for the game: ");
            int nLines = in.nextInt();
            int nColumns = in.nextInt();
            int nChips = getNChips(in);
            
            if (nLines <= nChips && nColumns <= nChips)
            throw new AreaTooSmallException();
            
            System.out.println("Insert Player1's name: ");
            String player1 = in.nextLine().trim();

            System.out.println("Insert Player2's name: ");
            String player2 = in.nextLine().trim();

            gm.newGame(nLines, nColumns, nChips, player1, player2);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    private static void loadSavedGame(Scanner in, GameManager gm) {
        try {
            Iterator<Game> gameIt = gm.savedGames();
            System.out.print(SAVED_GAMES_MENU);
            int current = 1;
            
            while (gameIt.hasNext()) {
                Game game = gameIt.next();
                Player P1 = game.getP1();
                Player P2 = game.getP2();
                
                System.out.printf(SAVED_GAME, current, P1.getName(), P2.getName());
                current++;
            }

            System.out.print("Select a game: ");
            int gameNumber = in.nextInt(); in.nextLine();
            
            gm.runGame(gameNumber);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    private static void registerNewPlayer(Scanner in, GameManager gm) {
        try {
            System.out.printf(PLAYER_SETPUT, PLAYER_COLOURS);
            Colour plaeyColour = getColour(in.next());
            String playerName = in.nextLine().trim();

            gm.registerUser(playerName, plaeyColour);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void printScoreCard(GameManager gm) {
        try {
            Iterator<Player> it = gm.getScoreCard();
        }
    }

    private static Colour getColour(String chosenColour) throws NoSuchColourException {
        for (Colour colour : Colour.values()) {
            if (chosenColour.toLowerCase().equals(colour.toString()))
            return colour;
        }

        throw new NoSuchColourException();
    }

    private static int getNChips(Scanner in) {
        int nChips = 0;

        while (nChips < 2) {
            System.out.print("Please insert the number of chips in a line needed to win: ");
            nChips = in.nextInt(); in.nextLine();

            if (nChips < 2)
                System.out.printf(SET_BOLD_TEXT + "Error: Number too small!!\n\n" + Colour.WHITE.getCode());

        }

        return nChips;
    }

    private static void runGame(GameSystem game, Scanner in) {
        while (!game.isGameOver()) {
            game.printBoard();
            game.nextMove(in); //TODO: change to make all prints be on main
        }

        Player winner = game.getWinner();

        if (winner != null) {
            System.out.println("Player: " + winner.getName() + " has won the game with " + winner.getMovesMade() + " moves");
        } else if (game.gameTied())
            System.out.println("There are no more free positions, game tied.");
            System.out.println("Thanks for playing!!");

    }

    private static void doNothing() {}
}

//TODO!: Fix GamesystemClass updating it to use the player class.
//TODO: if the whole board has been field end the game on a tie.
//TODO: Maybe when the game is saved ask the names of player 1 and 2, in order to be eaiser to start the game back up ltr on.