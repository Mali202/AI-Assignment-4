public class TestPlayers {

    public static void main(String[] args) {
        MiniMaxPlayer mmp = new MiniMaxPlayer();

        String boardState ="0,0,0,0,0,0,0,0,0";
        String move = mmp.MakeMove(boardState);
        System.out.println(boardState);
    }
}
