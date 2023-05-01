package client;

import java.util.Scanner;

import game.Farkle;

/**
 * The Console version of the Game.
 */
public class ConsoleDriver {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Farkle consoleGame = new Farkle();

		System.out.println("----------------------------------");
		System.out.println("---- Welcome to Farkle Client ----");
		System.out.println("----------------------------------");
		printState(consoleGame);
		do {
			rollDie(consoleGame);
			if (consoleGame.detectFarkle()) {
				System.out.println(" FARKLE!");
				System.out.println(" boo... switching player");
				consoleGame.passTurn(true);
				printState(consoleGame);
			} else {
				boolean madeSelection = false;
				while (!madeSelection) {
					boolean[] selection = new boolean[6];
					String prompt = " Which indecies would you like to keep? (1-6 space seperated)(-1 to end)";
					System.out.println(prompt);
					boolean stop = false;
					while (!stop) {
						try {
							int index = Integer.parseInt(in.next());
							if (index == -1) {
								stop = true;
							} else if (index <= 6 && index > 0) {
								selection[index - 1] = true;
							}
						} catch (Exception e) {
							System.out.print(" That's not a number from 1 to 6.\n" + prompt);
						}
					}
					consoleGame.setSelection(selection);
					int selectionScore = consoleGame.checkSelectionScore();
					if(selectionScore == -1) {
						System.out.println(" Invalid Selection");
					} else {
						System.out.println(" Selection Score: " + selectionScore);
						System.out.println(" Keep Selection? (Y/N)");
						if (in.next().toLowerCase().charAt(0) == 'y') {
							madeSelection = true;
						}
					}
				}
				System.out.println(" Selection Made");
				consoleGame.scoreSelection();
				System.out.println(" " + consoleGame.getActive_player().getName() + " TURN SCORE: "
						+ consoleGame.getActive_player().getTurnScore());
				if (consoleGame.getActive_player().canPass()) {
					System.out.println(" Would you like to roll again? (Y/N)");
					if (in.next().toLowerCase().charAt(0) != 'y') {
						System.out.println(" boo... switching player");
						consoleGame.passTurn(false);
						printState(consoleGame);
					}
				}

			}
		} while (!consoleGame.gameOver());
		System.out.println(consoleGame.getActive_player().getName() + " has won!");
	}

	/**
	 * Prints the current game state.
	 *
	 * @param consoleGame the console game
	 */
	private static void printState(Farkle consoleGame) {
		String out = "\n";
		out += "----------------------------------\n";
		out += " Turn: " + consoleGame.getTurn_count() + "\n";
		out += " Active Player: " + consoleGame.getActive_player() + "\n";
		out += " GAME SCORE: " + consoleGame.getActive_player().getScore() + "\n";
		out += " TURN SCORE: " + consoleGame.getActive_player().getTurnScore() + "\n";
		out += " \n";

		System.out.print(out);
	}

	/**
	 * Roll die. Print results.
	 *
	 * @param consoleGame the console game
	 */
	private static void rollDie(Farkle consoleGame) {
		System.out.println(" rolling...\n");
		String out = "";
		out += " ROLLED DIE ----------------------\n";
		out += "  Index:  | 1 | 2 | 3 | 4 | 5 | 6 |\n";
		out += "  Value: ";
		consoleGame.rollDice();
		int[] diePool = consoleGame.getDiePool();
		boolean[] lockDie = consoleGame.getLockDie();
		for (int i = 0; i < 6; i++) {
			if(!lockDie[i]) {
				out += " | ";
				out += diePool[i];
			} else {
				out += " |  "; 
			}
		}
		out += " |\n\n";
		System.out.print(out);

	}
}
