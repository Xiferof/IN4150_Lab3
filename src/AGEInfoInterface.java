import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Anirudh on 19/12/16.
 */
public interface AGEInfoInterface extends Remote
{
    public ProcId requestProcId(String binding)throws RemoteException;
    public boolean canStart() throws RemoteException;
    public int getNumberOfProcs() throws RemoteException;
    public  ProcId getBindingOf(int id) throws RemoteException;
}
