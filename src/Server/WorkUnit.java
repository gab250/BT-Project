package Server;

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
		
		for(int i=0; i < workLoad_.size() ; ++i)
		{
			results.put(workLoad_.get(i), YahooAPI.getHistoricalData(workLoad_.get(i), start , end));
		}

		parent_.Report(results);
	}		
	
}
