package teams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTable;

import userinterface.ConsoleWindow;
import userinterface.CustomListModel;

/**
 * Stores a list of FTC teams. Also contains a JTable to display the teams in the list.
 * There is also the capability to load a list of teams from a file, and each team from
 * its own respective file. Teams can be added to or removed from the list, as well as 
 * their individual data edited.
 * 
 * <blockquote><b>teams</b>: A private ArrayList containing all the teams on the list.<br>
 * <b>table</b>: A public JTable that stores a list of FTC teams.<br>
 * <b>teamsListFilePath</b>: The file path of the save files for the teams.<br>
 * <b>teamsListFileExt</b>: The default file extension for team save files. Currently empty.<br> 
 * </blockquote>

 * @author Jonathan Thomas
 *
 */
public class TeamsList {
	// ArrayList to store all the teams
	private ArrayList<Team> teams = new ArrayList<Team>();
	
	// A JTable to visually display the list of teams
	public JTable table = new JTable();

	public static transient String teamsListFilePath = "data/teams/";
	public static transient String teamsListFileExt = ".txt";
	
	public ConsoleWindow cons = new ConsoleWindow();
	
	/**
	 *
	 */
	public TeamsList(){
		// Update the table once. It will be empty
		refreshTeamsTable();		
	}
	
	/**
	 * Creates a list of teams from an array.
	 * 
	 * @param teams An array of Team objects that will populate the list.
	 */
	public TeamsList(Team[] teams){
		// Add the teams into the list
		for(int i = 0; i < teams.length; i++){
			addTeam(teams[i]);
		}
	}
	
	/**
	 * Set the console window for console output.
	 * 
	 * @param window The ConsoleWindow object that you want to add.
	 */
	public void setConsoleWindow(ConsoleWindow window){
		cons = window;
	}
	
	
	
	/**
	 * Updates the visual JTable with the teams currently in the list. This method must be called
	 * every time there is a change to the list.
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
		CustomListModel model = new CustomListModel(teamsList, columnHeaders);
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
	 * <p>This method adds a team to the list, prints the team's information to the console,
	 * and refreshes the JTable.
	 * 
	 * @param team The Team object that you would like to add.
	 */
	public void addTeam(Team team){
		teams.add(team);
		cons.printConsoleLine("Added team: " + team.toString());
		refreshTeamsTable();
	}
	
	/**
	 * Remove the team at the specified index from the list, prints a message to the console,
	 * and refreshes the JTable. If the provided index does not exist, throw an exception.
	 * 
	 * @throws IndexOutOfBoundsException Thrown if the index is out of the bounds of the ArrayList.
	 * @param index The index of the team you would like to remove.
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
	 * 
	 * @return An ArrayList containing all the teams on the list.
	 */
	public ArrayList<Team> getList(){
		return teams;
	}
	
	/**
	 * Returns the selected index of the JTable. If no row is selected, return -1.
	 * 
	 * @return The selected row of the JTable.
	 */
	public int getSelectedIndex(){
		return table.getSelectedRow();
	}
	
	/**
	 * Returns the team at the specified index in the list. If the index does not exist, throws an exception.
	 * 
	 * @param index The position of the team that you want.
	 * @return The Team object at that position.
	 */
	public Team getTeamAt(int index){
		return teams.get(index);
	}
	
	/**
	 * Returns a formatted string with a list of teams. Prints the total number of teams, and then a list of team
	 * names and numbers. This method is mostly useful for printing the contents of the list to the console. An example:
	 * <blockquote>
	 * List contains 2 teams:
	 * <blockquote>5029 : Powerstackers<br>
	 * 4251 : Cougars</blockquote>
	 * </blockquote>
	 * 
	 * @return A formatted string with the amount of teams, and a list of their names and numbers.
	 */
	public String getTeamsListString(){
		StringBuilder builder = new StringBuilder();
		builder.append("List contains " + teams.size() + ((teams.size() == 1)? " team:\n" : " teams:\n"));
		for(Team team: teams)
			builder.append("\t" + team.toString() + "\n");
		return builder.toString();
	}
	
	/**
	 * An exception class that is thrown whenever the index of an array is out-of-bounds.
	 * 
	 * @author Jonathan Thomas
	 */
	public class IndexOutOfBoundsException extends Exception{
		private static final long serialVersionUID = 1L;	
	}
	
	/**
	 * Save all the teams in the list to their respective files.
	 */
	public void saveAllTeams(){
		for(Team team: teams)
			Team.save(team);
	}
	
	/**
	 * Loads a list of teams from a data file.
	 * 
	 * <p>The files that this method reads are just lists of filenames for team saves. The inside
	 * of one file might look like this:
	 * <blockquote>4251_Cougars<br>
	 * 5029_Powerstackers<br>
	 * 5501_DRSS Enterprise<br></blockquote>
	 * 
	 * The strings you see are the file names for teams that are stored in the program's library.
	 * This method will look for those teams, and load them into Team objects, which it will use 
	 * to populate the list.
	 * 
	 * @param eventFileName The name of the file storing your list of teams.
	 */
	public void loadFromFile(String eventFileName) throws FileNotFoundException{
		// Create a File object
		File file = new File(teamsListFilePath + eventFileName);
		if(!file.exists())
			throw new FileNotFoundException();
		
		// Integer to store the total number of teams listed in the match file
		int totalFiles = 0;
		
		// Create a Scanner object to read the file
		Scanner in = new Scanner(file);
		
		// Read the file, and load a team file for each team listed in the match file
		if(in.hasNext()){
			cons.printConsoleLine("Teams found in " + eventFileName + ". Loading files:");
			while(in.hasNext()){
				// Create a String object to store the name of the team
				String teamName = in.nextLine();

				// If the file exists, load from it
				// Otherwise, print an error message to the console
				File teamFile = new File(Team.teamsDataPath+teamName+Team.teamsFileExt);
				if(file.exists()){
					teams.add(Team.load(teamFile));
					cons.printConsoleLine("\t" + teamName + " loaded successfully");
				}else
					cons.printConsoleLine("\tERROR: File \"" + teamName + "\" not found");
				totalFiles++;
			}
		}else
			// If the file is empty, print a message to the console
			cons.printConsoleLine("No teams found in " + eventFileName);
		
		// Print a message to the console to inform the user of what went on
		cons.printConsoleLine(" Loaded " + teams.size() + "/" + totalFiles + " succesfully.");
		
		// Remember to close the input stream!
		in.close();
	}
	
	public static void save(){
		
	}
	
	public static void main(String args[]){
		TeamsList list = new TeamsList();
		list.cons.setVisible(true);
		try {
			list.loadFromFile("roster.txt");
		} catch (FileNotFoundException e) {
			list.cons.printConsoleLine("Whoops");
		}
		
		
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		
		frame.add(list.table);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		list.cons.printConsoleLine("" + list.getTeamsListString());
	}
}
