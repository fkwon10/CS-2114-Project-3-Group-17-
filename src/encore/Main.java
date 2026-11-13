package encore;

import encore.controller.TicketingController;
import encore.model.TicketingModel;
import encore.view.ConsoleQueueView;


public class Main {
    public static void main(String[] args) {
      
        TicketingModel model = new TicketingModel(10); 
        ConsoleQueueView view = new ConsoleQueueView();

        model.addObserver(view);

        TicketingController controller = new TicketingController(model, view);
        controller.run();
    }
}
