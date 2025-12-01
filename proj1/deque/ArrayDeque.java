package deque;

// TODO: Update the logic of get.
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
        double usageRate = (double) size / items.length;
        if (usageRate < 0.25 && items.length >= 16) {
            resizeDeque((int) (items.length / 2));
        } else if (isSpilled()) {
            resizeDeque(items.length * 2);
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
        checkResize();

        items[nextFirst] = item;

        // Change the parameters.
        nextFirst = minusOne(nextFirst);
        size++;
    }

    /** Add item at the place of nextLast */
    public void addLast(T item) {
        checkResize();

        items[nextLast] = item;

        // Change the parameters.
        nextLast = plusOne(nextLast); 
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        // Initiallize a pointer p.
        int printedIndex = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[printedIndex]);
            System.out.print(" ");
            // Update the pointer to keep circuit.
            printedIndex = plusOne(printedIndex);
        }
        System.out.println();
    }

    public T removeFirst() {
        int firstIndex = plusOne(nextFirst);
        T returnItem = items[firstIndex];
        items[firstIndex] = null;

        nextFirst = plusOne(nextFirst);
        size--;
        checkResize();

        return returnItem;
    }

    public T removeLast() {
        int lastIndex = minusOne(nextLast);
        T returnItem = items[lastIndex];
        items[lastIndex] = null;

        nextLast = minusOne(nextLast);
        size--;
        checkResize();
        return returnItem;
    }

    public T get(int index) {
        int circuitIndex = plusOne(nextFirst);
        for (int i = 0; i < index; i++) {
            circuitIndex = plusOne(circuitIndex);
        }
        return items[circuitIndex];
    }

    public static main(String[] args) {
        ArrayDeque<Integer>[] test = new ArrayDeque<>(); 
    }
}
