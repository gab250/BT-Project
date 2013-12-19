package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Vector;

public class Test 
{
	public static void main(String[] args) throws IOException 
	{
		String start = "2013-12-17";
		String end = "2013-12-17";
		String symbol = "\"GOOG\"";
		
		Map<String,Map<String,Float>> result;
		result = YahooAPI.getHistoricalData(symbol, start, end);
		
		System.out.println("Done");
	
	}
}
