import java.util.Random;

public class WhateverPlayer implements Player {
    private final Random rand;

    /**
     * A constructor for the whatever player
     */
    public WhateverPlayer() {
        rand = new Random();
    }

    /**
     * Plays a turn of the whatever player by randomly choosing a blank spot
     * @param board The board to play on
     * @param mark The mark to put on the board
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        int row = rand.nextInt(size);
        int col = rand.nextInt(size);
        while (!board.putMark(mark, row, col)) {
            row = rand.nextInt(size);
            col = rand.nextInt(size);
        }
    }
}
