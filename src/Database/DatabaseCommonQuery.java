package Database;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DatabaseCommonQuery 
{
	public static long rowsInDailyPrice()
	{
		//Starting transaction
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("equities_master");
	    EntityManager em = factory.createEntityManager();
	    
	    em.getTransaction().begin();
	    
	    //Get row count in daily_price
	    TypedQuery<Long> dailyPriceRowCountQuery = em.createQuery("SELECT COUNT(dp.id_) FROM daily_price AS dp", Long.class);
	    long rowCount = dailyPriceRowCountQuery.getSingleResult();
	    	    
	    //Closing transaction
	    em.getTransaction().commit();
	    em.close();
	    
	    return rowCount;
	}
	
	public static long rowsInSymbol()
	{
		//Starting transaction
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("equities_master");
	    EntityManager em = factory.createEntityManager();
	    
	    em.getTransaction().begin();
	    
	    //Get row count in daily_price
	    TypedQuery<Long> dailyPriceRowCountQuery = em.createQuery("SELECT COUNT(sym.id_) FROM symbol AS sym", Long.class);
	    long rowCount = dailyPriceRowCountQuery.getSingleResult();
	    	    
	    //Closing transaction
	    em.getTransaction().commit();
	    em.close();
	    
	    return rowCount;
	    
	}
	
	public static Timestamp mostRecentDataNYSE()
	{
		//Starting transaction
	    EntityManagerFactory factory = Persistence.createEntityManagerFactory("equities_master");
	    EntityManager em = factory.createEntityManager();
			    
	    em.getTransaction().begin();
	        
	    TypedQuery<Timestamp> dailyPriceRowCountQuery = em.createQuery("SELECT MAX(dp.priceDate_) FROM daily_price AS dp WHERE dp.symbol_ IN "+ 
	    															   "(SELECT sym FROM exchange AS ex INNER JOIN symbol AS sym ON " +
	    															   "sym.exchange_=ex WHERE ex.abbrev_='NYSE')", Timestamp.class);
	    Timestamp mostRecentDate = dailyPriceRowCountQuery.getSingleResult();
	    	    
	    //Closing transaction
	    em.getTransaction().commit();
	    em.close();
	    
	    return mostRecentDate;
	}
	
	public static Timestamp mostRecentDataNASDAQ()
	{
		//Starting transaction
	    EntityManagerFactory factory = Persistence.createEntityManagerFactory("equities_master");
	    EntityManager em = factory.createEntityManager();
			    
	    em.getTransaction().begin();
	        
	    TypedQuery<Timestamp> dailyPriceRowCountQuery = em.createQuery("SELECT MAX(dp.priceDate_) FROM daily_price AS dp WHERE dp.symbol_ IN "+ 
				   													   "(SELECT sym FROM exchange AS ex INNER JOIN symbol AS sym ON " +
				   													   "sym.exchange_=ex WHERE ex.abbrev_='NASDAQ')", Timestamp.class);
	    Timestamp mostRecentDate = dailyPriceRowCountQuery.getSingleResult();
	    	    
	    //Closing transaction
	    em.getTransaction().commit();
	    em.close();
	    
	    return mostRecentDate;
	}
}
