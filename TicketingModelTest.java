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


    @Test
    public void testEnqueueFanValid()
    {
        model.enqueueFan("John", 2);

        FanTicketRequest request = model.stepQueue();

        assertEquals("John", request.getCustomerName());
    }


    @Test
    public void testEnqueueFanInvalidQuantity()
    {
        IllegalArgumentException e = null;
        try
        {
            model.enqueueFan("John", 0);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }
        assertNotNull(e);
    }


    @Test
    public void testEnqueueFanQuantityTooHigh()
    {
        IllegalArgumentException e = null;
        try
        {
            model.enqueueFan("John", 5);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }
        assertNotNull(e);
    }


    @Test
    public void testEnqueueFanDuplicate()
    {
        model.enqueueFan("John", 2);

        IllegalArgumentException e = null;
        try
        {
            model.enqueueFan("John", 1);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }
        assertNotNull(e);
    }


    @Test
    public void testEnqueueFan()
    {
        IllegalArgumentException e = null;

        // Null name
        try
        {
            model.enqueueFan(null, 2);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }

        assertNotNull(e);
        e = null;

        // Blank name
        try
        {
            model.enqueueFan("", 2);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }

        assertNotNull(e);
        e = null;

        // Quantity too low
        try
        {
            model.enqueueFan("John", 0);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }

        assertNotNull(e);
        e = null;

        // Quantity too high
        try
        {
            model.enqueueFan("John", 5);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }

        assertNotNull(e);
        e = null;

        // Valid enqueue
        model.enqueueFan("John", 2);

        // Duplicate customer
        try
        {
            model.enqueueFan("John", 1);
        }
        catch (IllegalArgumentException s)
        {
            e = s;
        }

        assertNotNull(e);

        // Verify valid request was added
        FanTicketRequest request = model.stepQueue();

        assertEquals("John", request.getCustomerName());
    }


    @Test
    public void testStepQueue()
    {
        IllegalStateException e = null;

        // Empty queue
        try
        {
            model.stepQueue();
        }
        catch (IllegalStateException s)
        {
            e = s;
        }

        assertNotNull(e);

        // Valid queue movement
        model.enqueueFan("John", 2);
        model.enqueueFan("Jane", 1);

        FanTicketRequest request = model.stepQueue();

        assertEquals("John", request.getCustomerName());

        e = null;

        // Checkout window already occupied
        try
        {
            model.stepQueue();
        }
        catch (IllegalStateException s)
        {
            e = s;
        }

        assertNotNull(e);
    }


    @Test
    public void testConfirmPurchase()
    {
        IllegalStateException e = null;

        // No active customer
        try
        {
            model.confirmPurchase();
        }
        catch (IllegalStateException s)
        {
            e = s;
        }

        assertNotNull(e);

        // Insufficient inventory
        model = new TicketingModel(2);

        model.enqueueFan("John", 3);
        model.stepQueue();

        e = null;

        try
        {
            model.confirmPurchase();
        }
        catch (IllegalStateException s)
        {
            e = s;
        }

        assertNotNull(e);

        // Successful purchase
        model = new TicketingModel(10);

        model.enqueueFan("Jane", 2);
        model.stepQueue();

        model.confirmPurchase();

        // Verify checkout window was freed
        model.enqueueFan("Alex", 1);

        FanTicketRequest request = model.stepQueue();

        assertEquals("Alex", request.getCustomerName());
    }


    @Test
    public void testCancelActiveTransaction()
    {
        IllegalStateException e = null;

        // No active customer
        try
        {
            model.cancelActiveTransaction();
        }
        catch (IllegalStateException s)
        {
            e = s;
        }

        assertNotNull(e);

        // Valid cancellation
        model.enqueueFan("John", 2);
        model.stepQueue();

        model.cancelActiveTransaction();

        // Verify checkout window was freed
        model.enqueueFan("Jane", 1);

        FanTicketRequest request = model.stepQueue();

        assertEquals("Jane", request.getCustomerName());
    }


    @Test
    public void testGetStatusSnapshot()
    {
        QueueStatusDTO status = model.getStatusSnapshot();

        assertNotNull(status);
    }
}
