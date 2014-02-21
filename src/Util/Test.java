package Util;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import Database.DailyPrice;
import Database.DataVendor;
import Database.Exchange;
import Database.Symbol;
import Dispatcher.DispatcherInterface.Job;

public class Test 
{
 
  public static void main(String[] args) throws IOException 
  {
	  /*
	  
	  	String startTime;
	  	String endTime;
	  
	    //Starting transaction
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("equities_master");
	    EntityManager em = factory.createEntityManager();
	    
	    em.getTransaction().begin();
		
		//Get all symbols
	    TypedQuery<Symbol> symbolQuery = em.createQuery("SELECT s FROM symbol s ", Symbol.class);
	    List<Symbol> symbolList =  symbolQuery.getResultList();
		
	    //Get most recent date in database
	    TypedQuery<Timestamp> mostRecentTimeQuery = em.createQuery("SELECT MAX(dp.priceDate_) FROM daily_price dp ", Timestamp.class);
	    Timestamp mostRecentTime =  mostRecentTimeQuery.getSingleResult();
	    Date mostRecentDate = new Date(mostRecentTime.getTime());
	    
	    //Closing transaction
	    em.getTransaction().commit();
	    em.close();
	    
	    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New-York"));
	    Date now = cal.getTime();
	    
	    if(now.after(mostRecentDate))
	    {
	    	//Construct time interval
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
	    	
	    	if(now.getHours() >= 18)
	    	{
	    		cal.setTime(mostRecentDate);
	    		cal.add(Calendar.DATE, 1);
	    		
	    		startTime = df.format(cal.getTime());
	    		endTime = df.format(now);  		
         	}
	    	else
	    	{
	    		cal.setTime(mostRecentDate);
	    		cal.add(Calendar.DATE, 1);
	    		
	    		startTime = df.format(cal.getTime());
	    		
	    		cal.setTime(now);
	    		cal.add(Calendar.DATE, -1);
	    		
	    		endTime = df.format(cal.getTime());
	    	}
	    	
	    	
	    	//Create ticker vector
	    	Vector<String> symbolTicker = new Vector<String>();
	    	
		    for(int i=0; i<symbolList.size(); ++i)
		    {
		    	symbolTicker.add(i, "\"" + symbolList.get(i).getTicker() + "\"");
		    }

		    //Process
		    result = client.Process(symbolTicker, args[4], args[5], null, Job.UPDATE_NEW_DATA);
	    	
	    }
	    else
	    {
	    	System.out.println("Problem!");
	    }
	    
	   */
  }
} 
