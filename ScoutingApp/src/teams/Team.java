package teams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Team {
	// Important team data. Name and number
	private int teamNumber;
	private String teamName;
	
	public static String teamsDataPath = "data/teams/";
	public static String teamsFileExt = "";
	
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
			loadFromFile(file);
		} catch (FileNotFoundException e) {
			System.out.println("[!] There was a problem loading a team's data file."
		 + "\n[!] \tThe file may be corrupted or may not exist\n[!] \t File: " + file.getName());
		}
	}
	
	public int getTeamNumber(){
		return teamNumber;
	}
	
	public void setTeamNumber(int number){
		teamNumber = number;
	}
	
	public String getTeamName(){
		return teamName;
	}
	
	public void setTeamName(String name){
		teamName = name;
	}
	
	/**
	 * Returns the team name and number in a formatted string
	 */
	public String toString(){
		return teamNumber + " : " + teamName;
	}
	
	public String getSaveFileName(){
		return teamNumber + "_" + teamName;
	}
	
	public void saveTeamInfo(){
		
		try{
			PrintWriter writer = new PrintWriter(teamsDataPath + getSaveFileName() + teamsFileExt);
			
			writer.println("teamName=" + teamName);
			writer.println("teamNumber=" + teamNumber);
			writer.close();
			System.out.println("[+] Saved team " + toString());
		}catch(FileNotFoundException ex){
			System.out.println("[!] There was an error saving team data");
		}
	}
	
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
		team.saveTeamInfo();
		
	}
}
