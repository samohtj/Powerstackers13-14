package userinterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

/**
 * This class is a JFrame that displays console output from the application.
 * 
 * @author Jonathan Thomas
 */
public class ConsoleWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    
    
    
	
	// Create a new text area. Initialize it with an opening message and the run date.
	private TextArea text = new TextArea("SCOUTER  Copyright (C) 2015 Powerstackers\n" + 
										 "This program comes with ABSOLUTELY NO WARRANTY;\n" + 
										 "This is free software, and you are welcome to redistribute it\n" + 
										 "under certain conditions; see LICENSE.txt for details.\n" + 
										 "powerstackersftc.com\n"+
										 new Date().toString()+"\n");
	
	// Create a check box that can be used to enable and disable the console window
	public JCheckBox checkboxEnableConsole = new JCheckBox("Show console");
	
	/**
	 * Creates a JFrame that contains a console output.
	 */
	public ConsoleWindow(){
		
		checkboxEnableConsole.addActionListener(new ShowConsoleCheckboxActionListener());
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 400);
		setLocationRelativeTo(null);
		
		text.setEditable(false);
		text.setBackground(Color.WHITE);
		text.setFont(new Font("Consolas", Font.PLAIN, 12));
		add(text);
	}
	
	/**
	 * Print a line of text to the console.
	 * 
	 * @param text The text you want to print.
	 */
	public void printConsoleLine(String text){
		this.text.append("\n" + text);
	}
	
	/**
	 * Set the size of the console window.
	 * 
	 * @param x
	 * @param y
	 */
	public void setConsoleSize(int x, int y){
		setSize(x,y);
	}
	
	/**
	 * This class is an ActionListener class that, when activated, shows or hides the console window,
	 * depending on whether the checkboxEnableConsole is selected or not.
	 * 
	 * @author Jonathan Thomas
	 *
	 */
	public class ShowConsoleCheckboxActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(checkboxEnableConsole.isSelected())
				setVisible(true);
			else
				setVisible(false);				
		}
	}
	
	public static void main(String[] args) {
		ConsoleWindow console = new ConsoleWindow();
		console.setVisible(true);
		
		console.printConsoleLine("This is a console line");
		console.printConsoleLine("So is this!!!");

	}
}
