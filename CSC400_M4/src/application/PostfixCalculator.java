package application;
//-------------------------------------------
//Pseudocode
//Author: Hailey King-Winterton
//Due Date: 09/08/2024
//-------------------------------------------
//Start
//		1. User input method: Initialize stack to store values
//			- Scan input expression
//				- if input is number, push into stack
//				- if input is operator, pop operands from stack & 
//					- solve
//					- push expression result into stack
//			- Output result
//		2. File upload method: 
//			- read file
//			- evaluate postfix expression line by line
//			- output each result
//		3. Main: 
//			- show menu options
//				- if 1: postfix from user input
//				- if 2: postfix from file
//				- if 3: exit program
//End
//-------------------------------------------
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PostfixCalculator {
    // Evaluate postfix method
	public int evaluatePostfix(String postfixExpression) {
	    Stack<Integer> stack = new Stack<>();
	    
	    try {
	        int length = postfixExpression.length();
	        for (int i = 0; i < length; i++) {
	            char c = postfixExpression.charAt(i);
	            // Ignore spaces
	            if (c == ' ') {
	                continue;
	            }
	         // If the character is a digit, process the full number (handles multi-digit numbers)
	            if (Character.isDigit(c)) {
	                int num = 0;
	                while (i < length && Character.isDigit(postfixExpression.charAt(i))) {
	                    num = num * 10 + (postfixExpression.charAt(i) - '0');
	                    i++;
	                }
	                i--; // adjust after processing
	                stack.push(num); // push onto stack
	            } else { // check for 2 operands on stack for operator
	                if (stack.size() < 2) {
	                    System.out.println("Error: Invalid expression.");
	                    return Integer.MIN_VALUE; 
	                }
	                // pop two operands from stack
	                int num2 = stack.pop();
	                int num1 = stack.pop();
	                // perform operation - based on operator
	                switch (c) { 
	                    case '+': // Add operator
	                        stack.push(num1 + num2);
	                        break;
	                    case '-': // Subtract operator
	                        stack.push(num1 - num2);
	                        break;
	                    case '*': // Multiply operator
	                        stack.push(num1 * num2);
	                        break;
	                    case '/': // Divide operator
	                        if (num2 == 0) { // check for division by zero
	                            System.out.println("Error: Division by zero.");
	                            return Integer.MIN_VALUE; 
	                        }
	                        stack.push(num1 / num2);
	                        break;
	                    case '%': // Modulus operator
	                        stack.push(num1 % num2);
	                        break;
	                    default: // Error handling
	                        System.out.println("Error: Invalid operator.");
	                        return Integer.MIN_VALUE;
	                }
	            }
	        }
	        // after processing, stack should contain one element (result)
	        if (stack.size() != 1) { // error handling
	            System.out.println("Error: Invalid expression. Stack has " + stack.size() + " items instead of one.");
	            return Integer.MIN_VALUE;
	        }
	        return stack.pop(); // return final result
	        
	    } catch (Exception e) { // error handling
	        System.out.println("Error: " + e.getMessage());
	        return Integer.MIN_VALUE;
	    }
	}  
	// Read & evaluate postfix from file method
    public void expressionFromFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) { // check if file exists
                System.out.println("Error: File does not exist.");
                return; // exit if file does not exist
            }
            Scanner fileScanner = new Scanner(file); // read file scanner
            while (fileScanner.hasNextLine()) { // read each line of file
                String expression = fileScanner.nextLine().trim();
                int result = evaluatePostfix(expression);
                if (result != Integer.MIN_VALUE) { // if valid, print
                    System.out.println("Result: " + result);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) { // error handling
            System.out.println("Error: File not found.");
        }
    }
	// Main
    public static void main(String[] args) {
        PostfixCalculator calculator = new PostfixCalculator();
        Scanner scanner = new Scanner(System.in); // user input scanner
        boolean exit = false;
        // Menu loop
        while (!exit) { 
            System.out.println("----Postfix Calculator Program----");
            System.out.println("_____________Main-Menu_____________");
            System.out.println("1. Input expression");
            System.out.println("2. Upload expression from file");
            System.out.println("3. Exit");
            System.out.println("___________________________________");
            System.out.print("Choose an option: ");
            
            int choice = 0;
            try {
                choice = scanner.nextInt(); // read user choice
                scanner.nextLine();
            } catch (InputMismatchException e) { // handling non-integer input
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue; 
            }
            // Menu choice handling
            switch (choice) {
            	// Input postfix expression
                case 1: 
                    System.out.print("Enter a postfix expression (ex: 1 2+): ");
                    String expression = scanner.nextLine().trim();
                    int result = calculator.evaluatePostfix(expression); // evaluate
                    if (result != Integer.MIN_VALUE) { // print if valid
                        System.out.println("Result: " + result);
                    }
                    break;
                // File of postfix expressoins
                case 2: 
                    System.out.print("Enter filename of postfix expression (ex: expressions.txt): ");
                    String filename = scanner.nextLine().trim();
                    calculator.expressionFromFile(filename); // evaluate 
                    break;
                // Exit program
                case 3: 
                    exit = true;
                    System.out.println("Exiting program");
                    break;
                // Default
                default: // handling invalid options
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }
}


