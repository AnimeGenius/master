import javax.swing.*;
import java.io.*;
import java.net.*;

public class Message implements Serializable{
	public String header;
	public int source;
	public int target;
	public int aux1; // for score
	public String aux2; //for answer
	public Girl girl; //null for all functions except for append item protocol/append girl protocol
	public MyConnection conn;
	
	public Message(int mtype, int a)
	{
		if(mtype == 1){
			header = "START GAME";
			source = 8888;
		}
		else if(mtype == 5){
			header = "END GAME";
			source = 8888;
		}
		else if(mtype == 9){
			header = "EXIT";
			source = a;
		}
	}

	public Message(int mtype, int targ, MyConnection c){
		if(mtype == 2){
			header = "JOIN NETWORK";
			target = targ;
			conn = c;
		}
	}

	public Message(int mtype, int src, int targ){
		if(mtype == 3){
			header = "PAUSE GAME";
			source = src;
			target = targ;
		}
		else if(mtype == 4){
			header = "RESUME GAME";
			source = src;
			target = targ;
		}
		if(mtype == 6){
			header = "APPEND SCORE";
			target = targ;
			source = 8888;
			aux1 = src;
		}
	}

	public Message(int mtype, Girl g){
		if(mtype == 7){
			header = "APPEND GIRL";
			source = 8888;
			girl = g;
		}
	}

	public Message(int mtype, int src, String ans){
		if(mtype == 8){
			header = "ANSWER";
			source = src;
			aux2 = ans;
		}
		else if(mtype == 10){
			header = ans;
			source = src;
		}
	}

	public Message(String a){
		header = a;

		/*
			a = "FALSE START"
			when received by client, notifies the servert to get the value of the randlist on the myconnection
			when received by servert, gets the randlist from the myconnection and sets the servert's gamestate = 1
		*/

		/*

			a = "FALSE PAUSE"
			when received by client, notifies the servert to set the value of the gamestate = 2
			when received by servert, set the gamestate = 2
		*/

		/*

			a = "FALSE RESUME"
			when received by client, notifies the servert to set the value to the gamestate = 1
			when received by servert, set the gamestate = 1
		*/

		/*
			
			a = "FALSE END"
			when receive by the client, repaints the ehandler button to start game
		*/
	}

}