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
    private int numCapReqs;
    private int numAcks;
    private int maxLvl;
    private int numReported;

    public AGEInfo(int minExpectedNumProcs)throws RemoteException
    {
        this.numProcs = 0;
        this.minExpectedNumProcs = minExpectedNumProcs;
        procBindings = new ProcId[minExpectedNumProcs];
        this.numCapReqs = 0;
        this.numAcks = 0;
        this.maxLvl = -1;
        this.numReported = 0;
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

    public synchronized void logResults(int numCapReqs, int numAcks, int lvl, int numCapt)
    {
        this.numCapReqs += numCapReqs;
        this.numAcks += numAcks;
        if(lvl > maxLvl)
            maxLvl = lvl;
        numReported++;
        if(numReported >= minExpectedNumProcs)
        {
            System.out.println(report());
        }
    }

    public String report()
    {
        return "NumCapReqs:" + numCapReqs + ", NumAcks:" + numAcks + ", maxLvl:" + maxLvl;
    }
}
