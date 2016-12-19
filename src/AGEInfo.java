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

    }

    public int requestProcId()
    {
        return numberOfProcces++;
    }
}
