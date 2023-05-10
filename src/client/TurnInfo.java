package client;

import javafx.beans.property.SimpleStringProperty;

/**
 * The Class TurnInfo.
 */
public class TurnInfo {
	
	/** The turn number. */
	private SimpleStringProperty turnNumber;
	
	/** The player 1 info. */
	private SimpleStringProperty p1;
	
	/** The player 2 info. */
	private SimpleStringProperty p2;

	/**
	 * Instantiates a new turn info.
	 */
	public TurnInfo() {
	}

	/**
	 * Instantiates a new turn info.
	 *
	 * @param s1 the s 1
	 * @param s2 the s 2
	 * @param s3 the s 3
	 */
	public TurnInfo(String s1, String s2, String s3) {
		turnNumber = new SimpleStringProperty(s1);
		p1 = new SimpleStringProperty(s2);
		p2 = new SimpleStringProperty(s3);
	}

	/**
	 * Gets the turn number.
	 *
	 * @return the turn number
	 */
	public String getTurnNumber() {

		return turnNumber.get();
	}

	/**
	 * Sets the turn number.
	 *
	 * @param s the new turn number
	 */
	public void setTurnNumber(String s) {

		turnNumber.set(s);
	}

	/**
	 * Gets the p1.
	 *
	 * @return the p1
	 */
	public String getP1() {

		return p1.get();
	}

	/**
	 * Sets the p1.
	 *
	 * @param s the new p1
	 */
	public void setP1(String s) {

		p1.set(s);
	}

	/**
	 * Gets the p2.
	 *
	 * @return the p2
	 */
	public String getP2() {

		return p2.get();
	}

	/**
	 * Sets the p2.
	 *
	 * @param s the new p2
	 */
	public void setP2(String s) {

		p2.set(s);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {

		return (turnNumber.get() + ", " + p1.get() + ", " + p2.get());
	}
}
