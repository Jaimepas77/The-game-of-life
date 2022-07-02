package view.GUIresources;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.Game;
import model.GameObserver;

public class BoardGrid extends JPanel implements GameObserver {

	private static final long serialVersionUID = 1L;
	
	private Game game;
	private JButton[][] boardBoxes;//Casillas (cajas)
	private int numRows;
	private int numCols;

	public BoardGrid(Game game) {
		super();
		this.game = game;
		numRows = game.getRows();
		numCols = game.getColumns();
		game.addObserver(this);

		initBoard();
	}

	public void initBoard() {
		this.setLayout(new GridLayout(numRows, numCols, -1, -1));//-1 of gap in order to eliminate the gap that exists with 0 as the parameter

		boardBoxes = new JButton[numRows][numCols];

		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				//Inicializar propiedades del botón
				boardBoxes[i][j] = new JButton();
				boardBoxes[i][j].setBackground(Color.WHITE);
				boardBoxes[i][j].setBorderPainted(true);

				//Configurar acciones del botón
				int x = i;//Coordenadas definitivas del botón
				int y = j;
				boardBoxes[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						game.setSquareState(!game.getSquareState(x, y), x, y);//Invertir estado
					}
				}
				);
				
				//Añadir el botón al panel
				this.add(boardBoxes[i][j]);
			}
		}
	}
	
	public void onBoardUpdate() {
		
		boolean[][] board = game.getBoard();
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if(board[i][j]) {
					boardBoxes[i][j].setBackground(Color.YELLOW);
				}
				else {
					boardBoxes[i][j].setBackground(Color.WHITE);
				}
			}
		}
	}
}
