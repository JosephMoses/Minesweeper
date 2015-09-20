import java.util.Random;

public class Mines 
{
	private boolean Bombs[][];
	private int row;
	private int column;
	
	public Mines()
	{
		super();
	}
	public void bombPanel(int row, int col)
	{
		Bombs = new boolean [row][col];  //creates a boolean to keep up with the bomb location
	}
	public  void addBomb(int row, int col, int numBombs) //randomly places correct number of bombs in the boolean array Bombs
	{
		int count =0;
		while (count <numBombs) //keep count of the bombs we are suppose to place and add them to Bombs[][]
		{
			Random rand = new Random();
			int i = rand.nextInt(row);
			int j = rand.nextInt(col);
			Bombs[i][j] = true;
			count++; //increment the count up for every bomb placed
		}
	
	}
	public boolean isBomb(int i, int j)
	{
		if (Bombs[i][j]==true) //check to see if there is a bomb located at this location, if so, return true
			return true; //there is a bomb in this location
		else
			return false; // there is not a bomb in this location
	}
	public void placeBombs(String text,int i, int j) //place the bombs in the correct location for a previously saved game
	{
		Bombs[i][j]= Boolean.valueOf(text);
	}
	
	public boolean[][] getBombs() //get the array for bomb location
	{
		return Bombs;
	}
	
}