package controller;

import model.Game;
import view.BatchUI;
import view.Viewer;

public class Controller {
	public static final int DEFAULT_ROWS = 6;
	public static final int DEFAULT_COLUMNS = 6;

	//the game itself
	private static Game game;
	private static Viewer view;
	
	//the interface (printline style class)

	public static void main(String[] args) {
		//Create game instance
		game = new Game(DEFAULT_ROWS, DEFAULT_COLUMNS);
		
		//Choose the interface type (batch - GUI) and run it
		view = new BatchUI(game);
	}
}
