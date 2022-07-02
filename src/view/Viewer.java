package view;

public interface Viewer {

	public default void runUI() {
		System.err.println("Viewer not implemented yet");
	}
}
