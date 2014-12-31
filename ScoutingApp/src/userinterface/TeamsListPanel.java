package userinterface;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import matches.Match;
import teams.TeamsList;
import teams.Team;

/**
 * A JPanel to visually display a list of FTC teams. The panel can also display buttons for 
 * adding/removing/editing teams. It can also display a small secondary panel with information
 * about the currently selected team.
 * 
 * @author Jonathan Thomas
 *
 */
public class TeamsListPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * The list is the TeamsList object that stores all the teams displayed in this panel.
	 */
	public TeamsList list = new TeamsList();
	
	// Create a JScrollPane and a TeamInfoPanel. These will be added directly into the panel.
	private JScrollPane scrollPane = new JScrollPane(list.table);
	private TeamInfoPanel teamInfoPanel = new TeamInfoPanel();
	
	//public ConsoleWindow cons = new ConsoleWindow();
	
	// Create buttons to add, delete, and edit teams
	private JButton teamAddBtn = new JButton("Add");
	private JButton teamDeleteBtn = new JButton("Delete");
	private JButton teamEditBtn = new JButton("Edit");
	
	/**
	 * Create a new TeamsListPanel to display a list of teams, their information, and options for
	 * editing the list.
	 */
	public TeamsListPanel(){
		this.setBorder(new TitledBorder("Teams"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		list.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(scrollPane);
		
		// Button Action Listeners
		teamAddBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showAddTeamDialog();
			}
		});
		
		teamDeleteBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(list.getSelectedIndex() != -1)
					deleteTeam();
				else
					list.cons.printConsoleLine("Cannot delete team; no row selected"); 
			}
		});
		
		
		JPanel btnsPanel = new JPanel();
		btnsPanel.setLayout(new BoxLayout(btnsPanel, BoxLayout.X_AXIS));
		//btnsPanel.setMaximumSize(new Dimension(300, 100));

		teamAddBtn.setPreferredSize(new Dimension(100,100));
		teamEditBtn.setPreferredSize(new Dimension(100,100));
		teamDeleteBtn.setPreferredSize(new Dimension(100,100));
		
		btnsPanel.add(teamAddBtn);
		btnsPanel.add(teamEditBtn);
		btnsPanel.add(teamDeleteBtn);
		
		// Buttons disabled, replaced with menu bar
		//add(btnsPanel);
		
		// Add the team info display panel, in invisible mode.
		list.table.addMouseListener(new ShowTeamInfoListener());
		teamInfoPanel.setVisible(false);
		add(teamInfoPanel);
		
	}
	
	/**
	 * A MouseAdapter class that updates the displayed team when their row is clicked.
	 * @author Jonathan Thomas
	 *
	 */
	private class ShowTeamInfoListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			teamInfoPanel.setVisible(true);
			teamInfoPanel.setTeam(list.getTeamAt(list.getSelectedIndex()));
		}
	}
	
	/**
	 * Set the console output window for the panel.
	 * 
	 * @param console
	 */
	public void setConsoleWindow(ConsoleWindow console){
		list.setConsoleWindow(console);
	}
	
	/**
	 * Show a dialog used to add teams to the list.
	 */
	public void showAddTeamDialog(){
		JTextField teamName = new JTextField(10);
		JTextField teamNumber = new JTextField(5);
		
		JPanel panel = new JPanel(new GridLayout(2, 2));
		panel.add(new JLabel("Team Name:"));
		panel.add(teamName);
		panel.add(new JLabel("Team Number:"));
		panel.add(teamNumber);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Enter Team Information", JOptionPane.OK_CANCEL_OPTION);
		
		if(result == JOptionPane.OK_OPTION)
			list.addTeam(new Team(Integer.parseInt(teamNumber.getText()), teamName.getText()));
	}
	
	/**
	 * Remove the currently selected team from the list. Selection is based 
	 * on the selected row of the JList. If no team is selected, the method does nothing.
	 */
	public void deleteTeam(){
		try{
			list.cons.printConsoleLine("Deleting team " + list.getTeamAt(list.getSelectedIndex()));
			list.removeTeam(list.getSelectedIndex());
		}catch(teams.TeamsList.IndexOutOfBoundsException e) {
			list.cons.printConsoleLine("ERROR: Index out of bounds. Index not selected or does not exist.");
		}
	}
	
	/**
	 * Edit the currently selected team's information. Selection is based on 
	 * the selected row of the JList. If no team is selected, the method does nothing.
	 * <p>THIS METHOD IS A WIP
	 */
	public void editTeam(){
		
	}
	
	/**
	 * Return the list of teams.
	 * 
	 * @return A TeamsList object containing the current active list of teams
	 */
	public TeamsList getList(){
		return list;
	}

	public static void main(String[] args) {
		
		Team[] teamsArray = new Team[4];
		teamsArray[Match.RED_1] = new Team(5029, "Powerstackers");
		teamsArray[Match.RED_2] = new Team(4251, "Cougar Robotics");
		teamsArray[Match.BLUE_1] = new Team(5501, "USS Enterprise");
		teamsArray[Match.BLUE_2] = new Team(5035, "Some random team");

		TeamsListPanel teamsFrame = new TeamsListPanel();
		
		for(int i = 0; i < teamsArray.length; i++){
			teamsFrame.list.addTeam(teamsArray[i]);
		}
		
		JFrame frame = new JFrame();
		frame.add(teamsFrame);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
