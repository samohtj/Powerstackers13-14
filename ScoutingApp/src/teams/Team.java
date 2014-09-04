package teams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import tournaments.Award;
import userinterface.ConsoleWindow;

public class Team {
	// Important team data. Name and number
	private int teamNumber = 0;
	private String teamName = "NO NAME";
	
	// Qualifying points and round points for this competition. This information is not stored in the team's data file
	private int qp = 0;
	private int rp = 0;
	
	// Average points earned in a match, and the total number of matches played
	private int averagePoints = 0;
	private int totalMatches = 0;
	
	private ArrayList<Award> awards = new ArrayList<Award>();
	
	// File path and extension for saving team data
	public static String teamsDataPath = "data/teams/";
	public static String teamsFileExt = "";
	
	public ConsoleWindow cons = new ConsoleWindow();
	
	/**
	 * Empty constructor. Use this, and then load the data from a file
	 */
	public Team(){}
	
	/**
	 * Constructor that sets team name and number
	 * @param teamNumber
	 * @param teamName
	 */
	public Team(int teamNumber, String teamName){
		this.teamNumber = teamNumber;
		this.teamName = teamName;
	}
	
	/**
	 * Constructor that sets team number
	 * @param teamNumber
	 */
	public Team(int teamNumber){
		this.teamNumber = teamNumber;
	}
	
	public Team(File file){
		try {
			// Try to load the team from a file
			loadFromFile(file);
		} catch (FileNotFoundException e) {
			// If there was a problem with the loading, print out a message to the user
			cons.printConsoleLine("There was a problem loading a team's data file."
					+ "\n\tThe file may be corrupted or may not exist\n\t File: " + file.getName());
		}
	}
	
	/**
	 * Returns the team's unique number
	 * @return
	 */
	public int getTeamNumber(){
		return teamNumber;
	}
	
	/**
	 * Sets the team's number
	 * @param number
	 */
	public void setTeamNumber(int number){
		teamNumber = number;
		cons.printConsoleLine("Set " + teamName + "'s number to " + teamNumber);
	}
	
	/**
	 * Returns the team's name
	 * @return
	 */
	public String getTeamName(){
		return teamName;
	}
	
	/**
	 * Sets the team's name
	 * @param name
	 */
	public void setTeamName(String name){
		teamName = name;
		cons.printConsoleLine("Set team " + teamNumber + "'s name to " + teamName);
	}
	
	/**
	 * Set the team's qualifying points
	 * @param qp
	 */
	public void setQP(int qp){
		this.qp = qp;
		cons.printConsoleLine("Set " + toString() + "'s Qualifying Points to " + qp);
	}
	
	/**
	 * Returns the team's qualifying points
	 * @return
	 */
	public int getQP(){
		return qp;
	}
	
	/**
	 * Add qualifying points to a team
	 * @param qp
	 */
	public void addQP(int qp){
		this.qp += qp;
		cons.printConsoleLine("Added " + qp + " qualifying points to team " + toString());
	}
	
	/**
	 * Set the team's round points
	 * @param rp
	 */
	public void setRP(int rp){
		this.rp = rp;
		cons.printConsoleLine("Set " + toString() + "'s Round Points to " + rp);
	}
	
	/**
	 * Returns the team's round points
	 * @return
	 */
	public int getRP(){
		return rp;
	}
	
	/**
	 * Add round points to a team
	 * @param rp
	 */
	public void addRP(int rp){
		this.rp += rp;
		cons.printConsoleLine("Added " + rp + " round points to team " + toString());
	}
	
	public String[] getAwardsList(){
		String[] newList = new String[awards.size()];
		for(int i = 0; i < awards.size(); i++)
			newList[i] = Award.awardTitles[awards.get(i).getAwardType()];
		return newList;
		
	}
	
	/**
	 * Add a list of awards to the team's history. Pass an array of Strings, with the first element of the array being the title of the
	 * competition. Example: {"Central Ohio Qualifier", "Inspire", "Winning Alliance Captain"}
	 * @param list
	 */
	public void addAwards(int awardType){
		awards.add(new Award(awardType));
	}
	
	/**
	 * Returns the team name and number in a formatted string
	 */
	public String toString(){
		return teamNumber + " : " + teamName;
	}
	
	/**
	 * Returns the team's name and number in a formatted string for use as a file name
	 * @return
	 */
	public String getSaveFileName(){
		return teamNumber + "_" + teamName;
	}
	
	/**
	 * Save the team's information to a file
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
			System.out.println("There was an error saving team data");
		}
	}
	
	/**
	 * Load the team's information from a file
	 * @param file
	 * @throws FileNotFoundException
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
