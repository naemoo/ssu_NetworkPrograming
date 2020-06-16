package Client;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;
import Server.Login;

public class ProblemClient {
	private String id;
	
	//RMI Resource
	Login l;
	
	//IO Resource
	private InputStream is;
	private OutputStream os;
	private BufferedReader br;
	private PrintWriter out;
	
	//Network Resource
	private SSLSocket sock;
	private SSLSocketFactory f = null;
	
	public ProblemClient() {
		String mServer = "127.0.0.1";
		String mServName = "1099";
		try {
			l = (Login) Naming.lookup("rmi://"+mServer+"/"+mServName);
		}
		catch (MalformedURLException mue) {
			System.out.println("MalformedURLException: " + mue);
		}
		catch(RemoteException re) {
			System.out.println("RemoteException: " + re);
		}
		catch(NotBoundException nbe) {
			System.out.println("NotBoundException: " + nbe);
		}
	}
	
	void network(String id) {// network 자원 구성 -> 쓰레드를 이용
		try {
			// trustedcerts 파일 경로
			System.setProperty("javax.net.ssl.trustStore", "trustedcerts");
	        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
	        f = (SSLSocketFactory) SSLSocketFactory.getDefault(); // SSLSocketFacroty info
	        sock = (SSLSocket) f.createSocket("127.0.0.1", 8888); // Socket Create
	        
			if (sock != null) {// 성공적으로 연결 되었다면 그 다음 과정 실행
				String[] supported = sock.getSupportedCipherSuites();
		        sock.setEnabledCipherSuites(supported);
		        sock.startHandshake();
				connection(id);
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "서버를 찾지 못 하였습니다.","연결 실패", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "서버와 연결 실패","연결 실패", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void connection(String id) {
		try {
			//IO 얻어오기
			is = sock.getInputStream();
			os = sock.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			out = new PrintWriter(os,true);
			
			//새로운 접속자 ID Server에게 알림
			String msg = "NEW_USER|"+id;
			send(msg);
			
			this.id = id;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void send(String msg) {
		out.println(msg);
	}
	String read() {
		try {
			return br.readLine();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	String getID() {
		return id;
	}
	public static void main(String[] args) {
		new LoginUI(new ProblemClient());
	}
}

