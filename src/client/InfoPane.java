package client;

import game.Farkle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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

	/** The reset button. */
	private Button resetButton;
	
	/**
	 * Gets the reset button.
	 *
	 * @return the reset button
	 */
	public Button getResetButton() {
		return resetButton;
	}
	
	/**
	 * Instantiates a new info pane.
	 *
	 * @param game the game
	 * @param handler the handler
	 */
	public InfoPane(Farkle game, EventHandler<ActionEvent> handler) {
		this.getStyleClass().add("pane");
		
		resetButton = new Button("Reset");
		resetButton.setOnAction(handler);
		
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
		
		this.getChildren().addAll(resetButton, activePlayerLbl, activePlayerScoreLbl, activePlayerNumFarklesLbl, activePlayerTurnScoreLbl,
				curSelectionScoreLbl);
		
		
	}
}
