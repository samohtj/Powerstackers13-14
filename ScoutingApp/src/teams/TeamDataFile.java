package teams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TeamDataFile extends File{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 *	Save file structure
	 *	0: Name
	 *	1: Number
	 *	2: Description
	 *	3: Year of competition
	 *	4: Average score; total score ever; total matches played
	 *	5: Game specific stuff
	 *	6: Awards won
	 *	7-n: Repeat 3-6 
	 */
	
	
	public static int NAME_INDEX = 0;
	public static int NUMBER_INDEX = 1;
	public static int DESCRIPTION_INDEX = 2;
	public static int YEAR_INDEX = 3;
	public static int AVG_SCORE_INDEX = 4;
	public static int GAME_INFO_INDEX = 5;
	public static int AWARDS_INDEX = 6;
	
	
	private String extension = "";
	private String path = "";
	
	
	/**
	 * Creates a new team data save file. The file stores all the team's relevant information,
	 * and can be loaded into a Team object via the loadTeamData function.
	 *  
	 * @param path The path of save file's location.
	 */
	public TeamDataFile(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Set the file type extension of the team data file. This is kept empty by default.
	 * @param extension
	 */
	public void setExtension(String extension){
		this.extension = extension;
	}
	
	/**
	 * 
	 * @return The file type extension of the data file.
	 */
	public String getExtension(){
		return extension;
	}
	
	public void setPath(){
		
	}
	public String getPath(){
		return path;
	}
	
	/**
	 * Load a new team data save file into a new Team object. The method will take all the data
	 * in the save file and put in into the Team object in their proper data fields.
	 * @return A new Team object containing all the team's data.
	 * @throws FileNotFoundException
	 */
	public Team loadTeamData() throws FileNotFoundException{
		// If the file does not exist, throw an exception
		if(!this.exists())
			throw new FileNotFoundException();
		
		// Create a Scanner object to read the file
		@SuppressWarnings("resource")
		Scanner input = new Scanner(this);
		
		// Create an ArrayList to store the file's contents
		ArrayList<String> contents = new ArrayList<String>();
		
		// Add all the contents of the save file into the ArrayList
		while(input.hasNext()){
			contents.add(input.nextLine());
		}
		
		// Create a new team, and add all the array contents into its fields
		Team newTeam = new Team();
		
		newTeam.setTeamNumber(Integer.parseInt(contents.get(NUMBER_INDEX)));
		newTeam.setTeamName(contents.get(NAME_INDEX));
		newTeam.setDescription(contents.get(DESCRIPTION_INDEX));
		
		return newTeam;		
	}

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		TeamDataFile dataFile = new TeamDataFile("data/teams/4251_Cougars");
		Team team = dataFile.loadTeamData();
		System.out.println(team.toString());

	}

}
