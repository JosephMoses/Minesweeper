import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;

class Clock extends JPanel
{
	private final ClockListener clockListener = new ClockListener();
	private final Timer timer = new Timer(1000, clockListener);
	private final JTextField timeField = new JTextField(7);
	private int loadMinute=0; //for loading a previous saved game's time
	private int loadSecond=0; //for loading a previous saved game's time
	private String minute;
	private String second;
	
	public Clock() //main constructor for initial game's clock
	{
		timer.setInitialDelay(0);
		
		JPanel panel = new JPanel();
		
		timeField.setHorizontalAlignment(JTextField.RIGHT);
		timeField.setEditable(false);
		
		panel.add(timeField);
		
		this.add(panel);
		this.setVisible(true);
	}
	public Clock(String savedClock) //constructor for a saved game's clock
	{
		//get the minutes for the saved game
		String savedMinute = savedClock.substring(0, 2);
		loadMinute = Integer.parseInt(savedMinute);
		//get the seconds for the saved game
		String savedSecond = savedClock.substring(3, 5);
		loadSecond = Integer.parseInt(savedSecond);
		
		timer.setInitialDelay(0);
		
		JPanel panel = new JPanel();
		
		timeField.setHorizontalAlignment(JTextField.RIGHT);
		timeField.setEditable(false);
		
		panel.add(timeField);
		
		this.add(panel);
		this.setVisible(true);
	}
	
	public void start() //start clock
	{
		timer.start();
	}
	
	public void stop() //stop clock
	{
		timer.stop();
	}
		
	private class ClockListener implements ActionListener
	{
		private int minutes;
		private int seconds;
		private int newMinute;
		private int newSecond;
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			NumberFormat formatter = new DecimalFormat("00");
			
			if (seconds == 60)//if the seconds equal a minute, add a minute to the clock and reset seconds to zero
			{
				seconds = 00;
				minutes++;
			}
			
			newMinute = minutes + loadMinute; //new time from the previously saved game
			newSecond = seconds + loadSecond; //new time from the previously saved game
			
			if (newSecond >= 60) //check to see if the time from the saved game is over the 60 seconds
			{
				newSecond = newSecond - 60;
				newMinute++;
			}
			
			minute = formatter.format(newMinute);
			second = formatter.format(newSecond);
			
			timeField.setText(String.valueOf("Time: " + minute + ":" + second)); //show the correct time 
			
			seconds++; //increment the seconds 
		}
	}
	
	public String getMinute()
	{
		return this.minute;
	}
	
	public String getSecond()
	{
		return this.second;
	}
	
	public String toString()
	{
		return minute + ":" + second;
	}
}
