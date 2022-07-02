package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

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

		//Board (CENTER)
		JPanel board = new BoardGrid(game);
		this.add(board, BorderLayout.CENTER);

		//Adapt the window appearance
		this.setBounds(200, 50, 900, 600);//Size and position if not maximized
		this.setMinimumSize(new Dimension(550, 400));//Minimum size
		//this.setExtendedState(JFrame.NORMAL);//Start the window maximized
		this.setVisible(true);
	}
}
