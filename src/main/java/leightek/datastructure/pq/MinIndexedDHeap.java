package leightek.datastructure.pq;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * An implementation of an indexed min D-ary heap priority queue.
 * @param <T>
 */
public class MinIndexedDHeap <T extends Comparable<T>> {

    private int sz; // current number of elements in the heap
    private final int N; // maximum number of elements in the heap
    private final int D; // the degree of every node in the heap
    private final int[] child, parent; // lookup array to track the child/parent indexes at each node
    public final int[] pm; // the Position Map maps Key Indexes (ki) to node position
    public final int[] im; // the Inverse Map maps node position [0, sz) to Key Indexes
    public final Object[] values; // the values associated with the keys, the array is indexed by ki

    public MinIndexedDHeap(int degreee, int maxSize) {
        if (maxSize <= 0) throw new IllegalArgumentException("maxSize <= 0");

        D = max(2, degreee);
        N = max(D + 1, maxSize);

        child = new int[N];
        parent = new int[N];
        pm = new int[N];
        im = new int[N];
        values = new Object[N];

        for (int i = 0; i < N; i++) {
            parent[i] = (i - 1) / D;
            child[i] = i * D + 1;
            pm[i] = im[i] = -1;
        }
    }

    public int size() {
        return sz;
    }

    public boolean isEmpty() {
        return  sz == 0;
    }

    /**
     * Test if the heap contains the node with index ki
     * @param ki  key index value
     * @return true if exists or else false
     */
    public boolean contains(int ki) {
        keyInBoundsOrThrow(ki);
        return pm[ki] != -1;
    }

    public int peekMinKeyIndex() {
        isNotEmptyOrThrow();
        return im[0];
    }

    public int pollMinKeyIndex() {
        int minki = peekMinKeyIndex();
        delete(minki);
        return minki;
    }

    @SuppressWarnings("unchecked")
    public T peekMinValue() {
        isNotEmptyOrThrow();
        return (T) values[im[0]];
    }

    public T pollMinValue() {
        T minValue = peekMinValue();
        delete(peekMinKeyIndex());
        return minValue;
    }

    public void insert(int ki, T value) {
        if (contains(ki))
            throw new IllegalArgumentException("index already exists; received: " + ki);
        valueNotNullOrThrow(value);
        pm[ki] = sz;
        im[sz] = ki;
        values[ki] = value;
        swim(sz++);
    }

    @SuppressWarnings("unchecked")
    public T valueOf(int ki) {
        keyExistsOrThrow(ki);
        return (T) values[ki];
    }

    @SuppressWarnings("unchecked")
    public T delete(int ki) {
        keyExistsOrThrow(ki);
        final int i = pm[ki];
        swap(i, --sz);
        sink(i);
        swim(i);
        T value = (T) values[ki];
        values[ki] = null;
        pm[ki] = -1;
        im[sz] = -1;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T update(int ki, T value) {
        keyExistsAndValueNotNullOrThrow(ki, value);
        final int i = pm[ki];
        T oldValue = (T) values[ki];
        values[ki] = value;
        sink(i);
        swim(i);
        return oldValue;
    }

    /**
     * Strictly decreases the value associated with 'ki' to 'value'
     * @param ki
     * @param value
     */
    public void decrease(int ki, T value) {
        keyExistsAndValueNotNullOrThrow(ki, value);
        if (less(value, values[ki])) {
            values[ki] = value;
            swim(pm[ki]);
        }
    }

    /**
     * Strictly increases the value associated with 'ki' to 'value'
     * @param ki
     * @param value
     */
    public void increase(int ki, T value) {
        keyExistsAndValueNotNullOrThrow(ki, value);
        if (less(values[ki], value)) {
            values[ki] = value;
            sink(pm[ki]);
        }
    }

    // Helper functions

    private void sink(int i) {
        for (int j = minChild(i); j != -1;) {
            swap(i, j);
            i = j;
            j = minChild(i);
        }
    }

    private void swim(int i) {
        while (less(i, parent[i])) {
            swap(i, parent[i]);
            i = parent[i];
        }
    }

    /**
     * From the parent node at index i find the minimum child below it
     * @param i
     * @return
     */
    private int minChild(int i) {
        int index = -1, from = child[i], to = min(sz, from +D);
        for (int j = from; j < to; j++) {
            if (less(j, i))
                index = i = j;
        }
        return index;
    }

    private void swap(int i, int j) {
        pm[im[j]] = i;
        pm[im[i]] = j;
        int tmp = im[i];
        im[i] = im[j];
        im[j] = tmp;
    }

    @SuppressWarnings("unchecked")
    private boolean less(int i, int j) {
        return ((Comparable<? super  T>) values[im[i]]).compareTo((T) values[im[j]]) < 0;
    }

    @SuppressWarnings("unchecked")
    private boolean less(Object obj1, Object obj2) {
        return ((Comparable<? super T>) obj1).compareTo((T) obj2) < 0;
    }

    @Override
    public String toString() {
        List<Integer> lst = new ArrayList<>(sz);
        for (int i = 0; i < sz; i++)
            lst.add(im[i]);
        return lst.toString();
    }

    private void isNotEmptyOrThrow() {
        if (isEmpty())
            throw new NoSuchElementException("Priority queue underflow");
    }

    private void keyExistsAndValueNotNullOrThrow(int ki, Object value) {
        keyExistsOrThrow(ki);
        valueNotNullOrThrow(value);
    }

    private void keyExistsOrThrow(int ki) {
        if (!contains(ki))
            throw new NoSuchElementException("Index does not exist; received: " + ki);
    }

    private void valueNotNullOrThrow(Object value) {
        if (value == null)
            throw new IllegalArgumentException("value cannot be null");
    }

    private void keyInBoundsOrThrow(int ki) {
        if (ki < 0 || ki >= N)
            throw new IllegalArgumentException("Key index out of bound; received: " + ki);
    }

    // Test functions

    public boolean isMinHeap() {
        return isMinHeap(0);
    }

    private boolean isMinHeap(int i) {
        int from = child[i], to = min(sz, from + D);
        for (int j = from; j < to; j++) {
            if (!less(i, j))
                return false;
            if (!isMinHeap(j))
                return false;
        }
        return true;
    }



}
