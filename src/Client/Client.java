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
		
		
		if(args[2].equals("-u"))
		{
			System.out.println("Fetching Symbols...");
			
			Vector<String> workLoad = YahooAPI.getNYSESymbols();
			
			System.out.println("Symbols retreived");
			
			int result = client.Process(workLoad);
			
			if(result > 0)
			{
				System.out.println("Results retreived, number of stocks : " + Integer.toString(result));
			}
			else
			{
				System.out.println("Dispatcher couldn't job");
			}
		}
		else if(args[2].equals("-n"))
		{
			System.out.println("Getting number of workers..");
			int nbOfWorkers = client.GetNbOfWorkers();
			System.out.println("Nb of workers : " + Integer.toString(nbOfWorkers));
		}
		
	}
	
	public int Process(Vector<String> workLoad)
	{
		int result=0;
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
	
	public int GetNbOfWorkers()
	{
		int nbOfWorkers=0;
		
		try 
		{
			nbOfWorkers = dispatcher_.GetNbOfWorkers();
			
		} 
		catch (RemoteException e) 
		{
			System.err.println("Error in dispatcher : " + e.getMessage());
		}
			
		return nbOfWorkers;
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
