package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.io.*;


public class LoginUI extends JFrame implements ActionListener{
	//UI Resource
	private JPanel contentPane;
	private JTextField id_tf;
	private JPasswordField pw_tf;
	private JButton login_btn;

	//Login Resource 
	private String id;
	private String pw;
	private boolean loginFlag = false;
	
	ProblemClient c;
	
	public LoginUI(ProblemClient c) {
		loginInitUI();
		listenStart();
		this.c = c;
	}
	
	private void listenStart() {
		login_btn.addActionListener(this);
	}
	
	
	private void loginInitUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 345, 512);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ID : ");
		lblNewLabel.setBounds(48, 241, 50, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("PW : ");
		lblNewLabel_1.setBounds(48, 296, 50, 15);
		contentPane.add(lblNewLabel_1);
		
		id_tf = new JTextField(10);
		id_tf.setBounds(137, 238, 96, 21);
		contentPane.add(id_tf);
		id_tf.setColumns(10);
		
		pw_tf = new JPasswordField();
		pw_tf.setColumns(10);
		pw_tf.setBounds(137, 293, 96, 21);
		contentPane.add(pw_tf);
		
		login_btn = new JButton("LogIn");
		login_btn.setBounds(111, 395, 91, 23);
		contentPane.add(login_btn);
		
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==login_btn) {
			id = id_tf.getText().trim();
			pw = pw_tf.getText().trim();
			
			// 로그인 시도
			try {
				loginFlag = c.l.login(id, pw);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//로그인 성공 시
			if(loginFlag==true) {
				Thread th = new Thread(()->{
					c.network(id);
					new Lobby(c);
				});
				th.start();
				this.dispose();
			}//로그인 실패 시
			else {
				JOptionPane.showMessageDialog(null, "잘못된 회원 정보입니다.","로그인 실패", JOptionPane.ERROR_MESSAGE);
				loginFlag = false;
			}
		}
	}
}
