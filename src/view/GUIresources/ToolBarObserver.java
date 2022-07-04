package view.GUIresources;

import java.awt.Color;

public interface ToolBarObserver {
	public default void onFirstColorUpdate(Color c) {}
	public default void onSecondColorUpdate(Color c) {}
}
