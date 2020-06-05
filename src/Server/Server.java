package Server;

import java.util.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import Server.Server.RoomInfo;
import Server.Server.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;

public class Server {
	//UserInfo List
	static private HashMap<String, Client> userInfoTable;
	
	//IP및 Port
	private int port = 8888;
	
	//User와 대화방 관리 List
	private List<User> user_list;
	private List<RoomInfo> room_list;
	
	//Network Resource
	private ServerSocket serverScoket;
	
	// SSL Network Resource
//	SSLServerSocketFactory sslserversocketfactory;
//	SSLServerSocket sslserversocket;
	
	//Room PK
	private int pk;
	
	
	public Server() {
		userInfoTable = new HashMap<>();
		user_list = new LinkedList<>();
		room_list= new LinkedList<>();
		loadUserInfo();
		//RMI Thread를 써서 열기
		Thread th = new Thread(()->{
			loginListen("127.0.0.1", "1099");
			}		
		);
		th.start();
		startServer();
		startListen();
		pk = 0;
	}
	//Login RMI 시작 
	private static void loginListen(String server,String servname) {
		try {
			Login l = new LoginImpl(userInfoTable);
			Naming.rebind("rmi://"+server+":1099/"+servname,l);
			System.out.println("Login start");
		}
		catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}
	
	// Login 정보를 위한 회원 정보 로딩
	private void loadUserInfo() {
		Client c;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Login.dat"));
			while((c=(Client)ois.readObject())!=null) {
				userInfoTable.put(c.toString(), c);
			}
		}
		catch (IOException e) {
		}
		catch (ClassNotFoundException e) {
		}
	}
	
	public void startServer() {
		try {
//			sslserversocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
//			sslserversocket = (SSLServerSocket)sslserversocketfactory.createServerSocket(port);
			serverScoket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void startListen() {
		Thread th = new Thread(()-> {
			while(true) {
				try {
//					SSLSocket sock = (SSLSocket)sslserversocket.accept();
					Socket sock = serverScoket.accept();
					User u = new User(sock);
					System.out.println(u.ID+"와 연결");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		th.start();
	}
	
	private void sendAll(String msg) {
		for(User u: user_list) {
			u.send(msg);
		}
	}
	
	class User extends Thread implements Comparable<User>{
		//User Imformation
		String ID;
		
		//IO Resource 
		BufferedReader br;
		InputStream is;
		PrintWriter out;
		OutputStream os;
		
		//Network Resource
//		SSLSocket socket;
		Socket socket;
		
		public User(Socket socket) {
			this.socket = socket;
			try {
				is = socket.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				os = socket.getOutputStream();
				out = new PrintWriter(socket.getOutputStream(),true);
				String msg = br.readLine();
				inMessage(msg);
				this.start();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//메세지 들어왔을 시 처리
		private void inMessage(String msg) {
			StringTokenizer st = new StringTokenizer(msg,"|");
			String protocol = st.nextToken();
			if(protocol.equals("NEW_USER")) {
				this.ID = st.nextToken();
				//새로운 유저에게 기존 유저 ID
				for(User u : user_list) {
					String a = "ORG_USER|"+u.ID;
					send("ORG_USER|"+u.ID);
				}
				//새로운 유저에게 기존에 존재하는 방 알림
				for(RoomInfo r:room_list) {
					String a = "ORG_USER|"+"ORG_ROOM|"+r.roomName+"|"+r.pk;
					send("ORG_ROOM|"+r.roomName+"|"+r.pk);
				}
				//기존유저에게 새로운 유저 알림
				sendAll("NEW_USER|"+this.ID);
				//user_list에 추가
				user_list.add(this);
				Collections.sort(user_list);
			}
			if(protocol.equals("NOTE")) {
				String fromUser = st.nextToken();
				String toUser = st.nextToken();
				for(User u:user_list) {
					if(u.ID.equals(toUser)) {
						msg = st.nextToken();
						u.send("NOTE|"+fromUser+"|"+msg);
					}
				}
			}
			if(protocol.equals("MAKEROOM")) {
				String roomName = st.nextToken();
				room_list.add(new RoomInfo(roomName, this));
				String nmsg = "NEWROOM|"+roomName+"|"+pk;
				sendAll(nmsg);
				pk++;
			}
			if(protocol.equals("JOINROOM")) {
				String roomPK = st.nextToken();
				for(RoomInfo r: room_list) {
					if(r.pk == Integer.parseInt(roomPK)) {
						r.addUser(this);
					}
				}
			}
		}
		
		//메세지 보내기
		private void send(String msg) {
			out.println(msg);
			System.out.println(msg+"보내기");
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					String msg = br.readLine();
					inMessage(msg);
				}
				catch(IOException e) {
					//User가 나갔다는 것을 알림
					int idx = Collections.binarySearch(user_list, this);
					user_list.remove(idx);
					sendAll("USER_OUT|"+ID);	
					//방 나가기?
					
					try {
						is.close();
						os.close();
						br.close();
						out.close();
					}
					catch(IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		}
		@Override
		public String toString() {
			return ID;
		}
		
		@Override
		public int compareTo(User u) {
			return ID.compareTo(u.ID);
		}
	}
	
	class RoomInfo implements Comparable<RoomInfo>{
		private String roomName;
		private int pk;
		private List<User> room_user_list = new LinkedList<>();
		
		
		public RoomInfo(String roomName,User u) {
			this.roomName = roomName;
			room_user_list.add(u);
		}
		@Override
		public int compareTo(RoomInfo r) {
			return pk - r.pk;
		}
		
		public void addUser(User u) {
			room_user_list.add(u);
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
