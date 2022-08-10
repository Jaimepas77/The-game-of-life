package view.GUIresources;

import javax.swing.JPanel;

import java.awt.Color;
import model.Game;
import model.GameObserver;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class BoardUI extends JPanel implements GameObserver, ToolBarObserver {

    private Game game;
    private int numRows;
    private int numCols;
    private Color aliveColor = Color.YELLOW;
    private Color deadColor = Color.WHITE;
    private int mouseX = -1, mouseY = -1;// -1 means that the mouse is out of the board. x - collumns ; y - rows

    public BoardUI(Game game) {
        super();
        this.game = game;
        numRows = game.getRows();
        numCols = game.getColumns();
        game.addObserver(this);

        initBoard();
    }

    private void initBoard() {
        // Here all the listeners should be added
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / (getWidth() / numCols);
                int y = e.getY() / (getHeight() / numRows);
                clickCell(y, x);
            }

            public void mouseExited(MouseEvent e) {
                mouseX = mouseY = -1;
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() / (getWidth() / numCols);
                int y = e.getY() / (getHeight() / numRows);
                overCell(y, x);
                repaint();
            }
        });
    }

    private void clickCell(int row, int col) {
        if (game.getSelectionState() == 1) {
            game.selectFirst(row, col);
        } else if (game.getSelectionState() == 2) {
            game.selectSecond(row, col);
        } else if (game.getSelectionState() == 3 || game.isToBeInserted()) {
            game.insertToBeInserted(row, col);// In fact, copy the selected area to the clicked place
        } else {
            game.setSquareState(!game.getSquareState(row, col), row, col);// Invert squares state
        }
    }

    private void overCell(int y, int x) {
        mouseX = x;
        mouseY = y;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        boolean[][] board = game.getBoard();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // Properties of the square to be painted
                int x = w / numCols * j;
                int y = h / numRows * i;
                int width = w / numCols;
                int height = h / numRows;

                if (board[i][j]) {
                    g.setColor(aliveColor);
                } 
                else {
                    g.setColor(deadColor);
                }

				if(game.getSelectionState() == 2) {
					if(i == game.getX1() && j == game.getY1()) {
                        g.setColor(g.getColor().darker());
					}
				}
				else if(game.getSelectionState() == 3) {
					int x1 = game.getX1();
					int y1 = game.getY1();
					int x2 = game.getX2();
					int y2 = game.getY2();
					if(i >= Math.min(x1, x2) && i <= Math.max(x1, x2) && j >= Math.min(y1,  y2) && j <= Math.max(y1,  y2)) {//If this square is in the range of the selected values ...
                        g.setColor(g.getColor().darker());
					}
				}

                if (mouseY != -1 && (game.isToBeInserted() || game.getSelectionState() == 3) && i >= mouseY && j >= mouseX) {//To paint a preview of a potential insertion
                    boolean[][] tmp = game.getToBeInserted();
                    if(tmp.length > i - mouseY && tmp[0].length > j - mouseX) {
                        // Paint the structure in a darker color
                        if (tmp[i-mouseY][j-mouseX]) {
                            g.setColor(aliveColor.darker());
                        } else {
                            g.setColor(deadColor.darker());
                        }
                    }
                }

                // if(mouseY == i && mouseX == j) {
                // g.setColor(g.getColor().darker());
                // }

                g.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);
                if (mouseY == i && mouseX == j) {
                    g.setColor(Color.ORANGE);
                    g.drawRect(x + 1, y + 1, width - 2, height - 2);
                }
            }
        }
        // g.fillRect(w-15, h-15, 10, 10);//See the corner
    }

    @Override
    public void onBoardUpdate() {
        // Repaint the places of the board
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