package encore.view;

public class TicketingModel
{
    private LinkedQueue<FanTicketRequest> queue;
    private FanTicketRequest activeCustomer;
    private int remainingInventory;
    private List<QueueObserver> observers;

    public TicketingModel(int inventory)
    {
        queue = new LinkedQueue<FanTicketRequest>();
        activeCustomer = new FanTicketRequest();
        remainingInventory = inventory;
        observers = new List<QueueObserver>();
    }


    public void addObserver(QueueObserver observer)
    {
        if (observer == null)
        {
            throw new IllegalArgumentException();
        }
        observers.add(observer);
    }

}
