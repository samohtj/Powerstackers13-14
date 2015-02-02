package teams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import tournaments.Award;
import userinterface.ConsoleWindow;

/**
 * Stores all the data for a FIRST Tech Challenge team. Stores the team's name, 
 * number, and other assorted info. Also stores the team's QP and RP totals for 
 * the current competition, as well as their average score over all competitions
 * and the awards that they have won.
 * 
 * <blockquote><ul>
 * <li>Number: The team's <b>identification number</b>.
 * <li>Name: The team's <b>name</b>.
 * <li>Description: A short description of the team, where they're from, etc.
 * </ul></blockquote>
 * 
 * <p>Also contains the capability to store and load teams' data as a serialized object.
 * 
 * <p>I'm planning to add more data points, such as number of balls scored, average 
 * height of the tubes, autonomous routines, etc.
 * 
 * @author Jonathan Thomas
 */
public class Team implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1036964836902877837L;
	
	// Important team data. Name and number
	private int teamNumber = 0;
	private String teamName = "NO NAME";
	
	// Qualifying points and round points for this competition. This information is not stored in the team's data file
	private transient int qp = 0;
	private transient int rp = 0;
	
	// Total points earned and total matches played. Used to calculate the average points a team earns.
	private int totalPoints = 0;
	private int totalMatches = 0;
	
	// A short description of the team
	private String description = "";
	
	private ArrayList<Award> awards = new ArrayList<Award>();
	
	// File path and extension for saving team data
	public static String teamsDataPath = "data/teams/";
	public static String teamsFileExt = ".dat";
	
	// Console window used for outputting text
	public transient ConsoleWindow cons = new ConsoleWindow();
	
	/**
	 * 
	 */
	public Team(){}
	
	/**
	 * Constructor that sets team name and number.
	 * 
	 * @param teamNumber An integer number, used to identify the team.
	 * @param teamName A String containing the team's name.
	 */
	public Team(int teamNumber, String teamName){
		this.teamNumber = teamNumber;
		this.teamName = teamName;
	}
	
	/**
	 * Constructor that sets team number only.
	 * 
	 * @param teamNumber An integer number, used to identify the team.
	 */
	public Team(int teamNumber){
		this.teamNumber = teamNumber;
	}
	
	/**
	 * Create a Team object directly from a file.
	 * <p>This constructor loads the team's data directly from a file.
	 * It parses the file for the team's information, and populates
	 * the object's data fields with what it finds. <p>If there is a
	 * problem with the file, the method will print a message to the
	 * console.
	 * 
	 * @param file The File object to load the team's information from.
	 */
	public Team(File file){
		// Try to load the team from a file
		load(file);
	}
	
	/**
	 * @return The team's number identifier.
	 */
	public int getTeamNumber(){
		return teamNumber;
	}
	
	/**
	 * Sets the team's number identifier.
	 * 
	 * @param number
	 */
	public void setTeamNumber(int number){
		teamNumber = number;
		cons.printConsoleLine("Set " + teamName + "'s number to " + teamNumber);
	}
	
	/**
	 * @return The team's name.
	 */
	public String getTeamName(){
		return teamName;
	}
	
	/**
	 * Sets the team's name
	 * 
	 * @param name A String with the team's name
	 */
	public void setTeamName(String name){
		teamName = name;
		cons.printConsoleLine("Set team " + teamNumber + "'s name to " + teamName);
	}
	
	/**
	 * Set the team's qualifying points total.
	 * 
	 * @param qp
	 */
	public void setQP(int qp){
		this.qp = qp;
		cons.printConsoleLine("Set " + toString() + "'s Qualifying Points to " + qp);
	}
	
	/**
	 * @return The team's qualifying points total.
	 */
	public int getQP(){
		return qp;
	}
	
	/**
	 * Add qualifying points to a team. The parameter amount is added to the team's current QP total.
	 * 
	 * @param qp The number of points you would like to add.
	 */
	public void addQP(int qp){
		this.qp += qp;
		cons.printConsoleLine("Added " + qp + " qualifying points to team " + toString());
	}
	
	/**
	 * Set the team's round points total.
	 * 
	 * @param rp
	 */
	public void setRP(int rp){
		this.rp = rp;
		cons.printConsoleLine("Set " + toString() + "'s Round Points to " + rp);
	}
	
	/**
	 * @return The team's round points total.
	 */
	public int getRP(){
		return rp;
	}
	
	/**
	 * Add round points to a team. The parameter amount will be added to the team's current RP total.
	 * 
	 * @param rp The number of points that you would like to add.
	 */
	public void addRP(int rp){
		this.rp += rp;
		cons.printConsoleLine("Added " + rp + " round points to team " + toString());
	}
	
	/**
	 * <b>CURRENTLY RETURNS SINGLE-DIMENSIONAL ARRAY. WILL FIX.</b>
	 * <p>Returns a list of awards that the team has won, in the form of a two-dimensional array. The first
	 * dimension is split by competition. Each element is a different event. The second dimension is the list
	 * for that event. The first element of every second-level array is the title of the competition, and the 
	 * following elements are the list of awards. An example:
	 * <blockquote><ul>
	 * <li>{"Dayton Regional Qualifier", "Think Award"}
	 * <li>{"Columbus Regional Qualifier", "PTC Design Award", "Finalist Alliance Member"}
	 * <li>{"Ohio State Championship", "Inspire Award", "Winning Alliance Member"}
	 * </ul></blockquote?
	 * 
	 * @return A String array containing a list of awards that the team has won.
	 */
	public String[] getAwardsList(){
		String[] newList = new String[awards.size()];
		for(int i = 0; i < awards.size(); i++)
			newList[i] = Award.awardTitles[awards.get(i).getAwardType()];
		return newList;
		
	}
	
	/**
	 * Add an award to the team's history. The parameter value is the identifier for the award you would like
	 * to add. The Award class has static constants for use in identifying awards.
	 * 
	 * <p><b>THIS DOESN'T WORK THE WAY I WANT IT TO. WILL FIX.</b>
	 * 
	 * @param awardType The identifier for the award you want to add.
	 */
	public void addAwards(int awardType){
		awards.add(new Award(awardType));
	}
	
	/**
	 * Returns the team name and number in a formatted string. Example:
	 * <blockquote><b>"5029 : Powerstackers"</b></blockquote>
	 */
	public String toString(){
		return teamNumber + " : " + teamName;
	}
	
	/**
	 * Returns the team's name and number in a formatted string for use as a file name. Example:
	 * <blockquote><b>5029_Powerstackers</b></blockquote>
	 */
	public String getSaveFileName(){
		return teamNumber + "_" + teamName;
	}
	
	/**
	 * Save the team's information to a file.
	 * <p>This method currently saves in properties file-type notation, with the name of the value and the 
	 * value itself separated by an equals sign. Example:
	 * 
	 * <blockquote>teamName=Powerstackers
	 * <br>teamNumber=5029</blockquote>
	 * 
	 * <p>If there is a problem with the file, the method will print a message to the console and the data
	 * will not be saved. Currently, the method only saves the team name and number. I'm planning to add
	 * the other information into the file, once I work out a way to separate data by competition.
	 */
	public static void save(Team team){
		try{
			// Create an object output stream to write to the file
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(teamsDataPath + team.getSaveFileName() + teamsFileExt));
			
			// Write the object to the file
			out.writeObject(team);
			out.close();
		}catch(FileNotFoundException ex){
			team.cons.printConsoleLine("There was an error saving team data");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			team.cons.printConsoleLine("There was an error saving team data");
		}
	}
	
	/**
	 * Load the team's information from a file.
	 * <p>This method reads in properties file-type notation, with the name of the value and the 
	 * value itself separated by an equals sign. Example:
	 * 
	 * <blockquote>teamName=Powerstackers
	 * <br>teamNumber=5029</blockquote>
	 * 
	 * <p>If there is a problem with the file, the method will throw an exception and the data
	 * will not be loaded. Currently, the method only loads the team name and number. I'm planning to add
	 * the other information into the file, once I work out a way to separate data by competition.
	 * 
	 * @param file The File object that stores the data you want to load.
	 * @throws FileNotFoundException Thrown if the file does not exist.
	 */
	public static Team load(File file){
		Team team = null;
		
		try {
			// Create an object input stream to read in the file
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			
			// Try to load the object from the file
			team = (Team) in.readObject();
			
			// Remember to close the input stream!
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// If for some reason the team wasn't loaded, create an empty team
		if(team==null){
			team = new Team();
			team.cons.printConsoleLine("There was a problem loading team data");
		}
		
		// Return the team
		return team;
	}
	
	
	
	/**
	 * Add 1 match to the total matches played, and add the specified number of points to the team's total.
	 * This helps in calculating the team's average score.
	 * 
	 * @param points
	 */
	public void addToAveragePoints(int points){
		totalMatches++;
		totalPoints += points;		
	}

	/**
	 * Calculate the team's average score through all matches played.
	 * 
	 * @return The team's average score.
	 */
	public int getAverageScore() {
		if(totalMatches == 0)
			return 0;
		else
			return (int) totalPoints / totalMatches;
	}
	
	/**
	 * Set the team's short description.
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * @return The team's short description.
	 */
	public String getDescription(){
		return description;
	}
	
	public static void main(String args[]){
		Team team1 = new Team(5029, "Powerstackers");
		Team team2 = new Team(4251, "Cougars");
		Team team3 = new Team(5501, "USS Enterprise");
		
		Team.save(team1);
		Team.save(team2);
		Team.save(team3);
	}
	
}
