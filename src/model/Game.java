package model;

import java.util.ArrayList;

public class Game {
	public static final String TITLE = "THE GAME OF LIFE";

	private int rows, columns;//Size of the board
	//	x	,	y

	//Board
	private boolean board[][];
	
	//Observers (observer pattern)
	ArrayList<GameObserver> observers = new ArrayList<GameObserver>();

	public Game(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		board = new boolean[rows][columns];//Initialised to false
	}

	public void setSquareState(boolean state, int x, int y)	//Set the square to on or off
	{
		//Board.setSquareState(state, x, y);
		board[x][y] = state;
		
		updateBoard();
	}

	public boolean getSquareState(int x, int y)
	{
		//Board.getSquareState(x, y);
		return board[x][y];
	}


	public void step()
	{
		boolean[][] ret = new boolean[rows][columns];

		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				int alive = countAlive(i, j, rows, columns);
				if(alive == 3 || (alive == 2 && board[i][j]))//Surviving conditions for a next gen cell
				{
					ret[i][j] = true;
				}
				else
				{
					ret[i][j] = false;
				}
			}
		}

		board = ret;
		
		updateBoard();
	}

	private int countAlive(int i, int j, int height, int width)
	{
		int ret = 0;
		int x[] = {-1, -1, -1, 0, 0, 1, 1, 1};
		int y[] = {-1, 0, 1, -1, 1, -1, 0, 1};
		for(int k = 0; k < 8; k++)
		{
			int a = i + x[k];
			int b = j + y[k];
			if(a >= 0 && a < height && b >= 0 && b < width && board[a][b])
			{
				ret++;
			}
		}

		return ret;
	}

	public void addObserver(GameObserver o) {
		observers.add(o);
	}
	
	private void updateBoard() {
		for(GameObserver o : observers) {
			o.onBoardUpdate();
		}
	}

	public String getStringBoard() {
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

	public boolean[][] getBoard() {
		return board;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
}
