/**
 * The class to run a full tic-tac-tor tournament between 2 players
 */
public class Tournament {
    private int rounds;
    private Renderer renderer;
    private Player player1;
    private Player player2;

    /**
     * A constructor for the tournament
     * @param rounds The number of rounds to play
     * @param renderer The renderer to use
     * @param player1
     * @param player2
     */
    public Tournament(int rounds, Renderer renderer, Player player1, Player player2) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * The method to play the tournament
     * @param size The size of the board of the tournament
     * @param winStreak The streak needed in order to win
     * @param playerName1 The type of the first player
     * @param playerName2 The type of the second player
     */
    public void playTournament(int size, int winStreak, String playerName1, String playerName2) {
        int[] winArray = {0,0,0};
        for (int i = 0; i < rounds; i++) {
            Game game;
            //So player1 is playing X and player2 is playing O
            if (i%2 == 0){
                game = new Game(player1,player2,size,winStreak,renderer);
                Mark winner = game.run();
                if (winner == Mark.X){
                    winArray[0]++;
                }
                else if (winner == Mark.O){
                    winArray[1]++;
                }
                //Tie
                else {
                    winArray[2]++;
                }
            }
            //So player1 is playing O and player2 is playing X
            else {
                game = new Game(player2,player1,size,winStreak,renderer);
                Mark winner = game.run();
                if (winner == Mark.X){
                    winArray[1]++;
                }
                else if (winner == Mark.O){
                    winArray[0]++;
                }
                //Tie
                else {
                    winArray[2]++;
                }
            }
        }
        System.out.println("######### Results #########");
        System.out.printf("Player1, %s won: %d rounds \n", playerName1, winArray[0]);
        System.out.printf("Player2, %s won: %d rounds \n", playerName2, winArray[1]);
        System.out.printf("Ties: %d \n", winArray[2]);
    }
    public static void main(String[] args) {
        int rounds = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);
        String rendererName = args[3];
        String player1Name = args[4];
        String player2Name = args[5];
        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(rendererName,size);
        PlayerFactory playerFactory = new PlayerFactory();
        Player player1 = playerFactory.buildPlayer(player1Name);
        Player player2 = playerFactory.buildPlayer(player2Name);
        Tournament tournament = new Tournament(rounds,renderer,player1,player2);
        tournament.playTournament(size,winStreak,player1Name,player2Name);
    }
}
