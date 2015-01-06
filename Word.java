/*
 * 
 Davin Chia
 * Word class to contain the string and their line numbers; to process individual words in the file.
 * 
 */
import java.util.ArrayList;
import java.util.Collections;

public class Word implements Comparable<Word> {
	
	private String element;
	private ArrayList<Integer> num;
	
	//constructor
	public Word() {
		
	}
	
	public Word(String word, int numline) {
		this.element = word;
		this.num = new ArrayList<Integer>();
		this.num.add(numline);
	}
	
	//getter
	public String element() {
		return this.element;
	}
	
	//gets all numLines stored in Word
	public String allNum() {
		String ans = "";
		for (int i = 0; i < this.num.size(); i++) {
			ans += this.num.get(i) + " ";
		}
		return ans;
	}
	
	//gets the first numLine stored in Word
	public int num() {
		return this.num.get(0);
	}
	
	//setter
	public void setElement(String x) {
		this.element = x;
	}
	
	public void addNumLine(int x) {
		this.num.add(x);
		Collections.sort(this.num);
	}
	
	//print version
	public String toString() {
		return this.element + " " + this.num();
	}


	@Override
	public int compareTo(Word o) {
		String original = this.element;
		String compare = o.element;
		return original.compareTo(compare);
	}
	
	public static void main(String[] args) {
		Word test = new Word("hello" , 1);
		Word test1 = new Word("aaaaa", 2);
		System.out.println(5);
		System.out.println(test.compareTo(test1));	
		test.addNumLine(4);
		test.addNumLine(5);
		System.out.println(test);
		System.out.println(test.num());
		
		String test2 = new String("hello");
		System.out.println(test2.compareTo("aaaaa"));
	}
	
}
