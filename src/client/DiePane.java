package client;

import game.Farkle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The Class DiePane.
 */
public class DiePane extends VBox{

	/** The die box. */
	private HBox dieBox;
	
	/** The options box. */
	private HBox optionsBox;
	
	/** The die imgs. */
	private ImageView[] dieImgs;

	/** The roll die btn. */
	private Button rollDieBtn;
	
	/** The score die btn. */
	private Button scoreDieBtn;
	
	/** The pass btn. */
	private Button passBtn;
	
	/** The roll msg lbl. */
	private Label rollMsgLbl;

	/** The die pool array. */
	private int[] die;
	
	/** The locked die array. */
	private boolean[] locked;
	
	/** The selection array. */
	private boolean[] selection;

	/**
	 * Gets the roll die btn.
	 *
	 * @return the roll die btn
	 */
	public Button getRollDieBtn() {
		return rollDieBtn;
	}
	
	/**
	 * Gets the score die btn.
	 *
	 * @return the score die btn
	 */
	public Button getScoreDieBtn() {
		return scoreDieBtn;
	}
	
	/**
	 * Gets the die imgs.
	 *
	 * @return the die imgs
	 */
	public ImageView[] getDieImgs() {
		return dieImgs;
	}
	
	/**
	 * Gets the selection.
	 *
	 * @return the selection
	 */
	public boolean[] getSelection() {
		return selection;
	}
	
	/**
	 * Sets the selection.
	 *
	 * @return the selection
	 */
	public void setSelection(boolean[] selection) {
		this.selection = selection;
	}
	
	/**
	 * Gets the pass btn.
	 *
	 * @return the pass btn
	 */
	public Button getPassBtn() {
		return passBtn;
	}

	/**
	 * Clear selection array.
	 */
	public void clearSelection() {
		selection = new boolean[6];
	}

	/**
	 * Instantiates a new die pane.
	 *
	 * @param handler the handler
	 */
	public DiePane(EventHandler<ActionEvent> handler) {
		this.setAlignment(Pos.CENTER);
		this.getStyleClass().add("pane");
		
		die = new int[6];
		locked = new boolean[6];
		selection = new boolean[6];

		dieBox = new HBox();
		dieBox.setAlignment(Pos.CENTER);

		optionsBox = new HBox();
		optionsBox.setAlignment(Pos.CENTER);

		rollDieBtn = new Button("Roll Dice");
		rollDieBtn.setOnAction(handler);

		scoreDieBtn = new Button("Score Dice");
		scoreDieBtn.setOnAction(handler);
		
		passBtn = new Button("Pass Turn");
		passBtn.setOnAction(handler);
		
		rollMsgLbl = new Label("Roll the dice!");
		rollMsgLbl.setId("info-lbl");
		this.getChildren().add(rollMsgLbl);
		
		optionsBox.getChildren().addAll(rollDieBtn, scoreDieBtn, passBtn);

		this.getChildren().addAll(dieBox, optionsBox);
	}

	/**
	 * Update state of the die from the game.
	 *
	 * @param game the game
	 * @param client the client
	 */
	public void updateState(Farkle game, ClientDriver client) {
		die = game.getDiePool();
		locked = game.getLockDie();

		this.getChildren().clear();
		dieBox = new HBox();
		dieBox.setAlignment(Pos.CENTER);
		dieBox.setSpacing(15);
		dieImgs = new ImageView[6];
		
		boolean dieAdded = false;
		
		if(die[0]!=0) {
			for (int i = 0; i < die.length; i++) {
				if(!locked[i]) {
					Image img = new Image(die[i] + ".png");
					dieImgs[i] = new ImageView(img);
					dieImgs[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent evt) {
							for(int i = 0; i < dieImgs.length; i++) {
								if(evt.getSource() == dieImgs[i]) {
									if(!selection[i]) {
										//System.out.println("Set true");
										selection[i] = true;
										dieImgs[i].setId("img");
									} else {
										//System.out.println("Set false");
										selection[i] = false;
										dieImgs[i].setId("");
									}
									client.selectionRefresh();
								}
							}
						}
					});
					dieImgs[i].setFitWidth(50);
					dieImgs[i].setFitHeight(50);
					if(selection[i]) {
						dieImgs[i].setId("img");
					}
					dieBox.getChildren().add(dieImgs[i]);
					dieAdded = true;
				}
			}
			this.getChildren().add(dieBox);
		}
		if (!dieAdded) {
			this.getChildren().add(rollMsgLbl);
		}
		
		this.getChildren().add(optionsBox);
		
		/* Enable/Disable Buttons */
		if(game.getActive_player() == game.getPlayer_1()) {
			passBtn.setDisable(!game.getActive_player().canPass());
			scoreDieBtn.setDisable(game.checkSelectionScore() <= 0);
			rollDieBtn.setDisable(!game.canRoll());
		} else {
			passBtn.setDisable(true);
			scoreDieBtn.setDisable(true);
			rollDieBtn.setDisable(true);
		}
		
	}

	/**
	 * Display farkle message to user.
	 *
	 * @param msg the msg
	 * @param clientDriver the client driver
	 */
	public void farkle(String msg, ClientDriver clientDriver) {
		this.getChildren().clear();
		Button farklePass = new Button("Pass turn");
		farklePass.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				clientDriver.refresh();
				if (clientDriver.isAIturn()) {
					clientDriver.AIturn();
				} else {
					clientDriver.refresh();
				}
			}
		});
		Label farkleMsg = new Label(msg);
		farkleMsg.setId("big-info-lbl");
		this.getChildren().addAll(farkleMsg, farklePass);
	}


}
