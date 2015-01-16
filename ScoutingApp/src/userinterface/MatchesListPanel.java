package userinterface;

import java.awt.BorderLayout;
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
 * Creates a JPanel containing a JScrollPane displaying a list of Matches. Also contains a
 * MatchesList to store those teams.
 * 
 * @author Jonathan Thomas
 *
 */
public class MatchesListPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The MatchesList object containing all the matches displayed in this panel.
	 */
	public MatchesList list = new MatchesList();
	
	/**
	 * The JScrollPane object containing the matches displayed in this panel.
	 */
	public JScrollPane scrollPane = new JScrollPane(list.table);
	
	// A small JPanel showing information about the selected match.
	private MatchInfoPanel matchDisplay = new MatchInfoPanel();
	
	/**
	 * Creates a new MatchesListPanel to display a list of matches. The panel can be added and
	 * manipulated like any Swing component.
	 * <p>The list argument is a MatchesList object. Passing this argument will populate the list
	 * with matches to display.
	 * 
	 * @param list
	 */
	public MatchesListPanel(MatchesList list){
		// Set this object's internal list to the list passed to the constructor
		this.list = list;
		// Fill the scrollpane with the items from the list
		scrollPane = new JScrollPane(this.list.table);
		// Set the list so that only one line can be selected at a time
		this.list.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Add a border to the scrollpane with the title "matches"
		scrollPane.setBorder(new TitledBorder("Matches"));
		// Add the scrollpane to the panel
		add(scrollPane, BorderLayout.CENTER);
		
		// Make the match info pane invisible, and add it to the panel
		matchDisplay.setVisible(false);
		add(matchDisplay, BorderLayout.SOUTH);
		
		// Try to set the match to be displayed in the match info panel. Display the 
		// currently selected match. If no match is selected, don't display anything.
		try{
			matchDisplay.setMatch(list.getMatchAt(0));
		}catch(IndexOutOfBoundsException e){
			matchDisplay.setMatch(new Match());
		}
		
		// Add a mouse listener to the scrollpane, to wait for a mouse click event
		list.table.addMouseListener(new ShowMatchInfoListener());
		
		// Set the preferred size of the panel
		setPreferredSize(new Dimension(400, 600));
	}
	
	/**
	 * Creates a new MatchesListPanel to display a list of matches. The panel can be added and
	 * manipulated like any Swing component.
	 */
	public MatchesListPanel(){
		this(new MatchesList());
	}
	
	/**
	 * An experimental ActionListener class. When activated, it is meant to update the matchDisplay
	 * panel with information about the currently selected match. Selection is based on the selected
	 * row of the scrollPane.
	 * 
	 * @author Jonathan Thomas
	 *
	 */
	private class ShowMatchInfoListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			JTable table = (JTable) e.getSource();
			matchDisplay.setVisible(true);
			matchDisplay.setMatch(list.getMatchAt(table.getSelectedRow()));
			matchDisplay.revalidate();
			//System.out.println("Current match: " + matchDisplay.getMatch().toString());
			//System.out.println("showing match " + table.getSelectedRow());
		}
	}
	
	public static void main(String[] args) {
		Team[] teamsArray = {new Team(5029, "Powerstackers"), new Team(4251, "Cougar Robotics"), 
				new Team(5501, "USS Enterprise"), new Team(5035, "Some random team")};

		Match[] matchesArray = {new Match(teamsArray, Match.MATCHTYPE_QUALIFICATION, 1), 
				new Match(teamsArray, Match.MATCHTYPE_QUALIFICATION, 2)};
		
		matchesArray[0].setRedScore(50);
		
		matchesArray[1].setRedScore(60);
		matchesArray[1].setBlueScore(67);
		
		MatchesListPanel frame = new MatchesListPanel(new MatchesList(matchesArray));

		
		JFrame framee = new JFrame();
		framee.add(frame);
		//framee.setSize(500, 500);
		framee.setLocationRelativeTo(null);
		framee.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framee.setVisible(true);
		framee.setExtendedState(framee.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	public void showAddMatchDialog() {
		// TODO Auto-generated method stub
		
	}
}
