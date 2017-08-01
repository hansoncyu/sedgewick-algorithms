import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// Object represents n by n board for use in the 8-puzzle problem

public class Board {
    
    private final int[] board;
    private final int N; // size of puzzle
    private int blank; // index of blank space
    private int ham; // Hamming priority. Don't calculate again when not 0
    private int man; // Manhattan priority
    private Stack<Board> neighbors;
    
    
    public Board(int[][] blocks) {
        ham = -1;
        man = -1;
        neighbors = new Stack<Board>();
        N = blocks.length;
        board = new int[N*N];
        for (int i = 0; i < N; i++) {
            for (int j =0; j < N; j++) {
                int index = i * N + j;
                board[index] = blocks[i][j];
                if (blocks[i][j] == 0) { blank = index; } // save position of blank space
            }
        }
    }
    
    public int dimension() {
        return N;
    }
    
    public int hamming() {
        if (ham == -1) {
            int temp = 0;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 0) { continue; }
                int properBlock = i + 1;
                if (board[i] != properBlock) { temp++; }
            }
            
            ham = temp;
        }
        
        return ham;
    }
    
    public int manhattan() {
        if (man == -1) {
            int temp = 0;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 0) { continue; }
                int rowDiff = Math.abs(((board[i] - 1) / N) - (i / N));
                int colDiff = Math.abs(((board[i] - 1) % N) - (i % N));                
                temp += rowDiff + colDiff;
            }
            
            man = temp;
        }
        
        return man;
    }
    
    public boolean isGoal() {
        return (manhattan() == 0);
    }
    
    private int[][] oneDToTwoD(int[] board) {
        int[][] twoD = new int[N][N];
        for (int i = 0; i < board.length; i++) {
            int row = i / N;
            int col = i % N;
            twoD[row][col] = board[i];
        }
        
        return twoD;
    }
    
    private void swap(int[] arr, int x, int y) {
        int swap = arr[x];
        arr[x] = arr[y];
        arr[y] = swap;
    }
    
    public Board twin() {
        int[] arrCopy = Arrays.copyOf(board, board.length);
        
        if (arrCopy[0] != 0 && arrCopy[1] != 0) {
            swap(arrCopy, 0, 1);
        } else {
            swap(arrCopy, 2, 3);
        }
        
        Board twinBoard = new Board(oneDToTwoD(arrCopy));
        return twinBoard;
    }
    
    public boolean equals(Object y) {
        if (this == y) { return true; }
        if (y == null) { return false; }
        if (this.getClass() != y.getClass()) { return false; }
        Board that = (Board) y;
        if (!Arrays.equals(this.board, that.board)) { return false; }
        return true; 
    }
    
    private void generateNeighbors() {
        int row = blank / N;
        int col = blank % N;
        int[] arrCopy = Arrays.copyOf(board, board.length);
        Board tempBoard;
        // do swaps and swap back to avoid making multiple array copies
        if (row != 0) {
            swap(arrCopy, blank, blank - N);
            tempBoard = new Board(oneDToTwoD(arrCopy));
            neighbors.push(tempBoard);
            swap(arrCopy, blank, blank - N);
        }
        if (col != 0) {
            swap(arrCopy, blank, blank - 1);
            tempBoard = new Board(oneDToTwoD(arrCopy));
            neighbors.push(tempBoard);
            swap(arrCopy, blank, blank - 1);
        }
        if (row != N -1) {
            swap(arrCopy, blank, blank + N);
            tempBoard = new Board(oneDToTwoD(arrCopy));
            neighbors.push(tempBoard);
            swap(arrCopy, blank, blank + N);
        }
        if (col != N - 1) {
            swap(arrCopy, blank, blank + 1);
            tempBoard = new Board(oneDToTwoD(arrCopy));
            neighbors.push(tempBoard);
            swap(arrCopy, blank, blank + 1);
        }
    }
    
    public Iterable<Board> neighbors() {
        if (neighbors.empty()) {
            generateNeighbors();
        }
        
        return neighbors;    
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < board.length; i++) {
            if (i > 0 && i % N == 0) { s.append("\n"); }
            s.append(String.format("%2d ", board[i]));            
        }
        
        return s.toString();
    }
    
    public static void main(String[] args) {
        int N = StdIn.readInt();
        int[][] twoD = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                twoD[i][j] = StdIn.readInt();
            }
        }
        
        Board testBoard = new Board(twoD);
        StdOut.println("Dimension = " + testBoard.dimension());
        StdOut.println("Hamming = " + testBoard.hamming());
        StdOut.println("Manhattan = " + testBoard.manhattan());
        StdOut.println("Goal = " + testBoard.isGoal());
        StdOut.println(testBoard.toString());
        StdOut.println("-------------------------------");
        StdOut.println("Twin:");
        StdOut.println(testBoard.twin().toString());
        StdOut.println("-------------------------------");
        StdOut.println("Neighbors:");
        for (Board neighbor : testBoard.neighbors()) {
            StdOut.println(neighbor.toString());
        }
    }
}