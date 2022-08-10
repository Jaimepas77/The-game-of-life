package view.GUIresources;

import javax.swing.JPanel;

import java.awt.Color;
import model.Game;
import model.GameObserver;
import java.awt.Graphics;
import java.awt.Dimension;

public class BoardUI extends JPanel implements GameObserver, ToolBarObserver {
    
	private Game game;
	private int numRows;
	private int numCols;
	private Color aliveColor = Color.YELLOW;
	private Color deadColor = Color.WHITE;

    public BoardUI(Game game) {
        super();
        this.game = game;
        numRows = game.getRows();
        numCols = game.getColumns();
        game.addObserver(this);

        initBoard();
    }

    private void initBoard() {
        //Here all the listeners should be added
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        boolean[][] board = game.getBoard();
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
                //Properties of the square to be painted
                int x = w/numCols * j; int y = h/numRows * i;
                int width = w/numCols; int height = h/numRows;

                if(board[i][j]) {
					g.setColor(aliveColor);
				}
				else {
                    g.setColor(deadColor);
				}
                g.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);
            }
        }
        // g.fillRect(w-15, h-15, 10, 10);//See the corner
    }

    @Override
    public void onBoardUpdate() {
        //Repaint the places of the board
        repaint();
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