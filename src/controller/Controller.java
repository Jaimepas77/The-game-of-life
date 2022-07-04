package controller;

import javax.swing.JOptionPane;

import model.Game;
import view.BatchUI;
import view.GraphicUI;

public class Controller {
	public static final int DEFAULT_ROWS = 30;
	public static final int DEFAULT_COLUMNS = 30;

	//the game itself
	private static Game game;

	public static void main(String[] args) {
		//Create game instance
		game = new Game(DEFAULT_ROWS, DEFAULT_COLUMNS);
		
		//Choose the interface type (batch - GUI) and run it
		int op = JOptionPane.showConfirmDialog(null, "¿Quieres emplear la interfaz gráfica?", "Elegir interfaz", JOptionPane.YES_NO_OPTION);
		if(op == JOptionPane.YES_OPTION) {
			new GraphicUI(game);
		}
		else {
			new BatchUI(game);
		}
	}
}
