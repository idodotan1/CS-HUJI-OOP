/**
 * The enum used to represent the marks in a tic-tac-toe game
 */
public enum Mark { BLANK, X, O;
    /**
    Converts a mark to a string representing that mark.
     */
    public String toString(Mark mark) {
        return switch (mark) {
            case X -> "X";
            case O -> "O";
            default -> null;
        };
    }
}
