import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;
import java.util.ArrayDeque;

public class Solver {
    
    private MinPQ<SearchNode> searchPQ, twinPQ;
    private int moves; // # of moves made. -1 if unsolvable
    private SearchNode solutionNode; // solved board with links to previous boards
    private boolean solvable; // if twin is solved, then original can't be solved
    private ArrayDeque<Board> solutionD; 
    
    public Solver(Board initial) {
        if (initial == null) { throw new java.lang.IllegalArgumentException(); }
        
        moves = 0;
        solutionNode = null;
        solvable = true;
        solutionD = new ArrayDeque<Board>();
        
        SearchNode root = new SearchNode(initial);
        root.prev = null;
        searchPQ = new MinPQ<SearchNode>(root.priorityOrder());
        searchPQ.insert(root);
        
        SearchNode rootTwin = new SearchNode(initial.twin());
        rootTwin.prev = null;
        twinPQ = new MinPQ<SearchNode>(root.priorityOrder());
        twinPQ.insert(rootTwin);
        
        
        while (!searchPQ.min().board.isGoal() && !twinPQ.min().board.isGoal()) { 
            makeSearch(searchPQ);
            makeSearch(twinPQ);
        }
        
        if (searchPQ.min().board.isGoal()) { 
            solutionNode = searchPQ.delMin();
            moves = solutionNode.moves;
            generateSolution();
        }
        else { 
            moves = -1;
            solvable = false;
        }
    }
    
    private class SearchNode {
        private Board board;
        private int moves;
        private int priority;
        private SearchNode prev;
        private int manhattan;
        
        public SearchNode(Board a) {
            board = a;
            moves = Solver.this.moves;
            manhattan = a.manhattan();
            priority = manhattan + moves;
        }
        
        public Comparator<SearchNode> priorityOrder() { return new ByPriority(); }
    
        private class ByPriority implements Comparator<SearchNode> {
            public int compare(SearchNode a, SearchNode b){
                if (a.priority > b.priority) { return 1; }
                else if (a.priority < b.priority) { return -1; }
                else {
                    if (a.manhattan > b.manhattan) { return 1; }
                    else if (a.manhattan < b.manhattan) { return -1; }
                }
                return 0;
            }          
        }
    }
    
    private void makeSearch(MinPQ<SearchNode> pq) {        
        SearchNode min = pq.delMin();
        moves = min.moves + 1;
        
        for (Board neighbor : min.board.neighbors()) {
            if (min.prev != null && min.prev.board.equals(neighbor)) { continue; }
            SearchNode newNeighbor = new SearchNode(neighbor);
            newNeighbor.prev = min;
            pq.insert(newNeighbor); 
        }
    }
    
       
    public boolean isSolvable() {
        return solvable;
    }
    
    public int moves() {
        return moves;
    }
    
    private void generateSolution() {
        SearchNode current = solutionNode;
        while (current != null) {
            solutionD.addFirst(current.board);
            current = current.prev;
        }
    }
    
    public Iterable<Board> solution() {
        if (!isSolvable()) { return null; }
        
        return solutionD;
    }
    
    public static void main(String[] args) {
         // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}