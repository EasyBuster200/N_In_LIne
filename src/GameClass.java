import Exceptions.OutOfBoundsException;

public class GameClass implements Game {

    //Constants
    private static final String WHITE = Colour.WHITE.getCode();

    //Variables
    private int nLines, nColumns, nChipsToWin, boardSize;
    private int board[][];
    private boolean isOver, tie;
    private Player P1, P2, currentPlayer, winner;


    public GameClass(int nLines, int nColumns, int nChipsToWin, Player P1, Player P2) {
        this.P1 = P1; this.P2 = P2;
        P1.resetMovesMade(); P2.resetMovesMade();
        this.currentPlayer = P1;
        winner = null;
        this.nLines = nLines; this.nColumns = nColumns; this.nChipsToWin = nChipsToWin;
        this.boardSize = nLines * nColumns;
        this.board = new int[nLines][nColumns];
        this.isOver = false; this.tie = false;
    }

    @Override
    public boolean isOver() {
        return isOver;
    }

    @Override
    public Player winner() {
        return winner;
    }

    @Override
    public boolean tied() {
        return tie;
    }

    @Override
    public Player getP1() {
        return P1;
    }

    @Override
    public Player getP2() {
        return P2;
    }

    @Override
    public void printBoard() {
        System.out.println();
        
        for(int i = nLines - 1; i > -1; i--) {
            System.out.printf((i + 1) + " ");
            for(int j = 0; j < nColumns; j++) {
                int current = board[i][j];

                if (current == 1)
                    System.out.print(P1.getColour() + "0 " + WHITE);

                else if (current == 2)
                    System.out.print(P2.getColour() + "0 " + WHITE);

                else 
                    System.out.print(Colour.DARK_GREY.getCode() + "0 " + WHITE);
            }
            System.out.println();
        }
        
        System.out.print("  ");

        for (int j = 0; j < nColumns; j++)
            System.out.print(j + 1 + " ");
        
        System.out.println();
    }

    @Override
    public void nextMove(int pos) throws OutOfBoundsException, GameEndedException {
        int collumn = pos - 1;
        int line = -1;

        if(collumn == -1) 
            throw new GameEndedException();
        
        if(pos >= nColumns || pos < 0)
            throw new OutOfBoundsException();

        for (int i = 0; i < nLines; i++)
            if(board[i][collumn] == 0) {
                line = i;
                break;
            }

        if (line == -1)
            throw new OutOfBoundsException();

        if (currentPlayer == P1)
            board[line][collumn] = 1;
        else 
            board[line][collumn] = 2;

        checkForWin(line, collumn);
        nextPlayer();
        currentPlayer.moveMade();

        if(P1.getMovesMade() + P2.getMovesMade() == boardSize) {
            tie = true;
            isOver = true;
        }
        
    }


    private void nextPlayer() {
        if (currentPlayer == P1)
            currentPlayer = P2;
        else
            currentPlayer = P1;
    }

    private void checkForWin(int line, int column) {
        int playerNum;

        if (currentPlayer == P1)
            playerNum = 1;
        else 
            playerNum = 2;
            
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
            int deltaX = dir[0];
            int deltaY = dir[1];

            int count = 1;
            int x = column + deltaX;
            int y = line + deltaY;

            // Check if the initial move is valid
            if (x >= 0 && x < nColumns && y >= 0 && y < nLines && board[y][x] == playerNum) {
                while (x >= 0 && x < nColumns && y >= 0 && y < nLines && board[y][x] == playerNum) {
                    count++;
                    x += deltaX;
                    y += deltaY;
                }
            
                x = column - deltaX;
                y = line - deltaY;
            
                while (x >= 0 && x < nColumns && y >= 0 && y < nLines && board[y][x] == playerNum) {
                    count++;
                    x -= deltaX;
                    y -= deltaY;
                }
            
                if (count >= nChipsToWin) {
                    printBoard();
                    winner = currentPlayer;
                    isOver = true;
                    break;
                }
            
                if (count > maxCount) {
                    maxCount = count;
                }
            }
        }

        if (maxCount >= nChipsToWin) {
            printBoard();
            winner = currentPlayer;
            currentPlayer.wonGame();
            isOver = true;
        }
    }

    
}
