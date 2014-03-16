package Server;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import Dispatcher.DispatcherInterface;

public class ServerNode implements ServerNodeInterface {

	private DispatcherInterface dispatcher_;
	private String name_, dispatcherHostName_,ipAddress_;
	private int id_, dispatcherRMIPort_, localRMIPort_, port_;
	
	public static void main(String[] args)
	{
		String publicIpAdress=null;
		
		//Getting public IPv4 address
		try
		{
		     URL myIP = new URL("http://icanhazip.com/");
		     BufferedReader in = new BufferedReader(
		                          new InputStreamReader(myIP.openStream()));
		     publicIpAdress = in.readLine();
					
	    }
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		
		System.out.println("Starting node with IP address " + publicIpAdress);
		System.setProperty("java.rmi.server.hostname", publicIpAdress);
		
		ServerNode serverNode = new ServerNode(args[0],args[1],Integer.valueOf(args[2].trim()),Integer.valueOf(args[3].trim()), Integer.valueOf(args[4].trim()), publicIpAdress);

		try 
		{
			serverNode.run();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public ServerNode(String hostName, String name, int localRMIPort, int port, int dispatcherRMIPort, String ipAddress)
	{
		name_ = name;
		dispatcherHostName_ = hostName;
		localRMIPort_ = localRMIPort;
		dispatcherRMIPort_ = dispatcherRMIPort;
		port_= port;
		ipAddress_=ipAddress;
    }
	
	public void Report(Map<String,Map<String,Map<String,Float>>> result)
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
			System.err.println("Unknown Error In Regsitring node: " + e.getMessage());
		}
		
		//Get dispatcher stub
		dispatcher_ = loadDispatcherStub(dispatcherHostName_);
		
		//Register current Node to dispatcher
		id_ = dispatcher_.Register(ipAddress_, name_, localRMIPort_);
		
		//Register failed
		if(id_ <= 0)
		{
			System.err.println("Couldn't register, closing node...");
			System.exit(-1);
		}
		
		System.out.println("Node " + Integer.toString(id_)  +" up");
		
	}
		
	@Override
	public void Process(Vector<String> workLoad, String startTime, String endTime) throws RemoteException 
	{
		WorkUnit workUnit = new WorkUnit(this,workLoad,startTime,endTime);
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
			System.err.println("Erreur in loadDispatcherStub : " + e.getMessage());
		} 
		catch (NotBoundException e) 
		{
			System.err.println("Erreur in loadDispatcherStub : " + e.getMessage());
		}
		
		return stub;
	}
	
}
