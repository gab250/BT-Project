package Util;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import Database.DataVendor;
import Database.Exchange;
import Database.Symbol;

public class Test 
{
 
  public static void main(String[] args) throws IOException 
  {
	  EntityManagerFactory factory = Persistence.createEntityManagerFactory("equities_master");
      EntityManager em = factory.createEntityManager();
    
      em.getTransaction().begin();
      
      //DataVendor vendor = new DataVendor("Company X", "http://xyz.com/","support@xyz.com",Timestamp.valueOf("2014-01-01 00:00:00"),Timestamp.valueOf("2014-01-01 00:00:00"));
      //Exchange exchange = new Exchange("NASDAQ","National Association of Securities Dealers Automated Quotations","New-York","USA","USD",Time.valueOf("00:00:00"),Timestamp.valueOf("2014-01-06 21:09:00"),Timestamp.valueOf("2014-01-06 21:09:00"));
      
      //Persist stock object
      //em.persist(exchange);
      
      
      //Fetch vendors
      TypedQuery<Symbol> query1 = em.createQuery("SELECT d FROM symbol d", Symbol.class);
      List<Symbol> symbols = query1.getResultList();
      
      
      /*
      Map<String,Map<String,Float>> result = YahooAPI.getHistoricalData("\"GOOG\"", "2013-12-20", "2013-12-24");
      
      em.getTransaction().begin();
      
      //Create Stock 
      Stock newStock = new Stock("GOOG",Exchange.NASDAQ);
    	
      //Persist stock object
      em.persist(newStock);
      /*
      //Create Chart Interval 
      ChartInterval chart = new ChartInterval(TimeFrame.DAY,Timestamp.valueOf("2013-12-24 00:00:00.0"), Currency.USD);
	  chart.setOpen(result.get("2013-12-24").get("Open"));
	  chart.setClose(result.get("2013-12-24").get("Close"));
	  chart.setHigh(result.get("2013-12-24").get("High"));
	  chart.setLow(result.get("2013-12-24").get("Low"));
	  chart.setVolume(result.get("2013-12-24").get("Volume"));
	  chart.setAdjclose(result.get("2013-12-24").get("Adj Close"));
	  chart.setSymbol(newStock);
		
	  //Create Key
	  chart.setKey();
	
	  //Persist chart objects
	  em.persist(chart);
      */
	  
      em.getTransaction().commit();
      em.close();
  }
} 
