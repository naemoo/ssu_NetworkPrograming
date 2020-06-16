package Server;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Server.Server.User;
import java.util.*;

public class GameRoomImpl extends UnicastRemoteObject implements GameRoom {
	Server.RoomInfo r;
	
	// flag
	boolean start = false;
	
	public GameRoomImpl(Server.RoomInfo r) throws RemoteException{
		super();
		this.r = r;
	}
	@Override
	public void getProblem(int i) throws RemoteException {
		r.roomSendAll("GAME|PROBLEM|"+i);
	}
	@Override
	public void invalidateUser() throws RemoteException {
		for(User u:r.room_user_list) {
			r.roomSendAll("GAME|INVALIDATE_USER|"+u.ID);
		}
	}
	@Override
	public void userOut(String userName) throws RemoteException {
		r.delUser(userName);
		r.roomSendAll("GAME|USEROUT|"+userName);
	}
	@Override
	public void start() throws RemoteException {
		r.roomSendAll("GAME|START");
	}
	@Override
	public void chat(String chat, String ID, boolean isAns) throws RemoteException {
		r.roomSendAll("GAME|CHAT|"+ID+"|"+chat+"|"+isAns);
	}
}
