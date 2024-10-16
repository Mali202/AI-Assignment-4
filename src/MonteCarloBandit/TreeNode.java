import org.javatuples.Pair;

import java.util.*;

public class TreeNode {
    String move;
    int wins;
    int visits;
    int side;
    TreeNode parent;
    BoardDataStructure state;
    List<TreeNode> children;

    public TreeNode(TreeNode parent, BoardDataStructure state, int side, String move)
    {
        this.parent = parent;
        this.state = state;
        this.side = side;
        this.move = move;
        wins = 0;
        visits = 0;
    }

    //Create children of a node
    public void createChildren()
    {
        children = new ArrayList<>();
        List<Pair<Integer, Integer>> moves = state.GetEmptySpots();
        for (Pair<Integer, Integer> move : moves)
        {
            BoardDataStructure newState = state.clone();
            newState.ApplyMove(move.getValue0(), move.getValue1(), side);
            TreeNode child = new TreeNode(this, newState, 3 - side, move.getValue0() + "," + move.getValue1());
            children.add(child);
        }
    }

    //Backpropagation of result of a simulation
    public void Update(int result)
    {
        TreeNode curNode = this;
        while (curNode != null)
        {
            curNode.visits++;
            curNode.wins += result;
            curNode = curNode.parent;
        }
    }

    //Get random child
    public TreeNode getRandomChild(Random r)
    {
        return children.get(r.nextInt(children.size()));
    }

    //Select child based on UCT
    public TreeNode selectChild() {
        int parentVisits = this.visits;
        return Collections.max(children, Comparator.comparing(c -> uctValue(parentVisits, c.wins, c.visits)));
    }

    private double uctValue(int totalVisits, int nodeWins, int nodeVisits) {
        if (nodeVisits == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) nodeWins / (double) nodeVisits) + 1.41 * Math.sqrt(Math.log(totalVisits) / (double) nodeVisits);
    }
}
