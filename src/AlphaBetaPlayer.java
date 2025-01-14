import org.javatuples.Pair;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

public class AlphaBetaPlayer implements Player {

    Socket connectToServer;

    BufferedReader isFromServer;
    PrintWriter osToServer;

    int BoardSize;
    int Side;
    int ResponseTime;

    int Port;
    String Server;
    String PlayerName;

    public AlphaBetaPlayer()
    {
        PlayerName = "AlphaBetaPlayer";
        BoardSize = 3;
        Side = 1;
    }

    public AlphaBetaPlayer(String name)
    {
        this("Local", 2001, name);
    }

    public AlphaBetaPlayer(String server, int port, String name)
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
                System.out.println("Response: " + nextL);

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

        Pair<Integer, String> pair = AlphaBeta(bds, true, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        String move = pair.getValue1();

        System.out.println("Move: " + move);
        return move;
    }

    private Pair<Integer, String> AlphaBeta(BoardDataStructure bds, boolean maximizingPlayer, int alpha, int beta, int depth) {
        int state = bds.CheckWinner();
        //System.out.println("Depth: " + depth);
        if (state != BoardDataStructure.Empty)
        {
            //System.out.println("Terminal");
            return state == Side ? Pair.with(1, "") : Pair.with(-1, "");
        }

        if (depth >= 1000)
        {
            return Pair.with(0, "");
        }

        String bestMove = "";
        List<Pair<Integer, Integer>> moves = bds.GetEmptySpots();
        if (moves.isEmpty()) {
            System.out.println("No Moves");
            return Pair.with(0, "");
        }

        int bestScore;
        if (maximizingPlayer)
        {
            bestScore = Integer.MIN_VALUE;
            for (Pair<Integer, Integer> move : moves)
            {
                bds.ApplyMove(move.getValue0(), move.getValue1(), Side);
                int score = AlphaBeta(bds, false, alpha, beta,depth + 1).getValue0();
                bds.TakeBackMove(move.getValue0(), move.getValue1());
                if (score > bestScore)
                {
                    bestScore = score;
                    bestMove = move.getValue0() + "," + move.getValue1();
                }

                if (bestScore > beta)
                {
                    return Pair.with(bestScore, bestMove);
                }

                alpha = Math.max(alpha, bestScore);
            }
        }
        else
        {
            bestScore = Integer.MAX_VALUE;
            for (Pair<Integer, Integer> move : moves)
            {
                bds.ApplyMove(move.getValue0(), move.getValue1(), 3 - Side);
                int score = AlphaBeta(bds, true, alpha, beta, depth + 1).getValue0();
                bds.TakeBackMove(move.getValue0(), move.getValue1());
                if (score < bestScore)
                {
                    bestScore = score;
                    bestMove = move.getValue0() + "," + move.getValue1();
                }
                if (bestScore < alpha)
                {
                    return Pair.with(bestScore, bestMove);
                }

                beta = Math.min(beta, bestScore);
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
