import javax.swing.*;
import java.util.Random;

public class RandAlgo{

	public static int[] randlist = new int [25];

	public static boolean isExist(int number, int counter){
		int x = 0;
		while(x < counter){
			if(number == randlist[x]){
				return false;
			}
			x++;
		}
		return true;
	}

	public static void printContent(){
		int x = 0;
		while(x < 25){
			System.out.println(randlist[x]);
			x++;
		}
	}

	public static void main(String args[]){
		int cnt = 0;
		Random rand = new Random();
		while(cnt < 25){
			int a = rand.nextInt(56);
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
		printContent();
	}


}