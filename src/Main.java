import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.InputMismatchException;

/**
 * Created by Anirudh on 18/12/16.
 */
public class Main {

    public static void main(String args[]) throws RemoteException
    {
        if(args.length==0)
        {
            System.out.println("Improper Arguments Please specify rmi host IP");
            return;
        }
        String bindingLocation;

//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new RMISecurityManager()); }

        //  int thisProcID= Integer.parseInt(args[0]);
//        boolean ifServer= Boolean.parseBoolean(args[1]);

        String rmiHostLocation = args[0];

        bindingLocation="rmi://"+rmiHostLocation+":1099/";

        int thisProcID = -1;

        try
        {
//            System.out.println(thisProcID + "TESTING");
//            AGEInfoInterface infoStub = ((AGEInfoInterface)( Naming.lookup(bindingLocation+"info")));
//            System.out.println("lookup success");
//            thisProcID = infoStub.requestProcId();
//            System.out.println("Got proc id for " + thisProcID);
            System.out.println(thisProcID + "TESTING");
            thisProcID = ((AGEInfoInterface)(Naming.lookup(bindingLocation+"info"))).requestProcId();
            System.out.println("Got proc id for " + thisProcID);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if(thisProcID == -1)
        {
            System.out.println("Could Not Get Proc Id");
            return;
        }
        System.out.println("Binding with Proc ID = "+thisProcID);
        AGEProc p= new AGEProc(thisProcID, bindingLocation);
        try
        {
            Naming.rebind(bindingLocation+thisProcID, p);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        try
        {
            AGEInfoInterface infoStub = (AGEInfoInterface) Naming.lookup(bindingLocation+"info");
            while(!infoStub.canStart())
            {
                waitTime(10);
                //Wait Until we can all start
            }
            int numberOfProcs= infoStub.getNumberOfProcs();
            p.run(numberOfProcs);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void waitTime(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static int getRandTime()
    {
        return ((int)(Math.random() * 1000));
    }

}


