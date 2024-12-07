/*
Shriya Gautam
CSE 143
TA: Yafqa Khan
*/

import java.util.*;
import java.io.*;

/*
The QuestionsGame class allows the client to guess a
mystery object by asking a series of yes or no questions
If the computer fails to guess the object, it prompts the client
to submit the object they were thinking of along with a yes or no question,
which allows the computer to improve at the game over time.
The class can print the current tree of questions in questions.txt
*/
public class QuestionsGame{
	private QuestionNode overallRoot;
	private Scanner console;
	/*
	A private static class that implements the nodes of the Question binary tree
	Each node stores text and a reference to the left and right child nodes
	*/
	private static class QuestionNode{
		public String text;
		public QuestionNode right;
		public QuestionNode left;
		//private constructor for the QuestionNode instance class
		//takes in text for the node
		private QuestionNode(String text){
			this.text = text;
		}
	}
	
	/*
	A constructor for the QuestionsGame object. It takes in no parameters, but initializes
	the game with one object: "computer"
	*/
	public QuestionsGame(){
		overallRoot = new QuestionNode("Computer");
		console = new Scanner(System.in);
	}
	
	
	//writes the contents of the current questions that the computer is using to a PrintStream object
	//takes in a PrintStream object to output to, and returns nothing
	public void write(PrintStream output){
		writeHelper(output, overallRoot);
	}
	
	/*private helper method for write
	takes in the output and the list of questions, returning void
	It writes the questionsa and answers in order in the following format:
	Q:
	Is it an animal?
	A:
	Dog
	A:
	Truck
	*/
	private void writeHelper(PrintStream output, QuestionNode current){
		if (current.left == null || current.right == null){
			output.print("A: \n" + current.text + "\n");
		} else{
			output.print("Q: \n" + current.text + "\n");
			writeHelper(output, current.left);
			writeHelper(output, current.right);
		}
		
	}
	
	//a public method that asks the client a series of yes or no questions
	//about their object
	//If the computer guesses the object incorrectly, the user can input their object and
	//a corresponding yes or no question to add to the computer's question list
	public void askQuestions(){
		questionsHelper(overallRoot);
	}
	
	//a private helper method for questions that takes in the tree of questions and goes
	//through certain questions depending on the client's answers until it guesses an object
	private QuestionNode questionsHelper(QuestionNode root){
		if (root.right != null || root.left != null){
			System.out.println(root.text + " (y/n)?");
			String response = console.next();
			if (response.equals("y")){
				root.left = questionsHelper(root.left);
			} else if (response.equals("n")){
				root.right = questionsHelper(root.right);
			}
		} else{
			QuestionNode guess = root;
			System.out.println("Would your object happen to be " + guess.text + " (y/n)?");
			String response = console.next();
			if (response.equals("y")){
				System.out.println("Great, I got it right!");
			} else {
				return updateTree(guess);
			}
		}
		return root;
	}
	
	//a private helper method that updates the tree with the client's new object
	//in the case that the computer guesses incorrectly
	//It takes in the object and returns an updated questions tree
	private QuestionNode updateTree(QuestionNode guess){
		System.out.println("What was the name of your object?");
		String newObject = console.next();
		System.out.println("Please give me a yes/no question that");
		System.out.println("distinguishes between your object and mine-->");
		String newQuestion = "";
		if (console.hasNextLine()){
			newQuestion += console.nextLine();
			newQuestion += console.nextLine();
		}
		QuestionNode root = new QuestionNode(newQuestion);
		root.left = new QuestionNode(newObject);
		root.right = guess;
		return root;
	}
	
	//public method that reads the contents of a Scanner to add to the computer's
	//list of questions to ask about the object
	//takes in a Scanner and returns nothing
	public void read(Scanner input){
		overallRoot = readHelper(input);
		
	}
	
	//private helper method for read, takes in a Scanner of the input and returns an updated
   //    question tree the series of questions asked by the computer follows the order
   //    in the input file
	private QuestionNode readHelper(Scanner input){
		String tag = input.nextLine();
		QuestionNode text = new QuestionNode(input.nextLine());
		if (tag.equals("Q: ") || tag.equals("Q:")){
			text.left = readHelper(input);
			text.right = readHelper(input);
		}
		return text;
	}
	
	// post: asks the user a question, forcing an answer of "y" or "n";
	//       returns true if the answer was yes, returns false otherwise
	private boolean yesTo(String prompt) {
		System.out.print(prompt + " (y/n)? ");
		String response = console.nextLine().trim().toLowerCase();
		while (!response.equals("y") && !response.equals("n")) {
			System.out.println("Please answer y or n.");
			System.out.print(prompt + " (y/n)? ");
			response = console.nextLine().trim().toLowerCase();
		}
		return response.equals("y");
	}
}