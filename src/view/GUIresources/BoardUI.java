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
    private boolean enableClick = true;

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
            @Override
            public void mousePressed(MouseEvent e) {
                int x = (int) resizeCoord(e.getX(), getWidth(), numCols);
                int y = (int) resizeCoord(e.getY(), getHeight(), numRows);
                clickCell(y, x);
            }

            @Override
            public void mouseReleased(MouseEvent e) {// Warning: the mouse can be released out of the panel
                int x = (int) resizeCoord(e.getX(), getWidth(), numCols);
                x = Math.max(x, 0);
                x = Math.min(x, numCols - 1);
                int y = (int) resizeCoord(e.getY(), getHeight(), numRows);
                y = Math.max(y, 0);
                y = Math.min(y, numRows - 1);

                releaseCell(y, x);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                overCell(-1, -1);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = (int) resizeCoord(e.getX(), getWidth(), numCols);
                int y = (int) resizeCoord(e.getY(), getHeight(), numRows);
                overCell(y, x);
            }

            @Override
            public void mouseDragged(MouseEvent e) {// Warning: the mouse can be dragged out of the panel
                int x = (int) resizeCoord(e.getX(), getWidth(), numCols);
                x = Math.max(x, 0);
                x = Math.min(x, numCols - 1);
                int y = (int) resizeCoord(e.getY(), getHeight(), numRows);
                y = Math.max(y, 0);
                y = Math.min(y, numRows - 1);

                overCell(y, x);
            }
        });
    }

    private float resizeCoord(float x, float maxX, float newMaxX) {// Resize coordinates without losing precision
        float newX = x / maxX * newMaxX;
        return newX;
    }

    private void clickCell(int row, int col) {
        if (enableClick) {// Only do smth when the board click is enabled
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
    }

    private void releaseCell(int row, int col) {
        if (game.getSelectionState() == 2) {
            game.selectSecond(row, col);
        }
    }

    private void overCell(int row, int col) {
        mouseX = col;
        mouseY = row;
        if (enableClick) {// Only paint the mouse pos when you can do smth with it
            repaint();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        boolean[][] board = game.getBoard();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // Properties of the square to be painted
                int x = (int) resizeCoord(j, numCols, w);
                int y = (int) resizeCoord(i, numRows, h);
                int width = (int) resizeCoord(j + 1, numCols, w) - x;// The space between the next col and this one is
                                                                     // the width
                int height = (int) resizeCoord(i + 1, numRows, h) - y;

                if (board[i][j]) {
                    g.setColor(aliveColor);
                } else {
                    g.setColor(deadColor);
                }

                if (game.getSelectionState() == 2) {
                    int x1 = game.getX1();// Row
                    int y1 = game.getY1();// Col
                    int x2 = mouseY;// Row
                    int y2 = mouseX;// Col
                    if (i >= Math.min(x1, x2) && i <= Math.max(x1, x2) && j >= Math.min(y1, y2)
                            && j <= Math.max(y1, y2)) {// If this square is in the range of the potentially selected
                                                       // values ...
                        g.setColor(g.getColor().darker());
                    }
                } else if (game.getSelectionState() == 3) {
                    int x1 = game.getX1();
                    int y1 = game.getY1();
                    int x2 = game.getX2();
                    int y2 = game.getY2();
                    if (i >= Math.min(x1, x2) && i <= Math.max(x1, x2) && j >= Math.min(y1, y2)
                            && j <= Math.max(y1, y2)) {// If this square is in the range of the selected values ...
                        g.setColor(g.getColor().darker());
                    }
                }

                if (mouseY != -1 && (game.isToBeInserted() || game.getSelectionState() == 3) && i >= mouseY
                        && j >= mouseX) {// To paint a preview of a potential insertion
                    boolean[][] tmp = game.getToBeInserted();
                    if (tmp.length > i - mouseY && tmp[0].length > j - mouseX) {
                        // Paint the structure in a darker color
                        if (tmp[i - mouseY][j - mouseX]) {
                            g.setColor(aliveColor.darker().darker());
                        } else {
                            g.setColor(deadColor.darker().darker());
                        }
                    }
                }

                // if(mouseY == i && mouseX == j) {
                // g.setColor(g.getColor().darker());
                // }

                g.fillRect(x, y, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);
                if (enableClick && mouseY == i && mouseX == j) {
                    g.setColor(Color.ORANGE);
                    g.drawRect(x + 1, y + 1, width - 2, height - 2);
                }
            }
        }
    }

    @Override
    public void onRunningUpdate(boolean running) {
        enableClick = !running;// Only enable the board interactions when the game is not running

        repaint();
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