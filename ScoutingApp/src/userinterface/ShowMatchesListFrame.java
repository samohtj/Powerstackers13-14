package userinterface;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import teams.Team;
import matches.Match;
import matches.MatchesList;

/**
 * Creates a list of standard FIRST Tech Challenge matches. 
 * @author Jonathan Thomas
 *
 */
public class ShowMatchesListFrame extends JPanel{
	
	// I had to add this, or Eclipse would be mad at me
	private static final long serialVersionUID = 1L;
	
	public MatchesList list = new MatchesList();
	public JScrollPane scrollPane = new JScrollPane(list.table);
	private ShowMatchFrame matchDisplay = new ShowMatchFrame();
	
	public ShowMatchesListFrame(MatchesList list){
		this.list = list;
		scrollPane = new JScrollPane(this.list.table);
		this.list.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setBorder(new TitledBorder("Matches"));
		add(scrollPane);
		
		
		add(matchDisplay);
		
		list.table.addMouseListener(new ShowMatchInfoListener());
		
		setPreferredSize(new Dimension(400, 600));
	}
	
	private class ShowMatchInfoListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			JTable table = (JTable) e.getSource();
			System.out.println("Clicked row" + table.getSelectedRow());
			matchDisplay = new ShowMatchFrame(list.getMatchAt(table.getSelectedColumn()));
		}
	}
	
	public ShowMatchesListFrame(){
		this(new MatchesList());
	}

	public static void main(String[] args) {
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
		
		ShowMatchesListFrame frame = new ShowMatchesListFrame();
		frame.list.addMatch(match1);
		frame.list.addMatch(match2);
		
		JFrame framee = new JFrame();
		framee.add(frame);
		//framee.setSize(500, 500);
		framee.setLocationRelativeTo(null);
		framee.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framee.setVisible(true);
		framee.setExtendedState(framee.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		

	}

}
