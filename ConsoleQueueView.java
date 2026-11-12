package encore.view;

import java.io.PrintStream;
import java.util.Scanner;
import encore.model.QueueStatusDTO;

public class ConsoleQueueView implements QueueObserver
{
    private final Scanner in;
    private final PrintStream out;

    public ConsoleQueueView()
    {
        this(new Scanner(System.in), System.out);
    }

    public ConsoleQueueView(Scanner in, PrintStream out)
    {
        if (in == null || out == null)
        {
            throw new IllegalArgumentException(
                "Input and output cannot be null.");
        }
        this.in = in;
        this.out = out;
    }

    @Override
    public void update(QueueStatusDTO status)
    {
        showStatus(status);
    }

    @Override
    public void showError(String message)
    {
        out.println("Error: " + message);
    }

    public void showStatus(QueueStatusDTO status)
    {
        out.println("\n--- Encore Queue Status ---");
        out.println("Fans waiting: " + status.getQueueLength());
        out.println("Next fan: " + (status.getFrontCustomerName() == null
            ? "None" : status.getFrontCustomerName()));
        out.println("Position: " + status.getPosition());
        out.println("Tickets remaining: " + status.getTicketsRemaining());
    }

    public int showMenu()
    {
        out.println("\n=== Encore Ticketing ===");
        out.println("1. Join queue");
        out.println("2. Advance queue");
        out.println("3. Confirm purchase");
        out.println("4. Decline purchase");
        out.println("5. View queue status");
        out.println("0. Exit");
        return readInt("Choose an option: ");
    }

    public String readName()
    {
        out.print("Customer name: ");
        return in.nextLine();
    }

    public int readQuantity()
    {
        return readInt("Tickets requested (1-4): ");
    }

    public int readInt(String prompt)
    {
        while (true)
        {
            out.print(prompt);
            String line = in.nextLine().trim();
            try
            {
                return Integer.parseInt(line);
            }
            catch (NumberFormatException e)
            {
                out.println("Please enter a valid whole number.");
            }
        }
    }

    public void showMessage(String message)
    {
        out.println(message);
    }

    public void showReceipt(String customerName, int quantity)
    {
        out.println("\n--- Encore Receipt ---");
        out.println("Customer: " + customerName);
        out.println("Tickets purchased: " + quantity);
        out.println("Purchase confirmed.");
    }
}