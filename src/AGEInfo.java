import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Anirudh on 18/12/16.
 */
public class AGEInfo extends UnicastRemoteObject implements AGEInfoInterface
{
    private int minExpectedNumProcs;
    private int numProcs;
    private ProcId[] procBindings;

    public AGEInfo(int minExpectedNumProcs)throws RemoteException
    {
        this.numProcs = 0;
        this.minExpectedNumProcs = minExpectedNumProcs;
        procBindings = new ProcId[minExpectedNumProcs];
    }

    public synchronized ProcId requestProcId(String binding)
    {
        ProcId pId = new ProcId(numProcs++, binding);
        procBindings[pId.getId()] = pId;
        return pId;
    }

    public boolean canStart()
    {
        return numProcs >= minExpectedNumProcs;
    }
    public int getNumberOfProcs()
    {
        return numProcs;
    }

    public ProcId getBindingOf(int id)
    {
        return procBindings[id];
    }
}
