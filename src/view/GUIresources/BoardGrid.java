package view.GUIresources;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.Game;
import model.GameObserver;

public class BoardGrid extends JPanel implements GameObserver, ToolBarObserver {

	private static final long serialVersionUID = 1L;
	
	private Game game;
	private JButton[][] boardBoxes;//Boxes of the board
	private int numRows;
	private int numCols;
	private Color aliveColor = Color.YELLOW;
	private Color deadColor = Color.WHITE;

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
				boardBoxes[i][j].setBackground(deadColor);
				boardBoxes[i][j].setBorderPainted(true);

				//button actions configuration
				int x = i;//A separate variable to preserve the final coordinates for the button
				int y = j;
				boardBoxes[i][j].addActionListener(new ActionListener() {//Click
					@Override
					public void actionPerformed(ActionEvent e) {
						if(game.getSelectionState() == 1) {
							game.selectFirst(x, y);
						}
						else if(game.getSelectionState() == 2) {
							game.selectSecond(x, y);
						}
						else if(game.getSelectionState() == 3 || game.isToBeInserted()) {
							game.insertToBeInserted(x, y);//In fact, copy the selected area to the clicked place
						}
						else {
							game.setSquareState(!game.getSquareState(x, y), x, y);//Invert squares state
						}
					}
				}
				);
				
				boardBoxes[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						if(game.isToBeInserted()) {
							boolean[][] tmp = game.getToBeInserted();
							for(int k = x; k-x < tmp.length && k < numRows; k++) {
								for(int l = y; l-y < tmp[k-x].length && l < numCols; l++) {
									if(tmp[k-x][l-y]) {
										boardBoxes[k][l].setBackground(aliveColor.darker());
									}
									else {
										boardBoxes[k][l].setBackground(deadColor.darker());//Paint the structure in a darker color										
									}
								}
							}
						}
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						if(game.isToBeInserted()) {
							onBoardUpdate();//Repaint the board unpainting the previsualization of the insertion
						}
					}
				});
				
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
					if(boardBoxes[i][j].getBackground() != aliveColor) {
						boardBoxes[i][j].setBackground(aliveColor);
					}
				}
				else {
					if(boardBoxes[i][j].getBackground() != deadColor) {//Only act if it is necessary (to prevent lag)
						boardBoxes[i][j].setBackground(deadColor);
					}
				}
				
				if(game.getSelectionState() == 2) {
					if(i == game.getX1() && j == game.getY1()) {
						boardBoxes[i][j].setBackground(boardBoxes[i][j].getBackground().darker());
					}
				}
				else if(game.getSelectionState() == 3) {
					int x1 = game.getX1();
					int y1 = game.getY1();
					int x2 = game.getX2();
					int y2 = game.getY2();
					if(i >= Math.min(x1, x2) && i <= Math.max(x1, x2) && j >= Math.min(y1,  y2) && j <= Math.max(y1,  y2)) {//If this square is in the range of the selected values ...
						boardBoxes[i][j].setBackground(boardBoxes[i][j].getBackground().darker());						
					}
				}
			}
		}
	}
	
	@Override
	public void onFirstColorUpdate(Color c) {
		aliveColor = c;
		onBoardUpdate();
	}
	
	@Override
	public void onSecondColorUpdate(Color c) {
		deadColor = c;
		onBoardUpdate();
	}
}
