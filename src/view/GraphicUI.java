package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import model.Game;
import view.GUIresources.*;

public class GraphicUI extends JFrame implements Viewer{

	private static final long serialVersionUID = 1L;

	private Game game;

	public GraphicUI(Game game) {
		super(Game.TITLE);
		this.game = game;
		runUI();
	}

	public void runUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Layout settings
		this.setLayout(new BorderLayout());
		
		//Toolbar (NORTH)
		ToolBar tools = new ToolBar(game);
		this.add(tools, BorderLayout.NORTH);

		//Board (CENTER)
		// BoardGrid board = new BoardGrid(game);
		BoardUI board = new BoardUI(game);
		this.add(board, BorderLayout.CENTER);
		tools.addObserver(board);

		//Adapt the window appearance
		this.setBounds(200, 50, 900, 600);//Default size and position if not maximised
		this.setMinimumSize(new Dimension(550, 400));//Minimum size
		//this.setExtendedState(JFrame.NORMAL);
		this.setVisible(true);
	}
}
