package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The Class TitleScreen.
 */
public class TitleScreen extends VBox{
	
	/** The title lbl. */
	private Label titleLbl;
	
	/** The options box. */
	private HBox optionsBox;
	
	/** The start btn. */
	private Button startBtn;
	
	/**
	 * Gets the start btn.
	 *
	 * @return the start btn
	 */
	public Button getStartBtn() {
		return startBtn;
	}
	
	/**
	 * Instantiates a new title screen.
	 *
	 * @param handler the handler
	 */
	public TitleScreen(EventHandler<ActionEvent> handler) {
		this.setAlignment(Pos.CENTER);
		this.getStyleClass().add("pane");
		
		titleLbl = new Label("FARKLE");	
		titleLbl.setId("title");
		
		optionsBox = new HBox();
		optionsBox.setAlignment(Pos.CENTER);
		optionsBox.getChildren().add(new Label("Options here"));
		
		startBtn = new Button("Start Game");
		startBtn.setOnAction(handler);
		
		this.getChildren().addAll(titleLbl, optionsBox, startBtn);
	}

}
