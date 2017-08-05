import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private int size;
    private Node root;

    
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        private boolean vertical;
        
        public Node(Point2D p, RectHV rect, boolean vertical) {
            this.p = p;
            this.rect = rect;
            this.vertical = vertical;            
        }
    }
        
    public KdTree() {
    }
    
    public boolean isEmpty() {
        return size() == 0;
    } 
    
    public int size() {
        return size;
    }                      
    
    public void insert(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }
                
        root = insert(root, p, 0, null, false);
    }             
    
    private Node insert(Node x, Point2D p, int level, Node prev, boolean left) {
        if (root == null) { 
            RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            size++;
            return new Node(p, rect, true);            
        }
            
        if (x == null) { 
            size++;
            RectHV rect;
            RectHV prevRect = prev.rect;
            boolean vertical = false;
            if (left) {
                if (level % 2 == 0) { 
                    vertical = true;
                    rect = new RectHV(prevRect.xmin(), prevRect.ymin(), prevRect.xmax(), prev.p.y());
                } else {
                    rect = new RectHV(prevRect.xmin(), prevRect.ymin(), prev.p.x(), prevRect.ymax());                    
                }
            } else {
                if (level % 2 == 0) {
                    vertical = true;
                    rect = new RectHV(prevRect.xmin(), prev.p.y(), prevRect.xmax(), prevRect.ymax());
                } else {
                    rect = new RectHV(prev.p.x(), prevRect.ymin(), prevRect.xmax(), prevRect.ymax());
                }
            }
            Node newN = new Node(p, rect, vertical);
            return newN;
        }
        double cmp;
        if (level % 2 == 0) { cmp = p.x() - x.p.x(); }
        else { cmp = p.y() - x.p.y(); }
        if (cmp < 0.0) { 
            x.lb = insert(x.lb, p, level + 1, x, true); 
        }
        else if (cmp > 0.0) { 
            x.rt = insert(x.rt, p, level + 1, x, false); 
        }
        else {
            if (p.compareTo(x.p) != 0.0) {
                x.rt = insert(x.rt, p, level + 1, x, false);
            }
        }
        
        return x;
    }
    
    
    public boolean contains(Point2D p) {
        if (p == null) { throw new java.lang.IllegalArgumentException(); }
        
        return contains(root, p, 0);
    }           
    
    private boolean contains(Node x, Point2D p, int level) {
        if (x == null) return false;
        double cmp;
        if (level % 2 == 0) { cmp = p.x() - x.p.x(); }
        else { cmp = p.y() - x.p.y(); }
        if (cmp < 0) { return contains(x.lb, p, level + 1); }
        else if (cmp > 0) { return contains(x.rt, p, level + 1); }  
        else {
            if (p.compareTo(x.p) == 0) { return true; }
            return contains(x.rt, p, level + 1); 
        }
    
    }
    
    public void draw() {        
        StdDraw.setPenRadius(0.01);
        for (Node node : nodes()) {
            StdDraw.setPenColor(StdDraw.BLACK);
            node.p.draw();            
            if (node.vertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
        }
    }
    
    private Iterable<Node> nodes() {
        Queue<Node> queue = new Queue<Node>();
        nodes(root, queue);
        return queue;
    }
    
    private void nodes(Node x, Queue<Node> queue) {
        if (x == null) return;
        queue.enqueue(x);
        nodes(x.lb, queue);
        nodes(x.rt, queue);
    }
    
    public Iterable<Point2D> range(RectHV rect) { 
        if (rect == null) { throw new java.lang.IllegalArgumentException(); }
        Queue<Point2D> queue = new Queue<Point2D>();
        range(rect, root, queue);
        return queue;
    }          
    
    private void range(RectHV rect, Node x, Queue<Point2D> queue) {
        if (x == null) { return; }
        if (x.rect.intersects(rect)) {
            if (rect.contains(x.p)) { queue.enqueue(x.p); }
            range(rect, x.lb, queue);
            range(rect, x.rt, queue);
        }
        else { return; }
    }
    
    public Point2D nearest(Point2D p) {  
        if (p == null) { throw new java.lang.IllegalArgumentException(); }
        Point2D closest = null; 
        closest = nearest(p, root, closest, 0);
        return closest;
    }           
    
    private Point2D nearest(Point2D p, Node x, Point2D closest, int level) {
        double distance = 0.0;
        if (x == null) { return closest; }
        if (closest != null) { distance = closest.distanceTo(p); }
        if (closest != null && x.rect.distanceTo(p) > distance) { 
            return closest; 
        }
        if (closest == null || x.p.distanceTo(p) < distance) {        
            closest = x.p;
            distance = x.p.distanceTo(p);          
        }    
       
        double cmp;
        if (level % 2 == 0) { cmp = p.x() - x.p.x(); }
        else { cmp = p.y() - x.p.y(); }         
        if (cmp < 0) {               
           closest = nearest(p, x.lb, closest, level + 1);          
           closest = nearest(p, x.rt, closest, level + 1);
           
        } else {         
           closest = nearest(p, x.rt, closest, level + 1);         
           closest = nearest(p, x.lb, closest, level + 1);
           
        }
        return closest;
    }
    
    public static void main(String[] args) {
       In in = new In(args[0]);
       KdTree kdt = new KdTree();
       while(!in.isEmpty()) {
           double x = in.readDouble();
           double y = in.readDouble();
           Point2D point = new Point2D(x, y);
           kdt.insert(point);
       }
       kdt.draw();
       StdOut.println("Range test");
       RectHV rect = new RectHV(0.0, 0.0, 0.5, 0.5);
        for (Point2D p : kdt.range(rect)) {
            StdOut.println(p.toString());
        }
       StdOut.println("Nearest Neighbor test");
       Point2D query = new Point2D(0.8, 0.7);
       query.draw();
       StdOut.println(kdt.nearest(query).toString());
    
    }                 
    
}