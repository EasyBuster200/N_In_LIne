import java.awt.Color;
import java.util.InputMismatchException;
import java.util.Scanner;

import Exceptions.AreaTooSmallException;

public class Main {
    //Menu
    private static final String TITLE = "    N em Linha \n";
    private static final String MENU = "\nAvailable Commands: \n 1 - Start New Game \n 2 - Load Game \n 3 - Setup New Player \n 4 - Score Card \n 5 - Exit\n\n";
    private static final String INPUT_ERROR = "\nInput must be one of the available commands\n";
    private static final String PLAYER_SETPUT = "\t\tPlayer Setup\n\tPlayer choose a name and colour:\n (%s)\n";
    public static final String SET_PLAIN_TEXT = "\033[0;0m";
    public static final String SET_BOLD_TEXT = "\033[0;1m";

    //Commands
    private static final int DEFAULT_SIZE = 50;
    private static final int START_NEW_GAME = 1;
    private static final int LOAD_GAME = 2;
    private static final int NEW_PLAYER = 3;
    private static final int SCORE_CARD = 4;
    private static final int EXIT = 5;
    private static int nbrPlayers;
    private static Player[] players;

    //Variables
    
    public static void main(String[] args) throws Exception {
        players =  new Player[DEFAULT_SIZE];
        nbrPlayers = 0;
        //TODO: Make method that will read all the existing players and save them on the array
        interperter();
    }

    private static void interperter() {
        Scanner in = new Scanner(System.in);
        boolean running = true;
        System.out.print(TITLE);

        while (running) {
            System.out.print(MENU);

            int cmd; 
            
            try {
                cmd = in.nextInt(); in.nextLine();
                
                switch (cmd) {
                    case START_NEW_GAME -> startNewGame(in);
                    case LOAD_GAME -> doNothing();
                    case NEW_PLAYER -> doNothing();
                    case SCORE_CARD -> doNothing();
                    case EXIT ->  running = false; //TODO: Make a function that prints the exit message while at the same time saving any information needed to files (Maybe ask if the current game is to be saved, overwrittting the previous save)

                }
            } catch (InputMismatchException e) {
                in.nextLine();
                System.out.print(INPUT_ERROR);
            }

        }

        in.close();
    }

    private static void startNewGame(Scanner in) { 
        try {
            System.out.print("Insert Player1s' name: ");
            String name1 = in.nextLine();
            Player P1 = getPlayer(name1);

            System.out.print("\nInsert Player2s' name: ");
            String name2 =  in.nextLine();
            Player P2 = getPlayer(name2);
            
            System.out.println("Please insert number of lines, followed by the number of columns in the new board: ");
            int nbrLines = in.nextInt(); int nbrCollumns = in.nextInt(); in.nextLine();
            int nChips = getNChips(in);

            if (nbrLines <= nChips && nbrCollumns <= nChips)
                throw new AreaTooSmallException();

            GameSystem game = new GameSystem(nbrLines, nbrCollumns, nChips, P1, P2);

        }catch (InputMismatchException e) {
            System.out.println(Colour.RED.getCode() + "Plese mind input types! Only write letters when asked to." + SET_PLAIN_TEXT);
        }catch(Exception e) {
            System.out.println(Colour.RED.getCode() + e.getMessage() + SET_PLAIN_TEXT);
        }


    }

    private static Player getPlayer(String name) throws NoSuchPlayerException {
        for (int i = 0; i < nbrPlayers; i++) {
            Player currentPlayer = players[i];
            if (currentPlayer.getName().toLowerCase().equals(name.toLowerCase()));
                return currentPlayer; 
        }

        throw new NoSuchPlayerException(name);
    }

    private static void printPlayerSetup() {
        String colours = " ";
    
        for (Colour colour : Colour.values()) {
            colours += (colour.getCode() + colour + SET_PLAIN_TEXT + " ");
        }
    
        System.out.printf(PLAYER_SETPUT, colours);

    }

    private static Player getPlayer(Scanner in) { //TODO: Unfinished
        System.out.print("-");
        String name = in.next();
        String colour = in.nextLine().strip();

        return new Player(name, getColour(colour));
        /**
         * TODO
         * 
         * Lets make this simpler, first we print a line saying 
         *              Player setup
         *      Players choose a name and colour:
         * (RED ORANGE YELLOW GREEN BLUE INDIGO VIOLET)
         * 
         * Player1: {name} {colour}. ---> If there is any type of exception make both players say write everything again
         * Player2: {name} {colour}. --|
         * 
         * Players are setup, starting game...
         * {game}
         * 
         * TODO: Make it so that both players can't have the same colour.
         */

    }

    private static Colour getColour(String chosenColour) throws InputMismatchException {
        for (Colour colour : Colour.values()) {
            if (chosenColour.toLowerCase().equals(colour.toString()))
                return colour;
        }

        return null; //TODO: Handle the exception
    }

    private static int getNChips(Scanner in) {
        int nChips = 0;

        while (nChips < 2) {
            System.out.print("Please insert the number of chips in a line needed to win: ");
            nChips = in.nextInt(); in.nextLine();

            if (nChips < 2)
                System.out.printf(SET_BOLD_TEXT + "Error: Number too small!!\n\n" + SET_PLAIN_TEXT);

        }

        return nChips;
    }

    private static void runGame(GameSystem game, Scanner in) {
        while (!game.isGameOver()) {
            game.printBoard();
            game.nextMove(in);
        }

        try {
            int winner = game.getWinner();

            if (winner != 0) {
                System.out.println("Player: " + winner + " has won the game with " + game.getMovesMade(winner) + " moves");
            } else if (game.gameTied())
                System.out.println("There are no more free positions, game tied.");
                System.out.println("Thanks for playing!!");

        }catch (NoSuchPlayerException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void doNothing() {}
}

//TODO!: Fix GamesystemClass updating it to use the player class.
//TODO: if the whole board has been field end the game on a tie.
//TODO: Maybe when the game is saved ask the names of player 1 and 2, in order to be eaiser to start the game back up ltr on.