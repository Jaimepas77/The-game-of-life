package model;

public interface GameObserver {

	public default void onBoardUpdate() {}
	public default void onRunningUpdate(boolean running) {}
	public default void onSelectionMode(int mode) {}
}
