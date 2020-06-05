package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Login extends Remote{
	public boolean login(String ID,String PW) throws RemoteException;
	public void hi() throws RemoteException;
}
