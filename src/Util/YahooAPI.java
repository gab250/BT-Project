package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class YahooAPI 
{
	public static Map<String,Map<String,Float>> getHistoricalData(String in_strSymbol, String in_strStartDate, String in_strEndDate) throws IOException
	{
		//Time parameters
		String strTimeParam =
				"s=" + in_strSymbol.substring(1, in_strSymbol.length()-1) +
				"&a=" + Integer.toString(Integer.valueOf(in_strStartDate.substring(5,7)) -1) +
				"&b=" + Integer.valueOf(in_strStartDate.substring(8,10)) +
				"&c=" + Integer.valueOf(in_strStartDate.substring(0,4)) + 
				"&d=" + Integer.toString(Integer.valueOf(in_strEndDate.substring(5,7)) -1) + 
				"&e=" + Integer.valueOf(in_strEndDate.substring(8,10)) +
				"&f=" +  Integer.valueOf(in_strEndDate.substring(0, 4)) + 
				"&g=d&ignore=.csv";
		
		String strUrl = "http://ichart.yahoo.com/table.csv?";
		strUrl+=strTimeParam;
		
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    Vector<String> csv = new Vector<String>();
	    String line;
	  
        try 
        {
        	
			url = new URL(strUrl);
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			
		    while ((line = br.readLine()) != null) 
		    {
	            csv.add(line);
	        }
        } 
        catch (MalformedURLException e) 
        {
			e.printStackTrace();
		} 
        catch (IOException e) 
        {
        	throw e;
       	}
        
        String[] keys = csv.get(0).split(",");
        
        Map<String,Map<String,Float>> historicalData =  new HashMap<String,Map<String,Float>>();
        
        for(int i=1; i<csv.size(); ++i)
        {
        	String[] dayData = csv.get(i).split(",");
        	String date = dayData[0];
        	
        	Map<String,Float> dayDataMap =  new HashMap<String,Float>();
        	
        	dayDataMap.put(keys[1], Float.valueOf(dayData[1]));	
        	dayDataMap.put(keys[2], Float.valueOf(dayData[2]));
        	dayDataMap.put(keys[3], Float.valueOf(dayData[3]));
        	dayDataMap.put(keys[4], Float.valueOf(dayData[4]));
        	dayDataMap.put(keys[5], Float.valueOf(dayData[5]));
        	dayDataMap.put(keys[6], Float.valueOf(dayData[6]));
        	
        	historicalData.put(date, dayDataMap);
        	
        }
        
        return historicalData;
       
	}

	public static Vector<String> getNasdaqSymbols()
	{
		String strUrl = "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nasdaq&render=download";
		
		Vector<String> symbols = new Vector<String>();
		String line;
		
		URL url;
	    InputStream is = null;
	    BufferedReader br;
		
		try 
        {
            url = new URL(strUrl);
			is = url.openStream();  // throws an IOException
		    br = new BufferedReader(new InputStreamReader(is));
		   
		    while ((line = br.readLine()) != null) 
		    {
		    	symbols.add(line.split(",")[0]);
	        }
	  
        } 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		symbols.remove(0);
		
		return symbols;
	}

	public static Vector<String> getNYSESymbols()
	{
		String strUrl = "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nyse&render=download";
		
		Vector<String> symbols = new Vector<String>();
		String line;
		
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String currentSymbol;
		
		try 
        {
            url = new URL(strUrl);
			is = url.openStream();  // throws an IOException
		    br = new BufferedReader(new InputStreamReader(is));
		   	    
		    while ((line = br.readLine()) != null) 
		    {
		    	currentSymbol = line.split(",")[0];
		    	
		    	
		    	if(currentSymbol.contains(" "))
		    	{
		    		currentSymbol = (currentSymbol.split(" ")[0]).concat("\"");
		    	}
		    	
		    	if(currentSymbol.contains("^"))
		    	{
		    		currentSymbol = currentSymbol.replace('^', ' ');
		    	    currentSymbol = (currentSymbol.split(" ")[0]).concat("\"");
		    	}
		    	else if(currentSymbol.contains("/"))
		    	{
		    		currentSymbol = currentSymbol.replace('/', ' ');
		    	    currentSymbol = (currentSymbol.split(" ")[0]).concat("\"");
		    	}
		    	else if(currentSymbol.contains("$"))
		    	{
		    		currentSymbol = currentSymbol.replace('$', ' ');
		    	    currentSymbol = (currentSymbol.split(" ")[0]).concat("\"");
		    	}
		    	
		    	if(!symbols.contains(currentSymbol) && !currentSymbol.equals("\"Symbol\""))
		    	{
		    		symbols.add(currentSymbol);
		    	}
		    	
	        }
	  
        } 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//Test for ^,$,/
		for(int i=0; i<symbols.size() ; ++i)
		{
			if(symbols.get(i).contains("^"))
			{
				System.err.println("Symbol Containing ^ : " + symbols.get(i));
			}
			
			if(symbols.get(i).contains("/"))
			{
				System.err.println("Symbol Containing / : " + symbols.get(i));
			}
			
			if(symbols.get(i).contains("$"))
			{
				System.err.println("Symbol Containing $ : " + symbols.get(i));
			}
			
			if(symbols.get(i).contains(" "))
			{
				System.err.println("Symbol Containing ' ' : " + symbols.get(i));
			}
		}
		
		return symbols;
	}
	
}
