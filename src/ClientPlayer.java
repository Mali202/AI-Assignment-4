/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.DateFormat;
import java.util.GregorianCalendar;
import javax.swing.*;
/**
 *
 * @author MC
 */
public class ClientPlayer {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {


        String server = "";
        int port = 0;
        
        GregorianCalendar now = new GregorianCalendar();
        DateFormat df = DateFormat.getTimeInstance();
        String timenow = df.format(now.getTime());
        //server local or hosted at a specific IP and port?
        String[] options = {"Local", "Other"};
        int x = JOptionPane.showOptionDialog(null, "Local or Other Server",
                "Select Server",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(x == 1)
        {
            server = JOptionPane.showInputDialog("Enter server name");
            port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number"));
        }
        //Will match be played once or eight times (tournament)
        options = new String[] {"Single", "Tournament"};
        int y = JOptionPane.showOptionDialog(null, "Single Game or Tournament Match",
                "Single of Tournament",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if(y == 0)
        {
            y = 1;
        }
        else y = 8;


        //What type of player will be initialized on the client?
        options = new String[] {"Random", "Human", "MiniMax", "MiniMax + AB Pruning", "MonteCarlo" ,"MonteCarlo with Bandit Heuristic"};
         x = JOptionPane.showOptionDialog(null, "Please select a player",
                "Select Player",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        String playername = "";
        
        
        //initialize the appropriate type of player
        for(int c = 0; c<y; c++)
        {
            if(x == 0)
            {   
                
                if(server.isEmpty())
                {
                    new RandomPlayer("RandomPlayer " + timenow);
                }
                else new RandomPlayer(server, port, "RandomPlayer " + timenow);


            }
            else if(x==1)
            {
                if(playername.isEmpty())
                {
                    playername = JOptionPane.showInputDialog("Enter player name");
                }
                if(server.isEmpty())
                {
                    new HumanPlayer(playername);
                }
                else new HumanPlayer(server, port, playername);           
            }
            else if(x==2)
            {
               if(server.isEmpty())
                {
                    new MiniMaxPlayer("MiniMaxPlayer " + timenow);
                }
                else new RandomPlayer(server, port, "MiniMaxPlayer " + timenow);
            }
            else if(x==3)
            {
                if(server.isEmpty())
                {
                    new RandomPlayer("AlphaBetaPlayer " + timenow);
                }
                else new RandomPlayer(server, port, "AlphaBetaPlayer " + timenow);
            }           
            else if(x==4)
            {
                if(server.isEmpty())
                {
                    new RandomPlayer("RandomPlayer " + timenow);
                }
                else new RandomPlayer(server, port, "RandomPlayer " + timenow);          
            }
            else if(x==5)
            {
                if(server.isEmpty())
                {
                    new RandomPlayer("RandomPlayer " + timenow);
                }
                else new RandomPlayer(server, port, "RandomPlayer " + timenow);
            }
            
            
        }
    } 
    
}
