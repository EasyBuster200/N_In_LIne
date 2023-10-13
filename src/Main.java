import java.util.Scanner;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import Exceptions.AreaTooSmallException;

public class Main {
    //Constants
    private static final String SAVE_FILE = "data.txt";

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
        interperter();
    }

    private static void interperter() {
        Scanner in = new Scanner(System.in);
        GameManager gm = getSaveFile();;
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
                    case EXIT ->  running = false;
                    //TODO: Add a case to print out registered players (name(in their colour), nbr wins)
                }
            } catch (InputMismatchException e) {
                in.nextLine();
                System.out.print(INPUT_ERROR);
            }

        }
        saveProgress(gm);
        in.close();
    }

    
    
    private static void startNewGame(Scanner in, GameManager gm) { 
        try {

            System.out.println("Insert Player1's name: ");
            String player1 = in.nextLine().trim();

            System.out.println("Insert Player2's name: ");
            String player2 = in.nextLine().trim();
            
            int nChips = getNChips(in);
            
            System.out.print("Please enter the number of lines, followed by the number of columns for the game: ");
            int nLines = in.nextInt();
            int nColumns = in.nextInt();
            
            if (nLines <= nChips && nColumns <= nChips)
                throw new AreaTooSmallException();
            
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
            Colour playerColour = getColour(in.next());
            String playerName = in.nextLine().trim();
            
            gm.registerUser(playerName, playerColour);

            System.out.println("Player: " + playerColour.getCode() + playerName + Colour.WHITE.getCode() + " was registered successfully!");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void printScoreCard(GameManager gm) {
       //TODO: I should have a seperate data file just for the scoreCard, which will save the top 5 players with most wins from all time.
       /*
        * The file would be read each time the application is started,
        * When a person wins a game, their new number of wins will be compared with the people on the scoreCard, from bottom to top
        * If the recent winner now belongs in the score card he/she will replace whoever was there before,
        * THis will cause all players that had less wins then that player to be bumped down one spot.
        */
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

    private static GameManager getSaveFile() {
        GameManager gm = new GameManagerClass();

        try (FileInputStream fi = new FileInputStream(SAVE_FILE);
            ObjectInputStream inStream = new ObjectInputStream(fi)) {
            
            gm =(GameManager) inStream.readObject();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return gm;
    }

    private static void saveProgress(GameManager gm) {
        try (FileOutputStream fo = new FileOutputStream(SAVE_FILE);
            ObjectOutputStream outStream = new ObjectOutputStream(fo)) {

            outStream.writeObject(gm);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
