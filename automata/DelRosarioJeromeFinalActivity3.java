/**
 * Del Rosario, Jerome D.
 * BSCS 2
 * TF 10:30 - 12:00
 * November 26, 2015
 */
import java.io.*;
import java.util.*;

/**
 * A program used for conversion of an infix expression to a postfix expression, and evaluation of a postfix expression.
 * In order to properly complete the above tasks, this program utilizes the Stack Data Structures.
 */
public class DelRosarioJeromeFinalActivity3{
	private BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
	private Stack<Character> operatorStack;
	private Stack<Integer> operandStack;
	private String input, infix, postfix;
	private int value, operand2, operand1;
	private char symbol, topSymbol;	
	
	/**
	 * Initialize an empty stack for operators
	 * <1> If the scannned character is an operand, add it to the postfixExpression string. 
	 * <2> If the scanned character is an operator and if the stack is empty, Push the character to the stack.
	 * <3> If the scanned character is an Operand and the stack is not empty, compare the precedence of the character with the element on top of the stack(topSymbol). 
	 *			If topSymbol has a higher precedence over the scanned character, Pop the stack and append it to the postfixExpression String, else, Push the 
	 * 			scanned character to stack. Repeat this step as long as stack is not empty and topSymbol has precedence over the character.
	 * <4> Repeat steps 1 - 3 until all the characters from in the infixExpression are scanned.
	 * <5> (After all characters are scanned, we have to add any character that the stackmay have to the Postfix string.) If stack is not empty add topStack to Postfix stringand Pop the stack. Repeat this step as long as stack is not empty.
	 * <6> Return the Postfix string.
	 */	
	public String convertToPostfix(String infixExpression){
		infixExpression = infixExpression.replaceAll("\\s",""); //removes all uneccessary spaces from the expression
		String postfixExpression = "";
		operatorStack = new Stack<Character>();
		System.out.println("\n=====================================================================\nShowing the step by step proccess of obtaining the postfix expression\n");
		System.out.printf("%-15s%-30s%-15s%n","Current Symbol","postfixExpression","operatorStack");
		for(int index=0;index<infixExpression.length();index++){
			symbol = infixExpression.charAt(index);
			if(Character.isLetter(symbol) || Character.isDigit(symbol)){ // executes when symbol is an operand
				postfixExpression = postfixExpression+symbol;
			}else{ // executes when the symbol is an operator
				if(operatorStack.isEmpty() || operatorStack.peek() == '(' || symbol == '(' || precedence(symbol, operatorStack.peek()))
					operatorStack.push(symbol);
				else if(symbol == ')'){
					while(!operatorStack.isEmpty() && operatorStack.peek() != '('){
						topSymbol = operatorStack.pop();
						postfixExpression = postfixExpression+topSymbol;
					} // appends all the operators to the postfixExpression until the open parenthesis
					if(!operatorStack.isEmpty() && operatorStack.peek() == '(')
						operatorStack.pop(); // discards the open parenthesis
				}else if(!precedence(symbol, operatorStack.peek())){
					while(!operatorStack.isEmpty() && precedence(operatorStack.peek(), symbol)){
						topSymbol = operatorStack.pop();
						postfixExpression = postfixExpression+topSymbol;
					}
					if(!operatorStack.isEmpty() && precedenceLevel(symbol) == precedenceLevel(operatorStack.peek())){ // executes when the precedence of the two operators is equal
						topSymbol = operatorStack.pop();
						postfixExpression = postfixExpression+topSymbol;
					}
					if(symbol != ')')
						operatorStack.push(symbol);
				}
			}
			System.out.printf("%-15s%-30s%-15s%n",symbol,postfixExpression,operatorStack.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(", ", ""));
		}
		while(!operatorStack.isEmpty()){ // executes when all the characters in the infixExpression has been scanned but there are still elements from the stack
			topSymbol = operatorStack.pop();
			postfixExpression = postfixExpression+topSymbol;
			System.out.printf("%-15s%-30s%-15s%n",symbol,postfixExpression,operatorStack.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(", ", ""));
		}
		return postfixExpression;
	}
	
	/**
	 * Initialize an empty stack for the operands.
	 * <1> If the character scanned is an operand, the character is pushed to the stack.
	 * <2> If the character scanned is an operator, the stack will pop 2 of it's elements and set the value to the result of applying the character scanned
	 *			to the two operands.
	 * <3> Push the value to the stack.
	 * <4> Repeat the steps 1-3 until all characters are scanned.
	 * <5> Return the value by popping the stack.
	 */
	public int evaluatePostfix(String postfixExpression){
		postfixExpression = postfixExpression.replaceAll("\\s",""); //removes all uneccessary spaces from the expression
		operandStack = new Stack<Integer>();
		System.out.println("\n==================================================================================\nShowing the step by step proccess of obtaining the value of the postfix expression\n");
		System.out.printf("%-15s%-15s%-15s%-6s%-25s%n","Current Symbol","First Operand","Second Operand","value","operandStack");
		for(int index=0;index<postfixExpression.length();index++){
			symbol = postfixExpression.charAt(index);
			if(Character.isDigit(symbol)){
				operandStack.push(Character.getNumericValue(symbol));
			}else{
				operand2 = operandStack.pop();
				operand1 = operandStack.pop();
				switch(symbol){
					case '+':
						value = operand1 + operand2;
						break;
					case '-':
						value = operand1 - operand2;
						break;
					case '*':
						value = operand1 * operand2;
						break;
					case '/':
						value = operand1 / operand2;
						break;
					case '$':
						value = (int) Math.pow((double) operand1, (double) operand2);
						break;
				}
				operandStack.push(value);
			}
			System.out.printf("%-15s%-15s%-15s%-6s%-25s%n",symbol,operand1,operand2,value,operandStack.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", ""));
		}
		return (operandStack.pop());
	}
	
	/**
	 * Reads a string from the user.
	 */
	private String readString(String message) {
		System.out.print(message+" ");
		System.out.flush();
		try {
			input = inputStream.readLine();
		}catch ( IOException io) {
			// ahueheu
		}
		return input;
	}
	
	/**
	 * Serves as the main menu for the program. 
	 * Let's the user to choose either to convert an infix expression to a postfix expression or,
	 * evaluate a postfix expression.
	 */
	public void run(){
		System.out.println("Hello, this is a simple program for conversion of infix expression to postfix expression and evaluation of a postfix expression.");
		while (true) {
			char option = readString("\nWhat do you want to do?\n" +
				"1. Convert an infix expression.\n" +
				"2. Evaluate a postfix expression\n"+
				"3. Quit program.\n"+
				"Type in your choice here: ").charAt(0);
			switch (option) {
				case '1':
					System.out.println("================================================================================");
					infix = readString("Type in an infix expression (minimize the use of 'spaces'): ");
					postfix = convertToPostfix(infix);
					System.out.println("=====================================================================");
					System.out.println("\nThe expression: "+infix+" at a postfix expression is: "+postfix);
					break;
				case '2':
					System.out.println("=============================================================================================================");
					postfix = readString("Type in a postfix expression you want to evaluate (minimize the use of 'spaces'): ");
					value = evaluatePostfix(postfix);
					System.out.println("==================================================================================");
					System.out.println("\nThe expression: "+postfix+" is equal to: "+value);
					break;
				case '3':
					System.out.println("Good bye.");
					return;
				default:
					System.out.println("Please choose among the choices only.");
			}
		}
	}
	
	public static void main(String[] args){
		DelRosarioJeromeFinalActivity3 program;
		try{
			program = new DelRosarioJeromeFinalActivity3();
			program.run();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	/** 
	 * Gives an operator a value, depending on it's precedence among other operators.
	 * The higher the precedence of an operator, the higher it's value.
	 */
	private int precedenceLevel(char op) {
	    switch (op) {
	    	case ')':
	    		return  -1;
	        case '+':
	        case '-':
	            return 0;
	        case '*':
	        case '/':
	            return 1;
	        case '$':
	            return 2;
	        case '(':
	        	return 3;
	        default:
	            throw new IllegalArgumentException("Operator unknown: " + op);
	    }
	}
	
	/**
	 * Compares two operators based on their precedence.
	 * Returns true if the first operator has a higher precedence compared to the second one, and
	 * returns false if the first operator has a(n) lower/equal precedence comapared to the second operator.
	 */
	private boolean precedence(char operator1, char operator2){
		if(precedenceLevel(operator1)>precedenceLevel(operator2))
			return true;
		else
			return false;
	}
}