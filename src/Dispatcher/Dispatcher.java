package Dispatcher;


import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Server.ServerNodeInterface;

public class Dispatcher implements DispatcherInterface {
	
	static private Map<Integer,Map<String,Map<String,Map<String,Float>>>> results_;
	static private Lock lock_;
	
	private Map<Integer,ServerNodeInterface> Workers_; 
	private int portRMI_,portDispatcher_;
		
	public static void main(String[] args) throws Exception
	{
		System.setProperty("java.rmi.server.hostname", "54.213.89.227");
		Dispatcher dispatcher = new Dispatcher(Integer.valueOf(args[0].trim()),Integer.valueOf(args[1].trim()));
		dispatcher.run();
	}	
	
	public Dispatcher(int portRMI, int portDispatcher)
	{
		Workers_ = new HashMap<Integer,ServerNodeInterface>();
		results_ = new HashMap<Integer,Map<String,Map<String,Map<String,Float>>>>();
		lock_ = new ReentrantLock();
		portRMI_ = portRMI;
		portDispatcher_ = portDispatcher;
	}
	
	@Override
	public int Register(String hostAdress, String workerName, int rmiPort) throws RemoteException 
	{
		ServerNodeInterface newWorker=null;
		int newId=0;
		
		//Import ServerNodeInterface Object
		try 
		{
			newWorker = loadServerNodeStub(hostAdress,workerName, rmiPort);
		} 
		catch (NotBoundException e) 
		{
			System.err.println("Error in register (Dispatcher) : " + e.getMessage()); 

		}
		catch(RemoteException e)
		{
			System.err.println("Error in register (Dispatcher) : " + e.getMessage()); 
		}
		
		//Save newWorker to active Worker map
		if(newWorker != null && !Workers_.containsValue(newWorker))
		{
			ClearDeadWorkers();
			
			//Check for dead spots
			if(Workers_.containsValue(null))
			{
				for(Entry<Integer,ServerNodeInterface> entry : Workers_.entrySet())
				{
					if(entry.getValue() == null)
					{
						newId = entry.getKey();
												
						break;
					}
				}
			}
			else
			{
				newId = Workers_.size() + 1;
			}	
					
			Workers_.put(newId, newWorker);
			
			System.out.println("Registered : " + workerName + "@" + hostAdress);
			System.out.println("Number of workers : " + Integer.toString(Workers_.size()));
		
		}
		else
		{
			System.out.println("Couldn't register : " + workerName + "@" + hostAdress);
		}
		
		//Return NewWorker Id 
		if(newId >0)
			return newId;
		else
			return -1;
	}

	@Override
	public void Report(int worker, Map<String,Map<String,Map<String,Float>>> result) throws RemoteException 
	{
		PutResult(worker,result);
	}

	@Override
	public int Process(Vector<String> workLoad) throws RemoteException 
	{
		//Get time
		Calendar cal = Calendar.getInstance();
    	SimpleDateFormat currentTime = new SimpleDateFormat("y:M:dd:HH:mm:ss");
		
		System.out.println("[ Starting job @ " + currentTime.format(Calendar.getInstance().getTime()) + " ]");
		
		long start = System.nanoTime();
	
		//Clear results 
		results_ = new HashMap<Integer,Map<String,Map<String,Map<String,Float>>>>();
		Map<String, Map<String,Map<String,Float>>> combinedResults = new HashMap<String,Map<String,Map<String,Float>>>();
			
		//Check livelyness of workers
		Vector<Integer> aliveWorkers = new Vector<Integer>();
		ClearDeadWorkers();
		aliveWorkers = GetWorkerAlive();
				
		//Dispatch work
		if(aliveWorkers.size() > 0)
		{
			//Keep History
			Map<Integer,Vector<String>> workDispatchingJournal = new HashMap<Integer,Vector<String>>();
					
			//Initialize vectors of workHistory
			for(int i=0; i<aliveWorkers.size(); ++i)
			{
				workDispatchingJournal.put(aliveWorkers.get(i), new Vector<String>());
			}
			
			//Split work
			Vector<Vector<String>> workLoads = new Vector<Vector<String>>();
		    int workLoadSize = workLoad.size()/aliveWorkers.size();
		    
		    for(int i=0; i<workLoad.size(); ++i)
		    {
		    	workLoads.add(new Vector<String>());
		    	
		    	for(int j=0; j<workLoadSize; ++j)
		    	{
		    		workLoads.get(i).add(workLoad.remove(0));
		    	}
		    }
		    		    
		    //Send workloads to Server Nodes
		    try
		    {
  				for(int i=0; i<aliveWorkers.size() ; ++i)
				{
						Workers_.get(aliveWorkers.get(i)).Process(workLoads.get(i));
						workDispatchingJournal.put(aliveWorkers.get(i),workLoads.get(i));
				}
		    }
		    catch(RemoteException e)
		    {
		    	System.err.println("Problem calling process on Node  : " + e.getMessage());
		    }
		    
		    int nbOfSeconds = 0;
		    
		    try 
		    {
	     		//Wait for completion 		
	       		do
	       		{
	       			Thread.sleep(1000);
	       			nbOfSeconds++;
	       			
				}while((GetResultSize() < workDispatchingJournal.size()) && (nbOfSeconds < 180));
	        } 
		    catch (InterruptedException e) 
		    {
				e.printStackTrace();
			}
   			
		    System.out.println("Result size : " + Integer.toString(GetResultSize()) + " workJournal Size : " + Integer.toString(workDispatchingJournal.size()));
		    
		    if(GetResultSize() == workDispatchingJournal.size())
		    {
			    //Merge results
	       		for(Integer key : results_.keySet())
	       		{
	       			combinedResults.putAll(results_.get(key));
	    		}
		    }
		    else if(GetResultSize() > workDispatchingJournal.size())
		    {
		    	System.err.println("Error Result size greater than workJournal size "  + Integer.toString(GetResultSize()) + Integer.toString(workDispatchingJournal.size()));
		    }
		    else
		    {
		    	System.out.println("Some Nodes aren't responding fuckem im out!");
		    	combinedResults = null;
		    }
		}
		else
		{
			combinedResults = null;
		}
		
		long end = System.nanoTime();
		
		System.out.println("Execution time : " + Float.toString((float)((end-start)/1000000.0)) + " ms");
				
		System.out.println("[ Finishing job @ " + currentTime.format(Calendar.getInstance().getTime()) + " ]");
		
		if(combinedResults != null)
		{
			return combinedResults.size();
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public int GetNbOfWorkers() throws RemoteException 
	{
		return GetWorkerAlive().size();
	}

	private void run() throws Exception
	{
		if(System.getSecurityManager() ==  null)
		{
			System.setSecurityManager(new SecurityManager());
		}

		try
		{
			System.out.println("PortDispatcher : " + Integer.toString(portDispatcher_) + " Port RMI : " +  Integer.toString(portRMI_));
			
			DispatcherInterface stub = (DispatcherInterface) UnicastRemoteObject.exportObject(this, portDispatcher_);
			Registry registry = LocateRegistry.getRegistry(portRMI_);
			registry.rebind("dispatcher", stub);
			
			System.out.println("Dispatcher ready");
		}
		catch(ConnectException e)
		{
			System.err.println("Impossible to connect to registry from Dispatcher");
			System.err.println("Erreur : " + e.getMessage());
		}
		catch(Exception e)
		{
			System.err.println("Erreur: " + e.getMessage());
		}
	}

	private ServerNodeInterface loadServerNodeStub(String hostname, String serverNodeName, int rmiPort) throws RemoteException,NotBoundException
	{
		ServerNodeInterface stub = null;

		Registry registry = LocateRegistry.getRegistry(hostname,rmiPort);
		stub = (ServerNodeInterface) registry.lookup(serverNodeName);

		return stub;
	}
	
	private int GetResultSize()
	{
		int size=0;
		lock_.lock();
		
		try
		{
			size = results_.size();			
		}
		finally
		{
			lock_.unlock();
		}
		
		return size;
	}

	private void PutResult(int workerID, Map<String,Map<String,Map<String,Float>>> result)
	{
		lock_.lock();
		
		try
		{
			results_.put(workerID, result);
 		}
		finally
		{
			lock_.unlock();
		}
		
	}
	
	private void ClearDeadWorkers()
	{
		for(int i=1; i<=Workers_.size(); ++i)
		{
			try
			{
				if(Workers_.get(i) != null)
				{
					Workers_.get(i).IsAlive();
				}				
			}
			catch(RemoteException e)
			{
				Workers_.put(i, null);
				continue;
			}
		}
	}

	private Vector<Integer> GetWorkerAlive()
	{
		Vector<Integer> result = new Vector<Integer>();

		for(int i=1; i<=Workers_.size(); ++i)
		{
				if(Workers_.get(i) != null)
				{
					result.add(i);
				}				
		}
		
		return result;
	}

}
