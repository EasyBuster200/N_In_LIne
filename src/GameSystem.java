import java.util.InputMismatchException;
import java.util.Scanner;
import Exceptions.OutOfBoundsException;

public class GameSystem {
    private static final String DARK_GREY = "\u001B[30m";
    private static final String WHITE = "\u001B[37m";
    private static final String PLAYER1 = "\u001B[34m"; //Blue
    private static final String PLAYER2 = "\u001B[31m"; //Red

    private int nLines, nColumns, currentPlayer, nChipsToWin, player1Moves, player2Moves, boardSize, winner;
    private int board[][];
    private boolean isOver, tie;
    public GameSystem (int nLines, int nColumns, int nChipsToWin) {
        this.nLines = nLines; this.nColumns = nColumns; this.nChipsToWin = nChipsToWin;
        this.currentPlayer = 1; this.winner = 0;
        this.player1Moves = 0;  this.player2Moves = 0; this.boardSize = nLines * nColumns;
        this.board = new int[nLines][nColumns];
        this.isOver = false; this.tie = false;
    }

    public boolean isGameOver() {return this.isOver;}

    public boolean gameTied() {return this.tie;}

    public int getWinner() { return this.winner;}

    public int getMovesMade(int player) throws NoSuchPlayerException {
        if (player == 1)
            return this.player1Moves;

        if (player == 2)
            return this.player2Moves;

        else 
            throw new NoSuchPlayerException(player);
    }

    public void printBoard() {
        System.out.println();
        
        for(int i = nLines - 1; i > -1; i--) {
            System.out.printf((i + 1) + " ");
            for(int j = 0; j < nColumns; j++) {
                int current = board[i][j];

                if (current == 1)
                    System.out.print(PLAYER1 + "0 " + WHITE);

                else if (current == 2)
                    System.out.print(PLAYER2 + "0 " + WHITE);

                else 
                    System.out.print(DARK_GREY + "0 " + WHITE);
            }
            System.out.println();
        }
        
        System.out.print("  ");

        for (int j = 0; j < nColumns; j++)
            System.out.print(j + 1 + " ");
        
        System.out.println();

    }

    public void nextMove(Scanner in) {
        boolean moveNotMade = true;

        while (moveNotMade) {
            System.out.println("Input 0 to exit.");
            try {
                if (currentPlayer == 1) {
                    System.out.print(PLAYER1 + "Player 1 " + WHITE + "make your next move: "); 
                    player1Moves++;
                }

                else if (currentPlayer == 2){
                    System.out.print(PLAYER2 + "Player 2 " + WHITE + "make your next move: ");
                    player2Moves++;
                }

                int move = in.nextInt() - 1;

                if (move  == - 1){
                    endGame(in);
                    break;
                }

                int y = getYCoord(move);
                board[y][move] = currentPlayer;
                checkForWin(y, move);
                nextPlayer();
                moveNotMade = false;

                if ((player1Moves + player2Moves) == boardSize) {
                    tie = true;
                    isOver = true;
                }
                    //TODO: Set tie boolean to true and check in main if there was a tie 

            }catch (InputMismatchException e){
                System.out.println("\nPlease insert only numbers into the prompt");
            }catch (OutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void checkForWin(int line, int collumn) {
        int[][] directions = {
            {0, 1},    // Right
            {0, -1},   // Left
            {1, 0},    // Down
            {1, 1},    // Diagonal (bottom-left to top-right)
            {1, -1},   // Diagonal (bottom-right to top-left)
            {-1, 1},   // Diagonal (top-left to bottom-right)
            {-1, -1}   // Diagonal (top-right to bottom-left)
        };
        
        int maxCount = 0;
        
        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            
            int count = 1;
            int x = collumn + dx;
            int y = line + dy;
            
            while (x >= 0 && x < nColumns && y >= 0 && y < nLines && board[y][x] == currentPlayer) {
                count++;
                x += dx;
                y += dy;
            }
        
            x = collumn - dx;
            y = line - dy;
        
            while (x >= 0 && x < nColumns && y >= 0 && y < nLines && board[y][x] == currentPlayer) {
                count++;
                x -= dx;
                y -= dy;
            }
        
            if (count >= nChipsToWin) {
                printBoard();
                gameWon();
                break;
            }
            
            if (count > maxCount) {
                maxCount = count;
            }
        }
        
        if (maxCount >= nChipsToWin) {
            printBoard();
            gameWon();
        }

    }

    private void gameWon() {
        winner = currentPlayer;
        //TODO: Make a variable that tells main if there has been a winner and if so ask if they wish to save their nbr of moves to the scoreboard, and if so ask the name to be put into the board. 
        isOver = true;
    }

    private void endGame(Scanner in) {
        System.out.print("Do you wish to save the current game (Y/N)? ");
        String answer = in.next();

        if (answer.equalsIgnoreCase("Y"))
            saveGame();

        else
            System.out.println("Thanks for playing!");

        isOver = true;
    }

    private void saveGame() {
        
    }

    private int getYCoord(int move) throws OutOfBoundsException {
        if (move >= nColumns || move < 0)
            throw new OutOfBoundsException();

        for (int i = 0; i < nLines; i++)
            if (board [i][move] == 0)
                return i;

        throw new OutOfBoundsException();
    }

    private void nextPlayer() {
        currentPlayer++;

        if (currentPlayer > 2)
            currentPlayer = 1;
    }
}
