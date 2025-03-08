public class Game {
    private final static int DEFAULT_WIN_STREAK = 3;
    private final Player playerX;
    private final Player playerO;
    private final Renderer renderer;
    private final Board board;
    private final int winStreak;
    private int numOfBlankSpots;

    /**
     * A constructor that creates a game with the default rules(size 3X3, and 3 win streak
     * @param playerX The X player
     * @param playerO The O Player
     * @param renderer The renderer
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.board = new Board();
        this.winStreak = DEFAULT_WIN_STREAK;
        numOfBlankSpots = board.getSize() * board.getSize();
    }

    /**
     * A constructor that creates a game with custom rules
     * @param playerX The X player
     * @param playerO The O player
     * @param size The size of the board(row/col)
     * @param winStreak The streak needed in order to win
     * @param renderer The renderer
     */
    public Game(Player playerX, Player playerO,
                int size, int winStreak,Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.board = new Board(size);
        this.winStreak = winStreak;
        numOfBlankSpots = board.getSize() * board.getSize();
    }

    /**
     * @return the streak needed in order to win
     */
    public int getWinStreak() {
        return winStreak;
    }

    /**
     * @return the board size
     */
    public int getBoardSize() {
        return board.getSize();
    }
    //Checks a direction for a winner
    private boolean checkDirection(int row, int col,
                                   int deltaRow, int deltaCol, Mark mark) {
        int k = 0;
        int curRow = row;
        int curCol = col;
        while (curRow < board.getSize() && curRow >= 0 &&
                curCol < board.getSize() && curCol >= 0 &&
                board.getMark(curRow, curCol) == mark) {
            k++;
            curRow += deltaRow;
            curCol += deltaCol;
        }
        return k >= winStreak;
    }
//Checks if there is a winner after the current round
    public boolean checkWin(int row, int col, Mark mark) {
        // Check horizontally (deltaRow = 0, deltaCol = 1)
        if (checkDirection(row, col, 0, 1, mark)) {
            return true;
        }

        // Check vertically (deltaRow = 1, deltaCol = 0)
        if (checkDirection(row, col, 1, 0, mark)) {
            return true;
        }
        //Check all diagonals
        if (checkDirection(row,col, 1, -1, mark)) {
            return true;
        }
        if (checkDirection(row,col, -1, -1, mark)) {
            return true;
        }
        if (checkDirection(row,col, -1, -1, mark)) {
            return true;
        }
        return checkDirection(row, col, 1, 1, mark);
    }
//Returns the winner after the current round, Blank if there isn't one
    private Mark hasWinner()
    {
        for (int i = 0; i < board.getSize(); i++){
            for (int j = 0; j < board.getSize(); j++){
                Mark currMark = board.getMark(i, j);
                if(currMark != Mark.BLANK && checkWin(i,j,currMark)){
                    return currMark;
                }
            }
        }
        return Mark.BLANK;
    }
    /**
     * Runs a single game
     * @returns the mark of the winner
     */
    public Mark run()
    {
        Mark winner = Mark.BLANK;
        boolean turnX = true;
        while (winner == Mark.BLANK && numOfBlankSpots > 0) {
            if (turnX) {
                playerX.playTurn(board,Mark.X);
                turnX = false;
            }
            else {
                playerO.playTurn(board,Mark.O);
                turnX = true;
            }
            winner = hasWinner(); //Blank if there is no winner
            numOfBlankSpots--; //After each turn there is one less blank spot
            renderer.renderBoard(board);
        }
        return winner;
    }
}
