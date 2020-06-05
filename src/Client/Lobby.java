package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class Lobby extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JList user_list;
	private JList room_list;
	private JButton note_btn;
	private JButton joinDialog_btn;
	private JButton makeDialog_btn;
	
	private Vector<String> userTable;
	private Vector<Room> roomTable;
	
	private ProblemClient c;
	
	public Lobby(ProblemClient c) {
		lobbyInitUI();
		userTable = new Vector<>();
		roomTable = new Vector<>();
		curList();
		listenStart();
		Thread listen = new Thread(()->{
			while(true) {
				String msg = c.read();
				lobbyInMessage(msg);
				System.out.println(msg);
			}
		});
		listen.start();
		this.c = c;
	}
	
	//lobbyUI GUI
	private void lobbyInitUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 353, 491);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("User List");
		label.setBounds(12, 10, 101, 15);
		contentPane.add(label);
		
		user_list = new JList();
		user_list.setBounds(12, 25, 315, 177);
		contentPane.add(user_list);
		
		note_btn = new JButton("Note");
		note_btn.setBounds(12, 421, 91, 23);
		contentPane.add(note_btn);
		
		joinDialog_btn = new JButton("Join Dialog");
		joinDialog_btn.setBounds(122, 421, 91, 23);
		contentPane.add(joinDialog_btn);
		
		makeDialog_btn = new JButton("Make Dialog");
		makeDialog_btn.setBounds(232, 421, 91, 23);
		contentPane.add(makeDialog_btn);
		
		room_list = new JList();
		room_list.setBounds(12, 235, 315, 177);
		contentPane.add(room_list);
		
		JLabel label_1 = new JLabel("Dialog list");
		label_1.setBounds(12, 212, 101, 15);
		contentPane.add(label_1);
		
		setVisible(true);
	}
	private void listenStart() {
		note_btn.addActionListener(this);
		joinDialog_btn.addActionListener(this);
		makeDialog_btn.addActionListener(this);
	}
	
	private void curList() {
		Collections.sort(userTable);
		Collections.sort(roomTable);
		user_list.setListData(userTable);
		room_list.setListData(roomTable);
	}
	
	private void intoDialog() {
//		new Dialog();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == note_btn) {
			String user = (String) user_list.getSelectedValue();
			if(user!=null) {
				String msg = JOptionPane.showInputDialog("보낼 쪽지 내용을 적어주세요.");
				if(msg != null) {
					//NOTE%FROM_USER%TO_USER%MESSAGE
					msg = "NOTE|"+ c.getID() +"|"+user+"|"+msg;
					c.send(msg);
				}
			}
			else
				JOptionPane.showMessageDialog(null, "쪽지를 보낼 상대를 선택하세요.","Note Error", JOptionPane.ERROR_MESSAGE);
		}
		else if(e.getSource() == joinDialog_btn) {
			Room room = (Room)room_list.getSelectedValue();
			if(room!= null) {
				System.out.println(room+"에 참여하기");
				//JOINROOM%ROOM_PK%NEW_USER
//				intoDialog();
			}
			else
				JOptionPane.showMessageDialog(null, "참여하고 싶은 대화방을 선택하세요.","Dialog Error", JOptionPane.ERROR_MESSAGE);
			
			//삭제
		}
		else if(e.getSource()==makeDialog_btn){
			String roomName = JOptionPane.showInputDialog("채팅방 이름을 설정하세요.");
			//서버에게 방 생성 알리기 - 내가 추가하지 않고 서버가 추가하기
			if(roomName != null ) {
				if(!roomName.isEmpty()) {
					//MAKEROOM%ROOMNAME
					String msg = "MAKEROOM|"+roomName;
					c.send(msg);
					//intoDialog();
					
				}
				else
					JOptionPane.showMessageDialog(null, "올바른 채팅방이름이 아닙니다.","Dialog Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void lobbyInMessage(String message) {
//		System.out.println(message+"들어옴");
		StringTokenizer st = new StringTokenizer(message,"|");
		String protocol = st.nextToken();
		if(protocol.equals("NEW_USER")) {
			String nUser = st.nextToken();
			userTable.add(nUser);
			curList();
		}
		else if(protocol.equals("ORG_USER")) {
			String oUser = st.nextToken();
			userTable.add(oUser);
			curList();
		}
		else if(protocol.equals("ORG_ROOM")) {
			String roomName = st.nextToken();
			String roomPk = st.nextToken();
			roomTable.add(new Room(roomName, roomPk));
			curList();
		}
		else if(protocol.equals("NOTE")) {
			String fromUser = st.nextToken();
			String content = st.nextToken();
			JOptionPane.showMessageDialog(null, content, fromUser + "로 부터 온 쪽지", JOptionPane.PLAIN_MESSAGE);
		}
		else if(protocol.equals("NEWROOM")) {
			String roomName = st.nextToken();
			String PK = st.nextToken();
			roomTable.add(new Room(roomName, PK));
			curList();
		}
	}
	
	class Room implements Comparable<Room>{
		String roomName;
		String PK;
		public Room(String roomName,String PK) {
			this.roomName = roomName;
			this.PK = PK;
		}
		@Override
		public int compareTo(Room o) {
			return Integer.parseInt(PK) - Integer.parseInt(o.PK);
		}
		@Override
		public String toString() {
			return (Integer.parseInt(PK)+1) +" : " + roomName;
		}
	}
}


