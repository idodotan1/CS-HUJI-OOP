public class CleverPlayer implements Player {
    /**
     * A constructor for clever player
     */
    public CleverPlayer() {
    }
    //Tries to find and continue a streak forward or downwards
    private boolean smartTurn(Board board, Mark mark) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getMark(i, j) == mark) {
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
    //Puts mark in first available place
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
     * Playes a turn of the clever player
     * @param board The board to play on
     * @param mark The mark to put on the board
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        if (smartTurn(board, mark)) {
            return; //If successfully found a streak
        }
        stupidTurn(board, mark); //If not put wherever possible
    }
}
