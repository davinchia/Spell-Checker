/*
 * Davin Chia
 *
 * This is a HashTable for lower and upper case alphabets. 
 * 
 */
import java.util.ArrayList;

public class HashTable {
	private ArrayList<Word> table[] = new ArrayList[52];
	
	//constructor 
	public HashTable() {
		for (int i = 0; i < table.length; i++) {
			table[i] = new ArrayList<Word>();
		}
	}
	
	//add the word, including its line number into the respective bucket/ArrayList
	public void add(int i, Word word) {
		if ( i < 0 && i > table.length)
			return;		//do nothing if index is out of bounds
		else { 
			String current = word.element();
			//check if the specific list already contains the word
			if (!contains(this.table[i], current))
				//we add if it does not contain the word
				this.table[i].add(word);
			else {
				//else we update the line number
				Word add = get(table[i], current);	//get the specific object containing the String
				add.addNumLine(word.num());
			}
		}
	}
	
	//master function that calls quicksort on all the different ArrayLists that 
	//exist in the HashTable
	public void sort() {
		for (int i = 0; i < table.length; i++) {
			quickSort(this.table[i], 0, this.table[i].size() - 1);
		}
 	}
	
	//sorting function to sort each individual ArrayList
	public void quickSort(ArrayList<Word> list, int lowerIndex, int higherIndex) {
		int i = lowerIndex;
		int j = higherIndex;
		if (!(higherIndex < 0)) {
			//call insertionSort if there are 3 or less elements in the index
			if ((higherIndex - lowerIndex) <= 2) 
				insertionSort(list, lowerIndex, higherIndex);
			//get the pivot
			int pivotNum = lowerIndex + (higherIndex - lowerIndex)/2;
			Word pivot = list.get(pivotNum);
			while (i <= j) {
				//System.out.println("quickSort");
				while (list.get(i).compareTo(pivot) < 0) {
					i++;
				}
				while (list.get(j).compareTo(pivot) > 0) {
					j--;
				}
				if (i <= j) {
					swap(list, i, j);
					i++;
					j--;
				}
			}
		}
		//call quickSort recursively
		if (lowerIndex < j) 
			quickSort(list, lowerIndex, j);
		if (i < higherIndex)
			quickSort(list, i, higherIndex);
	}
	
	//swapping function for sorting
	public void swap(ArrayList<Word> list, int i, int j) {
		Word temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}
	
	//insertion sort for sizes less than 3
	public void insertionSort(ArrayList<Word> list, int lowerIndex, int higherIndex) {
		//System.out.println("insertionSort");
		for(int i = lowerIndex; i <= higherIndex; i++) {
			//System.out.println(i);
			//System.out.println("original " + list);
			Word temp = list.get(i);
			int j;
			for (j = i-1; j >= 0 ; j--) {
				list.set(j+1 , list.get(j));
				//System.out.println("in " + list);
				if (temp.compareTo(list.get(j)) > 0) {
					break;
				}
			}
			//System.out.println("J: " + j);
			list.set(j+1, temp);
			//System.out.println("changed " + list);
		}
	}
	
	//hash function that returns 0 - 52 based on the first alphabet of the string
	public int hash(Word word) {
		int current = word.element().charAt(0);
		//System.out.println(current);
		int hash = -1;
		//System.out.println(current);
		//maps A - Z to 0 - 26; first 26 buckets
		if ( 65 <= current && current <= 90) {
			hash = current - 65;
		}
		//maps a - z to 26 - 52; next 26 buckets 
		else if (97 <= current && current <= 122) {
			hash = current - 97 + 25;
		}
		return hash;
	}
	
	//get function that returns the Word object that contains a specific String in a specific List
	public Word get(ArrayList<Word> list, String x) {
		Word ans = new Word();
		Word current;
		String test;
		for(int i = 0; i < list.size(); i++) {
			current = list.get(i);
			test = current.element();
			if (x.equals(test))
				ans = current; 
		}
		return ans;
	}
	
	
	//contains function that checks all ArrayLists in table for String
	public boolean contains(String word) {
		Word current;
		String test;
		for (int i = 0; i < table.length; i++) {
			for(int j = 0; j <table[i].size(); j++) {
				current = table[i].get(j);
				test = current.element();
				if (word.equals(test))
					return true;
			}
		}
		return false;
	}
	
	//contains function that checks specific ArrayList for String
		public boolean contains(ArrayList<Word> list, String word) {
			Word current;
			String test;
			for (int i = 0; i < list.size(); i++) {
				current = list.get(i);
				test = current.element();
				if (word.equals(test))
					return true;
			}
			return false;
		}
	//add all objects in HashTable to the String to be printed
	public String getAll() {
		String content = "";
		for(int i = 0; i < table.length; i++) {
			for(int j = 0; j < table[i].size(); j++) {
				Word print = table[i].get(j);
				content += print.element() + " " + print.allNum() + "\n";
			}
		}
		return content;
	}
	
	//print function
	public void printAll() {
		for(int i = 0; i < table.length; i++) {
			for(int j = 0; j < table[i].size(); j++) {
				String print = table[i].get(j).element();
				System.out.println(print);
			}
		}
	}
	
	public static void main(String[] args) {
		
		//the following are all test prompts

		HashTable test = new HashTable();
		/*
		for(int i = test.table.length - 1; i >= 0; i--) {
			Word current = new Word("hello" + i, i);
			test.add(i, current);
		}*/
		
		Word current = new Word("hello", 1);
		Word current1 = new Word("Uaabbb", 2);
		Word current2 = new Word("azzzes", 3);
		test.add(1, current2);
		test.add(1, current);
		test.add(1, current2);
		test.add(1, current);
		test.add(1, current1);
		test.add(1, current1);
		test.printAll();
		System.out.println();
		test.quickSort(test.table[1], 0, test.table[1].size() - 1);
		test.printAll();
		System.out.println();
		String test1;
		test1 = test.getAll();
		System.out.println(test1);
		System.out.println(1);
		String test2 = "Uaaaaa";
		System.out.println(test2.compareTo("abababa"));
		System.out.println(test.contains("hello"));
		System.out.println();
		Word Hash = new Word ("u", 0);
		System.out.println(test.hash(Hash));
	}

}
