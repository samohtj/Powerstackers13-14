package matches;
import java.util.ArrayList;
import java.util.Date;

import teams.Team;
import userinterface.ConsoleWindow;

/**
 * Creates a standard FIRST Tech Challenge match. Records all scores, and contains methods for calculating the winner.
 *  Also records the date, match number, the field name, and the teams involved.
 *  <blockquote><b>TEAMS:</b><br>
 *  	A list of teams is stored in an ArrayList object. I have plans to change this to a TeamList object.
 *  	Methods for adding and removing teams are included.
 * </blockquote>
 * @author Jonathan Thomas
 *
 */
public class Match {
		// Constants to identify alliance members, match types
	public static final int RED_1 	= 0;
	public static final int RED_2 	= 1;
	public static final int BLUE_1 	= 2;
	public static final int BLUE_2 	= 3;
	public static final boolean MATCHTYPE_QUALIFICATION = true;
	public static final boolean MATCHTYPE_FINAL 		= false;
	public static final int RED_WINS 	= 1;
	public static final int BLUE_WINS 	= 2;
	
	// The console window to be used for console output by this object
	ConsoleWindow cons = new ConsoleWindow();

	// Match data to store. Date, field number, match number, type of match, and teams involved
	Date matchDate = new Date();
	private int matchNumber;
	private String matchField = "1A";
	private ArrayList<Team> matchTeams = new ArrayList<Team>();
	private boolean type = MATCHTYPE_QUALIFICATION;	// Default to qualification match type
	
	// Rend and blue scores and penalties
	private int redScore 		= 0;
	private int blueScore 		= 0;
	private int redPenalties 	= 0;
	private int bluePenalties 	= 0;
	
	/**
	 * Empty constructor that does not set anything. Use the parseSaveString method to fill the object with data.
	 */
	public Match(){
		
	}
	
	/**
	 * Create a new Match object. Match type and number default to Qualifying and 1, respectively.
	 * 
	 * @param matchTeams An array of Team objects. This array will populate the match with teams.
	 */
	public Match(Team[] matchTeams){
		this.type = MATCHTYPE_QUALIFICATION;	// Make sure the match type is set to qualification
		this.matchNumber = 1;					// Default the match number to 1
		try {
			setTeams(matchTeams);				// Try to set the list of teams
		} catch (TooManyTeamsException | TooFewTeamsException e) {	// The wrong number of teams was given
				((TooManyTeamsException) e).showErrorMessage();		// Display an appropriate error message
		}
	}
	
	/**
	 * Create a new Match object. Match number defaults to 1.
	 * 
	 * @param matchTeams An array of Team objects. This array will populate the match with teams.
	 * @param type Whether the match is Qualifying or Elimination. The Match class has static constants for use setting this parameter.
	 */
	public Match(Team[] matchTeams, boolean type){
		this.type = type;		// Set the match type to the given type
		this.matchNumber = 1;	// Default the match number to 1
		try {
			setTeams(matchTeams);// Try to set the number of teams
		} catch (TooManyTeamsException | TooFewTeamsException e) {	// The wrong number of teams was given
			((TooManyTeamsException) e).showErrorMessage();
		}
	}
	
	/**
	 * Create a new Match object.
	 * 
	 * @param matchTeams An array of Team objects. This array will populate the match with teams.
	 * @param type Whether the match is Qualifying or Elimination. The Match class has static constants for use setting this parameter.
	 * @param matchNumber The number identifier for the match.
	 */
	public Match(Team[] matchTeams, boolean type, int matchNumber){
		this.type = type;				// Set the match type to the given type
		this.matchNumber = matchNumber;	// Set the match number to the given number
		try {
			setTeams(matchTeams);		// Try to set the list of teams
		} catch (TooManyTeamsException | TooFewTeamsException e) {	// The wrong number of teams was given
			((TooManyTeamsException) e).showErrorMessage();			// Show an appropriate error message
		}
	}
	
	/**
	 * Set the match number.
	 * 
	 * @param matchNumber An integer identifier
	 */
	public void setMatchNumber(int matchNumber){
		this.matchNumber = matchNumber;
	}
	
	/**
	 * @return The number identifier for this match.
	 */
	public int getMatchNumber(){
		return matchNumber;
	}
	
	/**
	 * Set the date and time of the match.
	 * 
	 * @param date A Date object containing the date and time of the match.
	 */
	public void setMatchDate(Date date){
		matchDate = date;
	}
	
	/**
	 * @return A Date object containing the date and time of the match.
	 */
	public Date getMatchDate(){
		return matchDate;
	}
	
	/**
	 * Set the name or number of the field where the match took place. Useful for large competitions, where there are more than one field on the floor.
	 * 
	 * @param matchField A String object containing the field's name or number.
	 */
	public void setMatchField(String matchField){
		this.matchField = matchField;
	}
	
	/**
	 * @return A String containing the name or number of the field where the match took place.
	 */
	public String getMatchField(){
		return matchField;
	}
	
	/**
	 * Set the list of teams participating in this match. Exactly 4 teams must participate in each match.
	 * 
	 * @param matchTeams An array of Team objects. The match will be populated with teams from this array.
	 * @throws TooManyTeamsException Thrown if the array parameter contains too many teams (maximum 4)
	 * @throws TooFewTeamsException Thrown if the array parameter contains too few teams (minimum 4)
	 */
	public void setTeams(Team[] matchTeams) throws TooManyTeamsException, TooFewTeamsException{
		// If the wrong number of teams is given (i.e., not exactly 4) throw an exception
		if(matchTeams.length > 4)
			throw new TooManyTeamsException();
		if(matchTeams.length < 4)
			throw new TooFewTeamsException();
		
		// Load the given teams into the list
		for(int i = 0; i < 4; i++)
			this.matchTeams.add(matchTeams[i]);	
	}
	
	/**
	 * Change the team at a certain position. The four positions are:
	 * <ol><li><b>RED 1</b>
	 * <li><b>RED 2</b>
	 * <li><b>BLUE 1</b>
	 * <li><b>BLUE 2</b>
	 * </ol>
	 * <p> The Match class contains static constants for use in setting these positions.
	 * 
	 * @param index The position of the team you are replacing.
	 * @param newTeam The Team that you are replacing it with.
	 */
	public void setTeam(int index, Team newTeam){
		matchTeams.set(index, newTeam);
	}
	
	/**
	 * @return An ArrayList containing all the teams participating in this match.
	 */
	public ArrayList<Team> getTeams(){
		return matchTeams;
	}
	
	/**
	 * Get the team at a specific position. The four positions are:
	 * <ol><li><b>RED 1</b>
	 * <li><b>RED 2</b>
	 * <li><b>BLUE 1</b>
	 * <li><b>BLUE 2</b>
	 * </ol>
	 * <p> The Match class contains static constants for use in setting these positions.
	 * 
	 * @param index The position of the team you want.
	 * @return A Team object
	 */
	public Team getTeam(int index){
		return matchTeams.get(index);
	}
	
	/**
	 * Set the score of the red alliance. <b>Score</b> refers to the number of points gathered, <i>before</i> penalties are applied.
	 * 
	 * @param score
	 */
	public void setRedScore(int score){
		redScore = score;
	}
	
	/**
	 * @return The score of the red alliance. <b>Score</b> refers to the number of points gathered, <i>before</i> penalties are applied.
	 */
	public int getRedScore(){
		return redScore;
	}
	
	/**
	 * Set the score of the blue alliance. <b>Score</b> refers to the number of points gathered, <i>before</i> penalties are applied.
	 * @param score
	 */
	public void setBlueScore(int score){
		blueScore = score;
	}
	
	/**
	 * @return The score of the blue alliance. <b>Score</b> refers to the number of points gathered, <i>before</i> penalties are applied.
	 */
	public int getBlueScore(){
		return blueScore;
	}
	
	/**
	 * Set the amount of points that the red alliance has been penalized.
	 * The penalty points are added to the opposing alliance's <b>score</b> to obtain the alliance's <b>final score</b>.
	 * 
	 * @param penalties
	 */
	public void setRedPenalties(int penalties){
		redPenalties = penalties;
	}
	
	/**
	 * @return The amount of points that the red alliance has been penalized.
	 */
	public int getRedPenalties(){
		return redPenalties;
	}
	
	/**
	 * Set the amount of points that the blue alliance has been penalized.
	 * The penalty points are added to the opposing alliance's <b>score</b> to obtain the alliance's <b>final score</b>.
	 * 
	 * @param penalties
	 */
	public void setBluePenalties(int penalties){
		bluePenalties = penalties;
	}
	
	/**
	 * @return The amount of points that the blue alliance has been penalized.
	 */
	public int getBluePenalties(){
		return bluePenalties;
	}
	
	/**
	 * Adds the blue alliance's <b>penalty points</b> to the red alliance's <b>score</b> to obtain the red alliance's <b>final score</b>.
	 * 
	 * @return The alliance's final score.
	 */
	public int getRedFinalScore(){
		return redScore + bluePenalties;
	}
	
	/**
	 * Adds the red alliance's <b>penalty points</b> to the blue alliance's <b>score</b> to obtain the blue alliance's <b>final score</b>.
	 * 
	 * @return The alliance's final score.
	 */
	public int getBlueFinalScore(){
		return blueScore + redPenalties;
	}
	
	/**
	 * Set the type of match. There are two kinds of matches:
	 * <blockquote><b>QUALIFICATION:</b> The first matches of the day. These matches are used to determine the top 4 teams 
	 * for alliance selection before the Elimination matches. The alliance members are randomly selected. Your
	 *  alliance partner in one match may be your opponent in another.
	 * </blockquote><blockquote><b>ELIMINAITON:</b> The matches after alliance selection. These matches are best 2/3, and determine the 
	 * overall winner of the competition. The alliances for these matches are selected in the Alliance Selection Ceremony.
	 * Four alliances of three teams compete to eliminate each other. The rounds are played best 2/3. Alliance 1 and 4 compete, 
	 * and alliance 2 and 3 compete. Then, the winners of those two brackets compete against each other, again 2/3, to determine
	 * the overall winning alliance.</blockquote>
	 * <p>The Match class contains static constants for the setting of the match type.
	 * 
	 * @param type A boolean specifying the type of match.
	 */
	public void setMatchType(boolean type){
		this.type = type;
	}
	
	/**
	 * @return The type of match.
	 */
	public boolean getMatchType(){
		return type;
	}
	
	/**
	 * Calculate the winner of the  match. The two alliance's scores and penalties are added up, 
	 * and the alliance with the highest overall score is declared the winner. <p>The Match class
	 * contains static constants for use in identifying the winner of the match.
	 * 
	 * @return The alliance with the highest overall score. There are two possible returns:
	 * <ol><li><b>RED_WINS:</b> The red alliance had the higher score
	 * <li><b>BLUE_WINS:</b> The blue alliance had the higher score.</ol>
	 * <p>The Match class contains static constants for use in identifying the winner of the match.
	 */
	public int getWinner(){
		return (getRedFinalScore() > getBlueFinalScore())? RED_WINS : BLUE_WINS;
	}
	
	/**
	 * @return The match type and number in a formatted String.
	 * <br>An example: "Qualification match# 5"
	 */
	public String getMatchTitle(){
		return ((type)? "Qualification " : "Finals ") + " match #" + matchNumber;
	}
	
	/**
	 * This method is meant for creating save files.
	 * <blockquote>An example: <b>"A1|q|5029|7065|4251|5501|35|10|60|0"</b></blockquote>
	 * <br>This example signifies a match played on field A1, of the Qualifying type, with teams
	 * 5029, 7065, 4251, and 5501 participating. The red alliance has a score of 35, and a penalty
	 * of 10. The blue alliance has a score of 60, and a penalty of 0.
	 * <p>This save string is meant to be read by a method. All the data values are separated by 
	 * the "|" symbol. The string is parsed by the method, and all the data is entered into the object.
	 * 
	 * @return A string with all the match data. 
	 */
	public String toSaveString(){
		return matchField + "|" + ((type)?"q":"f")+ "|" +
				matchTeams.get(0).getTeamNumber() + "|" + matchTeams.get(1).getTeamNumber() + "|" + matchTeams.get(2).getTeamNumber() + "|"
				+ matchTeams.get(3).getTeamNumber() + "|" + redScore + "|" + redPenalties + "|" + blueScore + "|" + bluePenalties;
	}
	
	/**
	 * This method parses a save string and loads the data into the object.
	 * <blockquote>An example: <b>"A1|q|5029|7065|4251|5501|35|10|60|0"</b></blockquote>
	 * <br>This example signifies a match played on field A1, of the Qualifying type, with teams
	 * 5029, 7065, 4251, and 5501 participating. The red alliance has a score of 35, and a penalty
	 * of 10. The blue alliance has a score of 60, and a penalty of 0.
	 * <p>All of the data points are separated by the "|" symbol. The method splits the string using
	 * that symbol as a regex, and parses all the resulting strings for their respective values.
	 * 
	 * @param saveString A string loaded from a text file.
	 * @throws SaveStringErrorException Thrown if there is an error in the save string.
	 */
	public void parseSaveString(String saveString) throws SaveStringErrorException{
		// This is where it would check the save string for errors
		
		String[] data = saveString.split("\\|");	// Split the string using the pipes (|) character as a regex
		matchField = data[0];						// Set the field number to the first value loaded
		type = (data[1].equals("q"))? MATCHTYPE_QUALIFICATION : MATCHTYPE_FINAL;	// Set the match type as given by the file
		redScore = Integer.parseInt(data[6]);		// Load the red alliance score
		redPenalties = Integer.parseInt(data[7]);	// Load the red alliance penalties
		blueScore = Integer.parseInt(data[8]);		// Load the blue alliance score
		bluePenalties = Integer.parseInt(data[9]);	// Load the blue alliance penalties
		
		// TEMPORARY
		// Add four fake teams to the list
		// Why????
		matchTeams.add(new Team(1000));
		matchTeams.add(new Team(1000));
		matchTeams.add(new Team(1000));
		matchTeams.add(new Team(1000));
	}
	
	/**
	 * This Exception class is thrown if there is some sort of issue with the save 
	 * string being loaded from file.
	 *  
	 * @author Jonathan Thomas
	 */
	public class SaveStringErrorException extends Exception{
		/**
		 * We need this for some reason
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Print an error message to the console.
		 */
		public void showErrorMessage(){
			cons.printConsoleLine("Error: Corrupted save string");
		}
	}
	
	/**
	 * This Exception class is thrown if too many teams are being added to a match.
	 * A match must contain exactly 4 teams, 2 per alliance.
	 * 
	 * @author Jonathan Thomas
	 */
	public class TooManyTeamsException extends Exception{
		/**
		 * We need this for some reason
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Print an error message to the console.
		 */
		public void showErrorMessage(){
			cons.printConsoleLine("Too many teams entered");
		}
	}
	
	/**
	 * This Exception class is thrown if too few teams are being added to a match.
	 * A match must contain exactly 4 teams, 2 per alliance.
	 * 
	 * @author Jonathan Thomas
	 *
	 */
	public class TooFewTeamsException extends Exception{
		/**
		 * We need this for some reason
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Print an error message to the console.
		 */
		public void showErrorMessage(){
			cons.printConsoleLine("Too few teams entered");
		}
	}
	
	public static void main(String[] args) {
		Team[] teamsArray = new Team[4];
		teamsArray[Match.RED_1] = new Team(5029, "Powerstackers");
		teamsArray[Match.RED_2] = new Team(4251, "Cougar Robotics");
		teamsArray[Match.BLUE_1] = new Team(5501, "USS Enterprise");
		teamsArray[Match.BLUE_2] = new Team(5035, "Some random team");
		
		Match newMatch = new Match(teamsArray);
		
		System.out.println("Setting red score to 50");
		newMatch.setRedScore(50);
		System.out.println(newMatch.getRedScore());
		
		System.out.println("red 1 is " + newMatch.getTeam(RED_1).toString());
		System.out.println(newMatch.toSaveString());
		
		String saveString = newMatch.toSaveString();
		Match secondMatch = new Match();
		try {
			secondMatch.parseSaveString(saveString);
		} catch (SaveStringErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("NEW MATCH:\n"  + secondMatch.toSaveString());

	}

}
