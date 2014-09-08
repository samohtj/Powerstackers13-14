package userinterface;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class CustomListModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// A list of Object arrays that will store the team name and number in String form.
	ArrayList<Object[]> list;
			
	// An array of Strings to store the column headers that will be displayed on the JScrollPane.
	String[] columnHeaders;
	
	// A list to store the colors of the rows
	ArrayList<Color> rowColors = new ArrayList<Color>();
			
	/**
	* Constructor that takes a list of team name and number Strings, and a list of column title Strings.
	* @param list
	* @param columnHeaders
	*/
	public CustomListModel(Object[][] list, String[] columnHeaders){
		this.columnHeaders = columnHeaders;
		this.list = new ArrayList<Object[]>();
		for(int i = 0; i < list.length; i++){
			this.list.add(list[i]);
		}
	}
	
	public CustomListModel(Object[][] list, String[] columnHeaders, ArrayList<Color> rowColors){
		this(list, columnHeaders);
		this.rowColors = rowColors;
	}
			
	/**
	* Return the number of columns.
	*/
	@Override
	public int getColumnCount() {
		return columnHeaders.length;
	}
		
	/**
	 * Return the number of rows.
	 */
	@Override
	public int getRowCount() {
		return list.size();
	}
	
	/**
	 * Return the team name and number stored at said position in the JTable
	 * @param rowIndex, columnIndex
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return list.get(rowIndex)[columnIndex];
	}
		
	/**
	 * Return the column header at said index.
	 * @param index
	 */
	public String getColumnName(int index){
		return columnHeaders[index];
	}

	public void setRowColor(int index, Color color){
		rowColors.set(index, color);
	}
	
	public Color getRowColor(int index){
		return rowColors.get(index);
	}

}
