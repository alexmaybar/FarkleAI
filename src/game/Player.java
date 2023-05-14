package game;

/**
 * The Class Player. Holds player information.
 */
public class Player {

	/** The score. */
	private int score;

	/** The current turn score. */
	private int turnScore;

	/** The number of farkles in a row the player has. */
	private int farkles;

	/** The name of the player. */
	private String name;

	/** Is this player a winner. */
	private boolean winner;
	
	/** Has the player scored in this game **/
	private boolean hasScored;

	/**
	 * Gets the players current score.
	 *
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Gets the score for the current turn.
	 *
	 * @return the turn score
	 */
	public int getTurnScore() {
		return turnScore;
	}

	/**
	 * Gets the name of the player.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the number of farkles.
	 *
	 * @return the farkles
	 */
	public int getFarkles() {
		return farkles;
	}

	public boolean isHasScored() {
		return hasScored;
	}

	public void setHasScored(boolean hasScored) {
		this.hasScored = hasScored;
	}

	/**
	 * Sets the value of winner.
	 *
	 * @param won the new winner
	 */
	public void setWinner(boolean won) {
		this.winner = won;
	}

	/**
	 * Checks if player is winner.
	 *
	 * @return true, if is winner
	 */
	public boolean isWinner() {
		return this.winner;
	}

	/**
	 * Instantiates a new player.
	 *
	 * @param name the name
	 */
	public Player(String name) {
		this.score = 0;
		this.turnScore = 0;
		this.farkles = 0;
		this.name = name;
		this.winner = false;
		this.hasScored = false;
	}

	/**
	 * Increases current turn scores.
	 *
	 * @param score the score to increase the current turn's score
	 */
	public void increaseScore(int score) {
		turnScore += score;
	}

	/**
	 * Chech if the Player can pass.
	 *
	 * @return true, if successful
	 */
	public boolean canPass() {
		if ((!hasScored && turnScore < 500) || turnScore == 0)
			return false;
		return true;
	}

	/**
	 * End the current turn.
	 */
	public void endTurn() {
		score += turnScore;
		turnScore = 0;
		farkles = 0;
		hasScored = true;
	}

	/**
	 * Farkle. Handles what happens when the player farkles.
	 */
	public void farkle() {
		farkles++;
		turnScore = 0;
		if (farkles == 3) {
			score -= 1000;
			farkles = 0;
		}
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return name;
	}
}
