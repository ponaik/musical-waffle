import com.intern.javacore.CustomLinkedList.MyLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyLinkedListTest {

    private MyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList<>();
    }

    @Test
    void testInitialSizeAndEmptyBehavior() {
        assertEquals(0, list.size());
        assertThrows(IllegalStateException.class, list::getFirst);
        assertThrows(IllegalStateException.class, list::getLast);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IllegalStateException.class, list::removeFirst);
        assertThrows(IllegalStateException.class, list::removeLast);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    void testAddFirstAndGetFirst() {
        list.addFirst(10);
        assertEquals(1, list.size());
        assertEquals(10, list.getFirst());
        list.addFirst(20);
        assertEquals(2, list.size());
        assertEquals(20, list.getFirst());
        assertEquals("[20, 10]", list.toString());
    }

    @Test
    void testAddLastAndGetLast() {
        list.addLast(1);
        assertEquals(1, list.size());
        assertEquals(1, list.getLast());
        list.addLast(2);
        list.addLast(3);
        assertEquals(3, list.size());
        assertEquals(3, list.getLast());
        assertEquals("[1, 2, 3]", list.toString());
    }

    @Test
    void testAddByIndexMiddleAndBounds() {
        list.addLast(1); // [1]
        list.addLast(3); // [1,3]
        list.add(1, 2);  // [1,2,3]
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        // add at head via add(index, el)
        list.add(0, 0); // [0,1,2,3]
        assertEquals(0, list.getFirst());
        // add at tail via add(index == size)
        list.add(list.size(), 4); // [0,1,2,3,4]
        assertEquals(4, list.getLast());
        // invalid indexes
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 100));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(list.size() + 1, 100));
    }

    @Test
    void testGetByIndexAndOutOfBounds() {
        list.addLast(5);
        list.addLast(6);
        list.addLast(7);
        assertEquals(5, list.get(0));
        assertEquals(6, list.get(1));
        assertEquals(7, list.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
    }

    @Test
    void testRemoveFirstAndRemoveLast() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3); // [1,2,3]
        int first = list.removeFirst();
        assertEquals(1, first);
        assertEquals(2, list.size());
        assertEquals("[2, 3]", list.toString());
        int last = list.removeLast();
        assertEquals(3, last);
        assertEquals(1, list.size());
        assertEquals("[2]", list.toString());
        // remove last remaining
        int removed = list.removeLast();
        assertEquals(2, removed);
        assertEquals(0, list.size());
        assertEquals("[]", list.toString());
        assertThrows(IllegalStateException.class, list::removeFirst);
        assertThrows(IllegalStateException.class, list::removeLast);
    }

    @Test
    void testRemoveByIndexMiddle() {
        for (int i = 0; i < 6; i++) list.addLast(i); // [0,1,2,3,4,5]
        int removed = list.remove(2); // remove value 2
        assertEquals(2, removed);
        assertEquals(5, list.size());
        assertEquals("[0, 1, 3, 4, 5]", list.toString());
        // remove first by index
        assertEquals(0, list.remove(0));
        // remove last by index
        assertEquals(5, list.remove(list.size() - 1));
        assertEquals("[1, 3, 4]", list.toString());
        // invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(list.size()));
    }

    @Test
    void testMixedOperationsMaintainConsistency() {
        list.addFirst(2);        // [2]
        list.addLast(3);         // [2,3]
        list.add(1, 5);          // [2,5,3]
        list.addFirst(7);        // [7,2,5,3]
        assertEquals(4, list.size());
        assertEquals(7, list.getFirst());
        assertEquals(3, list.getLast());
        assertEquals(5, list.remove(2)); // remove 5 -> [7,2,3]
        assertEquals("[7, 2, 3]", list.toString());
        assertEquals(7, list.removeFirst()); // [2,3]
        assertEquals(3, list.removeLast());  // [2]
        assertEquals(1, list.size());
        assertEquals(2, list.getFirst());
    }

    @Test
    void testToStringEmptyAndSingleElement() {
        assertEquals("[]", list.toString());
        list.addFirst(42);
        assertEquals("[42]", list.toString());
    }
}
