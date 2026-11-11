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
        remainingInventory = inventory;
        activeCustomer = null;
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


    public void enqueueFan(String customerName, int quantity)
    {
        if (customerName == null || customerName.isBlank())
        {
            throw new IllegalArgumentException("Invalid name selection.");
        }
        if (quantity < 1 || quantity > 4)
        {
            throw new IllegalArgumentException("Invalid ticket amount.");
        }
        for (FanTicketRequest request : queue)
        {
            if (request.getCustomerName().equals(customerName))
            {
                throw new IllegalArgumentException("Name already queued.");
            }
        } // food for thought what if two people of the same name are entered?
          // are we taking in usernames?
        FanTicketRequest request = new FanTicketRequest(customerName, quantity);

        queue.enqueue(request);
    }


    public FanTicketRequest stepQueue()
    {
        if (queue.isEmpty())
        {
            throw new IllegalStateException("No one in queue.");
        }
        if (activeCustomer != null)
        {
            throw new IllegalStateException("Someone is already checking out!");
        }
        FanTicketRequest temp = queue.dequeue();
        activeCustomer = temp;
        return activeCustomer;
    }


    public void confirmPurchase()
    {
        if (activeCustomer == null)
        {
            throw new IllegalStateException("No one is checking out.");
        }
        if (activeCustomer.getQuantity() > remainingInventory)
        {
            throw new IllegalStateException("Not enough remaining tickets");
        }
        remainingInventory -= activeCustomer.getQuantity();
        for (QueueObserver observer : obs)
        {
            // tbd on DTOupdate function
        }
        activeCustomer = null;
    }


    public void cancelActiveTransaction()
    {
        if (activeCustomer == null)
        {
            throw new IllegalStateException("No one is checking out.");
        }
        activeCustomer = null;
    }


    public QueueStatusDTO getStatusSnapshot()
    {
        // tbd on observer class help
    }
}
