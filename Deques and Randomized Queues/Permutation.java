import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rQ = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rQ.enqueue(s);
        }
        
        for (int i = 0; i < k; i++) { StdOut.println(rQ.dequeue()); }
    }        
}