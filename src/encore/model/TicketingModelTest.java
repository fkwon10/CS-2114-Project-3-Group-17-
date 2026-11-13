package encore.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import encore.view.QueueObserver;

public class TicketingModelTest {
    private TicketingModel model;

    private static class StubObserver implements QueueObserver {
        QueueStatusDTO lastStatus = null;
        String lastError = null;

        @Override
        public void update(QueueStatusDTO status) {
            this.lastStatus = status;
        }

        @Override
        public void showError(String message) {
            this.lastError = message;
        }
    }

    @Before
    public void setUp() {
        model = new TicketingModel(10);
    }

    @Test
    public void testAddObserverNull() {
        IllegalArgumentException e = null;
        try {
            model.addObserver(null);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
    }

    @Test
    public void testObserverReceivesUpdates() {
        StubObserver stub = new StubObserver();
        model.addObserver(stub);

        model.enqueueFan("John", 2);

        assertNotNull(stub.lastStatus);
        assertNull(stub.lastError);
        assertEquals(1, stub.lastStatus.getQueueLength());
        assertEquals("John", stub.lastStatus.getFrontCustomerName());
        assertEquals(10, stub.lastStatus.getTicketsRemaining());
    }

    @Test
    public void testEnqueueFanValid() {
        model.enqueueFan("John", 2);
        assertEquals(1, model.getQueueSize());

        FanTicketRequest request = model.stepQueue();
        assertEquals("John", request.getCustomerName());
        assertEquals(2, request.getQuantity());
    }

    @Test
    public void testEnqueueFan() {
        IllegalArgumentException e = null;

        // Null name
        try {
            model.enqueueFan(null, 2);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Blank name
        try {
            model.enqueueFan("", 2);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Quantity too low
        try {
            model.enqueueFan("John", 0);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Quantity too high
        try {
            model.enqueueFan("John", 5);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Valid enqueue
        model.enqueueFan("John", 2);

        // Duplicate customer
        try {
            model.enqueueFan("John", 1);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);

        // Verify valid request was added
        FanTicketRequest request = model.stepQueue();
        assertEquals("John", request.getCustomerName());
    }

    @Test
    public void testStepQueue() {
        IllegalStateException e = null;

        // Empty queue
        try {
            model.stepQueue();
        } catch (IllegalStateException s) {
            e = s;
        }
        assertNotNull(e);

        // Valid queue movement
        model.enqueueFan("John", 2);
        model.enqueueFan("Jane", 1);

        FanTicketRequest request = model.stepQueue();
        assertEquals("John", request.getCustomerName());
        assertEquals(1, model.getQueueSize());

        e = null;

        // Checkout window already occupied
        try {
            model.stepQueue();
        } catch (IllegalStateException s) {
            e = s;
        }
        assertNotNull(e);
    }

    @Test
    public void testConfirmPurchase() {
        IllegalStateException e = null;

        // No active customer
        try {
            model.confirmPurchase();
        } catch (IllegalStateException s) {
            e = s;
        }
        assertNotNull(e);

        // Insufficient inventory
        model = new TicketingModel(2);
        model.enqueueFan("John", 3);
        model.stepQueue();

        e = null;
        try {
            model.confirmPurchase();
        } catch (IllegalStateException s) {
            e = s;
        }
        assertNotNull(e);
        assertEquals(2, model.getTicketsRemaining());

        // Successful purchase
        model = new TicketingModel(10);
        model.enqueueFan("Jane", 2);
        model.stepQueue();
        model.confirmPurchase();

        assertEquals(8, model.getTicketsRemaining());
        assertNull(model.getActiveRequest());

        // Verify checkout window was freed
        model.enqueueFan("Alex", 1);
        FanTicketRequest request = model.stepQueue();
        assertEquals("Alex", request.getCustomerName());
    }

    @Test
    public void testConfirmPartialPurchase() {
        model = new TicketingModel(2);
        model.enqueueFan("John", 4);
        model.stepQueue();

        model.confirmPartialPurchase(2);
        assertEquals(0, model.getTicketsRemaining());
        assertNull(model.getActiveRequest());
    }

    @Test
    public void testCancelActiveTransaction() {
        IllegalStateException e = null;

        // No active customer
        try {
            model.cancelActiveTransaction();
        } catch (IllegalStateException s) {
            e = s;
        }
        assertNotNull(e);

        // Valid cancellation
        model.enqueueFan("John", 2);
        model.stepQueue();
        model.cancelActiveTransaction();

        assertEquals(10, model.getTicketsRemaining());
        assertNull(model.getActiveRequest());

        // Verify checkout window was freed
        model.enqueueFan("Jane", 1);
        FanTicketRequest request = model.stepQueue();
        assertEquals("Jane", request.getCustomerName());
    }

    @Test
    public void testGetStatusSnapshot() {
        QueueStatusDTO status = model.getStatusSnapshot();
        assertNotNull(status);
        assertEquals("None", status.getFrontCustomerName());
        assertEquals(0, status.getQueueLength());
        assertEquals(10, status.getTicketsRemaining());

        model.enqueueFan("John", 2);
        QueueStatusDTO updated = model.getStatusSnapshot();
        assertEquals("John", updated.getFrontCustomerName());
        assertEquals(1, updated.getQueueLength());
    }
}