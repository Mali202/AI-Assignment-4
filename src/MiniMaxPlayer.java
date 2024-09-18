import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

public class MiniMaxPlayer implements Player {

    Socket connectToServer;

    BufferedReader isFromServer;
    PrintWriter osToServer;

    int BoardSize;
    int Side;
    int ResponseTime;

    int Port;
    String Server;
    String PlayerName;

    public MiniMaxPlayer()
    {
        this("Local", 2001, "RandomPlayer");
    }

    public MiniMaxPlayer(String name)
    {
        this("Local", 2001, name);
    }

    public MiniMaxPlayer(String server, int port, String name)
    {
        PlayerName = name;
        Server= server;
        Port = port;

        GetConnection();
        boolean GameOver = false;
        try
        {
            System.out.println("Starting Game");
            while(!GameOver)
            {

                String nextL = isFromServer.readLine();
                System.out.println(nextL);

                if(nextL.length() > 1)
                {
                    osToServer.println(MakeMove(nextL));
                    osToServer.flush();
                }
                else
                {
                    System.out.println("Winner: " + nextL);
                    nextL = isFromServer.readLine();
                    System.out.println("Final Board: " + nextL);
                    GameOver = true;
                }
            }
            System.out.println("Game Over");
        }
        catch(IOException ioe)
        {
            System.out.println("Error: " + ioe.getMessage());
        }

    }

    /**
     * Get Next Move via Minimax Algorithm
     * @param board The current state of the board
     * @return The move to be made by the player
     */
    @Override
    public String MakeMove(String board) {
        BoardDataStructure bds = BoardDataStructure.GetBoardFromString(board, BoardSize);
        //Instant start = Instant.now();
        Pair<Integer, String> pair = MiniMax(bds, true);
        String move = pair.getValue1();
        //Instant end = Instant.now();
        System.out.println("Time taken: ");
        return move;
    }

    private Pair<Integer, String> MiniMax(BoardDataStructure bds, boolean maximizingPlayer) {
        int state = bds.CheckWinner();
        if (state != BoardDataStructure.Empty)
        {
            return state == Side ? Pair.with(1, "") : Pair.with(-1, "");
        }

        String bestMove = "";
        List<Pair<Integer, Integer>> moves = bds.GetEmptySpots();
        int bestScore;
        if (maximizingPlayer)
        {
            bestScore = Integer.MIN_VALUE;
            for (Pair<Integer, Integer> move : moves)
            {
                bds.ApplyMove(move.getValue0(), move.getValue1(), Side);
                int score = MiniMax(bds, false).getValue0();
                bds.TakeBackMove(move.getValue0(), move.getValue1());
                if (score > bestScore)
                {
                    bestScore = score;
                    bestMove = move.getValue0() + "," + move.getValue1();
                }
            }
        }
        else
        {
            bestScore = Integer.MAX_VALUE;
            for (Pair<Integer, Integer> move : moves)
            {
                bds.ApplyMove(move.getValue0(), move.getValue1(), 3 - Side);
                int score = MiniMax(bds, true).getValue0();
                bds.TakeBackMove(move.getValue0(), move.getValue1());
                if (score < bestScore)
                {
                    bestScore = score;
                    bestMove = move.getValue0() + "," + move.getValue1();
                }
            }
        }
        return Pair.with(bestScore, bestMove);
    }


    public String RandomMove(String board)
    {
        return "";
    }

    @Override
    public void GetConnection() {
        try {

            if(Server.equals("Local"))
            {
                connectToServer = new Socket(InetAddress.getLocalHost(),Port);
            }
            else connectToServer = new Socket(Server,Port);
            isFromServer = new BufferedReader(new InputStreamReader(connectToServer.getInputStream()));
            osToServer =  new PrintWriter(connectToServer.getOutputStream());
            System.out.println("Connected");
            osToServer.println(PlayerName);
            osToServer.flush();
            String setup = isFromServer.readLine();
            System.out.println(setup);
            StringTokenizer st = new StringTokenizer(setup, ",");
            BoardSize = Integer.parseInt(st.nextToken());
            Side = Integer.parseInt(st.nextToken());
            ResponseTime = Integer.parseInt(st.nextToken());

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
