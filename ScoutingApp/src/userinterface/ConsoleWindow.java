package userinterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class ConsoleWindow extends JFrame{

	// Create a new text area. Initialize it with an opening message and the run date.
	private TextArea text = new TextArea("**********************************\n" + 
										 "* FTC SCOUTING TRACKER APPLICATION\n" + 
										 "* Developed by FTC Team # 5029\n" + 
										 "* powerstackersftc.weebly.com\n" + 
										 "**********************************\n" + 
										 new Date().toString());
	
	// Create a check box that can be used to enable and disable the console window
	public JCheckBox checkboxEnableConsole = new JCheckBox("Show console");
	
	/**
	 * Creates a JFrame that contains a console output.
	 */
	public ConsoleWindow(){
		
		checkboxEnableConsole.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkboxEnableConsole.isSelected())
					setVisible(true);
				else
					setVisible(false);				
			}
		});
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 400);
		setLocationRelativeTo(null);
		
		text.setEditable(false);
		text.setBackground(Color.WHITE);
		text.setFont(new Font("Courier", Font.PLAIN, 12));
		add(text);
	}
	
	public void printConsoleLine(String text){
		this.text.append("\n" + text);
	}
	
	public void setConsoleSize(int x, int y){
		setSize(x,y);
	}
	
	public static void main(String[] args) {
		ConsoleWindow console = new ConsoleWindow();
		console.setVisible(true);
		
		console.printConsoleLine("This is a console line");
		console.printConsoleLine("So is this!!!");

	}

}
