package userinterface;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import teams.Team;

public class ShowTeamFrame extends JPanel{
	JLabel titleLabel = new JLabel();
	JLabel averageScoreLabel = new JLabel();
	JLabel descriptionLabel = new JLabel();
	
	public ShowTeamFrame(Team team){
		titleLabel.setText("Team " + team.getTeamNumber() + ": " + team.getTeamName());
		add(titleLabel);
	}
	
	public ShowTeamFrame(){
		this(new Team());
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Team team = new Team(2013, "THIS YEAR");
		ShowTeamFrame teamFrame = new ShowTeamFrame(team);
		frame.add(teamFrame);
		frame.setSize(200, 200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}
