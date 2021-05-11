package MainView;

import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainView extends JFrame{
	private JPanel contentPane;
	private JTextField textField; 

	private String id;
	private String ip;
	private int port;

	JButton happyBtn;
	JButton sadBtn;
	JButton angryBtn;
	JButton surpriseBtn;
	JButton sendBtn;
	JTextArea textArea;

	ImageIcon happy=new ImageIcon("/Users/bkcom/Desktop/happy.png");
	ImageIcon sad=new ImageIcon("/Users/bkcom/Desktop/sad.png");
	ImageIcon angry=new ImageIcon("/Users/bkcom/Desktop/angry.png");
	ImageIcon surprise=new ImageIcon("/Users/bkcom/Desktop/surprise.png");



	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	public MainView(String id, String ip, int port)
	{
		this.id = id;
		this.ip = ip;
		this.port = port;

		init();
		start();

		textArea.append("Client Info : " + id + " " + ip + " " + port + "\n");

		network();

	}

	public void network() {

		try {
			socket = new Socket(ip, port);
			if (socket != null)
			{
				Connection();
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			textArea.append("ERROR!!\n");
		}

	}

	public void Connection() {

		try { 
			is = socket.getInputStream();
			dis = new DataInputStream(is);

			os = socket.getOutputStream();
			dos = new DataOutputStream(os);

		} catch (IOException e) {
			textArea.append("ERROR!!\n");
		}

		send_Message(id);

		Thread th = new Thread(new Runnable() {

					@Override
					public void run() {
						while (true) {

							try {
								String msg = dis.readUTF();
								textArea.append(msg + "\n");
							} catch (IOException e) {
								textArea.append("ERROR!!\n");

								try {
									os.close();
									is.close();
									dos.close();
									dis.close();
									socket.close();
									break; //
								} catch (IOException e1) {

								}

							}
						}

					}
				});

		th.start();

	}

	public void send_Message(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) {
			textArea.append("ERROR!!\n");
		}
	}

	public void init() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 288, 422);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 272, 302);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JPanel emojiPanel=new JPanel();
		emojiPanel.setLayout(new GridLayout(1,4));
		emojiPanel.setBounds(0,312,280,30);
		happyBtn=new JButton(ChangeIconSize(happy));
		sadBtn=new JButton(ChangeIconSize(sad));
		angryBtn=new JButton(ChangeIconSize(angry));
		surpriseBtn=new JButton(ChangeIconSize(surprise));
		emojiPanel.add(happyBtn);
		emojiPanel.add(sadBtn);
		emojiPanel.add(angryBtn);
		emojiPanel.add(surpriseBtn);
		contentPane.add(emojiPanel);

		textField = new JTextField();
		textField.setBounds(0, 345, 155, 42);
		contentPane.add(textField);
		textField.setColumns(10);

		sendBtn = new JButton("Send");
		sendBtn.setBounds(161, 345, 111, 42);
		contentPane.add(sendBtn);

		textArea.setEnabled(false);

		setVisible(true);

	}
	
	public void start() {

		sendBtn.addActionListener(new Myaction());
		happyBtn.addActionListener(new SendEmoji());
		sadBtn.addActionListener(new SendEmoji());
		angryBtn.addActionListener(new SendEmoji());
		surpriseBtn.addActionListener(new SendEmoji());

	}

	class SendEmoji implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == happyBtn)
			{
				send_Message("\nªØ(¡Æ¡Ô¡¢¡Æ)ªØ ");
				textField.requestFocus();
			}
			if (e.getSource() == sadBtn)
			{
				send_Message("\n(;_;) ");
				textField.requestFocus();
			}
			if (e.getSource() == angryBtn)
			{
				send_Message("\n(£à¡â¢¥£«£©");
				textField.requestFocus();
			}
			if (e.getSource() == surpriseBtn)
			{
				send_Message("\n(o_O) !");
				textField.requestFocus();
			}

		}

	}
	
	class Myaction implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == sendBtn)
			{

				send_Message(textField.getText());
				textField.setText("");
				textField.requestFocus();
				
			}

		}

	}

	public ImageIcon ChangeIconSize(ImageIcon imageBefore)
	{
		Image before=imageBefore.getImage();
		Image after=before.getScaledInstance(25,25,Image.SCALE_SMOOTH);
		ImageIcon imageAfter=new ImageIcon(after);
		return imageAfter;
	}

}
