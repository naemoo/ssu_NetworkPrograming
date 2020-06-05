package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class LoginImpl extends UnicastRemoteObject implements Login{
	private HashMap<String, Client> userInfoTable; 
	public LoginImpl(HashMap<String, Client> userInfoTable) throws RemoteException{
		super();
		this.userInfoTable = userInfoTable;
	}
	@Override
	public boolean login(String ID, String PW) throws RemoteException {
		try {
			Client c = userInfoTable.get(ID);
			if(c.isRightPassword(PW)) {
				System.out.println("로그인 성공");
				return true;
			}
		}
		catch (Exception e) {
		}
		System.out.println("실패");
		return false;
	}
	@Override
	public void hi() throws RemoteException {
		System.out.println("hi");
	}
}
