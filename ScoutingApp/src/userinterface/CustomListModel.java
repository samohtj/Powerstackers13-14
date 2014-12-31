package userinterface;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * This is a custom list model class. Use this to create JScrollPane objects with named columns.
 * This class also stores the background color of the row, in case you want to change it dynamically.
 * 
 * @author Jonathan Thomas
 */
public class CustomListModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// A list of Object arrays that will store the team name and number in String form.
	private ArrayList<Object[]> list;
			
	// An array of Strings to store the column headers that will be displayed on the JScrollPane.
	private String[] columnHeaders;
	
	// A list to store the colors of the rows
	private ArrayList<Color> rowColors = new ArrayList<Color>();
			
	/**
	* Create a new CustomListModel, with a two-dimensional array of objects, and a list of column header names.
	* The constructor will create an ArrayList containing all the elements of the list array. Each single-dimensional
	* array will become its own object in the ArrayList.
	* <p>The first dimension of the list array are the columns of your JScrollPane. Each column has its own array element.
	* The second dimension is an array containing the row's contents. An example:
	* <blockquote>
	* {"5029", "Powerstackers"}<br>
	* {"5501", "USS Enterprise"}</blockquote>
	* This will create a JScrollPane with two rows and two columns. You can create any number of rows that you want, and
	* any number of columns.
	* 
	* @param list A two-dimensional array of Objects. The first dimension is the rows, the second is the columns.
	* @param columnHeaders An array of Strings containing the titles of the columns.
	*/
	public CustomListModel(Object[][] list, String[] columnHeaders){
		this.columnHeaders = columnHeaders;
		this.list = new ArrayList<Object[]>();
		for(int i = 0; i < list.length; i++){
			this.list.add(list[i]);
		}
	}
	
	/**
	* Create a new CustomListModel, with a two-dimensional array of objects, and a list of column header names.
	* The constructor will create an ArrayList containing all the elements of the list array. Each single-dimensional
	* array will become its own object in the ArrayList.
	* <p>The first dimension of the list array are the columns of your JScrollPane. Each column has its own array element.
	* The second dimension is an array containing the row's contents. An example:
	* <blockquote>
	* {"5029", "Powerstackers"}<br>
	* {"5501", "USS Enterprise"}</blockquote>
	* This will create a JScrollPane with two rows and two columns. You can create any number of rows that you want, and
	* any number of columns. 
	* <p>The <b>rowColors</b> argument takes an ArrayList of Color objects. Each Color corresponds to a row. You can use this
	* list to change the background color of a row based on its contents.
	* 
	* @param list
	* @param columnHeaders
	* @param rowColors
	*/
	public CustomListModel(Object[][] list, String[] columnHeaders, ArrayList<Color> rowColors){
		this(list, columnHeaders);
		this.rowColors = rowColors;
	}
			
	/**
	* @return The number of columns in the JScrollPane.
	*/
	@Override
	public int getColumnCount() {
		return columnHeaders.length;
	}
		
	/**
	 * @return The number of rows in the JScrollPane.
	 */
	@Override
	public int getRowCount() {
		return list.size();
	}
	
	/**
	 * @return The object at the specified row and column.
	 * @param rowIndex, columnIndex
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return list.get(rowIndex)[columnIndex];
	}
		
	/**
	 * @return The title of the specified column.
	 * @param index
	 */
	public String getColumnName(int index){
		return columnHeaders[index];
	}

	/**
	 * Set the background color of the specified column.
	 * 
	 * @param index The index of the column.
	 * @param color The color to use.
	 */
	public void setRowColor(int index, Color color){
		rowColors.set(index, color);
	}
	
	/**
	 * 
	 * @param index
	 * @return The color of the specified row.
	 */
	public Color getRowColor(int index){
		return rowColors.get(index);
	}

}
