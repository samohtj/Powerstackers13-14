package matches;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import userinterface.CustomListModel;

/**
 * Create a list of Match objects. Teams can be added to the list, as well
 * as removed from it. Their information can also be directly edited. The 
 * class stores the teams in an ArrayList object, which itself populates a
 * JTable element. The JTable can be displayed in any Swing application.
 * <blockquote><b>list</b>: The list is the ArrayList object containing the teams 
 * participating in the match. The teams in the list can be edited with the
 * MatchList class' methods.
 * <p><b>table</b>: The table is the JTable object that displays all the teams
 * in the list. The table has three columns:
 * <ul>
 * <li><b>Match #</b>: The number of the match.
 * <li><b>Red Score</b>: The final score of the red alliance in that match.
 * <li><b>Blue Score</b>: The final score of the blue alliance in that match.
 * </ul>
 * </blockquote>
 * 
 * The class also contains methods for adding and removing teams, as well as a
 * custom renderer class to change the colors of the rows in the table based on
 * the winner of the match. The column will change to the color of the winning alliance.
 * 
 * @author Jonathan Thomas
 *
 */
public class MatchesList {
	
	// Create an ArrayList to store the matches, and a JTable to visually display it.
	private ArrayList<Match> list = new ArrayList<Match>();
	public JTable table = new JTable();
	
	/**
	 * Constructor that sets the renderer for the JTable to the custom renderer.
	 */
	public MatchesList(){
		table.setDefaultRenderer(Object.class, new MatchTableCellRederer());
	}
	
	/**
	 * Constructor that sets the renderer for the JTable to the custom renderer, and adds all
	 * the matches in the parameter array to the list.
	 * 
	 * @param matches An array of Match objects that will populate the list with matches.
	 */
	public MatchesList(Match[] matches){
		table.setDefaultRenderer(Object.class, new MatchTableCellRederer());
		for(int i = 0; i < matches.length; i++){
			addMatch(matches[i]);
		}
		refreshMatchesTable();
	}
	
	/**
	 * Load a list of matches from a text file.
	 * <p> This method is a WIP
	 */
	public void loadFromFile(){
		
	}
	
	/**
	 * This method reloads all the matches in the <b>list</b> into the <b>table</b>.
	 * It must be called every time a change is made to the list.
	 * 
	 */
	private void refreshMatchesTable(){
		// A String array of column headers, and an Object two-dimensional array of match numbers and scores.
		String[] columnHeaders = {"Match #", "Red Score", "Blue Score"};
		Object[][] matchesList = new Object[list.size()][3];
		
		// An ArrayList to store the selected color for each row.
		ArrayList<Color> rowColors = new ArrayList<Color>();
		
		// For each match in the list, set the row contents to the match data, and decide which color the row should be based on the scores.
		for(int i = 0; i < list.size(); i++){
			String[] matchData = {Integer.toString(list.get(i).getMatchNumber()), Integer.toString(list.get(i).getRedFinalScore()),
					Integer.toString(list.get(i).getBlueFinalScore())};
			matchesList[i] = matchData;
			//rowColors.add((list.get(i).getWinner() == Match.RED_WINS)? Color.RED : Color.BLUE);
			if(list.get(i).getWinner() == Match.RED_WINS){
				rowColors.add((table.getSelectedRow() == i)? Color.PINK : Color.RED);
				System.out.println("Selected row: " + table.getSelectedRow()  + "\nIndex: " + i);
			}else{
				rowColors.add((table.getSelectedRow() == i)? Color.CYAN : Color.BLUE);
				System.out.println("Selected row: " + table.getSelectedRow()  + "\nIndex: " + i);
			}
		}
		
		// Create a new ListModel from the data we just collected and add it to the table
		CustomListModel model = new CustomListModel(matchesList, columnHeaders, rowColors);
		table.setModel(model);
		/*
		table.getColumnModel().getColumn(0).setMinWidth(25);
		table.getColumnModel().getColumn(0).setMaxWidth(200);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setMinWidth(25);
		table.getColumnModel().getColumn(1).setMaxWidth(125);
		table.getColumnModel().getColumn(1).setPreferredWidth(25);
		table.getColumnModel().getColumn(2).setMinWidth(25);
		table.getColumnModel().getColumn(2).setMaxWidth(125);
		table.getColumnModel().getColumn(2).setPreferredWidth(25);
		*/	
	}
	
	/**
	 * Add a match to the list.
	 * 
	 * @param match The Match object that you want to add.
	 */
	public void addMatch(Match match){
		list.add(match);
		refreshMatchesTable();
	}
	
	/**
	 * Remove a match from the list.
	 * 
	 * @param index The position of the Match in the list that you want to remove.
	 */
	public void removeMatch(int index){
		list.remove(index);
		refreshMatchesTable();
	}
	
	/**
	 * Return the match at the specified position in the list.
	 * 
	 * @param index The position in the list of the match that you want.
	 * @return A Match object.
	 */
	public Match getMatchAt(int index) throws IndexOutOfBoundsException{
		return list.get(index);
	}
	
	/**
	 * Gets the match that is currently selected. Selection is based on the selected row of the table.
	 * 
	 * @return The match that is currently selected.
	 */
	public int getSelectedMatch(){
		return (table.getSelectedRow() == -1)? 0 : table.getSelectedRow();
	}
	
	/**
	 * Return the whole list of teams.
	 * 
	 * @return An ArrayList containing all the teams on the list.
	 */
	public ArrayList<Match> getList(){
		return list;
	}
	
	/**
	 * This class is a custom renderer for the table. It changes the color of the row based on the winner
	 * of the match. The color changes to match the color of the winning alliance. The class also 
	 * 
	 * @author Jonathan Thomas
	 */
	static class MatchTableCellRederer extends DefaultTableCellRenderer {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	    	CustomListModel model = (CustomListModel) table.getModel();
	        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        component.setBackground(model.getRowColor(row));
	        component.setForeground(Color.WHITE);
	        component.setFont(new Font("Sans", Font.BOLD, 16));
	        return component;
	    }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
