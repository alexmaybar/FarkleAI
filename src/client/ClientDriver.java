package client;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import agent.AIPlayer;
import game.Farkle;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The Class ClientDriver.
 */
public class ClientDriver extends Application implements EventHandler<ActionEvent> {

	/** The game. */
	private Farkle game;

	private AIPlayer ai;

	/** The root pane. */
	private BorderPane root;

	/** The scene. */
	private Scene scene;

	/** The title screen. */
	private TitleScreen title;

	/** The die pane. */
	private DiePane diePane;

	/** The info pane. */
	private InfoPane infoPane;

	/** The scoreboard. */
	private TableView<TurnInfo> scoreboard;

	/** The data. */
	private ObservableList<TurnInfo> data;

	/** The start over btn. */
	private Button startOverBtn;

	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 */
	@Override
	public void start(Stage primaryStage) {
		root = new BorderPane();
		scene = new Scene(root, 875, 400);
		scene.getStylesheets().add("styles.css");

		game = new Farkle();
		ai = new AIPlayer();
		data = FXCollections.observableList(new ArrayList<>());

		title = new TitleScreen(this);
		infoPane = new InfoPane(game, this);

		root.setCenter(title);

		scoreboard = new TableView<>();
		scoreboard.setEditable(true);
		scoreboard.setId("table");
		scoreboard.setItems(data);

		TableColumn<TurnInfo, String> turnCol = new TableColumn<TurnInfo, String>("Turn #");
		turnCol.setSortable(false);
		turnCol.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("turnNumber"));
		TableColumn<TurnInfo, String> p1Col = new TableColumn<TurnInfo, String>("Player 1");
		p1Col.setSortable(false);
		p1Col.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("p1"));
		scoreboard.getColumns().setAll(Arrays.asList(turnCol, p1Col));
		TableColumn<TurnInfo, String> p2Col = new TableColumn<TurnInfo, String>("Player 2");
		p2Col.setSortable(false);
		p2Col.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("p2"));
		scoreboard.getColumns().setAll(Arrays.asList(turnCol, p1Col, p2Col));

		primaryStage.setTitle("Farkle GUI");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Handler.
	 *
	 * @param evt the evt
	 */
	@Override
	public void handle(ActionEvent evt) {
		if (evt.getSource() == title.getStartBtn() || evt.getSource() == startOverBtn || evt.getSource() == infoPane.getResetButton()) {
			game = new Farkle();
			data = FXCollections.observableList(new ArrayList<>());
			ai = new AIPlayer();

			scoreboard = new TableView<>();
			scoreboard.setEditable(true);
			scoreboard.setId("table");
			scoreboard.setItems(data);

			TableColumn<TurnInfo, String> turnCol = new TableColumn<TurnInfo, String>("Turn #");
			turnCol.setSortable(false);
			turnCol.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("turnNumber"));
			TableColumn<TurnInfo, String> p1Col = new TableColumn<TurnInfo, String>("Player 1");
			p1Col.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("p1"));
			p1Col.setSortable(false);
			scoreboard.getColumns().setAll(Arrays.asList(turnCol, p1Col));
			TableColumn<TurnInfo, String> p2Col = new TableColumn<TurnInfo, String>("Player 2");
			p2Col.setCellValueFactory(new PropertyValueFactory<TurnInfo, String>("p2"));
			p2Col.setSortable(false);
			scoreboard.getColumns().setAll(Arrays.asList(turnCol, p1Col, p2Col));
			diePane = new DiePane(this);
			diePane.updateState(game, this);
			root.setCenter(diePane);
			root.setRight(scoreboard);
			root.setLeft(infoPane);
			refresh();

		} else if (evt.getSource() == diePane.getRollDieBtn()) {
			// Roll the dice
			game.rollDice();
			diePane.clearSelection();
			if (!game.detectFarkle()) {
				refresh();
			} else {
				farkle();
			}

		} else if (evt.getSource() == diePane.getScoreDieBtn()) {
			// Active Player Scores a selection
			game.scoreSelection();
			diePane.clearSelection();
			refresh();
		} else if (evt.getSource() == diePane.getPassBtn()) {
			pass();
		}
	}

	public void farkle() {
		// What to do when a player farkles
		String msg = game.getActive_player().getName() + " has farkled!";
		if (game.getActive_player().getFarkles() + 1 == 3) {
			msg += "\nYou Lose 1000 points!";
		}
		if (game.endOfRound()) {
			TurnInfo temp = data.get(data.size() - 1);
			data.remove(data.size() - 1);
			temp.setP2("FARKLE");
			data.add(new TurnInfo(temp.getTurnNumber(), temp.getP1(), temp.getP2()));
		} else {
			data.add(new TurnInfo(Integer.toString(game.getTurn_count()), "FARKLE", ""));
		}
		game.passTurn(true);
		infoPane.getResetButton().setDisable(false);
		diePane.farkle(msg, this);
	}

	public void pass() {
		if (isAIturn()) {
			System.out.println("AI: TURN SCORE = " + game.getPlayer_2().getTurnScore());
			System.out.println("AI: END TURN\n");
			infoPane.getResetButton().setDisable(false);
		}
		// Active Player passes turn
		if (game.endOfRound()) {
			TurnInfo temp = data.get(data.size() - 1);
			data.remove(data.size() - 1);
			temp.setP2(Integer.toString(game.getActive_player().getScore() + game.getActive_player().getTurnScore()));
			data.add(new TurnInfo(temp.getTurnNumber(), temp.getP1(), temp.getP2()));
		} else {
			data.add(new TurnInfo(Integer.toString(game.getTurn_count()),
					Integer.toString(game.getActive_player().getScore() + game.getActive_player().getTurnScore()), ""));
		}
		game.passTurn(false);
		if (isAIturn()) {
			AIturn();
		} else {
			refresh();
		}
	}

	/**
	 * Refresh the UI and alert the game of a selection change.
	 *
	 * @param i the i
	 */
	public void selectionRefresh(int i) {
		game.setSelection(diePane.getSelection().clone());
		refresh();
	}

	/**
	 * Refresh the state of the UI.
	 */
	public void refresh() {
		if (game.gameOver()) {
			gameOver();
		} else {
			diePane.updateState(game, this);
			infoPane.updateInfo(game);
		}
	}

	/**
	 * Game over screen.
	 */
	public void gameOver() {
		VBox gameOverBox = new VBox();
		gameOverBox.getStyleClass().add("pane");
		gameOverBox.setAlignment(Pos.CENTER);

		Label gameOverLbl = new Label("GAME OVER");
		gameOverLbl.setId("title");

		Label winnerLbl = new Label(game.getActive_player().getName().toUpperCase() + " WON!");
		winnerLbl.setId("big-info-lbl");

		startOverBtn = new Button("Start Over");
		startOverBtn.setOnAction(this);

		gameOverBox.getChildren().addAll(gameOverLbl, winnerLbl, startOverBtn);

		root.setLeft(null);
		root.setCenter(gameOverBox);
	}

	public boolean isAIturn() {
		return game.getActive_player() == game.getPlayer_2();
	}

	public void AIturn() {
		infoPane.getResetButton().setDisable(true);
		System.out.println("AI TURN #" + game.getTurn_count());
		AIturnR();
	}

	private int aiRolls = 0;

	private void AIturnR() {
		if (!game.gameOver() && ai.rollAgain(game)) {
			game.rollDice();
			aiRolls++;
			System.out.println("AI: ROLL #" + aiRolls);
			diePane.clearSelection();
			// System.out.println("Roll Again");
			if (game.detectFarkle()) {
				System.out.println("AI: FARKLE\nAI: END TURN\n");
				aiRolls = 0;
				farkle();
				return;
			} else {
				boolean[] selection = ai.makeSelection(game);
				game.setSelection(selection.clone());
				diePane.setSelection(selection.clone());
				System.out.println("AI: SELECTION SCORE = " + game.checkSelectionScore());
				refresh();

				PauseTransition pause = new PauseTransition(Duration.seconds(3));
				pause.setOnFinished(e -> {
					game.scoreSelection();
					diePane.clearSelection();
					refresh();
					AIturnR();
				});
				pause.play();
			}
		} else {
			System.out.println("AI: MOVE TO PASS TURN");
			boolean[] finalSelection = ai.selectAllUnselectedDie(game);
			game.setSelection(finalSelection.clone());
			diePane.setSelection(finalSelection.clone());
			int finalValue = game.checkSelectionScore();
			refresh();
			if (finalValue > 0) {
				PauseTransition pause = new PauseTransition(Duration.seconds(3));
				System.out.println("AI: FINAL SELECTION SCORE = " + finalValue);
				pause.setOnFinished(e -> {
					game.scoreSelection();
					diePane.clearSelection();
					refresh();
					aiRolls = 0;
					pass();
				});
				pause.play();
			} else {
				aiRolls = 0;
				pass();
			}

		}
	}
}