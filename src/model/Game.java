package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {
	public static final String TITLE = "THE GAME OF LIFE";
	private static final int PAST_LIMIT = 80;//Limit to the amount of past states stored
	private static final long DELTA = 500;//The delay between steps in the running process (at speed 1)

	private int rows, columns;//Size of the board
	//	x	,	y

	//Board
	private boolean board[][];
	private ArrayDeque<boolean[][]> past = new ArrayDeque<boolean[][]>();

	//Observers (observer pattern)
	ArrayList<GameObserver> observers = new ArrayList<GameObserver>();

	private boolean running = false; //Whether the game is running or not
	private double speed = 1;//Modifier of the standard speed

	private boolean[][] toBeInserted = null;

	private int selectionState = 0;//0 not selecting; 1 selecting the first square; 2 selecting the second square; 3 area selected
	private int x1, y1, x2, y2;//Corners of the selected area

	public Game(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		board = new boolean[rows][columns];//Initialised to false

		x1 = y1 = x2 = y2 = -1;
	}

	public void setSquareState(boolean state, int x, int y)	//Set the square to on or off
	{
		storePresent();
		board[x][y] = state;

		updateBoard();
	}

	public boolean getSquareState(int x, int y)
	{
		return board[x][y];
	}

	public void selectFirst(int x, int y) {
		x1 = x;
		y1 = y;
		selectionState = 2;
		updateSelectionMode();
		updateBoard();
	}

	public void selectSecond(int x, int y) {
		x2 = x;
		y2 = y;
		selectionState = 3;

		//Copy the area to the to be inserted variable
		toBeInserted = new boolean[Math.max(x1,  x2) - Math.min(x1,  x2) + 1][Math.max(y1,  y2) - Math.min(y1,  y2) + 1];
		for(int i = Math.min(x1,  x2); i <= Math.max(x1,  x2);i++) {
			for(int j = Math.min(y1,  y2); j <= Math.max(y1,  y2); j++) {
				toBeInserted[i - Math.min(x1,  x2)][j - Math.min(y1,  y2)] = board[i][j];
			}
		}

		updateSelectionMode();
		updateBoard();
	}

	public void readFromFile(File file) throws FileNotFoundException {
		String name = file.getName();
		String type;
		if(name.length() >= 4) {
			type = name.substring(name.length() - 4);
		}
		else {
			type = "";
		}

		if(type.equals(".rle")) {
			toBeInserted = readFromFileRLE(file);
		}
		else {
			toBeInserted = null;
			System.err.println("format not implemented yet");
		}
	}

	private boolean[][] readFromFileRLE(File file) throws FileNotFoundException {
		boolean ret[][] = null;

		Scanner text = new Scanner(file);
		String line = text.nextLine();
		while(line.length() == 0 || line.charAt(0) == '#') {
			line = text.nextLine();
		}

		String info[] = line.split(" ");
		int x = 0;
		int y = 0;
		if(info.length == 6 || info.length == 9) {//Size of a normal RLE header

			info[2] = info[2].substring(0, info[2].length()-1);//Delete the last character (',')
			y = Integer.parseInt(info[2]);

			if(info.length == 9) {//If the y number is going to have a ','
				info[5] = info[5].substring(0, info[5].length()-1);
			}
			x = Integer.parseInt(info[5]);

			ret = new boolean[x][y];
		}
		else {
			System.err.println("Invalid header while reading an RLE file");
		}

		String str = text.next();
		while(str.charAt(str.length()-1) != '!') {
			str += text.next();
		}

		int i = 0, j = 0;
		int num = -1;
		for(char c : str.toCharArray()) {
			if(c >= '0' && c <= '9') {
				if(num == -1) {
					num = c - '0';
				}
				else {
					num *= 10;
					num += (c - '0');
				}
			}
			else if(c == '$') {
				i++;
				j = 0;
			}
			else if(c == '!') {
				break;
			}
			else {
				if(num == -1) {
					num = 1;
				}

				for(int k = 0; k < num; k++) {
					ret[i][j] = (c == 'o');
					j++;
				}

				num = -1;
			}
		}

		text.close();
		return ret;
	}

	public void insertToBeInserted(int x, int y) {
		storePresent();

		for(int i = x; i < toBeInserted.length + x && i < rows;i++) {
			for(int j = y; j < toBeInserted[i-x].length + y && j < columns; j++) {
				board[i][j] = toBeInserted[i-x][j-y];
			}
		}

		toBeInserted = null;//After inserted, it gets deleted
		if(selectionState == 3) {
			selectionState = 1;//After inserted, this is set to 1 in case that we inserted a selected area
		}
		else {
			selectionState = 0;
		}
		updateSelectionMode();
		updateBoard();
	}

	public void saveToBeInserted(File file) throws FileNotFoundException, FileAlreadyExistsException {
		//Save the structure in a file in RLE format
		String name = file.getName();
		String type;
		if(name.length() >= 4) {
			type = name.substring(name.length() - 4);
		}
		else {
			type = "";
		}

		if(!type.equals(".rle")) {
			file = new File(file.getAbsolutePath() + ".rle");
		}

		try {
			if(file.createNewFile()) {
				FileWriter myWriter = new FileWriter(file);
				myWriter.write(encodeToBeInserted());
				myWriter.close();
				//				System.out.println("Successfully wrote to the file.");
			}
			else {
				System.err.println("A file with that name already exists");
				throw new FileAlreadyExistsException(file.getName(), null, "The file couldn't be stored because a file with that exact name already exists.");
			}

		} catch (FileAlreadyExistsException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}

		toBeInserted = null;
		selectionState = 0;
		updateSelectionMode();
		updateBoard();
	}

	private String encodeToBeInserted() {
		String ret = "";

		ret = "x = " + toBeInserted[0].length + ", y = " + toBeInserted.length;
		ret += "\n";

		//Encode the array
		int alive = 0;
		int dead = 0;
		for(int i = 0; i < toBeInserted.length; i++) {
			for(int j = 0; j < toBeInserted[0].length; j++) {
				if(toBeInserted[i][j] == true) {
					if(dead > 0) {
						//Print dead
						ret += dead + "b";
						dead = 0;
					}

					alive++;
				}
				else {
					if(alive > 0) {
						ret += alive + "o";
						alive = 0;
					}

					dead++;
				}
			}
			if(alive > 0) {
				ret += alive + "o";
			}
			if(i < toBeInserted.length-1) {
				ret += "$";
			}
			else {
				ret += "!";
			}
			alive = 0;
			dead = 0;
		}

		return ret;
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
			while(past.size() > 1 && equalsBoard(past.getLast(), board)) {//While the past is the same as the present...
				past.removeLast();//Ignore this state
			}

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
		if(past.size() == 0 || !equalsBoard(board, past.getLast()))//If the board has changed from the last one... (otherwise don't save the same board two times)
		{
			boolean tmp[][] = new boolean[rows][columns];//Copy of the board
			for(int i = 0; i < rows; i++) {
				tmp[i] = Arrays.copyOf(board[i], columns);
			}

			past.addLast(tmp);//Update the stack of last movements
			if(past.size() > PAST_LIMIT) {//Limit the amount of past movements to 50 (or other number)
				past.removeFirst();
			}
		}
	}

	public void play() { //Run steps of the game. It can be stopped with the stop method
		if(!running) {
			running = true;
			updateRunningState();
			while(running) {
				step();
				try {
					Thread.sleep((long) (DELTA/speed));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if(equalsBoard(past.getLast(), board)) {
					running = false;
				}
			}
			updateRunningState();
		}
	}

	private boolean equalsBoard(boolean[][] board1, boolean[][] board2) {

		if(board1.length != board2.length) {
			return false;
		}

		for(int i = 0; i < board1.length; i++) {
			if(board1[i].length != board2[i].length) {
				return false;
			}
			for(int j = 0; j < board1[i].length; j++) {
				if(board1[i][j] != board2[i][j]) {
					return false;
				}
			}
		}

		return true;//If any difference is found false is returned; true otherwise
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
			a = (a + height) % height;
			b = (b + width) % width;
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

	private void updateSelectionMode() {
		for(GameObserver o : observers) {
			o.onSelectionMode(selectionState);
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

	public boolean[][] getToBeInserted() {
		return toBeInserted;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public boolean isToBeInserted() {
		if(toBeInserted == null) {
			return false;
		}
		else {
			return true;
		}
	}

	public int getSelectionState() {
		return selectionState;
	}

	public void setSelectionState(int selectionState) {
		this.selectionState = selectionState;
		updateSelectionMode();
		updateBoard();
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public void deleteToBeInserted() {
		toBeInserted = null;		
	}
}
