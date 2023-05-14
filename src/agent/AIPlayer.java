package agent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

import game.Farkle;

/**
 * The Class AIPlayer.
 */
public class AIPlayer {

	/** The lt. */
	LocalTime lt = LocalTime.now();

	/**
	 * Instantiates a new AI player.
	 */
	public AIPlayer() {
	}

	/**
	 * Select all unselected die.
	 *
	 * @param game the game
	 * @return the boolean[]
	 */
	public boolean[] selectAllUnselectedDie(Farkle game) {
		ArrayList<boolean[]> potentialSelections = getBoolArr(6);
		double maxSelectionScore = 0.0;
		boolean[] bestSelection = new boolean[6];
		for (boolean[] curSelection : potentialSelections) {
			game.setSelection(curSelection.clone());
			double curSelectionScore = game.checkSelectionScore();
			if (curSelectionScore >= maxSelectionScore) {
				maxSelectionScore = curSelectionScore;
				bestSelection = curSelection.clone();
			}
		}
		return bestSelection;
	}

	/**
	 * Make selection.
	 *
	 * @param game the game
	 * @return the boolean[]
	 */
	public boolean[] makeSelection(Farkle game) {
		ArrayList<boolean[]> potentialSelections = getBoolArr(6);
		int locked = 0;
		for (int j = 0; j < 6; j++) {
			if (game.getLockDie()[j])
				locked++;
		}
		double maxSelectionScore = 0.0;
		boolean[] bestSelection = new boolean[6];
		for (boolean[] curSelection : potentialSelections) {
			game.setSelection(curSelection.clone());
			double curSelectionScore = game.checkSelectionScore();
			int selected = 0;
			if (curSelectionScore > 0) {
				if (!game.getPlayer_2().isHasScored()) {
					if (game.getPlayer_2().getScore() + game.getPlayer_2().getTurnScore() + curSelectionScore >= 500) {
						return selectAllUnselectedDie(game);
					}
				}
				for (boolean b : curSelection) {
					if (b)
						selected++;
				}
				int dieLeft = 6 - locked - selected;
				curSelectionScore += (double) (maxPotential(dieLeft) * chanceToScore(dieLeft) * riskFactor(game));
				if (curSelectionScore >= maxSelectionScore) {
					maxSelectionScore = curSelectionScore;
					bestSelection = curSelection.clone();
				}
			}
		}
		System.out.println("AI: EXPECTED VALUE OF SELECTION = " + maxSelectionScore);
		return bestSelection;
	}

	/**
	 * Gets the bool arr.
	 *
	 * @param length the length
	 * @return the bool arr
	 */
	private ArrayList<boolean[]> getBoolArr(int length) {
		int numOptions = 1 << length;
		ArrayList<boolean[]> finalArray = new ArrayList<boolean[]>();
		for (int o = 0; o < numOptions; o++) {
			boolean[] newArr = new boolean[length];
			for (int l = 0; l < length; l++) {
				int val = (1 << l) & o;
				newArr[l] = val > 0;
			}
			finalArray.add(newArr);
		}
		return finalArray;
	}

	/**
	 * Chance to score.
	 *
	 * @param n the n
	 * @return the double
	 */
	private double chanceToScore(int n) {
		switch (n) {
		case 0:
			return 1 - .0231;
		case 1:
			return 1 - .6667;
		case 2:
			return 1 - .4444;
		case 3:
			return 1 - .2778;
		case 4:
			return 1 - .1574;
		case 5:
			return 1 - .0772;
		case 6:
			return 1 - .0231;
		default:
			return 0;
		}
	}

	/**
	 * Max potential.
	 *
	 * @param n the n
	 * @return the double
	 */
	private double maxPotential(int n) {
		switch (n) {
		case 0:
			return 3000.0;
		case 1:
			return 100.0;
		case 2:
			return 200.0;
		case 3:
			return 600.0;
		case 4:
			return 1000.0;
		case 5:
			return 2000.0;
		case 6:
			return 3000.0;
		default:
			return 0.0;
		}
	}

	/**
	 * Roll again.
	 *
	 * @param game the game
	 * @return true, if successful
	 */
	public boolean rollAgain(Farkle game) {
		if (!game.getActive_player().canPass()) {
			System.out.println("AI: MUST ROLL AGAIN");
			return true;
		}
		if ((game.getPlayer_1().isWinner() && (game.getPlayer_1()
				.getScore() > (game.getPlayer_2().getScore() + game.getPlayer_2().getTurnScore())))) {
			System.out.println("AI: LAST CHANCE TO WIN. KEEP GOING");
			return true;
		}
		int locked = 0;
		for (int j = 0; j < 6; j++) {
			if (game.getLockDie()[j])
				locked++;
		}
		if (!game.getPlayer_2().isHasScored()
				&& (game.getPlayer_2().getScore() + game.getPlayer_2().getTurnScore() > 500)) {
			System.out.println("AI: GET ON THE BOARD TO START");
			return false;
		}
		Random rand = new Random();
		double risk = .5 * Math.atan(6.0 * (((3.0 * chanceToScore(6 - locked) + riskFactor(game)) / 4.0) - .66)) + .6;
		if (game.getPlayer_2().getFarkles() == 2) {
			risk /= 2.0;
		}
		double again = rand.nextDouble();
		System.out.println("AI: DIE FOR NEXT ROLL = " + (6 - locked));
		System.out.println("AI: CHANCE TO ROLL AGAIN = " + risk);
		if (again <= risk) {
			return true;
		}
		return false;
	}

	/**
	 * Risk factor.
	 *
	 * @param game the game
	 * @return the double
	 */
	public double riskFactor(Farkle game) {
		int difference = game.getPlayer_1().getScore()
				- (game.getPlayer_2().getScore() + 2 * game.getPlayer_2().getTurnScore());

		if (difference < -5000)
			return 0.0;
		else if (difference >= -5000 && difference < -3000)
			return 1.0 / 6.0;
		else if (difference >= -3000 && difference < -1000)
			return 2.0 / 6.0;
		else if (difference >= -1000 && difference < 1000)
			return 3.0 / 6.0;
		else if (difference >= 1000 && difference < 3000)
			return 4.0 / 6.0;
		else if (difference >= 3000 && difference < 5000)
			return 5.0 / 6.0;
		else
			return 1.0;
	}

}
