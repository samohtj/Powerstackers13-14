package userinterface;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import teams.Team;

/**
 * A JPanel displaying information about a certain team. Team Name and Number are displayed.
 * 
 * @author Jonathan Thomas
 *
 */
public class TeamInfoPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel titleLabel = new JLabel();
	private JLabel averageScoreLabel = new JLabel();
	private JLabel descriptionLabel = new JLabel();
	
	/**
	 * Create a JPanel displaying information about a certain team.
	 * 
	 * @param team
	 */
	public TeamInfoPanel(Team team){
		setTeam(team);
	}
	
	/**
	 * Create a JPanel displaying information about a certain team.
	 */
	public TeamInfoPanel(){
		this(new Team());
	}
	
	/**
	 * Set the information displayed in the panel to that of a new Team.
	 * 
	 * @param team The team you want the panel to display.
	 */
	public void setTeam(Team team){
		titleLabel.setText("Team " + team.getTeamNumber() + ": " + team.getTeamName());
		averageScoreLabel.setText("Average Score: " + team.getAverageScore());
		descriptionLabel.setText("Description: " + team.getDescription());
		add(titleLabel);
		add(averageScoreLabel);
		add(descriptionLabel);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Team team = new Team(2013, "THIS YEAR");
		TeamInfoPanel teamFrame = new TeamInfoPanel(team);
		frame.add(teamFrame);
		frame.setSize(200, 200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}
