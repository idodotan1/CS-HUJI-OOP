/**
 * A class used to create and play a genius automate player
 */
public class GeniusPlayer implements Player {
    /**
     * A constructor for a genius player
     */
    public GeniusPlayer() {
    }
    //Tries to block the other player
    private boolean tryToBlock(Board board, Mark mark) {
        Mark oppositeMark = (mark == Mark.X) ? Mark.O : Mark.X;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getMark(i, j) == oppositeMark) {
                    if (tryToBlockInDirection(board, mark, i, j, 1, 0, oppositeMark)) {
                        return true;  // Down
                    }
                    if (tryToBlockInDirection(board, mark, i, j, 0, 1, oppositeMark)){
                        return true;  // Right
                    }
                    if (tryToBlockInDirection(board, mark, i, j, 1, 1, oppositeMark)) {
                        return true;  // Diagonal Down-Right
                    }
                    if (tryToBlockInDirection(board, mark, i, j, -1, 1, oppositeMark)) {
                        return true;  // Diagonal Up-Right
                    }
                    if (tryToBlockInDirection(board, mark, i, j, -1, 0, oppositeMark)) {
                        return true; // Up
                    }
                    if (tryToBlockInDirection(board, mark, i, j, 0, -1, oppositeMark)) {
                        return true; // Left
                    }
                    if (tryToBlockInDirection(board, mark, i, j, 1, -1, oppositeMark)) {
                        return true; // Diagonal Down-Left
                    }
                    if (tryToBlockInDirection(board, mark, i, j, -1, -1, oppositeMark)) {
                        return true; // Diagonal Up-Left
                    }
                }
            }
        }
        return false;
    }

    private boolean tryToBlockInDirection(Board board, Mark mark, int row, int col,
                                          int rowDelta, int colDelta, Mark oppositeMark) {
        int nextRow = row + rowDelta;
        int nextCol = col + colDelta;
        int nextNextRow = row + 2 * rowDelta;
        int nextNextCol = col + 2 * colDelta;

        if (nextRow >= 0 && nextRow < board.getSize() &&
                nextCol >= 0 && nextCol < board.getSize() &&
                board.getMark(nextRow, nextCol) == oppositeMark) {

            // Check bounds for the cell after the next
            if (nextNextRow >= 0 && nextNextRow < board.getSize() &&
                    nextNextCol >= 0 && nextNextCol < board.getSize() &&
                    board.getMark(nextNextRow, nextNextCol) == Mark.BLANK) {

                board.putMark(mark, nextNextRow, nextNextCol);
                return true;
            }
        }
        return false;
    }
    //Tries to put a mark in the corners
    private boolean checkCorners(Board board, Mark mark) {
        int size = board.getSize();
        if (board.getMark(0,0) == Mark.BLANK)
        {
            board.putMark(mark,0,0);
            return true;
        }
        if (board.getMark(0,size-1) == Mark.BLANK)
        {
            board.putMark(mark,0,size-1);
            return true;
        }
        if (board.getMark(size-1,0) == Mark.BLANK)
        {
            board.putMark(mark,size-1,0);
            return true;
        }
        if (board.getMark(size-1,size-1) == Mark.BLANK)
        {
            board.putMark(mark,size-1,size-1);
            return true;
        }
        return false;
    }
    //Tries to create a streak in 3 directions
    private boolean GeniusTurn(Board board, Mark mark) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getMark(i, j) == mark) {
                    if (i + 1 < size && j+1 < size && board.getMark(i + 1, j+1)
                            == Mark.BLANK){
                        board.putMark(mark, i + 1, j+1);
                        return true;
                    }
                    if (i + 1 < size && board.getMark(i + 1, j) == Mark.BLANK) {
                        board.putMark(mark, i + 1, j);
                        return true;
                    } else if (j + 1 < size && board.getMark(i, j + 1) == Mark.BLANK) {
                        board.putMark(mark, i, j + 1);
                        return true;
                    }
                    }
            }
        }
        return false;
    }
    //If didn't a mark so far puts in the first available location
    private void stupidTurn(Board board, Mark mark) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getMark(i, j) == Mark.BLANK) {
                    board.putMark(mark, i, j);
                    return;
                }
            }
        }
    }
    /**
     * Plays a genius player turn
     * @param board The board to play on
     * @param mark The mark to put on the board
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        //First, tries to block the other player
        if (tryToBlock(board, mark)) {
            return;
        }
        //Then tries to create a streak
        if (GeniusTurn(board,mark)){
            return;
        }
        //Then tries to put mark in the middle
        if (board.getMark(size/2,size/2) == Mark.BLANK){
            board.putMark(mark,size/2,size/2);
            return;
        }
        //Then tries to put mark in the corners
        if (checkCorners(board,mark)){
            return;
        }
        //If didn't put mark so far just puts wherever possible
        stupidTurn(board,mark);
    }
}
