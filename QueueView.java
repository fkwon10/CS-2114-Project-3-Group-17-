/**
 * <p>Implementations handle terminal-only problems themselves. For example,
 * a console implementation catches {@code InputMismatchException} on numeric
 * prompts, clears the bad input, and asks again. Business-rule errors (bad
 * quantity, duplicate name, blank name) are not checked here. The model
 * throws them and the controller passes the message to
 * {@code QueueObserver.showError}.
 *
 * @author Rafik Khismatov
 * @version 2026.11.12
 */
public interface QueueView {
 
    // ----------------------------------------------------------
    // Menu
    // ----------------------------------------------------------
 
    /**
     * Displays the main menu of available commands 
     */
    void showMenu();
 
    /**
     * Reads the user's menu selection. Implementations must re-prompt on
     * non-numeric input instead of throwing.
     *
     * @return the number the user entered
     */
    int readMenuChoice();
 
    // ----------------------------------------------------------
    // Joining the line
    // ----------------------------------------------------------
 
    /**
     * Prompts for and reads the fan's name. The raw text is returned as
     * typed; the model trims and validates it.
     *
     * @return the name the user entered
     */
    String promptCustomerName();
 
    /**
     * Prompts for and reads the requested ticket quantity. Implementations
     * must re-prompt on non-numeric input. Range checking (1-4) is left to
     * the model.
     *
     * @return the number the user entered
     */
    int promptTicketQuantity();
 
    /**
     * Confirms that a fan joined the line and shows their starting position.
     *
     * @param customerName the fan who joined
     * @param position     the fan's 1-based position in line
     */
    void showJoinConfirmation(String customerName, int position);
 
    // ----------------------------------------------------------
    // Checkout window
    // ----------------------------------------------------------
 
    /**
     * Announces that a fan has reached the front and is now in the
     * checkout window.
     *
     * @param customerName the fan now at the window
     * @param quantity     the number of tickets they requested
     */
    void showActiveCheckout(String customerName, int quantity);
 
    /**
     * Asks the active fan whether to confirm or decline the purchase.
     *
     * @return true to confirm, false to decline
     */
    boolean promptCheckoutDecision();
 
    /**
     * Tells the active fan that fewer tickets remain than they requested,
     * and asks whether they will accept the remaining tickets or give up
     * their place.
     *
     * @param requested the number of tickets the fan asked for
     * @param available the number of tickets still in stock
     * @return true to accept the smaller amount, false to forfeit
     */
    boolean promptAcceptPartial(int requested, int available);
 
    /**
     * Prints a receipt for a completed purchase.
     *
     * @param customerName     the fan who bought tickets
     * @param quantity         the number of tickets purchased
     * @param ticketsRemaining the inventory left after the purchase
     */
    void showReceipt(String customerName, int quantity, int ticketsRemaining);
 

    void showCancellation(String customerName);
 
    // ----------------------------------------------------------
    // Line-wide notices
    // ----------------------------------------------------------
 

    void showSoldOut(int fansStillWaiting);
 
    
    void showMessage(String message);
}
 
