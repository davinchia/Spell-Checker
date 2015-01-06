/*
 * Davin Chia
 * Functions implemented:
 * swap, delete, replace, insert split
 */
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class Driver {
	//global variable 
	static SeparateChainingHashTable<String> hash = new SeparateChainingHashTable<String>();	//hash to store words in dictionary
	static Scanner scan = new Scanner(System.in);			//for user input
	static LinkedList<Word> words; //global linkedlist for holding words in each file
	static SeparateChainingHashTable<String> index; 	//to store misspelled words
	
	//reads in the dictionary and stores in hash
	public static void readDict() throws IOException {
		System.out.println("Reading in Dictionary...");
		//reads dictionary file
		File file = new File("Dictionary.txt");
		Scanner dict = new Scanner(file);
		while (dict.hasNextLine()) {
			String word = dict.nextLine();
			hash.insert(word);		//inserts into file
		}
		System.out.println("Dictionary Read");
		dict.close();
	}
	
	//get userFile
	public static String getUserFile() {
		System.out.println("Please enter a file to spell check>>");
		String file = scan.nextLine();
		return file;
	}
	
	//read in user's File and store into Word object
	public static void readFile(File file) throws IOException {
		Scanner read = new Scanner(new FileReader(file));
		words =  new LinkedList<Word>();
		Scanner lineRead;
		String line;
		int lineNum = 1;
		while (read.hasNextLine()) {
			line = read.nextLine();
			lineRead = new Scanner(line);
			//adds words line by line to arraylist and stores number with them
			while (lineRead.hasNext()) {
				String current = lineRead.next();
				Word e = new Word(current, lineNum);
				words.add(e);
			}
			lineNum++;		//increments after every line
		}
	}
	
	//analyses the File; writes misspelled word in output file; stores misspelled words in hashTable
	public static void analyseFile(File file) throws IOException {
		String misspelled = "";
		index = new SeparateChainingHashTable<String>();
		for (int i = 0; i < words.size(); i++) {
			Word current = words.get(i);
			String word = current.element();
			if (!hash.contains(word)) {
				misspelled += current.element() + " " + current.num() + "\n";
				index.insert(word);
			}
		}
		String name = "misspelled-" + file.getName();
		writeFile(name, misspelled);
	}
	
	//get user command
	public static char getCommand() {
		System.out.println("Print Words(p), enter new file(f) or quit (q)");
		char ans = scan.nextLine().charAt(0);
		return ans;
	}
	
	//print misspelled words and get user to correct them
	static void printMisspelled() {
		SeparateChainingHashTable <String> ignore = new SeparateChainingHashTable<String>();	//we accumulate all the words to be ignored
		char comm;
		for (Word word : words) {		//we go through the file containing normal words and compare them to misspelled ones
			String check = word.element();	//turn word into a string
			if (index.contains(check)) {
				if (!ignore.contains(check)) {		//we only proceed if string has not been 'ignored'
					System.out.println(word);
					System.out.println("ignore all(i), suggest(s), next(n)?");
					comm = scan.nextLine().charAt(0);
					switch(comm) {
						case 'i':
							ignore.insert(check);		//we ignore, add to list to be ignored and proceed to the next word
							break;
						case 's':
							suggest(check, word);
							break;
						case 'n':
							continue;
					}
				}
			}
		}
		System.out.println("Spell-check complete.");
	}
	
	//suggest corrections for user and replaces wrong words with user's choice
	public static void suggest(String check, Word word) {
		LinkedList<String> ans = new LinkedList<String>();
		
		//adds the different answers to linkedlist
		swap(check, ans);	//use swap to get answers
		deletes(check, ans);	//use deletes to get answers
		replace(check, ans);	//use replacements to get answers
		insert(check, ans);		//use inserts to get answers
		split(check, ans);		//checks if there are two separate words in the word
		//if no suggested answers
		if (ans.isEmpty()) {
			System.out.println("Sorry, there are no suggestions!");
		}
		else {
			System.out.print("Replace with");
			for(int j = 0; j < ans.size(); j++) {
				System.out.print(" (" + (j + 1) + ")" + ans.get(j));
			}
			System.out.println(", or next(n)?");
			//if user wants to replace word
			if (scan.hasNextInt()) {
				int choice = scan.nextInt() - 1;
				scan.nextLine();
				//replace the element in Word
				word.setElement(ans.get(choice));
			}
			//we assume user has typed n and clear the scanner for next input
			else 
				scan.nextLine();
		}
	}
	
	//swaps the words and check them, returning an ArrayList of correct words
	public static void swap(String word, LinkedList<String> ans) {
		String test;
		char[] current = word.toCharArray();
		char[] test1;
		char temp;
		for (int i = 0; i < current.length - 1; i++)	{
			test = "";
			test1 = new char[current.length];
			//copy original to do work on
			for (int k = 0; k < current.length; k++) {
				test1[k] = current[k];
			}
			temp = test1[i];
			test1[i] = test1[i + 1];
			test1[i + 1] = temp;
			//form a string and test if hash contains it
			test = new String(test1);
			if (hash.contains(test) && !ans.contains(test)) {
				ans.add(test);
			}
		}
	}
	
	//deletes last char from word till only first char remains and checks if word exist
	public static void deletes(String word, LinkedList<String> ans) {
		String current = word;
		do{
			current = current.substring(0, current.length() - 1);
			if (hash.contains(current) && !ans.contains(current)) {
				ans.add(current);
			}
		}while(current.length() > 1);
	}
	
	//replaces each character in word with letter from 'a - z' and checks if the word exists
	//in the dictionary
	public static void replace(String word, LinkedList<String> ans) {
		String test;
		char[] current = word.toCharArray();
		char[] test1;
		//go through each letter
		for(int i = 0; i < current.length; i++) {
			//copy original array to test on
			test1 = new char[current.length];
			for (int h = 0; h < current.length; h++) {
				test1[h] = current[h];
			}
			//replace with lowercase
			for(int j = 97; j < 123; j++) {
				test1[i] = (char) j;
				//form string
				test = new String(test1);
				//System.out.println(test);
				//test
				if (hash.contains(test) && !ans.contains(test)) {
					ans.add(test);
				}
			}
			//replace with uppercase
			for(int k = 65; k < 91; k++) {
				test1[i] = (char) k;
				//form string
				test = new String(test1);
				if (hash.contains(test) && !ans.contains(test)) {
					ans.add(test);
				}
			}
		}
	}
	
	//insert a character from a - z in between each character in the word, starting before the
	//first character; checks result with dictionary hashtable
	public static void insert(String word, LinkedList<String> ans) {
		String test;
		char[] current = word.toCharArray();
		//convert to a linkedlist for convenient inserts
		LinkedList<Character> test2 = new LinkedList<Character>();
		for (int a = 0; a < current.length; a++) {
			test2.add(current[a]);
		}
		for(int i = 0; i < test2.size() + 1; i++) {
			//insert lowercase
			for(int j = 97; j < 123; j++) {
				//insert into string
				test2.add(i, (char) j);
				//form string
				test = "";
				for(int k = 0; k < test2.size(); k++) {
					test += test2.get(k);
				}
				if (hash.contains(test) && !ans.contains(test)) {
					ans.add(test);
				}
				test2.remove(i); //delete element 
			}
			//insert uppercase
			for(int l = 65; l < 91; l++) {
				//insert into string
				test2.add(i, (char) l);
				//form string
				test = "";
				for(int m = 0; m < test2.size(); m++) {
					test += test2.get(m);
				}
				if (hash.contains(test) && !ans.contains(test)) {
					ans.add(test);
				}
				test2.remove(i); //delete element
			}
		}	
	}
	
	//inserts a space between character in a word to find if both words have been accidentally merged
	public static void split(String word, LinkedList<String> ans) {
		String test;
		String test1;
		char[] current = word.toCharArray();
		//insert spaces in between characters
		for (int i = 1; i < current.length; i++) {
			test = "";
			test1 = "";
			//effectively insert a space in between the characters by breaking them into two 
			//strings and checking the strings separately
			for (int j = 0; j < i; j++) {
				test += current[j];
			}
			for (int j = i; j < current.length; j++) {
				test1 += current[j];
			}
			//check if it is in dictionary
			if (hash.contains(test) && hash.contains(test1)) {
				if (!ans.contains(test))
					ans.add(test);
				if (!ans.contains(test1))
					ans.add(test1);
			}
		}
	}
	//print corrected file
	public static void writeCorrected(File file) throws IOException {
		String name = "corrected-" + file.getName();
		String write = "";
		File out = new File(name);
		if (!out.exists()) {
			out.createNewFile();
		}
		int currentLine = 1;
		for (int i = 0; i < words.size(); i++) {
			Word current = words.get(i);
			if (current.num() > currentLine) {
				currentLine = current.num();
				write += "\n";
			}
			write += current.element() + " ";
		}
		FileWriter print = new FileWriter(out);
		print.write(write);
		print.close();
	}
	
	//writeFile to output
	public static void writeFile(String name, String content) throws IOException {
		File out = new File(name);
		if (!out.exists()) {
			out.createNewFile();
		}
		BufferedWriter write = new BufferedWriter (new FileWriter(out.getAbsoluteFile()));
		write.write(content);
		write.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		//read the dictionary and store it in a S.C. HashTable
		readDict();
		
		//getUserFile
		boolean run = true;
		while(run) {
			String fileName = getUserFile();
			File file = new File(fileName);
			if (!file.exists()) {
				System.out.println("Error! The file does not exist!"); 	//error checking
			}
			readFile(file);		//read the file and store the words as Word objects
			analyseFile(file);	//compare the contains of the Word objects against the dictionary and identify misspelled	
			writeCorrected(file);	//we write the corrected version in case user does not want to alter words; later alterations will overwrite this
			while(true) {
				char com = getCommand();
				if (com == 'q') {
					run = false;
					break;
				}
				else if (com == 'p') {
					printMisspelled();
					writeCorrected(file);
				}
				else if (com == 'f') {
					break;
				}
				else 
					System.out.println("Error. Wrong input.");
			}
		}
		System.out.println("Goodbye!");
	}
}
