package deque;

// TODO: Update all the index without size but nextFirst and nextLast.
public class ArrayDeque<T> {
    private T[] items;  // Pointer which points the address of the ArrayDeque.
    private int size;  // The size of the ArrayDeque.
    private int nextFirst;  // The place to implement with concerning all with FIRST.
    private int nextLast;  // The place to implement with concerning all with LAST.

    /** Initiallize the ArrayDeque */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = plusOne(nextFirst); // nextLast = 1.
    }

    /** Return if the Deque will spilled. */
    private boolean isSpilled() {
        return size == items.length;
    }

    /** Check and implement resizeDeque if usageRate is lower than 0.25 
     *  or will be spilled after adding item. */
    private void checkResize() {
        double usageRate = size / items.length;
        if (usageRate < 0.25) {
            resizeDeque((int) (size / 2));
        } else if (isSpilled()) {
            resizeDeque(size * 2);
        }
    }

    /** Resize the ArrayDeque into the size of capacity. */
    private void resizeDeque(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        // Copy the ArrayDeque twice.
        // First from nextFirst + 1 to the end. Length to copy is items.length - nextFirst - 1. 
        System.arraycopy(items, nextFirst + 1, temp, 0, items.length - nextFirst - 1);
        // Second from the start to the nextLast - 1. Length to copy is nextLast.
        System.arraycopy(items, 0, temp, items.length - nextFirst - 1, nextLast);
        items = temp;

        // Change the parameters.
        nextFirst = items.length - 1;
        nextLast = size;
    }
    
    /** Return the index of the circuit ArrayDeque when plusing. */
    private int plusOne(int index) {
        index++;
        if (index >= items.length) {
            index = 0;
        }
        return index;
    }

    /** Return the index of the circuit ArrayDeque when minusing. */
    private int minusOne(int index) {
        index--;
        if (index < 0) {
            index = items.length - 1;
        }
        return index;
    }

    /** Add item at the place of nextFirst */
    public void addFirst(T item) {
        items[nextFirst] = item;

        // Change the parameters.
        nextFirst = minusOne(nextFirst);
        size++;
    }

    /** Add item at the place of nextLast */
    public void addLast(T item) {
        items[nextLast] = item;

        // Change the parameters.
        nextLast = plusOne(nextLast); 
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[i]);
            System.out.print(" ");
        }
        System.out.println();
    }

    public T removeFirst() {

        size--;
    }

    public T removeLast() {
        T returnItem = items[size];
        items[size] = null;
        size--;
        return returnItem;
    }

    public T get(int index) {
        return items[];
    }

    public static main(String[] args) {
        ArrayDeque<Integer>[] test = new ArrayDeque<>(); 
    }
}
