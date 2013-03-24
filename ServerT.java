import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
public class ServerT extends Thread{
	public MyConnection head;
	public MyConnection conn;
	public MyConnection rover;
	public Girl ghead;
	public Girl grover;
	public int gameState;
	public int flag;
	public int[] randlist = new int [25];

	public ServerT(MyConnection c, MyConnection headx){
		head = headx;
		conn = c;
		gameState = 0;
		flag = 0;
		try{
			loadFromFile();
		}catch(Exception e){
			System.out.println("Error from loading the file.");
		}
	}

	public void loadFromFile() throws Exception{
		String filename = "Girl.bin";
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try{
			Object temp = ois.readObject();
			if(temp.getClass().getName().equals("Girl")){
				ghead = (Girl) temp;
			}
		}catch(EOFException e){
			System.out.println("File loaded");
		}
	}

	public String getScore(){
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

	public void omniSend(Message m){
		rover = head;
		while(rover != null){
			if(rover.status == 1){
				rover.sendMessage(m);
				if(m.header.equals("APPEND GIRL")){
					Girl xxx = getGirl(randlist[conn.currgirl]);
					System.out.print(xxx.name + " ");
				}
				System.out.println(m.header + " : " +  conn.name + " - " + rover.name);
			}
			rover = rover.next;
		}
	}

	public void omniData(Object m){
		rover = head;
		while(rover != null){
			if(rover.status == 1){
				rover.sendObject(m);
			}
			rover = rover.next;
		}
	}

	public void omniRandSet(){
		rover = head;
		while(rover != null){	
			int a = 0;
			while(a < 25){
				rover.randlist[a] = randlist[a];
				a++;
			}
			rover = rover.next;
		}
	}

	public void omniGirlSet(int a){
		rover = head;
		while(rover!=null){
			if(rover.status == 1){
				rover.currgirl = a;
			}
			rover = rover.next;
		}
	}

	public Girl getGirl(int a){
		grover = ghead;
		int cnt = 0;
		while(cnt < a){
			grover = grover.next;
			cnt++;
		}
		return grover;
	}

	public void omniScore(int a){
		rover = head;
		while(rover != null){
			rover.score = 0;
			rover = rover.next;
		}
	}

	public boolean isExist(int number, int counter){
		int x = 0;
		while(x < counter){
			if(number == randlist[x]){
				return false;
			}
			x++;
		}
		return true;
	}

	public void randAlgo(){
		int cnt = 0;
		Random rand = new Random();
		while(cnt < 25){
			int a = rand.nextInt(100);
			if(cnt == 0){
				randlist[cnt] = a;
				cnt++;
			}
			else{
				if(isExist(a, cnt)){
					randlist[cnt] = a;
					cnt++;
				}
			}
		}
	}

	public void printRand(){
		int a = 0;
		while(a < 25){
			System.out.println(randlist[a]);
			a++;
		}
	}

	public void run(){
		try{
			int gate = 0;
			while(gate == 0){
				Message msg = conn.getMessage();
				System.out.println(conn.name + " :: " + msg.header);
				if(msg.header.equals("START GAME")){
					if(gameState == 0){
						randAlgo();
						//printRand();
						flag = 1;
						omniRandSet();
						omniGirlSet(0);
						omniScore(0);
						Message m = new Message("FALSE START");
						omniSend(m);
						gameState = 1;
					}
				}
				else if(msg.header.equals("END GAME")){
					gameState = 0;
				}
				else if(msg.header.equals("EXIT")){
					conn.status = 2;
					Message mc = new Message(6, 0, 0);
					omniSend(mc);
					String scores = getScore();
					omniData(scores);
					gate = 1;
				}
				else if(msg.header.equals("PAUSE GAME")){
					if(gameState == 1){
						Message m = new Message("FALSE PAUSE");
						omniSend(m);
					}
				}
				else if(msg.header.equals("RESUME GAME")){
					if(gameState == 2){
						Message m = new Message("FALSE RESUME");
						omniSend(m);
					}
				}
				else if(msg.header.equals("ANSWER")){
					if(gameState == 1){
						int holder = conn.currgirl;
						Girl g = getGirl(randlist[holder]);
						if(msg.aux2.equals(g.name) || msg.aux2.equals(g.alt)){
							conn.updateScore();
							if((holder + 1) < 25){
								omniGirlSet(holder + 1);
								g = getGirl(randlist[holder + 1]);
								Message md = new Message(7, g);
								omniSend(md);
								Message mc = new Message(6, 0, 0);
								omniSend(mc);
								String scores = getScore();
								omniData(scores);
							}
							else{
								Message mc = new Message(6, 0, 0);
								omniSend(mc);
								String scores = getScore();
								omniData(scores);
								Message m = new Message(5, 1);
								omniSend(m);
							}
						}
					}
				}
				else if(msg.header.equals("FALSE START")){
					if(gameState == 0){
						if(flag == 0){
							int a = 0;
							while(a < 25){
								randlist[a] = conn.randlist[a];
								a++;
							}
							gameState = 1;
							flag = 1;
						}
					}
					int holder = conn.currgirl;
					Girl g = getGirl(randlist[holder]);
					Message md = new Message(7, g);
					omniSend(md);
					Message mc = new Message(6, 0, 0);
					omniSend(mc);
				}
				else if(msg.header.equals("FALSE RESUME")){
					if(gameState == 2){
						gameState = 1;
					}
				}
				else if(msg.header.equals("FALSE PAUSE")){
					if (gameState == 1) {
						gameState = 2;
					}
				}
			}
		}catch(Exception e){
			System.out.println("An error has occured!");
		}
	}
}