package Server;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import Dispatcher.DispatcherInterface;

public class ServerNode implements ServerNodeInterface {

	private DispatcherInterface dispatcher_;
	private String name_;
	private int id_, dispatcherRMIPort_, localRMIPort_, port_;
	
	public static void main(String[] args)
	{
		ServerNode serverNode = new ServerNode(args[0],args[1],Integer.valueOf(args[2]),Integer.valueOf(args[3]), Integer.valueOf(args[4]));
		
		try 
		{
			serverNode.run();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public ServerNode(String hostName, String name, int localRMIPort, int port, int dispatcherRMIPort)
	{
		name_ = name;
		dispatcher_ = loadDispatcherStub(hostName);
		localRMIPort_ = localRMIPort;
		dispatcherRMIPort_ = dispatcherRMIPort;
		port_= port;
    }
	
	public void Report(Map<String,Map<String,Float>> result)
	{
		try 
		{
			dispatcher_.Report(id_, result);
		} 
		catch (RemoteException e) 
		{
			System.err.println("Error : " + e.getMessage());
		}
	}
	
	private void run() throws Exception
	{
		if(System.getSecurityManager() ==  null)
		{
			System.setSecurityManager(new SecurityManager());
		}
		
		//Register current Node to RMI registry
		try
		{
			ServerNodeInterface stub = (ServerNodeInterface) UnicastRemoteObject.exportObject(this, port_);
			Registry registry = LocateRegistry.getRegistry(localRMIPort_);
			registry.rebind(name_, stub);
		}
		catch(ConnectException e)
		{
			System.err.println("Impossible to connect to registry from ServerNovde");
			System.err.println("Erreur : " + e.getMessage());
		}
		catch(Exception e)
		{
			System.err.println("Erreur: " + e.getMessage());
		}
		
		//Register current Node to dispatcher
		id_ = dispatcher_.Register(InetAddress.getLocalHost().getHostName(), name_, localRMIPort_);
		
		//Register failed
		if(id_ <= 0)
		{
			System.err.println("Couldn't register, closing node...");
			System.exit(-1);
		}
		
	}
		
	@Override
	public void Process(Vector<String> workLoad) throws RemoteException 
	{
		WorkUnit workUnit = new WorkUnit(this,workLoad);
		Thread workingThread = new Thread(workUnit);
		workingThread.start();
    }

	@Override
	public boolean IsAlive() throws RemoteException 
	{
		return true;
	}
	
	private DispatcherInterface loadDispatcherStub(String hostname)
	{
		DispatcherInterface stub = null;
		
		try 
		{
			Registry registry = LocateRegistry.getRegistry(hostname,dispatcherRMIPort_);
			stub = (DispatcherInterface) registry.lookup("dispatcher");
			
		} 
		catch (RemoteException e) 
		{
			
		} 
		catch (NotBoundException e) 
		{
			
		}
		
		return stub;
	}
	
}
