package encore.view;

import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SinglyLinkedQueueTest
{
    private SinglyLinkedQueue queue;

    @BeforeEach
    public void setUp()
    {
        queue = new SinglyLinkedQueue<>();
    }


    @Test
    public void testEnqueue()
    {
        IllegalArgumentException e = null;
        try
        {
            queue.enqueue(null);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }
        assertNotNull(e);
        queue.enqueue("test");
        assertEquals(1, queue.size());
    }


    @Test
    public void testDequeue()
    {
        NoSuchElementException e = null;
        try
        {
            queue.dequeue();
        }
        catch (NoSuchElementException s)
        {
            e = s;
        }
        assertNotNull(e);
        queue.enqueue("testA");
        queue.enqueue("testB");
        assertEquals(2, queue.size());
        queue.dequeue();
        assertEquals("testB", queue.peek());
    }


    @Test
    public void testPeek()
    {
        NoSuchElementException e = null;
        try
        {
            queue.peek();
        }
        catch (NoSuchElementException s)
        {
            e = s;
        }
        assertNotNull(e);
        queue.enqueue("testA");
        assertEquals("testA", queue.peek());
    }


    @Test
    public void testIsEmpty()
    {
        assertTrue(queue.isEmpty());
        queue.enqueue("testA");
        assertFalse(queue.isEmpty());
    }


    @Test
    public void testSize()
    {
        queue.enqueue("test");
        assertEquals(1, queue.size());
        queue.dequeue();
        assertEquals(0, queue.size());
    }
}
