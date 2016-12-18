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
        if(args.length<=2)
        {
            System.out.println("Improper Arguments Please specify number of Process and Number of iterations");
            return;
        }
        String bindingLocation;
        int thisProcID= Integer.parseInt(args[0]);
//        boolean ifServer= Boolean.parseBoolean(args[1]);
        String rmiHostLocation = args[2];
        bindingLocation="rmi://"+rmiHostLocation+":1099/";
        AGEProc p= new AGEProc();
        try {
            Naming.rebind(bindingLocation, p);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }


}


