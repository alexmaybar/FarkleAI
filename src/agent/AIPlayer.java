package agent;

import java.util.ArrayList;

import game.Farkle;

public class AIPlayer {

	public AIPlayer() {
	}

	public double riskFactor(Farkle game) {
		return .50;
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
				System.out.println("Max potential " + (6 - locked - selected) + " :  " + maxPotential(6 - locked - selected));
				curSelectionScore += (maxPotential(6 - locked - selected));
				if (curSelectionScore >= maxSelectionScore) {
					System.out.println("Change max to " + curSelectionScore);
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

	private double maxPotential(int n) {
		switch (n) {
		case 0:
			return 3000;
		case 1:
			return 100;
		case 2:
			return 200;
		case 3:
			return 600;
		case 4:
			return 1000;
		case 5:
			return 2000;
		case 6:
			return 3000;
		default:
			return 0;
		}
	}
}
