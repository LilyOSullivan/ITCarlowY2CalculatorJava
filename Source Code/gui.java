package calculator;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class gui
{
	protected static JTextField display = new JTextField("");
	protected static String memoryStore; 
	protected static JTextField output = new JTextField("");

	public gui()
	{
		JFrame CalcFrame = new JFrame("Calculator");
		CalcFrame.setLayout(new BorderLayout() );
        CalcFrame.setSize(450,450);      
        CalcFrame.setLocation(500, 500);
        CalcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalcFrame.setResizable(false);
        
        output.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 22));
        display.setMargin(new Insets(11,11,11,11)); // Padding within Input Textbox
        output.setMargin(new Insets(2,2,2,2));
        
        display.addKeyListener(new KeyAdapter() // Add a KeyListener to allow certain typing in the input textbox
        {
        	boolean consume=false;
            public void keyPressed(KeyEvent e)
            {
                if (!Algo.isNum(Character.toString((e.getKeyChar() ) ) )  
                        && !Algo.isOper(Character.toString((e.getKeyChar() ) ) ) 
                        && !Algo.isBrac(Character.toString((e.getKeyChar() ) ) ) 
                        && !(e.getKeyCode() == KeyEvent.VK_RIGHT) 
                        && !(e.getKeyCode() == KeyEvent.VK_LEFT) 
                        && !(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                        && !(e.getKeyCode() == KeyEvent.VK_PERIOD) )
                {
                    e.consume();
                    consume = true;
                }
                else
                {
                	consume = false;
                }
            }
            public void keyTyped(KeyEvent e) // Fix in Swing to properly consume input. AWT dosent need implementation of KeyTyped or KeyReleased
            {
            	if(consume)
            	{
            		e.consume();
            	}
            }
            public void keyReleased(KeyEvent ke)
            {
            	if(consume)
            	{
            		ke.consume();
            	}
            }
        });
        
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() // Add Global Listener, used for keypresses. Allows user to execute enter key/escape key functions without having focus on a specific component 
        {
            public void eventDispatched(AWTEvent e)
            {
                KeyEvent ke = (KeyEvent) e;
                if(ke.getID() == KeyEvent.KEY_PRESSED)
                {
                    if(ke.getKeyCode() == KeyEvent.VK_ENTER) 
                    {
                         preTest();
                    }
                    else if(ke.getKeyCode() == KeyEvent.VK_ESCAPE)
                    {
                    	clearscreens();
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
        
        JPanel lcdDisplay = new JPanel(); 
        lcdDisplay.setLayout(new BorderLayout());
        lcdDisplay.add(display,BorderLayout.CENTER);      

        lcdDisplay.add(output,BorderLayout.PAGE_END);
        CalcFrame.add(lcdDisplay,BorderLayout.PAGE_START);  
        JPanel buttonPanel = new JPanel();  
        buttonPanel.setLayout(new GridLayout(5, 5, -1, -1));	// (rows, columns, vertical and horizontal space) 
        CalcFrame.add(buttonPanel,BorderLayout.CENTER);
        
        Color borderColor = new Color (238,238,238);
        Color topRowButtons = new Color (237,211,220);
        Color operatorColor = new Color (178,225,242);
        Color bracketColor = new Color (219,222,177);

        // Using anonymous inner classes to avoid creating a variable/reference to each button.
        buttonPanel.add(new JButton("MC") {{ addActionListener(e -> memoryStore = ""); // Binds E to anything after the arrow operator ( -> ) , similar to pointers in C++
											 this.setBackground(topRowButtons);		   // "this" keyword needed to be used due to the anonymous inner class 
											 this.setFont(new Font("Calibri", Font.PLAIN,18)); 
											 this.setBorder(new LineBorder(borderColor)); }} );

		buttonPanel.add(new JButton("MR") {{ addActionListener(e -> display.setText(display.getText() + memoryStore ));
											 this.setBackground(topRowButtons); 
											 this.setFont(new Font("Calibri", Font.PLAIN,18)); 
											 this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("MS") {{ addActionListener(e -> memoryStore = display.getText() ); 
											 this.setBackground(topRowButtons); 
											 this.setFont(new Font("Calibri", Font.PLAIN,18)); 
											 this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("\u2190") {{ addActionListener(e -> backspace());
											 this.setBackground(topRowButtons);
											 this.setFont(new Font("Calibri", Font.PLAIN,18)); 
											 this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("C") {{ addActionListener(e -> clearscreens() );
											this.setBackground(topRowButtons);
											this.setFont(new Font("Calibri", Font.PLAIN,18)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("7") {{ addActionListener(e -> display.setText(display.getText() + "7" ));
											this.setBackground(Color.white);
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("8") {{ addActionListener(e -> display.setText(display.getText() + "8" ));
											this.setBackground(Color.white);
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("9") {{ addActionListener(e -> display.setText(display.getText() + "9" )); 
											this.setBackground(Color.white);
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		
		buttonPanel.add(new JButton("(") {{ addActionListener(e -> display.setText(display.getText() + "(" )); 
											this.setBackground(bracketColor);
											this.setFont(new Font("Calibri", Font.PLAIN,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton(")") {{ addActionListener(e -> display.setText(display.getText() + ")" )); 
											this.setBackground(bracketColor);
											this.setFont(new Font("Calibri", Font.PLAIN,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("4") {{ addActionListener(e -> display.setText(display.getText() + "4" )); 
											this.setBackground(Color.white);
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("5") {{ addActionListener(e -> display.setText(display.getText() + "5" ));
											this.setBackground(Color.white);
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("6") {{ addActionListener(e -> display.setText(display.getText() + "6" ));
											this.setBackground(Color.white); 
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("+") {{ addActionListener(e -> Sign ("+") ); 
											this.setBackground(operatorColor);
											this.setFont(new Font("Calibri", Font.PLAIN,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("\u0336") {{ addActionListener(e -> Sign ("-") ); 
											this.setBackground(operatorColor);
											this.setFont(new Font("Calibri", Font.PLAIN,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("1") {{ addActionListener(e -> display.setText(display.getText() + "1" ));
											this.setBackground(Color.white); 
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("2") {{ addActionListener(e -> display.setText(display.getText() + "2" ));
											this.setBackground(Color.white);
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("3") {{ addActionListener(e -> display.setText(display.getText() + "3" )); 
											this.setBackground(Color.white);
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("x") {{ addActionListener(e -> Sign ("*") ); 
											this.setBackground(operatorColor);
											this.setFont(new Font("Calibri", Font.PLAIN,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("\u00F7") {{ addActionListener(e -> Sign ("/") ); 
											this.setBackground(operatorColor);
											this.setFont(new Font("Calibri", Font.PLAIN,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JLabel()); // Used to Create bottom left blank space
		
		buttonPanel.add(new JButton("0") {{ addActionListener(e -> display.setText(display.getText() + "0" ));
											this.setBackground(Color.white);
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton(".") {{ addActionListener(e -> display.setText(display.getText() + "." ));
											this.setBackground(Color.white); 
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );
		
		buttonPanel.add(new JButton("=") {{ addActionListener(e -> preTest()); 
											this.setBackground(new Color (245,202,50)); 
											this.setFont(new Font("Calibri", Font.BOLD,22)); 
											this.setBorder(new LineBorder(borderColor)); }} );	
		buttonPanel.setBackground(borderColor);
		CalcFrame.setVisible(true);
	} // end GUI Constructor
	
	public void preTest() // Called when user requests a result of their expression. It performs basic validations before passing to the "Reverse Polish Notation algorithm"
	{	
		// ------------- count number of operators ---------------------------
		long countx = display.getText().chars().filter(ch -> ch == '-').count();  
		 countx = countx + display.getText().chars().filter(ch -> ch == '+').count();
		 countx = countx + display.getText().chars().filter(ch -> ch == '*').count();
		 countx = countx + display.getText().chars().filter(ch -> ch == '/').count();	
		// ------------- count number of operators ---------------------------
		 ArrayList<String> checkDecimal = new ArrayList<String>(Arrays.asList(display.getText().split("") ) ); // Convert Infix to ArrayList
		 
		 int pos = 0;
		 while ( checkDecimal.size()!=pos ) //check for full stops
		 {			 
			 if (checkDecimal.get(pos).equals(".") )
			 {			 
				 if (pos == 0) // is decimal point at beginning
				 {
					 checkDecimal.set(pos,"0.");
				 }
				 
				 else if (!Algo.isNum( checkDecimal.get(pos-1) ) )					 
				 {
					 checkDecimal.set(pos,"0.");
				 }			
			 }
			 		 
			 pos++;		 
			 
		 } //end while 
 
		 display.setText( String.join("", checkDecimal) );
	 
		 //-------------------------- original pretest ------------------------
		 if ( display.getText().equals("") )
		 {
			 output.setText("Nothing to calculate.");;
		 }
		

		else if ( Algo.isOper( display.getText().substring(display.getText().length() - 1) )  )
		{
			if (countx==1)
			{
				countx=0; // resets the operator counter
			}
			else 
			{								
				countx=0; // resets the operator counter
				Algo.convert( display.getText().substring(0, display.getText().length() - 1) );	// if there is an operator at the end it will be ignored
			}
		}
		 
		else if ( display.getText().substring(0,1).equals("-") && countx<=2 ) 
		{
			 Algo.convert(display.getText() );
		}
		 
		else 
		{	
			countx=0;	// resets the operator counter
			Algo.convert(display.getText() );
		}
	} // end preTest
	
	private void Sign (String operator) // Prevents multiple operators after each other when using buttons 
	{
		if((display.getText().length()) != 0 ) // if the string is not empty	
		{
			if( Algo.isOper( display.getText().substring(display.getText().length() - 1) ) ) // check the last operator
			{
				if (display.getText().length() != 1)
				{
					display.setText( display.getText().substring(0, display.getText().length()-1 ) + operator ); // replace last operator with new one		
				}
			}	
			else
			{
				display.setText(display.getText() + operator );
			}					
		} // end if the string is not empty	
		
		else  // string is empty but you can use - sign
		{
			if( operator.equals("-") )
			{
				display.setText(display.getText() + operator );
			}	
		}	
	} // Sign method
	
	private void backspace() // Deletes a character from input
	{
		try 
		{
			display.setText(display.getText().substring(0, display.getText().length()-1  ) );
		}
		catch(StringIndexOutOfBoundsException e)
		{
			output.setText("Nothnig to delete");
		}
	}
	
	private void clearscreens() // Clears both screens
	{
		display.setText("");
        output.setText("");
	}
} // end gui class