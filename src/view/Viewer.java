package view;

import model.Game;

public abstract class Viewer {
	protected Game game;

	public Viewer(Game game) {
		this.game = game;
		run();
	}
	
	public void run() {
		System.err.println("Viewer not implemented");
	}
}
