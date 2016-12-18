import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by Anirudh on 18/12/16.
 */
public class ServerMain
{

    public static void main(String args[]) throws RemoteException
    {
        AGEInfo infoObject= new AGEInfo();
            //Create RMI registry only if Server
            try
            {
                LocateRegistry.createRegistry(1099);
                System.out.println("Created Registry");
            } catch (RemoteException e)
            {
                System.out.println("Already Running Binding");
            }


    }
}
