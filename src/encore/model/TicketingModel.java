package encore.model;

import encore.view.QueueObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TicketingModel {
    private final SinglyLinkedQueue<FanTicketRequest> ticketQueue;
    private final Set<String> namesInLine;
    private final Map<String, String> customerTiers;
    private final List<QueueObserver> observers;
    private int ticketsRemaining;
    private FanTicketRequest activeRequest;

    public TicketingModel(int initialInventory) {
        if (initialInventory < 0) {
            throw new IllegalArgumentException("Inventory cannot be negative.");
        }
        this.ticketQueue = new SinglyLinkedQueue<>();
        this.namesInLine = new HashSet<>();
        this.customerTiers = new HashMap<>();
        this.observers = new ArrayList<>();
        this.ticketsRemaining = initialInventory;
        this.activeRequest = null;
    }

    public void addObserver(QueueObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null.");
        }
        observers.add(observer);
    }

    private void notifyObservers() {
        QueueStatusDTO dto = getStatusSnapshot();
        for (QueueObserver observer : observers) {
            observer.update(dto);
        }
    }

    /**
     * Returns the maximum tickets allowed per order for a valid membership tier:
     * CLASSIC -> 4, SILVER -> 6, GOLD -> 8.
     */
    public int getMaxTicketsForTier(String membershipTier) {
        if (membershipTier == null || membershipTier.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Membership tier cannot be blank (choose GOLD, SILVER, or CLASSIC).");
        }
        String normalized = membershipTier.trim().toUpperCase();
        switch (normalized) {
            case "CLASSIC":
                return 4;
            case "SILVER":
                return 6;
            case "GOLD":
                return 8;
            default:
                throw new IllegalArgumentException(
                    "Invalid membership tier: " + membershipTier + ". Must be GOLD, SILVER, or CLASSIC.");
        }
    }

    /**
     * Backward-compatible enqueue defaulting to CLASSIC tier (1-4 tickets).
     */
    public void enqueueFan(String customerName, int quantity) {
        enqueueFan(customerName, quantity, "CLASSIC");
    }

    /**
     * Enqueues a fan with a specific membership tier (GOLD, SILVER, or CLASSIC).
     * Perks:
     * - CLASSIC: up to 4 tickets
     * - SILVER: up to 6 tickets
     * - GOLD: up to 8 tickets + instant fast-track to checkout when window is free and queue is empty
     */
    public void enqueueFan(String customerName, int quantity, String membershipTier) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be blank.");
        }
        String cleanName = customerName.trim();
        if (!cleanName.matches("^[a-zA-Z0-9_]{2,20}$")) {
            throw new IllegalArgumentException("Name must be 2-20 alphanumeric characters.");
        }
        if (namesInLine.contains(cleanName)) {
            throw new IllegalArgumentException("Error: User already waiting in queue.");
        }

        int maxAllowed = getMaxTicketsForTier(membershipTier);
        String normalizedTier = membershipTier.trim().toUpperCase();

        if (quantity < 1 || quantity > maxAllowed) {
            throw new IllegalArgumentException(
                normalizedTier + " tier order quantity must be between 1 and " + maxAllowed + " tickets.");
        }

        FanTicketRequest request = new FanTicketRequest(cleanName, quantity);
        namesInLine.add(cleanName);
        customerTiers.put(cleanName, normalizedTier);

        // GOLD Perk: Instant VIP Fast-Track directly to checkout if window is open and queue is empty
        if ("GOLD".equals(normalizedTier) && activeRequest == null && ticketQueue.isEmpty()) {
            activeRequest = request;
        } else {
            ticketQueue.enqueue(request);
        }
        notifyObservers();
    }

    public String getCustomerTier(String customerName) {
        if (customerName == null) {
            return null;
        }
        return customerTiers.get(customerName.trim());
    }

    public FanTicketRequest stepQueue() {
        if (activeRequest != null) {
            throw new IllegalStateException("Someone is already checking out!");
        }
        if (ticketQueue.isEmpty()) {
            throw new IllegalStateException("No one in queue.");
        }
        activeRequest = ticketQueue.dequeue();
        notifyObservers();
        return activeRequest;
    }

    public void confirmPurchase() {
        if (activeRequest == null) {
            throw new IllegalStateException("No one is checking out.");
        }
        if (activeRequest.getQuantity() > ticketsRemaining) {
            throw new IllegalStateException("Not enough remaining tickets.");
        }
        ticketsRemaining -= activeRequest.getQuantity();
        String completedName = activeRequest.getCustomerName();
        namesInLine.remove(completedName);
        customerTiers.remove(completedName);
        activeRequest = null;
        notifyObservers();
    }

    public void confirmPartialPurchase(int agreedQuantity) {
        if (activeRequest == null) {
            throw new IllegalStateException("No one is checking out.");
        }
        if (agreedQuantity <= 0 || agreedQuantity > ticketsRemaining) {
            throw new IllegalArgumentException("Invalid partial ticket quantity.");
        }
        ticketsRemaining -= agreedQuantity;
        String completedName = activeRequest.getCustomerName();
        namesInLine.remove(completedName);
        customerTiers.remove(completedName);
        activeRequest = null;
        notifyObservers();
    }

    public void cancelActiveTransaction() {
        if (activeRequest == null) {
            throw new IllegalStateException("No one is checking out.");
        }
        String cancelledName = activeRequest.getCustomerName();
        namesInLine.remove(cancelledName);
        customerTiers.remove(cancelledName);
        activeRequest = null;
        notifyObservers();
    }

    public QueueStatusDTO getStatusSnapshot() {
        String frontName = "None";
        int position = 0;

        if (activeRequest != null) {
            frontName = activeRequest.getCustomerName();
            position = 0;
        } else if (!ticketQueue.isEmpty()) {
            frontName = ticketQueue.peek().getCustomerName();
            position = 1;
        }

        return new QueueStatusDTO(frontName, position, ticketQueue.size(), ticketsRemaining);
    }

    public FanTicketRequest getActiveRequest() {
        return activeRequest;
    }

    public int getTicketsRemaining() {
        return ticketsRemaining;
    }

    public int getQueueSize() {
        return ticketQueue.size();
    }
}