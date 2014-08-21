package matches;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * JTable that stores a list of matches. Can load a list of matches from a text file.
 * @author Jonathan
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
	 * Constructor that sets the renderer for the JTable to the custom renderer, and adds all the matches in the parameter array to the list.
	 * @param matches
	 */
	public MatchesList(Match[] matches){
		table.setDefaultRenderer(Object.class, new MatchTableCellRederer());
		for(int i = 0; i < matches.length; i++){
			addMatch(matches[i]);
		}
		refreshMatchesTable();
	}
	
	/**
	 * Load a list of matches from a text file (empty).
	 */
	public void loadFromFile(){
		
	}
	
	/**
	 * Update the JTable with the matches currently in the list.
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
			rowColors.add((list.get(i).getWinner() == Match.RED_WINS)? Color.RED : Color.BLUE);
		}
		
		// Create a new ListModel from the data we just collected.
		MatchesListModel model = new MatchesListModel(matchesList, columnHeaders, rowColors);
		
		// Set the JTable's model to the one we just created.
		table.setModel(model);
	}
	
	/**
	 * Add a match to the list.
	 * @param match
	 */
	public void addMatch(Match match){
		list.add(match);
		refreshMatchesTable();
	}
	
	/**
	 * Remove a match from the list.
	 * @param index
	 */
	public void removeMatch(int index){
		list.remove(index);
		refreshMatchesTable();
	}
	
	/**
	 * Return the match at the specified position in the list.
	 * @param index
	 * @return
	 */
	public Match getMatchAt(int index){
		return list.get(index);
	}
	
	public int getSelectedMatch(){
		return (table.getSelectedRow() == -1)? 0 : table.getSelectedRow();
	}
	
	public ArrayList<Match> getList(){
		return list;
	}
	
	private class MatchesListModel extends AbstractTableModel{
		
		ArrayList<Object[]> list;
		String[] columnHeaders;
		ArrayList<Color> rowColors = new ArrayList<Color>();
		
		public MatchesListModel(Object[][] list, String[] columnHeaders, ArrayList<Color> rowColors){
			this.columnHeaders = columnHeaders;
			this.list = new ArrayList<Object[]>();
			for(int i = 0; i < list.length; i++){
				this.list.add(list[i]);
				this.rowColors = rowColors;
			}	
		}
		
		public void setRowColor(int index, Color color){
			rowColors.set(index, color);
		}
		
		public Color getRowColor(int index){
			return rowColors.get(index);
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnHeaders.length;
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return list.get(rowIndex)[columnIndex];
		}
		
		public String getColumnName(int index){
			return columnHeaders[index];
		}
	}
	
	static class MatchTableCellRederer extends DefaultTableCellRenderer {

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	    	MatchesListModel model = (MatchesListModel) table.getModel();
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
