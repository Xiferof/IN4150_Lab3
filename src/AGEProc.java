import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
/**
 * Created by Anirudh on 18/12/16.
 */
public class AGEProc extends UnicastRemoteObject implements AGEProcInterface
{
    private static final double PERCENT_CANDIDATES = 0.2;

    // list of links to be traversed
    private boolean[] traversal;
    // this processes ID
    private ProcId procId;
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
    // String for info binding
    private String infoBinding;

    // for data collection
    private int captureMessages;
    private int ackMessages;

    public AGEProc()throws RemoteException
    {
        this.procId = new ProcId(-1, "");
        this.level = 0;
        this.numProcs = -1;
        this.killed = false;
        this.elected = false;
        this.candidate = false;
        this.traversal = null;
        this.owner = null;
        this.infoBinding = "";
    }

    public AGEProc(ProcId procID, String infoBinding)throws RemoteException
    {
        this.procId = procID;
        this.level = 0;
        this.numProcs = -1;
        this.killed = false;
        this.elected = false;
        this.candidate = false;
        this.traversal = null;
        this.owner = null;
        this.infoBinding = infoBinding;

        // todo
        captureMessages = 0;
        ackMessages = 0;
    }

    public AGEProc(ProcId procId, int numProcs)throws RemoteException
    {
        this.procId = procId;
        this.level = 0;
        this.numProcs = numProcs;
        this.killed = false;
        this.elected = false;
        this.candidate = false;
        this.traversal = initTraversal();
        this.owner = null;
        this.infoBinding = "";
    }


    public void  run(int numProcs) // todo number of procs in run rather than constructor or as method call?
    {
        System.out.println("Proc" + procId + " STARTING ALGORITHM");

        this.numProcs = numProcs;
        traversal = initTraversal();

        candidate = Math.random() < PERCENT_CANDIDATES;
        if(candidate)
            System.out.println("Proc" + procId + " is a CANIDATE");

        waitTime(getRandTime());
        while(candidate && !killed && hasUntraversed())
        {
            int link = getUntraversed();
            sendRequestTo(link);
            waitTime(getRandTime());
        }
        if(!killed && candidate && !hasUntraversed())
        {
            elected = true;
            System.out.println("========================================\n" +
                               "Proc" + procId + " has been elected!\n" +
                               "========================================");
        }


        waitTime(10000);
        System.out.println("Results Proc" + procId + ": level: " + level + ", CaptureMessages: " + captureMessages + ", ackMessages: " + ackMessages);
    }

    // waits amount of time
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

    // returns a random time between 0 and 1000
    public int getRandTime()
    {
        return ((int)(Math.random() * 1000));
    }

    // initializes the traversal links to false except for its link to self
    private boolean[] initTraversal()
    {
        boolean[] result = new boolean[numProcs];
        for(int i=0; i<numProcs; i++)
        {
            if(i == procId.getId()) // set own link to true;
                result[i] = true;
            else // set other links to false
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
        captureMessages++;
        sendTo(req, id);
    }

    // sends message to id
    private void sendTo(Message message, int id)
    {
        if(this.procId.getId() == id)
        {
            // this should not happen so make sure it is noticeable for debugging
            System.out.println("----------------------------- Proc" + procId + " sending self message -----------------------------");
        }
        // get RMI lookup and send message
        try
        {
            ProcId pId = ((AGEInfoInterface)(Naming.lookup(infoBinding))).getBindingOf(id);
            AGEProcInterface Rcv = (AGEProcInterface) Naming.lookup(pId.getBinding() + pId.getId());
            Rcv.receiveMessage(message);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // receives a message and handles it depending on the situation and message details.
    public void receiveMessage(Message message)
    {
//        System.out.println("Proc" + procId + " received message from " + message.getSenderID());
        if(candidate)
        {
            // Process concedes to this
            if((message.getTimestamp().getPId().getId() == this.procId.getId()) && (!this.killed))
            {
                System.out.println("Proc" + procId + " earns proc" + message.getSenderID() + "'s vote");
                level++;
                traversal[message.getSenderID().getId()] = true;
            }
            // this is less than received message therefore concede to better candidate
            else if(new Timestamp(level, procId).compareTo(message.getTimestamp()) < 0)
            {
                killed = true;
                candidate = false;
                Message concede = new Message(new Timestamp(message.getTimestamp()), procId);
                System.out.println("Proc" + procId + " Sending concede to " + message.getTimestamp().getPId());
                ackMessages++;
                sendTo(concede, message.getTimestamp().getPId().getId());
            }
        }
        else // not candidate
        {
            // Not poisoning ourselves
            if(message.getTimestamp().getPId().getId() != procId.getId())
            {
                // no current owner so this canidate wins
                if (owner == null)
                {
                    owner = new Timestamp(message.getTimestamp());
                    System.out.println("Proc" + procId + " Sending capture confirm to " + owner.getPId());
                    ackMessages++;
                    Message capture = new Message(new Timestamp(owner), this.procId);
                    sendTo(capture, owner.getPId().getId());
                }
                else // have owner therefore compare to see who is better
                {
                    Timestamp potentialOwner = new Timestamp(message.getTimestamp());
                    if (potentialOwner.compareTo(owner) > 0)
                    {
                        // potentialOwner is better than current owner so kill current
                        Message poison = new Message(new Timestamp(potentialOwner), procId);
                        System.out.println("Proc" + procId + " Sending Poison to proc" + owner.getPId() + " for proc" + potentialOwner.getPId());
                        captureMessages++;
                        sendTo(poison, owner.getPId().getId());

                        // and submit to potentialOwner
                        owner = potentialOwner;
                        System.out.println("Proc" + procId + " Sending capture acknowledge to " + owner.getPId());
                        ackMessages++;
                        Message capture = new Message(new Timestamp(owner), procId);
                        sendTo(capture, owner.getPId().getId());
                    }
                }
            }
        }
    }
}
