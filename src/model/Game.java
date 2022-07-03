package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	public static final String TITLE = "THE GAME OF LIFE";

	private int rows, columns;//Size of the board
	//	x	,	y

	//Board
	private boolean board[][];
	private ArrayDeque<boolean[][]> past = new ArrayDeque<boolean[][]>();

	//Observers (observer pattern)
	ArrayList<GameObserver> observers = new ArrayList<GameObserver>();

	private boolean running = false; //Whether the game is running or not
	private long delta = 1000;//The delay between steps in the running process

	public Game(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		board = new boolean[rows][columns];//Initialised to false
	}

	public void setSquareState(boolean state, int x, int y)	//Set the square to on or off
	{
		//Board.setSquareState(state, x, y);
		storePresent();
		board[x][y] = state;

		updateBoard();
	}

	public boolean getSquareState(int x, int y)
	{
		//Board.getSquareState(x, y);
		return board[x][y];
	}

	public void clearBoard() {
		storePresent();
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				if(board[i][j])
					board[i][j] = false;
			}
		}
		
		updateBoard();
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

		storePresent();

		board = ret;

		updateBoard();
	}

	public boolean stepBack() {//Return whether there was a past state or not to be restored
		if(past.size() > 0) {
			board = past.removeLast();
			updateBoard();
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isTherePast() {
		return past.size() > 0;//Is there any past state stored?
	}
	
	private void storePresent() {
		boolean tmp[][] = new boolean[rows][columns];//Copy of the board
		for(int i = 0; i < rows; i++) {
			tmp[i] = Arrays.copyOf(board[i], columns);
		}
		
		past.addLast(tmp);//Update the stack of last movements
		if(past.size() > 50) {//Limit the amount of past movements to 50
			past.removeFirst();
		}
	}

	public void play() { //Run steps of the game. It can be stopped with the stop method
		if(!running) {
			running = true;
			updateRunningState();
			while(running) {
				step();
				updateBoard();
				try {
					Thread.sleep(delta);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(compareBoard(past.getLast(), board)) {
					running = false;
				}
			}
			updateRunningState();
		}
	}

	private boolean compareBoard(boolean[][] board1, boolean[][] board2) {

		if(board1.length != board2.length) {
			return false;
		}
		for(int i = 0; i < board1.length; i++) {
			if(board1[i].length != board2.length) {
				return false;
			}
			for(int j = 0; j < board1[i].length; j++) {
				if(board1[i][j] != board2[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	public void pause() {//Stop the execution of the game (if running)
		if(running) {
			running = false;
			updateRunningState();
		}
	}

	public boolean isRunning() {
		return running;
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

	private void updateRunningState() {
		for(GameObserver o : observers) {
			o.onRunningUpdate(running);
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

	public long getDelta() {
		return delta;
	}

	public void setDelta(long delta) {
		this.delta = delta;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
}
