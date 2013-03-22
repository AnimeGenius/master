import javax.swing.*;
import java.io.*;
import java.net.*;
public class GirlDB{
	public static Girl head;
	public static Girl tail;
	public static Girl rover;
	public static Girl holder;

	public static void getTail(){
		rover = head;
		while(rover.next != null){
			rover = rover.next;
		}
		tail = rover;
	}


	public static void insert(Girl newNode){

		if(head == null){
			head = newNode;
			tail = newNode;
		}
		else{
			tail.next = newNode;
			tail = tail.next;
		}

	}

	public static void listItem(){
		rover = head;
		while(rover != null){
			System.out.println("" + rover.name + " : " + rover.alt + " : " + rover.img);
			rover = rover.next;
		}
	}

	public static void saveToFile() throws Exception{
		String filename = "Girl.bin";
		File file = new File(filename);
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(head);
		oos.close();
	}

	public static void loadFromFile() throws Exception{
		String filename = "Girl.bin";
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try{
			Object temp = ois.readObject();
			if(temp.getClass().getName().equals("Girl")){
				head = (Girl) temp;
				getTail();
			}
		}catch(EOFException e){
			System.out.println("File loaded");
		}
	}


	public static void main(String args[]){
		int a = 0;
		while(a == 0){
			int d = Integer.parseInt(JOptionPane.showInputDialog("Choose your decision:\n1 - Add a Girl\n2- Save to DB\n3- Load from DB\n4- List Items\n5- Exit"));
			if(d == 1){
				String name = JOptionPane.showInputDialog("Input Name:");
				String img = JOptionPane.showInputDialog("Input img location and name:");
				String alt = JOptionPane.showInputDialog("Input character's alternate name:");
				holder = new Girl(name, img, alt);
				insert(holder);
			}
			else if(d == 2){
				try{
					saveToFile();
				}catch(Exception e){
					System.out.println("Error in saving file");
				}
			}
			else if(d == 3){
				try{
					loadFromFile();
				}catch(Exception e){
					System.out.println("Error from loading the file.");
				}
			}
			else if(d == 4){
				listItem();
			}
			else if(d == 5){
				a = 1;
			}
		}
	}
}