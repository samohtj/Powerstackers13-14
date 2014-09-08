package teams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
 * <li>QP: The team's <b>qualification points</b>. The qualification points are added
 * to the team based on the outcome of their match. If the team wins their match,
 * they receive 2 qualification points. If they tie, they receive 1 point. If they
 * lose their match, they receive 0 points. The teams are ranked by this number
 * first. Teams that win more matches will be higher in the listing.
 * <li>RP: The team's <b>round points</b>. After the match is over, the final score of
 * the losing alliance is added to the round point total of all teams in the match. This
 * is kept as an indicator of how competitive the matches have been. A team with a high 
 * QP score may have won a lot of matches, but if their RP score is low, then the competition
 * has not been very tough. A team with a high RP score has been in a lot of competitive matches, 
 * where more points were scored all-around, and may be a better choice. 
 * </ul></blockquote>
 * 
 * <p>Also contains the capability to store and load teams' data from files. The files
 * are written in properties style, and are loaded from a fixed location.
 * 
 * <p>I'm planning to add more data points, such as number of balls scored, average 
 * height of the tubes, autonomous routines, etc.
 * 
 * @author Jonathan Thomas
 */
public class Team {
	// Important team data. Name and number
	private int teamNumber = 0;
	private String teamName = "NO NAME";
	
	// Qualifying points and round points for this competition. This information is not stored in the team's data file
	private int qp = 0;
	private int rp = 0;
	
	// Average points earned in a match, and the total number of matches played
	@SuppressWarnings("unused")
	private int averagePoints = 0;
	@SuppressWarnings("unused")
	private int totalMatches = 0;
	
	private ArrayList<Award> awards = new ArrayList<Award>();
	
	// File path and extension for saving team data
	public static String teamsDataPath = "data/teams/";
	public static String teamsFileExt = "";
	
	public ConsoleWindow cons = new ConsoleWindow();
	
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
		try {
			// Try to load the team from a file
			loadFromFile(file);
		} catch (FileNotFoundException e) {
			// If there was a problem with the loading, print out a message to the user
			cons.printConsoleLine("There was a problem loading a team's data file."
					+ "\n\tThe file may be corrupted or may not exist\n\t File: " + teamsDataPath + file.getName() + teamsFileExt);
		}
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
	public void saveTeamInfo(){
		try{
			// Create a PrintWriter to write the information to the file
			PrintWriter writer = new PrintWriter(teamsDataPath + getSaveFileName() + teamsFileExt);
			
			// Print the information to the file, and close it
			writer.println("teamName=" + teamName);
			writer.println("teamNumber=" + teamNumber);
			writer.close();
			
			// Print a message to the user
			System.out.println("Saved team " + toString());
		}catch(FileNotFoundException ex){
			cons.printConsoleLine("There was an error saving team data");
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
	public void loadFromFile(File file) throws FileNotFoundException{
		//File file = new File(teamsDataPath + teamName + teamsFileExt);
		
		if(!file.exists())
			throw new FileNotFoundException();
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(file);
		
		setTeamName(in.nextLine().split("=")[1]);
		setTeamNumber(Integer.parseInt(in.nextLine().split("=")[1]));
	}
	
	public static void main(String args[]){
		Team team = new Team(234, "The Feggits");	
		team.addAwards(Award.COMPASS_AWARD);
		System.out.println(team.getAwardsList()[0]);
	}
}
