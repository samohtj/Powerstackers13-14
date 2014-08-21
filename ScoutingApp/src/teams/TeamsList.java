package teams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

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
	
	/**
	 * Empty constructor.
	 */
	public TeamsList(){
		
	}
	
	/**
	 * Creates a list of teams from an array.
	 * @param teams
	 */
	public TeamsList(Team[] teams){
		for(int i = 0; i < teams.length; i++){
			addTeam(teams[i]);
		}
	}
	
	/**
	 * Loads a list of teams from a text file (empty).
	 */
	public void loadFromFile(String eventFileName) throws FileNotFoundException{
		File file = new File(teamsListFilePath + eventFileName);
		if(!file.exists())
			throw new FileNotFoundException();
		int totalFiles = 0;
		@SuppressWarnings("unused")
		Scanner in = new Scanner(file);
		if(in.hasNext()){
			System.out.println("\n[+] Teams found in " + eventFileName + ". Loading files:");
			while(in.hasNext()){
				String teamName = in.nextLine();

				if(new File(Team.teamsDataPath + teamName + Team.teamsFileExt).exists()){
					teams.add(new Team(new File(Team.teamsDataPath + teamName + Team.teamsFileExt)));
					System.out.println("[-] \t" + teamName);
				}else
					System.out.println("[!] \t" + teamName + " ERROR: File not found");
				totalFiles++;
			}
		}else
			System.out.println("[!] No teams found in " + eventFileName);
		
		System.out.println(((teams.size() == totalFiles)? "[+]" : "[!]") + " Loaded " +
				teams.size() + "/" + totalFiles + " succesfully.");
	}
	
	/**
	 * Updates the visual JTable with the teams currently in the list.
	 */
	private void refreshTeamsTable(){
		String[] columnHeaders = {"Team #", "Team Name"};
		Object[][] teamsList = new Object[teams.size()][2];
		for(int i = 0; i < teams.size(); i++){
			String[] teamData = {Integer.toString(teams.get(i).getTeamNumber()), teams.get(i).getTeamName()};
			teamsList[i] = teamData;
		}
		TeamsListModel model = new TeamsListModel(teamsList, columnHeaders);
		table.setModel(model);
		
	}
	
	/**
	 * Add a team to the list.
	 * @param team
	 */
	public void addTeam(Team team){
		teams.add(team);
		System.out.println(team.toString());
		refreshTeamsTable();
	}
	
	/**
	 * Remove the team at the specified index. Throws exception if index does not exist.
	 * @throws IndexOutOfBoundsException
	 * @param index
	 */
	public void removeTeam(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index < teams.size() - 1)
			throw new IndexOutOfBoundsException();
		teams.remove(index);
		refreshTeamsTable();
		System.out.println("team removed");
	}
	
	/**
	 * Return the list of teams itself.
	 * @return
	 */
	public ArrayList<Team> getList(){
		return teams;
	}
	
	public int getSelectedIndex(){
		return table.getSelectedRow();
	}
	
	public String getTeamsListString(){
		StringBuilder builder = new StringBuilder();
		builder.append("\n[+] List contains " + teams.size() + ((teams.size() == 1)? " team:\n" : " teams:\n"));
		for(int i = 0; i < teams.size(); i++)
			builder.append("[-] \t" + teams.get(i).toString() + "\n");
		return builder.toString();
	}
	
	public class IndexOutOfBoundsException extends Exception{
		
	}
	
	/**
	 * A custom ListModel to create a new JTable from the information in the teams list.
	 * @author Jonathan
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
		try {
			list.loadFromFile("LIST1");
		} catch (FileNotFoundException e) {
			System.out.println("Whoops");
		}
		
		System.out.println(list.getTeamsListString());
	}
}
