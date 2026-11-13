package encore.model;

import encore.view.QueueObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicketingModel {
    private final SinglyLinkedQueue<FanTicketRequest> ticketQueue;
    private final Set<String> namesInLine;
    private final List<QueueObserver> observers;
    private int ticketsRemaining;
    private FanTicketRequest activeRequest;

    public TicketingModel(int initialInventory) {
        if (initialInventory < 0) {
            throw new IllegalArgumentException("Inventory cannot be negative.");
        }
        this.ticketQueue = new SinglyLinkedQueue<>();
        this.namesInLine = new HashSet<>();
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

    public void enqueueFan(String customerName, int quantity) {
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
        if (quantity < 1 || quantity > 4) {
            throw new IllegalArgumentException("Order quantity must be between 1 and 4 tickets.");
        }

        FanTicketRequest request = new FanTicketRequest(cleanName, quantity);
        ticketQueue.enqueue(request);
        namesInLine.add(cleanName);
        notifyObservers();
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
        namesInLine.remove(activeRequest.getCustomerName());
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
        namesInLine.remove(activeRequest.getCustomerName());
        activeRequest = null;
        notifyObservers();
    }

    public void cancelActiveTransaction() {
        if (activeRequest == null) {
            throw new IllegalStateException("No one is checking out.");
        }
        namesInLine.remove(activeRequest.getCustomerName());
        activeRequest = null;
        notifyObservers();
    }

    public QueueStatusDTO getStatusSnapshot() {
        String frontName = "None";
        int position = 0;

        if (activeRequest != null) {
            frontName = activeRequest.getCustomerName();
            position = 0; // Currently at checkout window
        } else if (!ticketQueue.isEmpty()) {
            frontName = ticketQueue.peek().getCustomerName();
            position = 1; // Next in line
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
