package encore.view;

import encore.model.QueueStatusDTO;

/**
 * View contract for the Encore ticketing queue system.
 *
 * @author Rafik Khismatov
 * @version 2026.11.12
 */
public interface QueueView extends QueueObserver {

    void showMenu();

    int readMenuChoice();

    String promptCustomerName();

    int promptTicketQuantity();

    void showJoinConfirmation(String customerName, int position);

    void showActiveCheckout(String customerName, int quantity);

    boolean promptCheckoutDecision();

    boolean promptAcceptPartial(int requested, int available);

    void showReceipt(String customerName, int quantity, int ticketsRemaining);

    void showCancellation(String customerName);

    void showSoldOut(int fansStillWaiting);

    void showMessage(String message);

    void showStatus(QueueStatusDTO status);
}