package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import Util.YahooAPI;

import Database.DailyPrice;
import Database.Symbol;
import Dispatcher.DispatcherInterface;
import Dispatcher.DispatcherInterface.Job;

public class Client 
{
	private DispatcherInterface dispatcher_;
		
	public static void main(String[] args)
	{
		Client client = new Client();
		client.loadDispatcherStub(args[0],Integer.valueOf(args[1]));
		  	
		if(args[2].equals("-u"))
		{
			Dispatcher.DispatcherInterface.Exchange exchange=null;
		
			if(args[3].equals("NYSE") || args[3].equals("nyse"))
			{
				exchange=Dispatcher.DispatcherInterface.Exchange.NYSE;
			}
			else if(args[3].equals("NASDAQ") || args[3].equals("nasdaq"))
			{
				exchange=Dispatcher.DispatcherInterface.Exchange.NASDAQ;
			}
			else
			{
				System.err.println("Error exchange not supported");
				System.exit(-1);
			}
			
			//Starting transaction
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("equities_master");
		    EntityManager em = factory.createEntityManager();
		    
		    em.getTransaction().begin();
			
			//Retreive exchange entry in DB
		    TypedQuery<Database.Exchange> exchangeQuery = em.createQuery("SELECT e FROM exchange e WHERE e.abbrev_=:exchange", Database.Exchange.class);
		    exchangeQuery.setParameter("exchange", exchange.toString());
		    Database.Exchange exchangeEntry = exchangeQuery.getSingleResult();

		    //Retreive stocks
		    TypedQuery<String> symbolQuery = em.createQuery("SELECT s.ticker_ FROM symbol s WHERE s.exchange_=:exchange", String.class);
		    symbolQuery.setParameter("exchange", exchangeEntry);
		    List<String> symbolList =  symbolQuery.getResultList();
		    	    
		    //Reformat strings
		    for(int i=0; i<symbolList.size(); ++i)
		    {
               	symbolList.set(i, "\"" + symbolList.get(i) + "\"");
		    }

		    //Process
		    int result = client.Process(new Vector<String>(symbolList), args[4], args[5], exchange, Job.UPDATE_HISTORIC_DATA);
		    
			if(result > 0)
			{
				System.out.println("Database successfully updated");
			}
			else
			{
				System.out.println("Dispatcher couldn't do job");
			}
		    
     	    em.getTransaction().commit();
		    em.close();
		}
		else if(args[2].equals("-f"))
		{
			Dispatcher.DispatcherInterface.Exchange exchange=null;
			Vector<String> workLoad=null;
			
			if(args[3].equals("NYSE") || args[3].equals("nyse"))
			{
				exchange=Dispatcher.DispatcherInterface.Exchange.NYSE;
				workLoad=YahooAPI.getNYSESymbols();
			}
			else if(args[3].equals("NASDAQ") || args[3].equals("nasdaq"))
			{
				exchange=Dispatcher.DispatcherInterface.Exchange.NASDAQ;
				workLoad=YahooAPI.getNasdaqSymbols();
			}
			else
			{
				System.err.println("Error exchange not supported");
				System.exit(-1);
			}
			
			int result = client.Process(workLoad,args[4],args[5],exchange,Job.FILL_DATABASE);

			if(result > 0)
			{
				System.out.println("Database successfully filled");
			}
			else
			{
				System.out.println("Dispatcher couldn't do job");
			}

		    
		}
		else if(args[2].equals("-n"))
		{
			System.out.println("Getting number of workers..");
			int nbOfWorkers = client.GetNbOfWorkers();
			System.out.println("Nb of workers : " + Integer.toString(nbOfWorkers));
		}
	
	}
	
	public int Process(Vector<String> workLoad, String start, String end, Dispatcher.DispatcherInterface.Exchange exchange, Job job)
	{
		int result=0;
		String formatedResult="";
		
		try 
		{
			result = dispatcher_.Process(workLoad,start,end,exchange,job);
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
