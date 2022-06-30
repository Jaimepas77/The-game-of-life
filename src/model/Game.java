package model;

public class Game {
	private int rows, columns;//Size of the board
	//	x	,	y
	
	//Board
	private boolean board[][];
	
	public Game(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		board = new boolean[rows][columns];//Inicializado a false
	}

	public void setSquareState(boolean state, int x, int y)	//Set the square to on or off
	{
		//Board.setSquareState(state, x, y);
		board[x][y] = state;
	}
	
	public boolean getSquareState(int x, int y)
	{
		//Board.getSquareState(x, y);
		return board[x][y];
	}

	public String getBoard() {
		String ret = "";
		
		for(boolean[] r : board) {
			for(boolean x : r) {
				if(x) {
					ret += 'O';
				}
				else {
					ret += '_';
				}
			}
			ret += '\n';
		}
		
		return ret;
	}
}
