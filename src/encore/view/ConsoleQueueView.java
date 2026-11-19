package encore.view;

import encore.model.QueueStatusDTO;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleQueueView implements QueueView {
    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleQueueView() {
        this(new Scanner(System.in), System.out);
    }

    public ConsoleQueueView(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
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
        out.print("Enter customer name: ");
        return scanner.nextLine().trim();
    }

    @Override
    public String promptMembershipTier() {
        out.print("Membership tier (GOLD / SILVER / CLASSIC): ");
        return scanner.nextLine().trim();
    }

    @Override
    public int promptTicketQuantity() {
        return readInt("Enter ticket quantity: ");
    }

    @Override
    public boolean promptPartialStockDecision(String customerName, int requested, int available) {
        out.println(customerName + " requested " + requested + " ticket(s), but only "
            + available + " remain.");
        out.print("Accept partial stock of " + available + " ticket(s)? (Y/N): ");
        String response = scanner.nextLine().trim().toUpperCase();
        return response.equals("Y") || response.equals("YES");
    }

    @Override
    public void showReceipt(String customerName, int quantityPurchased, int remainingTickets) {
        out.println("\n--- Purchase Receipt ---");
        out.println("Customer: " + customerName);
        out.println("Tickets Purchased: " + quantityPurchased);
        out.println("Tickets Remaining: " + remainingTickets);
        out.println("------------------------");
    }

    @Override
    public void showSoldOut(int fansStillWaiting) {
        out.println("\n*** EVENT SOLD OUT ***");
        out.println("Fans still waiting in queue: " + fansStillWaiting);
    }

    @Override
    public void showStatus(QueueStatusDTO status) {
        out.println("\n--- Encore Queue Status ---");
        out.println("Front / Active Customer: " + status.getFrontCustomerName());
        out.println("Position: " + status.getPosition());
        out.println("Fans Waiting in Queue: " + status.getQueueLength());
        out.println("Tickets Remaining: " + status.getTicketsRemaining());
        out.println("---------------------------");
    }

    @Override
    public void showMessage(String message) {
        out.println(message);
    }

    @Override
    public void update(QueueStatusDTO status) {
        showStatus(status);
    }

    @Override
    public void showError(String message) {
        out.println("Error: " + message);
    }

    private int readInt(String prompt) {
        while (true) {
            out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                out.println("Error: Please enter a valid integer.");
            }
        }
    }
}