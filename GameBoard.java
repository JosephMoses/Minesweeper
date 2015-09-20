import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class GameBoard extends JPanel implements ItemListener, MouseListener, ActionListener
{
	private Clock clock;
	private ImageIcon bombIcon;
	private ImageIcon flagIcon;
	private JButton saveButton;  
	private JButton newButton;   
	private JLabel flagLabel;
	private JLabel underneath; //bottom of the board
	private JPanel topPanel; //top of the board
	private JPanel toolBar; //panel for the timer, save button, and new game button
	private JPanel board;
	private JToggleButton [][] mineField;
	private Mines mine;
	private boolean done;
	private boolean isAWin; //check to see if the player won the game
	private boolean start; //check to see if the clock is started
	private boolean[][] clearedCells; // keeps up with the clicked and disabled locations
	private boolean[][] flagPosition; // keeps up with flag locations
	private boolean[][] storedCells; // locations waiting to be cleared
	private int bombCount;
	private int columns;
	private int rows;
	private int numFlags; 
	private int oldBombCount;
	private int[][] cellValue; //keeps track of how many bombs are in surrounding area to current location, if the cell is a bomb, the value is ten
	

	public GameBoard(int r, int c, int explosives)//constructor -- builds new game board
	{
		rows = r;
		columns = c;
		bombCount = explosives; 
		numFlags = explosives; //have to have the same number of flags as you do bombs
		oldBombCount = bombCount; //holds the place of the bomb number in case the player wants to save game
		clock = new Clock(); //lets the board have a timer
		
		
		bombIcon = new ImageIcon("explosion1.jpg"); //icon for the bombs
		
		mine = new Mines(); //place bombs on the board
		mine.bombPanel(rows, columns);
		mine.addBomb(rows, columns, explosives);
		clearedCells = new boolean[rows][columns]; //finds and keeps up with the clicked and disabled locations
		cellValue = new int[rows][columns]; //how many bombs are in range 
		flagPosition = new boolean[rows][columns]; //keep up with flag location
		setCellValues(rows, columns); //labels the board with how many bombs are in range
		
		// Create GUI field of buttons.
		board = this;
		board.setLayout(new BorderLayout());
		
		//set the dimension of the board
		int panelX = 45 * columns;
		int panelY = 35 * rows;
		board.setPreferredSize(new Dimension(panelX, panelY));
		
		flagLabel = new JLabel(" ");
		flagLabel.setText("Flag count: " + String.valueOf(numFlags)); //prints out the number of flags available
		underneath = new JLabel("");
		flagIcon = new ImageIcon("flag1.jpg");
		underneath.setIcon(flagIcon); //shows the flag icon
		
		// Create Save & New Game Buttons
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		newButton = new JButton("New Game");
		newButton.addActionListener(this);
		
		toolBar = new JPanel(); //holds the buttons
		toolBar.setLayout(new GridLayout(1,5));
		toolBar.add(clock);
		toolBar.add(underneath);
		toolBar.add(flagLabel);	//flag count
		toolBar.add(saveButton);
		toolBar.add(newButton);
		
		
		board.add(toolBar, BorderLayout.NORTH);
		
		board.repaint(); //draw the new board with correct sizing
		board.validate();
		
		mineField = new JToggleButton[rows][columns]; //the buttons you click in the game
		
		showButtons(rows, columns);
	}
	
	public GameBoard(Scanner s) //Constructor -- load previously saved game
	{
		rows = s.nextInt();	//set number of rows
		columns = s.nextInt(); //set number of columns
		bombCount = s.nextInt(); //set bomb count
		numFlags = bombCount; //set number of flags, which has to be same as bomb count
		String currentTime = s.next();	//set time to the previously saved game's time
		oldBombCount = numFlags;
		
		bombIcon = new ImageIcon("explosion1.jpg"); //bomb icon
		
		mine = new Mines();
		mine.bombPanel(rows, columns);
		
		clearedCells = new boolean[rows][columns]; //finds and keeps up with the clicked and disabled locations
		cellValue = new int[rows][columns]; //how many bombs are in range 
		flagPosition = new boolean[rows][columns]; //keep up with flag location
		

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				mine.placeBombs(s.next(),i, j); //place bombs in correct location
			}
		}
		
		setCellValues(rows, columns);  //labels the board with how many bombs are in range
		clock = new Clock(currentTime); //add timer with the time from the previously saved game
		clock.stop(); //stop the clock and wait for the player to clock on a space to start again
		start = false; //clock has not started
	
		board = this;
		board.setLayout(new BorderLayout());
		
		//set dimension for the board
		int panelX = 42 * columns;
		int panelY = 33 * rows;
		board.setPreferredSize(new Dimension(panelX, panelY));
		
		//create Save & New Game buttons
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		newButton = new JButton("New Game");
		newButton.addActionListener(this);
		underneath = new JLabel("");
		underneath.setIcon(flagIcon);
		flagIcon = new ImageIcon("flag1.jpg");
		flagLabel = new JLabel(" ");
		flagLabel.setText("Flag count: " + String.valueOf(numFlags));
		
		toolBar = new JPanel();
		toolBar.setLayout(new GridLayout(1, 5));
		toolBar.add(clock);
		toolBar.add(underneath);
		toolBar.add(flagLabel);	
		toolBar.add(saveButton);
		toolBar.add(newButton);
		
		mineField = new JToggleButton[rows][columns]; //the buttons you click in the game
		
		showButtons(rows, columns);
		
		for (int i=0;i<rows;i++) 
		{
			for (int j=0;j<columns;j++)
			{
				flagPosition[i][j]= Boolean.valueOf(s.next()); //sets flag location
				
				if(flagPosition[i][j] == true) //keep up with the number of flags left
				{
					mineField[i][j].setText("!");
					numFlags--;
					flagLabel.setText("Flag count: " + String.valueOf(numFlags)); 
				}
			}
		}
		
		for (int i=0;i<rows;i++) //sets the clicked and disabled locations
		{
			for (int j=0;j<columns;j++)
			{
				clearedCells[i][j]=Boolean.valueOf(s.next());
				if(clearedCells[i][j] == true)
				{
					clearCell(i, j);
				}
			}
		}
	}
	
	public void exit() //exit the game
	{
		System.exit(0);
	}
	
	public void setCellValues(int rows, int columns) //sets the number of surrounding bombs in each cell 
	{
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				if(mine.isBomb(i, j) == true) //if there is a bomb, set the value to -1
				{
					cellValue[i][j] = -1; 
				}
				else //set the number of surrounding bombs
				{
					int numCell = adjacentBombs(i, j);
					cellValue[i][j] = numCell;
				}
			}
		}
	}

	public void showButtons(int r, int c) //adds the buttons for the user to click in the game
	{
		numFlags = oldBombCount;
		
		if(topPanel != null)
		{
			board.remove(topPanel);
		}
		if(toolBar !=null)
		{	
			board.remove(toolBar);
		}
		
		//remake toolBar
		toolBar = new JPanel();
		toolBar.setLayout(new GridLayout(1, 5));
		toolBar.add(clock);
		toolBar.add(underneath);
		toolBar.add(flagLabel);	
		toolBar.add(saveButton);
		toolBar.add(newButton);
		
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(r, c));
		
		for(int i = 0; i < r; i++) //listeners for each button location on the board
		{
			for(int j = 0; j < c; j++)
			{
				mineField[i][j] = new JToggleButton();
				mineField[i][j].addMouseListener(this);
				mineField[i][j].addItemListener(this);
				topPanel.add(mineField[i][j]);
			}
		}
		
		board.add(toolBar, BorderLayout.NORTH);
		board.add(topPanel, BorderLayout.CENTER);
		
		board.repaint(); //makes sure the board shows up correctly 
		board.validate();
	}
	
	// Mouse Listener (deals with right-clicks)
	public void mouseClicked(MouseEvent event1)
	{}
	public void mousePressed(MouseEvent event1)
	{}
	public void mouseEntered(MouseEvent event1)
	{}
	public void mouseExited(MouseEvent event1)
	{}
	
	public void mouseReleased(MouseEvent event1)
	{
		if(event1.getModifiers() == InputEvent.BUTTON3_MASK) //right-click
		{
			JToggleButton select = (JToggleButton)event1.getSource();
					int x = 0;
					int y = 0;
					
			for(int i = 0; i < rows; i++)
			{
				for(int j = 0; j < columns; j++)
				{
					
					if(event1.getSource() == mineField[i][j])
					{
						x = i;
						y = j;
						
						if(mineField[x][y].getText() == "" && mineField[x][y].isSelected()==false && numFlags > 0)//sets a flag at current location
						{
							mineField[x][y].setText("!");
							numFlags--;
							flagLabel.setText("Flag count: " + String.valueOf(numFlags));
						}
						
						else if (mineField[x][y].isSelected() ==true) //location is disabled already, nothing to do
						{}
						
						else if (mineField[x][y].getText() == "!") //remove the flag, and add one back to numFlags
						{
							mineField[x][y].setText("");
							numFlags++;
							flagLabel.setText("Flag count: " + String.valueOf(numFlags));
						}
						
						else //all flags used, nothing to do
						{}
					}
				}
			}
		}
	}
	
	public boolean isFlag(int i, int j)  //check for flag in current location, if there is a flag, return true
	{
		if(mineField[i][j].getText() == "!")
		{
			return true;
		}
		return false;
	}

	public void actionPerformed(ActionEvent event) //action listener for save and load buttons
	{
		if(event.getSource()==saveButton) //save button selected
		{
			clock.stop(); //stop the clock so you can save the time
			try
			{
				//create an output file for saved information
				
				//keeps up with the rows, columns, bomb count, and time of stopped clock for the current game
				PrintWriter outputFile = new PrintWriter("savegame.txt");
				outputFile.println(rows);
				outputFile.println(columns);
				outputFile.println(bombCount);
				String time = clock.toString();
				outputFile.println(time);
				outputFile.println();
				
				//keeps up with the bomb locations 
				String bombPlacement = "";
				for(int a = 0; a < rows; a++)
				{
					for(int b = 0; b < columns; b++)
					{
						bombPlacement =  bombPlacement + (mine.isBomb(a, b)) + " ";
					}
					outputFile.println(bombPlacement);
					bombPlacement = "";
				}
				outputFile.println();
				
				//keeps up with the flag locations
				String flagcells = "";
				for(int a = 0; a < rows; a++)
				{
					for(int b = 0; b < columns; b++)
					{
						flagcells =  flagcells + (isFlag(a, b)) + " ";
					}
					outputFile.println(flagcells);
				
					flagcells = "";
				}
				outputFile.println();
				
				//keeps up with the clicked and disabled locations
				String disabledcells = "";
				for(int a = 0; a < rows; a++)
				{
					for(int b = 0; b < columns; b++)
					{
						disabledcells =  disabledcells + (clearedCells[a][b]) + " ";
					}
					outputFile.println(disabledcells);
				
					disabledcells = "";
				}
				outputFile.close(); //close the file for good housekeeping skills 
			}
			catch (FileNotFoundException e){}
		}
		
		else if (event.getSource() == newButton) //New Game button selected
		{
			//create a whole new game based off of current board
			mine.bombPanel(rows,columns);
			mine.addBomb(rows, columns, bombCount);
			setCellValues(rows,columns);
			clock = new Clock();
			
			flagLabel.setText("Flag count: " + String.valueOf(oldBombCount));
			showButtons(rows, columns);
		}	

		else {}
	}

	public void itemStateChanged(ItemEvent e) //left-click
	{
		clock.start(); //start timer on initial button press
		
		if(e.getItemSelectable()== newButton) //do nothing if new button is selected for state change
		{}
		else
		{
			boolean complete = false; //check to see if the turn is over
			
			while(complete == false && isAWin == false) //check for if the player won or if the turn is over
			{
				int i = 0;
				while(i < rows && isAWin == false && complete == false) //keep checking for a win or if the turn is over
				{
					int j = 0;
					while(j < columns && isAWin == false && complete == false) //keep checking for a win or if the turn is over 
					{
						if(e.getItemSelectable()== mineField[i][j])
						{
							complete = true;//turn over
							
							if (mineField[i][j].getText() == "!") //adds back to the flag count
							{
								mineField[i][j].setText("");
								numFlags++;
								flagLabel.setText("Flag count: " + String.valueOf(numFlags));
							}
							bombOrNotBomb(i, j); //check to see if it is a bomb at location
						}
						j++;
					}
					i++;
				}
			}
			if(isAWin == true) //checks to see if the player is a winner
			{
				Winner();
			}
		}
	}

	public void bombOrNotBomb(int f, int g) //checks location to see if it is a bomb
	{
		// Get number that indicates a bomb or number of adjacent bombs
		
		int kaboom = cellValue[f][g]; //checks value of cell to see if it is a bomb
		
		if(kaboom == -1) // bomb location
		{
			// Disable that cell from being clickable.
			mineField[f][g].setIcon(bombIcon); //shows bomb 
			mineField[f][g].setEnabled(false); //cannot click on cells anymore
			
			for (int i=0;i<rows;i++)
			{
				for (int j=0;j<columns;j++)
				{
					if(mine.isBomb(i,j))
					{
						mineField[i][j].setIcon(bombIcon); //set bomb icon for all the bombs on the board
					}
				}
			}
			
			clock.stop(); //clock stops because the game is over
			String time = clock.toString();
			
			if(clock.getSecond()==null || clock.getMinute()==null) //only happens if player clicks a bomb on first click
			{
				time= "00:00";
			}
			JOptionPane.showMessageDialog(this, "Game Over!\nYour Time: "+time); //game over
			System.exit(0); //exit the board
		}
		
		else //not a bomb at this location
		{
			int check = clearCell(f, g); //check to see if the cells need to be cleared
			
			if (check == 0) //if no bomb near, clear the cell
			{
				clearedCells[f][g] = true;
				clearLoop(f, g);
			}
			else
			{
				clearedCells[f][g] = true;	//only clear the one button the player pressed			
			}
			isAWin = checkWinner(); //check to see if a win
		}
	}
	
	public void Winner() //shows pop-up if there is a winner 
	{
		if(done == false) //done with the turn
		{
			done = true;
			clock.stop(); //stop clock because end of game 
			String time = clock.toString();
			
			if(clock.getSecond() == null || clock.getMinute() == null) //only happens if player wins on first click
			{
				time = "00:00";
			}

			JOptionPane.showMessageDialog(this, "You Won!! \nYour Time: " + time); //winner pop-up
			System.exit(0); //exit the board
			
		}
		else
		{}
	}

	public boolean checkWinner() //checks to see if all of the unselected cells are bombs only, if not, return false
	{
		int spare = 0;
		for(int i = 0; i <= rows - 1; i++)
		{
			for(int j = 0; j <= columns -  1; j++)
			{
				if(clearedCells[i][j] == false)
				{
					spare++;
				}
			}
		}
		if(spare == bombCount)
		{
			return true;
		}
		return false;
	}

	public void clearLoop(int nextY, int nextX) //clears the current cell and any surround cells that have zero bombs near them
	{
		boolean done = false; //check to see if the process is done
		
		storedCells = new boolean[rows][columns]; //cells that have numbers in them for the bombs located near them
		
		while(done == false)
		{
			int smallX = nextX - 1;
			int bigX = nextX + 1;
			int smallY = nextY - 1;
			int bigY = nextY + 1;
			if(nextX == 0) //checks to see if the cell has a zero in it
			{
				smallX = nextX;
			}
			else if(nextX == columns - 1)
			{
				bigX = nextX;
			}
			else
			{}
			if(nextY == 0) //checks to see if the cell has a zero in it
			{
				smallY = nextY;
			}
			else if(nextY == rows - 1)
			{
				bigY = nextY;
			}
			else
			{}
			done = manageACell(smallY, bigY, smallX, bigX); //checks to see if the cell has been cleared, if it has no bombs near it

			int r = 0; 
			boolean exit = false; //exit the while loop
			while(r < rows && exit == false)
			{
				int c = 0;
				while(c < columns && exit == false)
				{
					if(storedCells[r][c] == true) //if location needs to be cleared
					{
						storedCells[r][c] = false; //location has been cleared
						exit = true; //exit while loop
						nextX = c;
						nextY = r;
						done = false;
					}
					else
					{}
					c++;
				}
				r++;
			}
		}
	}

	public boolean manageACell(int minY, int maxY, int minX, int maxX) //manages the current cell and clears the surrounding cells
	{
		boolean myDone = true;
		int numberBombs; //number of bombs near a cell
		
		for(int i = minY; i <= maxY; i++)
		{
			for(int j = minX; j <= maxX; j++)
			{
				if(clearedCells[i][j] == true) //if it has been cleared already, there is nothing to do
				{}
				else
				{
					numberBombs = clearCell(i, j); //check for the number of bombs near that location
					
					if(numberBombs == 0) //if there are no bombs near the location
					{
						clearedCells[i][j] = true; //the cell has been cleared
						
						if (mineField[i][j].getText() == "!")//add flags back to the flag count
						{
							mineField[i][j].setText("");
							numFlags++;
							flagLabel.setText("Flag count: " + String.valueOf(numFlags));
							
						}
						myDone = false;
						// adds cell to set that need adjacent cells cleared
						storedCells[i][j] = true;
					}
					else
					{
						clearedCells[i][j] = true; //cells have been cleared
						
						if (mineField[i][j].getText() == "!") //adds flags back to flag count 
						{
							mineField[i][j].setText("");
							numFlags++;
							flagLabel.setText("Flag count: " + String.valueOf(numFlags));
						}
					}
				}
			}
		}
		return myDone;
	}

	
	public int clearCell(int row, int col) //clear the cell
	{
		int numBombs = cellValue[row][col]; //number of bombs near the location
		
		mineField[row][col].setEnabled(false); //cannot click button any more 
		
		if(numBombs == 0) //no bombs in the area
		{
			mineField[row][col].setSelected(true); //has been selected
			mineField[row][col].setText(" "); //no text to show since there are no bombs near
		}
		else //bombs are near
		{
			mineField[row][col].setSelected(true); //has been selected
			mineField[row][col].setText(Integer.toString(numBombs)); //number of bombs near
		}
		return numBombs;
	}

	public int adjacentBombs(int y, int x) //decides what cells are near and how many bombs are in the near cells
	{
		int bottomX = x - 1;
		int topX = x + 1;
		int bottomY = y - 1;
		int topY = y + 1;
		if(x == 0)
		{
			bottomX = x;
		}
		else if(x == columns - 1)
		{
			topX = x;
		}
		else
		{}
		if(y == 0)
		{
			bottomY = y;
		}
		else if(y == rows - 1)
		{
			topY = y;
		}
		else
		{}		
		int total = getTotalBombs(bottomY, topY, bottomX, topX);
		return total; //total number bombs
	}

	public int getTotalBombs(int minI, int maxI, int minJ, int maxJ) //get the number of adjacent cells with bombs located in them
	{
		int number = 0;
		for(int i = minI; i <= maxI; i++)
		{
			for(int j = minJ; j <= maxJ; j++)
			{
				boolean check = mine.isBomb(i, j); //check each spot to see if there is bomb 
				
				if(check == true) //if there is a bomb, add one to number
				{
					number++;
				}
				else
				{}
			}
		}
		return number; //return number of bombs
	}
}