package Client;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Server.GameRoom;
import Server.Problem;

public class ProblemRoom extends JDialog implements ActionListener{
	
	//GUI Resource
	private final JPanel contentPanel = new JPanel();
	private JTextField answerTF;
	private JTextArea probTA;
	private JTextArea answerTA;
	private JList list;
	private JButton sendBTN;
	private JButton startBTN;
	private JButton level1BTN;
	private JButton level2BTN;
	private JButton level3BTN;
	private JButton level4BTN;
	private JButton level5BTN;
	private JScrollPane listSP;
	private JScrollPane probSP;
	private JScrollPane answerSP;
	
	// RMI Resource
	GameRoom gr;
	
	// Lobby
	Lobby l;
	
	// UserList
	Vector<String> user_list = new Vector<>();
	List<Problem> prob_list = new LinkedList<>();
	
	// Problem Resource
	String answer = null;
	String problem = null;
	

	public ProblemRoom(GameRoom gr,Lobby l) {
		connection(gr);
		makeFrame();
		listen();
		loadProblem();
		this.l = l;
	}
	
	private void connection(GameRoom gr) {
		this.gr = gr;
	}

	private void makeFrame() {
		setBounds(100, 100, 700, 505);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		probTA = new JTextArea();
		probSP = new JScrollPane(probTA);
		probSP.setBounds(166, 37, 508, 195);
		contentPanel.add(probSP);

		JLabel lblProblem = new JLabel("문제");
		lblProblem.setBounds(166, 12, 77, 15);
		contentPanel.add(lblProblem);

		JLabel lblAnswer = new JLabel("답");
		lblAnswer.setBounds(176, 242, 50, 15);
		contentPanel.add(lblAnswer);

		answerTA = new JTextArea();
		answerSP = new JScrollPane(answerTA);
		answerSP.setBounds(166, 267, 508, 136);
		contentPanel.add(answerSP);

		answerTF = new JTextField();
		answerTF.setBounds(166, 416, 406, 21);
		contentPanel.add(answerTF);
		answerTF.setColumns(10);

		sendBTN = new JButton("입력");
		sendBTN.setBounds(584, 413, 91, 23);
		contentPanel.add(sendBTN);

		startBTN = new JButton("시작");
		startBTN.setBounds(34, 8, 91, 23);
		contentPanel.add(startBTN);

		list = new JList();
		listSP = new JScrollPane(list);
		listSP.setBounds(34, 56, 91, 161);
		contentPanel.add(listSP);

		level1BTN = new JButton("★☆☆☆☆");
		level1BTN.setBounds(34, 256, 91, 23);
		contentPanel.add(level1BTN);

		level2BTN = new JButton("★★☆☆☆");
		level2BTN.setBounds(34, 287, 91, 23);
		contentPanel.add(level2BTN);

		level3BTN = new JButton("★★★☆☆");
		level3BTN.setBounds(34, 315, 91, 23);
		contentPanel.add(level3BTN);

		level4BTN = new JButton("★★★★☆");
		level4BTN.setBounds(34, 347, 91, 23);
		contentPanel.add(level4BTN);

		level5BTN = new JButton("★★★★★");
		level5BTN.setBounds(34, 380, 91, 23);
		contentPanel.add(level5BTN);

		JLabel lblJoinUser = new JLabel("참여중인 User");
		lblJoinUser.setBounds(34, 37, 100, 15);
		contentPanel.add(lblJoinUser);

		JLabel lblProblemLevel = new JLabel("문제 난이도");
		lblProblemLevel.setBounds(34, 227, 91, 15);
		contentPanel.add(lblProblemLevel);

		answerTA.setEditable(false);
		probTA.setEditable(false);
		level1BTN.setEnabled(false);
		level2BTN.setEnabled(false);
		level3BTN.setEnabled(false);
		level4BTN.setEnabled(false);
		level5BTN.setEnabled(false);

		answerTF.setEnabled(false);
		sendBTN.setEnabled(false);

		//종료 시 처리
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		this.setVisible(true);
	}

	private void loadProblem() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Problem.dat"));
			Problem p;
			while((p = (Problem)ois.readObject())!=null) {
				prob_list.add(p);
			}
		}
		catch(EOFException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void close() {
		// 유저 나갔을 떄 처리
		try {
			gr.userOut(l.c.getID());
			gr.invalidateUser();
			this.dispose();
			l.setVisible(true);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void listen() {
		level1BTN.addActionListener(this);
		level2BTN.addActionListener(this);
		level3BTN.addActionListener(this);
		level4BTN.addActionListener(this);
		level5BTN.addActionListener(this);
		startBTN.addActionListener(this);
		sendBTN.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == level1BTN) {
				gr.getProblem(0);
			} else if (e.getSource() == level2BTN) {
				gr.getProblem(1);
			} else if (e.getSource() == level3BTN) {
				gr.getProblem(2);
			} else if (e.getSource() == level4BTN) {
				gr.getProblem(3);
			} else if (e.getSource() == level5BTN) {
				gr.getProblem(4);
			} else if (e.getSource() == startBTN) {
				gr.start();
			} else if (e.getSource() == sendBTN && !answerTF.getText().trim().equals("")) {
				String chat = answerTF.getText();
				boolean isAns = false;
				if(chat.equals(answer)) {
					isAns = true;
				}	
				gr.chat(chat,l.c.getID(),isAns);
				answerTF.setText("");
			}
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	private void gameStart() {
		level1BTN.setEnabled(true);
		level2BTN.setEnabled(true);
		level3BTN.setEnabled(true);
		level4BTN.setEnabled(true);
		level5BTN.setEnabled(true);

		answerTF.setEnabled(true);
		sendBTN.setEnabled(true);
		startBTN.setEnabled(false);
	}
	
	public void inMessage(String message) {
		StringTokenizer st = new StringTokenizer(message,"|");
		String protocol = st.nextToken();
		protocol = st.nextToken();
		
		if(protocol.equals("USEROUT")) {
			String userName = st.nextToken();
			for(int i = 0;i<user_list.size();i++) {
				if(user_list.get(i).equals(userName))
					user_list.remove(i);
			}
			curList();
		}
		else if(protocol.equals("START")) {
			gameStart();
		}
		else if(protocol.equals("PROBLEM")) {
			int probIdx = Integer.parseInt(st.nextToken());
			probTA.setText("");
			problem = prob_list.get(probIdx).problem;
			answer = prob_list.get(probIdx).answer;
			if(problem!=null)
				probTA.append(problem);
		}
		else if(protocol.equals("INVALIDATE_USER")) {
			String user = st.nextToken();
			if(!user_list.contains(user)) {
				user_list.add(user);
				curList();
			}
		}
		else if(protocol.equals("CHAT")) {
			String user = st.nextToken();
			String chat = st.nextToken();
			boolean isAns = Boolean.parseBoolean(st.nextToken());
			
			answerTA.append(user+" : " +chat+"\n");
			if(isAns) {
				probTA.append("\n\n*************************************************\n"
						+ user+"님이 정답을 맞추셨습니다!!!!!!!!!");
			}
		}
		
	}
	
	private void curList() {
		list.setListData(user_list);
	}
}