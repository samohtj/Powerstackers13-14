package teams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import userinterface.ConsoleWindow;

/**
 * JTable that stores a list of FTC teams. Can load a list of teams from a text file.
 * @author Jonathan
 *
 */
public class TeamsList {
	// ArrayList to store all the teams
	private ArrayList<Team> teams = new ArrayList<Team>();
	
	// A JTable to visually display the list of teams
	public JTable table = new JTable();

	public static String teamsListFilePath = "data/teams/";
	public static String teamsListFileExt = "";
	
	public ConsoleWindow cons = new ConsoleWindow();
	
	/**
	 * Empty constructor.
	 */
	public TeamsList(){
		// Update the table once. It will be empty
		refreshTeamsTable();		
	}
	
	/**
	 * Creates a list of teams from an array.
	 * @param teams
	 */
	public TeamsList(Team[] teams){
		// Add the teams into the list
		for(int i = 0; i < teams.length; i++){
			addTeam(teams[i]);
		}
	}
	
	/**
	 * Set the console window for console output. Takes a console window object as input.
	 * @param window
	 */
	public void setConsoleWindow(ConsoleWindow window){
		cons = window;
	}
	
	/**
	 * Loads a list of teams from a text file.
	 */
	public void loadFromFile(String eventFileName) throws FileNotFoundException{
		// Create a File object
		File file = new File(teamsListFilePath + eventFileName);
		if(!file.exists())
			throw new FileNotFoundException();
		
		// Integer to store the total number of teams listed in the match file
		int totalFiles = 0;
		
		// Create a Scanner object to read the file
		@SuppressWarnings("resource")
		Scanner in = new Scanner(file);
		
		// Read the file, and load a team file for each team listed in the match file
		if(in.hasNext()){
			cons.printConsoleLine("Teams found in " + eventFileName + ". Loading files:");
			while(in.hasNext()){
				// Create a String object to store the name of the team
				String teamName = in.nextLine();

				// If the file exists, load from it
				// Otherwise, print an error message to the console
				if(new File(Team.teamsDataPath + teamName + Team.teamsFileExt).exists()){
					teams.add(new Team(new File(Team.teamsDataPath + teamName + Team.teamsFileExt)));
					cons.printConsoleLine("\t" + teamName + " loaded successfully");
				}else
					cons.printConsoleLine("\tERROR: File \"" + teamName + "\" not found");
				totalFiles++;
			}
		}else
			// If the file is empty, print a message to the console
			cons.printConsoleLine("No teams found in " + eventFileName);
		
		// Print a message to the console to inform the user of what went on
		cons.printConsoleLine(((teams.size() == totalFiles)? "[+]" : "[!]") + " Loaded " +
				teams.size() + "/" + totalFiles + " succesfully.");
	}
	
	/**
	 * Updates the visual JTable with the teams currently in the list.
	 */
	private void refreshTeamsTable(){
		// Create arrays to hold the column headers and the team's information
		String[] columnHeaders = {"Team #", "Team Name", "RP", "QP"};
		Object[][] teamsList = new Object[teams.size()][4];
		
		// Fill each row with the team's information
		for(int i = 0; i < teams.size(); i++){
			String[] teamData = {Integer.toString(teams.get(i).getTeamNumber()), teams.get(i).getTeamName(), 
					Integer.toString(teams.get(i).getRP()), Integer.toString(teams.get(i).getQP())};
			teamsList[i] = teamData;
		}
		
		// Create a new model with the information, and add it to the table
		TeamsListModel model = new TeamsListModel(teamsList, columnHeaders);
		table.setModel(model);
		
		// Set the width of the columns to their preferred widths
		table.getColumnModel().getColumn(0).setMinWidth(25);
		table.getColumnModel().getColumn(0).setMaxWidth(200);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(2).setMinWidth(25);
		table.getColumnModel().getColumn(2).setMaxWidth(125);
		table.getColumnModel().getColumn(2).setPreferredWidth(25);
		table.getColumnModel().getColumn(3).setMinWidth(25);
		table.getColumnModel().getColumn(3).setMaxWidth(125);
		table.getColumnModel().getColumn(3).setPreferredWidth(25);			
	}
	
	/**
	 * Add a team to the list.
	 * @param team
	 */
	public void addTeam(Team team){
		teams.add(team);
		cons.printConsoleLine("Added team: " + team.toString());
		refreshTeamsTable();
	}
	
	/**
	 * Remove the team at the specified index. Throws exception if index does not exist.
	 * @throws IndexOutOfBoundsException
	 * @param index
	 */
	public void removeTeam(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index > teams.size() - 1)
			throw new IndexOutOfBoundsException();
		cons.printConsoleLine("Team removed: " + teams.get(index).toString());
		teams.remove(index);
		refreshTeamsTable();
	}
	
	/**
	 * Return the list of teams itself.
	 * @return
	 */
	public ArrayList<Team> getList(){
		return teams;
	}
	
	/**
	 * Get the selected index of the table, return -1 if no row is selected
	 * @return
	 */
	public int getSelectedIndex(){
		return table.getSelectedRow();
	}
	
	/**
	 * Returns the team at the index
	 * @param index
	 * @return
	 */
	public Team getTeam(int index){
		return teams.get(index);
	}
	
	/**
	 * Returns a formatted string with a list of teams.
	 * @return
	 */
	public String getTeamsListString(){
		StringBuilder builder = new StringBuilder();
		builder.append("List contains " + teams.size() + ((teams.size() == 1)? " team:\n" : " teams:\n"));
		for(int i = 0; i < teams.size(); i++)
			builder.append("\t" + teams.get(i).toString() + "\n");
		return builder.toString();
	}
	
	/**
	 * 
	 * @author Jonathan
	 *
	 */
	public class IndexOutOfBoundsException extends Exception{
		
	}
	
	/**
	 * A custom ListModel to create a new JTable from the information in the teams list.
	 * @author Jonathan Thomas
	 *
	 */
	private class TeamsListModel extends AbstractTableModel{

		// A list of Object arrays that will store the team name and number in String form.
		ArrayList<Object[]> list;
		
		// An array of Strings to store the column headers that will be displayed on the JScrollPane.
		String[] columnHeaders;
		
		/**
		 * Constructor that takes a list of team name and number Strings, and a list of column title Strings.
		 * @param list
		 * @param columnHeaders
		 */
		public TeamsListModel(Object[][] list, String[] columnHeaders){
			this.columnHeaders = columnHeaders;
			this.list = new ArrayList<Object[]>();
			for(int i = 0; i < list.length; i++){
				this.list.add(list[i]);
			}
		}
		
		/**
		 * Return the number of columns.
		 */
		@Override
		public int getColumnCount() {
			return columnHeaders.length;
		}

		/**
		 * Return the number of rows.
		 */
		@Override
		public int getRowCount() {
			return list.size();
		}

		/**
		 * Return the team name and number stored at said position in the JTable
		 * @param rowIndex, columnIndex
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return list.get(rowIndex)[columnIndex];
		}
		
		/**
		 * Return the column header at said index.
		 * @param index
		 */
		public String getColumnName(int index){
			return columnHeaders[index];
		}

	}
	
	public void saveAllTeams(){
		for(int i = 0; i < teams.size(); i++)
			teams.get(i).saveTeamInfo();
	}
	
	public static void main(String args[]){
		TeamsList list = new TeamsList();
		//list.cons.setVisible(true);
		try {
			list.loadFromFile("LIST1");
		} catch (FileNotFoundException e) {
			list.cons.printConsoleLine("Whoops");
		}
		
		/*
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		
		frame.add(list.table);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		*/
		
		list.cons.printConsoleLine("" + list.getTeamsListString());
	}
}
