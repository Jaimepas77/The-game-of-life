package view;

import java.util.Scanner;

import model.Game;

public class BatchUI implements Viewer {
	private static Scanner in = new Scanner(System.in);
	private static boolean exit = false;
	private Game game;
	
	public BatchUI(Game game) {
		this.game = game;
		runUI();
	}
	
	public void runUI() {
		while(!exit) {
			System.out.println(game.getStringBoard()); // refreshBoard

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

	private String[] getCommand() {
		System.out.print(">");
		String ret[] = in.nextLine().split(" ");
		return ret;
	}

	private void executeCommand(String[] args) throws IllegalArgumentException {
		//Los comandos se añaden aquí
		switch(args[0]) {
		case "on":
			game.setSquareState(true, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			break;
		case "off":
			game.setSquareState(false, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			break;
		case "step":
			if(args.length == 1) {
				game.step();
			}
			else if (args.length == 2) {
				for(int i = 0; i < Integer.parseInt(args[1]); i++)
					game.step();
			}
			else {
				throw new IllegalArgumentException();
			}
			break;
		case "exit":
			exit = true;
			break;
		case "help":
			System.out.println("Helping...");
			System.out.println("on <x> <y>: switch on square in the row x and column y");
			System.out.println("off <x> <y>: switch off square in the row x and column y");
			System.out.println("step [<x>]: execute x steps of the game (x is 1 if not especified)");
			System.out.println("exit: finishes the program");
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

}
