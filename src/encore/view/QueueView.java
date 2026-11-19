package encore.view;

import encore.model.QueueStatusDTO;

public interface QueueView extends QueueObserver {
    void showMenu();
    int readMenuChoice();
    String promptCustomerName();
    String promptMembershipTier();
    int promptTicketQuantity();
    boolean promptPartialStockDecision(String customerName, int requested, int available);
    void showReceipt(String customerName, int quantityPurchased, int remainingTickets);
    void showSoldOut(int fansStillWaiting);
    void showStatus(QueueStatusDTO status);
    void showMessage(String message);
}