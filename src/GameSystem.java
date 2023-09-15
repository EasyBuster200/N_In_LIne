import java.util.InputMismatchException;
import java.util.Scanner;
import Exceptions.OutOfBoundsException;

public class GameSystem {
    private static final String DARK_GREY = "\u001B[30m";
    private static final String WHITE = "\u001B[37m";
    private static final String PLAYER1 = "\u001B[34m"; //Blue
    private static final String PLAYER2 = "\u001B[31m"; //Red

    private int nLines, nColumns, currentPlayer, nChipsToWin, player1Moves, player2Moves;
    private int board[][];
    private boolean isOver;
    
    public GameSystem (int nLines, int nColumns, int nChipsToWin) {
        this.nLines = nLines; this.nColumns = nColumns; this.nChipsToWin = nChipsToWin;
        this.currentPlayer = 1;
        this.player1Moves = 0;  this.player2Moves = 0;
        this.board = new int[nLines][nColumns];
        this.isOver = false;
    }

    public boolean isGameOver() {return this.isOver;}

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

            }catch (InputMismatchException e){
                System.out.println("\nPlease insert only numbers into the prompt");
            }catch (OutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void checkForWin(int line, int collumn) { //! Shouldnt work correctly, count might need to be reset each time. If not then a player could have 3 chips horrizontaly and one other chip in any direction and it would count as 4
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
                gameWon();
                break;
            }
            
            if (count > maxCount) {
                maxCount = count;
            }
        }
        
        if (maxCount >= nChipsToWin) {
            gameWon();
        }

    }

    private void gameWon() {
        int movesMade;

        if (currentPlayer == 1)
            movesMade = player1Moves;

        else if (currentPlayer == 2)
            movesMade = player2Moves;

        else 
            movesMade = 0;
            
        System.out.println("Player: " + currentPlayer + " has won the game with " + movesMade + " moves");
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
