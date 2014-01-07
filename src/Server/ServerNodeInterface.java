package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ServerNodeInterface extends Remote
{
	public void Process(Vector<String> workLoad, String startTime, String endTime) throws RemoteException;
	public boolean IsAlive() throws RemoteException;
}
