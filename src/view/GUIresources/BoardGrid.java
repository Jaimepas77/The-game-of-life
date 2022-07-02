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
	private JButton[][] boardBoxes;//Boxes of the board
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
				//Initialising button properties
				boardBoxes[i][j] = new JButton();
				boardBoxes[i][j].setBackground(Color.WHITE);
				boardBoxes[i][j].setBorderPainted(true);

				//button actions configuration
				int x = i;//A separate variable to preserve the final coordinates for the button
				int y = j;
				boardBoxes[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						game.setSquareState(!game.getSquareState(x, y), x, y);//Invert squares state
					}
				}
				);
				
				//Adding the button to the panel
				this.add(boardBoxes[i][j]);
			}
		}
	}
	
	@Override
	public void onBoardUpdate() {
		
		boolean[][] board = game.getBoard();
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if(board[i][j]) {
					if(boardBoxes[i][j].getBackground() != Color.YELLOW) {
						boardBoxes[i][j].setBackground(Color.YELLOW);
					}
				}
				else {
					if(boardBoxes[i][j].getBackground() != Color.WHITE) {//Only act if it is necessary (to prevent lag)
						boardBoxes[i][j].setBackground(Color.WHITE);
					}
				}
			}
		}
	}
}
