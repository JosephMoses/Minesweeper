# Minesweeper

##Introduction
This project was a group course assignment for Computer Programming II (Java). This project is based of the Window's Minesweeper game. This game includes a default board creation, custom board creation option, and load the most recently saved game option. In addtion to the playing board, each game has a timer, flag counter, a save button, and a new game button.

##Instructions
**Note that Project3.java needs to be compiled first.**
* First, a selection must be made to start the game. Select the default (option 1) for first time players, which is gives an 8x8 board with 10 bombs located on the board.

![alt text](https://github.com/ShannonAllene/Minesweeper/blob/master/Minesweeper%20Pictures/Initial%20Screen.png "Initial Screen")

* Select your first location on the grid to start your initial game. *Note that the timer will start once the first location is selected.*

![alt text](https://github.com/ShannonAllene/Minesweeper/blob/master/Minesweeper%20Pictures/FIrst%20Click.png "First Click")

* Keep selecting locations to clear areas until the game over message is displayed or the winning message is displayed.

![alt text](https://github.com/ShannonAllene/Minesweeper/blob/master/Minesweeper%20Pictures/Game%20Over.png "Game Over") ![alt text](https://github.com/ShannonAllene/Minesweeper/blob/master/Minesweeper%20Pictures/Winner.png "Winner")

* Run the file again to start a new game.
*Note that if you want to start a custom game (option 2) you will need to enter the diminsions of the table and the number of bombs.*

![alt text](https://github.com/ShannonAllene/Minesweeper/blob/master/Minesweeper%20Pictures/custom%20option.png "Custom Option")

* To save your game at any time, click the "Save" button on your game board.
* To start a new game while playing your current game, click the "New Game" button on your game board.
* To load the last saved game, select (option 3) from the list. *Note that this will only work after you have saved a previous game.*

##Notes
The timer (Clock class) was the hardest part of this project for us, mainly because we did not know how to make it work continuously and had to do a lot of research on it

We became very attentive to multidimensional arrays using rows first and then columns second. This was a small issue we had from the beginning, but was quickly overcame. The confusion with the coordinates happened because in math it is columns (x value) and then rows (y value), but in programming, it is rows, then columns.

##Acknowledgements
Alix Rosarion is co-creator of this Minesweeper project.
