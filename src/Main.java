import java.util.InputMismatchException;
import java.util.Scanner;

import Exceptions.AreaTooSmallException;

public class Main {
    //Menu
    private static final String TITLE = "    N em Linha \n";
    private static final String MENU = "\nAvailable Commands: \n 1 - Start New Game \n 2 - Load Game \n 3 - Score Card \n 4 - Exit\n\n";
    private static final String INPUT_ERROR = "\nInput must be one of the available commands\n";
    public static final String SET_PLAIN_TEXT = "\033[0;0m";
    public static final String SET_BOLD_TEXT = "\033[0;1m";

    //Commands
    private static final int START_NEW_GAME = 1;
    private static final int LOAD_GAME = 2;
    private static final int SCORE_CARD = 3;
    private static final int EXIT = 4;

    //Variables
    
    public static void main(String[] args) throws Exception {
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
                cmd = in.nextInt();
                
                switch (cmd) {
                    case START_NEW_GAME -> startNewGame(in);
                    case LOAD_GAME -> doNothing();
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
        GameSystem game;
        boolean gameNotStarted = true;
        
        while (gameNotStarted) {
            try {
                int nChips = getNChips(in);
                int nLines = 0, nColumns = 0;
                boolean dimensionsNotDefined = true;

                while (dimensionsNotDefined) {
                    try {
                        System.out.print("Please insert number of lines, followed by the number of columns in the new board: ");
                        nLines = in.nextInt();
                        nColumns = in.nextInt(); in.nextLine();

                        if (nLines <= nChips && nColumns <= nChips)
                            throw new AreaTooSmallException();

                        dimensionsNotDefined = false;

                    } catch (AreaTooSmallException e) {
                        System.out.println();
                        System.out.println(e.getMessage());
                    } catch (InputMismatchException e) {
                        in.nextLine();
                        System.out.printf(SET_BOLD_TEXT + "Error: insert only numbers into the prompt\n\n" + SET_PLAIN_TEXT); 
                    }
                }

                game = new GameSystem(nLines, nColumns, nChips); gameNotStarted = false;
                runGame(game, in);

                }catch (InputMismatchException e) {
                    in.nextLine();
                    System.out.printf(SET_BOLD_TEXT + "Error: insert only numbers into the prompt\n\n" + SET_PLAIN_TEXT); 
                }
        }
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
    }

    private static void doNothing() {}
}
//TODO: if the whole board has been field end the game on a tie