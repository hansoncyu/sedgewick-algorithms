import java.util.TreeSet;
import java.util.LinkedList;
import java.util.NavigableSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private TreeSet<Point2D> rbBST;
    
    
    public PointSET() {
        rbBST = new TreeSet<Point2D>();
    }
    
    public boolean isEmpty() {
        return rbBST.isEmpty();
    } 
    
    public int size() {
        return rbBST.size();
    }                      
    
    public void insert(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }
        
        rbBST.add(p);
    }             
    
    public boolean contains(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }
        
        return rbBST.contains(p);
    }           
    
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : rbBST) {
            point.draw();
        }
    }                         
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) { throw new java.lang.IllegalArgumentException(); }
        
        LinkedList<Point2D> points = new LinkedList<Point2D>();
        Point2D ceilingP = new Point2D(rect.xmin(), rect.ymin());
        Point2D floorP = new Point2D(rect.xmax(), rect.ymax());
        int cmp = ceilingP.compareTo(floorP);
        Point2D smallestP = cmp < 0 ? rbBST.ceiling(ceilingP) : rbBST.ceiling(floorP);
        Point2D biggestP = cmp < 0 ? rbBST.floor(floorP) : rbBST.ceiling(ceilingP);
        if (smallestP == null || biggestP == null) { return points; }
        NavigableSet<Point2D> subset = rbBST.subSet(smallestP, true, biggestP, true);        
        for (Point2D p : subset) {
            if (rect.contains(p)) { points.add(p); }
        }
        
        return points;
    }          
    
    public Point2D nearest(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }
        
        double minDistance = 0.0;
        Point2D nearestP = null;
        for (Point2D point : rbBST) {
            double distance = point.distanceTo(p);
            if (nearestP == null) {
                nearestP = point;
                minDistance = distance;
            }
            
            if (distance < minDistance) { 
                nearestP = point;
                minDistance = distance;
            }            
        }
            
        return nearestP;
    }           
    
    public static void main(String[] args) {
       In in = new In(args[0]);
       PointSET ps = new PointSET();
       while(!in.isEmpty()) {
           double x = in.readDouble();
           double y = in.readDouble();
           Point2D point = new Point2D(x, y);
           ps.insert(point);
       }
       ps.draw();
       StdOut.println("Range test");
       RectHV rect = new RectHV(0.0, 0.0, 0.0, 0.0);
        for (Point2D p : ps.range(rect)) {
            StdOut.println(p.toString());
        }
       StdOut.println("Nearest Neighbor test");
       Point2D query = new Point2D(0.8, 0.7);
       query.draw();
       StdOut.println(ps.nearest(query).toString());
    }                 
    
}