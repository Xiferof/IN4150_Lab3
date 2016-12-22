import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Anirudh on 18/12/16.
 */
public class AGEInfo extends UnicastRemoteObject implements AGEInfoInterface
{
    private int minExpectedNumProcs;
    private int numProcs;

    public AGEInfo(int minExpectedNumProcs)throws RemoteException
    {
        this.numProcs = 0;
        this.minExpectedNumProcs = minExpectedNumProcs;
    }

    public int requestProcId()
    {
        return numProcs++;
    }

    public boolean canStart()
    {
        if(numProcs >= minExpectedNumProcs)
        {
            System.out.println("Starting Algorithm");
            return true;
        }
        return false;
    }
    public int getNumberOfProcs()
    {
        return numProcs;
    }
}
