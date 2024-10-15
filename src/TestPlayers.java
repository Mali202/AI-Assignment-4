public class TestPlayers {

    public static void main(String[] args) {
        MonteCarloPlayer player = new MonteCarloPlayer();
        player.ResponseTime = 5;
        String boardState ="0,0,0,0,0,0,0,0,0";
        String move = player.MakeMove(boardState);
        System.out.println(move);
        System.out.println(boardState);
    }
}
