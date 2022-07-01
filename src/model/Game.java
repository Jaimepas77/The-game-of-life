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
