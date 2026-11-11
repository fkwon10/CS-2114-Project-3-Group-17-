package encore.view;

public class TicketingModelTest extends student.TestCase
{
    private SinglyLinkedQueue queue;
    
    void setUp() {
        queue = new SinglyLinkedQueue<>();
    }
    void testEnqueue() {
        IllegalArgumentException e = null;
        try {
            queue.enqueue(null);
        }
        catch (IllegalArgumentException s) {
            e=s;
        }
        assertNotNull(e);
        queue.enqueue("test");
        assertEquals(1, queue.size());
    }

}
