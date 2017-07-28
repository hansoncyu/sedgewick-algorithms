import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private int number; // number of line segments
    private Stack<LineSegment> lineSegments; // stack of line segments
    
    public BruteCollinearPoints(Point[] points) {
        if (points == null) { throw new java.lang.IllegalArgumentException(); }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) { throw new java.lang.IllegalArgumentException();}    
        }        
        number = 0;
        int N = points.length;
        lineSegments = new Stack<LineSegment>();
        Point[] subArr = new Point[4];
        for (int i = 0; i < N; i++) {
            if (i < N - 1) { 
                if (points[i].compareTo(points[i+1]) == 0) {throw new java.lang.IllegalArgumentException(); }
            }
            for (int j = i + 1; j < N; j++) {                
                for (int k = j + 1; k < N; k++) { 
                    for (int m = k + 1; m < N; m++) {                        
                        subArr[0] = points[i];
                        subArr[1] = points[j];
                        subArr[2] = points[k];
                        subArr[3] = points[m];
                        Arrays.sort(subArr);
                        if (collinear(subArr)) {
                            LineSegment line = new LineSegment(subArr[0], subArr[3]);
                            lineSegments.push(line);
                            number++;
                        }
                    }
                }
            }
        }
    }
    

    
    private boolean collinear(Point[] points) {
        double slope1 = points[0].slopeTo(points[1]);
        double slope2 = points[1].slopeTo(points[2]);
        double slope3 = points[2].slopeTo(points[3]);
        if (slope1 == slope2 && slope2 == slope3) { return true; }
        return false;
    }
    
    public int numberOfSegments() { return number; }
    
    public LineSegment[] segments() { return lineSegments.toArray(new LineSegment[lineSegments.size()]); }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}