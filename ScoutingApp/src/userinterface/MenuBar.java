package userinterface;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Sub-menus
	private JMenu menuFile = new JMenu("File");
	private JMenu menuEdit = new JMenu("Edit");
	private JMenu menuTeams = new JMenu("Teams");
	private JMenu menuMatches = new JMenu("Matches");
	private JMenu menuView = new JMenu("View");

	// Items for the TEAMS menu
	/**
	 * Found under <b>TEAMS-></b>
	 */
	public JMenuItem addTeamMenuItem = new JMenuItem("Add Team");
	
	/**
	 * Found under <b>TEAMS-></b> 
	 */
	public JMenuItem removeTeamMenuItem = new JMenuItem("Remove Team");
	
	/**
	 * Found under <b>TEAMS-></b>
	 */
	public JMenuItem editTeamMenuItem = new JMenuItem("Edit Team");
	
	/*
	 * 
	 * MATCHES MENU
	 * 
	 */

	public JMenuItem addMatchMenuItem = new JMenuItem("Add Match");
	
	/*
	 * 
	 * VIEW MENU
	 * 
	 */
	/**
	 * Found under <b>VIEW-></b>
	 */
	public JCheckBoxMenuItem showConsoleMenuItem = new JCheckBoxMenuItem("Show Console");

	/**
	 * Creates a full featured menu bar. Options include <b>FILE, EDIT, TEAMS, MATCHES, VIEW</b>.
	 * Action listeners for each option are added in the object that uses the menu. Meaning, the
	 * class that creates the MenuBar object also has to assign the action listeners.
	 */
	public MenuBar(){
		
		// Add team menu stuff to the menu bar
		menuTeams.add(addTeamMenuItem);
		menuTeams.add(removeTeamMenuItem);
		menuTeams.add(editTeamMenuItem);
		
		menuMatches.add(addMatchMenuItem);

		menuView.add(showConsoleMenuItem);
		
		// Add the menus to the menu bar
		add(menuFile);
		add(menuEdit);
		add(menuTeams);
		add(menuMatches);
		add(menuView);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
