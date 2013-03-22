import javax.swing.*;
import java.net.*;
import java.io.*;
public class Path{
	public static Girl head;
	public static Girl rover;

	public static void loadFromFile() throws Exception{
		String filename = "Girl.bin";
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try{
			Object temp = ois.readObject();
			if(temp.getClass().getName().equals("Girl")){
				head = (Girl) temp;
			}
		}catch(EOFException e){
			System.out.println("File loaded");
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

	public static void roveChange(){
		rover = head;
		while(rover != null){
			String temp = "";
			int a = 1;
			while(a<rover.img.length()){
				if(rover.img.charAt(a)!='/'){
					temp = temp + rover.img.charAt(a);
				}
				else{
					temp = temp + "\\";
				}
				a++;
			}
			rover.img = temp;
			rover = rover.next;
		}
	}

	public static void roveImgPrint(){
		rover = head;
		while(rover!=null){
			System.out.println(rover.img);
			rover = rover.next;
		}
	}

	public static void main(String args[]){
		System.out.println("Hello World!");
		try{
			loadFromFile();
		}catch(Exception e){
			System.out.println("Error occured!");
		}
		roveChange();
		roveImgPrint();
		try{
			saveToFile();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}