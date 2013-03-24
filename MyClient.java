import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.border.*;
public class MyClient extends JFrame{
	public static Socket socket;
	public static InputStream ins;
	public static ObjectInputStream oins;
	public static OutputStream a;
	public static ObjectOutputStream b;
	public JButton ehandle, sendAns, quit;
	public static int gameState;
	public JLabel imgHolder, scoreL;
	public JTextArea scoreTA, ansTA;
	public JScrollPane scoreSP;
	public static String header = "Anime Genius: ";
	public MyClient(){
		super(header);
		gameState = 0;
		Container c = getContentPane();
		c.setLayout(null);
		Border loweredbevel, raisedbevel;

		loweredbevel = BorderFactory.createLoweredBevelBorder();
		raisedbevel = BorderFactory.createRaisedBevelBorder();

		imgHolder = new JLabel(new ImageIcon("images\\Kashiwazaki Sena.jpg"));
		imgHolder.setSize(250, 250);
		imgHolder.setLocation(15, 15);
		imgHolder.setBorder(raisedbevel);
		c.add(imgHolder);

		ehandle = new JButton("Start Game");
		ehandle.setSize(150, 50);
		ehandle.setLocation(590, 15);
		ehandle.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Message msg = new Message("");
					if(gameState == 0){
						msg.header = "START GAME";
						gameState = 1;
						System.out.println("START KEY PRESS");
					}
					else if(gameState == 1){
						msg.header = "PAUSE GAME";
						gameState = 2;
						System.out.println("PAUSE KEY PRESS");
					}
					else if(gameState == 2){
						msg.header = "RESUME GAME";
						gameState = 1;
						System.out.println("RESUME KEY PRESS");
					}
					try{
						sendObject(msg);
					}catch(Exception E){
						System.out.println("Error has occured!");
					}
			}
			}
			);
		c.add(ehandle);

		quit = new JButton("EXIT");
		quit.setSize(150, 50);
		quit.setLocation(590, 80);
		quit.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Message msg = new Message(9, 0);
					System.out.println("QUIT KEY PRESS");
					try{
						sendObject(msg);
					}catch(Exception E){
						System.out.println("Error has occured!");
					}
					System.exit(0);
			}
			}
			);
		c.add(quit);

		scoreL = new JLabel("Score Board");
		scoreL.setSize(300, 20);
		scoreL.setLocation(275, 15);
		c.add(scoreL);

		scoreTA = new JTextArea();
		scoreTA.setEditable(false);
		scoreSP = new JScrollPane(scoreTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scoreSP.setSize(300, 230);
		scoreSP.setLocation(275, 35);
		scoreSP.setBorder(loweredbevel);
		c.add(scoreSP);

		ansTA = new JTextArea();
		ansTA.setSize(550, 45);
		ansTA.setLocation(15, 275);
		ansTA.setBorder(loweredbevel);
		c.add(ansTA);

		sendAns = new JButton("Send Answer");
		sendAns.setSize(150, 45);
		sendAns.setLocation(590, 275);
		sendAns.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String ans = ansTA.getText();
					ansTA.setText("");
					System.out.println("SEND KEY PRESS" + " : " + ans);
					Message msg = new Message(8, 0, ans);
					try{
						sendObject(msg);
					}catch(Exception E){
						System.out.println("Error has occured!");
					}
			}
			}
			);
		c.add(sendAns);


		setSize(760, 365);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void sendObject(Object a) throws Exception{
		b.writeObject(a);
	}

	public static Object reads() throws Exception{
		Object tempobj = oins.readObject();
		return tempobj;
	}

	public static void main(String args[]){
		String ip = JOptionPane.showInputDialog("Enter IP:");
		int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port number"));
		try{
			socket = new Socket(ip, port);
			a = socket.getOutputStream();
			b = new ObjectOutputStream(a);
			ins = socket.getInputStream();
			oins = new ObjectInputStream(ins);
			String a = JOptionPane.showInputDialog("Enter your name:");
			header = header + a;
			try{
				sendObject(a);
				/*
				Message m = new Message(9, 0);
				sendObject(m);
				*/
			}catch(Exception e){
				e.printStackTrace();
			}
			MyClient x = new MyClient();
			int gate = 0;
			while(gate == 0){
				try{
					Object thold = reads();
					Message msg = (Message) thold;
					System.out.println(msg.header);
					if(msg.header.equals("FALSE START")){
						Message reply = new Message("FALSE START");
						sendObject(reply);
						gameState = 1;
						x.ehandle.setText("PAUSE GAME");
					}
					else if(msg.header.equals("FALSE RESUME")){
						Message reply = new Message("FALSE RESUME");
						sendObject(reply);
						x.ehandle.setText("PAUSE GAME");
						gameState = 1;
					}
					else if(msg.header.equals("FALSE PAUSE")){
						Message reply = new Message("FALSE PAUSE");
						sendObject(reply);
						gameState = 2;
						x.ehandle.setText("RESUME GAME");
					}
					else if(msg.header.equals("APPEND SCORE")){
						Object xxx = reads();
						String scores = (String) xxx;
						x.scoreTA.setText("" + scores);
					}
					else if(msg.header.equals("APPEND GIRL")){
						Girl g = msg.girl;
						x.imgHolder.setIcon(new ImageIcon(g.img));
					}
					else if(msg.header.equals("FALSE END")){
						gameState = 0;
						x.ehandle.setText("START GAME");
					}
					else if(msg.header.equals("END GAME")){
						Message m = new Message(5, 1);
						gameState = 0;
						x.ehandle.setText("START GAME");
						sendObject(m);
					}
				}catch(Exception e){
					//e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}