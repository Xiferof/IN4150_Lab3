import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by Anirudh on 18/12/16.
 */
public class ServerMain
{

    public static void main(String args[]) throws RemoteException
    {

            //Create RMI registry only if Server
            try
            {
                LocateRegistry.createRegistry(1099);
                System.out.println("Created Registry");
            } catch (RemoteException e)
            {
                System.out.println("Already Running Binding");
            }
        AGEInfo infoObject= new AGEInfo();

        //Bind your information object on the Server
        try
        {
            Naming.rebind("rmi://localhost:1099/info",infoObject);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        System.out.println("Info Object binded to Server");

    }
}