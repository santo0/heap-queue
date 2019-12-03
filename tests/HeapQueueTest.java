import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HeapQueueTest {
    private final static int DEFAULT_QUEUE_INIT_SIZE = 6;
    private final static int RANDOM_BOUND = 42;
    private final String[] DEFAULT_STRING_VALUES = new String[]{"a", "b", "c", "d", "e", "f"};

    /**
     * Methods for testing
     */

    private int[] createSortedRandomIntArray() {
        int[] array = createUnsortedRandomIntArray();
        Arrays.sort(array);
        return array;
    }

    private int[] createUnsortedRandomIntArray() {
        int[] array = new int[DEFAULT_QUEUE_INIT_SIZE];
        Random rn = new Random();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            array[i] = rn.nextInt(RANDOM_BOUND);
        }
        return array;
    }

    private void addRandomElements(HeapQueue<Integer, String> queue) {
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            queue.add(i, DEFAULT_STRING_VALUES[i % DEFAULT_STRING_VALUES.length]);
        }
    }


    /**
     * Tests
     */

    @Test
    void testAddElems() {
        HeapQueue<Integer, Integer> queue = new HeapQueue<>();
        int[] integerPriority = createSortedRandomIntArray();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            queue.add(integerPriority[i], integerPriority[i]);
            assertTrue(queue.isSorted());
        }
        queue.printAllElementsInTree();
    }


    @Test
    void testAddElemsWithNulls() {
        HeapQueue<Integer, Integer> queue = new HeapQueue<>();
        int[] integerPriority = createSortedRandomIntArray();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            if (i % 2 == 0) {
                queue.add(integerPriority[i], integerPriority[i]);
            } else {
                queue.add(integerPriority[i], null);
            }
            assertTrue(queue.isSorted());
        }
        queue.printAllElementsInTree();
    }

    @Test
    void testAddElemsWithSamePriority() {
        HeapQueue<Integer, Integer> queue = new HeapQueue<>();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            queue.add(1, 1);
            assertTrue(queue.isSorted());
        }
        queue.printAllElementsInTree();
    }
    
    @Test
    void testRemoveElems() {
        HeapQueue<Integer, String> queue = new HeapQueue<>();
        addRandomElements(queue);
        queue.printAllElementsInTree();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            Integer removedElem = queue.remove();
            assertEquals(DEFAULT_QUEUE_INIT_SIZE - i - 1, removedElem);
            assertTrue(queue.isSorted());
        }
        queue.printAllElementsInTree();
    }

    @Test
    void testRecursiveOperations() {
        HeapQueue<Integer, Integer> queue = new HeapQueue<>();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            queue.addRecursiveEdition(i, i);
        }
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            Integer prior = queue.element();
            assertEquals(prior, queue.removeRecursiveEdition());
        }
    }

    /**
     * Testing exception handling
     */
    @Test
    void testRemoveException() {
        HeapQueue<String, Float> queue = new HeapQueue<>();
        assertThrows(NoSuchElementException.class, queue::remove);
    }

    @Test
    void testElementException() {
        HeapQueue<String, Float> queue = new HeapQueue<>();
        assertThrows(NoSuchElementException.class, queue::element);
    }
}

