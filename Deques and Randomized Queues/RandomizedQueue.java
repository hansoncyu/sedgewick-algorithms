import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arr;
    private int size;
    private int N;
    
    public RandomizedQueue() {
        arr = (Item[]) new Object[1];
        size = 0;
        N = 0;
    }
    
    public boolean isEmpty() { return size == 0; }
    
    public int size() { return size; }
    
    private void resize(int capacity) {
        Item[] newArr = (Item[]) new Object[capacity];
        int a = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                newArr[a] = arr[i];
                a++;
            }
        }
        arr = newArr;
        N = size;
    }    
    
    public void enqueue(Item item) {
        if (item == null) { throw new java.lang.IllegalArgumentException(); }
        if (N == arr.length) { resize(2 * arr.length); }
        arr[N++] = item;
        size++;
    }
    
    public Item dequeue() {
        if (size == 0) { throw new java.util.NoSuchElementException(); }
        int rand = StdRandom.uniform(0, N);
        while (arr[rand] == null) {
            rand = StdRandom.uniform(0, N);
        }
        Item item = arr[rand];
        arr[rand] = null;
        size--;
        if (size > 0 && size == arr.length/4) { resize(arr.length/2); }        
        return item;
    }
    
    public Item sample() {
        if (size == 0) { throw new java.util.NoSuchElementException(); }
        int rand = StdRandom.uniform(0, N);
        while (arr[rand] == null) {
            rand = StdRandom.uniform(0, N);
        }
        return arr[rand];
    }
    
    public Iterator<Item> iterator() { return new ListIterator(); }
    
    private class ListIterator implements Iterator<Item> {
        private int current;
        private Item[] arrIter;
        
        public ListIterator() {
            current = 0;
            arrIter = (Item[]) new Object[size];
            int a = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != null) {
                    arrIter[a] = arr[i];
                    a++;
                }
            }
            StdRandom.shuffle(arrIter);
        }
        
        public boolean hasNext() { return (current < arrIter.length); }
        
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
        
        public Item next() {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }
            return arrIter[current++];
        }
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Object> rQ = new RandomizedQueue<Object>();
        for (int i = 0; i < 10; i++) { rQ.enqueue(i); }
        for (Object s : rQ) { StdOut.println(s); }
        StdOut.println("-------------");
        for (int i = 0; i < 4; i++) { rQ.dequeue(); }
        for (Object s : rQ) { StdOut.println(s); }        
    }
        
}