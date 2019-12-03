import java.util.ArrayList;
import java.util.NoSuchElementException;

public class HeapQueue<V, P extends Comparable<? super P>> implements PriorityQueue<V, P> {
    private final ArrayList<TSPair<V, P>> pairs = new ArrayList<>();
    private long nextTimeStamp = 0L;

    /**
     * Adds a TSPair instance to pairs ArrayList using the values given in the parameters.
     * This operations is done iteratively.
     *
     * @param value    The value of the TSPair.
     * @param priority The priority of the TSPair.
     */
    @Override
    public void add(V value, P priority) {
        TSPair<V, P> addedElem = new TSPair<>(value, priority, nextTimeStamp);
        pairs.add(addedElem);
        upperRelocateIterative(addedElem);
        nextTimeStamp++;
    }

    /**
     * Relocates the element given in the parameters to a position where its parent has more priority than it.
     * This operation is done iteratively.
     *
     * @param addedElem The element that is going to be relocated.
     */
    private void upperRelocateIterative(TSPair<V, P> addedElem) {
        int indexOfAdded = pairs.size() - 1;
        int indexOfParent = parent(indexOfAdded);
        while (hasParent(indexOfAdded) && 0 <= addedElem.compareTo(pairs.get(indexOfParent))) {
            swapNodes(indexOfAdded, indexOfParent);
            indexOfAdded = indexOfParent;
            indexOfParent = parent(indexOfAdded);
        }
    }


    /**
     * Adds a TSPair instance to pairs ArrayList using the values given in the parameters.
     * This operations is done recursively.
     *
     * @param value    The value of the TSPair.
     * @param priority The priority of the TSPair.
     */
    public void addRecursiveEdition(V value, P priority) {
        TSPair<V, P> addedElem = new TSPair<>(value, priority, nextTimeStamp);
        pairs.add(addedElem);
        nextTimeStamp++;
        upperRelocateRecursive(pairs.size() - 1);
    }

    /**
     * Relocates the element of the index given in the parameters to a position where its parent has more priority than it.
     * This operation is done recursively.
     *
     * @param index The index of the element that is going to be relocated.
     */
    private void upperRelocateRecursive(int index) {
        if (hasParent(index) && pairs.get(index).compareTo(pairs.get(parent(index))) < 0) {
            swapNodes(index, parent(index));
            nextTimeStamp++;
            upperRelocateRecursive(parent(index));
        }
    }


    /**
     * Removes the first element in the queue and restructures the queue.
     * This operations is done iteratively.
     *
     * @return The element that has just been eliminated.
     */
    @Override
    public V remove() {
        if (pairs.size() == 0) {
            throw new NoSuchElementException();
        } else {
            V elementToRemove = element();
            TSPair<V, P> pivot = pairs.get(pairs.size() - 1);
            pairs.set(0, pivot);
            pairs.remove(pairs.size() - 1);
            lowerRelocateIterative(pivot);
            return elementToRemove;
        }
    }

    /**
     * Relocates the element given in the parameters to a position where its childs have less priority than it.
     * This operation is done iteratively.
     *
     * @param pivot The element that is going to be relocated.
     */
    private void lowerRelocateIterative(TSPair<V, P> pivot) {
        int indexOfPivot = 0;
        int indexOfBiggestChild = obtainIndexOfBiggerChildThanRoot(indexOfPivot);
        while (indexOfBiggestChild > 0) {
            pairs.set(indexOfPivot, pairs.get(indexOfBiggestChild));
            pairs.set(indexOfBiggestChild, pivot);
            indexOfBiggestChild = obtainIndexOfBiggerChildThanRoot(indexOfPivot);
        }
    }

    /**
     * Removes the first element in the queue and restructures the queue.
     * This operations is done recursively.
     *
     * @return The element that has just been eliminated.
     */
    public V removeRecursiveEdition() {
        if (pairs.size() == 0) {
            throw new NoSuchElementException();
        } else {
            V elementToRemove = element();
            TSPair<V, P> pivot = pairs.get(pairs.size() - 1);
            pairs.set(0, pivot);
            pairs.remove(pairs.size() - 1);
            lowerRelocateRecursive(0);
            return elementToRemove;
        }
    }

    /**
     * Relocates the element given in the parameters to a position where its childs have less priority than it.
     * This operation is done recursively.
     *
     * @param index The index of the element that is going to be relocated.
     */
    private void lowerRelocateRecursive(int index) {
        int indexOfBiggestChild = obtainIndexOfBiggerChildThanRoot(index);
        if (indexOfBiggestChild != -1) {
            swapNodes(index, indexOfBiggestChild);
            lowerRelocateRecursive(indexOfBiggestChild);
        }
    }

    /**
     * Returns the first element of the queue.
     *
     * @return The first element of the queue.
     */
    @Override
    public V element() {
        if (pairs.size() == 0) {
            throw new NoSuchElementException();
        } else {
            return pairs.get(0).value;
        }
    }

    /**
     * Returns the size of the queue.
     *
     * @return The number of elements that contains the queue.
     */
    @Override
    public int size() {
        return pairs.size();
    }

    static int parent(int index) {
        if (index % 2 == 0) {
            return (index - 2) / 2;
        } else {
            return (index - 1) / 2;
        }
    }

    static int left(int index) {
        return 2 * index + 1;
    }

    static int right(int index) {
        return 2 * index + 2;
    }

    boolean
    isValid(int index) {
        return 0 <= index && index < size();
    }

    boolean
    hasParent(int index) {
        return index > 0;
    }

    boolean
    hasLeft(int index) {
        return isValid(left(index));
    }

    boolean
    hasRight(int index) {
        return isValid(right(index));
    }

    void
    swapNodes(int i1, int i2) {
        TSPair<V, P> temp = pairs.get(i1);
        pairs.set(i1, pairs.get(i2));
        pairs.set(i2, temp);
    }

    /**
     * Given the index of a root and its childs, checks which node has more priority.
     *
     * @param indexLeftCh  The index of the left child.
     * @param indexRoot    The index of the root.
     * @param indexRightCh The index of the right child.
     * @return The index of the element with more priority. If it's the root who has more priority,
     * returns -1.
     */
    public int indexOfMaxPriority(int indexLeftCh, int indexRoot, int indexRightCh) {
        int maxIndex = indexOfMaxPriority(indexLeftCh, indexRightCh);
        return indexOfMaxPriorityBetweenChildAndRoot(indexRoot, maxIndex);
    }

    /**
     * Given the index of a root, the method searches for the child with more priority than the root.
     *
     * @param indexRoot The index of the root.
     * @return The index of the child with biggest priority that also is bigger than the root.
     * Returns -1 if none of the childs are bigger than the root.
     */
    int obtainIndexOfBiggerChildThanRoot(int indexRoot) {
        if (!hasLeft(indexRoot)) {
            return -1;
        } else if (!hasRight(indexRoot)) {
            int leftIndex = left(indexRoot);
            return indexOfMaxPriorityBetweenChildAndRoot(indexRoot, leftIndex);
        } else {
            int leftIndex = left(indexRoot);
            int rightIndex = right(indexRoot);
            return indexOfMaxPriority(leftIndex, indexRoot, rightIndex);
        }
    }


    /**
     * Checks which child has more priority.
     *
     * @param indexLeftCh  The index of the left child.
     * @param indexRightCh The index of the right child.
     * @return The indexLeftCh if the left child has more priority than the right child,
     * indexRightCh otherwise.
     */
    private int indexOfMaxPriority(int indexLeftCh, int indexRightCh) {
        return (0 <= pairs.get(indexLeftCh).compareTo(pairs.get(indexRightCh))) ? indexLeftCh : indexRightCh;
    }

    /**
     * Checks if the child has more priority than the root.
     *
     * @param rootIndex  The index of the root.
     * @param childIndex The index of the child of the root.
     * @return The index of the child if it have more priority than the root. -1 if the child has less
     * priority than the root.
     */
    private int indexOfMaxPriorityBetweenChildAndRoot(int rootIndex, int childIndex) {
        return (0 <= pairs.get(rootIndex).compareTo(pairs.get(childIndex))) ? -1 : childIndex;
    }

    /**
     * Prints all the elements in the queue in level order.
     */
    public void printAllElementsInTree() {
        for (TSPair<V, P> pair : pairs) {
            System.out.println(pair.toString());
        }
    }

    /**
     * Checks if the queue is sorted by priority.
     *
     * @return True if the queue is sorted, false otherwise.
     */
    public boolean isSorted() {
        return isSorted(0);
    }


    boolean isSorted(int i) {
        if (size() <= i) {
            return true;
        }
        if (hasLeft(i)) {
            if (pairs.get(i).compareTo(pairs.get(2 * i + 1)) < 0) {
                return false;
            }
        }
        if (hasRight(i)) {
            if (pairs.get(i).compareTo(pairs.get(2 * i + 2)) < 0) {
                return false;
            }
        }
        return (isSorted(2 * i + 1) && isSorted(2 * i + 2));
    }

    public static class TSPair<V, P extends Comparable<? super P>>
            implements Comparable<TSPair<V, P>> {
        private final V value;
        private final P priority;
        private final long timeStamp;

        /**
         * The constructor of TSPair.
         *
         * @param value     The value of the TSPair instance.
         * @param priority  The priority of the TSPair instance.
         * @param timeStamp The time when the element has been created.
         */
        public TSPair(V value, P priority, long timeStamp) {
            this.value = value;
            this.priority = priority;
            this.timeStamp = timeStamp;
        }


        /**
         * Gets the String that represents the instance of TSPair.
         *
         * @return The String that represents the instance of TSPair.
         */
        @Override
        public String toString() {
            if (priority == null) {
                return "Value: " + value.toString() + "\tPriority: " + "null" + "\tTimeStamp: " + timeStamp;
            } else {
                return "Value: " + value.toString() + "\tPriority: " + priority.toString() + "\tTimeStamp: " + timeStamp;
            }
        }

        /**
         * Compares the priorities and timeStamp of two instances of TSPair.
         *
         * @param other The instance of TSPair that is going to be compared with this instance.
         * @return A value greater than zero if this is bigger than other. A value lower than zero if this is smaller
         * than other. Zero if both instances are equals (it won't happen).
         */
        @Override
        public int compareTo(TSPair<V, P> other) {
            if (this.priority == null && other.priority == null) {
                return (other.timeStamp < this.timeStamp) ? 1 : -1;
            } else if (this.priority == null) {
                return -1;
            } else if (other.priority == null) {
                return 1;
            } else {
                int compPriority = this.priority.compareTo(other.priority);
                if (compPriority == 0) {
                    //We know that each timeStamp is unique, so it won't happen
                    //that there are two TSPair with the same timeStamp
                    return (other.timeStamp < this.timeStamp) ? 1 : -1;
                } else {
                    return compPriority;
                }
            }
        }
    }
}
