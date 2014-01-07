package Dispatcher;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Vector;

import Database.Stock;

public interface DispatcherInterface extends Remote
{
	public enum Job {FILL,UPDATE};
	
	public int Register(String hostAdress, String workerName, int rmiPort) throws RemoteException;
	public void Report(int worker,Map<String,Map<String,Map<String,Float>>> result) throws RemoteException;
	public  Map<String, Map<String,Map<String,Float>>> Process(Vector<String> workLoad,String startTime,String endTime,Stock.Exchange exchange, Job job) throws RemoteException;
	public int GetNbOfWorkers() throws RemoteException;
}
