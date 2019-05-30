package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Algo
{
	private static ArrayList<String> equation = new ArrayList<String>();
	private static int i = 0;

	public static void convert(String infix) throws IndexOutOfBoundsException
	{
		Stack<String> s = new Stack<String>(); // Used for Infix to Postfix
		ArrayList<String> a = new ArrayList<String>(Arrays.asList(infix.split("") ) ); // Convert Infix to ArrayList
		i=0;
		
		while(i != a.size()-1) 
		{
			if(isNum(a.get(i)) && isNum(a.get(i+1) ) ) // Concatenate Multidigit Numbers
			{
				a.set(i, a.get(i) + a.get(i+1) ); 
				a.remove(i+1);
				continue;			// Continue is used as not to increment i
			}
			
			else if((isNum(a.get(i) ) && a.get(i+1).equals("(")  ) 		// Add * to "number-(" condition. Allows 9(5+3)
				|| (a.get(i).equals(")") && isNum(a.get(i+1) ) ) )  	// Add * to ")-number" condition. Allows (5+3)9
			{
				a.add(i+1,"*");
			}
			
			else if(a.get(i).equals("-") ) // Concatenate - with number
			{
				for(int j=i-1; j>0 ;j--) // Test if - is for subtraction or negation. Ignoring brackets ,if first find operator then negation. If first find number then negation 
				{
					if(isOper(a.get(j) ) )
					{
						a.set(i, a.get(i) + a.get(i+1) ); 
						a.remove(i+1);
						break;
					}
					else if(isNum(a.get(j) ) )
					{
						break;
					}
				}
				
				if(i == 0) // If - is found as first element , that means negation
				{
					a.set(i, a.get(i) + a.get(i+1) ); 
					a.remove(i+1);
					continue;
				}
			}
			
			else if(a.get(i).equals(".") )  // Concatenate numbers to decimal points
			{
				if( isNum(a.get(i-1) ) && isNum(a.get(i+1) ) ) 
				{
					a.set(i,a.get(i-1) + a.get(i) + a.get(i+1) ); 
					a.remove(i-1);
					a.remove(i);
					i--;
					continue;	// Continue is used as not to increment i
				}
			}
			i++;
		}
		
		for(i=0; i< a.size(); i++) // Begin Infix-PostFix
		{
			switch(a.get(i) )
			{
				case "(": 				// If a “(“ bracket is found, push it onto the Stack
					s.push(a.get(i) );
					break;
					
				case ")": // If a “)” bracket is found, pop the Stack and save the popped values. Keep popping until a “(“bracket is found.
					while (!s.isEmpty() ) 
	                {
				         if (s.peek().equals( "(" )) 
				         {
				        	 s.pop();
					         break;	 
				         }
				         else 
			        	 {
				        	 equation.add(s.pop() ); 
			        	 }
	                }
					break;
					
				case "+": case "-": case "*": case "/": //If an operator is found, compare its precedence priority with a “peak” of the stack. If the operator has higher precedence than the peak element, push it to the stack
					 while (!s.isEmpty()) 
				     {
						 if (s.peek().equals("(") ) 
				         {
				            break;
				         } 
				         else 
				         {
				        	 if(pre(a.get(i) ) > pre(s.peek() ) )
		        			 {
				                 break;
		        			 }
				        	 else 
				        	 { 
				        		 equation.add(s.pop() );
				        	 }
				         } 
				     }
					 s.push(a.get(i) );
		             break;
	                
				default: // If an operand (number) is found, save its value
					equation.add(a.get(i) );
			}
		}
		
		while (!s.isEmpty() )  // Upon reaching the end of the infix expression, pop all the elements in the stack and append them to the end of the postfix ArrayList.
		{
			equation.add(s.pop() );
	    }
		
		for(i=0; i < equation.size() ;i++) // Checks for a missmatch of brackets
		{	
			if(equation.get(i).contains(")") || equation.get(i).contains("(") )
			{
				gui.output.setText("Bracket Missmatch");
				equation.clear();
				return;
			}
		}
		
		try 
		{
			result();
			gui.output.setText(infix);
		}
		catch(IndexOutOfBoundsException e)
		{
			gui.output.setText("Error in Equation passed");
		}
		catch(divZero e)
		{
			gui.output.setText("Cannot divide by zero");
		}
		catch(Exception e)
		{
			gui.output.setText("Uknown Error");
		}
		finally
		{
			equation.clear();
		}
	}

	private static int pre(String s) // Checks the precedence of passed value
	{
        switch (s) 
        {
	        case "+": case "-":
	            return 1;
	
	        case "*": case "/":
	            return 2;
        }
        return -1;
    }	
	
	public static boolean isNum(String s)  // Return true if the string passed into the method is a number, it will return false if it isnt a number.
	{
		try  
		{  
			@SuppressWarnings("unused")
			double d = Double.parseDouble(s);  
		}  
		catch(NumberFormatException e)  
		{  
			return false;  
		}
		return true;  
	}
	
	public static boolean isOper(String s) // Return true if the string passed into the method is a operator such as "+" or "-". It will return false if it isnt a operator.
	{  
		switch(s)
		{
			case "*": case "/": case "+": case "-": case ".":
			return true;
			
			default: 
			return false;
		}
	}
	
	public static boolean isBrac(String s) // Return true if the string passed in is a open or close bracket. It will return false if it isnt a bracket.
	{
		if(s.equals(")") || s.equals("(") )
		{
			return true;
		}
		return false;
	}
	
	private static double Calculate(double number1, String operator, double number2) //Return a result of the the two numbers passed into the method. Determine what type of calculation from passed operator
	{		
		double result=0;
		
			switch ( operator ) // finding operators (+,-,*,/) and numbers
			{	
				case "+":
					result = number1 + number2;
					break;	
					
				case "-":
					result = number1 - number2;
					break;	
					
				case "*":
					result = number1 * number2;
					break;	
					
				case "/":
					result = number1 / number2;
					break;	
				
					default: 			
				
			} // end switch	finding operators (+,-,*,/)	and numbers
			
			return result;
	}	

	private static void result() //Convert "Postfix" to a final result.
	{		
		while ( equation.size()!=1 )
		{
			i=0; // start at the beginning of the array
			
			while ( !equation.get(i).equals("-") && !equation.get(i).equals("+") && !equation.get(i).equals("*") && !equation.get(i).equals("/")  ) // find closest operator (+,-,*,/)
			{
				i++;		
			}			
				
			double result = Calculate (Double.parseDouble(equation.get(i-2)), equation.get(i), Double.parseDouble(equation.get(i-1)) );	
			
			equation.remove(i); //remove operator
			equation.remove(i-1); // remove second number
			String numberAsString = Double.toString(result); // convert the calculation back to to String
			equation.set(i-2, numberAsString); // replace where the first number was with the result			

		} // end while
		
		if(Double.parseDouble(equation.get(0)) == Double.POSITIVE_INFINITY)
		{
			throw new divZero();
		}
		else if(Double.parseDouble(equation.get(0) ) % 1 == 0) 
		{
			gui.display.setText(String.valueOf(Math.round(Double.parseDouble(equation.get(0) ) ) ) );
		}
		else
		{
			gui.display.setText(String.valueOf((equation.get(0) ) ) );
		}
	}
}