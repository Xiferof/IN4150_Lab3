import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Anirudh on 18/12/16.
 */
public class AGEProc extends UnicastRemoteObject implements AGEInterface {



    public AGEProc()throws RemoteException {}


    public void recieveMessage(Message message)
    {}
}
