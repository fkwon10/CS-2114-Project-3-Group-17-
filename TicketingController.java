package encore.controller;

import encore.model.FanTicketRequest;
import encore.model.TicketingModel;
import encore.view.ConsoleQueueView;


public class TicketingController
{
    private final TicketingModel model;
    private final ConsoleQueueView view;
    private FanTicketRequest checkoutRequest;

    public TicketingController(TicketingModel model, ConsoleQueueView view)
    {
        if (model == null || view == null)
        {
            throw new IllegalArgumentException(
                "Model and view cannot be null.");
        }
        this.model = model;
        this.view = view;
    }

    public void run()
    {
        boolean running = true;
        while (running)
        {
            int choice = view.showMenu();
            switch (choice)
            {
                case 1:
                    onJoinRequested(view.readName(), view.readQuantity());
                    break;
                case 2:
                    onStepLineRequested();
                    break;
                case 3:
                    onCheckoutConfirmed();
                    break;
                case 4:
                    onCheckoutDeclined();
                    break;
                case 5:
                    view.showStatus(model.getStatusSnapshot());
                    break;
                case 0:
                    running = false;
                    view.showMessage("Goodbye!");
                    break;
                default:
                    view.showError("Invalid menu option.");
                    break;
            }
        }
    }

    public void onJoinRequested(String customerName, int quantity)
    {
        try
        {
            model.enqueueFan(customerName, quantity);
            view.showMessage("Successfully joined the queue.");
        }
        catch (IllegalArgumentException e)
        {
            view.showError(e.getMessage());
        }
    }

    public void onStepLineRequested()
    {
        try
        {
            checkoutRequest = model.stepQueue();
            view.showMessage("Now checking out: "
                + checkoutRequest.getCustomerName());
        }
        catch (IllegalStateException e)
        {
            view.showError(e.getMessage());
        }
    }

    public void onCheckoutConfirmed()
    {
        try
        {
            model.confirmPurchase();
            if (checkoutRequest != null)
            {
                view.showReceipt(checkoutRequest.getCustomerName(),
                    checkoutRequest.getQuantity());
            }
            checkoutRequest = null;
        }
        catch (IllegalStateException e)
        {
            view.showError(e.getMessage());
        }
    }

    public void onCheckoutDeclined()
    {
        try
        {
            model.cancelActiveTransaction();
            checkoutRequest = null;
            view.showMessage("Checkout canceled.");
        }
        catch (IllegalStateException e)
        {
            view.showError(e.getMessage());
        }
    }
}