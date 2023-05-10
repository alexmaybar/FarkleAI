package client;

import game.Farkle;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * The Class InfoPane.
 */
public class InfoPane extends VBox {

	/** The active player lbl. */
	private Label activePlayerLbl;
	
	/** The active player score lbl. */
	private Label activePlayerScoreLbl;
	
	/** The active player turn score lbl. */
	private Label activePlayerTurnScoreLbl;
	
	/** The active player number of farkles lbl. */
	private Label activePlayerNumFarklesLbl;
	
	/** The cur selection score lbl. */
	private Label curSelectionScoreLbl;

	/**
	 * Instantiates a new info pane.
	 *
	 * @param game the game
	 */
	public InfoPane(Farkle game) {
		this.getStyleClass().add("pane");
		updateInfo(game);
	}
	
	/**
	 * Update information.
	 *
	 * @param game the game
	 */
	public void updateInfo(Farkle game) {
		this.getChildren().clear();
		
		activePlayerLbl = new Label("Active Player: " + game.getActive_player().getName());
		activePlayerLbl.setId("big-info-lbl");
		
		activePlayerScoreLbl = new Label("\tScore: " + game.getActive_player().getScore());
		activePlayerScoreLbl.setId("info-lbl");
		
		activePlayerTurnScoreLbl = new Label("\tTurn Score: " + game.getActive_player().getTurnScore());
		activePlayerTurnScoreLbl.setId("info-lbl");
		
		activePlayerNumFarklesLbl = new Label("\tFarkles: " + game.getActive_player().getFarkles());
		activePlayerNumFarklesLbl.setId("info-lbl");
		
		curSelectionScoreLbl = new Label("\tSelection Score: " + game.checkSelectionScore());
		if (game.checkSelectionScore() == -1) {
			curSelectionScoreLbl.setText("\tInvalid Selection");
		}
		curSelectionScoreLbl.setId("info-lbl");
		
		this.getChildren().addAll(activePlayerLbl, activePlayerScoreLbl, activePlayerNumFarklesLbl, activePlayerTurnScoreLbl,
				curSelectionScoreLbl);
	}
}
