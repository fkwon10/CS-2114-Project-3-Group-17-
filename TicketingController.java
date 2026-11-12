package encore.controller;

import encore.model.FanTicketRequest;
import encore.model.TicketingModel;
import encore.view.QueueView;

public class TicketingController {
    private final TicketingModel model;
    private final QueueView view;

    public TicketingController(TicketingModel model, QueueView view) {
        if (model == null || view == null) {
            throw new IllegalArgumentException("Model and view cannot be null.");
        }
        this.model = model;
        this.view = view;
    }

    public void run() {
        boolean running = true;
        while (running) {
            int choice = view.showMenu();
            switch (choice) {
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

    public void onJoinRequested(String customerName, int quantity) {
        try {
            model.enqueueFan(customerName, quantity);
            view.showMessage("Successfully joined the queue.");
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }

    public void onStepLineRequested() {
        try {
            FanTicketRequest active = model.stepQueue();
            view.showMessage("Now checking out: " + active.getCustomerName());
        } catch (IllegalStateException e) {
            view.showError(e.getMessage());
        }
    }

    public void onCheckoutConfirmed() {
        FanTicketRequest active = model.getActiveRequest();
        if (active == null) {
            view.showError("No one is checking out.");
            return;
        }

        int remaining = model.getTicketsRemaining();
        if (active.getQuantity() > remaining) {
            if (remaining == 0) {
                model.cancelActiveTransaction();
                view.showError("Sold out! Transaction canceled.");
                return;
            }
            if (view.promptAcceptPartial(active.getQuantity(), remaining)) {
                model.confirmPartialPurchase(remaining);
                view.showReceipt(active.getCustomerName(), remaining);
            } else {
                model.cancelActiveTransaction();
                view.showMessage("Partial offer declined. Checkout canceled.");
            }
            return;
        }

        try {
            model.confirmPurchase();
            view.showReceipt(active.getCustomerName(), active.getQuantity());
        } catch (IllegalStateException e) {
            view.showError(e.getMessage());
        }
    }

    public void onCheckoutDeclined() {
        try {
            model.cancelActiveTransaction();
            view.showMessage("Checkout canceled.");
        } catch (IllegalStateException e) {
            view.showError(e.getMessage());
        }
    }
}
