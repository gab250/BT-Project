package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Vector;

import Util.YahooAPI;

import Dispatcher.DispatcherInterface;

public class Client 
{
	private DispatcherInterface dispatcher_;
	
	
	public static void main(String[] args)
	{
		
		Client client = new Client();
		client.loadDispatcherStub(args[0],Integer.valueOf(args[1]));
		
		System.out.println("Fetching Symbols...");
		
		Vector<String> workLoad = YahooAPI.getNYSESymbols();
		
		System.out.println("Symbols retreived");
		
		System.out.println(client.Process(workLoad).toString());
	}
	
	public Map<String,Map<String,Map<String,Float>>> Process(Vector<String> workLoad)
	{
		Map<String,Map<String,Map<String,Float>>> result=null;
		String formatedResult="";
		
		try 
		{
			result = dispatcher_.Process(workLoad);
			
		} 
		catch (RemoteException e) 
		{
			System.err.println("Error in dispatcher : " + e.getMessage());
		}
			
		return result;
	}
	
	private void loadDispatcherStub(String hostname,int RMIPort)
	{
		DispatcherInterface stub = null;
		
		try 
		{
			Registry registry = LocateRegistry.getRegistry(hostname,RMIPort);
			stub = (DispatcherInterface) registry.lookup("dispatcher");
			
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		} 
		catch (NotBoundException e) 
		{
			e.printStackTrace();
		}
		
		dispatcher_= stub;
	}
}
