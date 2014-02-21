package Dispatcher;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Vector;

public interface DispatcherInterface extends Remote
{
	public enum Job {FILL_DATABASE,UPDATE_HISTORIC_DATA,UPDATE_NEW_DATA};
	public enum Exchange {NYSE,NASDAQ};
	
	public int Register(String hostAdress, String workerName, int rmiPort) throws RemoteException;
	public void Report(int worker,Map<String,Map<String,Map<String,Float>>> result) throws RemoteException;
	public int Process(Vector<String> workLoad,String startTime,String endTime,Exchange exchange, Job job) throws RemoteException;
	public int GetNbOfWorkers() throws RemoteException;
}
