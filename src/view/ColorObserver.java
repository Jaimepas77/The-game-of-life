package view;

import java.awt.Color;

public interface ColorObserver {
	public default void onFirstColorUpdate(Color c) {}
	public default void onSecondColorUpdate(Color c) {}
}
