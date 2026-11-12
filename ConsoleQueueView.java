package encore.view;

import java.io.PrintStream;
import java.util.Scanner;
import encore.model.QueueStatusDTO;

public class ConsoleQueueView implements QueueView {
    private final Scanner in;
    private final PrintStream out;

    public ConsoleQueueView() {
        this(new Scanner(System.in), System.out);
    }

    public ConsoleQueueView(Scanner in, PrintStream out) {
        if (in == null || out == null) {
            throw new IllegalArgumentException(
                "Input and output cannot be null.");
        }
        this.in = in;
        this.out = out;
    }

    @Override
    public void update(QueueStatusDTO status) {
        showStatus(status);
    }

    @Override
    public void showError(String message) {
        out.println("Error: " + message);
    }

    public void showStatus(QueueStatusDTO status) {
        out.println("\n--- Encore Queue Status ---");
        out.println("Fans waiting: " + status.getQueueLength());
        out.println("Next fan: " + (status.getFrontCustomerName() == null
            ? "None" : status.getFrontCustomerName()));
        out.println("Position: " + status.getPosition());
        out.println("Tickets remaining: " + status.getTicketsRemaining());
    }

    @Override
    public void showMenu() {
        out.println("\n=== Encore Ticketing ===");
        out.println("1. Join queue");
        out.println("2. Advance queue");
        out.println("3. Confirm purchase");
        out.println("4. Decline purchase");
        out.println("5. View queue status");
        out.println("0. Exit");
    }

    @Override
    public int readMenuChoice() {
        return readInt("Choose an option: ");
    }

    @Override
    public String promptCustomerName() {
        out.print("Customer name: ");
        return in.nextLine();
    }

    @Override
    public int promptTicketQuantity() {
        return readInt("Tickets requested (1-4): ");
    }

    @Override
    public void showJoinConfirmation(String customerName, int position) {
        out.println(customerName + " joined the line at position #" + position + ".");
    }

    @Override
    public void showActiveCheckout(String customerName, int quantity) {
        out.println("\n[Checkout Window] " + customerName
            + " is now up to purchase " + quantity + " ticket(s).");
    }

    @Override
    public boolean promptCheckoutDecision() {
        out.print("Confirm purchase? (Y/N): ");
        String response = in.nextLine().trim();
        return response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("YES");
    }

    @Override
    public boolean promptAcceptPartial(int requested, int available) {
        out.print("Requested " + requested + " tickets, but only " + available
            + " remain. Accept partial stock? (Y/N): ");
        String response = in.nextLine().trim();
        return response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("YES");
    }

    @Override
    public void showReceipt(String customerName, int quantity, int ticketsRemaining) {
        out.println("\n--- Encore Receipt ---");
        out.println("Customer: " + customerName);
        out.println("Tickets purchased: " + quantity);
        out.println("Tickets remaining: " + ticketsRemaining);
        out.println("Purchase confirmed.");
    }

    @Override
    public void showCancellation(String customerName) {
        out.println(customerName + "'s transaction was canceled. Checkout window is now free.");
    }

    @Override
    public void showSoldOut(int fansStillWaiting) {
        out.println("SOLD OUT! No tickets remain (" + fansStillWaiting + " fan(s) still waiting).");
    }

    @Override
    public void showMessage(String message) {
        out.println(message);
    }

    public int readInt(String prompt) {
        while (true) {
            out.print(prompt);
            String line = in.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                out.println("Please enter a valid whole number.");
            }
        }
    }
}
