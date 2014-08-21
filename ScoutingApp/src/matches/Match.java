package matches;
import java.util.ArrayList;
import java.util.Date;

import teams.Team;


public class Match {

	// Match data to store. Date, field number, match number, type of match, and teams involved
	Date matchDate = new Date();
	private int matchNumber;
	private String matchField = "1A";
	private ArrayList<Team> matchTeams = new ArrayList<Team>();
	private boolean type = true;
	
	// Constants to identify alliance members, match types
	public static final int RED_1 = 0;
	public static final int RED_2 = 1;
	public static final int BLUE_1 = 2;
	public static final int BLUE_2 = 3;
	public static final boolean MATCHTYPE_QUALIFICATION = true;
	public static final boolean MATCHTYPE_FINAL = false;
	public static final int RED_WINS = 1;
	public static final int BLUE_WINS = 2;
	
	// Rend and blue scores and penalties
	private int redScore = 0;
	private int blueScore = 0;
	private int redPenalties = 0;
	private int bluePenalties = 0;
	
	/**
	 * Empty constructor that does not set anything. Use parseSaveString to fill object
	 */
	public Match(){
		
	}
	
	/**
	 * Constructor that takes a list of teams. Match type and number default.
	 * @param matchTeams
	 */
	public Match(Team[] matchTeams){
		this.type = MATCHTYPE_QUALIFICATION;
		this.matchNumber = 1;
		try {
			setTeams(matchTeams);
		} catch (TooManyTeamsException | TooFewTeamsException e) {
				((TooManyTeamsException) e).showErrorMessage();
		}
	}
	
	/**
	 * Constructor that takes a list of teams and a match type. Match number defaults.
	 * @param matchTeams
	 * @param type
	 */
	public Match(Team[] matchTeams, boolean type){
		this.type = type;
		this.matchNumber = 1;
		try {
			setTeams(matchTeams);
		} catch (TooManyTeamsException | TooFewTeamsException e) {
			((TooManyTeamsException) e).showErrorMessage();
		}
	}
	
	/**
	 * Constructor that takes a list of teams, a match type, and a match number
	 * @param matchTeams
	 * @param type
	 * @param matchNumber
	 */
	public Match(Team[] matchTeams, boolean type, int matchNumber){
		this.type = type;
		this.matchNumber = matchNumber;
		try {
			setTeams(matchTeams);
		} catch (TooManyTeamsException | TooFewTeamsException e) {
			((TooManyTeamsException) e).showErrorMessage();
		}
	}
	
	public void setMatchNumber(int matchNumber){
		this.matchNumber = matchNumber;
	}
	
	public int getMatchNumber(){
		return matchNumber;
	}
	
	public void setMatchDate(Date date){
		matchDate = date;
	}
	
	public Date getMatchDate(){
		return matchDate;
	}
	
	public void setMatchField(String matchField){
		this.matchField = matchField;
	}
	
	public String getMatchField(){
		return matchField;
	}
	
	/**
	 * Set the list of teams. Throws exceptions if any number of teams other than 4 is provided.
	 * @param matchTeams
	 * @throws TooManyTeamsException
	 * @throws TooFewTeamsException
	 */
	public void setTeams(Team[] matchTeams) throws TooManyTeamsException, TooFewTeamsException{
		if(matchTeams.length > 4)
			throw new TooManyTeamsException();
		if(matchTeams.length < 4)
			throw new TooFewTeamsException();
		
		for(int i = 0; i < 4; i++)
			this.matchTeams.add(matchTeams[i]);	
	}
	
	/**
	 * Sets a specific team in a specific alliance position.
	 * @param index
	 * @param newTeam
	 */
	public void setTeam(int index, Team newTeam){
		matchTeams.set(index, newTeam);
	}
	
	public ArrayList<Team> getTeams(){
		return matchTeams;
	}
	
	public Team getTeam(int index){
		return matchTeams.get(index);
	}
	
	public void setRedScore(int score){
		redScore = score;
	}
	
	public int getRedScore(){
		return redScore;
	}
	
	public void setBlueScore(int score){
		blueScore = score;
	}
	
	public int getBlueScore(){
		return blueScore;
	}
	
	public void setRedPenalties(int penalties){
		redPenalties = penalties;
	}
	
	public int getRedPenalties(){
		return redPenalties;
	}
	
	public void setBluePenalties(int penalties){
		bluePenalties = penalties;
	}
	
	public int getBluePenalties(){
		return bluePenalties;
	}
	
	public int getRedFinalScore(){
		return redScore - redPenalties;
	}
	
	public int getBlueFinalScore(){
		return blueScore - bluePenalties;
	}
	
	public void setMatchType(boolean type){
		this.type = type;
	}
	
	public boolean getMatchType(){
		return type;
	}
	
	public int getWinner(){
		return (getRedFinalScore() > getBlueFinalScore())? RED_WINS : BLUE_WINS;
	}
	
	/**
	 * Returns the match type and number in a formatted string
	 * @return
	 */
	public String getMatchTitle(){
		return ((type)? "Qualification " : "Finals ") + " match #" + matchNumber;
	}
	
	/**
	 * Creates a string with all match data, for saving in text files
	 * @return
	 */
	public String toSaveString(){
		return matchField + "|" + ((type)?"q":"f")+ "|" +
				matchTeams.get(0).getTeamNumber() + "|" + matchTeams.get(1).getTeamNumber() + "|" + matchTeams.get(2).getTeamNumber() + "|"
				+ matchTeams.get(3).getTeamNumber() + "|" + redScore + "|" + redPenalties + "|" + blueScore + "|" + bluePenalties;
	}
	
	public void parseSaveString(String saveString) throws SaveStringErrorException{
		// This is where it would check the save string for errors
		
		String[] data = saveString.split("\\|");
		matchField = data[0];
		type = (data[1].equals("q"))? MATCHTYPE_QUALIFICATION : MATCHTYPE_FINAL;
		redScore = Integer.parseInt(data[6]);
		redPenalties = Integer.parseInt(data[7]);
		blueScore = Integer.parseInt(data[8]);
		bluePenalties = Integer.parseInt(data[9]);
		
		// TEMPORARY
		matchTeams.add(new Team(1000));
		matchTeams.add(new Team(1000));
		matchTeams.add(new Team(1000));
		matchTeams.add(new Team(1000));
	}
	
	public class SaveStringErrorException extends Exception{
		public void showErrorMessage(){
			System.out.println("Error: Corrupted save string");
		}
	}
	
	/**
	 * An exception for if too many teams are given to add
	 * @author Jonathan
	 *
	 */
	public class TooManyTeamsException extends Exception{
		public void showErrorMessage(){
			System.out.println("Too many teams entered");
		}
	}
	
	/**
	 * An exception for if too few teams are given to add
	 * @author Jonathan
	 *
	 */
	public class TooFewTeamsException extends Exception{
		public void showErrorMessage(){
			System.out.println("Too few teams entered");
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
