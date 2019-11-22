import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class HeapQueue<V, P extends Comparable<? super P>> implements PriorityQueue<V, P> {
    private final ArrayList<TSPair<V, P>> pairs = new ArrayList<>();
    private long nextTimeStamp = 0L;

    public ListIterator<TSPair<V, P>> listIterator() {
        return pairs.listIterator();
    }

    /**
     * Adds a TSPair instance to pairs ArrayList.
     * The added TSPair constructor parameters are the same as the add function plus nextTimeStamp which
     * determines the order of adding.
     *
     * @param value    The value of the TSPair.
     * @param priority The priority of the TSPair
     */
    @Override
    public void add(V value, P priority) {
        //Part d'afegir element
        TSPair<V, P> addedElem = new TSPair<>(value, priority, nextTimeStamp);
        pairs.add(addedElem);
        int indexOfAdded = pairs.size() - 1;
        int indexOfParent = parent(indexOfAdded);
        //Part d'escalada de l'element
        //Fer lo dels metodes privats que diu la documentacio
        while (hasParent(indexOfAdded) && 0 <= addedElem.compareTo(pairs.get(indexOfParent))) {
            swapNodes(indexOfAdded, indexOfParent);
            indexOfAdded = indexOfParent;
            indexOfParent = parent(indexOfAdded);
        }
        nextTimeStamp++;
    }


    //Hi han dos parts: la primera es eliminar i la segona es arreglar l'arbre
    @Override
    public V remove() {
        if (size() == 0) {
            throw new NoSuchElementException();
        } else {
            V elementToRemove = element();
            //Element obtained and removed
            TSPair<V, P> pivot = pairs.get(pairs.size() - 1);
            pairs.set(0, pivot);
            pairs.remove(pairs.size() - 1);
            //Reconstruction of the array
            int indexOfPivot = 0;
            int indexOfBiggestChild = obtainIndexOfBiggerChildThanRoot(indexOfPivot);
            while (indexOfBiggestChild >= 0) {
                pairs.set(indexOfPivot, pairs.get(indexOfBiggestChild));
                pairs.set(indexOfBiggestChild, pivot);
                indexOfBiggestChild = obtainIndexOfBiggerChildThanRoot(indexOfPivot);
            }
            return elementToRemove;
        }
    }

    @Override
    public V element() {
        if (pairs.size() == 0) {
            throw new NoSuchElementException();
        } else {
            return pairs.get(0).value;
        }
    }

    @Override
    public int size() {
        return pairs.size();
    }

    //Aixó d'aqui es pot fer més fancy
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

    public int indexOfMaxPriority(int i1, int i2, int i3) {
        int maxIndex = indexOfMaxPriority(i1, i3);
        return indexOfMaxPriorityBetweenChildAndRoot(i2, maxIndex);
    }

    int obtainIndexOfBiggerChildThanRoot(int indexRoot) {
        if (!hasLeft(indexRoot)) {
            return -1;
        } else {
            int leftIndex = left(indexRoot);
            if (!hasRight(indexRoot)) {
                return indexOfMaxPriorityBetweenChildAndRoot(indexRoot, leftIndex);
            } else {
                int rightIndex = right(indexRoot);
                return indexOfMaxPriority(leftIndex, indexRoot, rightIndex);
            }
        }
    }

    public int indexOfMaxPriority(int i1, int i2) {
        return (0 <= pairs.get(i1).compareTo(pairs.get(i2))) ? i1 : i2;
    }

    int indexOfMaxPriorityBetweenChildAndRoot(int rootIndex, int childIndex) {
        return (0 <= pairs.get(rootIndex).compareTo(pairs.get(childIndex))) ? -1 : childIndex;
    }

    //AIXO SHA DE BORRAR
    public void printAll() {
        for (int i = 0; i < pairs.size(); i++) {
            System.out.println(pairs.get(i).toString());
        }
    }

    boolean isSorted() {
        return isSorted(0);
    }

    public boolean isSorted(int i) {
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

    private static class TSPair<V, P extends Comparable<? super P>>
            implements Comparable<TSPair<V, P>> {
        private final V value;
        private final P priority;
        private final long timeStamp;

        public TSPair(V value, P priority, long timeStamp) {
            this.value = value;
            this.priority = priority;
            this.timeStamp = timeStamp;
        }

        @Override
        public String toString() {
            if (priority == null) {
                return "Value: " + value.toString() + "\tPriority: " + "null" + "\tTimeStamp: " + timeStamp;
            } else {
                return "Value: " + value.toString() + "\tPriority: " + priority.toString() + "\tTimeStamp: " + timeStamp;
            }
        }

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
