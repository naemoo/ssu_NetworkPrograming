package Server;

import java.io.*;

public class Client implements Serializable{
	String ID;
	String PassWord;
	
	public Client(String ID,String PassWord) {
		this.ID = ID;
		this.PassWord = PassWord;
	}
	
	public boolean isRightPassword(String pw) {
		if(PassWord.equals(pw)) {
			return true;
		}
		return false;
	}
	
	//Login 정보를 저장을 위한 Main
	public static void main(String[] args) {
		Client c1 = new Client("Lee","123");
		Client c2 = new Client("Nam","123");
		Client c3 = new Client("Kim","123");
		Client c4 = new Client("Cho","123");
		
		try(FileOutputStream fos = new FileOutputStream("Login.dat");
				ObjectOutputStream os = new ObjectOutputStream(fos)){
			os.writeObject(c1);
			os.writeObject(c2);
			os.writeObject(c3);
			os.writeObject(c4);
		}
		catch (Exception e) {
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Login.dat"));
			Client c;
			while((c = (Client)ois.readObject())!=null) {
				System.out.println(c);
			}
		}
		catch (Exception e) {
		}
		
	}
	@Override
	public String toString() {
		return this.ID;
	}
	
}
