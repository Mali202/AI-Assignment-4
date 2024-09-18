/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




import java.io.*;
import java.net.*;
import java.util.*;


/**
 *
 * @author MC
 */
public class RandomPlayer implements Player
{
    Socket connectToServer;
    
    BufferedReader isFromServer; 
    PrintWriter osToServer; 
    
    int BoardSize;
    int Side;
    int ResponseTime;
    
    Random r = new Random();
    
    int Port;
    String Server;
    String PlayerName;
    
    public RandomPlayer()
    {
        this("Local", 2001, "RandomPlayer");
    }
    
    public RandomPlayer(String name)
    {
        this("Local", 2001, name);
    }
            
    public RandomPlayer(String server, int port, String name)
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
    //can be used
    public int PlayRandomGame(BoardDataStructure cur, int CurSide)
    {
        //if there is a winner then we know it was after the previous move and therefore done by the opponent
        if(cur.CheckWinner() != BoardDataStructure.Empty)
        {


            return ((CurSide+1)%2)+1;
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
            return PlayRandomGame(cur, ((CurSide+1)%2)+1);
        }
        
    }

   @Override
    public String MakeMove(String board) 
    {
        BoardDataStructure temp = BoardDataStructure.GetBoardFromString(board, BoardSize);
        /*StringTokenizer st = new StringTokenizer(board, ",");
        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                temp.Board[c][r] = Integer.parseInt(st.nextToken());
            }
        }*/
        //just finds a random empty spot and plays at that spot
        while(true)
        {
            int col = r.nextInt(BoardSize);
            int row = r.nextInt(BoardSize);
            if(temp.Board[row][col]== BoardDataStructure.Empty)
            {
                return col + "," + row;
            }
        }
        
    }
    
}
