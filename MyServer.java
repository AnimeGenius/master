import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
public class MyServer{
	public static MyConnection head;
	public static MyConnection tail;
	public static MyConnection rover;

	public static void connAdd(MyConnection node){
		if(head == null){
			head = node;
			tail = node;
		}
		else{
			tail.next = node;
			tail = tail.next;
		}
	}

	public static void omniSend(Message m){
		rover = head;
		while(rover!=null){
			if(rover.status == 1){
				rover.sendMessage(m);
				System.out.println(m.header + " - " + rover.name);
			}
			rover = rover.next;
		}
	}

	public static String getScore(){
		rover = head;
		String blank = "";
		while(rover!=null){
			if(rover.status == 1){
				blank = blank + rover.name + " - " + rover.score + "\n";
			}
			rover = rover.next;
		}
		return blank;
	}

	public static void omniData(Object m){
		rover = head;
		while(rover != null){
			if(rover.status == 1){
				rover.sendObject(m);
			}
			rover = rover.next;
		}
	}

	public static void main(String args[]){
		try{
			ServerSocket ssocket = new ServerSocket(8888);
			System.out.println("Server waiting!");
			int clientcnt = 1; String name = "";
			head = null;
			while(true){
				Socket socket = ssocket.accept();
				System.out.println("A client has joined the rumble!");
				MyConnection bago = new MyConnection(socket, name, clientcnt);
				clientcnt++;
				Object a = bago.getObject();
				String handle = (String) a;
				bago.name = handle;
				connAdd(bago);
				ServerT servert = new ServerT(bago, head);
				servert.start();
				Message m = new Message(10, bago.number, "ENTRANCE");
				omniSend(m);
				Message mc = new Message(6, 0, 0);
				omniSend(mc);
				String scores = getScore();
				omniData(scores);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}