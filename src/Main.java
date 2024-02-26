import java.util.Scanner;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import Exceptions.AreaTooSmallException;
import Exceptions.GameStoppedException;
import Exceptions.NoSuchColourException;
import Exceptions.NoRegisteredPlayersException;
import Game.Colour;
import Game.Game;
import Game.GameManager;
import Game.GameManagerClass;
import Game.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Main {
    //Constants
    private static final String SAVE_FILE = "data.txt";

    //Menu
    private static final String TITLE = "    N em Linha \n";
    private static final String MENU = "\nAvailable Commands: \n 1 - Start New Game \n 2 - Load Game \n 3 - Setup New Player \n 4 - Registered Players \n 5 - Score Card \n 6 - Exit\n\n-";
    private static final String INPUT_ERROR = "\nInput must be one of the available commands\n";
    private static final String PLAYER_SETPUT = "\t\t  Player Setup\n\tPlayer choose a colour and name:\n (%s)\n\n-";
    private static final String SAVED_GAMES_MENU = "\tSaved Games:\n";
    private static final String SAVED_GAME = "%d- %s vs %s\n";
    private static final String NEXT_MOVE = "%s%s%s make your next move (0 to exit): ";
    private static final String WINNER = "%s%s%s was the winner, congrats!!!\n";
    private static final String SCORE_CARD_LAYOUT = "%s%s%s has won %d games.\n";
    private static final String SET_BOLD_TEXT = "\033[0;1m";
    private static final String THX_PLAYING = "Thank you for playing";
    private static final String PLAYER_COLOURS = " " + Colour.getColours();

    //Commands
    private static final int START_NEW_GAME = 1;
    private static final int LOAD_GAME = 2;
    private static final int NEW_PLAYER = 3;
    private static final int REGISTERED_PLAYERS = 4;
    private static final int SCORE_CARD = 5;
    private static final int EXIT = 6;
    
    //Variables
    private static GameManager gm;
    private static Scanner in;
    
    public static void main(String[] args) throws Exception {
        interperter();
    }

    /**
     * Method responsible for reading and interperting the users input.
     */
    private static void interperter() {
        in = new Scanner(System.in);
        gm = getSaveFile();;
        boolean running = true;
        System.out.print(TITLE);

        while (running) {
            System.out.print(MENU);

            int cmd; 
            
            try {
                cmd = in.nextInt(); in.nextLine();
                
                switch (cmd) {
                    case START_NEW_GAME -> startNewGame();
                    case LOAD_GAME -> loadSavedGame();
                    case NEW_PLAYER -> registerNewPlayer();
                    case REGISTERED_PLAYERS -> printRegisteredPlayers();
                    case SCORE_CARD -> printScoreCard();
                    case EXIT ->  running = false;
                }
            } catch (InputMismatchException e) {
                in.nextLine();
                System.out.print(INPUT_ERROR);
            }

        }
        saveProgress();
        in.close();
    }

    
    /**
     * Method called when the user decides to start a new game.
     * Method creates and registeres a new game with the information given by the user
     */
    private static void startNewGame() { 
        try {
            if (!gm.hasPlayers())
                throw new NoRegisteredPlayersException();
            
            System.out.print("Insert Player1's name: ");
            String player1 = in.nextLine().trim();

            System.out.print("Insert Player2's name: ");
            String player2 = in.nextLine().trim();
            
            int nChips = getNChips();
            
            System.out.print("Please enter the number of lines, followed by the number of columns for the game: ");
            int nLines = in.nextInt();
            int nColumns = in.nextInt();
            
            if (nLines <= nChips && nColumns <= nChips)
            throw new AreaTooSmallException();
            
            Game g = gm.newGame(nLines, nColumns, nChips, player1, player2);
            runGame(g);

            if (g.isOver())
                gm.removeGame(g.getGameId());
                
            
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    
    /**
     * Method to load a saved game from memory.
     */
    private static void loadSavedGame() {
        try {
            Iterator<Game> gameIt = gm.savedGames();
            List<UUID> gameIDs = new ArrayList<UUID> ();
            System.out.print(SAVED_GAMES_MENU);
            int current = 1;
            
            while (gameIt.hasNext()) {
                Game game = gameIt.next();
                Player P1 = game.getP1();
                Player P2 = game.getP2();
                gameIDs.add(game.getGameId());
                
                System.out.printf(SAVED_GAME, current, P1.getName(), P2.getName());
                current++;
            }
            
            System.out.print("Select a game: ");
            int gameNumber = in.nextInt(); in.nextLine();
            
            Game g = gm.getGame(gameIDs.get(gameNumber - 1));
            runGame(g);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    /**
     * Method to register a new player to the system
     */
    private static void registerNewPlayer() {
        try {
            System.out.printf(PLAYER_SETPUT, PLAYER_COLOURS);
            Colour playerColour = getColour(in.next());
            String playerName = in.nextLine().trim();
            
            gm.registerUser(playerName, playerColour);
            
            System.out.println("Player: " + playerColour.getCode() + playerName + Colour.WHITE.getCode() + " was registered successfully!");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Method responsible for printing the current scorecard
     */
    private static void printScoreCard() {
        try {
            Iterator<Player> it = gm.getScoreCard();
            int i = 0;
            
            while (it.hasNext() && i < 4) {
                Player next = it.next();
                
                System.out.printf(SCORE_CARD_LAYOUT, next.getColour(), next.getName(), Colour.WHITE.getCode(), next.getGamesWon());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Method to get the Colour object correspondant to the String given
     * @param chosenColour String format of the COlour we are looking for 
     * @return Colour object we are looking for
     * @throws NoSuchColourException when there is no colour of the given String
     */
    private static Colour getColour(String chosenColour) throws NoSuchColourException {
        for (Colour colour : Colour.values()) {
            if (chosenColour.toLowerCase().equals(colour.toString()))
            return colour;
        }
        
        throw new NoSuchColourException();
    }
    
    /**
     * Auxilary method to get the number of chips in a row needed to win the Game 
     * @return number of chips needed in a row to win the game
     */
    private static int getNChips() {
        int nChips = 0;
        
        while (nChips < 2) {
            System.out.print("Please insert the number of chips in a line needed to win: ");
            nChips = in.nextInt(); in.nextLine();
            
            if (nChips < 2)
            System.out.printf(SET_BOLD_TEXT + "Error: Number too small!!\n\n" + Colour.WHITE.getCode());
            
        }
        
        return nChips;
    }
    
    /**
     * Method to print out all the currently registered players
     */
    private static void printRegisteredPlayers() {
        try {
            Iterator<Player> it = gm.getPlayersIterator();
            
            while(it.hasNext()) {
                Player current = it.next();
                System.out.println(current.getColour() + current.getName() + Colour.WHITE.getCode());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to run a given Game until one of the users wins, or they decide to stop
     * @param g Game to be run
     */
    private static void runGame(Game g) {
        int move = 0;
        while (!g.isOver()) {
            g.printBoard();
            try {
                Player currentPlayer = g.currentPlayer();

                System.out.println("Input 0 to exit");
                System.out.printf(NEXT_MOVE, currentPlayer.getColour(), currentPlayer.getName(), Colour.WHITE.getCode());

                move = in.nextInt();
                g.nextMove(move);

            } catch (GameStoppedException e) {
                gameStopped(g);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (!g.tied() && move != 0) {
            Player winner = g.winner();
            System.out.printf(WINNER, winner.getColour(), winner.getName(), Colour.WHITE.getCode());
        }

    }
    
    /**
     * Method to read the save file of the GameManger Class
     * @return the GameManager object read from a file, or a new GameManger object if no such file was found
     */
    private static GameManager getSaveFile() {
        GameManager gm = new GameManagerClass();
        
        try (FileInputStream fi = new FileInputStream(SAVE_FILE);
        ObjectInputStream inStream = new ObjectInputStream(fi)) {
            
            gm =(GameManager) inStream.readObject();
            
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        
        return gm;
    }
    
    /**
     * Method to save the current state of the program to a File
     */
    private static void saveProgress() {
        try (FileOutputStream fo = new FileOutputStream(SAVE_FILE);
        ObjectOutputStream outStream = new ObjectOutputStream(fo)) {
            
            outStream.writeObject(gm);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Auxilary method to stop a game halfway through
     * @param g Game being stopped
     */
    private static void gameStopped(Game g) {
        System.out.print("Would you like to save the current game (Y/N): ");
        String answer = in.nextLine().strip();

        if (answer.equalsIgnoreCase("N")) {
            gm.removeGame(g.getGameId());
            System.out.println("Game successfully removed");

        }else
            System.out.println("Game successfully saved");

        System.out.println(THX_PLAYING);
    }
}

//TODO; Change order of printing the score card. Make it ordered by the number of wins
