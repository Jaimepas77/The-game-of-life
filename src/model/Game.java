package model;

public class Game {
	private int rows, columns;//Size of the board
	//	x	,	y
	
	//Board
	
	public Game(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
	}

	public void setSquareState(boolean state, int x, int y)	//Set the square to on or off
	{
		//Board.setSquareState(state, x, y);
	}
	
	public void getSquareState(int x, int y)
	{
		//Board.getSquareState(x, y);
	}
}
