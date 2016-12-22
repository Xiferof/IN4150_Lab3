import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by Anirudh on 18/12/16.
 */
public class ServerMain
{

    public static void main(String args[]) throws RemoteException
    {
        if(args.length==0)
        {
            System.out.println("Improper Arguments Please specify minimum number of expected processes.");
            return;
        }
        int minExpectedNumProcs= Integer.parseInt(args[0]);

        try
        {
            System.setProperty("java.security.policy","file:./server.policy");
            if (System.getSecurityManager() == null)
            {
                System.setSecurityManager(new RMISecurityManager());
            }
        } catch (Exception ex)
        {
            System.out.println(ex);
        }

        //Create RMI registry only if Server
        try
        {
            LocateRegistry.createRegistry(1099);
            System.out.println("Created Registry");
        } catch (RemoteException e)
        {
            System.out.println("Already Running Binding");
        }
        AGEInfo infoObject= new AGEInfo(minExpectedNumProcs);

        //Bind your information object on the Server
        try
        {
            Naming.rebind("rmi://localhost:1099/info", infoObject);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        System.out.println("\nInfo Object binded to Server");

    }
}
