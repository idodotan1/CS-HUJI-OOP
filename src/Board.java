import java.util.Arrays;
public class Board {
    private static final int DEFAULT_SIZE = 4;
    private final Mark[][] board;
    private final int size;

    /**
     * A board constructor that gets it's size as input
     * @param size the board (row/col) size
     */
    public Board(int size) {
        this.size = size;
        this.board = new Mark[size][size];
        for (Mark[] marks_row : board) {
            Arrays.fill(marks_row, Mark.BLANK);
        }
    }
    /**
     * A board constructor that sets the board size to a default value(3)
     */
    public Board() {
        this.size = DEFAULT_SIZE;
        this.board = new Mark[size][size];
        for (Mark[] marks_row : board) {
            Arrays.fill(marks_row, Mark.BLANK);
        }
    }

    /**
     * @return the size of a row/col in the board
     */
    public int getSize() {
        return size;
    }
    private boolean validCoordinate(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
    /**
     * Puts a new mark on the board
     * @param mark the mark to put
     * @param row
     * @param col
     * @return True if the mark was put successfully, False otherwise
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (!validCoordinate(row,col) || (board[row][col] != Mark.BLANK)) {
            return false;
        }
        board[row][col] = mark;
        return true;
    }

    /**
     * @param row
     * @param col
     * @return The mark at the board[row][col], if the coordinates are
     * invalid returns Blank
     */
    public Mark getMark(int row, int col) {
        if (validCoordinate(row,col)) {
            return board[row][col];
        }
        return Mark.BLANK;
    }
}
