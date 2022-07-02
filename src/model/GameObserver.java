package model;

public interface GameObserver {

	public default void onBoardUpdate() {}
	public default void onElemUpdate() {}
}
