package Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;


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
     	System.out.println(workLoad_.toString());

		parent_.Report(new HashMap<String,Map<String,Float>>());
	}		
	
}
