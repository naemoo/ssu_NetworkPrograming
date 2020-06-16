package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameRoom extends Remote{
	public void getProblem(int i) throws RemoteException;
	public void invalidateUser() throws RemoteException;
	public void userOut(String userName) throws RemoteException;
	public void start() throws RemoteException;
	public void chat(String chat,String ID,boolean isAns) throws RemoteException;
}
