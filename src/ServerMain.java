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
        int minExpectedNumProcs = -1;
        boolean runInfo = false;

        if(args.length < 1)
        {
            System.out.println("Needs info ip as argument");
            return;
        }
        String infoIp = args[0];

        if(args.length >= 3)
        {
            minExpectedNumProcs= Integer.parseInt(args[1]);
            String runInfoStr = args[2];
            runInfo = runInfoStr.equalsIgnoreCase("y");
        }

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


        if(runInfo)
        {
            System.out.println("Preparing to run info");
            AGEInfo infoObject= new AGEInfo(minExpectedNumProcs);
            //Bind your information object on the Server
            try
            {
                System.setProperty("java.rmi.server.hostname", infoIp);
                Naming.rebind("rmi://" + infoIp + ":1099/info", infoObject);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            System.out.println("\nInfo Object binded to Server");
        }
    }
}
