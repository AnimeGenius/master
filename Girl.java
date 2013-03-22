import javax.swing.*;
import java.io.*;
import java.net.*;
public class Girl implements Serializable{
	public String name;
	public String alt;
	public String img;
	public Girl next;
	public Girl(String a, String b, String c){
		name = a;
		img = b;
		alt = c;
	}
}