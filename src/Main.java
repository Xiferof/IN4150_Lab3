import java.rmi.Naming;
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
      //  int thisProcID= Integer.parseInt(args[0]);
//        boolean ifServer= Boolean.parseBoolean(args[1]);

        String rmiHostLocation = args[0];

        bindingLocation="rmi://"+rmiHostLocation+":1099/";

        int thisProcID = -1;

        try
        {
            AGEInfoInterface infoStub = (AGEInfoInterface) Naming.lookup(bindingLocation+"info");
            thisProcID = infoStub.requestProcId();
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
        AGEProc p= new AGEProc(thisProcID);
        try
        {
            Naming.rebind(bindingLocation+thisProcID, p);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }


}


