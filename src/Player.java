
/**
 *
 * @author MC
 */
public interface Player {
    //Establishes a connection with server and obtains relevant details such as board size and response time. You are welcome to adapt the RandomPlayer implementation for your players
    void GetConnection();


    //Receives current board state in string format. Returns the move to be played in the form "row,col" where row and col are both integers that represent the zero-indexed row and column of the board where the move is to be played
    String MakeMove(String board);
    
}
