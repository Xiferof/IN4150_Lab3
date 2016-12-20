import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Anirudh on 19/12/16.
 */
public interface AGEInfoInterface extends Remote
{
    public int requestProcId()throws RemoteException;
    public boolean canStart() throws RemoteException;
    public int getNumberOfProcs() throws RemoteException;
}
