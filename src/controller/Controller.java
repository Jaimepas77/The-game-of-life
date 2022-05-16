package controller;

import model.Game;

public class Controller {
	public static final int DEFAULT_ROWS = 6;
	public static final int DEFAULT_COLLUMNS = 6;
	
	//the game itself
	private static Game game;
	
	//the interface (printline style class)

	public static void main(String[] args) {
		//Choose the interface type (batch - GUI)
		
		//Create game instance
		game = new Game(DEFAULT_ROWS, DEFAULT_COLLUMNS);
		
		//Run interface thread (passing the controller itself as a parameter)
		
		
	}

}
