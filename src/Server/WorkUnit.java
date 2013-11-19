package Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import Util.YahooAPI;


public class WorkUnit implements Runnable 
{
	private ServerNode parent_;
	private Vector<String> workLoad_;

	public WorkUnit(ServerNode parent, Vector<String> workLoad)
	{
		parent_ = parent;
		workLoad_ = workLoad;
	}
			
	@Override
	public void run() 
	{
		//Results
		Map<String,Map<String,Map<String,Float>>> results = new HashMap<String,Map<String,Map<String,Float>>>();
		String start = "2013-10-01";
		String end = "2013-10-31";
		
		Vector<String> problematicSymbol = new Vector<String>();
		
		for(int i=0; i < workLoad_.size() ; ++i)
		{
			try
			{
				results.put(workLoad_.get(i), YahooAPI.getHistoricalData(workLoad_.get(i), start , end));
				System.out.println("Treated : " + workLoad_.get(i) + " progress : " + Integer.toString(i+1) + "/" + Integer.toString(workLoad_.size()));
			}
			catch(IOException e)
			{
				problematicSymbol.add(workLoad_.get(i));
				System.out.println("Problem with : " + workLoad_.get(i) + " progress : " + Integer.toString(i+1) + "/" + Integer.toString(workLoad_.size()));
			}
		}

		System.out.println(" ");
		System.out.println("Failed symbols : ");
		System.out.println(" ");
		
		for(int i=0; i<problematicSymbol.size(); ++i)
		{
			System.out.println(problematicSymbol.get(i));
		}
		
		parent_.Report(results);
	}		
	
}
