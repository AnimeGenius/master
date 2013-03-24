import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class ExtensionChanger{
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

	public static String changeExtension(String a){
		int cnt = 0;
		String tempo = "";
		while(cnt < (a.length() - 5)){
			tempo = tempo + a.charAt(cnt);
			cnt++;
		}
		tempo = tempo + ".jpg";
		return tempo;
	}

	public static void roverChange(){
		rover = head;
		while(rover!=null){
			if(rover.img.endsWith(".jpeg")){
				String holder = rover.img;
				String temp = changeExtension(holder);
				System.out.println(temp);
				rover.img = temp;
			}
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


	public static void main(String args[]){
		System.out.println("Hello World!");
		try{
			loadFromFile();
		}catch(Exception e){
			System.out.println("Error Occured!");
		}
		roverChange();
		try{
			saveToFile();
		}catch(Exception e){
			
		}
	}
}