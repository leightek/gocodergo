package leightek.datastructure.pq;

import java.util.*;

/**
 * A min priority queue implementation using a binary heap and a map
 */
public class PQueue <T extends Comparable<T>> {

    private int heapSize = 0; // The number of elements currently inside the heap
    private int heapCapacity = 0; // The internal capacity of the heap
    private List<T> heap = null; // A dynamic list to track the elements inside the heap
    private Map<T, TreeSet<Integer>> map = new HashMap<>(); // A map to track the indices of a node value found
                                                            // in the heap

    public PQueue() {
        this(1);
    }

    public PQueue(int size) {
        heap = new ArrayList<>(size);
    }

    // Construct a priority queue using Heapify, O(n)
    public PQueue(T[] elems) {

        heapSize = heapCapacity = elems.length;
        heap = new ArrayList<T>(heapCapacity);

        for (int i = 0; i < heapSize; i++) {
            mapAdd(elems[i], i);
            heap.add(elems[i]);
        }

        // Heapify process
        for (int i = Math.max(0, heapSize / 2 - 1); i >= 0; i--)
            sink(i);
    }

    // General construction, O(nlog(n))
    public PQueue(Collection<T> elems) {
        this(elems.size());
        for (T elem: elems)
            add(elem);
    }

    public boolean isEmpty() {
        return heapSize == 0;
    }

    public void clear() {
        for (int i = 0; i < heapCapacity; i++)
            heap.set(i, null);
        heapSize = 0;
        map.clear();
    }

    public int size() {
        return heapSize;
    }

    /**
     *
     * @return the value of the element with the lowest priority
     */
    public T peek() {
        if (isEmpty())
            return null;

        return heap.get(0);
    }

    /**
     * Removes the root of the heap, O(log(n))
     * @return the value of the root
     */
    public T poll() {
        return removeAt(0);
    }

    /**
     * Test if the element is in heap, O(1)
     * @param elem
     * @return true if the element is in the heap, or else false
     */
    public boolean contains(T elem) {
        if (elem == null)
            return false;
        return map.containsKey(elem);

        // if not use map, O(n)
//        for (int i = 0; i < heapSize; i++) {
//            if (heap.get(i).equals(elem))
//                return true;
//        }
//        return false;
    }

    /**
     * Add an element to the heap, the element must not be null, O(log(n))
     * @param elem
     */
    public void add(T elem) {
        if (elem == null)
            throw new IllegalArgumentException();

        if (heapSize < heapCapacity) {
            heap.set(heapSize, elem);
        } else {
            heap.add(elem);
            heapCapacity++;
        }
        mapAdd(elem, heapSize);

        swim(heapSize);
        heapSize++;
    }

    /**
     * Tests if the value of node i <= node j, i & j are valid indices, O(1)
     * @param i
     * @param j
     * @return true if the value of node i <= node j, or else false
     */
    private boolean less(int i, int j) {

        T node1 = heap.get(i);
        T node2 = heap.get(j);
        return node1.compareTo(node2) <= 0;
    }

    /**
     * Bottom up node swim, O(log(n))
     * @param k
     */
    private void swim(int k) {

        int parent = (k - 1) / 2;
        while (k > 0 && less(k, parent)) {
            swap(parent, k);
            k = parent;
            parent = (k - 1) / 2;
        }
    }

    /**
     * Top down node sink, O(log(n))
     * @param k
     */
    private void sink(int k) {

        while (true) {
            int left = 2 * k + 1; // Left node
            int right = 2 * k + 2; // Right node
            int smallest = left; // Assume left is the smallest

            if (right < heapSize && less(right, left)) // Right node is the smallest
                smallest = right;

            if (left >= heapSize || less(k, smallest))
                break;

            swap(smallest, k);
            k = smallest;
        }
    }

    /**
     * Swap tow nodes, i & j are valid indices, O(1)
     * @param i
     * @param j
     */
    private void swap(int i, int j) {

        T i_elem = heap.get(i);
        T j_elem = heap.get(j);

        heap.set(i, j_elem);
        heap.set(j, i_elem);

        mapSwap(i_elem, j_elem, i, j);
    }

    /**
     * Removes a particular element in the heap, O(log(n))
     * @param elem
     * @return true if the element is removed, or else false
     */
    public boolean remove(T elem) {
        if (elem == null)
            return false;

        // Linear remove via search, O(n)
//        for (int i = 0; i < heapSize; i++) {
//            if (elem.equals(heap.get(i))) {
//                removeAT(i);
//                return true;
//            }
//        }

        // Logarithmic removal with map, O(log(n))
        Integer index = mapGet(elem);
        if (index != null)
            removeAt(index);
        return index != null;
    }

    /**
     * Removes a node at at particular index, O(log(n))
     * @param i
     * @return
     */
    private T removeAt(int i) {

        if (isEmpty())
            return null;

        heapSize--;
        T removed_elem = heap.get(i);
        if (heapSize > 1 && i != heapSize)
            swap(i, heapSize);
        heap.set(heapSize, null);
        mapRemove(removed_elem, heapSize);

        if (i == heapSize)
            return removed_elem;

        if (heapSize > 1) {
            T elem = heap.get(i);
            sink(i);

            if (heap.get(i).equals(elem))
                swim(i);
        }

        return removed_elem;
    }

    /**
     * Recursively check if the heap is a min heap, start at root with k = 0
     * @param k
     * @return true if the heap is a min heap starting at index k, or else false
     */
    public boolean isMinHeap(int k) {
        if (k >= heapSize)
            return true;

        int left = 2 * k + 1;
        int right = 2 * k + 2;
        if ((left < heapSize && !less(k, left)) || (right < heapSize && !less(k, right)))
            return false;

        return isMinHeap(left) && isMinHeap(right);
    }

    /**
     * Add a node value and its index to the map
     * @param value
     * @param index
     */
    private void mapAdd(T value, int index) {

        TreeSet<Integer> set = map.get(value);
        if (set == null) {
            set = new TreeSet<>();
            set.add(index);
            map.put(value, set);
        } else
            set.add(index);
    }

    /**
     * Removes the index at a given value, O(log(n))
     * @param value
     * @param index
     */
    private void mapRemove(T value, int index) {

        TreeSet<Integer> set = map.get(value);
        set.remove(index);
        if (set.size() == 0)
            map.remove(value);
    }

    /**
     * Extracts an index position for the given value, the highest index is returned if multiple indexes exist
     * @param value
     * @return the existing index of the given value, the highest one if multiple indexes exist, or else null
     */
    private Integer mapGet(T value) {

        TreeSet<Integer> set = map.get(value);
        if (set != null)
            return set.last();
        return null;
    }

    /**
     * Exchanges the index of two nodes internally within the map
     * @param val1
     * @param val2
     * @param index1
     * @param index2
     */
    private void mapSwap(T val1, T val2, int index1, int index2) {

        Set<Integer> set1 = map.get(val1);
        Set<Integer> set2 = map.get(val2);
        set1.remove(index1);
        set2.remove(index2);
        set1.add(index2);
        set2.add(index1);
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}
