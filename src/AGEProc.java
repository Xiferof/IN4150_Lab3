import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
/**
 * Created by Anirudh on 18/12/16.
 */
public class AGEProc extends UnicastRemoteObject implements AGEInterface
{

    private boolean[] traversal;
    private int id;
    private int level;
    private int numProcs;
    private boolean killed;
    private boolean elected;
    private boolean canidate;
    private Timestamp owner;
    private Timestamp potentialOwner;

    public AGEProc()throws RemoteException
    {}

    public AGEProc(int id, int numProcs)throws RemoteException
    {
        this.id = id;
        this.level = 0;
        this.numProcs = numProcs;
        this.killed = false;
        this.elected = false;
        this.canidate = false;
        this.traversal = createTraversal();
    }

    private boolean[] createTraversal()
    {
        boolean[] result = new boolean[numProcs];
        for(int i=0; i<numProcs; i++)
        {
            if(i == id)
                result[i] = true;
            else
                result[i] = false;
        }
        return result;
    }

    private boolean hasUntraversed()
    {
        for(int i=0; i<traversal.length; i++)
        {
            if(!traversal[i])
                return true;
        }
        return false;
    }

    public void recieveMessage(Message message)
    {

    }
}
