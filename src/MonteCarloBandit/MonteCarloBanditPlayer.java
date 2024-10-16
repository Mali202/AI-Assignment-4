import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class MonteCarloBanditPlayer implements Player {

    Socket connectToServer;

    BufferedReader isFromServer;
    PrintWriter osToServer;

    int BoardSize;
    int Side;
    int ResponseTime;

    int Port;
    String Server;
    String PlayerName;

    Random r = new Random();

    //For testing purposes
    public MonteCarloBanditPlayer()
    {
        PlayerName = "MonteCarloPlayer";
        BoardSize = 3;
        Side = 1;
        ResponseTime = 30;
    }

    public MonteCarloBanditPlayer(String name)
    {
        this("Local", 2001, name);
    }

    public MonteCarloBanditPlayer(String server, int port, String name)
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

    @Override
    public String MakeMove(String board) {
        BoardDataStructure bds = BoardDataStructure.GetBoardFromString(board, BoardSize);
        return UCT(bds);
    }

    private String UCT(BoardDataStructure bds) {
        final boolean[] timeOutOccurred = {false};
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timeout occurred");
                timeOutOccurred[0] = true;
            }
        }, ResponseTime * 1000L);

        TreeNode root = new TreeNode(null, bds, Side, "");
        do {
            search(root);
        } while (!timeOutOccurred[0]);

        System.out.println("Visits: " + root.visits);
        return Collections.max(root.children, Comparator.comparing(c -> c.visits)).move;
    }

    private void search(TreeNode node) {
        int state = node.state.CheckWinner();
        if (state != BoardDataStructure.Empty) {
            node.Update(state == Side ? 1 : 0);
            return;
        }

        if (node.visits == 0) {
            node.createChildren();
            TreeNode newNode = node.getRandomChild(r);
            int result = PlayRandomGame(newNode.state, newNode.side);
            node.Update(result == Side ? 1 : 0);
        } else {
            TreeNode newNode = node.selectChild();
            search(newNode);
        }
    }

    public int PlayRandomGame(BoardDataStructure cur, int CurSide)
    {
        //if there is a winner then we know it was after the previous move and therefore done by the opponent
        if(cur.CheckWinner() != BoardDataStructure.Empty)
        {
            //System.out.println("curSide: " + CurSide + " Winner: " + (3 - CurSide));
            return 3 - CurSide;
        }
        else
        {
            //if no winner, then find a random empty spot to move and call playRandom game again, but it's the opponents move
            int col = r.nextInt(BoardSize);
            int row = r.nextInt(BoardSize);
            while(cur.Board[col][row] != BoardDataStructure.Empty)
            {
                col++;
                if(col == BoardSize)
                {
                    col = 0;
                    row++;
                }
                if(row== BoardSize)
                {
                    row = 0;
                }
            }

            cur.Board[col][row] = CurSide;
            return PlayRandomGame(cur,  3 - CurSide);
        }

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
