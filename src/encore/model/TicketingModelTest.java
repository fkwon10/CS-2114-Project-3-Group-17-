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
        assertEquals("CLASSIC", model.getCustomerTier("John"));

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

        // Quantity too high for default CLASSIC
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
    public void testMembershipTiersNormalPerks() {
        assertEquals(4, model.getMaxTicketsForTier("CLASSIC"));
        assertEquals(6, model.getMaxTicketsForTier("SILVER"));
        assertEquals(8, model.getMaxTicketsForTier("GOLD"));

        // GOLD perk: up to 8 tickets + instant fast-track to checkout when empty
        model.enqueueFan("VipFan", 8, "gold");
        assertEquals("GOLD", model.getCustomerTier("VipFan"));
        assertNotNull(model.getActiveRequest());
        assertEquals("VipFan", model.getActiveRequest().getCustomerName());
        assertEquals(0, model.getQueueSize());
        model.confirmPurchase();
        assertEquals(2, model.getTicketsRemaining());
        assertNull(model.getCustomerTier("VipFan"));

        // SILVER perk: up to 6 tickets, joins queue normally
        model.enqueueFan("SilverFan", 6, "SILVER");
        assertEquals("SILVER", model.getCustomerTier("SilverFan"));
        assertEquals(1, model.getQueueSize());
        assertNull(model.getActiveRequest());

        // CLASSIC perk: up to 4 tickets
        model.enqueueFan("ClassicFan", 4, "Classic");
        assertEquals("CLASSIC", model.getCustomerTier("ClassicFan"));
        assertEquals(2, model.getQueueSize());
    }

    @Test
    public void testMembershipTiersBadInput() {
        IllegalArgumentException e = null;

        // Null tier
        try {
            model.getMaxTicketsForTier(null);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Blank tier
        try {
            model.enqueueFan("John", 2, "   ");
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Unknown tier name
        try {
            model.enqueueFan("John", 2, "PLATINUM");
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // CLASSIC exceeding 4 tickets
        try {
            model.enqueueFan("John", 5, "CLASSIC");
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // SILVER exceeding 6 tickets
        try {
            model.enqueueFan("John", 7, "SILVER");
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // GOLD exceeding 8 tickets
        try {
            model.enqueueFan("John", 9, "GOLD");
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        assertEquals(0, model.getQueueSize());
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