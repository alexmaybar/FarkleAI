package agent;

import java.util.ArrayList;
import java.util.Random;
import game.Farkle;

public class AIPlayer {
	
	public AIPlayer() {
	}

	public boolean[] makeSelection(Farkle game) {
		ArrayList<boolean[]> potentialSelections = getBoolArr(6);
		int locked = 0;
		for (int j = 0; j < 6; j++) {
			if(game.getLockDie()[j])locked++;
			System.out.println(j + " : " + game.getDiePool()[j] + " " + game.getLockDie()[j]);
		}
		double maxSelectionScore = 0.0;
		boolean[] bestSelection = new boolean[6];
		for (boolean[] curSelection : potentialSelections) {
			game.setSelection(curSelection.clone());
			double curSelectionScore = game.checkSelectionScore();
			int selected = 0;
			if(curSelectionScore > 0) {
				for (boolean b : curSelection) {
					if(b)selected++;
				}
				int dieLeft = 6 - locked - selected;
				curSelectionScore += (double)(maxPotential(dieLeft) * chanceToScore(dieLeft) * riskFactor(game));
				if (curSelectionScore >= maxSelectionScore) {
					System.out.println("Change max to " + curSelectionScore);
					System.out.println("Die Left: " + dieLeft);
					System.out.println("Chance to Score: " + chanceToScore(dieLeft));
					maxSelectionScore = curSelectionScore;
					bestSelection = curSelection.clone();
				}
			}
		}
		return bestSelection;
	}

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

	public boolean rollAgain(Farkle game) {
		if (!game.getActive_player().canPass()) {
			return true;
		}
		
		int locked = 0;
		for (int j = 0; j < 6; j++) {
			if(game.getLockDie()[j])locked++;
		}
		Random rand = new Random();
		//double risk = Math.pow(((riskFactor(game) + 3.0*chanceToScore(6-locked))/4.0), 2);
		double risk = .5*Math.atan(6.0*(((3.0*chanceToScore(6-locked) + riskFactor(game))/4.0)-.66)) + .6;
		System.out.println("Risk Factor of Roll Again: " + risk);
		double again = rand.nextDouble();
		System.out.println("Random number to Roll Again: " + again);
		if (again <= risk)
			return true;
		return false;
	}

	public double riskFactor(Farkle game) {
		int difference = game.getPlayer_1().getScore() - (game.getPlayer_2().getScore() + 2*game.getPlayer_2().getTurnScore());

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
