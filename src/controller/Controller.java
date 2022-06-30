package controller;

import java.io.IOException;
import java.util.Scanner;

import model.Game;

public class Controller {
	public static final int DEFAULT_ROWS = 6;
	public static final int DEFAULT_COLLUMNS = 6;

	//the game itself
	private static Game game;

	private static boolean exit = false;
	private static Scanner in = new Scanner(System.in);
	//the interface (printline style class)

	public static void main(String[] args) {
		//Choose the interface type (batch - GUI)

		//Create game instance
		game = new Game(DEFAULT_ROWS, DEFAULT_COLLUMNS);

		//Run interface thread (passing the controller itself as a parameter)



		while(!exit) {
			System.out.println(game.getBoard()); // refreshBoard

			try {
				String[] command = getCommand();
				executeCommand(command);
			}
			catch(IllegalArgumentException e) {
				System.err.println("Comando no soportado");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static String[] getCommand() {
		System.out.print(">");
		String ret[] = in.nextLine().split(" ");
		return ret;
	}

	private static void executeCommand(String[] args) throws IllegalArgumentException {
		//Los comandos se añaden aquí
		switch(args[0]) {
		case "on":
			game.setSquareState(true, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			break;
		case "off":
			game.setSquareState(false, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			break;
		case "exit":
			exit = true;
			break;
		case "help":
			System.out.println("Helping...");
			System.out.println("on <x> <y>: switch on square in the row x and column y");
			System.out.println("off <x> <y>: switch off square in the row x and column y");
			System.out.println("exit: finish the program");
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

}
