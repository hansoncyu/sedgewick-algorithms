import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last;
    private int size;
    
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }
    
    public boolean isEmpty() {
        return first == null;
    }
    
    public int size() {
        return size;
    }
        
    public void addFirst(Item item) {
        if (item == null) { throw new java.lang.IllegalArgumentException(); }
        
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (oldFirst == null) { last = first; }
        else { oldFirst.prev = first; }
        size++;
    }
    
    public void addLast(Item item) {
        if (item == null) { throw new java.lang.IllegalArgumentException(); }
        
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (isEmpty()) { first = last; }
        else { oldLast.next = last; }
        size++;
    }
    
    public Item removeFirst() {
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        
        Item item = first.item;
        first = first.next;
        if (isEmpty()) { last = null; }
        else { first.prev = null; }
        size--;
        return item;
    }
    
    public Item removeLast() {
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        
        Item item = last.item;
        last = last.prev;
        if (last == null) { first = null; }
        else { last.next = null; }
        size--;
        return item;
    }
    
    public Iterator<Item> iterator() { return new ListIterator(); }
    
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() { return current != null; }
        
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
        
        public Item next() {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    public static void main(String[] args) {
        Deque<Object> dQ = new Deque<Object>();
        for (int i = 0; i < 25; i++) {
            dQ.addLast(i);
        }
        for (int i = 0; i < 20; i++) {
            dQ.addFirst(i);
        }
        for (Object i : dQ) { StdOut.println(i); }
        StdOut.println("-------------");
        
        for (int i = 0; i < 5; i++) { dQ.removeLast(); }
        for (Object i : dQ) { StdOut.println(i); }
        StdOut.println("-------------");
        for (int i = 0; i < 10; i++) { dQ.removeFirst(); }
        for (Object i : dQ) { StdOut.println(i); }
    }
}