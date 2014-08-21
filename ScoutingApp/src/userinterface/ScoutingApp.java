package userinterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import matches.Match;
import teams.Team;

public class ScoutingApp extends JFrame{
	
	JPanel teamPanel = new JPanel(new GridLayout(1, 2));
	JPanel matchPanel = new JPanel(new GridLayout(1, 2));

	ShowTeamsListFrame teamsListPanel = new ShowTeamsListFrame();
	ShowMatchesListFrame matchesListPanel = new ShowMatchesListFrame();
	
	//ShowMatchFrame matchInfoPanel = new ShowMatchFrame(matchesListPanel.list.getMatchAt(matchesListPanel.list.getSelectedMatch()));
	
	public ScoutingApp(){
		teamPanel.add(teamsListPanel);
		
		matchPanel.add(matchesListPanel);
		
		add(teamPanel, BorderLayout.WEST);
		add(matchPanel, BorderLayout.EAST);
		
		setTitle("Scouting Application");
		setSize(1000, 600);
		setLocationRelativeTo(null);
		//setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ScoutingApp frame = new ScoutingApp();
		
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
