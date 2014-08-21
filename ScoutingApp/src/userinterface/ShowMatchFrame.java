package userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import matches.Match;
import teams.Team;

/**
 * JPanel that shows match information, including alliance scores and a list of participating teams.
 * @author Jonathan
 *
 */
public class ShowMatchFrame extends JPanel{
	
	// Labels to store the names and numbers of the teams in the match
	JLabel red1Name = new JLabel();
	JLabel red2Name = new JLabel();
	JLabel blue1Name = new JLabel();
	JLabel blue2Name = new JLabel();
	
	// Labels to store the points, penalties,and final scores of the match
	JLabel redScoreLbl = new JLabel();
	JLabel redPenaltiesLbl = new JLabel();
	JLabel redFinalLbl = new JLabel();
	JLabel blueScoreLbl = new JLabel();
	JLabel bluePenaltiesLbl = new JLabel();
	JLabel blueFinalLbl = new JLabel();
	
	/**
	 * Constructor that fills all the labels and sets up the display window. Shows four teams, scores, and penalties
	 * @param match
	 */
	public ShowMatchFrame(Match match){
		// Get the names of the teams from the match
		red1Name.setText(match.getTeam(Match.RED_1).toString());
		red2Name.setText(match.getTeam(Match.RED_2).toString());
		blue1Name.setText(match.getTeam(Match.BLUE_1).toString());
		blue2Name.setText(match.getTeam(Match.BLUE_2).toString());
		
		// Create labels to tell the user which team is in which alliance
		JLabel red1Lbl = new JLabel("Red #1");
		JLabel red2Lbl = new JLabel("Red #2");
		JLabel blue1Lbl = new JLabel("Blue #1");
		JLabel blue2Lbl = new JLabel("Blue #2");
		red1Lbl.setForeground(Color.RED);
		red2Lbl.setForeground(Color.RED);
		blue1Lbl.setForeground(Color.BLUE);
		blue2Lbl.setForeground(Color.BLUE);
		
		// Create a panel to hold all the team names
		JPanel teamsPanel = new JPanel(new GridLayout(4, 2, 4, 4));
		
		teamsPanel.add(red1Lbl);
		teamsPanel.add(blue1Lbl);
		teamsPanel.add(red1Name);
		teamsPanel.add(blue1Name);
		teamsPanel.add(red2Lbl);
		teamsPanel.add(blue2Lbl);
		teamsPanel.add(red2Name);
		teamsPanel.add(blue2Name);
		
		// Add a border to the panel
		TitledBorder teamsBorder = new TitledBorder("Teams");
		teamsPanel.setBorder(teamsBorder);
		
		// Add the panel to the frame
		add(teamsPanel, BorderLayout.NORTH);
		
		// Get the score numbers from the match
		redScoreLbl.setText(Integer.toString(match.getRedScore()));
		redPenaltiesLbl.setText(Integer.toString(match.getRedPenalties()));
		redFinalLbl.setText(Integer.toString(match.getRedFinalScore()));
		blueScoreLbl.setText(Integer.toString(match.getBlueScore()));
		bluePenaltiesLbl.setText(Integer.toString(match.getBluePenalties()));
		blueFinalLbl.setText(Integer.toString(match.getBlueFinalScore()));
		
		// Set the font to Sans, Bold, 20 point
		redScoreLbl.setFont(new Font("Sans", Font.BOLD, 20));
		redPenaltiesLbl.setFont(new Font("Sans", Font.BOLD, 20));
		redFinalLbl.setFont(new Font("Sans", Font.BOLD, 20));
		blueScoreLbl.setFont(new Font("Sans", Font.BOLD, 20));
		bluePenaltiesLbl.setFont(new Font("Sans", Font.BOLD, 20));
		blueFinalLbl.setFont(new Font("Sans", Font.BOLD, 20));
		
		// Change the text color to white
		redScoreLbl.setForeground(Color.WHITE);
		redPenaltiesLbl.setForeground(Color.WHITE);
		redFinalLbl.setForeground(Color.WHITE);
		blueScoreLbl.setForeground(Color.WHITE);
		bluePenaltiesLbl.setForeground(Color.WHITE);
		blueFinalLbl.setForeground(Color.WHITE);
		
		// Center  align the text
		redScoreLbl.setHorizontalAlignment(SwingConstants.CENTER);
		redPenaltiesLbl.setHorizontalAlignment(SwingConstants.CENTER);
		redFinalLbl.setHorizontalAlignment(SwingConstants.CENTER);
		blueScoreLbl.setHorizontalAlignment(SwingConstants.CENTER);
		bluePenaltiesLbl.setHorizontalAlignment(SwingConstants.CENTER);
		blueFinalLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Set the background color to the alliance colors, and make the background visible
		redScoreLbl.setBackground(Color.RED);
		redPenaltiesLbl.setBackground(Color.RED);
		redFinalLbl.setBackground(Color.RED);
		blueScoreLbl.setBackground(Color.BLUE);
		bluePenaltiesLbl.setBackground(Color.BLUE);
		blueFinalLbl.setBackground(Color.BLUE);
		redScoreLbl.setOpaque(true);
		redPenaltiesLbl.setOpaque(true);
		redFinalLbl.setOpaque(true);
		blueScoreLbl.setOpaque(true);
		bluePenaltiesLbl.setOpaque(true);
		blueFinalLbl.setOpaque(true);
		
		// Create labels to show the categories
		JLabel pointsLbl = new JLabel("Points:");
		JLabel penaltiesLbl = new JLabel("Penalties:");
		JLabel scoresLbl = new JLabel("Score:");
		
		// Create a panel to hold the score numbers
		JPanel scoresPanel = new JPanel(new GridLayout(3, 3, 4, 4));
		
		scoresPanel.add(pointsLbl);
		scoresPanel.add(redScoreLbl);
		scoresPanel.add(blueScoreLbl);
		scoresPanel.add(penaltiesLbl);
		scoresPanel.add(redPenaltiesLbl);
		scoresPanel.add(bluePenaltiesLbl);
		scoresPanel.add(scoresLbl);
		scoresPanel.add(redFinalLbl);
		scoresPanel.add(blueFinalLbl);
		
		// Add a border to the panel
		TitledBorder scoresBorder = new TitledBorder("Scores");
		scoresPanel.setBorder(scoresBorder);
		
		// Add the panel to the frame
		add(scoresPanel, BorderLayout.CENTER);
		
		// Set the title to the name of the match
		//setTitle(match.getMatchTitle());
		
		// Show the window
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setLocationRelativeTo(null);
		//setSize(350, 250);
		//setVisible(true);
	}

	public static void main(String[] args) {
		Team[] teamsArray = new Team[4];
		teamsArray[Match.RED_1] = new Team(5029, "Powerstackers");
		teamsArray[Match.RED_2] = new Team(4251, "Cougar Robotics");
		teamsArray[Match.BLUE_1] = new Team(5501, "USS Enterprise");
		teamsArray[Match.BLUE_2] = new Team(5035, "Some random team");
		
		
		
		Match match = new Match(teamsArray, Match.MATCHTYPE_QUALIFICATION, 45);
		match.setRedScore(50);
		ShowMatchFrame panel = new ShowMatchFrame(match);
		
		JFrame frame = new JFrame();
		frame.add(panel);
		frame.setSize(350, 250);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		

	}

}
