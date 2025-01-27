/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author MC
 */

/*
Data structure used to represent a game state
 */
public class BoardDataStructure {

    //if no move then board entry will be 0, if red then 1 and if blue then 2
    public static int Empty = 0;
    public static int RedMove = 1;
    public static int BlueMove = 2;
    public int BoardSize = 7;

    public int[][] Board;

    public BoardDataStructure()
    {
        Board = new int[BoardSize][BoardSize];
    }

    public BoardDataStructure(int BS)
    {
        BoardSize = BS;
        Board = new int[BS][BS];
    }


    //Creates a deep copy of board. This can be used if you modulate the board state when simulating outcomes. If you don't clone the board each time you simulate a board from that state, then you need a mechanism to ensure you reset the board to its original state before simulating a new game.
    public BoardDataStructure clone()
    {
        BoardDataStructure temp = new BoardDataStructure(BoardSize);

        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                temp.Board[c][r] = this.Board[c][r];
            }
        }
        return temp;
    }


    //Used to convert 2-d array board to a string of moves seperated by a comma.
    public String GetBoardString()
    {
        String s = "";
        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                s= s+  this.Board[c][r] +",";
            }
        }
        return s;

    }

    //Used to convert a string of moves seperated by a comma to a 2-d array board
    public static BoardDataStructure GetBoardFromString(String s, int BS)
    {
        BoardDataStructure temp = new BoardDataStructure(BS);
        StringTokenizer st = new StringTokenizer(s, ",");
        for(int r =0; r < BS; r++)
        {
            for(int c = 0; c < BS; c++)
            {
                temp.Board[c][r] = Integer.parseInt(st.nextToken());
            }
        }
        return temp;
    }


    //displays board state
    public void DisplayBoard()
    {
        String s = "";
        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                s= s + this.Board[c][r] +",";
            }
            System.out.println(s);
            s = "";
        }

        System.out.println("");

    }


    //Helper method that can be used to check whether the board is in a terminal state, i.e. Red or Blue has won. Returns the corresponding player number if a player has won, else returns 0.
    public int CheckWinner()
    {
        BoardDataStructure temp = this.clone();
        //Check Red
        for(int c =0; c < BoardSize; c++)
        {
            if(CheckRecursion(temp, 0, c, RedMove)) return RedMove;
        }

        //CheckBlue
        for(int r = 0; r < BoardSize; r++)
        {
            if(CheckRecursion(temp, r, 0, BlueMove)) return BlueMove;
        }

        return Empty;
    }

    public boolean CheckRecursion(BoardDataStructure bds, int row, int col, int Move)
    {


        if(bds.Board[col][row] == Move)
        {
            if((Move == RedMove)&&(row == BoardSize-1)) return true;

            if((Move == BlueMove)&&(col == BoardSize-1)) return true;

            bds.Board[col][row] = Empty;


            //6 possible moves - checks if there is a consecutive chain that includes this move
            if(row-1 >=0)
            {
                if(CheckRecursion(bds, row-1, col, Move))
                {
                    return true;
                }
                else if(col+1 < BoardSize)
                {
                    if(CheckRecursion(bds, row-1, col+1, Move))
                    {
                        return true;
                    }
                }
            }

            if(row+1 < BoardSize)
            {
                if(CheckRecursion(bds, row+1, col, Move))
                {
                    return true;
                }
                else if(col-1 >= 0)
                {
                    if(CheckRecursion(bds, row+1, col-1, Move))
                    {
                        return true;
                    }
                }
            }

            if(col+1 < BoardSize)
            {
                if(CheckRecursion(bds, row, col+1, Move))
                {
                    return true;
                }
            }

            if(col-1 >= 0)
            {
                if(CheckRecursion(bds, row, col-1, Move))
                {
                    return true;
                }
            }
        }

        return false;
    }

    //Apply a move to the board
    public void ApplyMove(int col, int row, int player)
    {
        Board[col][row] = player;
    }

    //Take back a move from the board
    public void TakeBackMove(int col, int row)
    {
        Board[col][row] = Empty;
    }

    //Get Empty Spots on the board
    public List<Pair<Integer, Integer>> GetEmptySpots()
    {
        List<Pair<Integer, Integer>> emptySpots = new ArrayList<>();
        for(int r =0; r < BoardSize; r++)
        {
            for(int c = 0; c < BoardSize; c++)
            {
                if(Board[c][r] == Empty) emptySpots.add(Pair.with(c, r));
            }
        }
        return emptySpots;
    }
}
