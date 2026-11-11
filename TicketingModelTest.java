package encore.view;

import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TicketingModelTest
{

    private TicketingModel model;

    @BeforeEach
    public void setUp()
    {
        model = new TicketingModel(10);
    }


    @Test
    public void testAddObserverNull()
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
    }
    
}
