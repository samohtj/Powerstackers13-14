package userinterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import matches.Match;
import teams.Team;

public class ScoutingApp extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ConsoleWindow console = new ConsoleWindow();
	
	/* 
	 * MENU BAR
	 */
	// Main menu bar
	JMenuBar menuBar = new JMenuBar();
	
	// Sub-menus
	JMenu menuFile = new JMenu("File");
	JMenu menuEdit = new JMenu("Edit");
	JMenu menuTeams = new JMenu("Teams");
	JMenu menuMatches = new JMenu("Matches");
	JMenu menuView = new JMenu("View");
	
	// Items for the TEAMS menu
	JMenuItem addTeamMenuItem = new JMenuItem("Add Team");
	JMenuItem removeTeamMenuItem = new JMenuItem("Remove Team");
	JMenuItem editTeamMenuItem = new JMenuItem("Edit Team");
	
	// Items for the VIEW menu
	JCheckBoxMenuItem showConsoleMenuItem = new JCheckBoxMenuItem("Show Console");
	
	/*
	 *PANELS 
	 */
	JPanel teamPanel = new JPanel(new GridLayout(1, 2));
	JPanel matchPanel = new JPanel(new GridLayout(1, 2));

	ShowTeamsListFrame teamsListPanel = new ShowTeamsListFrame();
	ShowMatchesListFrame matchesListPanel = new ShowMatchesListFrame();
	
	//ShowMatchFrame matchInfoPanel = new ShowMatchFrame(matchesListPanel.list.getMatchAt(matchesListPanel.list.getSelectedMatch()));
	
	public ScoutingApp(){
		// Add menu items to their respective menus
		
		// Create listeners for the menu items in the TEAMS menu
		addTeamMenuItem.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {teamsListPanel.showAddTeamDialog();}});
		removeTeamMenuItem.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) {teamsListPanel.deleteTeam();}});
		
	
		// Create a new private action listener for the Show Console option that shows the console window or hides it,
		// based on whether the checkbox is selected
		showConsoleMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ev) {
				if(showConsoleMenuItem.isSelected())
					console.setVisible(true);
				else
					console.setVisible(false);
			}
		});
		menuView.add(showConsoleMenuItem);
		
		// Add the menus to the menu bar
		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		menuBar.add(menuTeams);
		menuBar.add(menuMatches);
		menuBar.add(menuView);
		add(menuBar, BorderLayout.NORTH);
		
		// Add the main con
		teamPanel.add(teamsListPanel);
		matchPanel.add(matchesListPanel);
		
		add(teamPanel, BorderLayout.WEST);
		add(matchPanel, BorderLayout.EAST);
		//add(console.checkboxEnableConsole, BorderLayout.SOUTH);
		
		teamsListPanel.setConsoleWindow(console);
		
		setTitle("Scouting Application");
		setSize(1000, 600);
		setLocationRelativeTo(null);
		//setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
	}
	
	public void setConsoleVisible(boolean vis){
		console.setVisible(vis);
	}
	
	public static void main(String[] args) {		
		ScoutingApp frame = new ScoutingApp();
		frame.console.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		Team[] teamsArray = new Team[4];
		teamsArray[Match.RED_1] = new Team(5029, "Powerstackers");
		teamsArray[Match.RED_2] = new Team(4251, "Cougar Robotics");
		teamsArray[Match.BLUE_1] = new Team(5501, "USS Enterprise");
		teamsArray[Match.BLUE_2] = new Team(5035, "Some random team");
		
		Match match1 = new Match(teamsArray, Match.MATCHTYPE_QUALIFICATION, 1);
		match1.setRedScore(50);
		Match match2 = new Match(teamsArray, Match.MATCHTYPE_QUALIFICATION, 2);
		match2.setRedScore(60);
		match2.setBlueScore(67);
		
		
		frame.matchesListPanel.list.addMatch(match1);
		frame.matchesListPanel.list.addMatch(match2);
		
		for(int i = 0; i < teamsArray.length; i++){
			frame.teamsListPanel.getList().addTeam(teamsArray[i]);
		}
		
		
		
	}

}
