import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {    
    private int numTrials;
    private int[] opened;
    private int size;
    
    
    public PercolationStats(int n, int trials) {
        if (trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        
        numTrials = trials;
        size = n*n;
        opened = new int[trials];
        int randomRow;
        int randomCol;
        Percolation perc;
        for (int i = 0; i < trials; i++) {
            perc = new Percolation(n);
            while (!perc.percolates()) {
                randomRow = StdRandom.uniform(1, n+1);
                randomCol = StdRandom.uniform(1, n+1);
                perc.open(randomRow, randomCol);
            }
            
            opened[i] = perc.numberOfOpenSites();
        }
                
    }    
    
    public double mean() {
        return StdStats.mean(opened)/size;
    }
    
    public double stddev() {
        return StdStats.stddev(opened)/size;
    }
    
    public double confidenceLo() {
        return (mean() - (1.96*stddev()/Math.sqrt(numTrials)));
    }
    
    public double confidenceHi() {
        return (mean() + (1.96*stddev()/Math.sqrt(numTrials)));
    }
    
    
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.println("mean = " + percStats.mean());
        StdOut.println("stddev = " + percStats.stddev());
        StdOut.println("95% confidence interval = " + percStats.confidenceLo() + ", " + percStats.confidenceHi());
    }
}