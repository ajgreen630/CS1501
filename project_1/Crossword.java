// ==================================================
// Student Submission Information:
// ==================================================
// Name: Alexi Green
// MyPitt Username: AJG143
// MyPitt E-mail: ajg143@pitt.edu
// PeoplesoftID: 4193629
// Course Information: CS 1501 Section 19302
// ==================================================
import java.io.*;
import java.util.*;

public class Crossword {
	// Background solutions counter:
	private static int sols = 0;
	// NOTE: Static declaration is to avoid a convoluted
	// amount of argument passing between methods and to
	// easily increment the number of solutions.

	public static void main(String args[]) throws IOException
	{
		// Starting point for time elapsed.
		long startTime = System.currentTimeMillis();

		// Read in dictionary file:
		Scanner fileScan = new Scanner(new FileInputStream(args[0]));
		// Put words from dictionary file into trie:
		TrieSTNew<String> D = new TrieSTNew<String>();
		String st;
		while(fileScan.hasNext())
		{
			st = fileScan.nextLine();
			D.put(st, st);
		}

		// Read in crossword board:
		fileScan = new Scanner(new FileInputStream(args[1]));
		int limit = Integer.parseInt(fileScan.nextLine());

		Cell[][] myBoard = new Cell[limit][limit];
		int row = 0;
		int col = 0;
		while(fileScan.hasNext())
		{
			st = fileScan.nextLine();
			char[] ch = st.toCharArray();

			for(char c : ch)
			{
				myBoard[row][col] = new Cell(row, col, c);
				// Set the accessibility of the cell:
				myBoard[row][col].setAccess();
				col++;
			}
			row++;
			col = 0;
		}

		// Decrement the limit to accomodate for array coordinates (will be used as parameter in
		// other methods):
		limit--;
		// Array of all normal letters:
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		// Find the first available cell on the board:
		boolean stop = false;
		row = 0;
		col = 0;
		for(int i = 0; i < myBoard.length && !stop; i++)
		{
			for(int j = 0; j < myBoard[i].length && !stop; j++)
			{
				if(myBoard[i][j].isAccess())
				{
					row = myBoard[i][j].getRow();
					col = myBoard[i][j].getColumn();
					
					stop = true;
				}
			}
		}

		// Print the board that was just read in:
		System.out.println("Starting board:");
		printBoard(myBoard);
		System.out.println();

		// If none of the blocks on the grid are accessible:
		if(!stop)
		{
			System.out.println("No available spaces on the board!");
			long endTime = System.currentTimeMillis();
			float estTime = (endTime - startTime)/ 1000F;
			System.out.println(sols + " solution(s) found in " + estTime + " seconds.");
			System.exit(0);
		}

		solvePuzzle(D, myBoard, alphabet, limit, row, col);

		long endTime = System.currentTimeMillis();
		float estTime = (endTime - startTime)/ 1000F;
		System.out.println("Found " + sols + " solution(s) in " + estTime + " seconds.");
	}

	public static void solvePuzzle(TrieSTNew<String> dict, Cell[][] board, char[] alphabet, int limit, int row, int col)
	{
		//printBoard(board);
		//System.out.println();
		// If the cell is not accessible and has a fixed character (i.e., not a black space), check if the string up through that
		// cell is valid:
		if(!board[row][col].isAccess() && board[row][col].getCharacter() != '-')
		{
			if(checkCell(dict, board, limit, row, col))
			{
					// If last cell:
					if(col == limit && row == limit)
					{
						sols++;
						if((sols - 1) % 10000 == 0)
						{
							System.out.println("Solution " + (sols - 1) + " Found:");
							printBoard(board);
							System.out.println();
						}
					}
					// If on last column but not last row, recurse:
					else if(col == limit && row != limit)
					{
						solvePuzzle(dict, board, alphabet, limit, row + 1, 0);
					}
					// If on last row but not last column OR not last row and not last column, recurse:
					else if((row == limit && col != limit) || (row != limit && col != limit))
					{
						solvePuzzle(dict, board, alphabet, limit, row, col + 1);
					}
			}
		}
		// If the cell is not accessible and is a black space:
		else if(!board[row][col].isAccess() && board[row][col].getCharacter() == '-')
		{
			// If on the last cell, increment solutions:
			if(row == limit && col == limit)
			{
				sols++;
				if((sols - 1) % 10000 == 0)
				{
					System.out.println("Solution " + (sols - 1) + " Found:");
					printBoard(board);
					System.out.println();
				}
			}
			// If on last column but not last row, recurse:
			else if(col == limit && row != limit)
			{
				solvePuzzle(dict, board, alphabet, limit, row + 1, 0);
			}
			// If on last row or not last row and not last column, recurse:
			else if((row == limit && col != limit) || (row != limit && col != limit))
			{
				solvePuzzle(dict, board, alphabet, limit, row, col + 1);
			}
		}
		
		// Otherwise, assume cell is accesible:
		else
		{
			for(int i = 0; i < alphabet.length; i++)
			{
				// Set content of cell to current character in alphabet:
				board[row][col].setCharacter(alphabet[i]);

				// Determine if character can be legally placed.
				// If checkCell returns true:
				if(checkCell(dict, board, limit, row, col))
				{
					// If last cell, increment solutions:
					if(col == limit && row == limit)
					{
						sols++;
						if((sols - 1) % 10000 == 0)
						{
							System.out.println("Solution " + (sols - 1) + " Found:");
							printBoard(board);
							System.out.println();
						}
					}
					// If on last column but not last row, recurse:
					else if(col == limit && row != limit)
					{
						solvePuzzle(dict, board, alphabet, limit, row + 1, 0);
					}
					// If on last row but not last column OR not last row and not last column, recurse:
					else if((row == limit && col != limit) || (row != limit && col != limit))
					{
						solvePuzzle(dict, board, alphabet, limit, row, col + 1);
					}
				}
			}	
		}
		// Reset cell:
		if(board[row][col].isAccess())
		{
			board[row][col].setCharacter('+');
		}
	}

	public static boolean checkCell(TrieSTNew<String> dict, Cell[][] board, int limit, int row, int col)
	{
		// If both checkRow and checkColumn return true:
		if(checkRow(dict, board, limit, row, col) && checkCol(dict, board, limit, row, col))
		{
			return true;
		}
		// Otherwise:
		else
		{
			return false;
		}
	}
	public static boolean checkRow(TrieSTNew<String> dict, Cell[][] board, int limit, int row, int col)
	{
		StringBuilder sb = new StringBuilder(limit + 1);
		for(int i = 0; i <= col; i++)
		{
			// If there is a black square before the cell we are focusing on,
			// start a new StringBuilder:
			if(board[row][i].getCharacter() == '-' && i <= col)
			{
				sb = new StringBuilder(limit + 1);
			}

			// If there is a black square or an empty space after the cell we are focusing on,
			// break:
			else if((board[row][i].getCharacter() == '-' || board[row][i].getCharacter() == '+') && i > col)
			{
				break;
			}
			// Otherwise, append from left-to-right up until we have appended
			// the letter in the current cell:
			else
			{
				sb.append(board[row][i].getCharacter());
			}
		}
		//System.out.println(sb.toString());
		int isPrefix = dict.searchPrefix(sb.toString());
		//System.out.println(sb.toString()+ " " + isPrefix);

		// Handling if string in the row we are on is cut short by a '-':
		if(col != limit)
		{
			if(board[row][col+1].getCharacter() == '-')
			{
				// If isPrefix == 0, sb is neither a prefix nor a word in the trie and we can return false.
				// If isPrefix == 1, sb is a prefix but is not an actual key and we can return false.
				if(isPrefix == 0 || isPrefix == 1)
				{
					return false;
				}
				// If isPrefix == 2, sb is in the trie but is not a prefix and we can return true.
				// If isPrefix == 3, sb is in the trie but is also a prefix to a longer word in the trie
				// and we can return true
				if(isPrefix == 2 || isPrefix == 3)
				{
					return true;
				}
			}
		}

		// If we are at the end of the row (sb must in the tree):
		if(col == limit)
		{
			// If isPrefix == 0, sb is neither a prefix nor a word in the trie and we can return false.
			// If isPrefix == 1, sb is a prefix but is not an actual key and we can return false.
			if(isPrefix == 0 || isPrefix == 1)
			{
				return false;
			}
			// If isPrefix == 2, sb is in the trie but is not a prefix and we can return true.
			// If isPrefix == 3, sb is in the trie but is also a prefix to a longer word in the trie
			// and we can return true.
			if(isPrefix == 2 || isPrefix == 3)
			{
				return true;
			}
		}
		// Otherwise, assume we are not at the end of the row:
		// If isPrefix == 0, sb is neither a prefix nor a word in the trie and we can return false.
		// If isPrefix == 2, sb is a word in the trie but it is not a prefix and we can return false.
		if(isPrefix == 0 || isPrefix == 2)
		{
			return false;
		}
		// If isPrefix == 1, sb is a prefix but is not an actual key and we can return true.
		// If isPrefix == 3, sb is in the trie but is also a prefix to a longer word in the trie
		// and we can return true. 
		if(isPrefix == 1 || isPrefix == 3)
		{
			return true;
		}

		// For 'missing return statement' prevention:
		return false;
	}

	public static boolean checkCol(TrieSTNew<String> dict, Cell[][] board, int limit, int row, int col)
	{
		StringBuilder sb = new StringBuilder(limit + 1);
		for(int i = 0; i <= row; i++)
		{
			// If there is a black square before the cell we are focusing on,
			// start a new StringBuilder:
			if(board[i][col].getCharacter() == '-' && i <= row)
			{
				sb = new StringBuilder(limit + 1);
			}

			// If there is a black square or an empty space after the cell we are focusing on,
			// break:
			else if((board[i][col].getCharacter() == '-' || board[i][col].getCharacter() == '+') && i > row)
			{
				break;
			}
			// Otherwise, append from left-to-right up until we have appended
			// the letter in the current cell:
			else
			{
				sb.append(board[i][col].getCharacter());
			}
		}
		//System.out.println(sb.toString());
		int isPrefix = dict.searchPrefix(sb.toString());
		//System.out.println(sb.toString()+ " " + isPrefix);

		// Handling if string in the column we are on is cut short by a '-':
		if(row != limit)
		{
			if(board[row+1][col].getCharacter() == '-')
			{
				// If isPrefix == 0, sb is neither a prefix nor a word in the trie and we can return false.
				// If isPrefix == 1, sb is a prefix but is not an actual key and we can return false.
				if(isPrefix == 0 || isPrefix == 1)
				{
					return false;
				}
				// If isPrefix == 2, sb is in the trie but is not a prefix and we can return true.
				// If isPrefix == 3, sb is in the trie but is also a prefix to a longer word in the trie
				// and we can return true.
				if(isPrefix == 2 || isPrefix == 3)
				{
					return true;
				}
			}
		}

		// If we are at the end of the column (sb must in the tree):
		if(row == limit)
		{
			// If isPrefix == 0, sb is neither a prefix nor a word in the trie and we can return false.
			// If isPrefix == 1, sb is a prefix but is not an actual key and we can return false.
			if(isPrefix == 0 || isPrefix == 1)
			{
				return false;
			}
			// If isPrefix == 2, sb is in the trie but is not a prefix and we can return true.
			// If isPrefix == 3, sb is in the trie but is also a prefix to a longer word in the trie
			// and we can return true.
			// Q: At the end of a row, can we return true as long as the prefix is in the trie?
			if(isPrefix == 2 || isPrefix == 3)
			{
				return true;
			}
		}

		// Otherwise, assume we are not at the end of the row:
		// If isPrefix == 0, sb is neither a prefix nor a word in the trie and we can return false.
		// If isPrefix == 2, sb is a word in the trie but it is not a prefix and we can return false.
		if(isPrefix == 0 || isPrefix == 2)
		{
			return false;
		}
		// If isPrefix == 1, sb is a prefix but is not an actual key and we can return true.
		// If isPrefix == 3, sb is in the trie but is also a prefix to a longer word in the trie
		// and we can return true. 
		if(isPrefix == 1 || isPrefix == 3)
		{
			return true;
		}

		// For 'missing return statement' prevention:
		return false;
	}

	public static void printBoard(Cell[][] board)
	{
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[i].length; j++)
			{
				System.out.print(board[i][j].toString());
			}
			System.out.println();
		}
	}
}