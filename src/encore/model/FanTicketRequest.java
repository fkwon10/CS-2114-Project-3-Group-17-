package encore.model;

import java.util.Objects;

/**
 * An immutable domain record representing one fan's request to buy tickets.
 *
 * @author Rafik Khismatov
 * @version 2026.11.12
 */
public final class FanTicketRequest {

    private final String customerName;
    private final int quantity;
    private final long joinedAtMillis;

    /**
     * Creates a request stamped with the current system time.
     *
     * @param customerName the fan's name
     * @param quantity     the number of tickets requested
     */
    public FanTicketRequest(String customerName, int quantity) {
        this(customerName, quantity, System.currentTimeMillis());
    }

    /**
     * Creates a request with an explicit join timestamp.
     *
     * @param customerName   the fan's name
     * @param quantity       the number of tickets requested
     * @param joinedAtMillis the time the fan joined the line, in epoch
     *                       milliseconds
     * @throws IllegalArgumentException if the name is blank or the quantity
     *                                  is not between 1 and 4
     */
    public FanTicketRequest(String customerName, int quantity,
        long joinedAtMillis) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Customer name cannot be blank.");
        }
        if (quantity < 1 || quantity > 4) {
            throw new IllegalArgumentException(
                "Quantity must be between 1 and 4.");
        }
        this.customerName = customerName.trim();
        this.quantity = quantity;
        this.joinedAtMillis = joinedAtMillis;
    }

    /**
     * @return the fan's name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @return the number of tickets requested
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return the time the fan joined the line, in epoch milliseconds
     */
    public long getJoinedAtMillis() {
        return joinedAtMillis;
    }

    /**
     * Two requests are equal when every field matches.
     *
     * @param obj the object to compare against
     * @return true if obj is a FanTicketRequest with identical fields
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FanTicketRequest)) {
            return false;
        }
        FanTicketRequest other = (FanTicketRequest) obj;
        return quantity == other.quantity
            && joinedAtMillis == other.joinedAtMillis
            && customerName.equals(other.customerName);
    }

    /**
     * @return a hash code consistent with {@link #equals(Object)}
     */
    @Override
    public int hashCode() {
        return Objects.hash(customerName, quantity, joinedAtMillis);
    }

    /**
     * @return a readable summary, e.g. "FanTicketRequest[Alice, 2 tickets]"
     */
    @Override
    public String toString() {
        return "FanTicketRequest[" + customerName + ", " + quantity
            + (quantity == 1 ? " ticket]" : " tickets]");
    }
}
