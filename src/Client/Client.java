package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import Util.YahooAPI;

import Database.ChartInterval;
import Database.Stock;
import Database.ChartInterval.Currency;
import Database.ChartInterval.TimeFrame;
import Database.Stock.Exchange;
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
			Exchange exchange=null;
		
			if(args[3].equals("NYSE") || args[3].equals("nyse"))
			{
				exchange=Exchange.NYSE;
			}
			else if(args[3].equals("NASDAQ") || args[3].equals("nasdaq"))
			{
				exchange=Exchange.NASDAQ;
			}
			else
			{
				System.err.println("Error exchange not supported");
				System.exit(-1);
			}
			
			//Creating transaction
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/stocks.odb");
		    EntityManager em = emf.createEntityManager();
		
		    //Starting transaction
		    em.getTransaction().begin();
		    
		    //Fetch symbols
		    TypedQuery<String> query1 = em.createQuery("SELECT s.symbol_ FROM STOCK s WHERE s.exchange_=:exchange", String.class);
		    query1.setParameter("exchange", exchange);
		    List<String> symbols = query1.getResultList();
		    
		    //Reformat strings
		    for(int i=0; i<symbols.size(); ++i)
		    {
		    	symbols.set(i, "\"" + symbols.get(i) + "\"");
		    }
		    
		    Vector<String> symbol = new Vector<String>(symbols);
		        
		    //Process
		    Map<String,Map<String,Map<String,Float>>> result = client.Process(symbol, args[4], args[5], exchange, Job.UPDATE);
		    
		    //Fetch stocks
		    TypedQuery<Stock> query2 = em.createQuery("SELECT s FROM STOCK s WHERE s.exchange_=:exchange", Stock.class);
		    query2.setParameter("exchange", exchange);
		    List<Stock> stocks = query2.getResultList();
		    
		    client.UpdateDatabase(result, new Vector<Stock>(stocks), TimeFrame.DAY, Currency.USD);
			
		}
		else if(args[2].equals("-f"))
		{
			Exchange exchange=null;
			Vector<String> workLoad=null;
			
			if(args[3].equals("NYSE") || args[3].equals("nyse"))
			{
				exchange=Exchange.NYSE;
				workLoad=YahooAPI.getNYSESymbols();
			}
			else if(args[3].equals("NASDAQ") || args[3].equals("nasdaq"))
			{
				exchange=Exchange.NASDAQ;
				workLoad=YahooAPI.getNasdaqSymbols();
			}
			else
			{
				System.err.println("Error exchange not supported");
				System.exit(-1);
			}
			
			Map<String, Map<String,Map<String,Float>>> result = client.Process(workLoad,args[4],args[5],exchange,Job.FILL);
			
			client.FillDatabase(result,TimeFrame.DAY,Currency.USD,exchange);
			
			/*
			if(result > 0)
			{
				System.out.println("Results retreived, number of stocks : " + Integer.toString(result));
			}
			else
			{
				System.out.println("Dispatcher couldn't do job");
			}
			*/
		
		}
		else if(args[2].equals("-n"))
		{
			System.out.println("Getting number of workers..");
			int nbOfWorkers = client.GetNbOfWorkers();
			System.out.println("Nb of workers : " + Integer.toString(nbOfWorkers));
		}
	
	}
	
	public Map<String, Map<String,Map<String,Float>>> Process(Vector<String> workLoad, String start, String end, Exchange exchange, Job job)
	{
		Map<String, Map<String,Map<String,Float>>> result=null;
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
	
	private void FillDatabase(Map<String, Map<String,Map<String,Float>>> data, TimeFrame timeFrame, Currency currency, Exchange exchange)
	{
		//Creating transaction
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/stocks.odb");
	    EntityManager em = emf.createEntityManager();
	
	    //Starting transaction
	    em.getTransaction().begin();
		
		//Fill stock
		for (Entry<String, Map<String, Map<String, Float>>> stock : data.entrySet()) 
		{
			//Create Stock 
			Stock newStock = new Stock(stock.getKey().substring(1, stock.getKey().length()-1),exchange);
			
			//Fill chart
			for (Map.Entry<String, Map<String,Float>> stockData : stock.getValue().entrySet()) 
			{
     			ChartInterval chart = new ChartInterval(timeFrame,Timestamp.valueOf(stockData.getKey() + " 00:00:00.0"), currency);
				chart.setOpen(stockData.getValue().get("Open"));
				chart.setClose(stockData.getValue().get("Close"));
				chart.setHigh(stockData.getValue().get("High"));
				chart.setLow(stockData.getValue().get("Low"));
				chart.setVolume(stockData.getValue().get("Volume"));
				chart.setAdjclose(stockData.getValue().get("Adj Close"));
				chart.setSymbol(newStock);
				
				//Create Key
				chart.setKey();
				
				//Persist chart objects
				em.persist(chart);
				
				//newStock.addChartInterval(chart);
			}
			
			//Persist stock object
			em.persist(newStock);
			
		}
				
		//End transaction
		em.getTransaction().commit();
		
	}

    private void UpdateDatabase(Map<String, Map<String,Map<String,Float>>> data, Vector<Stock> stocks, TimeFrame timeFrame, Currency currency)
	{

    	//Creating transaction
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/stocks.odb");
	    EntityManager em = emf.createEntityManager();
	    
	    //Start transaction
	    em.getTransaction().begin();
	    
	    //For every stock
  		for (int i=0; i<stocks.size(); ++i) 
  		{   
  			
  			if(data.get("\""  + stocks.get(i).getSymbol() + "\"")!=null)
		    {
	  			//Add new chart Intervals
	  			for (Map.Entry<String, Map<String,Float>> stockData : data.get("\""  + stocks.get(i).getSymbol() + "\"").entrySet()) 
	  			{
	  				
		       			ChartInterval chart = new ChartInterval(timeFrame,Timestamp.valueOf(stockData.getKey() + " 00:00:00.0"), currency);
		  				chart.setOpen(stockData.getValue().get("Open"));
		  				chart.setClose(stockData.getValue().get("Close"));
		  				chart.setHigh(stockData.getValue().get("High"));
		  				chart.setLow(stockData.getValue().get("Low"));
		  				chart.setVolume(stockData.getValue().get("Volume"));
		  				chart.setAdjclose(stockData.getValue().get("Adj Close"));
		  				chart.setSymbol(stocks.get(i));
		  				
		  				//Create Key
		  				chart.setKey();
		  				
		  				//Persist chart objects
		  				em.persist(chart);	  			
	  			}
			}
			else
			{
				System.err.println("Couldn't find : " + "\""  + stocks.get(i).getSymbol() + "\"");
			}
 			
  		}
	    
	    //End transaction
	    em.getTransaction().commit();
	}
}
