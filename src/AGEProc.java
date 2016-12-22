import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
/**
 * Created by Anirudh on 18/12/16.
 */
public class AGEProc extends UnicastRemoteObject implements AGEProcInterface
{
    // list of links to be traversed
    private boolean[] traversal;
    // this processes ID
    private int procId;
    // this processes level
    private int level;
    // total number of processes in the network
    private int numProcs;
    // has this process been captured by another process
    private boolean killed;
    // has this process been elected
    private boolean elected;
    // is this process a candidate
    private boolean candidate;
    // this processes owner
    private Timestamp owner;
    // the RMI binding location
    private String bindingLoc;

    public AGEProc()throws RemoteException
    {
        this.procId = -1;
        this.level = 0;
        this.numProcs = -1;
        this.killed = false;
        this.elected = false;
        this.candidate = false;
        this.traversal = null;
        this.owner = null;
        this.bindingLoc = "";
    }

    public AGEProc(int procID, String bindingLoc)throws RemoteException
    {
        this.procId = procID;
        this.level = 0;
        this.numProcs = -1;
        this.killed = false;
        this.elected = false;
        this.candidate = false;
        this.traversal = null;
        this.owner = null;
        this.bindingLoc = bindingLoc;
    }

    public AGEProc(int procId, int numProcs, String bindingLoc)throws RemoteException
    {
        this.procId = procId;
        this.level = 0;
        this.numProcs = numProcs;
        this.killed = false;
        this.elected = false;
        this.candidate = false;
        this.traversal = createTraversal();
        this.owner = null;
        this.bindingLoc = bindingLoc;
    }


    public void  run(int numProcs) // todo number of procs in run rather than constructor or as method call?
    {
        System.out.println("Proc" + procId + " STARTING ALGORITHM");

        this.numProcs = numProcs;
        traversal = createTraversal();

        candidate = Math.random() < 0.2;
        if(candidate)
            System.out.println("Proc" + procId + " is a CANIDATE");

        while(candidate && !killed && hasUntraversed())
        {
            waitTime(getRandTime());
            int link = getUntraversed();
            sendRequestTo(link);
        }
        if(!killed && candidate && !hasUntraversed())
        {
            elected = true;
            System.out.println("========================================\n" +
                               "Proc" + procId + " has been elected!\n" +
                               "========================================");
        }
    }

    public void waitTime(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public int getRandTime()
    {
        return ((int)(Math.random() * 1000));
    }

    // initializes the traversal links to false except for its link to self
    private boolean[] createTraversal()
    {
        boolean[] result = new boolean[numProcs];
        for(int i=0; i<numProcs; i++)
        {
            if(i == procId)
                result[i] = true;
            else
                result[i] = false;
        }
        return result;
    }

    // returns true if there is an untraversed link, false otherwise.
    private boolean hasUntraversed()
    {
        for(int i=0; i<traversal.length; i++)
        {
            if(!traversal[i])
                return true;
        }
        return false;
    }

    // returns the index of a random untraversed link
    private int getUntraversed()
    {
        int ndx = -1;
        do
        {
            ndx = (int)(Math.random() * traversal.length);
        }
        while (traversal[ndx]);
        return ndx;
    }

    // sends a request message to the id
    private void sendRequestTo(int id)
    {
        Message req = new Message(level, procId);
        System.out.println("Proc" + procId + " sending request to " + id + " with level " + level);
        sendTo(req, id);
    }

    // sends a message to the id
    private void sendTo(Message message, int id)
    {
        try
        {
            AGEProcInterface Rcv = (AGEProcInterface) Naming.lookup(bindingLoc + id);
            Rcv.receiveMessage(message);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // receives a message and handles it depending on the situation and message details.
    public void receiveMessage(Message message)
    {
        System.out.println("Proc" + procId + " received message from " + message.getSenderID());
        if(candidate)
        {
            // Process concedes to this
            if((message.getTimestamp().getId() == this.procId) && (!this.killed))
            {
                System.out.println("Proc" + procId + " earns proc" + message.getSenderID() + "'s vote");
                level++;
                traversal[message.getSenderID()] = true;
            }
            // this is less than received message therefore concede to better candidate
            else if(new Timestamp(level, procId).compareTo(message.getTimestamp()) < 0)
            {
                killed = true;
                candidate = false;
                Message concede = new Message(new Timestamp(message.getTimestamp()), procId);
                System.out.println("Proc" + procId + " Sending concede to " + message.getTimestamp().getId());
                sendTo(concede, message.getTimestamp().getId());
            }
        }
        else // not candidate
        {
            if (owner == null)
            {
                owner = new Timestamp(message.getTimestamp());
                System.out.println("Proc" + procId + " Sending capture confirm to " + owner.getId());
                Message capture = new Message(new Timestamp(owner), this.procId);
                sendTo(capture, owner.getId());
            }
            else
            {
                Timestamp potentialOwner = new Timestamp(message.getTimestamp());
                if(potentialOwner.compareTo(owner) > 0)
                {
                    Message poison = new Message(new Timestamp(potentialOwner), procId);
                    System.out.println("Proc" + procId + " Sending Poison to proc" + owner.getId() + " for proc" + potentialOwner.getId());
                    sendTo(poison, owner.getId());

                    owner = potentialOwner;
                    System.out.println("Proc" + procId + " Sending capture acknowledge to " + owner.getId());
                    Message capture = new Message(new Timestamp(owner), procId);
                    sendTo(capture, owner.getId());
                }
            }
        }
    }
}
