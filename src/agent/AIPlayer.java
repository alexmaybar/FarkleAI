package agent;

import game.Player;
import java.util.Random;

public class AIPlayer extends Player {

	double[] farkles = new double[] { 2.0 / 3.0, 1.0 / 2.3, 1.0 / 3.6, 1.0 / 6.4, 1.0 / 13.0, 1.0 / 43.2 };

	public AIPlayer() {
		super("AI");
		// TODO Auto-generated constructor stub
	}

	public boolean rollAgain() {
		Random rand = new Random();
		if (rand.nextDouble() <= riskFactor())
			return true;
		return false;
	}

	public double riskFactor() {
		int difference = game.player_1.score - game.player_2.score;

		if (difference < -5000)
			return 0;
		else if (difference >= -5000 && difference < -3000)
			return 1 / 6;
		else if (difference >= -3000 && difference < -1000)
			return 2 / 6;
		else if (difference >= -1000 && difference < 1000)
			return 3 / 6;
		else if (difference >= 1000 && difference < 3000)
			return 4 / 6;
		else if (difference >= 3000 && difference < 5000)
			return 5 / 6;
		else
			return 1;
	}

}
