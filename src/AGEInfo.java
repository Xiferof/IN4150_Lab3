import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Anirudh on 18/12/16.
 */
public class AGEInfo extends UnicastRemoteObject implements AGEInfoInterface
{
    private int numberOfProcces;

    public AGEInfo()throws RemoteException
    {
        numberOfProcces = 0;
    }

    public int requestProcId()
    {
        return numberOfProcces++;
    }

    public boolean canStart()
    {
        return true;
    }
    public int getNumberOfProcs()
    {
        return numberOfProcces;
    }
}
