package deque; 

// TODO: public Iterator<T> iterator()

// TODO: public boolean equals(Object o)

public class LinkedListDeque<T> {
    /** Initiallizing nested class. */
    public class Node{
        public Node prev;
        public T item;
        public Node next;

        // DLList.
        public Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    public Node sentinel;
    public int size;

    /** Create a LinkedListDeque WITHOUT ANY T item. */
    public LinkedListDeque() {
        // sentinel <-> sentinel
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /** Create a LinkedListDeque with a single T item. */
    public LinkedListDeque(T item) {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;

        // Create first Node.
        sentinel.next = new Node(sentinel, item, sentinel);
        // Make a circuit pointer.  CIRCUIT <- sentinel <-> 01 <-> CIRCUIT
        sentinel.next.prev = sentinel;
        sentinel.next.next = sentinel;
        sentinel.prev = sentinel.next;
        size++;
    }

    /** Add a Node with T item at sentinel.next, which is also the FIRST of the Deque. */
    public void addFirst(T item) {
        // Create an new Node.
        sentinel.next = new Node(sentinel, item, sentinel.next);
        // Update pointer. Keep it still circuit. Let new FirstNode <- old FirstNode.
        sentinel.next.next.prev = sentinel.next;
        size++;
    }

    /** Add a Node with T item at sentinel.prev, which is also the LAST of the Deque. */
    public void addLast(T item) {
        // Create an new Node.
        sentinel.prev = new Node(sentinel.prev, item, sentinel);
        // Update pointer. Keep it still circuit. Let sentinel -> new LastNode.
        sentinel.prev.prev.next = sentinel.prev;
        size++;
    }

    /** Return if the Deque is empty. */
    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    /**Return the size of the Deque. */
    public int size() {
        return size;
    }

    /** Print the current Deque. */
    public void printDeque() {
        for (Node p = sentinel.next; p != sentinel; p = p.next) {
            System.out.print(p.item);
            System.out.print(" ");
        }
        System.out.println();
    }

    /** Remove the first Node form the Deque. */
    public T removeFirst() {
        T currentItem = sentinel.next.item;

        // Delete the pointer start form the removed target in order to save memory.
        // Delete the Node from the Deque.
        sentinel.next = sentinel.next.next;
        sentinel.next.prev.prev = null;
        sentinel.next.prev.next = null;
        sentinel.next.prev = sentinel;

        if (sentinel.next == sentinel) {
            return null;
        }
        return currentItem;
    }

    /** Remove the first Node form the Deque. */
    public T removeLast() {
        T currentItem = sentinel.prev.item;

        // Delete the pointer start form the removed target in order to save memory.
        // Delete the Node from the Deque.
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next.next = null;
        sentinel.prev.next.prev = null;
        sentinel.prev.next = sentinel;

        if (sentinel.next == sentinel) {
            return null;
        }
        return currentItem;
    }
    /** A helper method which return the indexth item. */
    private T get(int index, Node p) {
        if (sentinel.next == sentinel || p == sentinel) {
            return null;
        }

        if (index == 0) {
            return p.item;
        }
        return get(index - 1, p.next);
    }

    /** Return the indexth item. */
    public T get(int index) {
        return get(index, sentinel.next);
    }

    @Override
    public String toString() {
        if (sentinel.next == sentinel) {
            return "EMPTY CIRCLE";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(sentinel.prev.item).append(" circuit) <-> ");
        Node p = sentinel.next;
        while (p != sentinel) {
            sb.append(p.item).append(" <-> ");
            p = p.next;
        }
        sb.append("(").append(sentinel.next.item).append(" circuit)");
        return sb.toString();
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> test = new LinkedListDeque<>(10);
        test.addFirst(9);
        test.addFirst(8);
        test.addFirst(7);
        test.addLast(11);
        test.addLast(12);
        test.addLast(13);
        test.get(0);
        test.get(1);
        test.get(2);
        test.get(3);
        System.out.println(test);
        System.out.println(test.size());
        System.out.println(test.isEmpty());
        test.printDeque();
        int test0 = test.removeFirst();
        int test1 = test.removeLast();
        int test2 = test.removeLast();
        System.out.println(test0);
        System.out.println(test1);
        System.out.println(test2);
        System.out.println(test);
        int test3 = test.removeFirst();
        System.out.println(test3);
        System.out.println(test);
        System.out.println(test.get(0));
    }

}