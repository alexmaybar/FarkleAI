package game;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Farkle.
 * Holds relevant information for a game of Farkle
 */
public class Farkle {
	
	/** The score to win. */
	final private int SCORE_TO_WIN = 10000;
	
	/** The random number generator. */
	private Random r;

	/** The die pool. These are the die currently in play.  */
	private int[] diePool;
	
	/** The locked die. These are the die that the player has saved this turn. */
	private boolean[] lockDie;
	
	/** The selection. These are the die that the player has marked for scoring this turn */
	private boolean[] selection;
	
	/** The current turn of the game. */
	private int turn_count;
	
	/**  Holds true is a die has been scored this turn *. */
	private boolean hasScored;
	
	/** The active player. */
	private Player active_player;
	
	private Player winner;

	/** player 1. */
	private Player player_1;
	
	/** player 2. */
	private Player player_2;

	/**
	 * Gets the current die pool.
	 *
	 * @return the die pool
	 */
	public int[] getDiePool() {
		return diePool;
	}

	/**
	 * Gets the locked die.
	 *
	 * @return the lock die
	 */
	public boolean[] getLockDie() {
		return lockDie;
	}
	
	/**
	 * Gets the current selection.
	 *
	 * @return the selection
	 */
	public boolean[] getSelection() {
		return selection;
	}

	/**
	 * Sets the selection.
	 *
	 * @param selection the new selection
	 */
	public void setSelection(boolean[] selection) {
		this.selection = selection;
	}

	/**
	 * Gets the turn count.
	 *
	 * @return the turn count
	 */
	public int getTurn_count() {
		return turn_count;
	}

	/**
	 * Gets the active player.
	 *
	 * @return the active player
	 */
	public Player getActive_player() {
		return active_player;
	}
	
	/**
	 * Gets the player 1.
	 *
	 * @return the player 1
	 */
	public Player getPlayer_1() {
		return player_1;
	}

	/**
	 * Sets the player 1.
	 *
	 * @param player_1 the new player 1
	 */
	public void setPlayer_1(Player player_1) {
		this.player_1 = player_1;
	}

	/**
	 * Gets the player 2.
	 *
	 * @return the player 2
	 */
	public Player getPlayer_2() {
		return player_2;
	}

	/**
	 * Sets the player 2.
	 *
	 * @param player_2 the new player 2
	 */
	public void setPlayer_2(Player player_2) {
		this.player_2 = player_2;
	}

	/**
	 * Instantiates a new Farkle game.
	 */
	public Farkle() {
		r = new Random();

		diePool = new int[6];
		lockDie = new boolean[6];
		selection = new boolean[6];
		
		//starts as true so the first player can roll
		hasScored = true;
		
		player_1 = new Player("Player 1");
		player_2 = new Player("Player 2");
		winner = null;
		
		active_player = player_1;
		turn_count = 1;
	}
	
	/**
	 * Checks if the last player (player 2) is active.
	 *
	 * @return true, if successful
	 */
	public boolean endOfRound() {
		return active_player == player_2;
	}
	
	/**
	 * checks if the game is over.
	 *
	 * @return true, if successful
	 */
	public boolean gameOver() {
		if (player_1.isWinner() && player_2.isWinner()) {
			if(player_1.getScore() > player_2.getScore()) {
				active_player = player_1;
				player_2.setWinner(false);
				return true;
			} else if (player_1.getScore() < player_2.getScore()){
				active_player = player_2;
				player_1.setWinner(false);
				return true;
			} else {
				return true; //Tie goes to first player to go out
			}
		} else if (active_player.isWinner()){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the current player can roll.
	 *
	 * @return true, if successful
	 */
	public boolean canRoll() {
		return hasScored;
	}
	

	/**
	 * Rolls the dice that are not locked.
	 */
	public void rollDice() {
		boolean full = true;
		for (boolean b : lockDie) {
			if(b == false)
				full = false;
		}
		if(full) {
			lockDie = new boolean[6];
			selection = new boolean[6];
		}
		for (int i = 0; i < 6; i++) {
			if (lockDie[i] != true) { //Die is not locked
				diePool[i] = r.nextInt(6)+1;
			}
		}
		hasScored = false;
	}
	
	/**
	 * Pass turn to the next player.
	 *
	 * @param farkle the farkle
	 */
	public void passTurn(boolean farkle) {
		if(farkle) {
			active_player.farkle();
		} else {
			active_player.endTurn();
		}
		if(active_player.getScore() >= SCORE_TO_WIN) {
			active_player.setWinner(true);
		}
		if(active_player == player_1) {
			active_player = player_2;
		} else {
			turn_count++;
			active_player = player_1;
		}
		diePool = new int[6];
		lockDie = new boolean[6];
		selection = new boolean[6];
		hasScored = true;
	}

	/**
	 * Detect if the farkle.
	 *
	 * @return true, if successful
	 */
	public boolean detectFarkle() {
		boolean farkle = true;
		farkle = (checkThreeOfAKind()) ? false : farkle;
		farkle = (checkOnes()) ? false : farkle;
		farkle = (checkFives()) ? false : farkle;
		farkle = (checkStraight()) ? false : farkle;
		farkle = (checkThreePairs()) ? false : farkle;
		farkle = (checkFourOfAKind()) ? false : farkle;
		farkle = (checkFiveOfAKind()) ? false : farkle;
		farkle = (checkSixOfAKind()) ? false : farkle;
		return farkle;
	}
	
	/**
	 * Score the current selection.
	 */
	public void scoreSelection() {
		int score = 0;
		score += scoreSixOfAKind();
		score += scoreStraight();
		score += scoreFiveOfAKind();
		score += scoreThreePairs();
		score += scoreFourOfAKind();
		score += scoreThreeOfAKind();
		score += scoreOnes();
		score += scoreFives();
		if (score > 0) hasScored = true;
		active_player.increaseScore(score);
	}
	
	/**
	 * Check selection score.
	 *
	 * @return the int
	 */
	public int checkSelectionScore() {
		int score = 0;
		boolean[] lockDieClone = lockDie.clone();
		boolean[] selectionClone = selection.clone();
		
		score += scoreSixOfAKind();
		score += scoreStraight();
		score += scoreFiveOfAKind();
		score += scoreThreePairs();
		score += scoreFourOfAKind();
		score += scoreThreeOfAKind();
		score += scoreOnes();
		score += scoreFives();
		
		for (boolean b : selection) {
			if(b) score = -1;
		}
		
		selection = selectionClone;
		lockDie = lockDieClone;
		return score;
	}
	
	/**
	 * Gets the number of each number rolled.
	 *
	 * @return the num of nums
	 */
	public int[] getNumOfNumsCheck() {
		int[] numOfNums = new int[6];
		for(int i = 0; i < 6; i++) {
			if(lockDie[i] != true)
				numOfNums[diePool[i]-1]++;
		}
		return numOfNums;
	}
	
	/**
	 * Gets the number of each number rolled and selected.
	 *
	 * @return the num of nums
	 */
	private int[] getNumOfNumsScore(){
		int[] numOfNums = new int[6];//array to count the amount of each number(i.e. index 0 is num of 1s)
		for(int i = 0; i < diePool.length; i++){
			if(selection[i] && !lockDie[i]){
				numOfNums[diePool[i] - 1]++;
			}
		}
		return numOfNums;
	}

	/**
	 * Check six of A kind.
	 *
	 * @return true, if successful
	 */
	private boolean checkSixOfAKind() {
		int[] numOfNums = getNumOfNumsCheck();
		for (int i : numOfNums) {
			if (i == 6) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Score six of A kind.
	 *
	 * @return the score
	 */
	private int scoreSixOfAKind() {
		int score = 0;
		int[] numOfNums = getNumOfNumsScore();
		for(int i = 0; i < numOfNums.length; i++){
			if(numOfNums[i] == 6){
				score  += 3000;
				int numUsed = 0;
				for(int j = 0; j < diePool.length; j++){
					if(diePool[j] == i + 1 && selection[j] && numUsed < 6 && !lockDie[j]){
						lockDie[j] = true;
						selection[j] = false;//remove dice so they aren't scored twice
						numUsed++;
					}
				}
			}
		}
		return score;
	}

	/**
	 * Check five of A kind.
	 *
	 * @return true, if successful
	 */
	private boolean checkFiveOfAKind() {
		int[] numOfNums = getNumOfNumsCheck();
		for (int i : numOfNums) {
			if (i == 5) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Score five of A kind.
	 *
	 * @return the score
	 */
	private int scoreFiveOfAKind() {
		int score = 0;
		int[] numOfNums = getNumOfNumsScore();
		for(int i = 0; i < numOfNums.length; i++){
			if(numOfNums[i] == 5){
				score  += 2000;
				int numUsed = 0;
				for(int j = 0; j < diePool.length; j++){
					if(diePool[j] == i + 1 && selection[j] && numUsed < 5 && !lockDie[j]){
						lockDie[j] = true;
						selection[j] = false;//remove dice so they aren't scored twice
						numUsed++;
					}
				}
			}
		}
		return score;
	}

	/**
	 * Check four of A kind.
	 *
	 * @return true, if successful
	 */
	private boolean checkFourOfAKind() {
		int[] numOfNums = getNumOfNumsCheck();
		for (int i : numOfNums) {
			if (i == 4) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Score four of A kind.
	 *
	 * @return the score
	 */
	private int scoreFourOfAKind() {
		int score = 0;
		int[] numOfNums = getNumOfNumsScore();
		for(int i = 0; i < numOfNums.length; i++){
			if(numOfNums[i] == 4){
				score  += 1000;
				int numUsed = 0;
				for(int j = 0; j < diePool.length; j++){
					if(diePool[j] == i + 1 && selection[j] && numUsed < 4 && !lockDie[j]){
						lockDie[j] = true;
						selection[j] = false;//remove dice so they aren't scored twice
						numUsed++;
					}
				}
			}
		}
		return score;
	}
	
	/**
	 * Check three of A kind.
	 *
	 * @return true, if successful
	 */
	private boolean checkThreeOfAKind() {
		int[] numOfNums = getNumOfNumsCheck();
		for (int i : numOfNums) {
			if (i == 3) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Score three of A kind.
	 *
	 * @return the score
	 */
	private int scoreThreeOfAKind() {
		int score = 0;
		int[] numOfNums = getNumOfNumsScore();
		for(int i = 0; i < numOfNums.length; i++){
			if(numOfNums[i] >= 3){
				if(i == 0) {
					score += 300;
				} else  {
					score += (i + 1) * 100; //add 100 times dice value for other 3 of a kinds
				}
				int numUsed = 0;
				for(int j = 0; j < diePool.length; j++){
					if(diePool[j] == i + 1 && selection[j] && numUsed < 3 && !lockDie[j]){
						lockDie[j] = true;
						selection[j] = false;//remove dice so they aren't scored twice
						numUsed++;
					}
				}
			}
		}
		return score;
	}

	/**
	 * Check three pairs.
	 *
	 * @return true, if successful
	 */
	private boolean checkThreePairs() {
		int[] numOfNums = getNumOfNumsCheck();
		int numPairs = 0;
		for(int i = 0; i < 6; i++){
			if(numOfNums[i] >= 2){
				numPairs++;
			}
		}
		return numPairs == 3;
	}
	
	/**
	 * Score three pairs.
	 *
	 * @return the score
	 */
	private int scoreThreePairs() {
		int pairs = 0;
		int[] numOfNums = getNumOfNumsScore();
		for(int i = 0; i < numOfNums.length; i++){
			if(numOfNums[i] == 4){
				pairs += 2;
			}else if(numOfNums[i] == 2){
				pairs++;
			}
		}
		if(pairs == 3){
			for(int i = 0; i < numOfNums.length; i++){
				if(numOfNums[i] == 4){
					for(int j = 0; j < diePool.length; j++){
						if(diePool[j] == i + 1 && selection[j] && !lockDie[j]){
							lockDie[j] = true;
							selection[j] = false;
						}
					}
				}else if(numOfNums[i] == 2){
					for(int j = 0; j < diePool.length; j++){
						if(diePool[j] == i + 1 && selection[j] && !lockDie[j]){
							lockDie[j] = true;
							selection[j] = false;
						}
					}
				}
			}
			return 1500;
		}
		return 0;
	}

	/**
	 * Check straight.
	 *
	 * @return true, if successful
	 */
	private boolean checkStraight() {
		int[] numOfNums = getNumOfNumsCheck();
		for(int i = 0; i < 6; i++){
			if(numOfNums[i] != 1){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Score straight.
	 *
	 * @return the score
	 */
	private int scoreStraight() {
		int[] numOfNums = getNumOfNumsScore();
		for (int i : numOfNums) {
			if(i != 1)
				return 0;
		}
		for(int i = 0; i < diePool.length; i ++) {
			lockDie[i] = true;
			selection[i] = false;
		}
		return 3000;
	}

	/**
	 * Check fives.
	 *
	 * @return true, if successful
	 */
	private boolean checkFives() {
		for (int i = 0; i < 6; i++) {
			if(lockDie[i] != true && diePool[i] == 5) 
				return true;
		}
		return false;
	}

	/**
	 * Score fives.
	 *
	 * @return the score
	 */
	private int scoreFives() {
		int score = 0;
		for(int i = 0; i < diePool.length; i++){
			if(diePool[i] == 5 && selection[i] && !lockDie[i]){
				lockDie[i] = true;
				selection[i] = false;
				score += 50;
			}
		}
		return score;
	}
	
	/**
	 * Check ones.
	 *
	 * @return true, if successful
	 */
	private boolean checkOnes() {
		for (int i = 0; i < 6; i++) {
			if(lockDie[i] != true && diePool[i] == 1) 
				return true;
		}
		return false;
	}
	
	/**
	 * Score ones.
	 *
	 * @return the score
	 */
	private int scoreOnes() {
		int score = 0;
		for(int i = 0; i < diePool.length; i++){
			if(diePool[i] == 1 && selection[i] && !lockDie[i]){
				lockDie[i] = true;
				selection[i] = false;
				score += 100;
			}
		}
		return score;
	}

}
