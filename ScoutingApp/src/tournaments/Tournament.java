package tournaments;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import matches.Match;
import matches.MatchesList;
import teams.Team;
import teams.TeamsList;
import userinterface.ConsoleWindow;

public class Tournament {
	
	public static String tournamentSavePath = "data/tournaments/";
	public static String tournamentSaveExt = "";

	private TeamsList roster = new TeamsList();
	private MatchesList matches = new MatchesList();
	
	private String eventName = "";
	private String eventLocation = "";
	
	@SuppressWarnings("unused")
	private ConsoleWindow cons = new ConsoleWindow();
	
	public void setEventName(String eventName){
		this.eventName = eventName;
	}
	
	public String getEventName(){
		return eventName;
	}
	
	public void setEventLocation(String eventLocation){
		this.eventLocation = eventLocation;
	}
	
	public String getEventLocation(){
		return eventLocation;
	}
	
	public void setRoster(TeamsList roster){
		this.roster = roster;
	}
	
	public TeamsList getRoster(){
		return roster;
	}
	
	public void setMatches(MatchesList matches){
		this.matches = matches;
	}
	
	public MatchesList getMatches(){
		return matches;
	}
	
	public void addTeam(Team team){
		roster.addTeam(team);
	}
	
	public void addMatch(Match match){
		matches.addMatch(match);
	}
	
	public void setConsoleWindow(ConsoleWindow cons){
		this.cons = cons;
	}
	
	public void saveData(){
		try{
			@SuppressWarnings("resource")
			PrintWriter writer = new PrintWriter(tournamentSavePath + eventName + tournamentSaveExt);
			writer.println("teamsList=");
		}catch(FileNotFoundException ex){
			
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
