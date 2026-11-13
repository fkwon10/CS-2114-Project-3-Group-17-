package encore.model;

public class QueueStatusDTO
{
    private final String frontCustomerName;
    private final int position;
    private final int queueLength;
    private final int ticketsRemaining;

    public QueueStatusDTO(
        String frontCustomerName,
        int position,
        int queueLength,
        int ticketsRemaining)
    {
        this.frontCustomerName = frontCustomerName;
        this.position = position;
        this.queueLength = queueLength;
        this.ticketsRemaining = ticketsRemaining;
    }


    public String getFrontCustomerName()
    {
        return frontCustomerName;
    }


    public int getPosition()
    {
        return position;
    }


    public int getQueueLength()
    {
        return queueLength;
    }


    public int getTicketsRemaining()
    {
        return ticketsRemaining;
    }
}
