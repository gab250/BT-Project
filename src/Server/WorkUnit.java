package Server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import Util.YahooAPI;


public class WorkUnit implements Runnable 
{
	private ServerNode parent_;
	private Vector<String> workLoad_;
	private String startTime_,endTime_;

	public WorkUnit(ServerNode parent, Vector<String> workLoad, String startTime, String endTime)
	{
		parent_ = parent;
		workLoad_ = workLoad;
		startTime_ = startTime;
		endTime_ = endTime;
	}
			
	@Override
	public void run() 
	{
		//Get time
    	SimpleDateFormat currentTime = new SimpleDateFormat("y:M:dd:HH:mm:ss");
		System.out.println("[ Starting work unit @ " + currentTime.format(Calendar.getInstance().getTime()) + " ]");
		
		//Results
		Map<String,Map<String,Map<String,Float>>> results = new HashMap<String,Map<String,Map<String,Float>>>();
		
		Vector<String> problematicSymbol = new Vector<String>();
		
		for(int i=0; i < workLoad_.size() ; ++i)
		{
			try
			{
				results.put(workLoad_.get(i), YahooAPI.getHistoricalData(workLoad_.get(i), startTime_ , endTime_));
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
		
		System.out.println("[ Leaving work unit @ " + currentTime.format(Calendar.getInstance().getTime()) + " ]");
		
		parent_.Report(results);
	}		
	
}
