import java.util.Scanner;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import Exceptions.AreaTooSmallException;
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
    private static final String SCORE_CARD_LAYOUT = "%s%s%s has won %d games.";
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
                    case REGISTERED_PLAYERS -> printRegisteredPlayers(gm);
                    case SCORE_CARD -> printScoreCard(gm);
                    case EXIT ->  running = false;
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
            if (!gm.hasPlayers())
                throw new NoRegisteredPlayersException();
            
            System.out.print("Insert Player1's name: ");
            String player1 = in.nextLine().trim();

            System.out.print("Insert Player2's name: ");
            String player2 = in.nextLine().trim();
            
            int nChips = getNChips(in);
            
            System.out.print("Please enter the number of lines, followed by the number of columns for the game: ");
            int nLines = in.nextInt();
            int nColumns = in.nextInt();
            
            if (nLines <= nChips && nColumns <= nChips)
            throw new AreaTooSmallException();
            
            Game g = gm.newGame(nLines, nColumns, nChips, player1, player2);
            runGame(g, in);

            if (g.isOver())
                gm.removeGame(g.getGameId());
                
            
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    
    private static void loadSavedGame(Scanner in, GameManager gm) {
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
            runGame(g, in);
            
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
    
    private static void printRegisteredPlayers(GameManager gm) {
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

    private static void runGame(Game g, Scanner in) {
        int move = 0;
        while (!g.isOver()) {
            g.printBoard();
            try {
                Player currentPlayer = g.currentPlayer();
                System.out.printf(NEXT_MOVE, currentPlayer.getColour(), currentPlayer.getName(), Colour.WHITE.getCode());
                move = in.nextInt();

                if (move == 0) {
                    System.out.println(THX_PLAYING); //TODO: ask if users want to save the game
                    break;
                }

                g.nextMove(move);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (!g.tied() && move != 0) {
            Player winner = g.winner();
            System.out.printf(WINNER, winner.getColour(), winner.getName(), Colour.WHITE.getCode());
        }

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
