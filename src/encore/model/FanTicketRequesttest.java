package encore.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class FanTicketRequestTest {

    @Test
    public void testConstructorValid() {
        FanTicketRequest r = new FanTicketRequest("  Alice  ", 8, 1000L);
        assertEquals("Alice", r.getCustomerName());
        assertEquals(8, r.getQuantity());
        assertEquals(1000L, r.getJoinedAtMillis());

        FanTicketRequest timed = new FanTicketRequest("Bob", 1);
        assertEquals("Bob", timed.getCustomerName());
        assertEquals(1, timed.getQuantity());
        assertTrue(timed.getJoinedAtMillis() > 0);
    }

    @Test
    public void testConstructorBadInput() {
        IllegalArgumentException e = null;

        // Null name
        try {
            new FanTicketRequest(null, 2);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Blank name
        try {
            new FanTicketRequest("   ", 2);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Quantity too low
        try {
            new FanTicketRequest("Alice", 0);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
        e = null;

        // Quantity too high
        try {
            new FanTicketRequest("Alice", 9);
        } catch (IllegalArgumentException s) {
            e = s;
        }
        assertNotNull(e);
    }

    @Test
    public void testEqualsAndHashCode() {
        FanTicketRequest a = new FanTicketRequest("Alice", 2, 500L);
        FanTicketRequest same = new FanTicketRequest("Alice", 2, 500L);
        assertEquals(a, a);
        assertEquals(a, same);
        assertEquals(a.hashCode(), same.hashCode());

        // Bad input: null, other type, and differing fields
        assertNotEquals(a, null);
        assertNotEquals(a, "Alice");
        assertNotEquals(a, new FanTicketRequest("Bob", 2, 500L));
        assertNotEquals(a, new FanTicketRequest("Alice", 3, 500L));
        assertNotEquals(a, new FanTicketRequest("Alice", 2, 501L));
    }

    @Test
    public void testToString() {
        assertEquals("FanTicketRequest[Alice, 1 ticket]",
            new FanTicketRequest("Alice", 1).toString());
        assertEquals("FanTicketRequest[Alice, 2 tickets]",
            new FanTicketRequest("Alice", 2).toString());
    }
}
