import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HeapQueueTest {
    private final static int DEFAULT_QUEUE_INIT_SIZE = 6;
    private final static int RANDOM_BOUND = 42;

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

    private void addSomeValues(HeapQueue<String, Integer> queue) {
        queue.add("patata", null);
        queue.add("patata2", 0);
        queue.add("patata3", null);
        queue.add("patata4", 0);
        queue.add("patata5", 2);
        queue.add("patata6", 3);
        queue.add("patata7", 9);
    }

    /**
     * Specific tests
     */


    @Test
    void customTest() {
        HeapQueue<String, Integer> queue = new HeapQueue<>();
        addSomeValues(queue);
        int size = queue.size();
        queue.printAll();
        System.out.println("---------------------");
        for (int i = 0; i < size; i++) {
            queue.remove();
            assertTrue(queue.isSorted());
            queue.printAll();
            System.out.println("---------------------");
        }
    }

    /**
     * Testing operations
     */
    @Test
    void testAddElems() {
        HeapQueue<Integer, Integer> queue = new HeapQueue<>();
        int[] integerPriority = createSortedRandomIntArray();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            queue.add(integerPriority[i], integerPriority[i]);
            assertTrue(queue.isSorted());
        }
        queue.printAll();
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
        queue.printAll();
    }

    @Test
    void testAddElemsWithSamePriority() {
        HeapQueue<Integer, Integer> queue = new HeapQueue<>();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            queue.add(1, 1);
            assertTrue(queue.isSorted());
        }
        queue.printAll();
    }


    @Test
    void testRemoveElems() {
        HeapQueue<Integer, Integer> queue = new HeapQueue<>();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            queue.add(i, i);
            assertTrue(queue.isSorted());
        }
        queue.printAll();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            queue.remove();
            assertTrue(queue.isSorted());
        }
        queue.printAll();
    }

    @Test
    void testAllValidValuesOperations() {
        HeapQueue<Integer, Integer> queue = new HeapQueue<>();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            //IntelliJ has obligated me to create the variable finalI
            int finalI = i;
            assertDoesNotThrow(() -> queue.add(finalI % 2, finalI % 4));
        }
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            Integer prior = queue.element();
            assertEquals(prior, queue.remove());
        }
    }

    @Test
    void testAllNullsOperations() {
        HeapQueue<String, String> queue = new HeapQueue<>();
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            assertDoesNotThrow(() -> queue.add(null, null));
        }
        for (int i = 0; i < DEFAULT_QUEUE_INIT_SIZE; i++) {
            String prior = queue.element();
            assertNull(prior);
            assertEquals(prior, queue.remove());
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
    void testRemoveDoesNotThrowException() {
        HeapQueue<String, Float> queue = new HeapQueue<>();
        queue.add("HOLA", 1.2f);
        assertDoesNotThrow(queue::remove);
    }

    @Test
    void testElementException() {
        HeapQueue<String, Float> queue = new HeapQueue<>();
        assertThrows(NoSuchElementException.class, queue::element);
    }

    @Test
    void testElementDoesNotThrowException() {
        HeapQueue<String, Float> queue = new HeapQueue<>();
        queue.add("EPA", 6.66f);
        assertDoesNotThrow(queue::element);
    }


}

