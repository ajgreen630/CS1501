// ==================================================
// Student Submission Information:
// ==================================================
// Name: Alexi Green
// MyPitt Username: AJG143
// MyPitt E-mail: ajg143@pitt.edu
// PeoplesoftID: 4193629
// Course Information: CS 1501 Section 19302
// ==================================================
// This class forms the data structure for a single cell
// within a crossword grid.
public class Cell {

	// Attributes:
	private int row;
	private int column; 
	private char character;
	// By default, access will be initialized to
	// false.
	private boolean access;

	// Constructor:
	public Cell (int r, int c, char ch)
	{
		row = r;
		column = c;
		character = ch;
	}
	// ==============================================================================
	// Getters:
	// ==============================================================================
	// getRow(): Returns the row coordinate of the cell:
	public int getRow()
	{
		return row;
	}
	// getColumn(): Returns the column coordinate of the cell.
	public int getColumn()
	{
		return column;
	}
	// getCharacter(): Returns the current character within the cell.
	public char getCharacter()
	{
		return character;
	}
	// isAccess(): Returns the accessibility (i.e., whether or not the
	// contents of the cell can be changed by the program) of 
	// the cell (true if the cell can be changed, false otherwise).
	public boolean isAccess()
	{
		return access;
	}
	// ==============================================================================
	// Setters:
	// ==============================================================================
	// setCharacter(): Sets the character within the cell if it is accessible.
	public void setCharacter(char ch)
	{
		// Placed this condition as a precaution to avoid errors where
		// fixed cells might be changed.
		if(isAccess())
		{
			character = ch;
		}
	}
	// setAccess(): Sets the accesibility of the cell to
	// true if the its character is a '+'.
	public void setAccess()
	{
		if(this.getCharacter() == '+')
		{
			access = true;
		}
	}
	// ==============================================================================
	// Other:
	// ==============================================================================
	public String toString()
	{
		String s = Character.toString(character);
		return s;
	}
}