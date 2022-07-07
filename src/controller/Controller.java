package controller;

import javax.swing.JOptionPane;

import model.Game;
import view.BatchUI;
import view.GraphicUI;

public class Controller {
	public static final int DEFAULT_ROWS = 30;
	public static final int DEFAULT_COLUMNS = 40;

	// the game itself
	private static Game game;

	public static void main(String[] args) {
		// Create game instance

		// Choose the interface type (batch - GUI) and run it

		
		boolean invalid = true;
		int rows = DEFAULT_ROWS;
		int columns = DEFAULT_COLUMNS;

		while (invalid) {
			String int1 = JOptionPane.showInputDialog("Introduzca numero de filas");
			try {
				invalid = false;
				rows = Integer.parseInt(int1);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "El texto " + int1 + " no se corresponde con un numero");

				invalid = true;
				continue;
			}
		}

		if (rows <= 0) {
			JOptionPane.showMessageDialog(null,
					rows + " menor que 0, sustituido por valor por defecto: " + DEFAULT_ROWS);
			rows = DEFAULT_ROWS;
		}
		invalid = true;

		while (invalid) {
			String int1 = JOptionPane.showInputDialog("Introduzca numero de columnas");
			try {
				invalid = false;
				columns = Integer.parseInt(int1);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "El texto " + int1 + " no se corresponde con un numero");
				invalid = true;
				continue;
			}
		}

		if (columns <= 0) {
			JOptionPane.showMessageDialog(null,
					columns + " menor que 0, sustituido por valor por defecto: " + DEFAULT_ROWS);
			columns = DEFAULT_COLUMNS;
		}
		game = new Game(rows, columns);

		int op = JOptionPane.showConfirmDialog(null, "¿Quieres emplear la interfaz gráfica?", "Elegir interfaz",
				JOptionPane.YES_NO_OPTION);
				
		if (op == JOptionPane.YES_OPTION) {


			new GraphicUI(game);
		} else

		{
			new BatchUI(game);
		}
	}
}
