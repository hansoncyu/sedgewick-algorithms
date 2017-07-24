import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size; // number of sites in each row or column
    private boolean[] opened; // value is true if opened
    private int openedCount; // number of open sites
    private int virtualTop;  // check percolation by seeing of virtualTop and bottom are connected
    private int virtualBottom;
    private WeightedQuickUnionUF wQU;
    
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        
        size = n;
        opened = new boolean[n * n + 2];
        virtualTop = xyTo1D(n, n) + 1;
        virtualBottom = xyTo1D(n, n) + 2;
        // add 2 extra virtual sites that will be connected to top row and bottom row
        wQU = new WeightedQuickUnionUF(n * n + 2);        
        int topRow, bottomRow;
        for (int i = 1; i < n+1; i++) {
            topRow = xyTo1D(1, i);
            bottomRow = xyTo1D(n, i);
            wQU.union(virtualTop, topRow);
            wQU.union(virtualBottom, bottomRow);
        }
    }
    
    private int xyTo1D(int x, int y) {        
        // subtract x an y by one to convert to zero-based array
        x--;
        y--;
        return x * size + y;
    }
    
    public void open(int row, int col) {
        if (row == 0 || col == 0 || row > size || col > size) {
            throw new java.lang.IllegalArgumentException();
        }
        
        int oneD = xyTo1D(row , col);
        if (opened[oneD]) {return ;}
        opened[oneD] = true;
        openedCount++;
        int left = xyTo1D(row, col-1);
        int right = xyTo1D(row, col+1);
        int above = xyTo1D(row-1, col);
        int below = xyTo1D(row+1, col);
        if (col - 1 > 0) {            
            if (opened[left]) {wQU.union(oneD, left);}
            
        }            
        if (col + 1 < size+1) {            
            if (opened[right]) {wQU.union(oneD, right);}
            
        }   
        if (row - 1 > 0) {            
            if (opened[above]) {wQU.union(oneD, above);}
            
        }   
        if (row + 1 < size+1) {            
            if (opened[below]) {wQU.union(oneD, below);}
            
        }   
    }
    
    public boolean isOpen(int row, int col) {
        if (row == 0 || col == 0 || row > size || col > size) {
            throw new java.lang.IllegalArgumentException();
        }
        
        int oneD = xyTo1D(row, col);
        return opened[oneD];        
    }
    
    public boolean isFull(int row, int col) {
        if (row == 0 || col == 0 || row > size || col > size) {
            throw new java.lang.IllegalArgumentException();
        }
        
        int oneD = xyTo1D(row, col);
        return (wQU.connected(oneD, virtualTop) && opened[oneD]);  
    }
    
    public int numberOfOpenSites() {
        return openedCount;
    }
    
    public boolean percolates() {
        return wQU.connected(virtualTop, virtualBottom);
    }
    
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation perc = new Percolation(n);
        
        // read x, y pairs from standard input
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            perc.open(row, col);                     
        }
        if (perc.percolates()) {StdOut.println("System Percolates");}
        else {StdOut.println("System Doesn't Percolate");}
    }
}
    