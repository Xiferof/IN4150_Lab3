import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteObjectInvocationHandler;

/**
 * Created by Anirudh on 18/12/16.
 */
public class Main {

    public static void main(String args[]) throws RemoteException
    {
        if(args.length<=2)
        {
            System.out.println("Improper Arguments Please specify number of Process and Number of iterations");
            return;
        }

        //Create RMI registry
        try
        {
            LocateRegistry.createRegistry(1099);
            System.out.println("Created Registry");
        }
        catch(RemoteException e)
        {
            System.out.println("Already Running Binding");
        }
        AGEProc p= new AGEProc();
        try {
            Naming.rebind("rmi://localhost:1099/", p);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }


}


