import java.io.*;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;

public class Project3
{
	private static int rows;
	private static int columns;
	private static int numBombs;
	private static JPanel panel;
	private static int selection;	//Holds the menu item selected by the user.
	private static Scanner scanner;
	
	public static void Menu()
	{
	//Display the Selection Menu
		System.out.println("\n----------------- MINESWEEPER -----------------------\n");
		System.out.println("Here are the available options: \n");
		System.out.println("1.Start a default game with an 8*8 board and 10 bombs");
		System.out.println("2.Create a custom game");
		System.out.println("3.Load the most recently saved game");
		System.out.println("4.Exit Menu\n");
		System.out.printf("Please enter the number associated with your selection: ");	
	}
	
	//Allow the user to custom a game
	public static void getCostum()
	{
	//Get the number of rows, columns and bombs from the user
		System.out.printf("\nPlease enter the number of rows for the board: ");
		Scanner sc = new Scanner(System.in);
		rows= sc.nextInt();
		System.out.printf("\nPlease enter the number of columns for the board: ");
		columns = sc.nextInt();
		System.out.printf("\nPlease Enter the number of bombs to be placed: ");
		numBombs = sc.nextInt();
		int capacity = rows *columns;
		while(numBombs >= (capacity/2)) //Limit the number of bombs that can be placed to half the board space
		{
			System.out.println("The board cannot not hold "+numBombs +" bombs");
			System.out.printf("Please enter a number less than " +capacity/2+": ");
			numBombs= sc.nextInt();
		}
		//Create the custom board
		panel = new GameBoard(rows, columns, numBombs);
	}
	//Build the default board with 8 rows, 8 columns and 10 bombs.
	public static void showDefault()
	{
		rows =8;
		columns =8;
		numBombs =10;
		panel = new GameBoard(rows,columns,numBombs);
		
	}
	//Allow the user to rebuild a saved game using data recovered from a file
	public static void loadSaved()
	{
		try
		{
			scanner = new Scanner(new File("savegame.txt"));
			panel = new GameBoard(scanner);
			
		}
		catch (FileNotFoundException e){}
	}
	public static void main (String[] args) throws Exception
	{
		do
		{	
			
			Menu();		//Displays the menu
			Scanner scan = new Scanner(System.in);
			selection = scan.nextInt();
		//Process each selection accordingly
			switch(selection)
			{
				case 1:
					showDefault();
					break;
				case 2:
					getCostum();
					break;
				case 3:
					loadSaved();
					break;
				case 4:
					System.exit(0);
			}	
				
		}while(selection<1 || selection >4); //Keep displaying the menu until a valid selection is made
		
		JFrame window = new JFrame();
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		
	}
}
