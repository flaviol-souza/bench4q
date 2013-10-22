package src.communication;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import src.Agent;
import src.storage.StorageThread;

public class AgentRMIImpl extends UnicastRemoteObject implements RMIInterface{
	private static String m_args[];
	protected AgentRMIImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	public static void main(String args[]) {

		// Create and install a security manager

		try {
			m_args = args;
			LocateRegistry.createRegistry(1099);
			AgentRMIImpl obj = new AgentRMIImpl();
			// Bind this object instance to the name "HelloServer"
			Naming.rebind("RMIServer", obj);

			System.out.println("AgentServer bound in registry");
		} catch (Exception e) {
			System.out.println("HelloImpl err: " + e.getMessage());
			e.printStackTrace();
		}
	}
	public void getStarted() throws RemoteException {
		try {
			System.out.println("This is response from Agent, RMI OK!");
			Agent.main(m_args);
			System.out.println("This is response from Agent, RMI OK!");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}    
	/**
	 * ¼´Ê±¶ÁÈ¡ÍÌÍÂÁ¿
	 * @return
	 */
	public int getThroughput() throws RemoteException
	{
		if(System.currentTimeMillis()>=StorageThread.getM_endTime()){
			return -1;
		}
		return StorageThread.getUniq().getCurThroughput();
	}
	
	public double getResponsetime() throws RemoteException
	{
		return StorageThread.getUniq().getCurResponseTime();
	}
}
