package src.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote{
	public void getStarted() throws RemoteException;
	public int getThroughput() throws RemoteException;
	public double getResponsetime() throws RemoteException;
}
