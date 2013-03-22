import java.io.*;
import java.net.*;
import javax.swing.*;
public class MyConnection{
	public Socket socket;
	public OutputStream a; public ObjectOutputStream b;
	public InputStream d; public ObjectInputStream h;
	public MyConnection next; 
	public String name;
	public int number;
	public int status;
	public int score;
	public int[] randlist = new int [25];
	public int gamestate;
	public int currgirl;
	public MyConnection(Socket s, String x, int numberx){
		try{
			name = x;
			status = 1; //1 meaning the user is active. else the user is offline
			score = 0;
			socket = s;
			number = numberx;
			gamestate = 0;
			a = socket.getOutputStream();
			b = new ObjectOutputStream(a);
			d = socket.getInputStream();
			h = new ObjectInputStream(d);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void updateScore(){
		this.score +=1;
	}
	public void sendMessage(Message msg){
		try{
			b.writeObject(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void sendObject(Object obj){
		try{
			b.writeObject(obj);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Object getObject(){
		Object obj = new Object();
		try{
			obj = h.readObject();
		}catch(Exception e){
			System.out.println("Notification: A client has left");
		}
		return obj;
	}
	public Message getMessage(){
		Message msg = new Message("");
		try{
			Object temp = h.readObject();
			Message xxxx = (Message) temp;
			msg = xxxx;
		}catch(Exception e){
			System.out.println("Notification: A client has left");
		}
		return msg;
	}
}