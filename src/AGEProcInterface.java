import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
/**
 * Created by Anirudh on 18/12/16.
 */

public interface AGEProcInterface extends Remote {

    public void recieveMessage(Message message) throws RemoteException;

}