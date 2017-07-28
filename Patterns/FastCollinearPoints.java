import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {    
    private int number; // number of line segments
    private Stack<LineSegment> lineSegments; // stack of line segments
    private Stack<Point[]> uniquePoints; // check start and end
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) { throw new java.lang.IllegalArgumentException(); }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) { throw new java.lang.IllegalArgumentException(); }
        }
        number = 0;
        int N = points.length;
        lineSegments = new Stack<LineSegment>();
        uniquePoints = new Stack<Point[]>();
        Point[] copy = new Point[N-1];
        for (int i = 0; i < N; i++) {
            int k = 0;
            for (int j = 0; j < N; j++) {
                if (j == i) { continue; }
                copy[k++] = points[j];
            }
            Arrays.sort(copy);
            Arrays.sort(copy, points[i].slopeOrder());
            findMaxSegments(points[i], copy);
        }
        
    }

    private void findMaxSegments(Point a, Point[] points) {
        int lo = 0;
        int hi = 0;
        int counter = 1;
        double currentSlope = 0.0;
        for (int i = 0; i < points.length; i++) {
            Double slope = a.slopeTo(points[i]);
            if (slope == currentSlope) {                
                hi = i;
                if (i == 0) { continue; }
                counter++;                
            }  
            if (i == points.length - 1 || slope != currentSlope) {
                if (counter >= 3) {   
                    Point start, end;
                    if (a.compareTo(points[lo]) == 1 && a.compareTo(points[hi]) == -1) {
                        start = points[lo];
                        end = points[hi];    
                    }
                    else if (a.compareTo(points[lo]) == -1) {
                        start = a;
                        end = points[hi];
                    }
                    else { 
                        start = points[lo];
                        end = a;
                    }
                    pushUniqueSegment(start, end);
                }
                counter = 1;
                currentSlope = slope;
                lo = i;
                hi = i;
            }                  
        }
    }
    
    private void pushUniqueSegment(Point lo, Point hi) {
        for (Point[] pointPair : uniquePoints) {            
            if (lo.compareTo(pointPair[0]) == 0 && hi.compareTo(pointPair[1]) == 0) { return ; }
        }
        number++;
        Point[] newPair = {lo, hi};
        uniquePoints.push(newPair);
        LineSegment line = new LineSegment(lo, hi);
        lineSegments.push(line);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}