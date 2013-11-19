package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class Test 
{
	public static void main(String[] args) 
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
		
		System.out.println("Done");
		
	}
}
