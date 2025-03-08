/**
 * The interface that a tic-tac-toe player needs to implement
 */
public interface Player {
    /**
     * Plays a turn of a player
     * @param board The board to play on
     * @param mark The mark to put on the board
     */
    void playTurn(Board board,Mark mark);
}
