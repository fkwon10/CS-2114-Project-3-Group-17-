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
            view.showMenu();
            int choice = view.readMenuChoice();

            switch (choice) {
                case 1:
                    String name = view.promptCustomerName();
                    String tier = view.promptMembershipTier();
                    int quantity = view.promptTicketQuantity();
                    onJoinQueue(name, quantity, tier);
                    break;
                case 2:
                    onAdvanceQueue();
                    break;
                case 3:
                    onConfirmPurchase();
                    break;
                case 4:
                    onDeclinePurchase();
                    break;
                case 5:
                    view.showStatus(model.getStatusSnapshot());
                    break;
                case 0:
                    view.showMessage("Goodbye!");
                    running = false;
                    break;
                default:
                    view.showError("Invalid menu option. Please choose 0-5.");
                    break;
            }
        }
    }

    public void onJoinQueue(String name, int quantity, String tier) {
        try {
            model.enqueueFan(name, quantity, tier);
            String cleanName = name.trim();
            String normalizedTier = model.getCustomerTier(cleanName);
            if ("GOLD".equals(normalizedTier)
                && model.getActiveRequest() != null
                && model.getActiveRequest().getCustomerName().equals(cleanName)) {
                view.showMessage(cleanName + " [GOLD VIP] fast-tracked directly to checkout for "
                    + quantity + " ticket(s)!");
            } else {
                view.showMessage(cleanName + " [" + normalizedTier + "] joined the queue for "
                    + quantity + " ticket(s).");
            }
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }

    public void onAdvanceQueue() {
        try {
            FanTicketRequest active = model.stepQueue();
            String tier = model.getCustomerTier(active.getCustomerName());
            view.showMessage("Now serving: " + active.getCustomerName()
                + " [" + tier + "] (" + active.getQuantity() + " ticket(s) requested).");
        } catch (IllegalStateException e) {
            view.showError(e.getMessage());
        }
    }

    public void onConfirmPurchase() {
        FanTicketRequest active = model.getActiveRequest();
        if (active == null) {
            view.showError("No customer is currently checking out.");
            return;
        }

        int requested = active.getQuantity();
        int available = model.getTicketsRemaining();

        if (available == 0) {
            view.showSoldOut(model.getQueueSize());
            model.cancelActiveTransaction();
            return;
        }

        try {
            if (requested <= available) {
                model.confirmPurchase();
                view.showReceipt(active.getCustomerName(), requested, model.getTicketsRemaining());
            } else {
                boolean acceptPartial = view.promptPartialStockDecision(
                    active.getCustomerName(), requested, available);
                if (acceptPartial) {
                    model.confirmPartialPurchase(available);
                    view.showReceipt(active.getCustomerName(), available, model.getTicketsRemaining());
                } else {
                    model.cancelActiveTransaction();
                    view.showMessage("Partial order declined. Transaction cancelled.");
                }
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }

    public void onDeclinePurchase() {
        try {
            FanTicketRequest active = model.getActiveRequest();
            String name = (active != null) ? active.getCustomerName() : "Customer";
            model.cancelActiveTransaction();
            view.showMessage("Transaction cancelled for " + name + ".");
        } catch (IllegalStateException e) {
            view.showError(e.getMessage());
        }
    }
}