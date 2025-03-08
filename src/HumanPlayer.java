public class HumanPlayer implements Player {
    private final static String INVALID_POSITION_MESSAGE =
            "Invalid mark position, Please choose a valid position: ";
    private final static String OCCUPIED_POSITION_MESSAGE =
            "Mark position is already occupied, please choose a valid position: ";

    /**
     * A constructor for a human player
     */
    public HumanPlayer() {
    }

    /**
     * Plays a human player turn by getting input from the user and validating it
     * @param board The board to play on
     * @param mark The mark to put on the board
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        System.out.println("Player " + mark.toString() +" type coordinates: ");
        int num = KeyboardInput.readInt();
        int row = num / 10;
        int col = num % 10;
        //If it didn't put a mark
        while (!(board.putMark(mark, row, col))) {
            /*If the coordinates are invalid then it returns blank, and if it was valid coordinates
             and the spot was blank then it would successfully put the mark there
             */
            if (board.getMark(row, col) == Mark.BLANK) {
                System.out.println(INVALID_POSITION_MESSAGE);
            }
            //The spot is not blank
            else {
                System.out.println(OCCUPIED_POSITION_MESSAGE);
            }
            //Try again
            num = KeyboardInput.readInt();
            row = num / 10;
            col = num % 10;
        }
    }
}
