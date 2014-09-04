package tournaments;

public class Award {
	public static final int INSPIRE_AWARD = 0;
	public static final int THINK_AWARD = 1;
	public static final int CONNECT_AWARD = 2;
	public static final int INNOVATE_AWARD = 3;
	public static final int PTC_DESIGN_AWARD = 4;
	public static final int MOTIVATE_AWARD = 5;
	public static final int CONTROL_AWARD = 6;
	public static final int PROMOTE_AWARD = 7;
	public static final int COMPASS_AWARD = 8;
	public static final int JUDGES_AWARD = 9;
	public static final int WINNING_ALLIANCE = 10;
	public static final int FINALIST_ALLIANCE = 11;
	
	/**
	 * A list of all the award names. Use awardType to find the right title.
	 */
	public static final String[] awardTitles = {
		"Inspire Award",
		"Think Award",
		"Connect Award",
		"Rockwell Collins Innovate Award",
		"PTC Design Award",
		"Motivate Award",
		"Control Award",
		"Promote Award",
		"Compass Award",
		"Judges' Award",
		"Winning Alliance",
		"Finalist Alliance"
	};	
	
	/**
	 * Store the type of the award. Defaults to -1.
	 */
	private int awardType = -1;
	
	/**
	 * Creates an award, and stores its type.
	 * @param awardType
	 */
	public Award(int awardType){
		this.awardType = awardType;
	}
	
	/**
	 * Set the type of award.
	 * @param awardType
	 */
	public void setAwardType(int awardType){
		this.awardType = awardType;
	}
	
	/**
	 * Returns the type of award
	 * @return
	 */
	public int getAwardType(){
		return awardType;
	}
}
